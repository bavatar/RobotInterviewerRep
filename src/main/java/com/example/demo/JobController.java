package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
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
        Date tempDate = new Date();
        job.setPostedDate(tempDate);
        jobRepository.save(job);
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

    @PostConstruct
    public void run(){
        // test to see if data already exists
//        if (roleRepository.findAll() == null) {
        try {
            if (roleRepository.count() > 0) {
                System.out.println("DataLoader: Accounts exists. Skip DataLoader");
                return;
            } else {
                System.out.println("DataLoader: ADMIN account does not exist.");
            }
        } catch (Exception e) {
            System.out.println("DataLoader: An error occurred trying to find the ADMIN account.");
            System.out.println("DataLoader: " + e.toString());
        }

        roleRepository.save(new Role("USER"));
        roleRepository.save(new Role("ADMIN"));
        roleRepository.save(new Role("SUPERVISOR"));

        Role adminRole = roleRepository.findByRole("ADMIN");
        Role userRole = roleRepository.findByRole("USER");
        Role supervisorRole = roleRepository.findByRole("SUPERVISOR");
        //roleRepository.findAll();

        // Test Jobs
        ArrayList<String> testKeywords = new ArrayList<String>(){{
            add("key1");
            add("key2");
            add("key3");
        }};

        HashMap<Integer, ArrayList<String>> testQsAndAs = new HashMap<>();
        testQsAndAs.put(1, new ArrayList<String>(){{
            add("Question 1");
            add("Answer 1");
        }});


        //End of Test Jobs

        User user = new User("jim@jim.com", "password", "Jim",
                "Jimmerson", true, "jim");

        Job testJob = new Job("title1", "0123456789", "employer",
                "employer@gmail.com", "description1",
                testKeywords, testQsAndAs, new Date(), user);

        testJob.setCurStatus(StaticData.Status.NOT_SUBMITTED);
        Set<Job> testSetJobs = new HashSet<Job>();
        testSetJobs.add(testJob);

        user.setResume(new StringBuffer("testString"));
        user.setRoles(Arrays.asList(userRole));

        user.setJobs(testSetJobs);
//        try {
        jobRepository.save(testJob);
        userRepository.save(user);
//        } catch (Exception e){
//            e.printStackTrace();
//            return;
//        }

        // Tests
        user = userRepository.findByUsername(user.getUsername());
        System.out.println("DataLoader: User Name: " + user.getUsername() + " Password: " + user.getPassword());
        System.out.println("DataLoader: User email: " + user.getEmail());
        user = userRepository.findByUsername(user.getUsername());

        user = new User("admin@admin.com", "password", "Admin",
                "User", true, "admin");
        user.setRoles(Arrays.asList(adminRole));

        //save job to user
        user.setJobs(testSetJobs);
        testJob.setUser(user);

        userRepository.save(user);
        jobRepository.save(testJob);

        String tesStr = "  Way Good ";
        tesStr = tesStr.trim();
        System.out.println(tesStr);
    }
}
