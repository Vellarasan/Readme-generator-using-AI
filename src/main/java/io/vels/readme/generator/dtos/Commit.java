package io.vels.readme.generator.dtos;

import java.net.URI;
import java.util.List;

/**
 * Represents a GitHub commit.
 */
public record Commit(
        URI url,
        String sha,
        String nodeId,
        URI htmlUrl,
        URI commentsUrl,
        CommitDetails commit,
        SimpleUser author,
        SimpleUser committer,
        List<Parent> parents,
        Stats stats,
        List<DiffEntry> files
) {
}