package com.example.demo;

import java.util.ArrayList;
import java.util.HashMap;

public class UserAnswersDto {
    long userId , jobId;
    // job id, answer


    private String answer;

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public UserAnswersDto() {
        userId = 0;
        jobId = 0;
        answer = "";
    }

    public UserAnswersDto(long userId, long jobId, String answer) {
        this.userId = userId;
        this.jobId = jobId;
        this.answer = answer;
    }

    static public ArrayList<UserAnswersDto> userAnswersArr = new ArrayList<>();
//    static public void addAnswer(long userId, long jobId, String answer){
////        userAnswer = new UserAnswersDto(userId, new HashMap<>());
//        for(int i = 0; i < userAnswersArr.size(); i++){
//            if(userId == userAnswersArr.get(i).userId) {
//                if (!userAnswersArr.get(i).answers.containsKey(jobId)) {
//                    userAnswersArr.get(i).answers.put(jobId, answer);
//                }
//            }
//            else{
//                UserAnswersDto toAdd = new UserAnswersDto();
//                toAdd.setUserId(userId);
//                HashMap<Long, String> jobAnswer = new HashMap<>();
//                jobAnswer.put(jobId, answer);
//                toAdd.setAnswers(jobAnswer);
//
//                userAnswersArr.add(toAdd);
//            }
//        }
//        userAnswersArr.add(new UserAnswersDto(userId, Has))
//    }

    static public UserAnswersDto getUserAnswerFromArr(long userId){
        for(int i = 0; i < userAnswersArr.size(); i++)
            if(userId == userAnswersArr.get(i).userId)
                return userAnswersArr.get(i);

        return null;
    }



    //Getters and Setters
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
}
