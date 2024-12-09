package com.asklepios.hospitalreservation_asklepios.Controller;

import com.asklepios.hospitalreservation_asklepios.Service.IF_RegistrationService;
import com.asklepios.hospitalreservation_asklepios.VO.HospitalVO;
import com.asklepios.hospitalreservation_asklepios.VO.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegistrationController {

  @Autowired
  IF_RegistrationService registrationservice;

  @GetMapping("/registration")
  public String registration(@SessionAttribute(name = "loginUser", required = false) UserVO user, Model model) {
    model.addAttribute("user", user);
    if (user == null) {
      return "redirect:/login";
    } else if(user.getUser_authority().equals("client")) {
//      alert 띄우는 방법 생각하기 20241208_2212
      return "redirect:/home";
    }else{
      return "registrationForm";
    }
  }

  @ResponseBody
  @PostMapping("/duplicateHospital")
  public boolean duplicateHospital(@RequestParam("hospital_address") String hospitalAddress,
                                  @RequestParam("hospital_name") String hospitalName) {
    return registrationservice.duplicateCheck(hospitalAddress, hospitalName);
  }

  @PostMapping("/register")
  public String register(@ModelAttribute HospitalVO hospitalvo){
    registrationservice.registerHospital(hospitalvo);
    return "redirect:/home";
  }
}
