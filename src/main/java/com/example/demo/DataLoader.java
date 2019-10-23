package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;


    @Autowired
    JobRepository jobRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public ArrayList<String> kWords(String str){
        ArrayList<String> arrList = new ArrayList();
        String[] kwords = str.split(",");
        for (String s: kwords){
            arrList.add(s.trim());
        }
        return arrList;
    }

    @Override
    public void run(String...strings) throws Exception {
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

        User user = new User("jim@jim.com", "password", "Jim",
                "Jimmerson", true, "jim");
        user.setRoles(Arrays.asList(userRole));
        userRepository.save(user);

        // Tests
        user = userRepository.findByUsername(user.getUsername());
        System.out.println("DataLoader: User Name: " + user.getUsername() + " Password: " + user.getPassword());
        System.out.println("DataLoader: User email: " + user.getEmail());
        user = userRepository.findByUsername(user.getUsername());

        user = new User("admin@admin.com", "password", "Admin",
                "User", true, "admin");
        user.setRoles(Arrays.asList(adminRole));
        userRepository.save(user);
        ArrayList<String> keyWords = new ArrayList<>();

        keyWords = kWords("CSS, Design, Engineering, Full Stack, HTML, JavaScript, Ruby On Rails, Web Design, Web Development, Software Development, Team Management");
        Job job = new Job();
        job.setKeywords(keyWords);
        job.setCurStatus(StaticData.Status.NOT_SUBMITTED);
        job.setDescription("This is a detailed description");
        job.setTitle("Senior Java Developer");
        job.setUser(user);
        job.setPhone("301-879-6524");
        job.setEmployerEmail("jj@test.com");
        job.setEmployerName("Amazon");
        Date tempDate = new Date();
        job.setPostedDate(tempDate);
        jobRepository.save(job);

        System.out.println("DataLoader: postedDate: " + job.getPostedDate());

        // Add another job with the same user
        keyWords = kWords("Programming Dev, Mobile apps, iPhone, Android Development, PhoneGap Software Development, Mobile Development, Ios App Development, Mobile Programming Languages");
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
        jobRepository.save(job);

//        String tesStr = "  Way Good, Better than Good ";
//        ArrayList<String> testArray = kWords(tesStr);
//        System.out.println("DataLoader: Test kWords(0) - (1)" + testArray.get(0) + " - " + testArray.get(1));
    }
}