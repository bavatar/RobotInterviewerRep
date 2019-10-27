package com.example.demo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.text.SimpleDateFormat;
import java.util.*;

import static java.time.format.DateTimeFormatter.BASIC_ISO_DATE;

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

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date postedDate;

    private ArrayList<String> keywords;

    private StaticData.Status curStatus;

    @DateTimeFormat(pattern ="yyyy-MM-dd HH:mm")
    Date interviewDateTime = new Date();

    @OneToMany(mappedBy = "job", fetch = FetchType.EAGER)
    private Set<QsAndAs> questionsAndAnswers;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Job() {
    }
    public Job(@NotNull @Size(min = 4) String title, @NotNull @Size(min = 4) String phone,
               @NotNull @Size(min = 4) String employerName, @NotNull @Size(min = 4)
                       String employerEmail, @NotNull @Size(min = 6) String description,
               Date postedDate, ArrayList<String> keywords, StaticData.Status curStatus,
               Date interviewDateTime, Set<QsAndAs> questionsAndAnswers, User user) {
        this.title = title;
        this.phone = phone;
        this.employerName = employerName;
        this.employerEmail = employerEmail;
        this.description = description;
        this.postedDate = postedDate;
        this.keywords = keywords;
        this.curStatus = curStatus;
        this.interviewDateTime = interviewDateTime;
        this.questionsAndAnswers = questionsAndAnswers;
        this.user = user;
    }

    public Set<QsAndAs> getQuestionsAndAnswers() {
        return questionsAndAnswers;
    }

    public void setQuestionsAndAnswers(Set<QsAndAs> questionsAndAnswers) {
        this.questionsAndAnswers = questionsAndAnswers;
    }

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

    public ArrayList<String> getKeywords() {
        return keywords;
    }

    public void setKeywords(ArrayList<String> keywords) {
        this.keywords = keywords;
    }

    public StaticData.Status getCurStatus() {
        return curStatus;
    }

    public void setCurStatus(StaticData.Status curStatus) {
        this.curStatus = curStatus;
    }

    public Date getInterviewDateTime() {
        return interviewDateTime;
    }

    public void setInterviewDateTime(Date interviewDateTime) {
        this.interviewDateTime = interviewDateTime;
    }
}
