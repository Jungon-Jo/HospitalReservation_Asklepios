package com.asklepios.hospitalreservation_asklepios.Controller;

import com.asklepios.hospitalreservation_asklepios.Service.IF_BoardService;
import com.asklepios.hospitalreservation_asklepios.Service.LikeService;
import com.asklepios.hospitalreservation_asklepios.Util.FileDataUtil;
//import com.asklepios.hospitalreservation_asklepios.Util.FileDataUtil;
import com.asklepios.hospitalreservation_asklepios.VO.BoardVO;
import com.asklepios.hospitalreservation_asklepios.VO.PageVO;
import com.asklepios.hospitalreservation_asklepios.VO.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;

@Controller
public class HomeController {
  @Autowired
  IF_BoardService boardService;
  FileDataUtil fileDataUtil;
    @GetMapping("/home")
    public String main(@SessionAttribute(name = "loginUser", required = false) UserVO user, Model model){
        model.addAttribute("user", user);
        return "home";
    }

  @GetMapping("/bboard_all")
  public String board_all(Model model, @ModelAttribute PageVO pagevo) throws Exception{
    if(pagevo.getPage()==null){
      pagevo.setPage(1);
    }
    String category="모든 글";
    pagevo.setTotalCount(boardService.boardCount(category));
    List<BoardVO> boardlist=boardService.boardAll(pagevo);
    List<BoardVO> noticelist=boardService.boardNoticeList();
    model.addAttribute("boardlist", boardlist);
    model.addAttribute("noticelist", noticelist);
    model.addAttribute("category", category);
    return "board/main";
  }

  @GetMapping("/bboard_health")
  public String board_health(Model model, @ModelAttribute PageVO pagevo) throws Exception {
    if(pagevo.getPage()==null){
      pagevo.setPage(1);
    }
    String category="오늘의 건강";
    pagevo.setTotalCount(boardService.boardCount(category));
    List<BoardVO> boardlist=boardService.boardList(pagevo,category);
//        System.out.println(pagevo.getPage());
//        System.out.println(pagevo.getStartNo()+"/"+pagevo.getEndNo());
//        System.out.println(pagevo.isNext());
//        System.out.println(pagevo.isPrev());

    model.addAttribute("boardlist",boardlist);
    model.addAttribute("category", category);
    return "board/main";
  }
  @GetMapping("/bboard_campaign")
  public String board_cam(Model model, @ModelAttribute PageVO pageVO ) throws Exception {
    if(pageVO.getPage()==null){
      pageVO.setPage(1);
    }
    String category="캠페인";
    pageVO.setTotalCount(boardService.boardCount(category));
    List<BoardVO> boardlist=boardService.boardList(pageVO,category);

    model.addAttribute("boardlist",boardlist);
    model.addAttribute("category", category);
    return "board/main";
  }
  @GetMapping("/bboard_med")
  public String board_med(Model model , @ModelAttribute PageVO pagevo) throws Exception {
    if(pagevo.getPage()==null){
      pagevo.setPage(1);
    }
    String category="의료정보";
    pagevo.setTotalCount(boardService.boardCount(category));
    List<BoardVO> boardlist=boardService.boardList(pagevo,category);
    model.addAttribute("boardlist",boardlist);
    model.addAttribute("category", category);
    return "board/main";
  }
  @GetMapping("/bboard_free")
  public String board_free(Model model , @ModelAttribute PageVO pagevo) throws Exception {
    if(pagevo.getPage()==null){
      pagevo.setPage(1);
    }
    String category="자유게시판";
    pagevo.setTotalCount(boardService.boardCount(category));
    List<BoardVO> boardlist=boardService.boardList(pagevo,category);
    model.addAttribute("boardlist",boardlist);
    model.addAttribute("category", category);
    return "board/main";
  }
  @GetMapping("bboard/write")
  public String write(){
    return "board/write";
  }

  @PostMapping("bboard/submitwrite")
  public String submitWrite(@ModelAttribute BoardVO boardVO,
                            @ModelAttribute MultipartFile[]file) throws Exception {

//    System.out.println(file.length);
    String [] newFileName=fileDataUtil.fileUpload(file);
    boardVO.setBoard_binary(newFileName);
    boardService.addBoard(boardVO);
    return "redirect:/bboard_all";

  }
  @GetMapping("/detail")
  public String detail(Model model, @ModelAttribute PageVO pagevo,
                       @RequestParam("no") String no) throws Exception {
    BoardVO boardVO=boardService.detail(no);
    model.addAttribute("boardVO",boardVO);
    return "board/detail";
  }
  @GetMapping("/modboard")
  public String mod(Model model,@ModelAttribute BoardVO boardVO
  ) throws Exception {
//        System.out.println(no);
    boardVO=boardService.modBoard(boardVO.getBoard_sequence());
//        System.out.println(boardvo.getBoard_content());
    model.addAttribute("boardVO",boardVO);
    return "board/modwrite";
  }
  @PostMapping("/save")
  public String save(@ModelAttribute BoardVO boardVO) throws Exception {
//        System.out.println(boardVO.getBoard_category());
    boardService.modBoard(boardVO);
    return "redirect:/bboard_health";
  }

}

