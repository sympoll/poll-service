package com.MTAPizza.Sympoll.pollmanagementservice.dto.vote;

import java.util.UUID;

public record VoteCreateRequest(
        UUID pollId,
        UUID userId,
        int votingItemId
) {
}
