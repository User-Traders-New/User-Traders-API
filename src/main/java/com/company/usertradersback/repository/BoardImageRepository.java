package com.company.usertradersback.repository;

import com.company.usertradersback.entity.BoardImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface BoardImageRepository extends JpaRepository<BoardImageEntity,Integer> {

    @Transactional
    @Query("select bi.path from BoardImageEntity bi " +
            "where bi.boardId.id = :boardId")
    List<String> findByPath(Integer boardId);

    @Transactional
    @Modifying
    @Query("delete from BoardImageEntity bi where bi.boardId.id = :boardId")
    void deleteAllByBoardId(Integer boardId);

    @Transactional
    List<BoardImageEntity> findAllByBoardId_Id(Integer boardId);

    @Transactional
    @Query("select bi.path from BoardImageEntity bi where bi.boardId.id = :boardId")
    List<String> selectThumbnailPath(Integer boardId);

    @Transactional
    Optional<String> findFirstByPath(Integer boardId);


}
