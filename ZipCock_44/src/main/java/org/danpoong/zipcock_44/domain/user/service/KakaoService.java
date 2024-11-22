package org.danpoong.zipcock_44.domain.user.service;

import io.netty.handler.codec.http.HttpHeaderValues;
import jakarta.servlet.http.HttpSession;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.danpoong.zipcock_44.domain.user.UserRepository;
import org.danpoong.zipcock_44.domain.user.dto.response.KakaoLogoutResponseDTO;
import org.danpoong.zipcock_44.domain.user.dto.response.KakaoTokenResponseDTO;
import org.danpoong.zipcock_44.domain.user.dto.response.KakaoUserInfoResponseDTO;
import org.danpoong.zipcock_44.domain.user.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.*;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
@Data
public class KakaoService {


    private String clientId;
    private final String KAUTH_TOKEN_URL_HOST;
    private final String KAUTH_USER_URL_HOST;
    private final String redirect_uri;
    private final UserRepository userRepository;
    private final RestTemplate restTemplate;
    private static final Logger log = LoggerFactory.getLogger(KakaoService.class);


    @Autowired
    public KakaoService(UserRepository userRepository,@Value("${spring.kakao.redirect}") String redirect_uri,@Value("${spring.kakao.client}") String clientId,RestTemplate restTemplate) {
        KAUTH_TOKEN_URL_HOST ="https://kauth.kakao.com";
        KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
        this.clientId = clientId;
        this.redirect_uri = redirect_uri;
        this.userRepository = userRepository;

        // RestTemplate 직접 생성
        restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        this.restTemplate = restTemplate;
    }

    // 주석 1
    public String getAccessTokenFromKakao(String code) {

        // Spring WebFlux로 토큰 받아오기 ( 어렵다.. )
        KakaoTokenResponseDTO kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST)
                .post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code") // 요청인자 : grant_type
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .queryParam("redirect_uri",redirect_uri)
                        //.queryParam("client_secret",client_secret)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDTO.class)
                .block();

        log.info(" [Kakao Service] Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        log.info(" [Kakao Service] Refresh Token ------> {}", kakaoTokenResponseDto.getRefreshToken());
        //제공 조건: OpenID Connect가 활성화 된 앱의 토큰 발급 요청인 경우 또는 scope에 openid를 포함한 추가 항목 동의 받기 요청을 거친 토큰 발급 요청인 경우
        log.info(" [Kakao Service] Id Token ------> {}", kakaoTokenResponseDto.getIdToken());
        log.info(" [Kakao Service] Scope ------> {}", kakaoTokenResponseDto.getScope());

        return kakaoTokenResponseDto.getAccessToken();
    }

    public KakaoUserInfoResponseDTO getUserInfo(String accessToken){

        KakaoUserInfoResponseDTO userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoUserInfoResponseDTO.class)
                .block();

        log.info("[ Kakao Service ] Auth ID ---> {} ", userInfo.getId());
        log.info("[ Kakao Service ] NickName ---> {} ", userInfo.getKakaoAccount().getProfile().getNickName());
        log.info("[ Kakao Service ] ProfileImageUrl ---> {} ", userInfo.getKakaoAccount().getProfile().getProfileImageUrl());

        return userInfo;
    }


    // 받아온 회원 정보를 DB에 저장하는 메소드
    public User saveOrUpdateKakaoUser(KakaoUserInfoResponseDTO kakaoUserInfoResponseDTO){

        Long loginId = kakaoUserInfoResponseDTO.getId();
        String kakaoId = kakaoUserInfoResponseDTO.getKakaoAccount().getProfile().getNickName();
        String email = kakaoUserInfoResponseDTO.getKakaoAccount().getEmail();

        User user = userRepository.findByEmail(email) // 새로운 로그인 객체일 경우 build()를 통해 Set 하고 유저를 등록
                .orElseGet(()-> User.builder()
                        .loginId(String.valueOf(loginId)) // Long 타입의 kakaoId를 String 타입으로 형 변환
                        .username(kakaoId)
                        .password("kakaoPass")
                        .sido("서울시") // 카카오로그인 인 경우 default로 "서울시 단풍로 44" 를 기입
                        .sigungu("단풍로")
                        .roadname("44")
                        .email(email)
                        .role("ROLE_ADMIN")
                        .build());

        return userRepository.save(user);
    }

    public ResponseEntity<String> logout(String accessToken) {
        // 현재 로그인된 사용자 정보 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            log.warn("Unauthorized logout attempt");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body("User is not authenticated. Please log in.");
        }

        String username = authentication.getName();
        log.info("Logout request received for user: {}", username);

        try {
            WebClient webClient = WebClient.create(KAUTH_USER_URL_HOST);
            KakaoLogoutResponseDTO response = webClient.post()
                    .uri("/v1/user/logout")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .retrieve()
                    .onStatus(HttpStatusCode::isError, clientResponse -> Mono.error(new RuntimeException("Kakao API error")))
                    .bodyToMono(KakaoLogoutResponseDTO.class)
                    .block();

            log.info("Kakao logout successful: {}", response);
            return ResponseEntity.ok("Logout successful");
        } catch (Exception e) {
            log.error("Error during Kakao logout: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error during Kakao logout: " + e.getMessage());
        }
    }
}






