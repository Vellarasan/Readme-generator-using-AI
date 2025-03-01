package io.vels.readme.generator.dtos;


/**
 * Represents statistics for a commit.
 */
public record Stats(
        int additions,
        int deletions,
        int total
) {
}