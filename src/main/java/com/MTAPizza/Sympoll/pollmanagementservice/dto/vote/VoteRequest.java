package com.MTAPizza.Sympoll.pollmanagementservice.dto.vote;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.action.VoteAction;

public record VoteRequest(
        int votingItemId,
        String action
) {
}
