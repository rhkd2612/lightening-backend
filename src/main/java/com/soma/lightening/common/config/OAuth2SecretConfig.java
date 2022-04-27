package com.soma.lightening.common.config;

import com.soma.lightening.common.security.oauth.GithubAuth;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 클라이언트 아이디, 시크릿 아이디 주입 설정파일
@Configuration
public class OAuth2SecretConfig {
    @Value("${oauth.domain}")
    private String domain;
    @Value("${oauth.frontend-domain}")
    private String frontendDomain;
    @Value("${oauth.github.clientId}")
    private String githubClientId;
    @Value("${oauth.github.secretId}")
    private String githubSecretId;

    @Bean
    public GithubAuth githubAuth() {
        return new GithubAuth(domain, frontendDomain, githubClientId, githubSecretId);
    }
}
