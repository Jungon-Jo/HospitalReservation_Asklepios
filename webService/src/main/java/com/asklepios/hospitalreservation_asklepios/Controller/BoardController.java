package com.asklepios.hospitalreservation_asklepios.Controller;

import com.asklepios.hospitalreservation_asklepios.Service.IF_BoardService;
import com.asklepios.hospitalreservation_asklepios.Service.IF_UserService;
import com.asklepios.hospitalreservation_asklepios.Util.FileDataUtil;
//import com.asklepios.hospitalreservation_asklepios.Util.FileDataUtil;
import com.asklepios.hospitalreservation_asklepios.VO.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

@Controller
public class BoardController {
  @Autowired
  IF_BoardService boardService;
  @Autowired
  FileDataUtil fileDataUtil;
  @Autowired
  IF_UserService userService;

  @GetMapping("/bboard_all")
  public String board_all(Model model, @ModelAttribute PageVO pagevo) throws Exception{
    MemberVO user=userService.findMember();
    model.addAttribute("user", user);
    if(pagevo.getPage()==null){
      pagevo.setPage(1);
    }
    String category="모든 글";
    pagevo.setTotalCount(boardService.boardCount(category));
//    System.out.println(pagevo.getTotalCount());
//    System.out.println(pagevo.getPage());
//    System.out.println(pagevo.getStartNo());
    List<BoardVO> boardlist=boardService.boardAll(pagevo);
//    List<BoardVO> noticelist=boardService.boardNoticeList();
    model.addAttribute("boardlist", boardlist);
//    model.addAttribute("noticelist", noticelist);
    model.addAttribute("category", category);
    return "board/main";
  }

  @GetMapping("/bboard_health")
  public String board_health(Model model, @ModelAttribute PageVO pagevo,
                             @RequestParam(value = "orderNumber", required = false) Integer orderNumber) throws Exception {
    MemberVO user=userService.findMember();
    if(pagevo.getPage()==null){
      pagevo.setPage(1);
    }
    if(orderNumber==null){
      orderNumber=1;
    }
    String category="오늘의 건강";
//    System.out.println(pagevo.getSearchKeyword());
    pagevo.setTotalCount(boardService.boardCount(category));
    List<BoardVO> boardlist=boardService.boardList(pagevo,category,orderNumber);
//        System.out.println(pagevo.getPage());
//        System.out.println(pagevo.getStartNo()+"/"+pagevo.getEndNo());
//        System.out.println(pagevo.isNext());
//        System.out.println(pagevo.isPrev());
    model.addAttribute("user", user);
    model.addAttribute("boardlist",boardlist);
    model.addAttribute("category", category);
    model.addAttribute("orderNumber", orderNumber);
    return "board/main";
  }
  @GetMapping("/bboard_campaign")
  public String board_cam(Model model, @ModelAttribute PageVO pageVO,
                          @RequestParam(value = "orderNumber", required = false) Integer orderNumber) throws Exception {
    MemberVO user=userService.findMember();
    if(pageVO.getPage()==null){
      pageVO.setPage(1);
    }
    if(orderNumber==null){
      orderNumber=1;
    }
    String category="캠페인";

    pageVO.setTotalCount(boardService.boardCount(category));
    List<BoardVO> boardlist=boardService.boardList(pageVO,category,orderNumber);
    model.addAttribute("user", user);
    model.addAttribute("boardlist",boardlist);
    model.addAttribute("category", category);
    model.addAttribute("orderNumber", orderNumber);
    return "board/main";
  }
  @GetMapping("/bboard_med")
  public String board_med(Model model , @ModelAttribute PageVO pagevo,
                          @RequestParam(value = "orderNumber", required = false) Integer orderNumber) throws Exception {
    MemberVO user=userService.findMember();
    if(pagevo.getPage()==null){
      pagevo.setPage(1);
    }
    if(orderNumber==null){
      orderNumber=1;
    }
    String category="의료정보";

    pagevo.setTotalCount(boardService.boardCount(category));
    List<BoardVO> boardlist=boardService.boardList(pagevo,category,orderNumber);
    model.addAttribute("user", user);
    model.addAttribute("boardlist",boardlist);
    model.addAttribute("category", category);
    model.addAttribute("orderNumber", orderNumber);
    return "board/main";
  }
  @GetMapping("/bboard_free")
  public String board_free(Model model , @ModelAttribute PageVO pagevo,
                           @RequestParam(value = "orderNumber", required = false) Integer orderNumber) throws Exception {
    if(pagevo.getPage()==null){
      pagevo.setPage(1);
    }
    if(orderNumber==null){
      orderNumber=1;
    }
    String category="자유게시판";
    pagevo.setTotalCount(boardService.boardCount(category));
    List<BoardVO> boardlist=boardService.boardList(pagevo,category,orderNumber);
    model.addAttribute("user", userService.findMember());
    model.addAttribute("boardlist",boardlist);
    model.addAttribute("category", category);
    model.addAttribute("orderNumber", orderNumber);
    return "board/main";
  }
  @GetMapping("/write")
  public String write(  Model model){
    model.addAttribute("user", userService.findMember());
    return "board/write";
  }

  @PostMapping("/submitwrite")
  public String submitWrite(@ModelAttribute BoardVO boardVO,
                            @ModelAttribute MultipartFile [] file) throws Exception {

//    System.out.println(file.toString());
    String []newFileName=fileDataUtil.fileUpload(file);
    String boardFilename="";
    for(int i=0;i<newFileName.length;i++){
      boardFilename+=(newFileName[i]);
      if(i!=newFileName.length-1){
        boardFilename+=",";
      }
    }
    boardVO.setBoard_binary(boardFilename);
    System.out.println(boardVO.getBoard_binary());
    boardService.addBoard(boardVO);

    System.out.println(boardVO.getBoard_category());
    if(boardVO.getBoard_category().equals("오늘의 건강")){
      return "redirect:bboard_health";
    }else if(boardVO.getBoard_category().equals("캠페인")){
      return "redirect:bboard_campaign";
    }else if(boardVO.getBoard_category().equals("의료기기")){
      return "redirect:bboard_med";
    }else if(boardVO.getBoard_category().equals("자유게시판")){
      return "redirect:bboard_free";
    }else{
      return "redirect:bboard_health";
    }
  }
  @GetMapping("/detail")
  public String detail(Model model, @ModelAttribute PageVO pagevo,
                       @RequestParam("no") String no) throws Exception {
    BoardVO boardVO=boardService.detail(no);
    model.addAttribute("user", userService.findMember());
    model.addAttribute("boardVO",boardVO);
    return "board/detail";
  }

  @GetMapping("/modboard")
  public String mod(@ModelAttribute BoardVO boardVO,
                    Model model, HttpServletResponse response, HttpServletRequest request) throws Exception {
//        System.out.println(no);
    MemberVO user=userService.findMember();
    if(user.getUser_id().equals(boardVO.getBoard_user_id())){
      boardVO=boardService.modBoard(boardVO.getBoard_sequence());
//        System.out.println(boardvo.getBoard_content());
      model.addAttribute("user", user);
      model.addAttribute("boardVO",boardVO);
      return "board/modwrite";
    }else{
      response.setContentType("text/html; charset=UTF-8");
      response.setCharacterEncoding("UTF-8");
      request.setCharacterEncoding("UTF-8");
      response.getWriter().println("<script> alert('게시글은 작성자만 수정이 가능합니다');history.back(-1);</script>");
      response.getWriter().close();
    }
    return null;
  }
  @PostMapping("/save")
  public String save(@ModelAttribute BoardVO boardVO) throws Exception {
//        System.out.println(boardVO.getBoard_category());
    boardService.modBoard(boardVO);
    return "redirect:/bboard_health";
  }

  @GetMapping("/deleteboard")
  public String deleteboard(@RequestParam String board_sequence) throws Exception {
//    System.out.println(board_sequence);
    boardService.delBoard(board_sequence);
    return "redirect:/bboard_health";

  }
  @ResponseBody
  @PostMapping("/like")
  public HashMap<String,Object> like(@RequestBody LikeVO likeVO){
    HashMap<String,Object> map = new HashMap<>();
    likeVO.setLiked(boardService.checkLike(likeVO));
    int heart= boardService.countHeart(likeVO);
    map.put("heart",heart);
    map.put("likeVO",likeVO);
    return map;
  }
  @ResponseBody
  @PostMapping("/likecheck")
  public boolean likecheck(@RequestBody LikeVO likeVO) {
    return boardService.firstLike(likeVO);
  }

  @ResponseBody
  @GetMapping("/listOrderBy")
  public List<BoardVO> listOrderBy(@RequestBody PageVO pageVO
          ,@RequestBody HashMap<String,Object> orderInfo) throws Exception {
    if(pageVO.getPage()==null){
      pageVO.setPage(1);
    }

    String category= (String) orderInfo.get("category");
    int orderNumber= (int) orderInfo.get("orderBy");
    return boardService.boardList(pageVO, category, orderNumber);
  }
}

