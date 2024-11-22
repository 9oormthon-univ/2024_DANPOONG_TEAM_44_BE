package org.danpoong.zipcock_44.domain.user;

import org.danpoong.zipcock_44.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {

    // Username을 받아 DB 테이블에서
    Optional<User> findByUsername(String username);

    Optional<User> findByLoginId(String loginId);
    Boolean existsByUsername(String username);
    Optional<User> findByEmail(String email);


}
