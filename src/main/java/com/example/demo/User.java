package com.example.demo;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name="User_Data")
public class User{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name="enabled")
    private Boolean enabled;

    @Column(name = "username")
    private String username;

    public StringBuffer getResume() {
        return resume;
    }

    public void setResume(StringBuffer resume) {
        this.resume = resume;
    }

    @Column(name = "resume")
    private StringBuffer resume;

    @OneToMany(mappedBy = "user")
    private Set<Job> jobs;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;


    public User() {
        this.jobs = new HashSet<>();
    }

    public User(String email, String password, String firstName, String lastName, Boolean enabled, String username) {
        this.setEmail(email);
        this.setPassword(password);
        this.setFirstName(firstName);
        this.setLastName(lastName);
        this.setEnabled(enabled);
        this.setUsername(username);
    }

    // Sets the status for newly submitted applications
    public void getMatches(StringBuffer res, Set<Job> jobs){
        int percent = 0;
        int numberFound = 0;
        boolean pass = false;

        for (Job job: jobs) {
            percent = 0;
            numberFound = 0;
            pass = false;
            if (job.getCurStatus()!= StaticData.Status.NOT_SUBMITTED){
                continue;
            }

            for (String s : job.getKeywords()){
                if (res.indexOf(s) != -1){
                    numberFound++;
                }
            }
            percent = (100 * numberFound) / job.getKeywords().size();
            if (percent > 80) {
                job.setCurStatus(StaticData.Status.PENDING_INTERVIEW);
            } else {
                job.setCurStatus(StaticData.Status.REJECTED);
            }
        }
    }

    public void getMatches(){
        int percent = 0;
        int numberFound = 0;
        boolean pass = false;

        for (Job job: jobs) {
            percent = 0;
            numberFound = 0;
            pass = false;
            if (job.getCurStatus()!= StaticData.Status.NOT_SUBMITTED){
                continue;
            }

            for (String s : job.getKeywords()){
                if (resume.indexOf(s) != -1){
                    numberFound++;
                }
            }
            percent = (100 * numberFound) / job.getKeywords().size();
            if (percent > 80) {
                job.setCurStatus(StaticData.Status.PENDING_INTERVIEW);
            } else {
                job.setCurStatus(StaticData.Status.REJECTED);
            }
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        this.password = passwordEncoder.encode(password);
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }
}
