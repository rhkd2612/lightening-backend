package com.soma.lightening.common.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "oauth_account")
@Getter
@NoArgsConstructor
public class OAuth2Account {

    @Id
    @Column(name = "account_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username") // oauth에서는 providerId + provider
    private String username;

    @Column(name = "password")
    private String password;

    @Column(name = "provider")
    private String provider; // github, naver

    @Column(name = "provider_id")
    private String providerId; // oauth2 id

    @Column(name = "profile_image")
    private String profileImage;

    @Column(name = "token_weight")
    private Long tokenWeight; // 리프레시 토큰 가중치, // 리프레시 토큰 안에 기입된 가중치가 tokenWeight 보다 작을경우 해당 토큰은 유효하지않음

    @Column(name = "nickname", length = 50)
    private String nickname;

    @Column(name = "activated")
    private boolean activated;

    @ManyToMany
    @JoinTable( // JoinTable은 테이블과 테이블 사이에 별도의 조인 테이블을 만들어 양 테이블간의 연관관계를 설정 하는 방법
            name = "account_authority",
            joinColumns = {@JoinColumn(name = "account_id", referencedColumnName = "account_id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "authority_name")})
    private Set<Authority> authorities;

    @Builder
    public OAuth2Account(String password, String provider, String providerId, String profileImage, String nickname, Set<Authority> authorities, boolean activated) {
        this.username = providerId + provider;
        this.password = password;
        this.profileImage = profileImage;

        this.provider = provider;
        this.providerId = providerId;
        this.nickname = nickname;
        this.authorities = authorities;
        this.activated = activated;
        this.tokenWeight = 1L; // 초기 가중치는 1
    }

    public void increaseTokenWeight() {
        this.tokenWeight++;
    }
}
