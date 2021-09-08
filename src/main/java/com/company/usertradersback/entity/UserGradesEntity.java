package com.company.usertradersback.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "UserGrades")
public class UserGradesEntity {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 평점 고유번호
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "userSendId")
    // 평점 주는 회원
    private UserEntity userSendId;

    @ManyToOne
    @JoinColumn(name = "userRecvId")
    // 평점 받는 회원
    private UserEntity userRecvId;

    @Column(name = "grade")
    // 평점 1~5점
    private Integer grade;

    @Builder
    public UserGradesEntity(Integer id,
                            UserEntity userSendId,
                            UserEntity userRecvId,
                            Integer grade) {
        this.id = id;
        this.userSendId = userSendId;
        this.userRecvId = userRecvId;
        this.grade = grade;
    }
}
