package com.asklepios.hospitalreservation_asklepios.Controller;
import com.asklepios.hospitalreservation_asklepios.Service.IF_ReservationService;
import com.asklepios.hospitalreservation_asklepios.Service.IF_UserService;
import com.asklepios.hospitalreservation_asklepios.VO.MemberVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;


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
//        System.out.println(SecurityContextHolder.getContext().getAuthentication());
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

    @GetMapping("/predict")
    public String predict(Model model) {
        return "predictPopup";
    }

    @ResponseBody
    @PostMapping("/imgPredict")
    public ResponseEntity<byte[]> imgPredict(@RequestParam("file") MultipartFile file){
        try{
            //FastAPI에 보낼 multipart 요청준비
            MultiValueMap<String,Object> body=new LinkedMultiValueMap<>();
            body.add("file",file.getResource());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);

            //FastAPI 서버로 요청 전송
            HttpEntity<MultiValueMap<String,Object>> requestEntity =
                    new HttpEntity<>(body,headers);
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<byte[]> response=restTemplate.postForEntity(
                    "http://localhost:8000/imgPredict",requestEntity,byte[].class);
            //FastApi의 응답을 그대로 클라이언트에 리턴
            return response;
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.badRequest().body(null);
        }
    }

}
