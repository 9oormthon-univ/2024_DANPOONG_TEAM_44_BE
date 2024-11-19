package org.danpoong.zipcock_44.domain.user.controller;

import io.jsonwebtoken.Claims;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.apache.tomcat.util.http.parser.Authorization;
import org.danpoong.zipcock_44.domain.user.dto.request.SignUpRequest;
import org.danpoong.zipcock_44.domain.user.dto.response.SignUpResponse;
import org.danpoong.zipcock_44.domain.user.service.UserService;
import org.danpoong.zipcock_44.global.jwt.filter.JWTUtil;
import org.danpoong.zipcock_44.global.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/signup")
    public ResponseEntity<SignUpResponse> signUp(@RequestBody SignUpRequest signUpRequest) throws Exception{
        SignUpResponse signUpResponse = userService.signUp(signUpRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(signUpResponse);
    }

    @DeleteMapping("/signout")
    public ResponseEntity signOut(@RequestHeader("Authorization") String token){
        try {

            String jwtToken = token.substring(7).trim(); // "Bearer " 제거 후 공백 제거
            String loginId = jwtUtil.getLoginId(jwtToken);
            if (loginId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 ID를 찾을 수 없습니다.");
            }
            log.info(loginId);

            // 로그인 ID가 없으면 실패 처리
            if (loginId == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("로그인 ID를 찾을 수 없습니다.");
            }

            // 서비스 호출
            return userDetailsServiceImpl.signOut(loginId);
        } catch (RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 에러가 발생했습니다.");
        }
    }
}
