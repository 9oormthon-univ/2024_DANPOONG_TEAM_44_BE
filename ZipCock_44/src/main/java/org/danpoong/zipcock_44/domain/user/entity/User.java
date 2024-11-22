package org.danpoong.zipcock_44.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.danpoong.zipcock_44.domain.chat.entity.ChatRoom;
import org.danpoong.zipcock_44.domain.post.entity.Post;
import org.danpoong.zipcock_44.global.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USER")
public class User extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private long id;

    @Column(name="loginId",nullable = false,unique = true)
    private String loginId;

    @Column(name="username",nullable = false,unique = true)
    private String username;

    @Column(name="password",nullable = false)
    private String password; // 비밀번호

    @Column(name="sido",updatable = true)
    private String sido; // 특별시 , 시 , 도 부분

    @Column(name = "sigungu",updatable = true)
    private String sigungu; // 시 , 군 , 구

    @Column(name = "roadname",updatable = true)
    private String roadname; // 도로명 주소


    @Column(name = "email", nullable = false, unique = true)
    private String email; // 이메일

    @Column(name = "role")
    private String role; // SpringSecurity 권한

    @OneToMany(mappedBy = "user")
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "buyer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> buyerChatRooms = new ArrayList<>();

    @OneToMany(mappedBy = "seller", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> sellerChatRooms = new ArrayList<>();

    @Builder
    public User(String loginId,String username,String password,String sido,String sigungu,String roadname,String email,String role){
        this.loginId = loginId;
        this.username = username;
        this.password = password;
        this.sido = sido;
        this.sigungu = sigungu;
        this.roadname = roadname;
        this.email = email;
        this.role = (role == null || role.isEmpty()) ? "ROLE_USER" : role;
    }
}
