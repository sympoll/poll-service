package com.MTAPizza.Sympoll.pollmanagementservice.model.poll;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.answer.AnswerResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.model.answer.Answer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "polls")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pollId;

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "num_answers_allowed")
    private int numAnswersAllowed;

    @Column(name = "creator_id")
    private int creatorId;

    @Column(name = "group_id")
    private int groupId;

    @Column(name = "time_created")
    private LocalDateTime timeCreated;

    @Column(name = "time_updated")
    private LocalDateTime timeUpdated;

    @Column(name = "time_ends")
    private LocalDateTime timeEnds;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "poll_id")
    private List<Answer> answersList;


    /**
     * @return A PollResponse representation if this poll
     */
    public PollResponse toPollResponse(){
        return new PollResponse(
                this.getPollId(),
                this.getTitle(),
                this.getDescription(),
                this.getNumAnswersAllowed(),
                this.getCreatorId(),
                this.getGroupId(),
                this.getTimeCreated(),
                this.getTimeUpdated(),
                this.getTimeEnds(),

                /* Convert Answers to answer responses */
                this.getAnswersList().stream().map(answer -> new AnswerResponse(
                        answer.getAnswerId(),
                        answer.getAnswerOrdinal(),
                        answer.getAnswerContent(),
                        answer.getNumberOfVotes()
                )).toList());
    }
}