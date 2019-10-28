package com.example.demo;

import java.util.List;

public class QuestionCreationDto {
    private List<String> questions;

    public QuestionCreationDto() {
    }

    public QuestionCreationDto(List<String> questions) {
        this.questions = questions;
    }

    public void addAddQuestions(List<String> questions){
        this.questions.addAll(questions);
    }

    public List<String> getQuestions() {
        return questions;
    }

    public void setQuestions(List<String> questions) {
        this.questions = questions;
    }
}
