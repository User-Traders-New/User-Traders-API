package com.company.usertradersback.dto;

import com.company.usertradersback.payload.Payload;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserTokenDto {

    // 고정 페이로드
    private Payload payload;

    // 로그인 토큰값
    private String token;


    @Builder
    public UserTokenDto(Payload payload,String token){
        this.payload = payload;
        this.token =token;
    }

}
