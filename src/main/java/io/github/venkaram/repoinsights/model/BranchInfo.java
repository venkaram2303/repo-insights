package io.github.venkaram.repoinsights.model;

public record BranchInfo(
    String name,
    String latestCommitSha
) { }