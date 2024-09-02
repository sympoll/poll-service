package com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.choice;

import java.util.List;
import java.util.UUID;

public record VotingItemsCheckedRequest(List<Integer> votingItemIds, UUID userId){
}
