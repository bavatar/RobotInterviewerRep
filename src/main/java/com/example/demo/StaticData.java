package com.example.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class StaticData {
    private ArrayList<String> behavioralQuestions;
    private Status curStatus = Status.NOT_SUBMITTED;   // Initial
    private HashMap<String, String> techQs;

    public HashMap<String, String> getTechQs() {
        return techQs;
    }

    static public void removeAppliedJobForUser(long uid, long jobID){
        for (int j = 0; j < applied_jobs.size(); j++) {
            if (applied_jobs.get(j).userID == uid) {
                for (int i = 0; i<applied_jobs.get(j).arrList.size(); i++){
                    if (applied_jobs.get(j).arrList.get(i).getId() == jobID) {
                        // remove this job from arrList
                        applied_jobs.get(j).arrList.remove(i);
                        break;
                    }
                }
            }
        }
    }

    public void addTechQs(String jobType, String question) {
        //String should be composite with a ; delimiter
        techQs.put(jobType, question);
    }

    // added 7pm 10-23-19
    static public ArrayList<ArrayListE> applied_jobs = new ArrayList<>();

    static public void AddAppliedJobUserID(Job j, long uid){
        if (doesUserHaveAnyPriorAppliedJobs(uid)){
            for (ArrayListE arrayListE: applied_jobs) {
                if (arrayListE.userID == uid) {
                    arrayListE.arrList.add(j);
                }
            }
        }
        else {
            ArrayListE temp = new ArrayListE();
            temp.arrList.add(j);
            temp.userID= uid;
            applied_jobs.add(temp);
        }
    }

    static boolean doesUserHaveAnyPriorAppliedJobs(long uid){
        for (ArrayListE arrayListE: applied_jobs){
            if (arrayListE.userID == uid){
                return true;
            }
        }
        return false;
    }
    static public ArrayList<Job> getJobsByApplicantID (long usrID){
        ArrayList<Job> appliedByApp = new ArrayList<>();
        for (ArrayListE arrayListE: applied_jobs){
            if (arrayListE.userID == usrID){
                return arrayListE.arrList;
            }
        }
        return appliedByApp;
    }

    public StaticData() {
        techQs = new HashMap<>();
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
