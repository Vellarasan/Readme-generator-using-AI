package io.vels.readme.generator.dtos;


import java.time.ZonedDateTime;

/**
 * Represents a Git user (author or committer).
 */
public record GitUser(
        String name,
        String email,
        ZonedDateTime date
) {
}

