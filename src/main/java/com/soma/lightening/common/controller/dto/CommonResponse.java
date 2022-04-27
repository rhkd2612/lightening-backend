package com.soma.lightening.common.controller.dto;


import lombok.Builder;
import lombok.Getter;

import java.util.Date;
import java.util.UUID;

@Getter
@Builder //.build로 객체 생성하게 해줌
public class CommonResponse {
    private Object response;
    private Object error;
}