package com.MTAPizza.Sympoll.pollmanagementservice.service.voting.item;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.count.VoteCountRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.count.VoteCountResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.model.voting.item.VotingItem;
import com.MTAPizza.Sympoll.pollmanagementservice.repository.voting.item.VotingItemRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

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

    public VoteCountResponse getVoteCount(VoteCountRequest voteCountRequest) {
        // TODO: validate vote count request here.

        VotingItem votingItem = votingItemRepository.getReferenceById(voteCountRequest.votingItemId());
        return new VoteCountResponse(voteCountRequest.pollId(), voteCountRequest.votingItemId(), votingItem.getVoteCount());
    }
}
