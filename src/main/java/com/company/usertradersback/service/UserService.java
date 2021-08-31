package com.company.usertradersback.service;

import com.company.usertradersback.config.jwt.JwtTokenProvider;
import com.company.usertradersback.dto.UserDto;
import com.company.usertradersback.entity.UserEntity;
import com.company.usertradersback.entity.UserIsLoginedEntity;
import com.company.usertradersback.exception.user.ApiIllegalArgumentException;
import com.company.usertradersback.repository.UserIsLoginedRepository;
import com.company.usertradersback.repository.UserRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {
    // 여기서 @AutoWired 를쓰면 파라미터 값이 1개가 아니라서 안되고,
    // @RequiredArgsConstructor 써서 final 객체 부르면 스프링 빈 종속성 문제 에서 순환 참조 문제가 발생 된다.
    // 따라서 생성자 주입 해줄 때 @LAZY로 지연 로딩 시켜준다. 허나 이건 임시 방편이다.

    private final UserRepository userRepository;
    private final UserIsLoginedRepository userIsLoginedRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    public UserService(@Lazy UserRepository userRepository,
                       @Lazy UserIsLoginedRepository userIsLoginedRepository,
                       @Lazy JwtTokenProvider jwtTokenProvider, @Lazy PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userIsLoginedRepository = userIsLoginedRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

//     Spring Security 필수 메소드 구현
//     @param userid 이메일 아이디
//     @return UserDetails
//     @throws UsernameNotFoundException 유저가 없을 때 예외 발생

    // Spring security 필수 메소드 구현
    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    // 회원 로그인 , 한 회원 이메일, 비밀번호 조회
    @Transactional
    public String login(Map<String, String> user) {
        UserEntity userEntity = userRepository.findByEmail(user.get("email"))
                .orElseThrow(() -> new ApiIllegalArgumentException("가입되지 않은 E-MAIL 입니다."));
        if (!passwordEncoder.matches(user.get("password"), userEntity.getPassword())) {
            throw new ApiIllegalArgumentException("잘못된 비밀번호 입니다.");
        }
        int a = userIsLoginedRepository.checkId(userEntity.getId());

        if(a>=1){
            LocalDateTime logoutAt = userIsLoginedRepository.findByLogoutAt(userEntity.getId());
            userIsLoginedRepository.updateLoginAt(logoutAt,LocalDateTime.now(),userEntity.getId());
        }else{
            userIsLoginedRepository.save(
                    UserIsLoginedEntity.builder()
                            .id(userEntity.getId())
                            .status(1)
                            .loginAt(LocalDateTime.now())
                            .build()
            );
        }
        return jwtTokenProvider.createToken(userEntity.getUsername(), userEntity.getRoles());
    }

    //회원 토큰 값 유효성 검사
    @Transactional
    public boolean validToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }
    //토큰값을 받아서 로그아웃
    @Transactional
    public String logout(String token) {
        String email = jwtTokenProvider.getUserPk(token);
        Integer id = this.selectId(email);
        LocalDateTime loginAt = userIsLoginedRepository.findByLoginAt(id);
        userIsLoginedRepository.updateLogoutAt(LocalDateTime.now(),loginAt,id);
        return "로그 아웃을 완료하였습니다.";
    }
    // 회원 가입, 회원 정보 저장
    @Transactional
    public Integer register(UserDto userDto) {
        return userRepository.save(UserEntity.builder()
                .email(userDto.getEmail())
                .password(passwordEncoder.encode(userDto.getPassword()))
                .userName(userDto.getUserName())
                .nickname(userDto.getNickname())
                .departmentId(userDto.getDepartmentId())
                .studentId(userDto.getStudentId())
                .gender(userDto.getGender())
                .loginType(userDto.getLoginType())
                .imagePath("https://usertradersbucket.s3.ap-northeast-2.amazonaws.com/basic/profile_img.gif")
                .createAt(LocalDateTime.now())
                .modifiedAt(LocalDateTime.now())
                .roles(Collections.singletonList("일반 회원"))
                .build()).getId();
    }


    @Transactional
    public String emailCheck(String email) {
        if (userRepository.selectEmailCount(email)>=1)
                  return "중복된 이메일 입니다.";
        else return "사용 가능한 이메일 입니다.";
    }
    @Transactional
    public String nickNameCheck(String nickname) {
        if (userRepository.selectNicknameCount(nickname)>=1)
            return "중복된 닉네임 입니다.";
        else return "사용 가능한 닉네임 입니다.";
    }

    //unique한 email로 해당 회원 한명 프로필 정보 조회
    @Transactional
    public UserDto profile(String token) {
        String email = jwtTokenProvider.getUserPk(token);
        Optional<UserEntity> userEntityWrapper = userRepository.findByEmail(email);
        UserEntity userEntity = userEntityWrapper.get();
        return UserDto.builder().build().UserEntityToDto(userEntity);
    }

    // pk인 id로 해당 회원 한명 프로필 정보 조회
    @Transactional
    public UserDto findUserById(Integer id) {
        Optional<UserEntity> userEntityWrapper = userRepository.findById(id);
        UserEntity userEntity = userEntityWrapper.get();
        return UserDto.builder().build().UserEntityToDto(userEntity);
    }

    //token 값으로 구한 email로 pk,id 조회
    @Transactional
    public Integer selectId(String email){
        return userRepository.findIdByEmail(email);
    }
    //token 값으로 id를 구하여 회원 한명 프로필 정보 조회 (복잡한 코드 사용x)
    @Transactional
    public UserDto findUserByToken(String token) {
        String email = jwtTokenProvider.getUserPk(token);
        Integer id = this.selectId(email);
        System.out.println(userRepository.findById(id));
        Optional<UserEntity> userEntityWrapper = userRepository.findById(id);
        UserEntity userEntity = userEntityWrapper.get();
        return UserDto.builder().build().UserEntityToDto(userEntity);
    }

    // user 객체로 회원 한명 프로필 정보 조회 (객체를 받는건 비효율,사용 x)
    @Transactional
    public UserDto findUserByUser(UserEntity user) {

        Optional<UserEntity> userEntityWrapper = userRepository.findById(user.getId());
        UserEntity userEntity = userEntityWrapper.get();
        return UserDto.builder().build().UserEntityToDto(userEntity);
    }

    // 변경할 회원 정보와 , 해당 회원의 pk인 id로 ,회원 한명 프로필 정보 수정
    @Transactional
    public Integer profileUpdate(UserDto userDto,
                                 String token,
                                 List<MultipartFile> files) {
        Integer id = this.findUserByToken(token).getId();

        System.out.println(files.size());
        System.out.println(files);
//
//        AwsS3 awsS3 = new AwsS3();
//        awsS3.upload();
//
//

        Optional<UserEntity> userEntityWrapper = userRepository.findById(id);
        userEntityWrapper.ifPresent(userEntity -> {
            userEntity = UserEntity.builder()
                    .id(userDto.getId())
                    .email(userDto.getEmail())
                    .password(passwordEncoder.encode(userDto.getPassword()))
                    .userName(userDto.getUserName())
                    .nickname(userDto.getNickname())
                    .departmentId(userDto.getDepartmentId())
                    .studentId(userDto.getStudentId())
                    .gender(userDto.getGender())
                    .loginType(userDto.getLoginType())
                    .imagePath(userDto.getImagePath())
                    .build();
            userRepository.save(userEntity);
        });
        return userEntityWrapper.get().getId();
    }

    //회원 한명 정보 삭제
    @Transactional
    public void deleteById(Integer id) {
        userRepository.deleteById(id);
    }



}
