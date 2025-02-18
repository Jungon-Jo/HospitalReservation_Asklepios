//package com.asklepios.hospitalreservation_asklepios.Controller;
//
//import com.asklepios.hospitalreservation_asklepios.Service.QandAService;
//import com.asklepios.hospitalreservation_asklepios.VO.QuestionVO;
//import com.asklepios.hospitalreservation_asklepios.VO.Question_imgVO;
//import com.asklepios.hospitalreservation_asklepios.VO.QuestionlistVO;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.ui.Model;
//import com.asklepios.hospitalreservation_asklepios.Service.IF_UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.time.Duration;
//import java.time.LocalDateTime;
//import java.time.format.DateTimeFormatter;
//import java.util.Arrays;
//import java.util.List;
//
//
//
//
//@Controller
//public class QandAController {
//  @Autowired
//  IF_UserService userservice;
//  @Autowired
//  private QandAService service;
//
//  //html에 hidden으로 추가함-혜린
//  @GetMapping("/qanda")
//  public String qna(Model model) {
//    //model.addAttribute("user",  userservice.findMember());
//    //유저id만 필요!
//      model.addAttribute("userId",get_userId());
//    return "qanda/questionForm";
//  }
//
//  @GetMapping("/qanda2")
//  public String qna() {
//    //model.addAttribute("user",  userservice.findMember());
//    //유저id만 필요!
//
//    return "qanda/questionForm2";
//  }
//
//  //질문저장
//  @PostMapping("/qnaSubmit")
//  public String qnaSubmit(@RequestParam(value = "file",required = false) List< MultipartFile > file
//          ,@ModelAttribute QuestionVO questionVO) {
//    System.out.println("등러온:"+questionVO);
//      //질문을 작성한 사용자 확인
//      if (file==null || file.isEmpty()){
//        System.out.print("file: 없음 ");
//        boolean result =service.save_text(questionVO);
//        //return result ?ResponseEntity.ok().body("success"):ResponseEntity.badRequest().body("fail");
//      }else {
//        boolean result =service.save_text_w_img(file,questionVO);
//      }
//    //홈으로 말고 질문 리스트화면으로 리다이렉트 구상 중...
//    return "redirect:home";
//  }
//
//
//  //답변 저장
//  @PostMapping("/answer")
//  @ResponseBody
//  public ResponseEntity<String> answer_doctor(@ModelAttribute QuestionVO vo){
//    boolean result = service.answer(vo,get_userId());
//    if (result){
//      return ResponseEntity.ok().body("success");
//    }
//    return ResponseEntity.badRequest().body("fail");
//  }
//  //큐엔에이 리스트 가져오기
//  @GetMapping("/qandaList")
//  @ResponseBody
//  public ResponseEntity<String> qandalist(){
//    List<QuestionlistVO> list =service.getList();
//    System.out.println(list);
//
//    return ResponseEntity.ok().body("success");
//  }
//
//
//  //현재 사용자 구하기
//  //현재 사용자가 디비에 있는 회원인지 비교하는 코드 추가해야함..
//  public String get_userId(){
//    String result="";
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//      result = ((UserDetails) authentication.getPrincipal()).getUsername();
//      System.out.println(result);
//      //lin99
//      return result;
//    }
//    return result;
//  }
//
//  //질문 자세히 보기
//  @GetMapping("/show")
//  public String show(int id,Model model){
//    QuestionlistVO vo = service.showdeatil(id);
//    System.out.println("시간확인"+vo.getDate());
//    String date = vo.getDate();
//    String result = getWrittenTime(date);
//    System.out.print("전처리 시간"+result);
//    vo.setDate(result);
//    //이미지 전처리
//    for(Question_imgVO q :vo.getImgs()){
//      System.out.println("이미지 전처리");
//      String img_url = "/getImg/"+q.getFileName();
//      q.setFileName(img_url);
//    }
//    //회원 이름 전처리
//    String[]name = vo.getUser_name().split("");
//    String first = name[0]+"**";
//    System.out.println(first);
//    vo.setUser_name(first);
//    //의사답변 시간 전처리
//    for(QuestionlistVO answer :vo.getAnswerlist()){
//      String answer_date =answer.getDate();
//      answer.setDate(getWrittenTime(answer_date));
//    }
//    System.out.println(vo);
//    model.addAttribute("list",vo);
//    return "qanda/questionForm2";
//  }
//
//  //의사 답변 화면 - 질문 제목이 필요한 경우
//  @GetMapping("/answerPage")
//  public void answerPage(int questionId){
//    String subject = service.getSubject(questionId);
//    System.out.println(subject);
//  }
//
//  //게시글 작성 시간과 현재 시간을 계산함
//  String getWrittenTime(String db_date){
//
//    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
//    LocalDateTime formatted_date = LocalDateTime.parse(db_date,formatter);
//    LocalDateTime current_time =LocalDateTime.now();
//    Duration get_between_date = Duration.between(formatted_date,current_time);
//    long day = get_between_date.toDays();
//    long hour =get_between_date.toHours();
//    long minutes =get_between_date.toMinutes();
//    System.out.printf("day:hour:minutes %d:%d:%d",day,hour,minutes);
//        /*현재 시간을 기준으로 1시간 이하 차이 -> 몇 분전 으로 표시
//        초 차이 -> 방금 전으로 표시
//        * 1시간 차이 -> 몇 시간 전으로 표시
//        * 하루 차이 -> 몇 일전으로 표시*/
//    if (day>6){
//      System.out.print("7일 넘게 차이남");
//      System.out.print("시간"+db_date);
//      String a =formatted_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
//      String[] arr = Arrays.copyOfRange(a.split(""), 0, 11);
//      return String.join("",arr);
//
//    }else if(day>0){
//      System.out.print("day를 기준으로 차이남");
//      return day+"일 전";
//    }else if (hour>0){
//      System.out.print("hour를 기준으로 차이남");
//      return hour+"시간 전";
//    }else if (minutes>0){
//      System.out.print("minutes를 기준으로 차이남");
//      return minutes+"분 전";
//    }else {
//      return "방금 전";
//    }
//
//  }
//}
