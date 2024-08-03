package com.MTAPizza.Sympoll.pollmanagementservice.model.voting.item;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voting_item_options")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int votingItemId;

    @Column(name = "ordinal")
    private int votingItemOrdinal;

    @Column(name = "description")
    private String description;

    @Column(name = "vote_count")
    private int voteCount;
}