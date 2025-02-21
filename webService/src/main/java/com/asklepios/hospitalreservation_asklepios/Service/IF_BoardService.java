package com.asklepios.hospitalreservation_asklepios.Service;

import com.asklepios.hospitalreservation_asklepios.VO.BoardVO;
import com.asklepios.hospitalreservation_asklepios.VO.LikeVO;
import com.asklepios.hospitalreservation_asklepios.VO.PageVO;
import java.util.List;


public interface IF_BoardService {
    public void addBoard(BoardVO boardVO) throws Exception;
    public List<BoardVO> boardAll(PageVO pagevo) throws Exception;
    public List<BoardVO> boardList(PageVO pagevo,String category, int orderNumber) throws Exception;
    //    public List<BoardVO> boardHealthList(PageVO pagevo)throws Exception;
//    public List<BoardVO> boardCampaignList(PageVO pagevo)throws Exception;
//    public List<BoardVO> boardMedList(PageVO pagevo)throws Exception;
//    public List<BoardVO> boardFreeList(PageVO pagevo)throws Exception;
    public List<BoardVO> boardNoticeList()throws Exception;
    public int boardCount(String category);
    public BoardVO modBoard(String no) throws Exception;
    public void modBoard(BoardVO boardVO);
    public BoardVO detail(String no) throws Exception;
    public void delBoard(String no) throws Exception;
    public boolean checkLike(LikeVO likeVO);
    public boolean firstLike(LikeVO likeVO);
    public int countHeart(LikeVO likeVO);
}
