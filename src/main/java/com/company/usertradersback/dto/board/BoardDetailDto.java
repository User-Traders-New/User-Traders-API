package com.company.usertradersback.dto.board;

import com.company.usertradersback.payload.Payload;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class BoardDetailDto {

    private Payload payload;

    private BoardResponseDto boardResponseDto;

    private List<BoardImageDto> boardImageDto;

    @Builder
    public BoardDetailDto(Payload payload , BoardResponseDto boardResponseDto
    ,List<BoardImageDto> boardImageDto){
        this.payload = payload;
        this.boardResponseDto = boardResponseDto;
        this.boardImageDto = boardImageDto;
    }
}
