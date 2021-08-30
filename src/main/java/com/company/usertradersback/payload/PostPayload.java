package com.company.usertradersback.payload;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class PostPayload {
    private String message;
    private boolean isSuccess;
    private HttpStatus httpStatus;
    private String token;
    private boolean valid;

    public PostPayload(){

    }

    //회원가입 builder
    public PostPayload(String message,boolean isSuccess,HttpStatus httpStatus){
        this.message = message;
        this.isSuccess = isSuccess;
        this.httpStatus = httpStatus;
    }
    // 로그인 builder
    public PostPayload(String token,String message,boolean isSuccess,HttpStatus httpStatus){
        this.token = token;
        this.message = message;
        this.isSuccess = isSuccess;
        this.httpStatus = httpStatus;
    }
    // 토큰 유효성 검사 builder
    public PostPayload(boolean valid,String message,boolean isSuccess,HttpStatus httpStatus){
        this.valid = valid;
        this.message = message;
        this.isSuccess = isSuccess;
        this.httpStatus = httpStatus;
    }

    public PostPayload(String message,boolean isSuccess,HttpStatus httpStatus,String token,boolean valid){
        this.message = message;
        this.isSuccess = isSuccess;
        this.httpStatus = httpStatus;
        this.token = token;
        this.valid = valid;
    }
}
