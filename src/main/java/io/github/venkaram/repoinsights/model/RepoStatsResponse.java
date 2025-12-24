package io.github.venkaram.repoinsights.model;

import java.time.Instant;
import java.util.List;

public record RepoStatsResponse(
    Instant createdDatetime,
    int repoCount,
    List<RepoStat> repositories
) { }
