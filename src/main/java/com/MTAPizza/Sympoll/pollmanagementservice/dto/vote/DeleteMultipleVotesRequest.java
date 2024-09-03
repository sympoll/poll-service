package com.MTAPizza.Sympoll.pollmanagementservice.dto.vote;

import java.util.List;

public record DeleteMultipleVotesRequest(List<Integer> votingItemIds) {
}
