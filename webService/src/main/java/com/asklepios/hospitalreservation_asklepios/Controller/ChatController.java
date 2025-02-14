package com.asklepios.hospitalreservation_asklepios.Controller;

import com.asklepios.hospitalreservation_asklepios.Service.IF_ChatService;
import com.asklepios.hospitalreservation_asklepios.Service.IM_ChatService;
import com.asklepios.hospitalreservation_asklepios.VO.ChatVO;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PostMapping("/api/chat/recommend")
    public String recommendMedicalDepartment(@RequestBody ChatVO chatVO) {
        if(chatVO.getIdentifier().equals("symptomChatbot")) {
            System.out.println("1");
            return chatService.recommendDepartment(chatVO.getMainMessage());
        } else if(chatVO.getIdentifier().equals("guideChatbot")) {
            return chatService.guideMessage(chatVO.getMainMessage());
        }
        return null;
    }

    @PostMapping("/api/medical/recommend")
    public String recommendMedical(@RequestBody ChatVO chatVO) {
        return chatService.recommendDepartment(chatVO.getMainMessage());
    }

}
