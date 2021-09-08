package com.company.usertradersback.repository;

import com.company.usertradersback.entity.BoardParentCommentEntity;
import com.company.usertradersback.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;

public interface BoardParentCommentRepository extends JpaRepository<BoardParentCommentEntity,Integer> {

    @Transactional
    @Modifying
    @Query("delete from BoardParentCommentEntity bpc where bpc.userId = :user and bpc.id = :id")
    void deleteById(UserEntity user, Integer id);

    @Transactional
    @Query("select count(bpc.id) from BoardParentCommentEntity bpc where bpc.userId.id = :userId and bpc.id = :id")
    Integer exist(Integer userId, Integer id);
}
