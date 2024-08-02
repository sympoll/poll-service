package com.MTAPizza.Sympoll.pollmanagementservice.service.poll;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
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
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollService {
    private final PollRepository pollRepository;

    public PollResponse createPoll(PollCreateRequest pollCreateRequest) {
        Poll poll = Poll.builder()
                .title(pollCreateRequest.title())
                .description(pollCreateRequest.description())
                .nofAnswersAllowed(pollCreateRequest.nofAnswersAllowed())
                .creatorId(pollCreateRequest.creatorId())
                .groupId(pollCreateRequest.groupId())
                .deadline(convertToDate(pollCreateRequest.deadline()))
                .votingItems(convertVotingItemsToModel(pollCreateRequest.votingItems()))
                .build();

        Validator.validateNewPoll(poll);
        pollRepository.save(poll);
        log.info("POLL: {} by USER: {} was created.", poll.getPollId(), poll.getCreatorId());
        return poll.toPollResponse();
    }


    /**
     * Converts a list of voting items strings into a list of Answer entities.
     *
     * @param votingItems List of voting items strings to be converted.
     * @return List of voting items entities.
     */
    private List<VotingItem> convertVotingItemsToModel(List<String> votingItems) {
        List<VotingItem> resVotingItems = new ArrayList<>();
        int ord = 0;
        for (String votingItem : votingItems) {
            VotingItem newVotingItem = new VotingItem();
            newVotingItem.setDescription(votingItem);
            newVotingItem.setVotingItemOrdinal(ord++);
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
     * Retrieves all polls from the repository and maps them to PollResponse DTOs.
     *
     * @return List of PollResponse DTOs containing details of all polls.
     */
    public List<PollResponse> getAllPolls() {
        return pollRepository.findAll()
                .stream()
                .map(Poll::toPollResponse).toList();
    }

    public UUID deletePoll(UUID pollId) {
        pollRepository.deleteById(pollId);
        log.info("POLL: {} was deleted.", pollId);
        return pollId;
    }

    public PollResponse getPollById(UUID pollId) {
        return pollRepository.getReferenceById(pollId).toPollResponse();
    }
}
