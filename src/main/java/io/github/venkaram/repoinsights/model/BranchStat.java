package io.github.venkaram.repoinsights.model;

import java.time.ZonedDateTime;

public record BranchStat(
    String branchName,
    String lastCommitBy,
    ZonedDateTime lastCommitDate,
    String lastCommitSha
) { }
