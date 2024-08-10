package com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.count;

import java.util.UUID;

public record VoteCountResponse(
        UUID pollId,
        int votingItemId,
        int voteCount
) {
}
