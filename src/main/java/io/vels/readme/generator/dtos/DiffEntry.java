package io.vels.readme.generator.dtos;


import java.net.URI;

/**
 * Represents a diff entry in a commit.
 */
public record DiffEntry(
        String sha,
        String filename,
        String status,
        int additions,
        int deletions,
        int changes,
        URI blobUrl,
        URI rawUrl,
        URI contentsUrl,
        String patch,
        String previousFilename
) {
}