package org.danpoong.zipcock_44.domain.chat.entity;

import jakarta.persistence.*;
import lombok.*;
import org.danpoong.zipcock_44.domain.post.entity.Post;
import org.danpoong.zipcock_44.domain.user.User;
import org.danpoong.zipcock_44.global.common.BaseTimeEntity;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "buyer_id", nullable = false)
    private User buyer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @OneToMany(mappedBy = "chatRoom", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("createdDate ASC")
    private List<ChatMessage> messages = new ArrayList<>();

    @Builder
    public ChatRoom(Post post, User buyer, User seller) {
        this.post = post;
        this.buyer = buyer;
        this.seller = seller;
    }
}
