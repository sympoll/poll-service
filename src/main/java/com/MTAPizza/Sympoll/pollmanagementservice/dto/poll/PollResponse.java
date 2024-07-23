package com.MTAPizza.Sympoll.pollmanagementservice.dto.poll;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.answer.AnswerResponse;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record PollResponse(
        UUID pollId,
        String title,
        String description,
        int numAnswersAllowed,
        int creatorId,
        int groupId,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timeCreated,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timeUpdated,
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
        LocalDateTime timeEnds,
        List<AnswerResponse> answersList
) {
}
