package org.danpoong.zipcock_44.global.security.service;


import lombok.Data;
import org.danpoong.zipcock_44.domain.user.UserRepository;
import org.danpoong.zipcock_44.domain.user.dto.request.ChangeLocationRequest;
import org.danpoong.zipcock_44.domain.user.entity.User;
import org.danpoong.zipcock_44.global.security.entity.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
@Data
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private final UserRepository userRepository;

    // UserDetails 인터페이스의 loadUserByUsername 메소드를 오버라이드
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException{

        // Username 대신 LoginId를 username에 넣도록 함. 해당 플렛폼에서 username은 닉네임으로 사용됨
        Optional<User> userData = userRepository.findByLoginId(loginId);
        // Optional로 감싸서 해당 정보가 존재하지 않을 경우에 null 반환

        // Optional이 비어 있지 않으면 CustomUserDetails를 반환, 비어 있으면 예외를 던짐
        return userData
                .map(user->new UserDetailsImpl(user))
                .orElseThrow(()->new UsernameNotFoundException("사용자를 찾을 수 없습니다."));
    }

    // 회원 탈퇴 로직구현
    @Transactional
    public ResponseEntity<?> signOut(String loginId){
        Optional<User> user = userRepository.findByLoginId(loginId);
        if(user.isPresent()){
            userRepository.deleteById(user.get().getId());
            return new ResponseEntity<>("회원 탈퇴 성공", HttpStatus.OK);
        }
        else{
            throw new RuntimeException("회원 정보가 존재하지 않음");
        }
    }

    @Transactional
    public ResponseEntity<?> changeLocation(String loginId, ChangeLocationRequest request){
        Optional<User> user = userRepository.findByLoginId(loginId);
        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User not found with loginId: " + loginId);
        }

        User userPatch = user.get();

        userPatch.setSido(request.getSido());
        userPatch.setSigungu(request.getSigungu());
        userPatch.setRoadname(request.getRoadname());

        userRepository.save(userPatch);
        return ResponseEntity.ok("사용자 위치 정보가 성공적으로 업데이트되었습니다.");
    }

}
