package com.soma.lightening.post.dto;

import com.soma.lightening.post.domain.Emotion;
import com.soma.lightening.post.domain.EmotionType;
import lombok.Data;
import lombok.Getter;

@Data
@Getter
public class EmotionDto{
    private String username; // 참여자 아이디
    private String nickname;
    private EmotionType emotionType; // 참여 타입(관심, 참가)

    public EmotionDto(Emotion emotion) {
        this.username = emotion.getAccount().getUsername();
        this.nickname = emotion.getAccount().getNickname();
        this.emotionType = emotion.getEmotionType();
    }
}