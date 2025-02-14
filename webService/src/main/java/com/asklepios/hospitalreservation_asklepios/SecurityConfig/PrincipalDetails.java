package com.asklepios.hospitalreservation_asklepios.SecurityConfig;

import com.asklepios.hospitalreservation_asklepios.VO.MemberVO;
import com.asklepios.hospitalreservation_asklepios.VO.UserVO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class PrincipalDetails implements OAuth2User {
    @Getter
    private UserVO user;
    private String username;
    private String password;
    private Map<String, Object> attributes;

    public PrincipalDetails(UserVO user, Map<String, Object> attributes) {
        this.user=user;
    }
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public <A> A getAttribute(String name) {
        return OAuth2User.super.getAttribute(name);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(user.getUser_authority()));
    }

    @Override
    public String getName() {
        return "";
    }
}
