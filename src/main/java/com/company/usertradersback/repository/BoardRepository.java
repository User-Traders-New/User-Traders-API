package com.company.usertradersback.repository;

import com.company.usertradersback.entity.BoardEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<BoardEntity,Integer>  {

    List<BoardEntity> findByTitleContaining(String title);

    @Query("select b from BoardEntity b " +
            "left join b.categoryId ci on b.categoryId = ci.id" +
            " left join ci.subCategoryId sci on ci.subCategoryId = sci.id where ci.id = :categoryId and sci.id= :subCategoryId")
    List<BoardEntity> selectAll(Integer categoryId,Integer subCategoryId);

    List<BoardEntity> findAllByUserId_Id(Integer userId);

}
