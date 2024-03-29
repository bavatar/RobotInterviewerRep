package com.example.demo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Entity
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotNull
    @Size(min=4)
    private String title, phone, employerName, employerEmail;

    @NotNull
    @Size(min=6)
    private String description;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date postedDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime interviewDateTime = LocalDateTime.now();

    private String keywords;

    private String jobType;

    private StaticData.Status curStatus;

    @Column(length = 20000, name = "question")
    private HashSet<String> questions = new HashSet<>();
    @Column(length = 20000, name = "answers")
    private HashSet<String> answers = new HashSet<>();

//    @OneToMany(mappedBy = "job", fetch = FetchType.EAGER)
//    private Set<QsAndAs> questionsAndAnswers;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Job() {
    }

    public Job(@NotNull @Size(min = 4) String title, @NotNull @Size(min = 4) String phone,
               @NotNull @Size(min = 4) String employerName, @NotNull @Size(min = 4) String employerEmail,
               @NotNull @Size(min = 6) String description, Date postedDate, LocalDateTime interviewDateTime,
               String keywords, String jobType, StaticData.Status curStatus, HashSet<String> questions,
               HashSet<String> answers, User user) {
        this.title = title;
        this.phone = phone;
        this.employerName = employerName;
        this.employerEmail = employerEmail;
        this.description = description;
        this.postedDate = postedDate;
        this.interviewDateTime = interviewDateTime;
        this.keywords = keywords;
        this.jobType = jobType;
        this.curStatus = curStatus;
        this.questions = questions;
        this.answers = answers;
        this.user = user;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public void setInterviewDateTime(LocalDateTime interviewDateTime) {
        this.interviewDateTime = interviewDateTime;
    }

    public LocalDateTime getInterviewDateTime() {
        return interviewDateTime;
    }

    public String getStringInterviewDateTime(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");
        return interviewDateTime.format(formatter);
    }

    public HashSet<String> getQuestions() {
        return questions;
    }

    public void setQuestions(HashSet<String> questions) {
        this.questions = questions;
    }

    public HashSet<String> getAnswers() {
        return answers;
    }

    public void setAnswers(HashSet<String> answers) {
        this.answers = answers;
    }

//    public Set<QsAndAs> getQuestionsAndAnswers() {
//        return questionsAndAnswers;
//    }
//
//    public void setQuestionsAndAnswers(Set<QsAndAs> questionsAndAnswers) {
//        this.questionsAndAnswers = questionsAndAnswers;
//    }

    public String getEmployerName() {
        return employerName;
    }

    public void setEmployerName(String employerName) {
        this.employerName = employerName;
    }

    public String getEmployerEmail() {
        return employerEmail;
    }

    public void setEmployerEmail(String employerEmail) {
        this.employerEmail = employerEmail;
    }


    public Date getPostedDate() {
        return postedDate;
    }

    public void setPostedDate(Date postedDate) {
        this.postedDate = postedDate;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public StaticData.Status getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(StaticData.Status curStatus) {
        this.curStatus = curStatus;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
