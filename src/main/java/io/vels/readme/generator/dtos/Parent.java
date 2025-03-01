package io.vels.readme.generator.dtos;


import java.net.URI;

/**
 * Represents a parent commit.
 */
public record Parent(
        String sha,
        URI url,
        URI htmlUrl
) {
}
