package com.MTAPizza.Sympoll.pollmanagementservice.dto.vote;

import java.time.LocalDateTime;
import java.util.UUID;

public record VoteResponse(
        UUID userId,
        int votingItemId,
        LocalDateTime voteDateTime
) {
}
