package com.company.usertradersback.controller;


import com.company.usertradersback.config.jwt.JwtTokenProvider;
import com.company.usertradersback.dto.UserDto;
import com.company.usertradersback.entity.UserEntity;
import com.company.usertradersback.env.Url;
import com.company.usertradersback.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@CrossOrigin(origins = Url.url)
@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    // 회원 토큰 값 유효성 검사
    @GetMapping(value = "/valid")
    public boolean valid(@RequestParam("token") String token) {
        return jwtTokenProvider.validateToken(token);
    }

    // 로그인
    @PostMapping("/login")
    public ResponseEntity login(@RequestBody Map<String, String> user) {
        return ResponseEntity.ok(userService.login(user));
    }
    // 로그 아웃
    @GetMapping(value = "/logout")
    public ResponseEntity logoutPage(HttpServletRequest request, HttpServletResponse response) {
        new SecurityContextLogoutHandler().logout(request, response, SecurityContextHolder.getContext().getAuthentication());
        return ResponseEntity.ok("로그아웃 되었습니다.");
    }

    //회원 가입
    @PostMapping(value = "/register")
    public ResponseEntity register(@RequestBody UserDto userDto) {
        userService.register(userDto);
        return ResponseEntity.ok("회원가입이 완료되었습니다.");
    }

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
    //회원 탈퇴
    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity delete(@PathVariable("id") Integer id) {
        userService.deleteById(id);
        return ResponseEntity.ok("회원이 탈퇴 되었습니다.");
    }
}