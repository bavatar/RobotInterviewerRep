package com.example.demo;

import java.util.ArrayList;

public class StaticData {
    private ArrayList<String> behavioralQuestions;
    private Status curStatus = Status.NOT_SUBMITTED;   // Initial

    public StaticData() {
        behavioralQuestions = new ArrayList<>();
        behavioralQuestions.add("What is your greatest achievement?");
        behavioralQuestions.add("What is your greatest failure?");
        behavioralQuestions.add("How do you like working on a team?");
        behavioralQuestions.add("Do you like people?");
    }

    public String getCurStatus() {
        String statusStr = curStatus.name();
        return statusStr;
    }

    public void setCurStatus(Status curStatus) {
        this.curStatus = curStatus;
    }

    public enum Status
    {
        NOT_SUBMITTED,
        // User has applied for the job but the decision on granting an interview has not yet been made
        SUBMITTED,
        // PENDING_INTERVIEW indicates that the interview has not been scheduled yet but is pending
        PENDING_INTERVIEW,
        PENDING_OFFER,
        // PENDING_SCHEDULED_INTERVIEW indicates that the interview is pending and has been scheduled.
        PENDING_SCHEDULED_INTERVIEW,
        REJECTED;
    }

    public ArrayList<String> getBehavioralQuestions() {
        return behavioralQuestions;
    }

    public void setBehavioralQuestions(ArrayList<String> behavioralQuestions) {
        this.behavioralQuestions = behavioralQuestions;
    }
}
