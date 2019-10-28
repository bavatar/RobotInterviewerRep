package com.example.demo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ScheduleInterview {
    private long userId, jobId;
    private LocalDateTime selectedTime;
    private String stringSelectTime;

    static public ArrayList<ScheduleInterview> userScheduleDates = new ArrayList<>();

    public ScheduleInterview(){
    }

    public String getStringSelectTime() {
        return stringSelectTime;
    }

    public void setStringSelectTime(String stringSelectTime) {
        this.stringSelectTime = stringSelectTime;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public LocalDateTime getSelectedTime() {
        return selectedTime;
    }

    public void setSelectedTime(LocalDateTime selectedTime) {
        this.selectedTime = selectedTime;
    }
}
