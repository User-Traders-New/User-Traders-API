package com.company.usertradersback.dto.board;

import com.company.usertradersback.dto.user.UserConciseDto;
import com.company.usertradersback.entity.BoardCategoryEntity;
import com.company.usertradersback.entity.BoardEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
//필요한 게시물 정보만 조회를 위한 ResponseDto
public class BoardResponseDto {

    // 게시물 고유번호
    private Integer id;

    // 회원 고유번호
    private UserConciseDto userId;

    // 게시물 제목
    private String title;

    // 게시물 내용
    private String content;

    // 게시물 가격
    private String price;

    // 카테고리 고유번호
    private BoardCategoryEntity categoryId;

    // 게시물 조회수
    private Integer views;

    // 게시물 등급 0:새상품,1:S급,2:A급,3:B급
    private Integer grade;

    // 게시물 판매상태 0:판매중,1:예약중,2:판매완료
    private Integer status;

    // 게시물 등록날짜
    private LocalDateTime createAt;

    // 게시물 수정날짜
    private LocalDateTime modifiedAt;

    //좋아요 총 수
    private Integer likeCount;

    //채팅 수
    private Integer chatCount;

    //댓글 수
    private Integer commentCount;

    //썸네일
    private String thumbnail;



    @Builder
    public BoardResponseDto(Integer id, UserConciseDto userId, String title,
                            String content, String price, BoardCategoryEntity categoryId, Integer views, Integer grade,
                            Integer status, LocalDateTime createAt, LocalDateTime modifiedAt,
                            Integer likeCount, Integer chatCount, Integer commentCount
            , String thumbnail) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.content = content;
        this.price = price;
        this.categoryId = categoryId;
        this.views = views;
        this.grade = grade;
        this.status = status;
        this.createAt = createAt;
        this.modifiedAt = modifiedAt;
        this.likeCount = likeCount;
        this.chatCount = chatCount;
        this.commentCount = commentCount;
        this.thumbnail = thumbnail;

    }

    //게시물 전체 조회를 위한, BoardEntity 를 BoardResponseDto 로 변환
    public BoardResponseDto convertEntityToDto(BoardEntity boardEntity
            , Integer likeCount, Integer commentCount,String thumbnail,String userGrade

    ) {

        return BoardResponseDto.builder()
                .id(boardEntity.getId())
                .userId(new UserConciseDto().convertEntityToDto(boardEntity.getUserId(),userGrade))
                .title(boardEntity.getTitle())
                .content(boardEntity.getContent())
                .price(boardEntity.getPrice())
                .categoryId(boardEntity.getCategoryId())
                .views(boardEntity.getViews())
                .grade(boardEntity.getGrade())
                .status(boardEntity.getStatus())
                .createAt(boardEntity.getCreateAt())
                .modifiedAt(boardEntity.getModifiedAt())
                .likeCount(likeCount)
                .commentCount(commentCount)
                .thumbnail(thumbnail)
                .build();
    }
}
