package org.danpoong.zipcock_44.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ChatMessageInfo {
    private String lastMessageContent;
    private int unreadMessageCount;
    private String timeSinceLastMessage;
}
