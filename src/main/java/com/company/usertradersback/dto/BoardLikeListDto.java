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
public class BoardLikeListDto {
    private Payload payload;

    private List<BoardLikeUserDto> boardLikeUserDtoList;

    @Builder
    public BoardLikeListDto(Payload payload,List<BoardLikeUserDto> boardLikeUserDtoList){
        this.payload = payload;
        this.boardLikeUserDtoList = boardLikeUserDtoList;
    }

}
