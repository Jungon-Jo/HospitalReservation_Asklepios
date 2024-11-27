package com.asklepios.hospitalreservation_asklepios.Service;

import com.asklepios.hospitalreservation_asklepios.Repository.IF_BoardMapper;
import com.asklepios.hospitalreservation_asklepios.VO.BoardVO;
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
        String category=boardVO.getBoard_category();
        if(category!=null){
            if(category.equals("1")){
                boardVO.setBoard_category("오늘의 건강");
            } else if (category.equals("2")) {
                boardVO.setBoard_category("캠페인");
            }else if (category.equals("3")) {
                boardVO.setBoard_category("의료기기");
            }else if (category.equals("4")) {
                boardVO.setBoard_category("자유게시판");
            }
        }
        boardMapper.insertOne(boardVO);
    }

    @Override
    public List<BoardVO> boardAll(PageVO pagevo) throws Exception {
        return boardMapper.selectAll(pagevo);
    }

    @Override
    public List<BoardVO> boardHealthList(PageVO pagevo) {
        return boardMapper.selectHealth( pagevo);
    }
    @Override
    public List<BoardVO> boardCampaignList(PageVO pagevo) {
        return boardMapper.selectCampaign( pagevo);
    }
    @Override
    public List<BoardVO> boardMedList(PageVO pagevo) {
        return boardMapper.selectMed(pagevo);
    }
    @Override
    public List<BoardVO> boardFreeList(PageVO pagevo) {
        return boardMapper.selectFree(pagevo);
    }

    @Override
    public int boardCount() {
        return boardMapper.countBoard();
    }

    @Override
    public BoardVO modBoard(String no) throws Exception {

        return boardMapper.selectOne(no);
    }

    @Override
    public void modBoard(BoardVO boardVO) {
        String category=boardVO.getBoard_category();
        if(category!=null){
            if(category.equals("1")){
                boardVO.setBoard_category("오늘의 건강");
            } else if (category.equals("2")) {
                boardVO.setBoard_category("캠페인");
            }else if (category.equals("3")) {
                boardVO.setBoard_category("의료기기");
            }else if (category.equals("4")) {
                boardVO.setBoard_category("자유게시판");
            }
        }
//        System.out.println(boardVO.getBoard_sequence());
//        System.out.println(boardVO.getBoard_content());
//        System.out.println(boardVO.getBoard_category());
        boardMapper.updateBoard(boardVO);
    }

    @Override
    public BoardVO detail(String no) throws Exception {
        BoardVO boardVO=boardMapper.selectOne(no);
        boardVO.setBoard_viewcount(
                Integer.toString(Integer.parseInt(boardVO.getBoard_viewcount())+1)
        );
//        System.out.println(boardVO.getBoard_title());
//        System.out.println(boardVO.getBoard_viewcount());
        boardMapper.plusViewCount(boardVO);
        return boardVO;
    }


}
