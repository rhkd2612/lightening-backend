package com.soma.lightening.post.service;

import com.soma.lightening.common.entity.OAuth2Account;
import com.soma.lightening.common.repository.OAuth2AccountRepository;
import com.soma.lightening.post.domain.Post;
import com.soma.lightening.post.domain.PostTag;
import com.soma.lightening.post.domain.PostType;
import com.soma.lightening.post.dto.PostDto;
import com.soma.lightening.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final OAuth2AccountRepository accountRepository;

    // newPost는 여기에 작성하면 된다 (readOnly = false)
    // account의 정보는 accountRepository.findOne
    @Transactional
    public Long newPost(String username, String appointmentDate, PostTag postTag, Date recruitEndDate, String content, int maxCount){
        OAuth2Account account = accountRepository.findOneWithAuthoritiesByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("이름 없음"));

        Post post = Post.newPost(account, appointmentDate, postTag, recruitEndDate, content, maxCount);
        postRepository.save(post);

        return post.getId();
    }

    public Page<PostDto> findPosts(Pageable pageable){
        return postRepository.findAll(pageable).map(p -> new PostDto(p));
    }

    public Page<PostDto> findPostsByTag(PostTag postTag, Pageable pageable){
        return postRepository.findAllByPostTag(postTag, pageable).map(p -> new PostDto(p));
    }

    public Page<PostDto> findPostsByType(PostType postType, Pageable pageable){
        return postRepository.findAllByPostType(postType, pageable).map(p -> new PostDto(p));
    }

    public Page<PostDto> findPostsByTagAndType(PostTag postTag, PostType postType, Pageable pageable){
        return postRepository.findAllByPostTagAndPostType(postTag, postType,pageable).map(p -> new PostDto(p));
    }

    public Post findPostById(Long postId) {
        return postRepository.findById(postId).get();
    }
}
