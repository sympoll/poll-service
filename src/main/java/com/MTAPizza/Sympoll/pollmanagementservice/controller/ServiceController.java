package com.MTAPizza.Sympoll.pollmanagementservice.controller;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.health.HealthResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.service.poll.PollService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/poll")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
public class ServiceController {
    private final PollService pollService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PollResponse createPoll(@RequestBody PollCreateRequest pollCreateRequest){
        log.info("Received request to create a poll");
        log.debug("Poll received to create: {}", pollCreateRequest);
        return pollService.createPoll(pollCreateRequest);
    }

    @GetMapping("/fetch-all")
    @ResponseStatus(HttpStatus.OK)
    public List<PollResponse> getAllPolls(){
        log.info("Received request to retrieve all polls");
        return pollService.getAllPolls();
    }

    @GetMapping("/fetch-by-poll-id")
    @ResponseStatus(HttpStatus.OK)
    public PollResponse getPollById(@RequestParam UUID pollId){
        log.info("Received request to get poll by id: {}", pollId);
        log.debug("Poll ID received to get: {}", pollId);
        return pollService.getPollById(pollId);
    }


    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public UUID deletePoll(@RequestParam UUID pollId){
        log.info("Received request to delete poll with id {}", pollId);
        log.debug("Poll ID received to delete: {}", pollId);
        return pollService.deletePoll(pollId);
    }

    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public HealthResponse HealthCheck(){
        log.info("Received health check request, returning OK");
        return new HealthResponse("Running", "Poll Management Service is up and running.");
    }
}
