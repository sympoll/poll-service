package com.MTAPizza.Sympoll.pollmanagementservice.client;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.choice.PollChoiceRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.choice.PollChoiceResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

import java.util.List;

public interface VoteClient {

    @PostExchange("/api/vote/user-choices")
    ResponseEntity<List<PollChoiceResponse>> getPollVotesByUser(@RequestBody PollChoiceRequest pollChoiceRequest);
}
