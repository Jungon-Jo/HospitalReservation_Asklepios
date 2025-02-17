package com.asklepios.hospitalreservation_asklepios.VO;

import java.io.Serializable;
import java.util.Map;

public class IM_GoogleUserInfo implements IF_OAuth2UserInfo, Serializable{
    private Map<String, Object> attributes;

    public IM_GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }
    @Override
    public String getProviderId() {
        return (String) attributes.get("sub");
    }

    @Override
    public String getProvider() {
        return "google";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }

    @Override
    public String getPhoneNumber() {
        return null;
    }
}
