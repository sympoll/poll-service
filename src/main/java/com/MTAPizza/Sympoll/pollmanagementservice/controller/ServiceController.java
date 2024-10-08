package com.MTAPizza.Sympoll.pollmanagementservice.controller;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.group.DeleteGroupPollsRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.group.DeleteGroupPollsResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.health.HealthResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.create.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.delete.PollDeleteRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.delete.PollDeleteResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.update.PollUpdateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.update.PollUpdateResponse;
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
     * @param userId Optional userId to fetch polls with user's choices.
     * @return List of all polls of the received group.
     */
    @GetMapping("/by-group-id")
    @ResponseStatus(HttpStatus.OK)
    public List<PollResponse> getPollsByGroupId(@RequestParam String groupId, @RequestParam(required = false) UUID userId){
        log.info("Received request to get all polls of group with ID: {}", groupId);
        return pollService.getPollsByGroupId(groupId, userId);
    }


    /**
     * Returns the given user's polls from all groups he is a member of.
     * @param userId User ID to fetch his polls
     * @return List of polls in descending order by creation date
     */
    @GetMapping("/all-user-polls")
    @ResponseStatus(HttpStatus.OK)
    public List<PollResponse> getAllUserPolls(@RequestParam UUID userId){
        log.info("Received request to get all polls of user with ID: {}", userId);
        return pollService.getAllUserPolls(userId);
    }

    /**
     * Fetch all polls of multiple groups.
     * @param groupIds List of group IDs to fetch their polls.
     * @param userId Optional userId to fetch polls with user's choices.
     * @return List of polls of the received groups, sorted by date posted, newest first.
     */
    @PostMapping("/by-multiple-group-ids")
    @ResponseStatus(HttpStatus.OK)
    public List<PollResponse> getPollsByMultipleGroupIds(@RequestBody List<String> groupIds, @RequestParam(required = false) UUID userId){
        log.info("Received request to get all polls of groups with IDs: {}", groupIds);
        return pollService.getPollsByMultipleGroupIds(groupIds, userId);
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
     * Update a poll's details.
     * @param pollUpdateRequest Details of the poll to update, and the new details.
     * @return ID of the updated poll and the newly saved details.
     */
    @PostMapping("/by-poll-id")
    @ResponseStatus(HttpStatus.OK)
    public PollUpdateResponse updatePoll(@RequestBody PollUpdateRequest pollUpdateRequest){
        log.info("Received request to update the poll with ID: {}", pollUpdateRequest.pollId());
        return pollService.updatePoll(pollUpdateRequest);
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

    /**
     * Deleting all polls related to the given group id.
     * @param deleteGroupPollsRequest The given group id.
     * @return A DTO with the removed poll ids.
     */
    @DeleteMapping("by-group-id")
    @ResponseStatus(HttpStatus.OK)
    public DeleteGroupPollsResponse deleteGroupPolls(@RequestBody DeleteGroupPollsRequest deleteGroupPollsRequest) {
        log.info("Received request to delete all group's polls");
        return pollService.deleteGroupPolls(deleteGroupPollsRequest);
    }
}
