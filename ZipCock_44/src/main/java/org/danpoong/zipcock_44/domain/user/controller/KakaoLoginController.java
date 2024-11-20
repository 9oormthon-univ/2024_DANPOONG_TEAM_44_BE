package org.danpoong.zipcock_44.domain.user.controller;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.danpoong.zipcock_44.domain.user.dto.response.KakaoUserInfoResponseDTO;
import org.danpoong.zipcock_44.domain.user.service.KakaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@Slf4j
@Data
public class KakaoLoginController {

    private final KakaoService kakaoService;


    /*
    @GetMapping("/test")
    public String forTest(){
        return " test ok ";
    }

     */

    // accessToken을 받아온
    @GetMapping("/getInfo") // Redirect URL 재설정 필요함
    public ResponseEntity<?> login(@RequestParam("code") String code) throws IOException {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDTO userInfo = kakaoService.getUserInfo(accessToken);
        kakaoService.saveOrUpdateKakaoUser(userInfo); // 로그인
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
