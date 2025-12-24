# Repo Insights

A lightweight, deployable service to analyze repository health and activity across GitHub and Bitbucket using a single, unified API.

Repo Insights aggregates repository, branch, and commit metadata at an organization or project level and exposes it via simple REST endpoints.
It is designed to be SCM-agnostic, Kubernetes-friendly, and easy to extend.

---

## Features

- Supports GitHub and Bitbucket via configuration
- Unified REST APIs across SCMs
- Aggregated repository → branch → latest commit view
- Handles SCM pagination internally
- Simple, consistent error responses
- Deployable as a standalone service on Kubernetes
- Clean provider abstraction for adding new SCMs

---

## Architecture Overview

Controller → Service → RepoProvider (GitHub / Bitbucket)
→ SCM REST APIs

- Controllers expose REST endpoints
- Service layer performs aggregation
- RepoProvider abstracts SCM-specific logic
- SCM-specific models and pagination are fully encapsulated

---

## Getting Started

### Prerequisites

- Java 17+
- Maven
- GitHub or Bitbucket access token

---

## Configuration

Repo Insights is configured entirely via environment variables.

Common configuration:

    SERVER_PORT        Port to run the service (default: 8080)
    REPO_TYPE          github or bitbucket
    REPO_URL           Base SCM API URL
    REPO_ACCESS_TOKEN  SCM access token
    REPO_KEY           GitHub org or Bitbucket project key

---

### GitHub example

    export REPO_TYPE=github
    export REPO_URL=https://api.github.com
    export REPO_ACCESS_TOKEN=ghp_xxxxx
    export REPO_KEY=my-github-org

---

### Bitbucket example

    export REPO_TYPE=bitbucket
    export REPO_URL=https://api.bitbucket.org/2.0
    export REPO_ACCESS_TOKEN=bbtoken_xxxxx
    export REPO_KEY=MY_PROJECT_KEY

---

## Running Locally

Using Maven:

    mvn spring-boot:run

Or using the packaged JAR:

    java -jar target/repo-insights.jar

---

## API Endpoints

### List repositories

    GET /org/{org}/repos

Response:

    [
      {
        "name": "repo-1",
        "description": "Sample repository"
      }
    ]

---

### List branches for a repository

    GET /org/{org}/repos/{repoSlug}

Response:

    [
      {
        "name": "main",
        "latestCommitSha": "abc123"
      }
    ]

---

### Get commit details

    GET /org/{org}/repos/{repoSlug}/commit/{commitId}

Response:

    {
      "sha": "abc123",
      "author": "Venkat",
      "commitDate": "2025-01-01T10:00:00Z"
    }

---

### Aggregated repository stats

    GET /repo-stats/{org}

Response:

    {
      "createdDatetime": "2025-01-01T10:15:30Z",
      "repoCount": 2,
      "repositories": [
        {
          "repoName": "repo-1",
          "branches": [
            {
              "branchName": "main",
              "lastCommitBy": "Venkat",
              "lastCommitDate": "2025-01-01T10:12:30Z",
              "lastCommitSha": "abc123"
            }
          ]
        }
      ]
    }

---

## Error Handling

All errors are returned in a consistent format:

    {
      "timestamp": "2025-01-01T10:20:00Z",
      "status": 502,
      "error": "Bad Gateway",
      "message": "Failed to fetch data from GitHub",
      "path": "/repo-stats/my-org"
    }

- SCM failures return 502 Bad Gateway
- Internal errors return 500 Internal Server Error

---

## Testing

Run unit tests using:

    mvn test

Tests focus on aggregation logic and provider abstraction correctness.

---

## Container Image (Jib)

Repo Insights supports container image builds using **Jib**, without requiring a Dockerfile.

### Build & Push Image

The container image can be built and pushed directly using Maven:

    mvn clean compile jib:build

By default, the image is pushed to Docker Hub with:
- a **timestamp-based tag** (yyMMdd.HHmm)
- the `latest` tag

Example image names:

    docker.io/venkaram2303/repo-insights:250124.2145
    docker.io/venkaram2303/repo-insights:latest

### Base Image

The service uses the following base image:

    eclipse-temurin:17-jre

### Running the Image

    docker run -p 8080:8080 \
      -e REPO_TYPE=github \
      -e REPO_URL=https://api.github.com \
      -e REPO_ACCESS_TOKEN=<token> \
      -e REPO_KEY=<org-or-project> \
      venkaram2303/repo-insights:latest

No Dockerfile is required. Container builds are fully managed via Jib configuration.


## Contributing

Contributions are welcome.

Guidelines:
- Keep providers SCM-specific
- Avoid leaking SCM models outside providers
- Prefer clarity over clever abstractions
- Add tests for any new behavior

---

## License

MIT License
