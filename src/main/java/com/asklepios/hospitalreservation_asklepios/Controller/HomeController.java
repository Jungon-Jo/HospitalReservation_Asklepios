package com.asklepios.hospitalreservation_asklepios.Controller;

import com.asklepios.hospitalreservation_asklepios.Service.IF_BoardService;
import com.asklepios.hospitalreservation_asklepios.VO.BoardVO;
import com.asklepios.hospitalreservation_asklepios.VO.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    IF_BoardService boardService;

    @GetMapping("/home")
    public String main(){
        return "home";
    }
    @GetMapping("/bboard_health")
    public String board_health(Model model, @ModelAttribute PageVO pagevo) throws Exception {
        if(pagevo.getPage()==null){
            pagevo.setPage(1);
        }
        pagevo.setTotalCount(boardService.boardCount());
        List<BoardVO> boardlist=boardService.boardHealthList(pagevo);
        model.addAttribute("boardlist",boardlist);
        return "board/main";
    }
    @GetMapping("/bboard_campaign")
    public String board_cam(Model model, @ModelAttribute PageVO pagevo ) throws Exception {
        List<BoardVO> boardlist=boardService.boardCampaignList( pagevo);
        model.addAttribute("boardlist",boardlist);
        return "board/main";
    }
    @GetMapping("/bboard_med")
    public String board_med(Model model , @ModelAttribute PageVO pagevo) throws Exception {
        List<BoardVO> boardlist=boardService.boardMedList( pagevo);
        model.addAttribute("boardlist",boardlist);
        return "board/main";
    }
    @GetMapping("/bboard_free")
    public String board_free(Model model , @ModelAttribute PageVO pagevo) throws Exception {
        List<BoardVO> boardlist=boardService.boardFreeList(pagevo);
        model.addAttribute("boardlist",boardlist);
        return "board/main";
    }
    @GetMapping("bboard/write")
    public String write(){
        return "board/write";
    }

    @PostMapping("/bboard/submitwrite")
    public String submitWrite(@ModelAttribute BoardVO boardVO) throws Exception {
        boardService.addBoard(boardVO);
        return "redirect:/bboard";

    }

}
