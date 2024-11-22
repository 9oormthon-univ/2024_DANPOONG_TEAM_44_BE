package org.danpoong.zipcock_44.domain.user.controller;

import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.danpoong.zipcock_44.domain.user.dto.response.KakaoUserInfoResponseDTO;
import org.danpoong.zipcock_44.domain.user.service.KakaoService;
import org.danpoong.zipcock_44.global.jwt.filter.LogoutFilterImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.io.IOException;

@RestController
@Slf4j
@Data
public class KakaoLoginController {

    private final KakaoService kakaoService;
    private final LogoutFilterImpl logoutFilter;

    // accessToken을 받아온 code 값 삽입
    @GetMapping("/getInfo") // Redirect URL 재설정 필요함
    public ResponseEntity<?> login(@RequestParam("code") String code ,HttpSession session) throws IOException {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDTO userInfo = kakaoService.getUserInfo(accessToken);
        log.info(code);
        session.setAttribute("access_token", accessToken);
        kakaoService.saveOrUpdateKakaoUser(userInfo); // 로그인 정보 업데이트
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/kakaoLogout")
    public ResponseEntity<String> kakaoLogout(@RequestHeader(value = "Authorization", required = false) String authorizationHeader) {

        // 요청 헤더에서 access_token 추출
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body("Access token is missing or invalid.");
        }
        String accessToken = authorizationHeader.replace("Bearer ", "");
        // 서비스 호출
        return kakaoService.logout(accessToken);
    }

}
