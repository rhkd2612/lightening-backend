package com.soma.lightening.post.domain;

import com.soma.lightening.common.entity.OAuth2Account;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@Table(name="emotion")
public class Emotion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="emotion_id")
    private Long id;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="account_id")
    private OAuth2Account account;

    @Column(name = "date")
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date date = new Date();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="post_id")
    private Post post;

    @Enumerated(EnumType.STRING)
    @Column(name = "emotion_type")
    private EmotionType emotionType; // PARTICIPATE, WILLING

    public static Emotion newEmotion(OAuth2Account account, Post post, EmotionType emotionType) {
        Emotion emotion = new Emotion();
        emotion.setAccount(account);
        emotion.setEmotionType(emotionType);
        emotion.setPost(post);
        return emotion;
    }
}
