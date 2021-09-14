package com.company.usertradersback.dto.user;

import com.company.usertradersback.payload.Payload;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
//로그인시 해당 회원에게 토큰값을 주는 UserTokenDto (responseDto)
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
