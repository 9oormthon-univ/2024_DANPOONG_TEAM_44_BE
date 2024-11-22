package org.danpoong.zipcock_44.domain.chat.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.danpoong.zipcock_44.domain.chat.entity.ChatMessage;

import java.time.LocalDateTime;


@Data
@NoArgsConstructor
public class ChatMessageDto {
    private Long id;
    private Long chatRoomId;
    private Long senderId;
    private String senderName;
    private String content;
    private LocalDateTime createdDate;

    public static ChatMessageDto fromEntity(ChatMessage chatMessage) {
        ChatMessageDto dto = new ChatMessageDto();
        dto.setId(chatMessage.getId());
        dto.setChatRoomId(chatMessage.getChatRoom().getId());
        dto.setSenderId(chatMessage.getSender().getId());
        dto.setSenderName(chatMessage.getSender().getUsername());
        dto.setContent(chatMessage.getContent());
        dto.setCreatedDate(chatMessage.getCreatedDate());
        return dto;
    }
}
