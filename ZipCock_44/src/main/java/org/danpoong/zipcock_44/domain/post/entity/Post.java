package org.danpoong.zipcock_44.domain.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.danpoong.zipcock_44.domain.chat.entity.ChatRoom;
import org.danpoong.zipcock_44.domain.user.User;
import org.danpoong.zipcock_44.global.common.BaseEntity;

import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "POST")
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000, nullable = false)
    private String content;

    @Column(nullable = false)
    private double latitude; // 위도

    @Column(nullable = false)
    private double longitude; // 경도

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Image> images = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatRoom> chatRooms = new ArrayList<>();
    public Post(User user, String title, String content, double latitude, double longitude) {
        this.user = user;
        this.title = title;
        this.content = content;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    //-------연관관계 메서드-------//
    public void addImage(Image image) {
        image.setPost(this); // Post와 Image의 연관 관계 설정
        this.images.add(image);
    }
}
