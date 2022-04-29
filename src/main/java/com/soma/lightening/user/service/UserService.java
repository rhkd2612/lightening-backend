package com.soma.lightening.user.service;

import com.soma.lightening.common.entity.OAuth2Account;
import com.soma.lightening.common.repository.OAuth2AccountRepository;
import com.soma.lightening.user.dto.UserInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final OAuth2AccountRepository oAuth2AccountRepository;

    @Transactional(readOnly = true)
    public UserInfoDto getMyUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        OAuth2Account account = oAuth2AccountRepository.findOneWithAuthoritiesByUsername(userDetails.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("이름을 찾을 수 없음"));

        return UserInfoDto.of(account);
    }
}
