package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.mail.internet.MimeMessage;

@Controller
public class EmailController {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private UserService userService;

    @Autowired
    JobRepository jobRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    QandAsRepository qandAsRepository;

    @RequestMapping("/submit_email")
    @ResponseBody
    String resumeForm(Model model) {
        try {
            sendEmailAttach();
//            return "listjobs";
        } catch (Exception ex) {
            return "Error in sending email: " + ex;
        }
        return "listjobs";
    }

    private void sendEmailAttach() throws Exception {
        MimeMessage message = sender.createMimeMessage();

        // Enable the multipart flag!
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setFrom("user@email.com");
        helper.setTo("no-reply@deadpool.com");
        helper.setText("Your resume has been submitted");
        helper.setSubject("Application");

        ClassPathResource file = new ClassPathResource("resume.txt");
        helper.addAttachment("resume.txt", file);

        sender.send(message);
    }

    // This may require the Job ID as well to inform the hiring manager which job they are appealing
    @RequestMapping("/appeal/")
    public String appeal(@RequestParam("id") long id, Model model) {
        model.addAttribute("mail", new SendMail());
        model.addAttribute("job", jobRepository.findById(id).get());

        System.out.println("/appeal : " + jobRepository.findById(id).get().getEmployerName());
        System.out.println("/appeal : " + jobRepository.findJobById(id).getEmployerName());

       // sendEmail(id);

        if (userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        }
        else{
            System.out.println("AppController:jobList:userService.getUser(): is null");
        }

        return "emailform";
    }

    @RequestMapping(value = "/appeal_email", method = RequestMethod.POST)
//    @ResponseBody
    public String appealForm(@ModelAttribute("job") Job job) {
        try {
            sendEmail(job);
           // sendEmail();
//            return "Email Sent!";
        } catch (Exception ex) {
            return "Error in sending email: " + ex;
        }
//        return "redirect:/";
        return "redirect:/mypage";
    }

    private void sendEmail(Job job) throws Exception {

        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        User user = userService.getUser();

        helper.setFrom(user.getEmail());
        System.out.println(job.getEmployerEmail());
        helper.setTo(job.getEmployerEmail());
       // UserAnswersDto u = UserAnswersDto.getUserAnswerFromArr(user.getId());
        helper.setText("<html><body>" + message + "</html></body>", true);
        helper.setSubject("Appeal");

        sender.send(message);
    }

}
