package io.vels.readme.generator.service;

import io.vels.readme.generator.config.GitHubConfig;
import io.vels.readme.generator.dtos.Commit;
import io.vels.readme.generator.interceptors.LoggingInterceptor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Collections;
import java.util.List;

/**
 * Service class for interacting with GitHub API and downloading repository contents.
 * This service allows downloading specified file types from a GitHub repository,
 * with support for both include and exclude patterns.
 */
@Service
public class GitHubService {

    public static final String GITHUB_BASE_URL = "https://api.github.com";
    private final RestClient restClient;
    private final GitHubConfig config;

    /**
     * Constructs a new GithubService with the specified dependencies.
     *
     * @param builder The RestClient.Builder to use for creating the RestClient.
     * @param config  The GitHub configuration properties.
     */
    public GitHubService(RestClient.Builder builder,
                         GitHubConfig config) {
        this.config = config;
        this.restClient = builder
                .baseUrl(GITHUB_BASE_URL)
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2022-11-28")
                .defaultHeader("Authorization", "Bearer " + config.token())  // Remove the colon after Bearer
                .requestInterceptor(new LoggingInterceptor())
                .build();
    }

    /**
     * Retrieves the content of the Commit ID
     *
     * @param owner    The owner of the repository.
     * @param repo     The name of the repository.
     * @param commitId The commit ID
     * @return A {@link Commit} objects representing the contents of the specific commit.
     */
    public Commit getCommitContents(String owner, String repo, String commitId) {

        return restClient.get()
                .uri("/repos/{owner}/{repo}/commits/{commitId}", owner, repo, commitId)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });
    }

    public List<String> getCommitMessages(String owner, String repo) {

        List<Commit> commits = restClient.get()
                .uri("/repos/{owner}/{repo}/commits", owner, repo)
                .retrieve()
                .body(new ParameterizedTypeReference<>() {
                });

        // Return the list of commit message alone
        return commits != null ? commits.stream()
                .map(eachCommit -> eachCommit.commit().message())
                .toList() : Collections.emptyList();
    }
}