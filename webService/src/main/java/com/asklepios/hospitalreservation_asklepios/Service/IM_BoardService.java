package com.asklepios.hospitalreservation_asklepios.Service;

import com.asklepios.hospitalreservation_asklepios.Repository.IF_BoardMapper;
import com.asklepios.hospitalreservation_asklepios.VO.BoardVO;
import com.asklepios.hospitalreservation_asklepios.VO.LikeVO;
import com.asklepios.hospitalreservation_asklepios.VO.PageVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IM_BoardService implements IF_BoardService{
    @Autowired
    IF_BoardMapper boardMapper;

    @Override
    public void addBoard(BoardVO boardVO) {
        selectCategory(boardVO);
        System.out.println(boardVO.toString());
        boardMapper.insertOne(boardVO);
    }

//    @Override
//    public List<BoardVO> BoardList(PageVO pagevo,String type) throws Exception {
//       List<BoardVO> boardList=List.of();
//        if(type.equals("모든글")){
//            boardList=boardMapper.selectAll(pagevo);
//        }else if(type.equals("오늘의건강")){
//            boardList=boardMapper.selectHealth(pagevo);
//        }else if(type.equals("캠페인")){
//            boardList=boardMapper.selectCampaign(pagevo);
//        }else if(type.equals("의료정보")){
//            boardList=boardMapper.selectMed(pagevo);
//        }else if(type.equals("자유게시판")){
//            boardList=boardMapper.selectFree(pagevo);
//        }
//        System.out.println(boardList.size());
//        return boardList;
//    }

    @Override
    public List<BoardVO> boardAll(PageVO pagevo) throws Exception {
        return boardMapper.selectAll(pagevo);
    }

    @Override
    public List<BoardVO> boardList(PageVO pagevo, String category, int orderNumber) throws Exception {
        if(orderNumber==1){
            return boardMapper.selectBoardOrderByDate(pagevo,category);
        }else if(orderNumber==2){
            return boardMapper.selectBoardOrderByView(pagevo, category);
        }else if(orderNumber==3){
            return boardMapper.selectBoardOrderByLike(pagevo,category);
        }else{
            return null;
        }
    }

//    @Override
//    public List<BoardVO> boardHealthList(PageVO pagevo) {
//        return boardMapper.selectHealth( pagevo);
//    }
//    @Override
//    public List<BoardVO> boardCampaignList(PageVO pagevo) {
//        return boardMapper.selectCampaign( pagevo);
//    }
//    @Override
//    public List<BoardVO> boardMedList(PageVO pagevo) {
//        return boardMapper.selectMed(pagevo);
//    }
//    @Override
//    public List<BoardVO> boardFreeList(PageVO pagevo) {
//        return boardMapper.selectFree(pagevo);
//    }

    @Override
    public List<BoardVO> boardNoticeList(){
        return boardMapper.selectNotice();
    }

    @Override
    public int boardCount(String category) {
        if(category.equals("모든 글")){
            return boardMapper.countAll();
        }else{
            return boardMapper.countBoard(category);
        }
    }

    @Override
    public BoardVO modBoard(String no) throws Exception {
        return boardMapper.selectOne(no);
    }

    @Override
    public void modBoard(BoardVO boardVO) {
        selectCategory(boardVO);
//        System.out.println(boardVO.getBoard_sequence());
//        System.out.println(boardVO.getBoard_content());
//        System.out.println(boardVO.getBoard_category());
        boardMapper.updateBoard(boardVO);
    }

    @Override
    public BoardVO detail(String no) throws Exception {
        BoardVO boardVO=boardMapper.selectOne(no);
        boardVO.setBoard_viewcount((boardVO.getBoard_viewcount()+1));
//        System.out.println(boardVO.getBoard_title());
//        System.out.println(boardVO.getBoard_viewcount());
        boardMapper.plusViewCount(boardVO);
        return boardVO;
    }
    private void selectCategory(BoardVO boardVO){
        String category=boardVO.getBoard_category();
        if(category!=null){
            if(category.equals("0")) {
                boardVO.setBoard_category("공지사항");
            }else if(category.equals("1")){
                boardVO.setBoard_category("오늘의 건강");
            } else if (category.equals("2")) {
                boardVO.setBoard_category("캠페인");
            }else if (category.equals("3")) {
                boardVO.setBoard_category("의료정보");
            }else if (category.equals("4")) {
                boardVO.setBoard_category("자유게시판");
            }
        }
    }

    @Override
    public void delBoard(String no) throws Exception {
        boardMapper.deleteBoard(no);
    }

    public boolean checkLike(LikeVO likeVO){
        boolean flag =false;
        if(boardMapper.checkLike(likeVO)!=0) {
            flag = true;
            boardMapper.minusLikeCount(likeVO);
            boardMapper.delLike(likeVO);

        }else{
            boardMapper.plusLikeCount(likeVO);
            boardMapper.addLike(likeVO);
        }
        return flag;
    }
    public boolean firstLike(LikeVO likeVO){
        int check=boardMapper.checkLike(likeVO);
        boolean flag =false;
        if(check==1) {
            flag=true;
        }
        return flag;
    }

    public int countHeart(LikeVO likeVO){
        return boardMapper.selectHeart(likeVO);
    }
}
