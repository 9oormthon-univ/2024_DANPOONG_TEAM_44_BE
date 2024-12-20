package org.danpoong.zipcock_44.domain.user.service;


import lombok.Data;
import org.danpoong.zipcock_44.domain.user.dto.request.SignUpRequest;
import org.danpoong.zipcock_44.domain.user.dto.response.SignUpResponse;
import org.danpoong.zipcock_44.domain.user.entity.User;
import org.danpoong.zipcock_44.domain.user.repository.UserRepository;
import org.danpoong.zipcock_44.global.common.code.ErrorCode;
import org.danpoong.zipcock_44.global.common.response.CustomException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;


@Data
@Service
public class UserService {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public SignUpResponse signUp(SignUpRequest signUpRequest) throws Exception {
        // 이메일 중복 체크
        if (userRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            throw new CustomException(ErrorCode.EMAIL_DUPLICATED);
        }

        // 비밀번호 검증: null 또는 빈 값일 경우 예외 처리
        if (signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty()) {
            throw new CustomException(ErrorCode.PASSWORD_REQUIRED);
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

    public boolean checkIfEmailDuplicated(Map<String, String> req) {
        return userRepository.existsByEmail(req.get("email"));
    }

    public boolean checkIfUsernameDuplicated(Map<String, String> req) {
        return userRepository.existsByUsername(req.get("username"));
    }

}
