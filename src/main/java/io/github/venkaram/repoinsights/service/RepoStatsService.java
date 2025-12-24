package io.github.venkaram.repoinsights.service;

import io.github.venkaram.repoinsights.model.*;
import io.github.venkaram.repoinsights.provider.RepoProvider;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class RepoStatsService {

  private final RepoProvider repoProvider;

  public RepoStatsService(RepoProvider repoProvider) {
    this.repoProvider = repoProvider;
  }

  public RepoStatsResponse buildRepoStats(String key) {
    // 1. Fetch repositories
    List<RepositoryInfo> repositories = repoProvider.listRepositories(key);

    // 2. Aggregate repo -> branches -> latest commit
    List<RepoStat> repoStats = repositories.stream()
        .map(repo -> {
          List<BranchInfo> branches =
              repoProvider.listBranches(key, repo.name());

          List<BranchStat> branchStats = branches.stream()
              .map(branch -> {
                CommitInfo commit = repoProvider.getCommit(
                    key,
                    repo.name(),
                    branch.latestCommitSha()
                );

                return new BranchStat(
                    branch.name(),
                    commit.author(),
                    commit.commitDate(),
                    commit.sha()
                );
              })
              .toList();

          return new RepoStat(repo.name(), branchStats);
        })
        .toList();

    // 3. Build response
    return new RepoStatsResponse(
        Instant.now(),
        repositories.size(),
        repoStats
    );
  }
}
