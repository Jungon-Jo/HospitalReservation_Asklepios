package com.asklepios.hospitalreservation_asklepios.Repository;
import com.asklepios.hospitalreservation_asklepios.VO.BoardVO;
import com.asklepios.hospitalreservation_asklepios.VO.PageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;


@Mapper
public interface IF_BoardMapper {
    public void insertOne(BoardVO boardVO);
    public List<BoardVO> selectHealth(PageVO pagevo);
    public List<BoardVO> selectCampaign(PageVO pagevo);
    public List<BoardVO> selectMed(PageVO pagevo);
    public List<BoardVO> selectFree(PageVO pagevo);
    public int countBoard();

    public BoardVO selectOne(String no);
    public void updateBoard(BoardVO boardVO);
}
