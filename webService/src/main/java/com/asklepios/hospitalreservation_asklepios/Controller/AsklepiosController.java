package com.asklepios.hospitalreservation_asklepios.Controller;
import com.asklepios.hospitalreservation_asklepios.Service.IF_ReservationService;
import com.asklepios.hospitalreservation_asklepios.Service.IF_UserService;
import com.asklepios.hospitalreservation_asklepios.Service.IM_QandAService;
import com.asklepios.hospitalreservation_asklepios.VO.MemberVO;
import com.asklepios.hospitalreservation_asklepios.VO.QuestionVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


@Controller
public class AsklepiosController {
    @Autowired
    IF_UserService userservice;
    @Autowired
    IF_ReservationService reservationservice;
    @Autowired
    private IM_QandAService service;

    @GetMapping("/")
    public String home(Model model)  {return "redirect:home";}

    @GetMapping("/home")
    public String main(Model model) {
        model.addAttribute("unreadCount",check_unread());
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

    /*질문글 읽음 확인 체크 함수*/
    public int check_unread() {
        int count =0;
        String user = get_userId();
        if (!user.isEmpty()) {
            if (userservice.getAuthority(user)) {
                System.out.println(Arrays.toString(service.getAnswer_unread(user)));
                if(service.getAnswer_unread(user).length == 0){
                    return count;
                }
                for (char a : service.getAnswer_unread(user)) {
                    if (a == '0') {
                        count++;
                    }
                }
                //답변이 없거나 시간초과로 답한경우 -> ai 답변 체크 후 읽음 유무
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                List<QuestionVO> list = service.checkUnReadTime_service(user);
                QuestionVO vo = list.get(0);
                LocalDateTime question_date_format = LocalDateTime.parse(vo.getSub(),formatter);
                LocalDateTime answer = LocalDateTime.parse(vo.getDate(),formatter);
                Duration d =Duration.between(question_date_format,answer);
                LocalDateTime now = LocalDateTime.now();
                Duration d_ai =Duration.between(question_date_format,now);
                if(vo.getDate() == null || d.toMinutes()>60){
                    if (d_ai.toMinutes()>60){
                        System.out.println("ai 답변 가능");
                        for (char b : service.getAi_unread(user)) {
                            System.out.println(b);
                            if (b == '0') {
                                count++;
                            }
                        }
                    }
                }
            }
        }
        System.out.println("ai count"+ count);
        return count;
    }

    //현재 사용자 구하기
    public String get_userId(){
        String result="";
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            result = ((UserDetails) authentication.getPrincipal()).getUsername();
            System.out.println(result);
            //lin99
            return result;
        }
        return result;
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
