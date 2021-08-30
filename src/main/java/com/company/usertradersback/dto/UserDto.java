package com.company.usertradersback.dto;

import com.company.usertradersback.entity.DepartmentEntity;
import com.company.usertradersback.entity.UserEntity;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
//회원
public class UserDto {

    // 회원 고유번호
    private Integer id;

    // 회원 이메일
    private String email;

    // 회원 비밀번호
    private String password;

    // 회원 이름
    private String userName;

    // 회원 닉네임
    private String nickname;

    // 회원 학과 고유번호
    private DepartmentEntity departmentId;

    // 회원 학번
    private String studentId;

    // 회원 성별 0:남자,1:여자
    private Integer gender;

    // 회원 역활
    private List<String> roles = new ArrayList<>();

    // 회원 로그인 종류 0:일반,1:카카오
    private Integer loginType;

    // 회원 프로필 이미지
    private String imagePath;

    // 회원 등록 날짜
    private LocalDateTime createAt;

    // 회원 수정 날짜
    private LocalDateTime modifiedAt;

    @Builder
    public UserDto(Integer id, String email, String password, String userName, String nickname,
                   DepartmentEntity departmentId, String studentId,
                   Integer gender, List<String> roles, Integer loginType, String imagePath,
                   LocalDateTime createAt, LocalDateTime modifiedAt) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.userName = userName;
        this.nickname = nickname;
        this.departmentId = departmentId;
        this.studentId = studentId;
        this.gender = gender;
        this.roles = roles;
        this.loginType = loginType;
        this.imagePath = imagePath;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
    }

    //회원 정보 추가를 위한 엔티티 - 디티오 변환 ,그러나 안쓰고 직접 빌드 하여 사용했다.
    public UserEntity convertDtoToEntity() {
        return UserEntity.builder()
                .id(id)
                .email(email)
                .password(password)
                .userName(userName)
                .nickname(nickname)
                .departmentId(departmentId)
                .studentId(studentId)
                .gender(gender)
                .roles(roles)
                .loginType(loginType)
                .imagePath(imagePath)
                .createAt(createAt)
                .modifiedAt(modifiedAt)
                .build();
    }

    //나중에 쓸 일 있을것 같아서 유저정보들 객체에 담아놨음
    public UserDto UserEntityToDto(UserEntity userEntity) {
        return UserDto.builder()
                .id(userEntity.getId())
                .build();
    }


}