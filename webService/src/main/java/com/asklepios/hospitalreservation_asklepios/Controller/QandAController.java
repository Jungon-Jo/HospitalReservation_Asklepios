package com.asklepios.hospitalreservation_asklepios.Controller;

import com.asklepios.hospitalreservation_asklepios.Service.IM_QandAService;
import com.asklepios.hospitalreservation_asklepios.VO.QuestionVO;
import com.asklepios.hospitalreservation_asklepios.VO.Question_imgVO;
import com.asklepios.hospitalreservation_asklepios.VO.QuestionlistVO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import com.asklepios.hospitalreservation_asklepios.Service.IF_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;


@Controller
public class QandAController {
  @Autowired
  IF_UserService userservice;
  @Autowired
  private IM_QandAService service;

  //html에 hidden으로 추가함-혜린
  @GetMapping("/qanda")
  public String qna(Model model) {
    //model.addAttribute("user",  userservice.findMember());
    //유저id만 필요!
      model.addAttribute("userId",get_userId());
    return "qanda/questionForm";
  }




  //질문저장 -> 저장과 동시에 파이썬 api 서버에 요청
  @PostMapping("/qnaSubmit")
  public ResponseEntity<Object> qnaSubmit(@RequestParam(value = "file",required = false) List< MultipartFile > file
          , @ModelAttribute QuestionVO questionVO) {
    int result =0;
    System.out.println("들어온:"+questionVO);
      //질문을 작성한 사용자 확인
      questionVO.setUser_id(get_userId());
      try {
        if (file==null || file.isEmpty()){
          System.out.print("file: 없음 ");
          result =service.save_text(questionVO);
          //return result ?ResponseEntity.ok().body("success"):ResponseEntity.badRequest().body("fail");
        }else {
          result =service.save_text_w_img(file,questionVO);
        }
        return new ResponseEntity<>(result,HttpStatus.OK);
      }catch (Exception e){
        System.out.println(e.getMessage());
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
      }


  }


  //답변 저장
  @PostMapping("/answer")
  @ResponseBody
  public ResponseEntity<String> answer_doctor(@ModelAttribute QuestionVO vo){
    boolean result = service.answer(vo,get_userId());
    if (result){
      return ResponseEntity.ok().body("success");
    }
    return ResponseEntity.badRequest().body("fail");
  }
  //큐엔에이 리스트 가져오기
  @GetMapping("/qandaList")
  public String qandalist(Model model){
    List<QuestionlistVO> list =service.getList();
    System.out.println(list);
    //tag 분할 -수정 -혜린
    for(QuestionlistVO vo:list){
      if (vo.getTag() != null) {
        String[] temp = vo.getTag().split("\\s*,\\s*");
        System.out.println(Arrays.toString(temp));
        vo.setTagList(temp);
        System.out.println(vo);
      }
    }
    model.addAttribute("list",list);
    return "qanda/questionList";
  }


  //현재 사용자 구하기
  //현재 사용자가 디비에 있는 회원인지 비교하는 코드 추가해야함..
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

  //질문 자세히 보기
  @GetMapping("/show")
  public String show(int id,Model model){
    int count =0; //의사 답변 수
    QuestionlistVO vo = service.showdetail(id);
    //tag 처리
    if (vo.getTag() !=null){
      String[] temp = vo.getTag().split("\\s*,\\s*");
      vo.setTagList(temp);
    }
    vo.setId(id);
    System.out.println("시간확인"+vo.getDate());
    String date = vo.getDate();
    String result = getWrittenTime(date);
    System.out.print("전처리 시간"+result);
    vo.setDate(result);
    //이미지 전처리
    for(Question_imgVO q :vo.getImgs()){
      System.out.println("이미지 전처리");
      String img_url = "/getImg/"+q.getFileName();
      q.setFileName(img_url);
    }
    //회원 이름 전처리
    String[]name = vo.getUser_name().split("");
    String first = name[0]+"**";
    System.out.println(first);
    vo.setUser_name(first);

    //하루 동안 답변이 달렸는지 확인 - 현재를 기준으로 첫 답변이 한시간 이상 차이나는 경우
      if (vo.getAnswerlist().isEmpty()||check_answer_time(vo.getAnswerlist())){
        //ai 답변 가져오기
        if (service.get_aiAnswer(id) == null){
          System.out.println("해당 ai 데이터 없음");
        }else {
          List<QuestionlistVO> list = new ArrayList<QuestionlistVO>();
          list.add(service.get_aiAnswer(id));
          vo.setAnswerlist(list);
        }
      }

      //의사답변 시간 전처리
      for(QuestionlistVO answer :vo.getAnswerlist()){
        String answer_date =answer.getDate();
        answer.setDate(getWrittenTime(answer_date));
        count++;
      }



    System.out.println(vo);
    model.addAttribute("list",vo);
    model.addAttribute("count",count);
    return "qanda/questionDetail";
  }

  //의사 답변 화면 - 질문 제목이 필요한 경우
  @GetMapping("/answerPage")
  public String answerPage(int questionId,Model model){
    String subject = service.getSubject(questionId);
    model.addAttribute("id",questionId);
    model.addAttribute("qna_title",subject);
    return "qanda/questionAnswerForm";
  }

  //게시글 작성 시간과 현재 시간을 계산함
  String getWrittenTime(String db_date){
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime formatted_date = LocalDateTime.parse(db_date,formatter);
    LocalDateTime current_time =LocalDateTime.now();
    Duration get_between_date = Duration.between(formatted_date,current_time);
    long day = get_between_date.toDays();
    long hour =get_between_date.toHours();
    long minutes =get_between_date.toMinutes();
    System.out.printf("day:hour:minutes %d:%d:%d",day,hour,minutes);
        /*현재 시간을 기준으로 1시간 이하 차이 -> 몇 분전 으로 표시
        초 차이 -> 방금 전으로 표시
        * 1시간 차이 -> 몇 시간 전으로 표시
        * 하루 차이 -> 몇 일전으로 표시*/
    if (day>6){
      System.out.print("7일 넘게 차이남");
      System.out.print("시간"+db_date);
      String a =formatted_date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
      String[] arr = Arrays.copyOfRange(a.split(""), 0, 11);
      return String.join("",arr);

    }else if(day>0){
      System.out.print("day를 기준으로 차이남");
      return day+"일 전";
    }else if (hour>0){
      System.out.print("hour를 기준으로 차이남");
      return hour+"시간 전";
    }else if (minutes>0){
      System.out.print("minutes를 기준으로 차이남");
      return minutes+"분 전";
    }else {
      return "방금 전";
    }

  }

  @PostMapping("/fast_api")
  void testa(@RequestBody Map<String,String>a){
    System.out.println(a);

  }

  //ai 답변 성립하는지 확인
  boolean check_answer_time(List<QuestionlistVO> list){
    String date = list.get(0).getDate();
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    LocalDateTime localDateTime = LocalDateTime.parse(date,formatter);
    LocalDateTime now = LocalDateTime.now();
    Duration d =Duration.between(localDateTime,now);
    if (d.toHours()>0){
      return false; //한시간 이내에 답변을 함
    }else {
      return true; //ai 답변
    }
  }

  /*질문글 읽음 확인 체크 함수*/
  public void check_unread() {

  }
}
