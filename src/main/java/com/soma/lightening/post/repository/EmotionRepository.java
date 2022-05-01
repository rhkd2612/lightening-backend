package com.soma.lightening.post.repository;

import com.soma.lightening.common.entity.OAuth2Account;
import com.soma.lightening.post.domain.Emotion;
import com.soma.lightening.post.domain.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
    @EntityGraph(attributePaths = "account")
    List<Emotion> findAllByAccount_Id(Long accountId);
    List<Emotion> findByAccountAndPost(OAuth2Account account, Post post);
}
