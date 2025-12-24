package io.github.venkaram.repoinsights.model;

import java.util.List;

public record RepoStat(
    String repoName,
    List<BranchStat> branches
) { }
