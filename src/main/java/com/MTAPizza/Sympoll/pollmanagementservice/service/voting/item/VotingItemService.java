package com.MTAPizza.Sympoll.pollmanagementservice.service.voting.item;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.VoteResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.action.VoteAction;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.count.VoteCountRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.vote.count.VoteCountResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.model.voting.item.VotingItem;
import com.MTAPizza.Sympoll.pollmanagementservice.repository.voting.item.VotingItemRepository;
import com.MTAPizza.Sympoll.pollmanagementservice.validator.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class VotingItemService {
    private final VotingItemRepository votingItemRepository;
    private final Validator validator;

    /**
     * Update a specific vote (add or remove voting) in the database.
     * @param voteRequest Information of the vote to be updated and the requested action.
     * @return The voting item count and description.
     */
    public VoteResponse updateVotingItem(VoteRequest voteRequest) {
        validator.validateVoteRequest(voteRequest);
        int action;

        if(VoteAction.ADD.name().equalsIgnoreCase(voteRequest.action())){
            action = 1;
        } else {
            action = -1;
        }

        VotingItem votingItem = votingItemRepository.getReferenceById(voteRequest.votingItemId());
        votingItem.setVoteCount(votingItem.getVoteCount() + action);
        votingItemRepository.save(votingItem);
        log.info("Voting item {} updated", voteRequest.votingItemId());

        return new VoteResponse(votingItem.getDescription(), votingItem.getVoteCount());
    }

    /**
     * Retrieve votes count of a specific vote.
     * @param voteCountRequest Voting item ID of requested vote.
     * @return Count of votes for the requested vote.
     */
    public VoteCountResponse getVoteCount(VoteCountRequest voteCountRequest) {
        validator.validateVoteCountRequest(voteCountRequest);

        VotingItem votingItem = votingItemRepository.getReferenceById(voteCountRequest.votingItemId());
        return new VoteCountResponse(votingItem.getVoteCount());
    }
}
