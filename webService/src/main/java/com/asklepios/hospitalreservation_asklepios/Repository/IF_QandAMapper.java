package com.asklepios.hospitalreservation_asklepios.Repository;

import com.asklepios.hospitalreservation_asklepios.VO.QuestionVO;
import com.asklepios.hospitalreservation_asklepios.VO.Question_imgVO;
import com.asklepios.hospitalreservation_asklepios.VO.QuestionlistVO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface IF_QandAMapper {

    //이미지 첨부
    @Insert("INSERT INTO img_question(fileName,question_id,origin) values(#{fileName},#{question_id},#{origin})")
    void save_qanda_img(Question_imgVO vo);

    //질문글
    //tag랑 sub 필수로 할지말지 고민중--우선 필수로 했는데 바꿀수도
    @Insert("INSERT INTO question(title,content,tag,sub,user_id) values(#{title},#{content},#{tag},#{sub},#{user_id})")
    @Options(useGeneratedKeys = true, keyProperty ="id")
    void save_qanda_text(QuestionVO vo);

    //답변
    @Insert("INSERT INTO answer(question_id,content,user_doctor_id,hospital_name,user_doctor_medical) values(#{id},#{content},#{user_id},#{sub},#{tag})")
    void save_answer(QuestionVO listvo);

    //화면 리스트 가져옴
    List<QuestionlistVO> list();

    //질문 자세히 보기
    QuestionlistVO detailQandA(int questionId);
   //답변테이블 데이터 만들기 전 통합데이터 구함
   QuestionVO pre_getDoctorInfo(String doctor_id);
    //질문 자세히 보기 -- 의사 정보 가져오기
    List<QuestionlistVO> show_detail_getDoctorInfo(int question_id);

    //질문 제목 가져옴
    @Select("SELECT title FROM question where id=#{questionId}")
    String getSubjuct(int questionId);

    //ai 답변 가져옴
    @Select("SELECT CONTENT as content,DATE as date,AI_SUB as tag,AI_NAME as sub FROM AI_ANSWER WHERE QUESTION_ID =#{questionId}")
    QuestionlistVO getAIAnswer(int questionId);
}
