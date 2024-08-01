package com.MTAPizza.Sympoll.pollmanagementservice.model.voting.item;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer_options")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VotingItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int votingItemId;

    @Column(name = "ordinal")
    private int votingItemOrdinal;

    @Column(name = "answer_text")
    private String description;

    @Column(name = "num_of_votes")
    private int voteCount;
}