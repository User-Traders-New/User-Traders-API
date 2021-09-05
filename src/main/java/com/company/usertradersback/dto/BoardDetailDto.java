package com.company.usertradersback.dto;

import com.company.usertradersback.payload.Payload;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class BoardDetailDto {

    private Payload payload;

    private BoardDto boardDto;

    @Builder
    public BoardDetailDto(Payload payload ,BoardDto boardDto ){
        this.payload = payload;
        this.boardDto = boardDto;
    }
}
