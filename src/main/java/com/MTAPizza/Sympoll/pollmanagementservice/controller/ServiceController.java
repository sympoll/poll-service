package com.MTAPizza.Sympoll.pollmanagementservice.controller;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.health.HealthResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteCastRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.delete.VoteDeleteRequest;
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
     * @param pollId ID of the Poll to delete.
     * @return The ID of the poll that was deleted.
     */
    @DeleteMapping("/by-poll-id")
    @ResponseStatus(HttpStatus.OK)
    public UUID deletePoll(@RequestParam UUID pollId){
        log.info("Received request to delete poll with ID {}", pollId);
        log.info("Poll ID received to delete: {}", pollId);
        return pollService.deletePoll(pollId);
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
     * @param createVoteRequest Information of the vote to be created.
     * @return The created vote for the Voting service.
     */
    @PostMapping("/vote")
    @ResponseStatus(HttpStatus.OK)
    public VoteResponse castVote(@RequestBody VoteCastRequest createVoteRequest) {
        log.info("Received request to cast vote");
        return votingItemService.castVote(createVoteRequest);
    }

    /**
     * Retrieve votes count of a specific vote.
     * @param votingItemId Voting item ID of requested vote.
     * @return Count of votes for the requested vote.
     */
    @GetMapping("/vote")
    @ResponseStatus(HttpStatus.OK)
    public Integer getVoteCount(@RequestParam int votingItemId) {
        log.info("Received request to retrieve vote count");
        return votingItemService.getVoteCount(votingItemId);
    }

    /**
     * Update vote count decrement in the database.
     * @param voteDeleteRequest Information of the vote to be deleted.
     * @return The UUID of the vote that was deleted.
     */
    @DeleteMapping("/vote")
    @ResponseStatus(HttpStatus.OK)
    public UUID deleteVote(@RequestBody VoteDeleteRequest voteDeleteRequest) {
        log.info("Received request to delete vote");
        log.info("Vote ID received to be deleted: {}", voteDeleteRequest.voteId());
        return votingItemService.deleteVote(voteDeleteRequest);
    }
}
