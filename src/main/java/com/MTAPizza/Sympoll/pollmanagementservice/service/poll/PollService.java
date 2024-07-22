package com.MTAPizza.Sympoll.pollmanagementservice.service.poll;

import com.MTAPizza.Sympoll.pollmanagementservice.dto.answer.AnswerResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollCreateRequest;
import com.MTAPizza.Sympoll.pollmanagementservice.dto.poll.PollResponse;
import com.MTAPizza.Sympoll.pollmanagementservice.model.answer.Answer;
import com.MTAPizza.Sympoll.pollmanagementservice.model.poll.Poll;
import com.MTAPizza.Sympoll.pollmanagementservice.repository.poll.PollRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PollService {
    private final PollRepository pollRepository;

    public Poll createPoll(PollCreateRequest pollCreateRequest) {
        Poll poll = Poll.builder()
                .title(pollCreateRequest.title())
                .description(pollCreateRequest.description())
                .numAnswersAllowed(pollCreateRequest.numAnswersAllowed())
                .creatorId(pollCreateRequest.creatorId())
                .groupId(pollCreateRequest.groupId())
                .timeCreated(convertToDate(pollCreateRequest.timeCreated()))
                .timeUpdated(convertToDate(pollCreateRequest.timeEnds()))
                .answersList(convertAnswersToModel(pollCreateRequest.answers()))
                .build();

        pollRepository.save(poll);
        log.info("POLL: {} by USER: {} was created.", poll.getPollId(), poll.getCreatorId());
        return poll;
    }


    private List<Answer> convertAnswersToModel(List<String> answers) {
        List<Answer> newAnswers = new ArrayList<>();
        for (String answer : answers) {
            int ord = 0;
            Answer newAnswer = new Answer();
            newAnswer.setAnswerContent(answer);
            newAnswer.setAnswerOrdinal(ord++);
            newAnswer.setNumberOfVotes(0);
        }

        return newAnswers;
    }

    private LocalDateTime convertToDate(String timeStamp) {
        Instant instant = Instant.parse(timeStamp);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    public List<PollResponse> getAllPolls() {
        return pollRepository.findAll()
                .stream()
                .map(poll -> new PollResponse(
                        poll.getPollId(),
                        poll.getTitle(),
                        poll.getDescription(),
                        poll.getNumAnswersAllowed(),
                        poll.getCreatorId(),
                        poll.getGroupId(),
                        poll.getTimeCreated(),
                        poll.getTimeUpdated(),
                        poll.getTimeEnds(),

                        /* Convert Answers to answer responses */
                        poll.getAnswersList().stream().map(answer -> new AnswerResponse(
                                answer.getAnswerId(),
                                answer.getAnswerOrdinal(),
                                answer.getAnswerContent(),
                                answer.getNumberOfVotes()
                        )).toList())).toList();
    }
}
