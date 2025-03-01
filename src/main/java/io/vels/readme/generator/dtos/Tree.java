package io.vels.readme.generator.dtos;


import java.net.URI;

/**
 * Represents a tree in a Git repository.
 */
public record Tree(
        String sha,
        URI url
) {
}
