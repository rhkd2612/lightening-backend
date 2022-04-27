package com.soma.lightening.common.controller;

import com.soma.lightening.common.controller.dto.CommonResponse;
import com.soma.lightening.exception.error.OAuth2LoginFailedException;
import com.soma.lightening.common.security.oauth.GithubOAuthUserInfo;
import com.soma.lightening.common.service.OAuth2Service;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {
    private final OAuth2Service oAuth2Service;


    @GetMapping("/authenticate/github") // 깃허브 인증요청 API
    public ResponseEntity<CommonResponse> authorizeGithub(String redirect) throws IOException {
        // 깃허브 로그인 화면 URL을 응답한다
        CommonResponse response = CommonResponse.builder()
                .response(oAuth2Service.getGithubLoginUrl(redirect))
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // 일단 지금은 구현하고 구조는 나중에 개선
    @GetMapping("/oauth2/code/github") // 깃허브 callback url 경로
    public void getGithubOAuth2Code(HttpServletResponse response, String code, String redirect) throws IOException {
        // 액세스토큰을 파라미터로 넣어 프론트엔드 경로로 리다이렉트 시킨다.
        System.out.println(redirect);
        String accessToken = null;
        try {
            // 깃허브 서버로부터 해당 코드를 통해 액세스토큰을 발급받고 그 액세스토큰으로 사용자 정보를 조회해 반환
            GithubOAuthUserInfo userInfo = oAuth2Service.getGithubUserInfo(code);
            System.out.println(userInfo.getName());
            // 해당 정보를 이용해 액세스토큰 발급
            accessToken = oAuth2Service.getGithubUserAccessToken(userInfo);
            // 응답, 액세스토큰을 쿼리스트링으로 프론트엔드에 보낸다.
            // 그럼 프론트엔드는 토큰을 파라미터에서 꺼낸다
        } catch (OAuth2LoginFailedException e) {
            // OAuth 로그인 실패 시
            e.printStackTrace();
            response.sendRedirect(oAuth2Service.getFrontendUrl()); // 기본 경로로 리다이렉트
        }
        response.sendRedirect(oAuth2Service.getFrontendUrl() + "/redirect?access_token=" + accessToken + "&redirect=" + redirect);
    }
}
