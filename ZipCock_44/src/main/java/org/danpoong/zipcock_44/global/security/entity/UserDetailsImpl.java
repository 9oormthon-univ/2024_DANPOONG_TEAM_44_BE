package org.danpoong.zipcock_44.global.security.entity;

import jakarta.transaction.Transactional;
import lombok.Data;
import org.danpoong.zipcock_44.domain.user.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Transactional
@Data
public class UserDetailsImpl implements UserDetails {

    private final User user;

    // 사용자의 권한을 반환하는 메소드
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){

        return List.of(new SimpleGrantedAuthority(user.getRole()));

    }


    // 사용자의 비밀번호 가져오기
    @Override
    public String getPassword(){
        return user.getPassword();
    }

    // 사용자의 아이디 가져오기 :
    // LoginFilter 클래스에서 UsernamePasswordAuthenticationToken(loginId,password); 형식으로 가져왔으므로 이를 준수해야함
    @Override
    public String getUsername(){
        return user.getLoginId();
    }

    // 계정 만료여부 확인 메소드
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 계정 잠금여부 확인 메소드
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 자격 증명 만료여부 확인
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 계정 활성화 여부 확인
    @Override
    public boolean isEnabled() {
        return true;
    }


}
