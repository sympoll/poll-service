package com.MTAPizza.Sympoll.pollmanagementservice.model.answer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "answer_options")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Answer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int answerId;

    @Column(name = "ordinal")
    private int answerOrdinal;

    @Column(name = "answer_text")
    private String answerContent;

    @Column(name = "num_of_votes")
    private long numberOfVotes;
}