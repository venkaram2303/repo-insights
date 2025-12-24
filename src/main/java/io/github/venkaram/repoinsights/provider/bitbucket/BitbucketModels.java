package io.github.venkaram.repoinsights.provider.bitbucket;

import java.util.List;

record BitbucketRepoResponse(List<BitbucketRepo> values) { }

record BitbucketRepo(
    String name,
    String slug
) { }

record BitbucketBranchResponse(List<BitbucketBranch> values) { }

record BitbucketBranch(
    String name,
    BitbucketCommitRef target
) { }

record BitbucketCommitRef(
    String hash
) { }

record BitbucketCommit(
    String hash,
    String date,
    BitbucketAuthor author
) { }

record BitbucketAuthor(
    String raw
) { }

record BitbucketPage<T>(
    List<T> values,
    String next
) { }
