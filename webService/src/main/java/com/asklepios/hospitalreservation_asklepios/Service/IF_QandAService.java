package com.asklepios.hospitalreservation_asklepios.Service;

import com.asklepios.hospitalreservation_asklepios.VO.QuestionVO;
import com.asklepios.hospitalreservation_asklepios.VO.QuestionlistVO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface IF_QandAService {
    int save_text_w_img(List<MultipartFile> files, QuestionVO vo);
    int save_text(QuestionVO vo);
    boolean answer(QuestionVO vo,String userId);
    List<QuestionlistVO> getList();
    QuestionlistVO showdetail(int question_id);
    String getSubject(int questionId);
    //ai 답변 가져오기
    QuestionlistVO get_aiAnswer(int questionId);

}
