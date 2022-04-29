package com.soma.lightening.user.controller;


import com.soma.lightening.user.dto.UserInfoDto;
import com.soma.lightening.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/user")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<UserInfoDto> getMyUserInfo() {
        return new ResponseEntity<>(userService.getMyUserInfo(), HttpStatus.OK);
    }
}
