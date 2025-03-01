package io.vels.readme.generator.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(value = "github")
public record GitHubConfig(String token) {

    public GitHubConfig {
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("GitHub token must not be null or blank");
        }
    }
}
