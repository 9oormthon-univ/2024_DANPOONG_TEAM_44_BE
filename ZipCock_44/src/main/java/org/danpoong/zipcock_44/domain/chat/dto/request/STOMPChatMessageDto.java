package org.danpoong.zipcock_44.domain.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.danpoong.zipcock_44.domain.chat.entity.ChatMessage;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class STOMPChatMessageDto {

    private Long chatRoomId;

    private Long senderId;

    private String content;

    public static STOMPChatMessageDto fromEntity(ChatMessage chatMessage) {
        return STOMPChatMessageDto.builder()
                .chatRoomId(chatMessage.getChatRoom().getId())
                .senderId(chatMessage.getSender().getId())
                .content(chatMessage.getContent())
                .build();
    }
}
