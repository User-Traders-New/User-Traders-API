package com.company.usertradersback.controller;


import com.company.usertradersback.dto.*;
import com.company.usertradersback.env.Url;
import com.company.usertradersback.payload.Payload;
import com.company.usertradersback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
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
            Payload payload = Payload.builder()
                    .message("토큰 유효성 검사에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
            UserValidDto userValidDto = UserValidDto.builder()
                    .payload(payload)
                    .valid(valid)
                    .build();
            return new ResponseEntity<>(userValidDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
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
            //로그인, 반환값 token
            String token = userService.login(user);

            // 고정 응답값
            Payload payload = Payload.builder()
                    .message("로그인에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();

            //응답값 + 로그인 반환값 token
            UserTokenDto userTokenDto = UserTokenDto.builder()
                    .payload(payload)
                    .token(token)
                    .build();

            return new ResponseEntity<>(userTokenDto, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
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
    public ResponseEntity logout(@RequestHeader("token") String token
                                 ) {
        try {
            userService.logout(token);
            Payload payload = Payload.builder()
                    .message("로그아웃에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
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
            Payload payload = Payload.builder()
                    .message("회원가입에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("회원가입에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    //이메일 중복검사 API
    @GetMapping(value = "/email-check")
    public ResponseEntity emailCheck(
            @RequestParam("email")String email){
        try {
                   UserEmailCheckDto userEmailCheckDto
                           = UserEmailCheckDto.builder()
                           .check(userService.emailCheck(email))
                           .email(email)
                           .build();
            Payload payload = Payload.builder()
                    .message("이메일 검사에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
            userEmailCheckDto.setPayload(payload);

           return new ResponseEntity<>(userEmailCheckDto,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("이메일 검사에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //닉네임 중복검사 API
    @GetMapping(value = "/nickname-check")
    public ResponseEntity nicknameCheck(
            @RequestParam("nickname")String nickname){
        try {
            UserNicknameCheckDto userNicknameCheckDto
                    = UserNicknameCheckDto.builder()
                    .check(userService.nickNameCheck(nickname))
                    .nickname(nickname)
                    .build();
            Payload payload = Payload.builder()
                    .message("닉네임 검사에 성공하였습니다.")
                    .isSuccess(true)
                    .httpStatus(HttpStatus.OK)
                    .build();
            userNicknameCheckDto.setPayload(payload);

            return new ResponseEntity<>(userNicknameCheckDto,HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("닉네임 검사에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // 회원 한명의 프로필 조회
    @GetMapping(value = "/profile") // 한 유저 상세 정보 단, 토큰 값이 있어야 가능
    public ResponseEntity profile(
            @RequestHeader("token") String token
            ) {
        try {
            if (userService.validToken(token)){
                UserDto userDto = userService.profile(token);
                Payload payload = Payload.builder()
                        .message("프로필 조회에 성공하였습니다.")
                        .isSuccess(true)
                        .httpStatus(HttpStatus.OK)
                        .build();
                userDto.setPayload(payload);
                return new ResponseEntity<>(userDto, HttpStatus.OK);
            }else {
                return new ResponseEntity<>("토큰이 만료 되었습니다.",HttpStatus.EXPECTATION_FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("프로필 조회에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 회원 한명의 프로필 수정.
    @PatchMapping(value = "profile/update/{id}")
    public ResponseEntity profile(
            @RequestHeader("token") String token,
            @RequestBody @Validated UserDto userDto,
            List<MultipartFile> files
                                  ) {
        try {

            if (userService.validToken(token)){

                userService.profileUpdate(userDto, token,files);

                Payload payload = Payload.builder()
                        .message("프로필 수정에 성공하였습니다.")
                        .isSuccess(true)
                        .httpStatus(HttpStatus.OK)
                        .build();
                return new ResponseEntity<>(payload, HttpStatus.OK);
            }else {
                return new ResponseEntity<>("토큰이 만료 되었습니다.",HttpStatus.EXPECTATION_FAILED);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Payload payload = Payload.builder()
                    .message("프로필 수정에 실패하였습니다.")
                    .isSuccess(false)
                    .httpStatus(HttpStatus.INTERNAL_SERVER_ERROR)
                    .build();
            return new ResponseEntity<>(payload, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //회원 탈퇴 (예정)
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        userService.deleteById(id);
        return ResponseEntity.ok("회원이 탈퇴 되었습니다.");
    }
}