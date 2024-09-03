package com.MTAPizza.Sympoll.pollmanagementservice.client;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.DeleteMultipleVotesRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.DeleteMultipleVotesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.DeleteExchange;

public interface VoteClient {
    @DeleteExchange("api/vote/delete-multiple")
    ResponseEntity<DeleteMultipleVotesResponse> deleteMultipleVotes(@RequestBody DeleteMultipleVotesRequest deleteMultipleVotesRequest);
}
