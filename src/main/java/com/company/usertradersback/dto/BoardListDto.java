package com.company.usertradersback.dto;

import com.company.usertradersback.payload.Payload;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
// 모든 게시물 List 반환을 위한 BoardListDto
public class BoardListDto {

    private Payload payload;

    private List<BoardDto> boardDtoList;

    @Builder
    public BoardListDto(Payload payload,List<BoardDto> boardDtoList){
        this.payload = payload;
        this.boardDtoList = boardDtoList;
    }

}
