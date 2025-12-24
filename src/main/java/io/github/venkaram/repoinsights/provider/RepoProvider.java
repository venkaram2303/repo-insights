package io.github.venkaram.repoinsights.provider;

import io.github.venkaram.repoinsights.model.BranchInfo;
import io.github.venkaram.repoinsights.model.CommitInfo;
import io.github.venkaram.repoinsights.model.RepositoryInfo;

import java.util.List;

public interface RepoProvider {

  List<RepositoryInfo> listRepositories(String key);

  List<BranchInfo> listBranches(String key, String repoSlug);

  CommitInfo getCommit(String key, String repoSlug, String commitId);
}
