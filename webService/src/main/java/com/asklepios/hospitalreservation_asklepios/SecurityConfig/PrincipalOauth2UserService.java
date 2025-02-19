package com.asklepios.hospitalreservation_asklepios.SecurityConfig;

import com.asklepios.hospitalreservation_asklepios.Service.IF_UserService;
import com.asklepios.hospitalreservation_asklepios.VO.IF_OAuth2UserInfo;
import com.asklepios.hospitalreservation_asklepios.VO.IM_GoogleUserInfo;
import com.asklepios.hospitalreservation_asklepios.VO.IM_NaverUserInfo;
import com.asklepios.hospitalreservation_asklepios.VO.UserVO;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {
    private final IF_UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        System.out.println("getClientRegistration: "+userRequest.getClientRegistration().getRegistrationId());
//        System.out.println("getAccessToken: "+userRequest.getAccessToken());
//        System.out.println("getAttributes:"+super.loadUser(userRequest).getAttributes());

        OAuth2User oAuth2User = super.loadUser(userRequest);
        IF_OAuth2UserInfo oAuth2UserInfo=null;
        String provider=userRequest.getClientRegistration().getRegistrationId();
        if(provider.equals("google")){
            oAuth2UserInfo=new IM_GoogleUserInfo(oAuth2User.getAttributes());
        }else if(provider.equals("naver")){
            oAuth2UserInfo=new IM_NaverUserInfo((Map)oAuth2User.getAttributes().get("response"));
        }
//        String provideId=oAuth2User.getAttribute("sub");
        System.out.println("oAuth2UserInfo: "+oAuth2UserInfo);
        String email= oAuth2UserInfo.getEmail();
        String username=oAuth2UserInfo.getName();
        String userId=provider.charAt(0)+"_"+email.split("@")[0];
//        String picture=oAuth2User.getAttribute("picture");
        String password="OAuth2";
        String role="scClient";
        String phoneNumber=oAuth2UserInfo.getPhoneNumber();
        Optional<UserVO>user= Optional.ofNullable(userService.printOneInfo(userId));
        //DB에 저장여부 확인
        if(user.isEmpty()) {
            UserVO newUser = UserVO.builder()
                    .user_id(userId)
                    .user_email(email)
                    .user_name(username)
                    .user_password(passwordEncoder.encode(password))
                    .user_authority(role)
                    .user_tel(phoneNumber)
//                    .user_image(picture)
//                    .user_info_agreement("agree")
                    .build();
            userService.addUserCommonInfo(newUser);
            System.out.println(newUser.toString());
        }
        UserVO loginUser=userService.printOneInfo(userId);
//        System.out.println(loginUser.toString());
        loginUser.setUser_name(userId);
        loginUser.setUser_authority("ROLE_"+role);
        return new PrincipalDetails(loginUser,oAuth2User.getAttributes());

//           return new PrincipalDetails(newUser,oAuth2User.getAttributes());
//        }else{
//            System.out.println(user.toString());
//            return new PrincipalDetails(user.get(),oAuth2User.getAttributes());
//        }
    }
}
