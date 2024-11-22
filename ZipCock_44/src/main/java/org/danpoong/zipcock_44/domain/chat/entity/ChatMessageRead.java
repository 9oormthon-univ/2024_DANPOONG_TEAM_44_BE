package org.danpoong.zipcock_44.domain.chat.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.danpoong.zipcock_44.domain.user.entity.User;
import org.danpoong.zipcock_44.global.common.BaseTimeEntity;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessageRead extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_message_read_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_id", nullable = false)
    private ChatMessage chatMessage;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean isRead = false;

    @Builder
    public ChatMessageRead(ChatMessage chatMessage, User user, boolean isRead) {
        this.chatMessage = chatMessage;
        this.user = user;
        this.isRead = isRead;
    }

    public void markAsRead() {
        this.isRead = true;
    }
}
