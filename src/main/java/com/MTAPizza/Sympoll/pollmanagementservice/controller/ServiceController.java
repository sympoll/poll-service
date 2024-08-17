package com.MTAPizza.Sympoll.pollmanagementservice.controller;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.health.HealthResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.delete.PollDeleteRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.delete.PollDeleteResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.count.VoteCountRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.count.VoteCountResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.service.poll.PollService;
import com.MTAPizza.Sympoll.pollmanagementservice.service.voting.item.VotingItemService;
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
    private final VotingItemService votingItemService;

    /**
     * Create a new poll to save in the database.
     * @param pollCreateRequest Information of the poll to be created.
     * @return The created poll, as it is saved in the database.
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PollResponse createPoll(@RequestBody PollCreateRequest pollCreateRequest){
        log.info("Received request to create a poll");
        log.info("Poll received to create: {}", pollCreateRequest);
        return pollService.createPoll(pollCreateRequest);
    }

    /**
     * Fetch all the polls currently saved in the database.
     * @return List of polls.
     */
    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<PollResponse> getAllPolls(){
        log.info("Received request to retrieve all polls");
        return pollService.getAllPolls();
    }

    /**
     * Fetch a poll from the database, by its ID.
     * @param pollId ID of the poll to fetch.
     * @return The poll requested.
     */
    @GetMapping("/by-poll-id")
    @ResponseStatus(HttpStatus.OK)
    public PollResponse getPollById(@RequestParam UUID pollId){
        log.info("Received request to get poll by ID: {}", pollId);
        return pollService.getPollById(pollId);
    }

    /**
     * Fetch all polls of a specific group.
     * @param groupId Group ID to fetch all its polls.
     * @return List of all polls of the received group.
     */
    @GetMapping("/by-group-id")
    @ResponseStatus(HttpStatus.OK)
    public List<PollResponse> getPollsByGroupId(@RequestParam String groupId){
        log.info("Received request to get all polls of group with ID: {}", groupId);
        return pollService.getPollsByGroupId(groupId);
    }

    /**
     * Fetch all polls of multiple groups.
     * @param groupIds List of group IDs to fetch their polls.
     * @return List of polls of the received groups, sorted by date posted, newest first.
     */
    @PostMapping("/by-multiple-group-ids")
    @ResponseStatus(HttpStatus.OK)
    public List<PollResponse> getPollsByMultipleGroupIds(@RequestBody List<String> groupIds){
        log.info("Received request to get all polls of groups with IDs: {}", groupIds);
        return pollService.getPollsByMultipleGroupIds(groupIds);
    }

    /**
     * Delete a poll from the database, by its ID.
     * @param pollDeleteRequest ID of the Poll to delete and the ID of the user.
     * @return The ID of the poll that was deleted.
     */
    @DeleteMapping("/by-poll-id")
    @ResponseStatus(HttpStatus.OK)
    public PollDeleteResponse deletePoll(@RequestBody PollDeleteRequest pollDeleteRequest){
        log.info("Received request to delete poll with ID {}", pollDeleteRequest.pollId());
        log.debug("Poll ID received to delete: {}", pollDeleteRequest.pollId());
        return pollService.deletePoll(pollDeleteRequest);
    }

    /**
     * Health check of the Poll Management Service.
     * @return JSON with info on the service's status.
     */
    @GetMapping("/health")
    @ResponseStatus(HttpStatus.OK)
    public HealthResponse HealthCheck(){
        log.info("Received health check request, returning OK");
        return new HealthResponse("Running", "Poll Management Service is up and running.");
    }

    /**
     * Update a specific vote in the database.
     * @param voteRequest Information of the vote to be created.
     * @return The created vote for the Voting service.
     */
    @PutMapping("/vote")
    @ResponseStatus(HttpStatus.OK)
    public VoteResponse updateVotingItem(@RequestBody VoteRequest voteRequest) {
        log.info("Received request to update voting item");
        return votingItemService.updateVotingItem(voteRequest);
    }

    /**
     * Retrieve votes count of a specific vote.
     * @param voteCountRequest Voting item ID of requested vote.
     * @return Count of votes for the requested vote.
     */
    @GetMapping("/vote")
    @ResponseStatus(HttpStatus.OK)
    public VoteCountResponse getVoteCount(@RequestBody VoteCountRequest voteCountRequest) {
        log.info("Received request to retrieve vote count");
        return votingItemService.getVoteCount(voteCountRequest);
    }
}
