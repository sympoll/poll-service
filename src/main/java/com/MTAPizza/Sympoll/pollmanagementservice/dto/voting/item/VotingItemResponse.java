package com.MTAPizza.Sympoll.pollmanagementservice.dto.voting.item;

public record VotingItemResponse(
        int votingItemId,
        String description,
        boolean chosen,
        long voteCount
) {
}
