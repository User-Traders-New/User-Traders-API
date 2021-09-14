package com.company.usertradersback.repository;

import com.company.usertradersback.entity.QBoardImageEntity;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Repository
public class BoardImageQueryDsl {

    @PersistenceContext
    EntityManager entityManager;

    @Transactional
    public String selectPath(Integer boardId) {

        JPAQueryFactory jpaQueryFactory = new JPAQueryFactory(entityManager);
        QBoardImageEntity qBoardImageEntity = QBoardImageEntity.boardImageEntity;

        try {
            return jpaQueryFactory.selectFrom(qBoardImageEntity)
                            .where(qBoardImageEntity.boardId.id.eq(boardId)).fetchOne().getPath();

        } catch (Exception e) {
                return null;
        }
    }
}
