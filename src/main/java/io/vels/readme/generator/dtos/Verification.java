package io.vels.readme.generator.dtos;


import java.time.ZonedDateTime;

/**
 * Represents verification information for a commit.
 */
public record Verification(
        boolean verified,
        String reason,
        String payload,
        String signature,
        ZonedDateTime verifiedAt
) {
}
