package com.MTAPizza.Sympoll.pollmanagementservice.validator;

import com.MTAPizza.Sympoll.pollmanagementservice.client.GroupClient;
import com.MTAPizza.Sympoll.pollmanagementservice.client.UserClient;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.delete.PollDeleteRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.group.GroupIdExistsResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.validator.user.UserIdExistsResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.action.VoteAction;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.count.VoteCountRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.exception.access.denied.AccessDeniedException;
import com.MTAPizza.Sympoll.pollmanagementservice.exception.not.found.ResourceNotFoundException;
import com.MTAPizza.Sympoll.pollmanagementservice.repository.poll.PollRepository;
import com.MTAPizza.Sympoll.pollmanagementservice.repository.voting.item.VotingItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

/**
 * This class handles the validation of data received from the client.
 * In case of an error, the method will throw an exception to be handled by the 'PollExceptionHandler'.
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class Validator {
    private final PollRepository pollRepository;
    private final VotingItemRepository votingItemRepository;
    private final UserClient userClient;
    private final GroupClient groupClient;

    public void validateNewPoll(PollCreateRequest poll){
        validateAllowedVotingItems(poll.votingItems().size(), poll);
        validateDeadline(LocalDateTime.now(), poll);
        validateGroupIdExist(poll.groupId());
        validateUserIdExist(poll.creatorId());
        validateVotingItemsStringList(poll);
    }

    public void validateGetPollByIdRequest(UUID pollId) {
        validatePollIdExist(pollId);
    }

    public void validateGetPollsByGroupIdRequest(String groupId) {
        validateGroupIdExist(groupId);
    }

    public void validateGetPollsByMultipleGroupIdsRequest(List<String> groupIds) {
        for (String groupId : groupIds) {
            validateGroupIdExist(groupId);
        }
    }

    public void validateDeletePollRequest(PollDeleteRequest pollDeleteRequest) {
        validateUserHasPermissions(pollDeleteRequest.userId(), pollDeleteRequest.groupId(), pollDeleteRequest.pollId());
        validatePollIdExist(pollDeleteRequest.pollId());
    }

    private void validateAllowedVotingItems(int nofVotingItems, PollCreateRequest poll) {
        if (poll.nofAnswersAllowed() > nofVotingItems) {
            log.info("User {} tried to create a poll but an number of answers allowed was given.", poll.creatorId());
            throw new IllegalArgumentException("Number of allowed answers is greater than number of available answers");
        }
    }

    private void validateDeadline(LocalDateTime timeCreated, PollCreateRequest poll){
        // Convert deadline string time stamp to LocalDateTime object
        Instant instant = Instant.parse(poll.deadline());
        LocalDateTime deadline = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (deadline.isBefore(timeCreated)) {
            log.info("User {} tried to create a poll but an invalid deadline was given.", poll.creatorId());
            throw new IllegalArgumentException("A deadline cannot be earlier than the time a poll was created");
        }
    }

    private void validateUserIdExist(UUID userId) {
        log.info("Sending validating user id request to user service");
        ResponseEntity<UserIdExistsResponse> response = userClient.checkUserIdExists(userId);

        if (response.getStatusCode().is2xxSuccessful()) {
            if(!response.getBody().isExists()){
                log.info("User {} does not exists.", userId);
                throw new ResourceNotFoundException("User " + userId + " does not exist");
            }
        }
    }

    private void validateVotingItemsStringList(PollCreateRequest poll) {
        if(poll.votingItems() == null || poll.votingItems().isEmpty()){
            log.info("User {} tried to create a poll without voting item list", poll.creatorId());
            throw new  IllegalArgumentException("A list of voting items cannot be empty");
        }
    }

    private void validatePollIdExist(UUID pollId) {
        if(!pollRepository.existsById(pollId)) {
            log.info("User tried to interact with a poll that does not exist.");
            throw new ResourceNotFoundException("Poll with id " + pollId + " does not exist");
        }
    }

    private void validateGroupIdExist(String groupId) {
        log.info("Sending validating group id request to group service");
        ResponseEntity<GroupIdExistsResponse> response = groupClient.checkGroupIdExists(groupId);

        if (response.getStatusCode().is2xxSuccessful()) {
            if(!response.getBody().isExists()){
                log.info("Group {} does not exists.", groupId);
                throw new ResourceNotFoundException("Group " + groupId + " does not exist");
            }
        }
    }

    private void validateUserHasPermissions(UUID userId, String groupId, UUID pollId) {
        if(!isUserCreatedThePoll(userId,pollId) && !isUserHasPermission(userId, groupId)) {
            log.info("User {} has no permissions to delete the poll", userId);
            throw new AccessDeniedException("User " + userId + " has no permissions to delete the poll");
        }
    }

    public void validateVoteRequest(VoteRequest voteRequest) throws IllegalArgumentException{
        validateVotingItemIdExists(voteRequest.votingItemId());
        validateVoteAction(voteRequest.action());
        validateDeleteVote(voteRequest);

    }

    public void validateVoteCountRequest(VoteCountRequest voteCountRequest) throws IllegalArgumentException {
        validateVotingItemIdExists(voteCountRequest.votingItemId());
    }

    private void validateVotingItemIdExists(int votingItemId) {
        if(!votingItemRepository.existsById(votingItemId)) {
            log.info("Client tried to vote for an answer that does not exist.");
            throw new ResourceNotFoundException("Vote with id " + votingItemId + " does not exist");
        }
    }

    private void validateVoteAction(String action) {
        if(!action.equals("add") && !action.equals("remove")) {
            log.info("Client tried to request an action that does not exist.");
            throw new ResourceNotFoundException("Action " + action + " does not exist");
        }
    }

    private void validateDeleteVote(VoteRequest voteRequest) throws IllegalArgumentException{
        if(VoteAction.REMOVE.name().equalsIgnoreCase(voteRequest.action())) {
            int voteCount = votingItemRepository.getReferenceById(voteRequest.votingItemId()).getVoteCount();
            if(voteCount <= 0) {
                log.info("Client tried to remove vote from a vote with 0 vote count.");
                throw new IllegalArgumentException("Can not remove vote from voting item with 0 vote count");
            }
        }
    }

    private boolean isUserCreatedThePoll(UUID userId, UUID pollId) {
        return pollRepository.getReferenceById(pollId).getCreatorId().equals(userId);
    }

    private boolean isUserHasPermission(UUID userId, String groupId) {
        log.info("Sending validating permissions request to group service");
        ResponseEntity<Boolean> response = groupClient.checkUserPermissionToDeletePoll(userId, groupId);
        boolean result = false;

        if (response.getStatusCode().is2xxSuccessful()) {
            result = response.getBody();
        }

        return result;
    }
}
