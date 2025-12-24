package io.github.venkaram.repoinsights.provider.github;

import io.github.venkaram.repoinsights.config.RepoProps;
import io.github.venkaram.repoinsights.exception.RepoInsightsException;
import io.github.venkaram.repoinsights.model.BranchInfo;
import io.github.venkaram.repoinsights.model.CommitInfo;
import io.github.venkaram.repoinsights.model.RepositoryInfo;
import io.github.venkaram.repoinsights.provider.RepoProvider;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

public class GitHubRepoProvider implements RepoProvider {

  private final RestTemplate restTemplate;
  private final RepoProps repoProps;

  public GitHubRepoProvider(RepoProps repoProps) {
    this.repoProps = repoProps;
    this.restTemplate = new RestTemplate();
  }

  @Override
  public List<RepositoryInfo> listRepositories(String org) {
    String url = repoProps.baseUrl() + "/orgs/" + org + "/repos";

    System.out.println("Fetching repositories from URL: " + url);

    List<GitHubRepo> repos =
        fetchAllPages(url, GitHubRepo[].class);

    return repos.stream()
        .map(r -> new RepositoryInfo(r.name(), r.description()))
        .toList();
  }

  @Override
  public List<BranchInfo> listBranches(String org, String repoSlug) {
    String url = repoProps.baseUrl()
        + "/repos/" + org + "/" + repoSlug + "/branches";

    List<GitHubBranch> branches =
        fetchAllPages(url, GitHubBranch[].class);

    return branches.stream()
        .map(b -> new BranchInfo(b.name(), b.commit().sha()))
        .toList();
  }

  @Override
  public CommitInfo getCommit(String org, String repoSlug, String commitId) {
    String url = repoProps.baseUrl() + "/repos/" + org + "/" + repoSlug + "/commits/" + commitId;

    try {
      HttpEntity<Void> request = new HttpEntity<>(authHeaders());
      GitHubCommitResponse response =
          restTemplate.exchange(url, HttpMethod.GET, request, GitHubCommitResponse.class)
              .getBody();

      return new CommitInfo(
          response.sha(),
          response.commit().author().name(),
          ZonedDateTime.parse(response.commit().author().date())
      );
    } catch (Exception ex) {
      throw new RepoInsightsException(
          "Failed to fetch commit from GitHub. Repo: " + repoSlug + ", commit: " + commitId,
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
      String baseUrl,
      Class<T[]> responseType
  ) {
    List<T> results = new ArrayList<>();
    int page = 1;

    try {
      while (true) {
        String url = baseUrl + "?per_page=100&page=" + page;

        ResponseEntity<T[]> response = restTemplate.exchange(
            url,
            HttpMethod.GET,
            new HttpEntity<>(authHeaders()),
            responseType
        );

        T[] body = response.getBody();
        if (body == null || body.length == 0) {
          break;
        }

        results.addAll(List.of(body));

        if (body.length < 100) {
          break; // last page
        }

        page++;
      }
    } catch (Exception ex) {
      throw new RepoInsightsException(
          "Failed to fetch data from GitHub API",
          ex
      );
    }

    return results;
  }

}
