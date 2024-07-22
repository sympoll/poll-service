package com.MTAPizza.Sympoll.pollmanagementservice.dto.answer;

public record AnswerResponse(
        int answerId,
        int answerOrdinal,
        String answerContent,
        long numberOfVotes
) { }
