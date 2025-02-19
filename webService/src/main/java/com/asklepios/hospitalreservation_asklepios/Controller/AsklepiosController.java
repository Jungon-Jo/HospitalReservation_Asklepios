package com.asklepios.hospitalreservation_asklepios.Controller;
import com.asklepios.hospitalreservation_asklepios.Service.IF_ReservationService;
import com.asklepios.hospitalreservation_asklepios.Service.IF_UserService;
import com.asklepios.hospitalreservation_asklepios.VO.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
public class AsklepiosController {
    @Autowired
    IF_UserService userservice;
    @Autowired
    IF_ReservationService reservationservice;

    @GetMapping("/")
    public String home(Model model)  {return "redirect:home";}

    @GetMapping("/home")
    public String main(Model model) {
        MemberVO member = userservice.findMember();
        model.addAttribute("user", member);
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        //첫 소셜 로그인 시 추가정보 입력
        if(member != null) {
            if(member.getUser_name_eng()==null){
                return "redirect:socialInfo";
            }
        }
        return "home";
    }



    @ResponseBody
    @PostMapping("findDoctorCode")
    public String findDoctorCode(@RequestParam("user_id") String userId) {
//        System.out.println(userservice.findDoctorCode(userId));
        return userservice.findDoctorCode(userId);
    }

    @ResponseBody
    @PostMapping("popularHospital")
    public String[] popularHospital(){
        return reservationservice.popularHospital();
    }

    @ResponseBody
    @PostMapping("reservationCount")
    public int reservationCount(@RequestParam("doctor_code") String doctorCode) {
//        System.out.println(userservice.countReservation(doctorCode));
        return userservice.countReservation(doctorCode);
    }

    @ResponseBody
    @PostMapping("totalReservationCount")
    public int totalReservationCount(@RequestParam("user_id") String userId) {
//        System.out.println(userservice.countTotalReservation(userId));
        return userservice.countTotalReservation(userId);
    }
    @GetMapping("reservationStatusDoctor")
    public String reservationStatusDoctor( Model model) {
        model.addAttribute("user", userservice.findMember());
        return "myPage/reservationStatusDoctor";
    }
    @GetMapping("reservationStatusClient")
    public String reservationStatusClient( Model model) {
        model.addAttribute("user", userservice.findMember());
        return "myPage/reservationStatusClient";
    }
}
