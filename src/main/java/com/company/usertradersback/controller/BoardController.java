package com.company.usertradersback.controller;


import com.company.usertradersback.dto.*;
import com.company.usertradersback.entity.UserEntity;
import com.company.usertradersback.env.Url;
import com.company.usertradersback.payload.Payload;
import com.company.usertradersback.service.BoardService;
import com.company.usertradersback.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@CrossOrigin(origins = Url.url)
@RequestMapping(value = "/boards")
public class BoardController {

    @Autowired
    private BoardService boardService;

    @Autowired
    private UserService userService;

    // 모든 게시물 리스트 조회
    @GetMapping(value = "/list/all")
    public ResponseEntity listAll() {

        try {
            List<BoardDto> List = boardService.listAll();

            Payload payload = Payload.builder()
                    .message("전체 게시물 리스트 조회에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();

            BoardListDto boardListDto = BoardListDto.builder()
                    .payload(payload)
                    .boardDtoList(List)
                    .build();

            return new ResponseEntity<>(boardListDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("전체 게시물 리스트 조회에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping(value = "/list/page") // 모든 게시물을 페이지 네이션 한 리스트 조회
    public ResponseEntity listAllInfinite(@RequestParam(value = "limit", defaultValue = "1") Integer limit) {
        // 여기서 요청 인자(RequestParam) limit는 page를 말한다.
        try {
            List<BoardDto> List = boardService.listAllInfinite(limit);

            Payload payload = Payload.builder()
                    .message("해당 페이지 게시물 조회에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();

            BoardListDto boardListDto = BoardListDto.builder()
                    .payload(payload)
                    .boardDtoList(List)
                    .build();

            return new ResponseEntity<>(boardListDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("해당 페이지 게시물 조회에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 한 게시물 의 id 안에 들어 있는 정보를 조회
    //@PathVariable :url 파라 미터 값 id를 인자로 받음.
    @GetMapping(value = "/list/detail/{id}")
    public ResponseEntity listDetail(@PathVariable("id") Integer id) {
        try {
            Payload payload = Payload.builder()
                    .message("해당 페이지 게시물 조회에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
            BoardDetailDto boardDetailDto = BoardDetailDto.builder()
                    .payload(payload)
                    .boardDto(boardService.listDetail(id))
                    .build();

            return new ResponseEntity<>(boardDetailDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("해당 페이지 게시물 조회에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //keword를 받아서 게시물 제목을 비교하여 게시물 조회(검색)
    @GetMapping(value = "/search/title")
    public ResponseEntity searchTitleOrContent(@RequestParam String keyword) {

        try {
            List<BoardDto> List = boardService.searchTitle(keyword);

            Payload payload = Payload.builder()
                    .message("제목으로 게시물 검색에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();

            BoardListDto boardListDto = BoardListDto.builder()
                    .payload(payload)
                    .boardDtoList(List)
                    .build();

            return new ResponseEntity<>(boardListDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("제목으로 게시물 검색에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //대분류 카테고리Id와 서브 카테고리 Id를 통한 게시물 조회(검색)
    @GetMapping(value = "/category/search")
    public ResponseEntity searchCategory(@RequestParam("categoryId") Integer categoryId
            , @RequestParam("subCategoryId") Integer subCategoryId
    ) {

        try {
            List<BoardDto> List =
                    boardService.searchCategory(categoryId, subCategoryId);

            Payload payload = Payload.builder()
                    .message("카테고리로 게시물 검색에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();

            BoardListDto boardListDto = BoardListDto.builder()
                    .payload(payload)
                    .boardDtoList(List)
                    .build();

            return new ResponseEntity<>(boardListDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("카테고리로 게시물 검색에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //나의 게시물 전체 조회
    @GetMapping(value = "/list/my")//
    public ResponseEntity listMyBoards(@RequestHeader("token") String token,
                                       @AuthenticationPrincipal UserEntity userEntity) {
        try {
            if (userService.validToken(token)) {
                List<BoardDto> List = boardService.listMyBoards(userEntity);

                Payload payload = Payload.builder()
                        .message("나의 게시물 조회에 성공하였습니다.")
                        .isSuccess(true)
                        .httpStatus(HttpStatus.OK)
                        .build();

                BoardListDto boardListDto = BoardListDto.builder()
                        .payload(payload)
                        .boardDtoList(List)
                        .build();

                return new ResponseEntity<>(boardListDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("토큰이 만료 되었습니다.", HttpStatus.EXPECTATION_FAILED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("나의 게시물 조회에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 해당 userId를 가지는 회원의 전체 게시물
    @GetMapping(value = "/list/userId")
    public ResponseEntity listUserId(
            @RequestHeader("token") String token,
            @RequestParam(value = "userId") Integer userId) {
        try {
            if (userService.validToken(token)) {
                List<BoardDto> List = boardService.findAllByUserId(userId);

                Payload payload = Payload.builder()
                        .message("해당 " + userId + "번 회원님의 게시물 조회에 성공하였습니다.")
                        .isSuccess(true)
                        .httpStatus(HttpStatus.OK)
                        .build();

                BoardListDto boardListDto = BoardListDto.builder()
                        .payload(payload)
                        .boardDtoList(List)
                        .build();

                return new ResponseEntity<>(boardListDto, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("토큰이 만료 되었습니다.", HttpStatus.EXPECTATION_FAILED);
            }

        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("해당 " + userId + "번 회원님의 게시물 조회에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
    //대분류 카테고리 조회
    @GetMapping(value = "/list/category")
    public ResponseEntity listSubCategoryId() {
        try {

            List<BoardSubCategoryDto> List = boardService.listSubCategoryId();

            Payload payload = Payload.builder()
                    .message("대분류 카테고리 조회에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();

            BoardSubCategoryListDto boardListDto = BoardSubCategoryListDto.builder()
                    .payload(payload)
                    .boardSubCategoryDtoList(List)
                    .build();

            return new ResponseEntity<>(boardListDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("대분류 카테고리 조회에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //대분류 카테 고리 Id를 통해 서브 카테고리 조회
    @GetMapping(value = "/list/category/sub")
    public ResponseEntity listCategoryId(
            @RequestParam("subCategoryId") Integer subCategoryId
    ) {
        try {

            List<BoardCategoryDto> List = boardService.listCategoryId(subCategoryId);

            Payload payload = Payload.builder()
                    .message("서브 카테고리 조회에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();

            BoardCategoryListDto boardListDto = BoardCategoryListDto.builder()
                    .payload(payload)
                    .boardCategoryDtoList(List)
                    .build();

            return new ResponseEntity<>(boardListDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("서브 카테고리 조회에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


//
//    @PostMapping(value = "/register") // 한 게시물 저장
//    public ResponseEntity register(BoardDto boardDto, List<MultipartFile> files, @AuthenticationPrincipal UserEntity user) {//@RequestBody :HTTP 요청 몸체를 자바 객체로 변환
//        boardService.save(boardDto, files, user);
//        return ResponseEntity.ok(boardDto.getId() + "번 게시물이 저장되었습니다.");
//    }
//
//    @PatchMapping(value = "/list/{id}") // 한 게시물의 id 를 받아서 그 안에 들어 있는 게시물 정보 수정.
//    public ResponseEntity update(@RequestBody @Validated BoardDto boardDto,
//                                 @PathVariable("id") Integer id) {
//        boardDto.setId(id);
//        boardService.updateById(boardDto, id);
//        return ResponseEntity.ok(id + "번 게시물이 수정되었습니다.");
//
//    }

    // 한 게시물 삭제(예정)
    @DeleteMapping(value = "/list/{id}")
    public ResponseEntity delete(
            @PathVariable("id") Integer id) {
        boardService.deleteById(id);
        return ResponseEntity.ok(id + "번 게시물이 삭제되었습니다.");
    }


}