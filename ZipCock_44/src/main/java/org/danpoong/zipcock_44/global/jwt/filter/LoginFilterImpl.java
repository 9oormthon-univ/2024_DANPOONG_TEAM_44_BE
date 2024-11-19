package org.danpoong.zipcock_44.global.jwt.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.danpoong.zipcock_44.global.jwt.entity.Refresh;
import org.danpoong.zipcock_44.global.jwt.repository.RefreshRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;



import java.util.Collection;
import java.util.Date;
import java.util.Iterator;


@Component
public class LoginFilterImpl extends UsernamePasswordAuthenticationFilter{


    private final AuthenticationManager authenticationManager; // final 필드
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;

    // 생성자 주입
    @Autowired
    public LoginFilterImpl(AuthenticationManager authenticationManager, JWTUtil jwtUtil, RefreshRepository refreshRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshRepository = refreshRepository;

        // AuthenticationManager를 필터에 설정
        setAuthenticationManager(authenticationManager);
    }

    // 쿠키 생성 메소드
    private Cookie createCookie(String key, String value) {
        Cookie cookie = new Cookie(key, value);
        cookie.setMaxAge(24*60*60); // 만료시간 1일 설정
        cookie.setHttpOnly(true); // JavaScript에서 쿠키 접근 불가
        return cookie;
    }

    // Refresh 엔티티에 add를 수행하는 메소드
    private void addRefreshEntity(String loginId, String refresh, Long expiredMs) {
        Date date = new Date(System.currentTimeMillis() + expiredMs);

        Refresh refreshEntity = new Refresh();
        refreshEntity.setLoginId(loginId);
        refreshEntity.setRefresh(refresh);
        refreshEntity.setExpiration(date.toString());

        refreshRepository.save(refreshEntity);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        // 로그인 시도
        String loginId = request.getParameter("loginId");
        String password = request.getParameter("password");
        System.out.println("LoginId: " + loginId + ", Password: " + password);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(loginId, password,null);
        return authenticationManager.authenticate(authToken);
    }

    // 로그인 성공 시 JWT 발급
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        String username = authentication.getName(); // 인증된 사용자 이름 (loginId)
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = authorities.iterator();
        GrantedAuthority auth = iterator.next();
        String role = auth.getAuthority();

        // Access 토큰과 Refresh 토큰 생성
        String access = jwtUtil.createJwt("access", username, role, 600000L); // Access 토큰
        String refresh = jwtUtil.createJwt("refresh", username, role, 86400000L); // Refresh 토큰

        // Refresh 토큰 저장
        addRefreshEntity(username, refresh, 86400000L);

        // 응답에 헤더와 쿠키 추가
        response.setHeader("access", access);
        response.addCookie(createCookie("refresh", refresh));
        response.setStatus(HttpStatus.OK.value());
    }

    // 로그인 실패 시 처리
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) {
        response.setStatus(401); // 인증 실패 시 401 응답 코드 반환
    }
}
