package com.asklepios.hospitalreservation_asklepios.Controller;

import com.asklepios.hospitalreservation_asklepios.Service.IF_ReservationService;
import com.asklepios.hospitalreservation_asklepios.VO.ReservationVO;
import com.asklepios.hospitalreservation_asklepios.VO.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ReservatioinController {
  @Autowired
  IF_ReservationService reservationService;

  @GetMapping("/reservation")
  public String reservation(@SessionAttribute(name = "loginUser", required = false) UserVO user, Model model) {
    model.addAttribute("user", user);
    if(user == null){
      return "redirect:/login";
    }else{
      return "reservationPlace";
    }
  }

  @ResponseBody
  @PostMapping("/findHospitalName")
  public boolean findHospitalName(@RequestParam("hospital_name") String hospitalName,
                                  @RequestParam("hospital_address") String hospitalAddr) {
    return reservationService.checkHospitalName(hospitalName, hospitalAddr);
  }

  @PostMapping("/reserve")
  public String reserve(@SessionAttribute(name = "loginUser", required = false) UserVO user,
                        @RequestParam(value = "hospital_name", required = false) String hospitalName,
                        Model model){
    System.out.println(user.toString());
    model.addAttribute("user", user);
    String doctorCode ="4fb63188-1578-4a0a-ae21-5a2786cb85cf";
    String doctorName = reservationService.findDoctorName(doctorCode);
    String hospitalCode = reservationService.findHospitalCode(hospitalName);
    model.addAttribute("hospital_name", hospitalName);
    model.addAttribute("hospital_code", hospitalCode);
    model.addAttribute("doctor_name", doctorName);
    model.addAttribute("doctor_code", doctorCode);
    model.addAttribute("user_name",user.getUser_name());
    model.addAttribute("user_id",user.getUser_id());
    model.addAttribute("user_tel",user.getUser_tel());
    return "reservationForm";
  }

  @ResponseBody
  @PostMapping("/isReserve")
  public String[] reserve1(@RequestParam("reservation_date") String reservationDate,
                           @RequestParam("hospital_code") String hospitalCode,
                           @RequestParam("doctor_code") String doctorCode){
//    System.out.println(hospitalCode);
//    System.out.println(doctorCode);
//    System.out.println(reservationDate);

    return reservationService.findAllReservationTime(hospitalCode, doctorCode, reservationDate);
  }

  @PostMapping("/reserve/submit")
  public String reservationSubmit(@ModelAttribute ReservationVO reservationvo){
//    System.out.println(reservationvo.toString());
    reservationService.addReservation(reservationvo);
    return "redirect:/home";
  }

//  @GetMapping("/reservationForm")
//  public String reservationForm(@SessionAttribute(name = "loginUserId", required = false) String userId, Model model) {
//    model.addAttribute("userId", userId);
//    return "reservationForm";
//  }

}
