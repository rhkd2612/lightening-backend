package com.soma.lightening.post.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Builder
@Getter
public class WritePostRequestDto {
    @NotEmpty
    private String content;
    @NotEmpty
    private String appointmentDate;
    @NotNull
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    private Date recruitEndDate;
    @NotEmpty
    private String postTag;
    @NotNull
    private Integer maxCount;
}
