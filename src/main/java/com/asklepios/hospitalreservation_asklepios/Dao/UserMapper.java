package com.asklepios.hospitalreservation_asklepios.Dao;

import com.asklepios.hospitalreservation_asklepios.VO.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    String selectPwd(UserVO userVO);
}
