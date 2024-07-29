package com.MTAPizza.Sympoll.pollmanagementservice.controller;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.health.HealthResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.model.poll.Poll;
import com.MTAPizza.Sympoll.pollmanagementservice.service.poll.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/poll")
@RequiredArgsConstructor
public class ServiceController {
    private final PollService pollService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PollResponse createPoll(@RequestBody PollCreateRequest pollCreateRequest){
        return pollService.createPoll(pollCreateRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PollResponse> getAllPolls(){
        return pollService.getAllPolls();
    }

    @GetMapping("/id")
    @ResponseStatus(HttpStatus.OK)
    public PollResponse getPollById(@RequestParam UUID pollId){
        return pollService.getPollById(pollId);
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public UUID deletePoll(@RequestParam UUID pollId){
        return pollService.deletePoll(pollId);
    }

    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public HealthResponse HealthCheck(){
        return new HealthResponse("Running", "Poll Management Service is up and running.");
    }
}
