package com.soma.lightening.common.service;

import com.soma.lightening.common.entity.Authority;
import com.soma.lightening.common.entity.OAuth2Account;
import com.soma.lightening.exception.error.OAuth2LoginFailedException;
import com.soma.lightening.common.security.oauth.GithubOAuthUserInfo;
import com.soma.lightening.common.repository.OAuth2AccountRepository;
import com.soma.lightening.common.security.TokenProvider;
import com.soma.lightening.common.security.oauth.GithubAuth;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OAuth2Service {
    private final WebClient githubWebClient;
    private final WebClient apiGithubWebClient;
    private final OAuth2AccountRepository oAuth2AccountRepository;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final GithubAuth githubAuth;

    public String getGithubLoginUrl(String redirect) {
        return "https://github.com/login/oauth/authorize" +
                "?client_id=" + githubAuth.getClientId() +
                "&redirect_uri=" + githubAuth.getDomain() + "/api/oauth2/code/github?redirect=" + redirect;
    }
    public String getFrontendUrl() {
        return githubAuth.getFrontendDomain();
    }

    public GithubOAuthUserInfo getGithubUserInfo(String code) throws OAuth2LoginFailedException {
        // 깃허브로부터 사용자 정보 조회를 위한 액세스토큰 파라미터 발급
        Map<String, Object> body = new HashMap<>();
        body.put("client_id", githubAuth.getClientId());
        body.put("client_secret", githubAuth.getSecretId());
        body.put("code", code);

        // access_token=gho_16C7e42F292c6912E7710c838347Ae178B4a&scope=repo%2Cgist&token_type=bearer
        String githubAccessTokenParams = githubWebClient.post()
                .uri("/login/oauth/access_token")
                .bodyValue(body)
                .retrieve()
                .bodyToMono(String.class)
                .doOnError(error -> {
                    // throw new error plz, 그리고 상위에서 리다이렉트
                    throw new OAuth2LoginFailedException();
                })
                .block();
        String githubAccessToken = githubAccessTokenParams
                .substring(githubAccessTokenParams.indexOf('=') + 1, githubAccessTokenParams.indexOf('&'));
        String tokenType = githubAccessTokenParams
                .substring(githubAccessTokenParams.lastIndexOf('=') + 1);

        System.out.println("code " + code);
        System.out.println("github access token params " + githubAccessTokenParams);
        // 해당 액세스토큰으로 사용자 조회해서 정보를 받는다.
        GithubOAuthUserInfo userInfo = apiGithubWebClient.get()
                .uri("/user")
                .header("Authorization", tokenType + " " + githubAccessToken)
                .retrieve()
                .bodyToMono(GithubOAuthUserInfo.class)
                .doOnError(error -> {
                    // throw new error plz, 그리고 상위에서 리다이렉트
                    throw new OAuth2LoginFailedException();
                })
                .block();

        return userInfo;
    }

    // 지금은 깃허브만 가능하지만 나중에는 구글, 네이버도 가능하게 GithubOAuthUserInfo 를 추상화하자
    @Transactional
    public String getGithubUserAccessToken(GithubOAuthUserInfo userInfo) {
        // 해당 정보가 디비에 존재하는지 확인
            // 사용자가 존재하다면 그 정보를 가지고 액세스토큰을 만들어 반환, username은 providerid + providertype
            // 사용자가 존재하지 않으면 userInfo로 사용자를 디비에 생성 후 액세스토큰을 만들어 반환, username은 providerid + providertype
        // 권한은 ROLE_USER로, 시큐리티 템플릿 로그인 참고
        final String PROVIDER = "github";
        final String NO_PASSWORD = "NO_PASSWORD";

        String username = userInfo.getId() + PROVIDER; // providerid, provider로 username을 사용
        OAuth2Account account = oAuth2AccountRepository.findOneWithAuthoritiesByUsername(username)
                .orElseGet(()->null);
        if(account == null) {
            // 새로 만들어라, 회원가입
            Authority authority = Authority.builder()
                    .authorityName("ROLE_USER")
                    .build();

            // 생성자 안에서 username을 providerid + provider로 지정한다.
            account = oAuth2AccountRepository.saveAndFlush(OAuth2Account.builder()
                            .providerId(userInfo.getId())
                            .provider(PROVIDER)
                            .password(passwordEncoder.encode(NO_PASSWORD))
                            .nickname(userInfo.getName())
                            .profileImage(userInfo.getAvatar_url())
                            .authorities(Collections.singleton(authority))
                            .activated(true)
                    .build());
        }
        // 받아온 유저네임과 패스워드를 이용해 UsernamePasswordAuthenticationToken 객체 생성, oauth이므로 패스워드는 NO_PASSWORD
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, NO_PASSWORD);
        // authenticationToken 객체를 통해 Authentication 객체 생성
        // 이 과정에서 CustomUserDetailsService 에서 우리가 재정의한 loadUserByUsername 메서드 호출
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);
        // 그 객체를 시큐리티 컨텍스트에 저장
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 인증 정보를 기준으로 jwt access 토큰 생성
        String accessToken = tokenProvider.createToken(authentication);
        return accessToken;
    }
}
