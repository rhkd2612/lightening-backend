package com.soma.lightening.post.dto;

import com.soma.lightening.post.domain.EmotionType;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotNull;

@Builder
@Getter
public class EmotionRequestDto {
    @NotNull
    private Long postId;
    @NotNull
    private String type;
}
