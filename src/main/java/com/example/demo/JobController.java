package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
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

    @Autowired
    JobService jobService;

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
////        model.addAttribute("users", userRepository.findAll());
        if (userService.getUser() != null) {
            long uID = userService.getUser().getId();
            model.addAttribute("user_id", userService.getUser().getId());
            model.addAttribute("user", userService.getUser());
            model.addAttribute("jobs", StaticData.getJobsByApplicantID(uID));
            model.addAttribute("statusPendingInterview", StaticData.Status.PENDING_INTERVIEW);
            model.addAttribute("statusPendingScheduledInterview", StaticData.Status.PENDING_SCHEDULED_INTERVIEW);
            model.addAttribute("statusRejected", StaticData.Status.REJECTED);

            boolean currentJobApplications = StaticData.doesUserHaveAnyPriorAppliedJobs(uID);
            String msgStr = "";
            if (currentJobApplications){
                String testStr = StaticData.doesUserHaveAnyInterviewsScheduled(uID);
                if (!testStr.equals("")) {
                    msgStr = "Please check on the status of your current Job Applications.\n" +
                            "You have an interview scheduled to start at: " + testStr + "\n" +
                            "Please click the interview button within 30 minutes of this time.";
                }
            }
            model.addAttribute("userMessage", msgStr);
            model.addAttribute("mapOfStatus", StaticData.perJobStatus);
            model.addAttribute("mapOfTimes", StaticData.selectedTimes);

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
        // dropdown of jobType Choices
        ArrayList<String> jobTypes = new ArrayList<>();
        jobTypes.add("Developer");
        jobTypes.add("Mobile");
        jobTypes.add("Cloud");
        model.addAttribute("jobTypes", jobTypes);

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
        StaticData.perJobStatus.put(job.getId(),StaticData.Status.NOT_SUBMITTED);
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
        Job jobCreator = jobRepository.findJobById(id);
        jobCreator.setCurStatus(StaticData.Status.PENDING_OFFER);
        jobCreator.setEmployerName("bob"); // testing failed switched to static variables
//        jobRepository.save(jobCreator);
        //jobService.update(jobCreator);

        StaticData.perJobStatus.put(jobCreator.getId(),StaticData.Status.PENDING_OFFER);

        StaticData staticData = new StaticData();
//        QuestionCreationDto questions = new QuestionCreationDto();
        ArrayList<String> questions = new ArrayList<>();

        questions.addAll(staticData.getBehavioralQuestions());
        if(jobCreator.getJobType().equals("Developer")) { // first job
            staticData.setDeveloperQuestions();
            questions.addAll(staticData.getTechQs());
        }
        else if(jobCreator.getJobType().equals("Mobile")){
            staticData.setMobileQuestions();
            questions.addAll(staticData.getTechQs());
        }
        else if(jobCreator.getJobType().equals("Cloud")){
            staticData.setCloudQuestions();
            questions.addAll(staticData.getTechQs());
        }

        model.addAttribute("currJob" , currJob);
        model.addAttribute("questions",questions);
        User user = userService.getUser();
        model.addAttribute("job", jobCreator);

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
    public String processInterview(
            @ModelAttribute("userAnswer") UserAnswersDto userAnswersDto
//                                   @ModelAttribute Job job
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

//        Job job = jobRepository.findJobById(userAnswersDto.jobId);
//
////        jobService.update(job);
//        jobRepository.save(job);


        Job job = jobRepository.findJobById(userAnswersDto.jobId);
//        job.setCurStatus(StaticData.Status.PENDING_OFFER);
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
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");

        LocalDateTime tempTime = LocalDateTime.now();
        String formatDateTime = tempTime.format(formatter);
        dateOptions.add(formatDateTime);

        for(int i = 1; i <= 14; i+=4) {
           // add conditionals if there's time...
            tempTime = tempTime.withHour(9);
            tempTime = tempTime.withMinute(0);
            formatDateTime = tempTime.format(formatter);
            dateOptions.add(formatDateTime);

            tempTime = tempTime.withHour(12);
            tempTime = tempTime.withMinute(30);
            formatDateTime = tempTime.format(formatter);
            dateOptions.add(formatDateTime);

            tempTime = tempTime.plusDays(4);
        }

        model.addAttribute("currJob" , jobRepository.findJobById(id));
        model.addAttribute("dateOptions", dateOptions);

        ScheduleInterview sI = new ScheduleInterview();
        sI.setUserId(userService.getUser().getId());
        sI.setJobId(jobRepository.findJobById(id).getId());
        model.addAttribute("selDate" , sI);
        return "scheduleform";
    }

    @PostMapping("/process_selectdate")
    public String processDate(@ModelAttribute ScheduleInterview selDate){
        //Job job = jobRepository.findJobById(selDate.getJobId());
        //job.setCurStatus(StaticData.Status.PENDING_SCHEDULED_INTERVIEW);

        StaticData.perJobStatus.put(selDate.getJobId(),StaticData.Status.PENDING_SCHEDULED_INTERVIEW);
        StaticData.selectedTimes.put(selDate.getJobId(),selDate.getStringSelectTime());

        //Continiue later at home
        String tmp = selDate.getStringSelectTime();
      //  selDate.setSelectedTime(LocalDateTime.parse(tmp));
      //  ScheduleInterview.userScheduleDates.add(selDate);
        System.out.println("selected time: " + tmp);
        //selDate.setStringSelectTime();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy hh:mm a");
        selDate.setSelectedTime(LocalDateTime.parse(selDate.getStringSelectTime(), formatter));


        //job.setInterviewDateTime(selDate.getSelectedTime());
       // job.setCurStatus(StaticData.Status.PENDING_SCHEDULED_INTERVIEW);
//        jobService.update(job);
//        jobRepository.save(job);
        System.out.println("After processing interview form: ");
        System.out.println("Selected time is: " + selDate.getStringSelectTime());


        return "redirect:/mypage";
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
        StaticData.perJobStatus.put(job.getId(),StaticData.Status.SUBMITTED);

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
        //Joe added
        ArrayList<String> jobTypes = new ArrayList<>();
        jobTypes.add("Developer");
        jobTypes.add("Mobile");
        jobTypes.add("Cloud");
        model.addAttribute("jobTypes", jobTypes);
        return "jobform";
    }

    @RequestMapping("/delete/{id}")
    public String delCourse(@PathVariable("id") long id) {
        jobRepository.deleteById(id);
        return "redirect:/";
    }

}
