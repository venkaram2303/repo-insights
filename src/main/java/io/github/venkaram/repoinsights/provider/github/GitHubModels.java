package io.github.venkaram.repoinsights.provider.github;

/**
 * Internal DTOs representing GitHub REST API responses.
 * These models are NOT part of the public API and must not
 * leak outside the GitHub provider package.
 */
record GitHubRepo(
    String name,
    String description
) { }

record GitHubBranch(
    String name,
    GitHubCommitRef commit
) { }

record GitHubCommitRef(
    String sha
) { }

record GitHubCommitResponse(
    String sha,
    GitHubCommit commit
) { }

record GitHubCommit(
    GitHubAuthor author
) { }

record GitHubAuthor(
    String name,
    String date
) { }
