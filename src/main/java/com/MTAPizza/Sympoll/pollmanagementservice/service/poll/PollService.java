package com.MTAPizza.Sympoll.pollmanagementservice.service.poll;

import com.MTAPizza.Sympoll.pollmanagementservice.client.GroupClient;
import com.MTAPizza.Sympoll.pollmanagementservice.client.UserClient;
import com.MTAPizza.Sympoll.pollmanagementservice.client.VoteClient;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.group.DeleteGroupPollsRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.group.DeleteGroupPollsResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.group.GroupNameResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.delete.PollDeleteRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.delete.PollDeleteResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.user.UsernameResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.DeleteMultipleVotesRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.DeleteMultipleVotesResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.model.voting.item.VotingItem;
import com.MTAPizza.Sympoll.pollmanagementservice.model.poll.Poll;
import com.MTAPizza.Sympoll.pollmanagementservice.repository.poll.PollRepository;
import com.MTAPizza.Sympoll.pollmanagementservice.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollService {
    private final PollRepository pollRepository;
    private final Validator validator;
    private final UserClient userClient;
    private final GroupClient groupClient;
    private final VoteClient voteClient;

    /**
     * Given a list of polls, fetch their creator names and group names from user and group services
     *
     * @param polls Polls to convert.
     * @return DTO list of PollResponses with creator and group name.
     */
    public List<PollResponse> getPollResponsesWithCreatorAndGroupNames(List<Poll> polls) {
        // Collect all creator and group IDs from the list of polls
        Set<UUID> creatorIds = polls.stream()
                .map(Poll::getCreatorId)
                .collect(Collectors.toSet());
        Set<String> groupIds = polls.stream()
                .map(Poll::getGroupId)
                .collect(Collectors.toSet());

        // Fetch all creator names and group names concurrently
        CompletableFuture<Map<UUID, String>> creatorNamesFuture = CompletableFuture.supplyAsync(() -> getCreatorNames(creatorIds));
        CompletableFuture<Map<String, String>> groupNamesFuture = CompletableFuture.supplyAsync(() -> getGroupNames(groupIds));

        CompletableFuture<Void> combinedFuture = CompletableFuture.allOf(creatorNamesFuture, groupNamesFuture);
        combinedFuture.join();

        // Combine results into a map
        Map<UUID, String> creatorNames = creatorNamesFuture.join();
        Map<String, String> groupNames = groupNamesFuture.join();

        // Map each poll to its response with the fetched creator and group names
        return polls.stream()
                .map(poll -> {
                    poll.setCreatorName(creatorNames.getOrDefault(poll.getCreatorId(), "Unknown Creator"));
                    poll.setGroupName(groupNames.getOrDefault(poll.getGroupId(), "Unknown Group"));
                    return poll.toPollResponse();
                })
                .collect(Collectors.toList());
    }

    private Map<UUID, String> getCreatorNames(Set<UUID> creatorIds) {
        try {
            log.info("Batch fetching creator names from user service");
            ResponseEntity<List<UsernameResponse>> response = userClient.getUserNameList(new ArrayList<>(creatorIds));
            return Objects.requireNonNull(response.getBody()).stream()
                    .collect(Collectors.toMap(UsernameResponse::userId, UsernameResponse::username));
        } catch (Exception e) {
            log.error("Failed to fetch creator names", e);
            return Collections.emptyMap();
        }
    }

    private Map<String, String> getGroupNames(Set<String> groupIds) {
        try {
            log.info("Batch fetching group names from group service");
            ResponseEntity<List<GroupNameResponse>> response = groupClient.getGroupNameList(new ArrayList<>(groupIds));
            return Objects.requireNonNull(response.getBody()).stream()
                    .collect(Collectors.toMap(GroupNameResponse::groupId, GroupNameResponse::groupName));
        } catch (Exception e) {
            log.error("Failed to fetch group names", e);
            return Collections.emptyMap();
        }
    }

    /**
     * Create and add a poll to the database.
     *
     * @param pollCreateRequest Details of the poll to add.
     * @return The poll that was added to the database.
     */
    @Transactional
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

        // Wrap the poll in a list to use batch fetching logic
        List<Poll> singlePollList = Collections.singletonList(poll);

        // Fetch and return the poll response with creator and group names
        List<PollResponse> pollResponses = getPollResponsesWithCreatorAndGroupNames(singlePollList);

        // Return the first (and only) PollResponse from the list
        return pollResponses.get(0);
    }

    /**
     * Converts a list of voting items strings into a list of Answer entities.
     *
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
     *
     * @param timeStamp The timestamp string in ISO-8601 format.
     * @return LocalDateTime representation of the timestamp.
     */
    private LocalDateTime convertToDate(String timeStamp) {
        Instant instant = Instant.parse(timeStamp);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Retrieves all polls from the repository.
     * Retrieves their creator name and group name.
     *
     * @return List of poll responses in descending order by creation date.
     */
    public List<PollResponse> getAllPolls() {
        log.info("Retrieving all polls in database...");
        List<Poll> polls =  pollRepository.findAll();

        return getPollResponsesWithCreatorAndGroupNames(polls)
                .stream()
                .sorted(Comparator.comparing(PollResponse::timeCreated).reversed())
                .toList();
    }

    /**
     * Delete a poll from the database.
     *
     * @param pollDeleteRequest ID of the poll to delete and the ID of the user.
     * @return the ID of the poll deleted.
     */
    @Transactional
    public PollDeleteResponse deletePoll(PollDeleteRequest pollDeleteRequest) {
        validator.validateDeletePollRequest(pollDeleteRequest);
        // TODO: send request to voting service to delete all corresponding votes.
        log.info("Deleting poll with ID: {}", pollDeleteRequest.pollId());
        pollRepository.deleteById(pollDeleteRequest.pollId());
        log.info("POLL: {} was deleted.", pollDeleteRequest.pollId());
        return new PollDeleteResponse(pollDeleteRequest.pollId());
    }

    /**
     * Retrieve a poll from the database.
     *
     * @param pollId ID of the poll to retrieve.
     * @return The retrieved poll's details.
     */
    public PollResponse getPollById(UUID pollId) {
        validator.validateGetPollByIdRequest(pollId);

        log.info("Retrieving poll with ID: {}", pollId);

        Poll poll = pollRepository.getReferenceById(pollId);

        // Wrap the poll in a list to use batch fetching logic
        List<Poll> singlePollList = Collections.singletonList(poll);

        // Fetch and return the poll response with creator and group names
        List<PollResponse> pollResponses = getPollResponsesWithCreatorAndGroupNames(singlePollList);

        // Return the first (and only) PollResponse from the list
        return pollResponses.get(0);
    }

    /**
     * Get a list of all polls of a group.
     * Sorted by creation date, newest first.
     */
    public List<PollResponse> getPollsByGroupId(String groupId) {
        validator.validateGetPollsByGroupIdRequest(groupId);

        log.info("Retrieving all polls by group ID: {}", groupId);
        List<Poll> polls = pollRepository
                .findAll()
                .stream()
                .filter(poll -> poll.getGroupId().equals(groupId))
                .toList();

        return getPollResponsesWithCreatorAndGroupNames(polls)
                .stream()
                .sorted(Comparator.comparing(PollResponse::timeCreated).reversed())
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
        for (String groupId : groupIds) {
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
        return getPollResponsesWithCreatorAndGroupNames(resPolls)
                .stream()
                .sorted(Comparator.comparing(PollResponse::timeCreated).reversed())
                .toList();
    }

    /**
     * Get a list of all polls from all groups the given user is a member of.
     * Polls are returned sorted in descending order by creation date.
     */
    public List<PollResponse> getAllUserPolls(UUID userId) {
            List<String> userGroups = Objects.requireNonNull(
                    Objects.requireNonNull(groupClient.getAllUserGroups(userId)
                            .getBody()).userGroups());

            List<Poll> polls = pollRepository.findByGroupIdIn(userGroups);

            // Sort polls by timeCreated in descending order in the service layer
            return getPollResponsesWithCreatorAndGroupNames(polls)
                    .stream().sorted(Comparator.comparing(PollResponse::timeCreated).reversed())
                    .toList();
    }

    /**
     * Deleting all polls related to the given group id.
     * @param deleteGroupPollsRequest The given group id.
     * @return A DTO with the removed poll ids.
     */
    @Transactional
    public DeleteGroupPollsResponse deleteGroupPolls(DeleteGroupPollsRequest deleteGroupPollsRequest) {
        List<Poll> groupPolls = pollRepository.findByGroupId(deleteGroupPollsRequest.groupId());
        List<UUID> pollIds = groupPolls.stream().map(Poll::getPollId).toList();

        sendDeleteRequestToVoteService(groupPolls);
        pollRepository.deleteByPollIdIn(pollIds);
        return new DeleteGroupPollsResponse(pollIds);
    }

    /**
     * 'DeleteGroupPolls' helper, sending delete request to the vote service.
     * @param groupPolls List of the group's poll ids.
     */
    private void sendDeleteRequestToVoteService(List<Poll> groupPolls) {
        List<Integer> allPollsVotingItemIds = groupPolls.stream()
                .flatMap(poll -> poll.getVotingItems().stream())
                .map(VotingItem::getVotingItemId)
                .collect(Collectors.toList());

        ResponseEntity<DeleteMultipleVotesResponse> respone = voteClient.deleteMultipleVotes(new DeleteMultipleVotesRequest(allPollsVotingItemIds));

        if (!respone.getStatusCode().is2xxSuccessful()) {
            //TODO throw error.
        }
    }
}
