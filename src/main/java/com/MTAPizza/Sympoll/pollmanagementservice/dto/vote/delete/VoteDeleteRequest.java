package com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.delete;

import java.util.UUID;

public record VoteDeleteRequest(
        UUID voteId,
        int votingItemId
) {
}
