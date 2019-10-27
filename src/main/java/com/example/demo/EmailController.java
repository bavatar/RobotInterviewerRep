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
    UserService userService;

    @Autowired
    JobRepository jobRepository;


    // This may require the Job ID as well to inform the hiring manager which job they are appealing
    @RequestMapping("/appeal/{id}")
    public String appealForm(@PathVariable("id") long id, Model model) {
        model.addAttribute("user_id", userService.getUser().getId());
        model.addAttribute("job", jobRepository.findById(id));
//        Job currJob = jobRepository.findById(id).get();
        return "emailform";
            }

    @RequestMapping(value="/process_appeal", method=RequestMethod.POST)
    public String processForm(){
        return "redirect:/mypage";
    }

    @RequestMapping(value = "/appeal_email", method = RequestMethod.POST)
    @ResponseBody
    String appealForm(Model model) {
        try {
            sendEmail();
         return "Email sent!";
        } catch (Exception ex) {
            return "Error in sending email: " + ex;
        }
//
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

    @RequestMapping(value = "/chat_email", method = RequestMethod.POST)
    @ResponseBody
    String chatForm(Model model) {
        try {
            sendEmailQA();
          return "Email Sent!";
        } catch (Exception ex) {
            return "Error in sending email: " + ex;
        }
//        return "redirect:/";
//        return "mypage";
    }

    private void sendEmailQA() throws Exception {
        MimeMessage message = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("user@email.com.com");
        helper.setTo("no-reply@deadpool.com");
        helper.setText("<html><body>" + message + "</html></body>", true);
        helper.setSubject("Questionnaire Responses");

        sender.send(message);
    }

    @RequestMapping(value="/process_chat", method=RequestMethod.POST)
    public String processQAForm(){
        return "redirect:/mypage";
    }
}
