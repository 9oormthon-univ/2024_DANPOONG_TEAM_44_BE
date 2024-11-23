package org.danpoong.zipcock_44.global.jwt.filter;

import io.jsonwebtoken.*;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
@Component
@Data
public class JWTUtil {


    // JWT 비밀키 담는 필드
    private final SecretKey secretKey;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret){

        this.secretKey = new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());

        // secretKey를 받아와서 SecreteSpec 객체를 통해 secret값읋 바이트 배열로 변환 -> 'HS256'알고리즘을 통해 비밀 키 객체 생성
    }

    // JWT 토큰을 파싱하고 유효성 검증
    public Claims parseClaims(String token) {
        try {
            // JWT 토큰을 파싱하여 Claims 객체를 반환
            return Jwts.parser()
                    .setSigningKey(secretKey) // 비밀키로 서명 검증
                    .build()
                    .parseClaimsJws(token) // 토큰을 파싱하여 Claims 객체 반환
                    .getBody(); // Claims 반환
        } catch (MalformedJwtException | ExpiredJwtException | UnsupportedJwtException e) {
            // 유효하지 않은 토큰에 대해 예외 처리
            return null;
        }
    }

    // 토큰에서 Username 추출
    public String getLoginId(String token){
        System.out.println("Received Token: " + token); // 토큰 로그
        return Jwts.parser()
                .setSigningKey(secretKey) // 비밀키 설정
                .build() // 파서 빌드
                .parseClaimsJws(token) // JWT 토큰 파싱
                .getBody() // 페이로드 추출
                .get("loginId", String.class); // loginId 추출
    }

    // 토큰에서 Role 추출
    public String getRole(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("role", String.class);
    }

    // JWT 토큰 만료여부 확인
    public Boolean isExpired(String token) {

        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().getExpiration().before(new Date());
    }

    public String getCategory(String token){
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload().get("category",String.class);
    }

    //새로운 JWT 토큰 생성하는 메소드
    public String createJwt(String category,String loginId, String role, Long expiredMs) {

        return Jwts.builder()
                .claim("category",category)
                .claim("loginId", loginId)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis())) // 토큰 발급 시간을 현재 시간으로 설정
                .expiration(new Date(System.currentTimeMillis() + expiredMs)) // 만료시간 설정 -> expiredMS + 현재 시간을 통해서 결정
                .signWith(secretKey) // secretKey를 통해 signature 부분을 채움
                .compact(); // JWT를 문자열 형식으로 변환하여 반환
    }

    public boolean validateToken(String token) {
        try {
            Jwts.parser()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token);
            return true; // Token is valid
        } catch (JwtException e) {
            return false;
        }
    }

}
