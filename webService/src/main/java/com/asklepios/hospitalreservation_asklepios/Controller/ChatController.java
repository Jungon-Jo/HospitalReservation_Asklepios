package com.asklepios.hospitalreservation_asklepios.Controller;

import com.asklepios.hospitalreservation_asklepios.Service.IF_ChatService;
import com.asklepios.hospitalreservation_asklepios.Service.IM_ChatService;
import com.asklepios.hospitalreservation_asklepios.VO.ChatVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ChatController {
    @Autowired
    IF_ChatService chatService;

//    private final IM_ChatService IMChatService;
//
//    public ChatController(IM_ChatService IMChatService) {
//        this.IMChatService = IMChatService;
//    }

    @PostMapping("/api/chat")
    public ResponseEntity<String> recommendMedicalDepartment(@RequestBody ChatVO chatVO) {
        try {
            // ✅ [Step 1] 요청 데이터 확인
            System.out.println("📩 [Controller] 요청 수신");
            System.out.println("🔹 Identifier: " + chatVO.getIdentifier());
            System.out.println("🔹 MainMessage: " + chatVO.getMainMessage());

            String response;

            // ✅ [Step 2] Service 호출 및 처리
            if ("symptomChatbot".equals(chatVO.getIdentifier())) {
                System.out.println("🏥 [Controller] 진료과 추천 서비스 호출");
                response = chatService.recommendDepartment(chatVO.getMainMessage());
            } else if ("guideChatbot".equals(chatVO.getIdentifier())) {
                System.out.println("💬 [Controller] 서비스 이용 가이드 호출");
                response = chatService.guideMessage(chatVO.getMainMessage());
            } else {
                System.out.println("❌ [Controller] 잘못된 Identifier 값: " + chatVO.getIdentifier());
                return ResponseEntity.badRequest().body("잘못된 Identifier 값입니다.");
            }

            // ✅ [Step 3] Service에서 반환된 응답 확인
            if("symptomChatbot".equals(chatVO.getIdentifier())) {
                System.out.println("✅ [Controller] 응답 반환: " + response);
                return ResponseEntity.ok("진료 과는 " + "'"+ response + "'" + "입니다.");
            } else {
                System.out.println("✅ [Controller] 응답 반환: " + response);
                return ResponseEntity.ok(response);
            }

        } catch (Exception e) {
            // ✅ 예외 발생 시 상세 로그 출력
            System.out.println("❌ [Controller] 예외 발생: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500).body("서버 오류 발생");
        }
    }

    @PostMapping("/api/medical/recommend")
    public String recommendMedical(@RequestBody ChatVO chatVO) {
        return chatService.recommendDepartment(chatVO.getMainMessage());
    }
}
