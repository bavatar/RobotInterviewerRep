package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
        return "mypage";
    }

    @RequestMapping("/manageresumes")
    public String manageResumes(Model model) {
        model.addAttribute("user", userService.getUser());
//        model.addAttribute(("resName"))
        return "manageresumes";
    }

    @PostMapping("/manageresumes")
    public String processResumes(@ModelAttribute User user, @RequestParam("file") MultipartFile file, Model model){
        BufferedReader br = null;
        String line = "";
        String fileName = "";
        String fName = "";
        String resStr = "";
        String dir = "";
        String fileContents = "";

        user = userService.getUser();

        dir = System.getProperty("user.dir");   // C:\Users\John\IdeaProjects\CloudinaryUpload
        System.out.println("dir: " + dir.replace("\\", "/"));
        dir = dir.replace("\\", "/");

        if(file.isEmpty()) {
            return "redirect:/manageresumes";
        }

        try {
            fName = file.getOriginalFilename();

            if (fName.contains("txt")) {
                fileName = dir + "/" + fName;
                String resume_name = fName;
                fName = fName.replace(".txt", "");

                System.out.println("fileName: " + fileName);    //fileName: C:/Users/John/IdeaProjects/CloudinaryUpload/Sr_AWS_Cloud_Engr.txt
                br = new BufferedReader(new FileReader(fileName));
                while ((line = br.readLine()) != null) {
                    System.out.println("processActor: line= " + line);
                    fileContents += line;
                }
                user.setResume(fileContents);
                user.setResume_name(fName);
                userRepository.save(user);
            }
            else {
                return "redirect:/manageresumes";   // A *.txt file is required
            }
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/manageresumes";
        }
        return "redirect:/";
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
//    public String processJob(@ModelAttribute Job job, BindingResult result) {
    public String processJob(@Valid @ModelAttribute("job") Job job, BindingResult result, Model model) {
//        if(result.hasErrors()){
//            System.out.println("processJob: Cannot continue as there have be errors with result.");
//            return "jobform";
//        }
        //Add new job... i.e new identity
        job.setUser(userService.getUser());

        job.setCurStatus(StaticData.Status.NOT_SUBMITTED);

        Date tempDate = new Date();
        job.setPostedDate(tempDate);
        jobRepository.save(job);

        // Add Questions & Answers?  Test
        QsAndAs testQsAndAs = new QsAndAs();
        testQsAndAs.setJob(job);
        testQsAndAs.setQuestion("1. Talk about a time when you had to work closely with someone whose personality " +
                "was very different from yours." +                                                          "\n"
                + "2. Tell me about a time when you made sure a customer was pleased with your service." +  "\n"
                + "3. How do you reverse an array in place in Java?"                                     +  "\n"
                + "4. How do you find the largest and smallest number in an unsorted integer array?");
        testQsAndAs.setAnswer("Answer 1");
        qandAsRepository.save(testQsAndAs);

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

    @RequestMapping("/addinterview/")
    public String showInterviewForm(
            @RequestParam("id") long id,
            Model model){
        Job currJob = jobRepository.findByUser(userService.getUser());
        StaticData staticData = new StaticData();
//        QuestionCreationDto questions = new QuestionCreationDto();
        ArrayList<String> questions = new ArrayList<>();


        questions.addAll(staticData.getBehavioralQuestions());
        if(id == 7) { // first job
            staticData.setDeveloperQuestions();
            questions.addAll(staticData.getTechQs());
        }




        model.addAttribute("currJob" , currJob);
        model.addAttribute("questions",questions);
        User user = userService.getUser();
        model.addAttribute("job", currJob);

//        UserAnswersDto uA = new UserAnswersDto();
//        HashMap<Long, String> jobAns = new HashMap<Long, String>();
//        jobAns.put(currJob.getId(),"");
//        uA.setAnswers(jobAns);
//        UserAnswersDto.userAnswersArr.add(uA);

        UserAnswersDto uA = new UserAnswersDto();
        uA.setUserId(user.getId());
        uA.setJobId(jobRepository.findJobById(id).getId());

        model.addAttribute("userAnswer", uA);
//        Job currJob = jobRepository.findJobById(id);
//        model.addAttribute("questions", currJob.getQuestions());
//        model.addAttribute("answers", currJob.getAnswers());
//        model.addAttribute("qsAndAs", qandAsRepository.findAllByJob(currJob));
//        model.addAttribute("job", currJob);
//        model.addAttribute("qsAndAs", new QsAndAs(currJob, "q1", "a1"));
//        model.addAttribute("qsAndAs", qandAsRepository.findAllByJob(currJob));
        return "interviewform";
    }

    @PostMapping("/processinterview")
    public String processInterview(@ModelAttribute UserAnswersDto userAnswersDto
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

        Job job = jobRepository.findJobById(userAnswersDto.jobId);
        job.setCurStatus(StaticData.Status.PENDING_OFFER);
        System.out.println("After processing interview form: ");
        UserAnswersDto.userAnswersArr.add(userAnswersDto);

//        System.out.println(qsAndAs.getAnswer());
//        System.out.println(qsAndAs.getQuestion());
//            qandAsRepository.save(qsAndAs);
//            jobRepository.save(job);
        return "redirect:/mypage";
    }


    @RequestMapping("/selectdate")
    public String showScheduleForm(
            @RequestParam("id") long id, Model model)
    {
        ArrayList<String> dateOptions = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy HH:mm");

        LocalDateTime tempTime = LocalDateTime.now();
        String formatDateTime = tempTime.format(formatter);
        dateOptions.add(formatDateTime);
        for(int i =0; i <= 14; i++) {
            tempTime.withHour(9);
            tempTime.withMinute(0);
            formatDateTime = tempTime.format(formatter);
            dateOptions.add(formatDateTime);

            tempTime.withHour(12);
            tempTime.withMinute(0);
            formatDateTime = tempTime.format(formatter);
            dateOptions.add(formatDateTime);

            tempTime.withHour(15);
            tempTime.withMinute(30);
            formatDateTime = tempTime.format(formatter);
            dateOptions.add(formatDateTime);

            tempTime.plusDays(1);
        }

        model.addAttribute("dateOptions", dateOptions);

        ScheduleInterview sI = new ScheduleInterview();
        sI.setUserId(userService.getUser().getId());
        sI.setJobId(jobRepository.findJobById(id).getId());
        model.addAttribute("selDate" , sI);
        return "scheduleform";
    }


    @RequestMapping("/apply/{id}")
    public String applyJob(@PathVariable("id") long id, Model model){
        model.addAttribute("job", jobRepository.findById(id).get());
        Job job = jobRepository.findById(id).get();
        job.setCurStatus(StaticData.Status.SUBMITTED);
        User user = userService.getUser();
        String tempRes = user.getResume();
        if ((tempRes == null) || (tempRes == ""))
        {
            System.out.println("Error: the user cannot apply for a job until a resume is selected.");
            return "redirect:/";
        }
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
