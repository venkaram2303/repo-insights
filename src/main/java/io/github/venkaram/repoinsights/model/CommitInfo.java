package io.github.venkaram.repoinsights.model;

import java.time.ZonedDateTime;

public record CommitInfo(
    String sha,
    String author,
    ZonedDateTime commitDate
) { }
