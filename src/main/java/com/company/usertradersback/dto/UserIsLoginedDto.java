package com.company.usertradersback.dto;

import java.time.LocalDateTime;

public class UserIsLoginedDto {
    // 회원 고유번호
    private Integer id;

    // 로그인 상태 0:로그아웃,1:로그인
    private Integer status;

    // 로그인시간
    private LocalDateTime loginAt;

    // 로그아웃시간
    private LocalDateTime logoutAt;

}
