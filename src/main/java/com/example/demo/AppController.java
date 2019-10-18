package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@Controller
public class AppController {
    @Autowired
    JobRepository jobRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    RoleRepository userRepository;

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

    @GetMapping("/add")
    public String addJob(Model model){
        if (userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        } else {
            System.out.println("AppController:jobList:userService.getUser(): is null");
        }

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
    public String processJob(@ModelAttribute Job job) {
        Date tempDate = new Date();
        job.setPostedDate(tempDate);
        jobRepository.save(job);
        job.setUser(userService.getUser());
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
