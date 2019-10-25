package com.example.demo;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Date;

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

    private ArrayList<String> keywords;
    private StaticData.Status curStatus;

    private HashMap<Integer, ArrayList<String>> questionsAndAnswers;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id")
    private User user;

    public Job(@NotNull @Size(min = 4) String title, @NotNull @Size(min = 4) String phone,
               @NotNull @Size(min = 4) String employerName, @NotNull @Size(min = 4) String employerEmail,
               @NotNull @Size(min = 6) String description, ArrayList<String> keywords, HashMap<Integer,
            ArrayList<String>> questionsAndAnswers, Date postedDate, User user) {
        this.title = title;
        this.phone = phone;
        this.employerName = employerName;
        this.employerEmail = employerEmail;
        this.description = description;
        this.keywords = keywords;
        this.questionsAndAnswers = questionsAndAnswers;
        this.postedDate = postedDate;
        this.user = user;
    }

    public Job() {
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

    public HashMap<Integer, ArrayList<String>> getQuestionsAndAnswers() {
        return questionsAndAnswers;
    }

    public void setQuestionsAndAnswers(HashMap<Integer, ArrayList<String>> questionsAndAnswers) {
        this.questionsAndAnswers = questionsAndAnswers;
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
}
