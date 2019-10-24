package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

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
//  model.addAttribute("cars", carRepository.findAll());
//  model.addAttribute("categories", categoryRepository.findAll());
    @RequestMapping("/mypage")
    public String myPage(Model model){
//        model.addAttribute("jobs", jobRepository.findAll());
//        model.addAttribute("users", userRepository.findAll());
        if (userService.getUser() != null) {
            long uID = userService.getUser().getId();
            model.addAttribute("user_id", userService.getUser().getId());
            model.addAttribute("user", userService.getUser());
            model.addAttribute("jobs", StaticData.getJobsByApplicantID(uID));
        }
        //User user = userRepository.findById(userService.getUser().getId());
//        User user = userRepository.findById(user_id);
        return "mypage";
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
        job.setUser(userService.getUser());
        job.setEmployerEmail("jj@test.com");
        job.setEmployerName("Amazon");
        Date tempDate = new Date();
        job.setPostedDate(tempDate);
        jobRepository.save(job);
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
