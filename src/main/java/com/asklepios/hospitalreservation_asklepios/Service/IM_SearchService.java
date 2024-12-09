package com.asklepios.hospitalreservation_asklepios.Service;

import com.asklepios.hospitalreservation_asklepios.Repository.IF_SearchMapper;
import com.asklepios.hospitalreservation_asklepios.VO.HospitalVO;
import com.asklepios.hospitalreservation_asklepios.VO.Hospital_doctorVO;
import com.asklepios.hospitalreservation_asklepios.VO.ReviewVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
@Service
public class IM_SearchService implements IF_SearchService{
    @Autowired
    IF_SearchMapper searchMapper;
    @Override
    public List<HospitalVO> searchHospital(String name) {
        return searchMapper.selectHospital(name);
    }

    @Override
    public List<Hospital_doctorVO> searchInfo(String name) {
        List<Hospital_doctorVO> list=searchMapper.selectInfo(name);
//        System.out.println(list.toString());
        return searchMapper.selectInfo(name);
    }


    @Override
    public List<Hospital_doctorVO> filterDate(List<Hospital_doctorVO> hospitalList) {
        List<Hospital_doctorVO> modHospitalList = hospitalList;
        modHospitalList.removeIf(hospitalVO -> hospitalVO.getHospital_date().equals("n"));
//        System.out.println(modHospitalList.size());
        return modHospitalList;
    }

    @Override
    public List<Hospital_doctorVO> filterIng(List<Hospital_doctorVO> hospitalList) {
        List<Hospital_doctorVO> modHospitalList=hospitalList;
        LocalTime localTime=LocalTime.now();
        int nowHour= localTime.getHour();
        int nowMinute=localTime.getMinute();
//        System.out.println(nowHour+":"+nowMinute);
//        System.out.println(localTime);
//        System.out.println(localTime.getHour());
//        System.out.println(localTime.getMinute());
//        System.out.println(modHospitalList.get(0).getHospital_time().substring(0,2));
//        System.out.println(modHospitalList.get(0).getHospital_time().substring(6,8));
        for(int i=0;i<modHospitalList.size();i++){
            int startTime=Integer.parseInt(modHospitalList.get(i).getHospital_time().substring(0,2));
            int endTime=Integer.parseInt(modHospitalList.get(i).getHospital_time().substring(6,8));
            int startMin=Integer.parseInt(modHospitalList.get(i).getHospital_time().substring(3,5));
            int endMin=Integer.parseInt(modHospitalList.get(i).getHospital_time().substring(9));
//            System.out.println(modHospitalList.get(i).getHospital_name());
//           System.out.println("시작시간");
//            System.out.println(startTime+":"+startMin);
//            System.out.println("종료시간");
//            System.out.println(endTime+":"+endMin);
            if(nowHour<startTime||nowHour>endTime){
                modHospitalList.remove(i);
                i--;
            }else if(nowHour==startTime){
                if(nowMinute<startMin){
                    modHospitalList.remove(i);
                    i--;
                }
            }else if(nowHour==endTime){
                if(nowMinute>=endMin){
                    modHospitalList.remove(i);
                    i--;
                }
            }

        }
//        for (HospitalVO hospitalVO : modHospitalList) {
//            System.out.println(hospitalVO.getHospital_time());
//        }
        return modHospitalList;

    }

    @Override
    public List<ReviewVO> searchReview(String hospital_code) {
        return searchMapper.selectReview(hospital_code);
    }
}
