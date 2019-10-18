package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.security.Principal;

@Controller
public class HomeController {
    // Added for ToDo
//    @Autowired
//    TodoRepository todoRepository;

    @Autowired
    private UserService userService;

    // added for 4.06 needed here? also in UserService
    @Autowired
    RoleRepository roleRepository;

    // Added for 4.05
    @Autowired
    UserRepository userRepository;

    @Autowired
    private UserService jobRepository;

//    // Added for ToDo
//    @RequestMapping("/")
//    public String listTasks(Model model){
//        model.addAttribute("todos", todoRepository.findAll());
//        return "list";
//    }

    @RequestMapping("/delete_profile/{id}")
    public String delUser(@PathVariable("id") long id, Model model){
        System.out.println("HomeController: Delete user with id: " + id);
        try {
            userRepository.deleteById(id);
        }
        catch (Exception e){
            System.out.println("HomeController:delUser: " + e.getMessage());
        }

        if (userRepository.existsById(id)){
            System.out.println("HomeController: delUser: User exists with id: " + id);
        }
        else {
            System.out.println("HomeController: delUser: User does Not exist with id: " + id);
        }
        return "redirect:/showusers";
        // return "index";
    }

    // Added for ADMIN Role
    @RequestMapping("/showusers")
    public String listUsers(Model model){
        model.addAttribute("users", userRepository.findAll());
//        model.addAttribute("jobs", jobRepository.findAll());
        try {
            if (userService.getUser() != null) {
                model.addAttribute("user_id", userService.getUser().getId());
            }
        } catch (Exception e){
            System.out.println("HomeController: listUsers: " + "userService.getUser() == null");
        }
        return "showusers";
    }
//    // Added for ADMIN Role
//    @RequestMapping("/showusers")
//    public String listUsers(Model model){
//        model.addAttribute("users", userRepository.findAll());
////        model.addAttribute("jobs", jobRepository.findByEmail("test"));
//        return "showusers";
//    }

//    @PostConstruct
//    public void postConstruct() {
//        // Alternative to DataLoader - not using it now
////        if (roleRepository.findAll()== null){
////            roleRepository.save(new Role("USER"));
////            roleRepository.save(new Role("ADMIN"));
////        }
//    }

//    // Added for ToDo
//    @GetMapping("/add")
//    public String todoForm(Model model){
//        model.addAttribute("todo", new Todo());
//        return "todoform";
//    }

    // Added for ToDo
//    @PostMapping("/process")
//    public String processForm(@Valid Todo todo, BindingResult result){
//        if (result.hasErrors()){
//            return "todoform";
//        }
//        todoRepository.save(todo);
//        return "redirect:/";
//    }

    @PostMapping("/process_profile")
    public String processProfile(@Valid
              @ModelAttribute("user") User user, BindingResult result,
              Model model){
        model.addAttribute("user", user);
        System.out.println("HomeController: Save Change to Admin role 0" + user.getRoles().toString());
        if(result.hasErrors()){
            return "updateprofile";
        }
        else {
            for (Role r:user.getRoles()){
                if (r.getRole().equals("ADMIN")){
                    System.out.println("HomeController: Save Change to Admin role");
                    //user.getRoles().add(new Role("ADMIN"));
                    userService.saveAdmin(user);
                    model.addAttribute("message", "User Account Updated to Admin Role");
                    // test
//                    if (r.getRole().equalsIgnoreCase("ADMIN")) {
//                        user.getRoles().add(new Role("USER"));
//                    }
                    return "redirect:/showusers";
                }
                else if (user.getRoles().toString().contains("USER")){
                    System.out.println("HomeController: Save Change to User role");
                    userService.saveUser(user);
                    model.addAttribute("message", "User Account Updated to User Role");
                    return "redirect:/showusers";
                } else if (user.getRoles().toString().contains("SUPERVISOR")){
                    System.out.println("HomeController: Save Change to Supervisor Role");
                    userService.saveSupervisor(user);
                    model.addAttribute("message", "User Account Updated to Supervisor Role");
                    return "redirect:/showusers";
                }
            }

            if (user.getRoles().toString().contains("USER")){
                userService.saveUser(user);
            }
            else if (user.getRoles().toString().contains("ADMIN")){
                System.out.println("HomeController: Save Change to Admin role 1");
                userService.saveAdmin(user);
            } if (user.getRoles().toString().contains("SUPERVISOR")){
                userService.saveSupervisor(user);
            }
            model.addAttribute("message", "User Account Updated");
        }
//        return "redirect:/showusers";
        return "redirect:/";
//        return "showusers";
    }

    // Added for ToDo
//    @RequestMapping("/detail/{id}")
//    public String showCourse(@PathVariable("id") long id, Model model){
//        model.addAttribute("todo", todoRepository.findById(id).get());
//        return "show";
//    }

    // Added for ToDo
//    @RequestMapping("/update/{id}")
//    public String updateCourse(@PathVariable("id") long id, Model model){
//        model.addAttribute("todo", todoRepository.findById(id).get());
//        return "todoform";
//    }

    @RequestMapping("/update_profile/{id}")
    public String updateProfile(@PathVariable("id") long id, Model model){
//        model.addAttribute("user", userRepository.findById(id).get()); JA 10-17-2019 run time error?


        model.addAttribute("user", userRepository.findById(id));
        model.addAttribute("roles", roleRepository.findAll());
        return "updateprofile";
    }

    // Added for ToDo
//    @RequestMapping("/delete/{id}")
//    public String delCourse(@PathVariable("id") long id){
//        todoRepository.deleteById(id);
//        return "redirect:/";
//    }


    // Added for 4.06
    @GetMapping("/register")
    public String showRegistrationPage(Model model){
        model.addAttribute("user", new User());
        return "registration";
    }

    // Added for 4.06
    @PostMapping("/register")
    public String processRegistrationPage(@Valid
               @ModelAttribute("user") User user, BindingResult result,
               Model model){
        model.addAttribute("user", user);

        if(result.hasErrors()){
            return "registration";
        }
        else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Created");
        }
        return "redirect:/login";
        //return "listtasks";
//        return "index";
    }

    @RequestMapping("/login")
    public String login(){
        return "login";
    }

    // Modified for 4.05
    @RequestMapping("/secure")
    public String secure(Principal principal, Model model) {
        String username = principal.getName();
//        System.out.println("secure: username= " + username);
//        User user = userRepository.findByUsername(username);
//        System.out.println("secure: email= " + user.getEmail());
//        System.out.println("secure: Password= " + user.getPassword());
        model.addAttribute("user", userRepository.findByUsername(username));
        return "secure";
    }
}
