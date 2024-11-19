package org.danpoong.zipcock_44.global.jwt.filter;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Data;
import org.danpoong.zipcock_44.domain.user.entity.User;
import org.danpoong.zipcock_44.global.security.entity.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Data
public class JWTFilter extends OncePerRequestFilter{
    private final JWTUtil jwtUtil;

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
            return;
        }

        //accessToken에서 loginId,Role 받아오기
        String loginId = jwtUtil.getLoginId(accessToken);
        String role = jwtUtil.getRole(accessToken);

        // user 객체 생성 후 필드 주입
        User user = User.builder()
                .loginId(loginId)
                .role(role)
                .build();

        UserDetailsImpl userDetailsImpl = new UserDetailsImpl(user);

        Authentication authToken = new UsernamePasswordAuthenticationToken(userDetailsImpl,null,userDetailsImpl.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }
}
