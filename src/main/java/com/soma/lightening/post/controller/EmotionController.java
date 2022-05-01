package com.soma.lightening.post.controller;

import com.soma.lightening.common.controller.dto.CommonResponse;
import com.soma.lightening.post.domain.EmotionType;
import com.soma.lightening.post.dto.EmotionRequestDto;
import com.soma.lightening.post.service.EmotionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class EmotionController {
    private final EmotionService emotionService;

    @PostMapping("/post/emotion")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<CommonResponse> requestEmotion(@Valid @RequestBody EmotionRequestDto emotionRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        EmotionType type = null;
        try {
            type = EmotionType.valueOf(emotionRequestDto.getType());
        } catch (Exception e) {
            type = EmotionType.PARTICIPATE;
        }

        emotionService.newEmotion(userDetails.getUsername(), emotionRequestDto.getPostId(), type);

        return ResponseEntity.ok(CommonResponse.builder()
                .response("성공")
                .build());
    }


    @DeleteMapping("/post/emotion")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<CommonResponse> removeEmotion(@RequestParam(value="postId") Long postId) {


        emotionService.deleteEmotion(postId);

        return ResponseEntity.ok(CommonResponse.builder()
                .response("성공")
                .build());
    }



}
