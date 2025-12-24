package io.github.venkaram.repoinsights.service;

import io.github.venkaram.repoinsights.model.*;
import io.github.venkaram.repoinsights.provider.RepoProvider;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZonedDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RepoStatsServiceTest {

  @Mock
  private RepoProvider repoProvider;

  @InjectMocks
  private RepoStatsService repoStatsService;

  @Test
  void shouldAggregateRepoStatsCorrectly() {
    // given
    String org = "test-org";

    RepositoryInfo repo = new RepositoryInfo("repo-1", "Test repo");
    BranchInfo branch = new BranchInfo("main", "sha-123");

    CommitInfo commit = new CommitInfo(
        "sha-123",
        "Venkat",
        ZonedDateTime.parse("2025-01-01T10:00:00Z")
    );

    when(repoProvider.listRepositories(org))
        .thenReturn(List.of(repo));

    when(repoProvider.listBranches(org, "repo-1"))
        .thenReturn(List.of(branch));

    when(repoProvider.getCommit(org, "repo-1", "sha-123"))
        .thenReturn(commit);

    // when
    RepoStatsResponse response = repoStatsService.buildRepoStats(org);

    // then
    assertThat(response).isNotNull();
    assertThat(response.repoCount()).isEqualTo(1);
    assertThat(response.repositories()).hasSize(1);

    RepoStat repoStat = response.repositories().get(0);
    assertThat(repoStat.repoName()).isEqualTo("repo-1");
    assertThat(repoStat.branches()).hasSize(1);

    BranchStat branchStat = repoStat.branches().get(0);
    assertThat(branchStat.branchName()).isEqualTo("main");
    assertThat(branchStat.lastCommitBy()).isEqualTo("Venkat");
    assertThat(branchStat.lastCommitSha()).isEqualTo("sha-123");
  }
}
