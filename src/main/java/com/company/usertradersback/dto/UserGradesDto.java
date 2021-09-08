package com.company.usertradersback.dto;

import com.company.usertradersback.entity.UserEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserGradesDto {

    // 평점 고유번호
    private Integer id;

    // 평점 주는 회원
    private UserEntity userSendId;

    // 평점 받는 회원
    private UserEntity userRecvId;

    // 평점 1~5점
    private Integer grade;

    @Builder
    public UserGradesDto(Integer id,
                            UserEntity userSendId,
                            UserEntity userRecvId,
                            Integer grade){
        this.id = id;
        this.userSendId = userSendId;
        this.userRecvId = userRecvId;
        this.grade =grade;
    }
}
