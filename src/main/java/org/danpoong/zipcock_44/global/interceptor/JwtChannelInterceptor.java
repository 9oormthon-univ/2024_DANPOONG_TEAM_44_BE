package org.danpoong.zipcock_44.global.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.danpoong.zipcock_44.global.jwt.filter.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtChannelInterceptor implements ChannelInterceptor {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtChannelInterceptor(JWTUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        // STOMP 헤더를 가져옴
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // CONNECT 명령어일 때만 인증 시도
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {

            String authToken = accessor.getFirstNativeHeader("Authorization"); // 'access' 대신 'Authorization' 사용

            if (authToken != null && authToken.startsWith("Bearer ")) {
                String token = authToken.substring(7); // "Bearer " 제거

                if (jwtUtil.validateToken(token)) {
                    String userId = jwtUtil.getLoginId(token);

                    UserDetails userDetails = userDetailsService.loadUserByUsername(userId);

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                    accessor.setUser(authentication);

                    // 세션 저장
                    String sessionId = accessor.getSessionId();

                    log.info("WebSocket authentication successful for user: {}, sessionId: {}", userId, sessionId);
                } else {
                    log.warn("Invalid JWT token");
                }
            } else {
                log.warn("Authorization header missing or does not start with Bearer");
            }
        }

        // DISCONNECT 명령어일 때 세션 제거
        if (StompCommand.DISCONNECT.equals(accessor.getCommand())) {
            UsernamePasswordAuthenticationToken authentication = (UsernamePasswordAuthenticationToken) accessor.getUser();
            if (authentication != null) {
                String userId = authentication.getName();
                String sessionId = accessor.getSessionId();
                log.info("WebSocket disconnected for user: {}, sessionId: {}", userId, sessionId);
            }
        }

        return message;
    }
}
