package com.asklepios.hospitalreservation_asklepios.VO;

import lombok.*;

import java.io.Serializable;

@Data
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DoctorVO implements Serializable {
    private String user_doctor_code;
    private String user_doctor_id;
    private String user_doctor_hospital_code;
    private String user_doctor_medical;
    private String user_doctor_career;
    private String user_doctor_graduate;
}
