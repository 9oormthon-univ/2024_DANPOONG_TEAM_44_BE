package org.danpoong.zipcock_44.global.security.config;

import jakarta.servlet.http.HttpServletRequest;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.global.jwt.filter.JWTFilter;
import org.danpoong.zipcock_44.global.jwt.filter.JWTUtil;
import org.danpoong.zipcock_44.global.jwt.filter.LoginFilterImpl;
import org.danpoong.zipcock_44.global.jwt.filter.LogoutFilterImpl;
import org.danpoong.zipcock_44.global.jwt.repository.RefreshRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;


import java.util.Collections;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AuthenticationConfiguration authenticationConfiguration;
    // AuthenticationManager가 인자로 받을 AuthenticationConfiguration 객체 생성자 주입
    // @Data 혹은 @RequiredArgsConstructor 어노테이션으로 DI 필수
    private final JWTUtil jwtUtil;
    private final RefreshRepository refreshRepository;



    // 카카오 로그인 허용 URL
    public static final String[] allowUrls = {
            "/swagger-ui/**",
            "/swagger-resources/**",
            "/v3/api-docs/**",
            "/api/v1/posts/**",
            "/api/v1/replies/**",
            //"/login",
            "/auth/login/kakao/**"
    };

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception{
        return configuration.getAuthenticationManager();
    }
    // AuthenticationManager Bean에 등록


    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
    // BCryptPasswordEncoder 클래스의 생성자
    // BCryptPasswordEncoder : Spring Security 에서 비밀번호를 암호화 할 때 사용하는 클래스

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // CORS 설정
                .cors((corsCustomizer->corsCustomizer.configurationSource((new CorsConfigurationSource() {
                    // corsCustomizer를 통해서 CORFS 설정 커스터마이즈
                    @Override
                    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
                        CorsConfiguration config = new CorsConfiguration();
                        // CORS 설정 객체 생성

                        config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                        // 3000번 포트로 들어오는 요청 허용
                        config.setAllowedMethods(Collections.singletonList("*"));
                        // 모든 REST 메소드 호용
                        config.setAllowCredentials(true);
                        // Credentials 허용
                        config.setAllowedHeaders(Collections.singletonList("*"));
                        // 모든 헤더 허용
                        config.setMaxAge(3600L);
                        // 요청 캐시 시간 설정 -> 1시간
                        config.setExposedHeaders(Collections.singletonList("Authorization"));
                        // 응답 헤더로 노출할 헤더를 설정

                        return config;

                    }
                }))));

        http
                .csrf((auth)->auth.disable()); // csrf disable 설정
        http
                .formLogin((auth)-> auth.disable()); // From 로그인 방식 disable
        http
                .httpBasic((auth)-> auth.disable()); // http basic 인증 방식 disable
        http
                .authorizeHttpRequests((auth)->auth // 경로별 인가 작업
                        .requestMatchers("/login","/signup").permitAll() // 모든 사용자에게 접근 허용
                        .requestMatchers(allowUrls).permitAll()
                        .requestMatchers("/admin").hasRole("ADMIN") // ADMIN 권한이 있는 사용자만 접근 가능
                        .requestMatchers("/reissue").permitAll() // Refresh로 Access 토큰 재발급 기능 URL
                        .requestMatchers("/signout").hasRole("ADMIN") // 회원 탈퇴는 인증된 사용자만 가능
                        .anyRequest().authenticated()); // 나머지 경로는 인증된 사용자만 사용 가능
        http
                .addFilterBefore(new JWTFilter(jwtUtil), LoginFilterImpl.class);
        http
                .addFilterAt(new LoginFilterImpl(authenticationManager(authenticationConfiguration), jwtUtil, refreshRepository), UsernamePasswordAuthenticationFilter.class);
        http
                .addFilterBefore(new LogoutFilterImpl(jwtUtil, refreshRepository), org.springframework.security.web.authentication.logout.LogoutFilter.class);
        // 로그인 필터
        // addFilterAt : 필터 체인에 필터를 사입하는 메소드
        // LoginFilter : 로그인 요청을 처리하는 커스텀 터
        // authenticationConfiguration : 인증 관리자에 필요한 설정을 제공하는 객체 ( 코드 참조 ) -> authenticationManager를 사용해서 로그인 인증 처리
        // UsernamePasswordAuthenticationFilter.class : 사용자 이름과 비밀번호를 사용하여 인증을 수행하는 필터 ( Spring Security 기본 제공 )


        http // 세션 설정
                .sessionManagement((session)->session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)); // JWT를 통한 인증 - 인가를 위해서 세션을 STATELESS 상태로 설정 -> RESTful API


        return http.build();
    }
}
