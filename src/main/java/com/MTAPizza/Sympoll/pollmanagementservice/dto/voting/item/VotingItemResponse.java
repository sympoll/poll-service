package com.MTAPizza.Sympoll.pollmanagementservice.dto.voting.item;

public record VotingItemResponse(
        int votingItemId,
        int votingItemOrdinal,
        String description,
        long voteCount
) {
}
