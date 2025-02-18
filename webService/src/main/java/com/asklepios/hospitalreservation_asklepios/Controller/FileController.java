package com.asklepios.hospitalreservation_asklepios.Controller;

import com.asklepios.hospitalreservation_asklepios.Service.QandAService;
import com.asklepios.hospitalreservation_asklepios.VO.QuestionVO;
import com.asklepios.hospitalreservation_asklepios.VO.QuestionlistVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class FileController {

    @Autowired
    private QandAService service;

    //이미지를 포함한 질문이 들어오면 동작
//    @PostMapping("/file_upload")
//    @ResponseBody
//    public ResponseEntity<String> file_upload(@RequestParam(value = "file",required = false)List<MultipartFile> file,@RequestParam("title")String title,
//                                              @RequestParam("content")String content , @RequestParam("tag")String tag, @RequestParam("sub")String sub,@ModelAttribute QuestionVO questionVO){
//
//        //질문을 작성한 사용자 확인
//        /*Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication!= null){
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            userDetails.getUsername()
//        }*/
//
//        boolean result =service.save_text(file,questionVO);
//        if (result){
//            return ResponseEntity.ok().body("success");
//        }else{
//            return ResponseEntity.badRequest().body("fail");
//        }
//
//    }

    //답변 저장
    @PostMapping("/answer")
    @ResponseBody
    public ResponseEntity<String> answer_doctor(@ModelAttribute QuestionVO vo){
        String id = "lind99";
       boolean result = service.answer(vo,id);
       if (result){
           return ResponseEntity.ok().body("success");
       }
        return ResponseEntity.badRequest().body("fail");
    }
    @GetMapping("/qandaList")
    @ResponseBody
    public ResponseEntity<String> qandalist(){
        //큐엔에이 리스트 가져오기
        List<QuestionlistVO> list =service.getList();
        System.out.println(list);
        return ResponseEntity.ok().body("success");
    }
    //현재 사용자 구하기
    @GetMapping("/test")
    public void test(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            String result = ((UserDetails) authentication.getPrincipal()).getUsername();
            System.out.println(result);
            //lin99
        }
    }

    //질문 자세히 보기
    @GetMapping("/show")
    public void show(int id){
        QuestionlistVO vo = service.showdeatil(id);
        System.out.println(vo);
    }

    //의사 답변 화면
    @GetMapping("/answerPage")
    public void answerPage(int questionId){
        String subject = service.getSubject(questionId);
        System.out.println(subject);
    }
}
