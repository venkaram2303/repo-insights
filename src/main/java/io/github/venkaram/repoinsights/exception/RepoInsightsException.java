package io.github.venkaram.repoinsights.exception;

public class RepoInsightsException extends RuntimeException {

  public RepoInsightsException(String message) {
    super(message);
  }

  public RepoInsightsException(String message, Throwable cause) {
    super(message, cause);
  }
}
