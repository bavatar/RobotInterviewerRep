package com.example.demo.Testing;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TestController {

    @RequestMapping("/testpage")
    public String testPage(Model model){
        return "testpage";
    }

}
