package org.danpoong.zipcock_44.global.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        // HTTP 헤더에서 Authorization 값 추출
        String authorizationHeader = request.getHeaders().getFirst("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            log.warn("Authorization header is missing or invalid");
            return false; // 핸드셰이크 거부
        }

        // JWT 토큰에서 사용자 정보를 추출하여 attributes에 저장
        String token = authorizationHeader.substring(7); // "Bearer " 제거
        log.info("Extracted token: {}", token);

        // 필요 시 JWT를 파싱하여 사용자 정보를 attributes에 추가
        // attributes.put("userId", parsedUserId);

        return true; // 핸드셰이크 허용
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 핸드셰이크 이후에 추가 로직이 필요한 경우 처리
        log.info("Handshake completed successfully");
    }
}
