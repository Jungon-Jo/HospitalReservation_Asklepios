package com.asklepios.hospitalreservation_asklepios.Controller;

import com.asklepios.hospitalreservation_asklepios.Service.IF_ReservationService;
import com.asklepios.hospitalreservation_asklepios.Service.IF_UserService;
import com.asklepios.hospitalreservation_asklepios.VO.MemberVO;
import com.asklepios.hospitalreservation_asklepios.VO.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@Controller
public class AsklepiosController {
    @Autowired
    IF_UserService userservice;
    @Autowired
    IF_ReservationService reservationservice;

    @GetMapping("/")
    public String home(Model model)  {return "redirect:home";}

    @GetMapping("/home")
    public String main(Model model) throws IOException {
        MemberVO member = userservice.findMember();
        model.addAttribute("user", member);
        //첫 소셜 로그인 시 추가정보 입력
        if(member != null) {
            if(member.getUser_name_eng()==null){
                return "redirect:socialInfo";
            }
        }
        return "home";
    }

    @GetMapping("/socialInfo")
    public String socialInfo(Model model,HttpServletResponse response, HttpServletRequest request) throws IOException {
        model.addAttribute("user", userservice.findMember());
        response.setContentType("text/html; charset=UTF-8");
        response.setCharacterEncoding("UTF-8");
        request.setCharacterEncoding("UTF-8");
        response.getWriter().println("<script> alert('추가 정보 작성을 위해 해당 페이지로 이동합니다.');</script>");
//        response.getWriter().close();
       return "userJoin/insertSocialCommonInfo";
    }
    @PostMapping("/saveSocialInfo")
    public String saveSocialInfo(@ModelAttribute("user") UserVO user
            ,@RequestParam(value = "file", required = false) MultipartFile file ) {
        //새로운 메서드 생성필요
        MemberVO member = userservice.findMember();
        user.setUser_authority("scClient");
        user.setUser_id(member.getUser_id());
        user.setUser_password("Oauth2");
        user.setUser_image(file.getName());
//        user.setUser_name_eng("Taekyun Shin");
        System.out.println(user.toString());
        userservice.modifySocialUserCommonInfo(user);
        return "redirect:/home";
    }
    @GetMapping("/showAgreement")
    public String showAgreement(){
        return "userJoin/socialAgreement";
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
