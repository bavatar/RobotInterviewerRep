
package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.header.writers.frameoptions.StaticAllowFromStrategy;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    QandAsRepository qandAsRepository;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public ArrayList<String> kWords(String str) {
        ArrayList<String> arrList = new ArrayList();
        String[] kwords = str.split(",");
        for (String s : kwords) {
            arrList.add(s.trim());
        }
        return arrList;
    }

    @Override
    public void run(String... strings) throws Exception {
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

//        User user = new User("jim@jim.com", "password", "Jim",
//                "Jimmerson", true, "jim");
        // First User
        User user = new User();
        user.setEmail("bavatar@hotmail.com");
        user.setPassword("password");
        user.setFirstName("jim");
        user.setLastName("jimmerson");
        user.setEnabled(true);
        user.setUsername("jim");
        //StringBuilder res = new StringBuilder(10000);
        String res = "Software/Web Developer I am a freelance full stack web engineer who loves a good web design as much as the next " +
                "guy I am proficient in HTML, CSS, JavaScript, and Ruby/Rails. I value communication and punctuality amongst team members highly, " +
                "so I am very reliable and a hard worker. Keywords: CSS, Design, Engineering, Full Stack, HTML, JavaScript, Ruby On Rails, " +
                "Web Design, Web Development, Software Development, Team Management, iPhone, web development";
        user.setResume(res);
        user.setResume_name("Web_Developer");
        user.setJobs(new HashSet<Job>());
        user.setRoles(Arrays.asList(userRole));
        userRepository.save(user);

        // Tests
        user = userRepository.findByUsername(user.getUsername());
        System.out.println("DataLoader: User Name: " + user.getUsername() + " Password: " + user.getPassword());
        System.out.println("DataLoader: User email: " + user.getEmail());
        user = userRepository.findByUsername(user.getUsername());

        // Second User
        user = new User();
        user.setEmail("admin@gmail.com");
        user.setPassword("password");
        user.setFirstName("Steve");
        user.setLastName("Emerson");
        user.setEnabled(true);
        user.setUsername("admin");
        res = "Scopic Software offers high-quality and affordable web development and design services, " +
                "providing customized solutions that best fit your business's unique needs. Having over 12 years of " +
                "experience in the software industry, we have helped hundreds of businesses across different industries " +
                "to succeed, by delivering exceptional custom web development services. We guarantee the visually " +
                "engaging, highly interactive and responsive front-end work you need to conquer the hearts of your " +
                "ever-demanding customers. As no front-end work is of use without a great back-end work, we help you " +
                "develop the exceptional back-end that your application deserves to have. We are experts in the " +
                "different technology stacks, starting from the PHP/MySQL solution and full-stack JavaScript solutions " +
                "with relational and non-relational databases to highly scalable cloud serverless solutions based on " +
                "Amazon Web Services. Check out our website portfolio to get the feel of our work and let's connect " +
                "to see how we can help you build the unique web application of your dreams!" +
                "Key Words: web development, customized solutions, software develpment, full stack, " +
                "PHP, MySQL, JavaScript, RDBMS, serverless, Cloud, Amazon Web Services";
        user.setResume(res);
        user.setResume_name("AWS_Developer");
        user.setJobs(new HashSet<Job>());
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);

        // Third User
        user = new User();
        user.setEmail("user3@gmail.com");
        user.setPassword("password");
        user.setFirstName("Joe");
        user.setLastName("Emerson");
        user.setEnabled(true);
        user.setUsername("Joe");
        res = "Scopic Software offers high-quality and affordable web development and design services, " +
                "providing customized solutions that best fit your business's unique needs. Having over 12 years of " +
                "experience in the software industry, we have helped hundreds of businesses across different industries " +
                "to succeed, by delivering exceptional custom web development services. We guarantee the visually " +
                "engaging, highly interactive and responsive front-end work you need to conquer the hearts of your " +
                "ever-demanding customers. As no front-end work is of use without a great back-end work, we help you " +
                "develop the exceptional back-end that your application deserves to have. We are experts in the " +
                "different technology stacks, starting from the PHP/MySQL solution and full-stack JavaScript solutions " +
                "with relational and non-relational databases to highly scalable cloud serverless solutions based on " +
                "Amazon Web Services. Check out our website portfolio to get the feel of our work and let's connect " +
                "to see how we can help you build the unique web application of your dreams!" +
                "Key Words: web development, customized solutions, software develpment, full stack, " +
                "PHP, MySQL, JavaScript, RDBMS, serverless, Cloud, Amazon Web Services";
        user.setResume(res);
        user.setResume_name("Software_Developer");
        user.setJobs(new HashSet<Job>());
        user.setRoles(Arrays.asList(userRole));
        userRepository.save(user);

        // First Job
        String keyWords = "CSS, Design, Engineering, Full Stack, HTML, JavaScript, Ruby On Rails, Web Design, Web Development, Software Development, Team Management";
        Job job = new Job();
        job.setKeywords(keyWords);
        job.setCurStatus(StaticData.Status.NOT_SUBMITTED);
        job.setDescription("This is a detailed description");
        job.setTitle("Senior Java Developer");
        job.setUser(user);
        job.setPhone("301-879-6524");
        job.setEmployerEmail("bavatar@hotmail.com");
        job.setEmployerName("Amazon");
        Date tempDate = new Date();
        job.setPostedDate(tempDate);

        StaticData staticData = new StaticData();
        HashSet<String> questions = new HashSet<>();
        questions.addAll(staticData.getBehavioralQuestions());
        staticData.setDeveloperQuestions();
        questions.addAll(staticData.getTechQs());
        job.setQuestions(questions);
        job.setAnswers(new HashSet<>());

        // test for Updating Job => works
        job.setPhone("202-679-5543");
        job.setJobType("Developer");
        jobRepository.save(job);

//        staticData.setDeveloperQuestions();
//        Set<QsAndAs> testQAList = new HashSet<>();
//
//
//        for(int i = 1; i <= 4; i++) {
//            QsAndAs testQsAndAs = new QsAndAs();
//            testQsAndAs.setJob(job);
//            testQsAndAs.setQuestion(staticData.getBehavioralQuestions().get(i-1));
//            testQsAndAs.setAnswer("Answer");
//            qandAsRepository.save(testQsAndAs);
//            testQAList.add(testQsAndAs);
//        }
//        for(int i = 1; i <= 4; i++) {
//            QsAndAs testQsAndAs = new QsAndAs();
//            testQsAndAs.setJob(job);
//            testQsAndAs.setQuestion(staticData.getTechQs().get(i-1));
//            testQsAndAs.setAnswer("Answer");
//            qandAsRepository.save(testQsAndAs);
//            testQAList.add(testQsAndAs);
//        }


//        QsAndAs testQaAndAs2 = new QsAndAs();
//        testQaAndAs2.setJob(job);
//        testQaAndAs2.setQuestion("Question 2");
//        testQaAndAs2.setAnswer("Answer 2");
//        qandAsRepository.save(testQaAndAs2);


//        testQAList.add(testQaAndAs2);

//        job.setQuestionsAndAnswers(testQAList);

//        Set<QsAndAs> testQAList = new HashSet<>();
//        testQAList.add(testQsAndAs);


//        for(QsAndAs q : testQAList) {
//            System.out.println(q.getQuestion() + " " + q.getAnswer());
//        }

        System.out.println("DataLoader: postedDate: " + job.getPostedDate());

        // Second job with the same user
        keyWords = "Programming Dev, Mobile apps, iPhone, Android Development, " +
                "PhoneGap Software Development, Mobile Development, Ios App Development, " +
                "Mobile Programming Languages";
        job = new Job();
        job.setKeywords(keyWords);
        job.setCurStatus(StaticData.Status.NOT_SUBMITTED);
        job.setDescription("This is a detailed description");
        job.setTitle("Senior Solutions Architect");
        job.setUser(user);
        job.setPhone("301-345-6524");
        job.setEmployerEmail("bb@test.com");
        job.setEmployerName("Ascension Enterprizes");
        tempDate = new Date();
        job.setPostedDate(tempDate);
        job.setJobType("Mobile");
        jobRepository.save(job);
    }
}
