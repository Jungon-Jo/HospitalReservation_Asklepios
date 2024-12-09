package com.asklepios.hospitalreservation_asklepios.Controller;

import com.asklepios.hospitalreservation_asklepios.VO.UserVO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.SessionAttribute;

@Controller
public class AsklepiosController {

    @GetMapping("/")
    public String home(@SessionAttribute(name = "loginUser", required = false) UserVO user, Model model) {
        model.addAttribute("user", user);
        return "home";
    }

//    @GetMapping("/login")
//    public String login() {
//        return "login";
//    }
}
