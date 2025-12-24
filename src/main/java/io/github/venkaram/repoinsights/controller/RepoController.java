package io.github.venkaram.repoinsights.controller;

import io.github.venkaram.repoinsights.model.BranchInfo;
import io.github.venkaram.repoinsights.model.CommitInfo;
import io.github.venkaram.repoinsights.model.RepositoryInfo;
import io.github.venkaram.repoinsights.provider.RepoProvider;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/org/{org}")
public class RepoController {

  private final RepoProvider repoProvider;

  public RepoController(RepoProvider repoProvider) {
    this.repoProvider = repoProvider;
  }

  /**
   * GET /org/{org}/repos
   */
  @GetMapping("/repos")
  public List<RepositoryInfo> listRepositories(@PathVariable String org) {
    return repoProvider.listRepositories(org);
  }

  /**
   * GET /org/{org}/repos/{repoSlug}
   */
  @GetMapping("/repos/{repoSlug}")
  public List<BranchInfo> listBranches(
      @PathVariable String org,
      @PathVariable String repoSlug
  ) {
    return repoProvider.listBranches(org, repoSlug);
  }

  /**
   * GET /org/{org}/repos/{repoSlug}/commit/{commitId}
   */
  @GetMapping("/repos/{repoSlug}/commit/{commitId}")
  public CommitInfo getCommit(
      @PathVariable String org,
      @PathVariable String repoSlug,
      @PathVariable String commitId
  ) {
    return repoProvider.getCommit(org, repoSlug, commitId);
  }
}
