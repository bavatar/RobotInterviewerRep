package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.mail.internet.MimeMessage;

@Controller
public class EmailController {

    @Autowired
    private JavaMailSender sender;

    @Autowired
    private UserService userService;

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
    @RequestMapping("/appeal")
    public String appeal(Model model) {
        model.addAttribute("mail", new SendMail());
        if (userService.getUser() != null) {
            model.addAttribute("user_id", userService.getUser().getId());
        }
            else{
                System.out.println("AppController:jobList:userService.getUser(): is null");
            }
                return "emailform";
    }

    @RequestMapping(value = "/appeal_email", method = RequestMethod.POST)
    @ResponseBody
    String appealForm(Model model) {
        try {
            sendEmail();
//            return "Email Sent!";
        } catch (Exception ex) {
            return "Error in sending email: " + ex;
        }
//        return "redirect:/";
        return "listjobs";
    }

    private void sendEmail() throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("user@email.com.com");
        helper.setTo("no-reply@deadpool.com");
        helper.setText("<html><body>" + message + "</html></body>", true);
        helper.setSubject("Appeal");

        sender.send(message);
    }

}
