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

    public PollResponse createPoll(PollCreateRequest pollCreateRequest) {
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


    /**
     * Converts a list of answer strings into a list of Answer entities.
     *
     * @param answers List of answer strings to be converted.
     * @return List of Answer entities.
     */
    private List<Answer> convertAnswersToModel(List<String> answers) {
        List<Answer> newAnswersList = new ArrayList<>();
        int ord = 0;
        for (String answer : answers) {
            Answer newAnswer = new Answer();
            newAnswer.setAnswerContent(answer);
            newAnswer.setAnswerOrdinal(ord++);
            newAnswer.setNumberOfVotes(0);
            newAnswersList.add(newAnswer);
        }

        return newAnswersList;
    }

    /**
     * Converts a timestamp string to LocalDateTime.
     *
     * @param timeStamp The timestamp string in ISO-8601 format.
     * @return LocalDateTime representation of the timestamp.
     */
    private LocalDateTime convertToDate(String timeStamp) {
        Instant instant = Instant.parse(timeStamp);
        return instant.atZone(ZoneId.systemDefault()).toLocalDateTime();
    }

    /**
     * Retrieves all polls from the repository and maps them to PollResponse DTOs.
     *
     * @return List of PollResponse DTOs containing details of all polls.
     */
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
