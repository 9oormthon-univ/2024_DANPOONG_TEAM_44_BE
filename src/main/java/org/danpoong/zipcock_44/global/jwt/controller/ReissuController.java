package org.danpoong.zipcock_44.global.jwt.controller;


import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.danpoong.zipcock_44.global.jwt.filter.JWTUtil;
import org.danpoong.zipcock_44.global.jwt.repository.RefreshRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Data
@RestController
public class ReissuController {


    private final JWTUtil jwtUtil;
    // 나중에 Service와 Controller 부분으로 나눌필요가 있음
    private final RefreshRepository refreshRepository;

    // Refresh 토큰을 재발급 하는 메소드
    @PostMapping("/reissue")
    public ResponseEntity<?> reissue(HttpServletRequest request, HttpServletResponse response){

        String refresh = null;
        Cookie[] cookies = request.getCookies();
        for(Cookie cookie: cookies){
            if(cookie.getName().equals("refresh")){
                // Refresh 변수에 쿠키의 Refresh Value 넣기
                refresh = cookie.getValue();
            }
        }

        // Refresh Token 비엇는지 확인
        if(refresh==null){

            return new ResponseEntity<>("Refresh Token NULL", HttpStatus.BAD_REQUEST);
        }

        // 토큰 만료 여부 확인
        try{
            jwtUtil.isExpired(refresh);
        } catch (ExpiredJwtException e){
            return new ResponseEntity<>("Refresh Token Expired",HttpStatus.BAD_REQUEST);
        }

        // 토큰이 refresh인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(refresh);

        if (!category.equals("refresh")) {

            //response status code
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        //DB에 저장되어 있는지 확인
        Boolean isExist = refreshRepository.existsByRefresh(refresh);
        if (!isExist) {

            //response body
            return new ResponseEntity<>("invalid refresh token", HttpStatus.BAD_REQUEST);
        }

        String username = jwtUtil.getLoginId(refresh);
        String role = jwtUtil.getRole(refresh);

        // 새로운 Access 토큰 생성
        String newAccess = jwtUtil.createJwt("access", username, role, 600000L);
        // 리프래시 토큰 갱신
        String newRefresh = jwtUtil.createJwt("refresh", username, role, 86400000L);

        // 헤더 설정
        response.setHeader("access", newAccess);

        // 리프래시 토큰 갱신
        response.addCookie(createCookie("refresh", newRefresh));

        return new ResponseEntity<>(HttpStatus.OK);
    }

    private Cookie createCookie(String key, String value) {

        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60);
        //cookie.setSecure(true);
        //cookie.setPath("/");
        cookie.setHttpOnly(true);

        return cookie;
    }
}
