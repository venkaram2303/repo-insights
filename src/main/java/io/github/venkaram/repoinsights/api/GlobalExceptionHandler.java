package io.github.venkaram.repoinsights.api;

import io.github.venkaram.repoinsights.exception.RepoInsightsException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(RepoInsightsException.class)
  public ResponseEntity<ApiError> handleRepoInsightsException(
      RepoInsightsException ex,
      HttpServletRequest request
  ) {
    return build(HttpStatus.BAD_GATEWAY, ex, request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(
      Exception ex,
      HttpServletRequest request
  ) {
    return build(HttpStatus.INTERNAL_SERVER_ERROR, ex, request);
  }

  private ResponseEntity<ApiError> build(
      HttpStatus status,
      Exception ex,
      HttpServletRequest request
  ) {
    ApiError error = new ApiError(
        Instant.now(),
        status.value(),
        status.getReasonPhrase(),
        ex.getMessage(),
        request.getRequestURI()
    );
    return ResponseEntity.status(status).body(error);
  }
}
