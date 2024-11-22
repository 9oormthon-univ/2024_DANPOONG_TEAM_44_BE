package org.danpoong.zipcock_44.global.jwt.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class Refresh {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="loginId") // 실제 컬럼명 확인 후 수정
    private String loginId;

    @Column(name="refresh") // 실제 컬럼명 확인 후 수정
    private String refresh;

    @Column(name="expiration") // 실제 컬럼명 확인 후 수정
    private String expiration;

}
