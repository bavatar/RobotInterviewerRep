package com.example.demo;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

public class StaticData {
    private Status curStatus = Status.NOT_SUBMITTED;   // Initial

    private ArrayList<String> behavioralQuestions = new ArrayList<>();
    private ArrayList<String> techQs = new ArrayList<>();

     public ArrayList<String> getBehaviorQuestions(){
        return behavioralQuestions;
     }

     public ArrayList<String> getTechQs(){
         return techQs;
     }

     //remember to match Questions to types of jobs

     public void setDeveloperQuestions(){
         techQs.add("Do you like programming? If yes, why?");
         techQs.add("Tell me about the project you’ve worked on that you’re most proud of. " +
                 "What did you do that worked out particularly well?");
         techQs.add("Tell me about the project you’ve worked on that you’re least proud of. " +
                 "What would you do differently?");
         techQs.add("If you could master one technology this year, what would it be?");
     }

     public void setManagerQuestions(){
         techQs.add("Describe your management style");
         techQs.add("How do you define success?");
         techQs.add("Tell me about a time you led by example.");
         techQs.add("What is your biggest management weakness?");
     }

     public void setHelpDeskQuestions(){
         techQs.add("How important IT skills are in help desk service and how you keep yourself " +
                 "updated with those skills?");
         techQs.add("What is your worst experience so far as a help desk assistant?");
         techQs.add("What is the difference between helpdesk and service desk?");
         techQs.add("What is the recent skill you have learned that can be helpful for help desk position?");
     }


    static public ArrayList<String> kWords(String str){
        ArrayList<String> arrList = new ArrayList();
        String[] kwords = str.split(",");
        for (String s: kwords){
            arrList.add(s.trim());
        }
        return arrList;
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
//        techQs = new HashMap<>();
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
        // Initial Default State
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
