package io.github.venkaram.repoinsights.controller;

import io.github.venkaram.repoinsights.model.RepoStatsResponse;
import io.github.venkaram.repoinsights.service.RepoStatsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/repo-stats")
public class RepoStatsController {

  private final RepoStatsService repoStatsService;

  public RepoStatsController(RepoStatsService repoStatsService) {
    this.repoStatsService = repoStatsService;
  }

  @GetMapping("/{org}")
  public RepoStatsResponse getRepoStats(@PathVariable String org) {
    return repoStatsService.buildRepoStats(org);
  }
}
