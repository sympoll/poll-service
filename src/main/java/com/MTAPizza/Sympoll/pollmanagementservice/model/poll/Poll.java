package com.MTAPizza.Sympoll.pollmanagementservice.model.poll;

import com.MTAPizza.Sympoll.pollmanagementservice.model.answer.Answer;

import java.util.Date;
import java.util.List;

public class Poll {
    private final int pollNo;
    private String pollQuestion;
    private final String creatorUsername;
    private final String groupID;
    private final String votingID;
    private final Date creationDate;
    private Date votingDeadline;
    private List<Answer> answersList;

    public Poll(int pollNo, String pollQuestion, String creatorUsername, String groupID, String votingID, Date creationDate, Date votingDeadline, List<Answer> answersList) {
        this.pollNo = pollNo;
        this.pollQuestion = pollQuestion;
        this.creatorUsername = creatorUsername;
        this.groupID = groupID;
        this.votingID = votingID;
        this.creationDate = creationDate;
        this.votingDeadline = votingDeadline;
        this.answersList = answersList;
    }

    public int getPollNo() {
        return pollNo;
    }

    public String getPollQuestion() {
        return pollQuestion;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public String getGroupID() {
        return groupID;
    }

    public String getVotingID() {
        return votingID;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Date getVotingDeadline() {
        return votingDeadline;
    }

    public List<Answer> getAnswersList() {
        return answersList;
    }
}
