package com.MTAPizza.Sympoll.pollmanagementservice.dto.poll;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.answer.AnswerResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.List;

public record PollResponse(
        int pollId,
        String title,
        String description,
        int numAnswersAllowed,
        int creatorId,
        int groupId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        Date timeCreated,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        Date timeUpdated,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss'Z'")
        Date timeEnds,
        List<AnswerResponse> answersList
) {
}
