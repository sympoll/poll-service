package com.MTAPizza.Sympoll.pollmanagementservice.validator;

import com.MTAPizza.Sympoll.pollmanagementservice.model.poll.Poll;
import org.hibernate.boot.model.naming.IllegalIdentifierException;

import java.time.LocalDateTime;

/**
 * This class handles the validation of data received from the client.
 * In case of an error, the method will throw an exception to be handled by the 'PollExceptionHandler'.
 */
public class Validator {
    public static void validateNewPoll(Poll poll) throws IllegalArgumentException{
        validateAllowedAnswers(poll.getAnswersList().size(), poll.getNumAnswersAllowed());
        validateDeadline(poll.getTimeCreated(), poll.getDeadline());
    }

    private static void validateAllowedAnswers(int numOfAnswers, int numOfAllowedAnswers) {
        if (numOfAllowedAnswers > numOfAnswers) {
            throw new IllegalArgumentException("Number of allowed answers is greater than number of available answers");
        }
    }

    private static void validateDeadline(LocalDateTime timeCreated, LocalDateTime deadline){
        if (deadline.isBefore(timeCreated)) {
            throw new IllegalArgumentException("A deadline cannot be earlier than the time a poll was created");
        }
    }
}
