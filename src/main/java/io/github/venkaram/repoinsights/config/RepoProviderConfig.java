package io.github.venkaram.repoinsights.config;

import io.github.venkaram.repoinsights.provider.RepoProvider;
import io.github.venkaram.repoinsights.provider.bitbucket.BitbucketRepoProvider;
import io.github.venkaram.repoinsights.provider.github.GitHubRepoProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepoProviderConfig {

  @Bean
  public RepoProvider repoProvider(RepoProps repoProps) {
    return switch (repoProps.type()) {
      case github -> new GitHubRepoProvider(repoProps);
      case bitbucket -> new BitbucketRepoProvider(repoProps);
    };
  }
}