package io.github.venkaram.repoinsights.provider.bitbucket;

import io.github.venkaram.repoinsights.config.RepoProps;
import io.github.venkaram.repoinsights.exception.RepoInsightsException;
import io.github.venkaram.repoinsights.model.BranchInfo;
import io.github.venkaram.repoinsights.model.CommitInfo;
import io.github.venkaram.repoinsights.model.RepositoryInfo;
import io.github.venkaram.repoinsights.provider.RepoProvider;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class BitbucketRepoProvider implements RepoProvider {

  private final RestTemplate restTemplate;
  private final RepoProps repoProps;

  public BitbucketRepoProvider(RepoProps repoProps) {
    this.repoProps = repoProps;
    this.restTemplate = new RestTemplate();
  }

  @Override
  public List<RepositoryInfo> listRepositories(String workspace) {

    String url = repoProps.baseUrl()
        + "/repositories/" + workspace
        + "?q=project.key=\"" + repoProps.key() + "\""
        + "&pagelen=100";

    List<BitbucketRepo> repos = fetchAllPages(
        url,
        new ParameterizedTypeReference<>() {
        }
    );

    return repos.stream()
        .map(r -> new RepositoryInfo(r.slug(), r.name()))
        .toList();
  }

  @Override
  public List<BranchInfo> listBranches(String workspace, String repoSlug) {

    String url = repoProps.baseUrl()
        + "/repositories/" + workspace + "/" + repoSlug
        + "/refs/branches?pagelen=100";

    List<BitbucketBranch> branches = fetchAllPages(
        url,
        new ParameterizedTypeReference<>() {
        }
    );

    return branches.stream()
        .map(b -> new BranchInfo(b.name(), b.target().hash()))
        .toList();
  }

  @Override
  public CommitInfo getCommit(String workspace, String repoSlug, String commitId) {
    String url = repoProps.baseUrl()
        + "/repositories/" + workspace + "/" + repoSlug
        + "/commit/" + commitId;

    try {
      BitbucketCommit response =
          restTemplate.exchange(url, HttpMethod.GET,
                  new HttpEntity<>(authHeaders()),
                  BitbucketCommit.class)
              .getBody();

      return new CommitInfo(
          response.hash(),
          response.author().raw(),
          ZonedDateTime.parse(response.date())
      );
    } catch (Exception ex) {
      throw new RepoInsightsException(
          "Failed to fetch commit from Bitbucket. Repo: " + repoSlug + ", commit: " + commitId,
          ex
      );
    }
  }

  private HttpHeaders authHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(repoProps.accessToken());
    headers.setAccept(List.of(MediaType.APPLICATION_JSON));
    return headers;
  }

  private <T> List<T> fetchAllPages(
      String url,
      ParameterizedTypeReference<BitbucketPage<T>> type
  ) {
    List<T> results = new ArrayList<>();

    try {
      while (url != null) {
        ResponseEntity<BitbucketPage<T>> response =
            restTemplate.exchange(
                url,
                HttpMethod.GET,
                new HttpEntity<>(authHeaders()),
                type
            );

        BitbucketPage<T> body = response.getBody();
        if (body == null) {
          break;
        }

        results.addAll(body.values());
        url = body.next();
      }
    } catch (Exception ex) {
      throw new RepoInsightsException(
          "Failed to fetch data from Bitbucket API",
          ex
      );
    }

    return results;
  }

}
