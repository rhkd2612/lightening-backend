package com.soma.lightening.common.repository;

import com.soma.lightening.common.entity.OAuth2Account;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OAuth2AccountRepository extends JpaRepository<OAuth2Account, Long> {
    @EntityGraph(attributePaths = "authorities") // 엔티티그래프 통해 EAGER로 가져온다.
    Optional<OAuth2Account> findOneWithAuthoritiesByUsername(String username); // user를 기준으로 유저를 조회할 때 권한정보도 가져온다.
}