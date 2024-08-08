package com.MTAPizza.Sympoll.pollmanagementservice.dto.vote;

import java.util.UUID;

public record CreateVoteRequest(
        UUID pollId,
        UUID userId,
        int votingItemId
) {
}
