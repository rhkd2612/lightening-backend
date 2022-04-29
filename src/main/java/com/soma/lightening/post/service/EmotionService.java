package com.soma.lightening.post.service;

import com.soma.lightening.common.entity.OAuth2Account;
import com.soma.lightening.common.repository.OAuth2AccountRepository;
import com.soma.lightening.exception.error.DuplicateMemberException;
import com.soma.lightening.post.domain.Emotion;
import com.soma.lightening.post.domain.EmotionType;
import com.soma.lightening.post.domain.Post;
import com.soma.lightening.post.repository.EmotionRepository;
import com.soma.lightening.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EmotionService {
    private final EmotionRepository emotionRepository;
    private final PostRepository postRepository;
    private final OAuth2AccountRepository accountRepository;

    @Transactional
    public Long newEmotion(String username, Long postId, EmotionType emotionType){
        OAuth2Account account = accountRepository.findOneWithAuthoritiesByUsername(username)
                .orElseThrow(()->new UsernameNotFoundException("이름 찾을 수 없음"));
        Post post = postRepository.findById(postId).get();

        // 이후 예외처리 사용
        if(account.getId() == post.getAccount().getId()) throw new DuplicateMemberException();

        // 이미 눌렀으면 불가능하게
        Emotion beforeEmotion = emotionRepository.findByAccountAndPost(account, post);
        if(beforeEmotion != null) throw new DuplicateMemberException();

        Emotion emotion = Emotion.newEmotion(account, post, emotionType);
        emotionRepository.save(emotion);
        post.addEmotion(emotion);

        return emotion.getId();
    }

    public List<Emotion> findEmotionsByAccountId(Long accountId){
        return emotionRepository.findAllByAccount_Id(accountId);
    }
}
