package com.MTAPizza.Sympoll.pollmanagementservice.dto.vote;

import java.util.UUID;

public record VoteCastRequest(
        UUID pollId,
        UUID userId,
        int votingItemId
) {
}
