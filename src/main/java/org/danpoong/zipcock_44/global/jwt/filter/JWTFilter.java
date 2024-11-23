package org.danpoong.zipcock_44.global.jwt.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.danpoong.zipcock_44.global.security.entity.UserDetailsImpl;
import org.danpoong.zipcock_44.global.security.service.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Component
@Data
@Slf4j
public class JWTFilter extends OncePerRequestFilter{


    private final JWTUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;

    @Autowired
    public JWTFilter(JWTUtil jwtUtil, UserDetailsServiceImpl userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        //  요청 헤더에서 Access 토큰 가져오기
        String accessToken = request.getHeader("access");

        // Access 토큰이 비어있을 경우
        if(accessToken == null){
            filterChain.doFilter(request,response);
            return;
        }

        // 토큰 만료여부를 확인
        try{
            jwtUtil.isExpired(accessToken);
        } catch(ExpiredJwtException e){

            PrintWriter writer = response.getWriter();
            writer.print("Access Token Expired");
            log.info("a");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access 인지 확인하기 위해 String 에 담기
        String category = jwtUtil.getCategory(accessToken);

        // access 토큰인지 확인
        if(!category.equals("access")){
            PrintWriter writer = response.getWriter();
            writer.print("Invalid Access Token");

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            log.info("b");
            return;
        }

        // accessToken에서 추출한 loginId를 바탕으로 DB를 통해 사용자 객체 생성, ContextHolder에 반영
        UserDetailsImpl userDetails = (UserDetailsImpl) userDetailsService.loadUserByUsername(jwtUtil.getLoginId(accessToken));
        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
        log.info("last");
        filterChain.doFilter(request, response);
    }
}
