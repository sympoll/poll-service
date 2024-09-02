package com.MTAPizza.Sympoll.pollmanagementservice.model.poll;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.voting.item.VotingItemResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.model.voting.item.VotingItem;
import com.MTAPizza.Sympoll.pollmanagementservice.service.poll.PollService;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    @Transient
    private String creatorName;

    @Column(name = "group_id")
    private String groupId;

    @Transient
    private String groupName;

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
     * Converts the current Poll object into a PollResponse object.
     * This method populates the PollResponse with detailed information
     * including the creator's name, group name, and the user's chosen voting items.
     *
     * @param creatorName        The name of the poll's creator.
     * @param groupName          The name of the group associated with the poll.
     * @param chosenVotingItems  A list of voting item IDs that were chosen by the user.
     *                           This is used to set the 'chosen' field in the VotingItemResponse.
     * @return A PollResponse object that represents the current Poll,
     *         with voting items marked as chosen if applicable.
     */
    public PollResponse toPollResponse(String creatorName, String groupName, List<Integer> chosenVotingItems) {
        return new PollResponse(
                pollId,
                title,
                description,
                nofAnswersAllowed,
                creatorId,
                creatorName,
                groupId,
                groupName,
                timeCreated,
                timeUpdated,
                deadline,
                votingItems.stream()
                        .sorted(Comparator.comparing(VotingItem::getVotingItemId))
                        .map(votingItem -> votingItem.toVotingItemResponseWithChosen(chosenVotingItems.contains(votingItem.getVotingItemId())))
                        .collect(Collectors.toList())
        );
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