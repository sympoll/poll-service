package com.MTAPizza.Sympoll.pollmanagementservice.service.poll;

import com.MTAPizza.Sympoll.pollmanagementservice.client.GroupClient;
import com.MTAPizza.Sympoll.pollmanagementservice.client.UserClient;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.delete.PollDeleteRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.delete.PollDeleteResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.model.voting.item.VotingItem;
import com.MTAPizza.Sympoll.pollmanagementservice.model.poll.Poll;
import com.MTAPizza.Sympoll.pollmanagementservice.repository.poll.PollRepository;
import com.MTAPizza.Sympoll.pollmanagementservice.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollService {
    private final PollRepository pollRepository;
    private final Validator validator;
    private final UserClient userClient;
    private final GroupClient groupClient;

    /**
     * Get poll response DTO of the poll, including the creator name.
     * @param poll Poll to convert.
     * @return DTO of PollResponse with a creator name.
     */
    private PollResponse getPollResponseWithCreatorName(Poll poll) {
        poll.setCreatorName(
                Objects.requireNonNull(
                        userClient.getUserById(poll.getCreatorId())
                                .getBody()).username());

        poll.setGroupName(
                Objects.requireNonNull(
                        groupClient.getGroupNameById(poll.getGroupId())
                                .getBody()).groupName());

        return poll.toPollResponse();
    }

    /**
     * Create and add a poll to the database.
     * @param pollCreateRequest Details of the poll to add.
     * @return The poll that was added to the database.
     */
    public PollResponse createPoll(PollCreateRequest pollCreateRequest) {
        validator.validateNewPoll(pollCreateRequest);

        Poll poll = Poll.builder()
                .title(pollCreateRequest.title())
                .description(pollCreateRequest.description())
                .nofAnswersAllowed(pollCreateRequest.nofAnswersAllowed())
                .creatorId(pollCreateRequest.creatorId())
                .groupId(pollCreateRequest.groupId())
                .deadline(convertToDate(pollCreateRequest.deadline()))
                .build();
        poll.setVotingItems(convertVotingItemsToModel(pollCreateRequest.votingItems(), poll.getPollId()));

        pollRepository.save(poll);
        log.info("POLL: {} by USER: {} was created.", poll.getPollId(), poll.getCreatorId());
        return getPollResponseWithCreatorName(poll);
    }

    /**
     * Converts a list of voting items strings into a list of Answer entities.
     * @param votingItems List of voting items strings to be converted.
     * @return List of voting items entities.
     */
    private List<VotingItem> convertVotingItemsToModel(List<String> votingItems, UUID pollId) {
        List<VotingItem> resVotingItems = new ArrayList<>();

        for (String votingItem : votingItems) {
            VotingItem newVotingItem = new VotingItem();
            newVotingItem.setPollId(pollId);
            newVotingItem.setDescription(votingItem);
            newVotingItem.setVoteCount(0);
            resVotingItems.add(newVotingItem);
        }

        return resVotingItems;
    }

    /**
     * Converts a timestamp string to LocalDateTime.
     * @param timeStamp The timestamp string in ISO-8601 format.
     * @return LocalDateTime representation of the timestamp.
     */
    private LocalDateTime convertToDate(String timeStamp) {
        Instant instant = Instant.parse(timeStamp);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Retrieves all polls from the repository and maps them to PollResponse DTOs.
     * @return List of PollResponse DTOs containing details of all polls.
     */
    public List<PollResponse> getAllPolls() {
        log.info("Retrieving all polls in database...");
        return pollRepository
                .findAll()
                .stream()
                .map(this::getPollResponseWithCreatorName)
                .toList();
    }

    /**
     * Delete a poll from the database.
     * @param pollDeleteRequest ID of the poll to delete and the ID of the user.
     * @return the ID of the poll deleted.
     */
    public PollDeleteResponse deletePoll(PollDeleteRequest pollDeleteRequest) {
        validator.validateDeletePollRequest(pollDeleteRequest);

        log.info("Deleting poll with ID: {}", pollDeleteRequest.pollId());
        pollRepository.deleteById(pollDeleteRequest.pollId());
        log.info("POLL: {} was deleted.", pollDeleteRequest.pollId());
        return new PollDeleteResponse(pollDeleteRequest.pollId());
    }

    /**
     * Retrieve a poll from the database.
     * @param pollId ID of the poll to retrieve.
     * @return The retrieved poll's details.
     */
    public PollResponse getPollById(UUID pollId) {
        validator.validateGetPollByIdRequest(pollId);

        log.info("Retrieving poll with ID: {}", pollId);
        return getPollResponseWithCreatorName(pollRepository.getReferenceById(pollId));
    }

    /**
     * Get a list of all polls of a group.
     * Sorted by creation date, newest first.
     */
    public List<PollResponse> getPollsByGroupId(String groupId) {
        validator.validateGetPollsByGroupIdRequest(groupId);

        log.info("Retrieving all polls by group ID: {}", groupId);
        return pollRepository
                .findAll()
                .stream()
                .filter(poll -> poll.getGroupId().equals(groupId))
                .sorted() // Sort by date, most recent poll first.
                .map(this::getPollResponseWithCreatorName)
                .toList();
    }

    /**
     * Get a list of all polls of multiple groups.
     * Sorted by creation date, newest first.
     */
    public List<PollResponse> getPollsByMultipleGroupIds(List<String> groupIds) {
        validator.validateGetPollsByMultipleGroupIdsRequest(groupIds);

        log.info("Retrieving all polls by multiple group IDs: {}", groupIds);
        List<Poll> resPolls = new ArrayList<>();
        for(String groupId : groupIds) {
            resPolls.addAll(
                    pollRepository
                        .findAll()
                        .stream()
                        .filter(poll -> poll.getGroupId().equals(groupId))
                        .toList());
        }
        // First Sort the result polls by date, most recent poll first,
        // then map each Poll to a PollResponse object,
        // and return the result.
        return resPolls.stream().sorted().map(this::getPollResponseWithCreatorName).toList();
    }
}
