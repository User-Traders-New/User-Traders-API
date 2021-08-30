package com.company.usertradersback.controller;


import com.company.usertradersback.dto.UserDto;
import com.company.usertradersback.entity.UserEntity;
import com.company.usertradersback.env.Url;
import com.company.usertradersback.payload.PostPayload;
import com.company.usertradersback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = Url.url)
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // 회원 토큰 값 유효성 검사
    @GetMapping(value = "/valid")
    public ResponseEntity validToken(@RequestParam("token") String token) {
        try {
            boolean valid = userService.validToken(token);
            PostPayload payload = PostPayload.builder()
                    .valid(valid)
                    .message("토큰 유효성 검사에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            PostPayload payload = PostPayload.builder()
                    .message("토큰 유효성 검사에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> user) {

        try {
            String token = userService.login(user);
            PostPayload payload = PostPayload.builder()
                    .message("로그인에 성공하였습니다.")
                    .isSuccess(true)
                    .token(token)
                    .httpStatus(HttpStatus.OK)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            PostPayload payload = PostPayload.builder()
                    .message("로그인에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    // 로그 아웃은 서버에서 jwt 토큰을 삭제하지 않고 프론트 로컬 storage에
    // 들어있는 jwt를 지운다. 그리고 UserIsLogined 날짜 및 상태값 수정하여 DB 저장
    @PostMapping(value = "/logout")
    public ResponseEntity logout(@RequestHeader("token") String token,
                                 @AuthenticationPrincipal UserEntity userEntity){
        try {
            userService.logout(token,userEntity);
            PostPayload payload = PostPayload.builder()
                    .message("로그아웃에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            PostPayload payload = PostPayload.builder()
                    .message("로그아웃에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //회원 가입
    @PostMapping(value = "/register")
    public ResponseEntity register(@RequestBody UserDto userDto) {
        try {
            userService.register(userDto);
            PostPayload payload = PostPayload.builder()
                    .message("회원가입에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            PostPayload payload = PostPayload.builder()
                    .message("회원가입에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //이메일 중복검사 API

    //닉네임 중복검사 API

    // 회원 한명의 프로필 조회
    @GetMapping(value = "/profile") // 한 유저 상세 정보 단, 토큰 값이 있어야 가능
    public ResponseEntity profile(@AuthenticationPrincipal UserEntity userEntity) {
        return ResponseEntity.ok(userService.profile(userEntity));
    }

    // 회원 한명의 프로필 수정.
    @PatchMapping(value = "profile/update/{id}")
    public ResponseEntity profile(@RequestBody @Validated UserDto userDto,
                                  @PathVariable("id") Integer id) {
        userDto.setId(id);
        userService.updateById(userDto, id);
        return ResponseEntity.ok("내 정보를 수정 하였습니다.");
    }

    //회원 탈퇴 (예정)
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        userService.deleteById(id);
        return ResponseEntity.ok("회원이 탈퇴 되었습니다.");
    }
}