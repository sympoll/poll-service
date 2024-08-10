package com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.count;

import java.util.UUID;

public record VoteCountRequest(
        UUID pollId,
        int votingItemId
) {
}
