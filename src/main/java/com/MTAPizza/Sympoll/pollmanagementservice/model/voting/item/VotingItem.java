package com.MTAPizza.Sympoll.pollmanagementservice.model.voting.item;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.voting.item.VotingItemResponse;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "voting_item_options")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int votingItemId;

    @Column(name = "poll_id")
    private UUID pollId;

    @Column(name = "description")
    private String description;

    @Column(name = "vote_count")
    private int voteCount;

    public VotingItemResponse toVotingItemResponse() {
        return new VotingItemResponse(
                votingItemId,
                description,
                false,
                voteCount
        );
    }

    /**
     * Converts the VotingItem to a VotingItemResponse, setting the checked field as specified.
     *
     * @param isChecked Whether this voting item is checked by the user.
     * @return A VotingItemResponse with the checked field set.
     */
    public VotingItemResponse toVotingItemResponseWithChosen(boolean isChecked) {
        return new VotingItemResponse(
                votingItemId,
                description,
                isChecked, // Set the checked field based on the passed argument
                voteCount
        );
    }
}