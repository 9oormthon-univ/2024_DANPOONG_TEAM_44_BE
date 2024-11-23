package org.danpoong.zipcock_44.domain.user.controller;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.danpoong.zipcock_44.domain.user.dto.request.ChangeLocationRequest;
import org.danpoong.zipcock_44.domain.user.dto.request.SignUpRequest;
import org.danpoong.zipcock_44.domain.user.dto.response.SignUpResponse;
import org.danpoong.zipcock_44.domain.user.service.UserService;
import org.danpoong.zipcock_44.global.common.response.ApiResponse;
import org.danpoong.zipcock_44.global.jwt.filter.JWTUtil;
import org.danpoong.zipcock_44.global.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@RestController
public class UserController {

    @Autowired
    private final UserService userService;

    @Autowired
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    private final JWTUtil jwtUtil;


    // 회원 가입 메소드
    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) throws Exception{
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpResponse);
    }

    @PostMapping("/check-email")
    public ApiResponse<Boolean> checkIfEmailDuplicated(@RequestBody Map<String, String> req) {
        return ApiResponse.ok(userService.checkIfEmailDuplicated(req));
    }

    @PostMapping("/check-username")
    public ApiResponse<Boolean> checkIfUsernameDuplicated(@RequestBody Map<String, String> req) {
        return ApiResponse.ok(userService.checkIfUsernameDuplicated(req));
    }

    // 회원 탈퇴 메소드
    @DeleteMapping("/signout")
    public ResponseEntity signOut(@RequestHeader("access") String token){
        try {
            String loginId = jwtUtil.getLoginId(token);
            if (loginId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 ID를 찾을 수 없습니다.");
            }
            log.info(loginId);

            // 서비스 호출
            return userDetailsServiceImpl.signOut(loginId);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러가 발생했습니다.");
        }
    }

    @PatchMapping("/changeLocation")
    public ResponseEntity<?> changeLocation(@RequestHeader("access") String token, @RequestBody ChangeLocationRequest request){
        try {
            String loginId = jwtUtil.getLoginId(token);
            if (loginId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 ID를 찾을 수 없습니다.");
            }
            log.info(loginId);

            // 서비스 호출 -> 바꾸기
            return userDetailsServiceImpl.changeLocation(loginId,request);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러가 발생했습니다.");
        }
    }

    // 마이페이지에서 필요한 사용자 정보 불러오는 메소드 : 2024.11.23 22:43 수정
    @GetMapping("/userInfo")
    public ResponseEntity<?> mypageInfo(@RequestHeader("access") String token){
        try{
            String loginId = jwtUtil.getLoginId(token);
            if (loginId == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("로그인 ID를 찾을 수 없습니다.");
            }
            log.info(loginId);

            return userDetailsServiceImpl.showInfo(loginId);
        } catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 내부 에러가 발생하였습니다.");
        }
    }
}