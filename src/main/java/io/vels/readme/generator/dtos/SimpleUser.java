package io.vels.readme.generator.dtos;


import java.net.URI;
import java.time.ZonedDateTime;

/**
 * Represents a GitHub user with simplified information.
 */
public record SimpleUser(
        String name,
        String email,
        String login,
        long id,
        String nodeId,
        URI avatarUrl,
        String gravatarId,
        URI url,
        URI htmlUrl,
        URI followersUrl,
        String followingUrl,
        String gistsUrl,
        String starredUrl,
        URI subscriptionsUrl,
        URI organizationsUrl,
        URI reposUrl,
        String eventsUrl,
        URI receivedEventsUrl,
        String type,
        boolean siteAdmin,
        ZonedDateTime starredAt,
        String userViewType
) {
}
