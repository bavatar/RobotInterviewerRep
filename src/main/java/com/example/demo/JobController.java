package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.*;

@Controller
public class JobController {
    @Autowired
    JobRepository jobRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    QandAsRepository qandAsRepository;


    @RequestMapping("/")
    public String jobList(Model model){
        model.addAttribute("jobs", jobRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        if (userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        }
//        long test = 0;
//        try {
//            if (userService.getUser().getId() > test) {
//                model.addAttribute("user_id", userService.getUser().getId());
//                System.out.println("AppController: jobList: userService.getUser(): " + userService.getUser().getId());
//            } else {
//                System.out.println("AppController:jobList:userService.getUser(): is null");
//                return "login"; // test 10-18-2019
//            }
//        } catch (Exception e){
//            System.out.println("AppController:jobList:userService.getUser().getId(): is null");
//            return "login"; // test 10-18-2019
//        }
//        Long id=0;
//        User user = userRepository.findById(id);
        return "listjobs";
    }

    // JA 10-23-19 11:11am
    @RequestMapping("/mypage")
    public String myPage(Model model){
//        model.addAttribute("jobs", jobRepository.findAll());
//        model.addAttribute("users", userRepository.findAll());
        if (userService.getUser() != null) {
            long uID = userService.getUser().getId();
            model.addAttribute("user_id", userService.getUser().getId());
            model.addAttribute("user", userService.getUser());
            model.addAttribute("jobs", StaticData.getJobsByApplicantID(uID));
            model.addAttribute("statusPendingInterview", StaticData.Status.PENDING_INTERVIEW);
            model.addAttribute("statusPendingScheduledInterview", StaticData.Status.PENDING_SCHEDULED_INTERVIEW);
            model.addAttribute("statusRejected", StaticData.Status.REJECTED);
        }
        //User user = userRepository.findById(userService.getUser().getId());
//        User user = userRepository.findById(user_id);
        return "mypage";
    }

    @RequestMapping("/schedule/{id}")
    public String scheduleApplicationJob(@PathVariable("id") long id, Model model){

        return "";
    }

    @RequestMapping("/cancel/{id}")
    public String cancelApplicationJob(@PathVariable("id") long id, Model model){
        model.addAttribute("jobs", jobRepository.findAll());
        model.addAttribute("users", userRepository.findAll());
        if (userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        }

        // Remove the applied job with the given id for the current user
        long uID = userService.getUser().getId();
        model.addAttribute("job", jobRepository.findById(id).get());
        StaticData.removeAppliedJobForUser(uID, id);
        return "listjobs";
    }

    @GetMapping("/add")
    public String addJob(Model model){
        model.addAttribute("job", new Job());

        if (userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        } else {
            System.out.println("AppController:jobList:userService.getUser(): is null");
        }
        return "jobform";
    }

    @PostMapping("/processsearch")
    public String searchResult(Model model, @RequestParam(name="search") String search) {
        model.addAttribute("jobs", jobRepository.findJobByTitleContainingIgnoreCase(search));
        return  "searchlist";
    }

    @PostMapping("/processjob")
    public String processJob(@ModelAttribute Job job, BindingResult result) {
        if(result.hasErrors()){
            return "jobform";
        }
        //Add new job... i.e new identity
        job.setUser(userService.getUser());
        job.setCurStatus(StaticData.Status.NOT_SUBMITTED);
        Date tempDate = new Date();
        job.setPostedDate(tempDate);
        jobRepository.save(job);
        return "redirect:/";
    }


    //Processing interview form
//    @GetMapping("/interviewform")
//    public String showInterviewForm(Model model){
//        model.addAttribute("job", jobRepository.fin)
//    }

    @RequestMapping("/base")
    public String base(@PathVariable("id") long id, Model model){
//        long userId = userService.getUser().getId();

//        model.addAttribute("job", jobRepository.findByUser(userService.getUser()));
//        if(userService.getUser() != null)
//            model.addAttribute("user_id", userService.getUser().getId());
        return "base";
    }

//    @GetMapping("/addinterview")
//    public String interviewForm(@PathVariable("id") long id, Model model){
//        model.addAttribute("job", jobRepository.findById(id).get());
//<<<<<<< HEAD
//        return "interviewform";
//    }

    @RequestMapping("/addinterview/{id}")
    public String showInterviewForm(
            @PathVariable("id") long id,
                                    Model model){
//        Job currJob = jobRepository.findByUser(userService.getUser());
        Job currJob = jobRepository.findJobById(id);
        model.addAttribute("questions", currJob.getQuestions());
        model.addAttribute("answers", currJob.getAnswers());
        model.addAttribute("qsAndAs", qandAsRepository.findAllByJob(currJob));
//        model.addAttribute("job", currJob);
//        model.addAttribute("qsAndAs", new QsAndAs(currJob, "q1", "a1"));
//        model.addAttribute("qsAndAs", qandAsRepository.findAllByJob(currJob));
        return "interviewform";
    }

    @PostMapping("/processinterview")
    public String processInterview(Set<String> answers
//            QsAndAs qsAndAs
//                              @RequestParam(name="interviewDate") String interviewDate
    ) {
//        try {
//            String pattern = "yyyy-MM-dd";
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
//            String formattedDate = interviewDate.substring(0);
//            Date realDate = simpleDateFormat.parse(formattedDate);
////            job.setInterviewDate
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
////
        System.out.println("After processing interview form: ");
//        System.out.println(qsAndAs.getAnswer());
//        System.out.println(qsAndAs.getQuestion());
//            qandAsRepository.save(qsAndAs);
//            jobRepository.save(job);
            return "redirect:/";
        }



    @RequestMapping("/apply/{id}")
    public String applyJob(@PathVariable("id") long id, Model model){
        model.addAttribute("job", jobRepository.findById(id).get());
        Job job = jobRepository.findById(id).get();
        job.setCurStatus(StaticData.Status.SUBMITTED);
        User user = userService.getUser();
//        user.getApplied_jobs().add(job);

        // JA 10-23-19
        StaticData.AddAppliedJobUserID(job, user.getId());

        int count = user.getJobs().size();
        System.out.println("JobController: applyJob: Number of Jobs in User: " + user.getUsername() + " Count: " + count);

        user.getMatches();  // Evaluate all jobs w/Status == SUBMITTED
        if (job.getCurStatus() == StaticData.Status.PENDING_INTERVIEW) {
            System.out.println("applyJob: " + "Interview is pending.");
            // send an email or popup to user to go to myPage to schedule an interview
            // during an available window.  When they go to myPage they will see cards for each job they have
            // applied to.  Each will have a button to apply for an interview
            // if the interview has not been scheduled - add PENDING_SCHEDULED_INTERVIEW
        }
        else {
            System.out.println("applyJob: " + "Candidate was rejected");
        }
//        return "show";
        return "redirect:/";
    }

    @RequestMapping("/detail/{id}")
    public String showJob(@PathVariable("id") long id, Model model){
        model.addAttribute("job", jobRepository.findById(id).get());
        return "show";
    }

    @RequestMapping("/update/{id}")
    public String updateJob(@PathVariable("id") long id, Model model){
        model.addAttribute("job", jobRepository.findById(id).get());
        return "jobform";
    }

    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id) {
        jobRepository.deleteById(id);
        return "redirect:/";
    }

}
