package io.github.venkaram.repoinsights.config;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Validated
@ConfigurationProperties(prefix = "repo-insights.scm")
public record RepoProps(
    @NotNull RepoType type,
    @NotBlank String baseUrl,
    @NotBlank String accessToken,
    @NotBlank String key
) {
  public enum RepoType {
    github, bitbucket
  }
}
