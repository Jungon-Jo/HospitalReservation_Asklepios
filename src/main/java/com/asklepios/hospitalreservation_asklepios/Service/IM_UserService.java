package com.asklepios.hospitalreservation_asklepios.Service;

import com.asklepios.hospitalreservation_asklepios.Repository.IF_UserMapper;
import com.asklepios.hospitalreservation_asklepios.VO.UserVO;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IM_UserService implements IF_UserService{
    @Autowired
    IF_UserMapper usermapper;

    @Override
    public boolean login(UserVO userVO) {
        String pwd = usermapper.selectPwd(userVO);
        if(userVO.getUser_password().equals(pwd)){
//            System.out.println("일치");
            return true;
        }else{
//            System.out.println("불일치");
            return false;
        }
    }

    @Override
    public String findId(String user_name, String reg_first, String reg_last) {
        int cnt = 0;
        List<UserVO> ulist = usermapper.selectAllName();
        String reg_num = reg_first.concat(reg_last);
//        System.out.println(reg_num);
//        System.out.println(register_number);
        // 유저 목록에 사용자가 있는지 확인
        // 있으면 조회
        // 없으면 없다고 통보
        for(UserVO userVO : ulist){
//            System.out.println(userVO.getUser_name());
            if(user_name.equals(userVO.getUser_name())){
//                System.out.println("일치");
                cnt++;
            }
        }

        if(cnt > 0){
            String register_number = usermapper.selectRegnum(user_name);
            if(reg_num.equals(register_number)){
//                System.out.println("일치");
                String user_id = usermapper.selectId(reg_num);
                int idx = user_id.length()/2;
                String str = user_id.substring(0, idx) + user_id.replaceAll(".","*").substring(idx);
                return str;
            }else {
//                System.out.println("불일치");
                return null;
            }
        }else{
            return null;
        }
    }

}
