package org.danpoong.zipcock_44.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.danpoong.zipcock_44.domain.chat.entity.ChatMessage;

/**
 * 메시지 도착 시 수신자에게 전송되는 알림 DTO.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ChatNotificationDto {

    private Long messageId;
    private Long chatRoomId;
    private Long senderId;
    private String senderName;
    private String content;
    private String timestamp;

    public static ChatNotificationDto fromMessage(ChatMessage chatMessage) {
        return ChatNotificationDto.builder()
                .messageId(chatMessage.getId())
                .chatRoomId(chatMessage.getChatRoom().getId())
                .senderId(chatMessage.getSender().getId())
                .senderName(chatMessage.getSender().getName())
                .content(chatMessage.getContent())
                .timestamp(chatMessage.getCreatedDate().toString())
                .build();
    }
}
