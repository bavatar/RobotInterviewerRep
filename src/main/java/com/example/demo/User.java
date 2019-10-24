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

//    @Column(name = "applied_jobs")
//    private Set<Job> applied_jobs;

    @Column(length = 20000, name = "resume")
    private String resume;

    @OneToMany(mappedBy = "user")
    private Set<Job> jobs;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;
//
//    public Set<Job> getApplied_jobs() {
//        return applied_jobs;
//    }
//
//    public void setApplied_jobs(Set<Job> applied_jobs) {
//        this.applied_jobs = applied_jobs;
//    }

    public User(String email, String password, String firstName, String lastName, Boolean enabled, String username, Set<Job> applied_jobs, String resume, Set<Job> jobs, Collection<Role> roles) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.enabled = enabled;
        this.username = username;
//        this.applied_jobs = applied_jobs;
        this.resume = resume;
        this.jobs = jobs;
        this.roles = roles;
    }

    public String getResume() {
        return resume;
    }

    public void setResume(String resume) {
        this.resume = resume;
    }

    public User() {
        this.jobs = new HashSet<>();
//        this.applied_jobs = new HashSet<>();
    }


    // Sets the status for newly submitted applications
    public void getMatches(String res, Set<Job> jobs){
        int percent = 0;
        int numberFound = 0;
        boolean pass = false;
        StringBuffer resBuffer = new StringBuffer(res);

        for (Job job: jobs) {
            percent = 0;
            numberFound = 0;
            pass = false;
            if (job.getCurStatus()!= StaticData.Status.NOT_SUBMITTED){
                continue;
            }

            for (String s : job.getKeywords()){
                if (resBuffer.indexOf(s) != -1){
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

        System.out.println("getMatches: Number of applied_jobs" + StaticData.applied_jobs.size());
        ArrayList<Job> applArray = StaticData.getJobsByApplicantID(id);
        System.out.println("getMatches: Number of StaticData.getJobsAppliedByUserId(id).size: " + StaticData.getJobsByApplicantID(id).size());

        //StringBuilder res = new StringBuilder(10000);
        StringBuffer resBuffer = new StringBuffer(resume);

        System.out.println("getMatches: Number of User Jobs: " + jobs.size());
        for (Job job: applArray) {
            percent = 0;
            numberFound = 0;
            pass = false;
            if (job.getCurStatus()== StaticData.Status.NOT_SUBMITTED){
                continue;
            }

            for (String s : job.getKeywords()){
                // crashes on the following line JA 10-23-19
                if (resBuffer.indexOf(s) != -1){
                    numberFound++;
                }
            }
            percent = (100 * numberFound) / job.getKeywords().size();
            if (percent > 80) {
                job.setCurStatus(StaticData.Status.PENDING_INTERVIEW);
                System.out.println("getMatches: Passed Rating percent: " + percent);
            } else {
                System.out.println("getMatches: Rejected Rating percent: " + percent);
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

//    public Set<Job> getApplied_jobs() {
//        return applied_jobs;
//    }

//    public void setApplied_jobs(Set<Job> applied_jobs) {
//        this.applied_jobs = applied_jobs;
//    }
}
