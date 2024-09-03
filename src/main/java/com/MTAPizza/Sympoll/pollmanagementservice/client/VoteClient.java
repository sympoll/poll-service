package com.MTAPizza.Sympoll.pollmanagementservice.client;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.DeleteMultipleVotesRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.DeleteMultipleVotesResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.choice.VotingItemsCheckedRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.choice.VotingItemsCheckedResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface VoteClient {
    @DeleteExchange("api/vote/delete-multiple")
    ResponseEntity<DeleteMultipleVotesResponse> deleteMultipleVotes(@RequestBody DeleteMultipleVotesRequest deleteMultipleVotesRequest);
    @PostExchange("/api/vote/user-choices")
    ResponseEntity<VotingItemsCheckedResponse> getPollVotesByUser(@RequestBody VotingItemsCheckedRequest pollChoiceRequest);
}
