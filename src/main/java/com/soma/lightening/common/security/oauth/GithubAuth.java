package com.soma.lightening.common.security.oauth;

import lombok.Getter;

// 추상화해야함 나중에
@Getter
public class GithubAuth {
    private String clientId;
    private String secretId;
    private String domain;
    private String frontendDomain;

    public GithubAuth(String domain, String frontendDomain, String clientId, String secretId) {
        this.domain = domain;
        this.frontendDomain = frontendDomain;
        this.clientId = clientId;
        this.secretId = secretId;
    }
}
