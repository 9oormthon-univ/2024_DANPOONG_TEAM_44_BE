package org.danpoong.zipcock_44.global.jwt.repository;

import jakarta.transaction.Transactional;
import org.danpoong.zipcock_44.global.jwt.entity.Refresh;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshRepository extends JpaRepository<Refresh,Long> {

    Boolean existsByRefresh(String refresh);

    // JWT를 탈취하여 토큰을 복제한 경우를 위해 발급즉시 서버측 저장소에 기억 후 기억되어있는 Refresh 토큰만 사용하고 나머지는 폐기
    @Transactional
    void deleteByRefresh(String refresh);

}
