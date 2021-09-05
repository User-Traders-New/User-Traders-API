package com.company.usertradersback.repository;

import com.company.usertradersback.entity.BoardImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardImageRepository extends JpaRepository<BoardImageEntity,Integer> {

    @Query("select bi.path from BoardImageEntity bi " +
            "where bi.boardId.id = :boardId")
    List<String> findByPath(Integer boardId);

    @Modifying
    @Query("delete from BoardImageEntity bi where bi.boardId.id = :boardId")
    void deleteAllByBoardId(Integer boardId);
}
