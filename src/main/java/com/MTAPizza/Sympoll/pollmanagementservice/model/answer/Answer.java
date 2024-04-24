package com.MTAPizza.Sympoll.pollmanagementservice.model.answer;

public class Answer {
    private int answerNo;
    private String answerContent;
    private long numberOfVotes;

    public Answer(int answerNo, String answerDet) {
        this.answerNo = answerNo;
        this.answerContent = answerDet;
    }

    public int getAnswerNo() {
        return answerNo;
    }

    public String getAnswerContent() {
        return answerContent;
    }

    public long getNumberOfVotes() {
        return numberOfVotes;
    }

    public void setNumberOfVotes(long numberOfVotes) {
        this.numberOfVotes = numberOfVotes;
    }
}
