package com.MTAPizza.Sympoll.pollmanagementservice.service.voting.item;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.delete.VoteDeleteRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.model.voting.item.VotingItem;
import com.MTAPizza.Sympoll.pollmanagementservice.repository.voting.item.VotingItemRepository;
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

    public VoteResponse createVote(VoteCreateRequest createVoteRequest) {
        //TODO: validate vote request here.

        VotingItem votingItem = votingItemRepository.getReferenceById(createVoteRequest.votingItemId());
        votingItem.setVoteCount(votingItem.getVoteCount() + 1);
        votingItemRepository.save(votingItem);
        log.info("Voting item {} voted", createVoteRequest.votingItemId());
        return new VoteResponse(createVoteRequest.userId(), createVoteRequest.votingItemId(), LocalDateTime.now());
    }

    public int getVoteCount(int votingItemId) {
        // TODO: validate voting item id here.

        VotingItem votingItem = votingItemRepository.getReferenceById(votingItemId);
        return votingItem.getVoteCount();
    }

    public UUID deleteVote(VoteDeleteRequest voteDeleteRequest) {
        //TODO: validate vote delete request here. (Including checking the current vote count before decrementing)

        VotingItem votingItem = votingItemRepository.getReferenceById(voteDeleteRequest.votingItemId());
        votingItem.setVoteCount(votingItem.getVoteCount() - 1);
        votingItemRepository.save(votingItem);
        log.info("Voting item {} unvoted", voteDeleteRequest);
        return voteDeleteRequest.voteId();
    }
}
