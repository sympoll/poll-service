package com.MTAPizza.Sympoll.pollmanagementservice.model.poll;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.voting.item.VotingItemResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.model.voting.item.VotingItem;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "polls")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Poll implements Comparable<Poll>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID pollId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "nof_answers_allowed")
    private Integer nofAnswersAllowed;

    @Column(name = "creator_id")
    private UUID creatorId;

    @Column(name = "group_id")
    private String groupId;

    @Column(name = "time_created")
    private final LocalDateTime timeCreated = LocalDateTime.now(); // Initialize to the current time.

    @Column(name = "time_updated")
    private LocalDateTime timeUpdated; // NOTE: the last time the poll was updated, can be ignored for now.

    @Column(name = "deadline")
    private LocalDateTime deadline;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "poll_id")
    private List<VotingItem> votingItems;

    /**
     * @return A PollResponse representation if this poll
     */
    public PollResponse toPollResponse(){
        return new PollResponse(
                this.getPollId(),
                this.getTitle(),
                this.getDescription(),
                this.getNofAnswersAllowed(),
                this.getCreatorId(),
                this.getGroupId(),
                this.getTimeCreated(),
                this.getTimeUpdated(),
                this.getDeadline(),

                /* Convert Answers to answer responses */
                this.getVotingItems().stream()
                        .sorted(Comparator.comparing(VotingItem::getVotingItemId))
                        .map(votingItem -> new VotingItemResponse(
                        votingItem.getVotingItemId(),
                        votingItem.getVotingItemOrdinal(),
                        votingItem.getDescription(),
                        votingItem.getVoteCount()
                )).toList());
    }

    /**
     * Compare two polls by the timeCreated field.
     * @param other the object to be compared.
     * @return the most recent poll of the two.
     */
    @Override
    public int compareTo(Poll other) {
        return other.getTimeCreated().compareTo(this.getTimeCreated());
    }
}