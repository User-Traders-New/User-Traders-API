package com.company.usertradersback.dto;

import com.company.usertradersback.payload.Payload;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserValidDto {
    // 고정 페이로드
    private Payload payload;

    // 로그인 토큰값
    private boolean valid;


    @Builder
    public UserValidDto(Payload payload,boolean valid){
        this.payload = payload;
        this.valid = valid;
    }
}
