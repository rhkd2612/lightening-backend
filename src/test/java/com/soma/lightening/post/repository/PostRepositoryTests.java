package com.soma.lightening.post.repository;

import com.soma.lightening.common.entity.OAuth2Account;
import com.soma.lightening.common.repository.OAuth2AccountRepository;
import com.soma.lightening.post.domain.*;
import com.soma.lightening.post.service.EmotionService;
import com.soma.lightening.post.service.PostService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@SpringBootTest
@ActiveProfiles("local")
public class PostRepositoryTests {
    @Autowired
    private PostRepository postRepository;
    @Autowired
    private PostService postService;
    @Autowired
    private EmotionRepository emotionRepository;
    @Autowired
    private EmotionService emotionService;
    @Autowired
    private OAuth2AccountRepository oAuth2AccountRepository;

    @Test
    @Transactional
    void postSaveTest() {
        //Post post = Post.newPost(null, "오늘 오후까지", PostTag.MEAL, new Date(),"aa",1024);
        //post = postRepository.save(post);
        //System.out.println(post.getDate());
    }


}
