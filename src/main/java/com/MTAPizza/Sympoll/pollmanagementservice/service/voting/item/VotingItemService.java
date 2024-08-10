package com.MTAPizza.Sympoll.pollmanagementservice.service.voting.item;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.delete.VoteDeleteRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.model.voting.item.VotingItem;
import com.MTAPizza.Sympoll.pollmanagementservice.repository.voting.item.VotingItemRepository;
import com.MTAPizza.Sympoll.pollmanagementservice.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotingItemService {
    private final VotingItemRepository votingItemRepository;
    private final Validator validator;

    /**
     * Update a specific vote in the database.
     * @param createVoteRequest Information of the vote to be created.
     * @return The created vote for the Voting service.
     */
    public VoteResponse createVote(VoteCreateRequest createVoteRequest) {
        validator.validateNewVote(createVoteRequest);

        VotingItem votingItem = votingItemRepository.getReferenceById(createVoteRequest.votingItemId());
        votingItem.setVoteCount(votingItem.getVoteCount() + 1);
        votingItemRepository.save(votingItem);
        log.info("Voting item {} voted", createVoteRequest.votingItemId());
        return new VoteResponse(createVoteRequest.userId(), createVoteRequest.votingItemId(), LocalDateTime.now());
    }

    /**
     * Retrieve votes count of a specific vote.
     * @param votingItemId Voting item ID of requested vote.
     * @return Count of votes for the requested vote.
     */
    public int getVoteCount(int votingItemId) {
        validator.validateVotingItemIdExists(votingItemId);

        VotingItem votingItem = votingItemRepository.getReferenceById(votingItemId);
        return votingItem.getVoteCount();
    }

    /**
     * Update vote count decrement in the database.
     * @param voteDeleteRequest Information of the vote to be deleted.
     * @return The UUID of the vote that was deleted.
     */
    public UUID deleteVote(VoteDeleteRequest voteDeleteRequest) {
        validator.validateDeleteVote(voteDeleteRequest);

        VotingItem votingItem = votingItemRepository.getReferenceById(voteDeleteRequest.votingItemId());
        votingItem.setVoteCount(votingItem.getVoteCount() - 1);
        votingItemRepository.save(votingItem);
        log.info("Voting item {} unvoted", voteDeleteRequest);
        return voteDeleteRequest.voteId();
    }
}
