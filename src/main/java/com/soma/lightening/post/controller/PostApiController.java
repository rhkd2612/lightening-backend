package com.soma.lightening.post.controller;

import com.soma.lightening.common.controller.dto.CommonResponse;
import com.soma.lightening.post.domain.*;
import com.soma.lightening.post.dto.PostDto;
import com.soma.lightening.post.dto.WritePostRequestDto;
import com.soma.lightening.post.repository.PostRepository;
import com.soma.lightening.post.service.PostService;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostApiController {
    private final PostService postService;

    @GetMapping("/posts")
    public Page<PostDto> posts(Pageable pageable,
                               @RequestParam(value="tag", required = false) String postTag,
                               @RequestParam(value="type", required = false) String postType){
        PostTag curTag;
        PostType curType;
        try{ curTag = PostTag.valueOf(postTag);} catch(Exception e){curTag = null;}
        try{ curType = PostType.valueOf(postType);} catch(Exception e){curType = null;}

        if(curTag == null && curType == null)
            return postService.findPosts(pageable);
        else if(curTag == null)
            return postService.findPostsByType(curType, pageable);
        else if(curType == null)
            return postService.findPostsByTag(curTag, pageable);
        return postService.findPostsByTagAndType(curTag, curType, pageable);
    }

    @PostMapping("/post")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<CommonResponse> writePost(@Valid @RequestBody WritePostRequestDto writePostRequestDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        PostTag tag = null;
        try {
            tag = PostTag.valueOf(writePostRequestDto.getPostTag());
        } catch (Exception e) {
            tag = PostTag.ETC;
        }
        postService.newPost(userDetails.getUsername(),
                writePostRequestDto.getAppointmentDate(),
                tag,
                writePostRequestDto.getRecruitEndDate(),
                writePostRequestDto.getContent(),
                writePostRequestDto.getMaxCount());

        return ResponseEntity.ok(CommonResponse.builder()
                        .response("OK")
                .build());
    }
}
