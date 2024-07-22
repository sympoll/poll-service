package com.MTAPizza.Sympoll.pollmanagementservice.model.poll;

import com.MTAPizza.Sympoll.pollmanagementservice.model.answer.Answer;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "polls")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Poll {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pollId;

    private String title;
    private String description;
    private int numAnswersAllowed;
    private int creatorId;
    private int groupId;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeCreated;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeUpdated;

    @Temporal(TemporalType.TIMESTAMP)
    private Date timeEnds;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JoinColumn(name = "poll_id")
    private List<Answer> answersList;

}