package org.danpoong.zipcock_44.domain.user.service;


import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.domain.user.UserRepository;
import org.danpoong.zipcock_44.domain.user.dto.request.SignUpRequest;
import org.danpoong.zipcock_44.domain.user.dto.response.SignUpResponse;
import org.danpoong.zipcock_44.domain.user.entity.User;
import org.danpoong.zipcock_44.global.security.entity.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;


@Data
@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) throws Exception {


        // 이메일 중복 체크
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new RuntimeException("해당 이메일은 이미 존재합니다.");
        }

        // 비밀번호 검증: null 또는 빈 값일 경우 예외 처리
        if (signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty()) {
            throw new IllegalArgumentException("비밀번호는 필수 항목입니다.");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(signUpRequest.getPassword());

        // User 엔티티 생성
        User user = User.builder()
                .loginId(signUpRequest.getLoginId())
                .username(signUpRequest.getUsername())
                .password(encodedPassword)
                .sido(signUpRequest.getSido())
                .sigungu(signUpRequest.getSigungu())
                .roadname(signUpRequest.getRoadname())
                .email(signUpRequest.getEmail())
                .role("ROLE_ADMIN")
                .build();

        // User 엔티티 저장
        User savedUser = userRepository.save(user);

        // SignUpResponse 생성 및 반환
        return SignUpResponse.builder()
                .id(savedUser.getId()) // 저장된 User의 ID
                .loginId(savedUser.getLoginId())
                .username(savedUser.getUsername())
                .sido(savedUser.getSido())
                .sigungu(savedUser.getSigungu())
                .roadname(savedUser.getRoadname())
                .email(savedUser.getEmail())
                .build();
    }
}
