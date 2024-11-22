package org.danpoong.zipcock_44.domain.user.repository;

import org.danpoong.zipcock_44.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

}
