package com.MTAPizza.Sympoll.pollmanagementservice.validator;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.delete.VoteDeleteRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.model.poll.Poll;
import com.MTAPizza.Sympoll.pollmanagementservice.repository.poll.PollRepository;
import com.MTAPizza.Sympoll.pollmanagementservice.repository.voting.item.VotingItemRepository;
import com.MTAPizza.Sympoll.pollmanagementservice.service.voting.item.VotingItemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
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

    public void validateNewPoll(PollCreateRequest poll) throws IllegalArgumentException{
        validateAllowedVotingItems(poll.votingItems().size(), poll);
        validateDeadline(LocalDateTime.now(), poll);
    }

    private void validateAllowedVotingItems(int nofVotingItems, PollCreateRequest poll) {
        if (poll.nofAnswersAllowed() > nofVotingItems) {
            log.warn("User {} tried to create a poll but an number of answers allowed was given.", poll.creatorId());
            throw new IllegalArgumentException("Number of allowed answers is greater than number of available answers");
        }
    }

    private void validateDeadline(LocalDateTime timeCreated, PollCreateRequest poll){
        // Convert deadline string time stamp to LocalDateTime object
        Instant instant = Instant.parse(poll.deadline());
        LocalDateTime deadline = instant.atZone(ZoneId.systemDefault()).toLocalDateTime();

        if (deadline.isBefore(timeCreated)) {
            log.warn("User {} tried to create a poll but an invalid deadline was given.", poll.creatorId());
            throw new IllegalArgumentException("A deadline cannot be earlier than the time a poll was created");
        }
    }

    public void validateNewVote(VoteCreateRequest voteCreateRequest) throws IllegalArgumentException{
        validatePollIdExists(voteCreateRequest.pollId());
        // TODO: validate user id
        validateVotingItemIdExists(voteCreateRequest.votingItemId());
    }


    private void validatePollIdExists(UUID pollId) {
        if(!pollRepository.existsById(pollId)) {
            log.warn("Client tried to vote in a poll that does not exist.");
            throw new IllegalArgumentException("Poll with id " + pollId + " does not exist");
        }
    }

    public void validateVotingItemIdExists(int votingItemId) {
        if(!votingItemRepository.existsById(votingItemId)) {
            log.warn("Client tried to vote for an answer that does not exist.");
            throw new IllegalArgumentException("Vote with id " + votingItemId + " does not exist");
        }
    }

    public void validateDeleteVote(VoteDeleteRequest voteDeleteRequest) throws IllegalArgumentException{
        validateVotingItemIdExists(voteDeleteRequest.votingItemId());
        validateVotingCountGreaterThanZero(votingItemRepository.getReferenceById(voteDeleteRequest.votingItemId()).getVoteCount());
    }

    private void validateVotingCountGreaterThanZero(int votingCount) {
        if(votingCount <= 0) {
            log.warn("Client tried to delete vote from a vote with 0 vote count.");
            throw new IllegalArgumentException("Can not delete vote from voting item with 0 vote count");
        }
    }
}
