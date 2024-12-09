package com.asklepios.hospitalreservation_asklepios.Service;

import com.asklepios.hospitalreservation_asklepios.VO.HospitalVO;
import com.asklepios.hospitalreservation_asklepios.VO.Hospital_doctorVO;
import com.asklepios.hospitalreservation_asklepios.VO.ReviewVO;

import java.util.List;

public interface IF_SearchService {
    public List<HospitalVO> searchHospital(String name);
    public List<Hospital_doctorVO> searchInfo(String name);
    public List<Hospital_doctorVO> filterDate(List<Hospital_doctorVO> modHospitalList);
    public List<Hospital_doctorVO> filterIng(List<Hospital_doctorVO> modHospitalList);
    public List<ReviewVO> searchReview(String hospital_code);
}
