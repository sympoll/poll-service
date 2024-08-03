package com.MTAPizza.Sympoll.pollmanagementservice.validator;

import com.MTAPizza.Sympoll.pollmanagementservice.model.poll.Poll;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;

/**
 * This class handles the validation of data received from the client.
 * In case of an error, the method will throw an exception to be handled by the 'PollExceptionHandler'.
 */
@Slf4j
public class Validator {
    public static void validateNewPoll(Poll poll) throws IllegalArgumentException{
        validateAllowedVotingItems(poll.getVotingItems().size(), poll);
        validateDeadline(poll.getTimeCreated(), poll);
    }

    private static void validateAllowedVotingItems(int nofVotingItems, Poll poll) {
        if (poll.getNofAnswersAllowed() > nofVotingItems) {
            log.warn("User {} tried to create a poll but an number of answers allowed was given.", poll.getCreatorId());
            throw new IllegalArgumentException("Number of allowed answers is greater than number of available answers");
        }
    }

    private static void validateDeadline(LocalDateTime timeCreated, Poll poll){
        if (poll.getDeadline().isBefore(timeCreated)) {
            log.warn("User {} tried to create a poll but an invalid deadline was given.", poll.getCreatorId());
            throw new IllegalArgumentException("A deadline cannot be earlier than the time a poll was created");
        }
    }
}
