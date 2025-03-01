package io.vels.readme.generator.dtos;


import java.net.URI;

/**
 * Represents the details of a commit.
 */
public record CommitDetails(
        URI url,
        GitUser author,
        GitUser committer,
        String message,
        int commentCount,
        Tree tree,
        Verification verification
) {
}