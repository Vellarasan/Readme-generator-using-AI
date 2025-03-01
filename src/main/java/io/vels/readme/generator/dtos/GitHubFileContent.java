package io.vels.readme.generator.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public record GitHubFileContent(
        String name,
        String path,
        String sha,
        String size,
        String url,
        @JsonProperty("html_url")
        String htmlUrl,
        @JsonProperty("git_url")
        String gitUrl,
        @JsonProperty("download_url")
        String downloadUrl,
        String type,
        String content) {
}