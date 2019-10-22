package com.example.demo;

import java.util.ArrayList;

public class StaticData {
    private ArrayList<String> behavioralQuestions;
    private Status curStatus = Status.NOT_SUBMITTED;   // Initial

    public StaticData() {
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
        NOT_SUBMITTED, SUBMITTED, PENDING_INTERVIEW, PENDING_OFFER, REJECTED;
    }

    public ArrayList<String> getBehavioralQuestions() {
        return behavioralQuestions;
    }

    public void setBehavioralQuestions(ArrayList<String> behavioralQuestions) {
        this.behavioralQuestions = behavioralQuestions;
    }
}
