package com.example.demo;

import javax.persistence.*;

@Entity
public class QsAndAs {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id")
    private Job job;

    @Column(length = 20000, name = "question")
    private String question;

//    @Column(name = "answer")
    private String answer;

    public QsAndAs() {
    }

    public QsAndAs(Job job, String question, String answer) {
        this.job = job;
        this.question = question;
        this.answer = answer;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
