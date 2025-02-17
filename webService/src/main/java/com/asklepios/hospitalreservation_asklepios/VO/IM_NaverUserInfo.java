package com.asklepios.hospitalreservation_asklepios.VO;

import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.Serializable;
import java.util.Map;

public class IM_NaverUserInfo implements IF_OAuth2UserInfo, Serializable {
    private Map<String, Object> attributes;

    public IM_NaverUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return (String)attributes.get("email");
    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }

    @Override
    public String getPhoneNumber() {
        return (String)attributes.get("phone");
    }
}
