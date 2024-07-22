package com.MTAPizza.Sympoll.pollmanagementservice.controller;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.model.poll.Poll;
import com.MTAPizza.Sympoll.pollmanagementservice.service.poll.PollService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/poll")
@RequiredArgsConstructor
public class ServiceController {
    private final PollService pollService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Poll createPoll(@RequestBody PollCreateRequest pollCreateRequest){
        return pollService.createPoll(pollCreateRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<PollResponse> getAllPolls(){
        return pollService.getAllPolls();
    }
}
