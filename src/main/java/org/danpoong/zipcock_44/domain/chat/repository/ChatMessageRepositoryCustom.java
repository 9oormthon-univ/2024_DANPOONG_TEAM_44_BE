package org.danpoong.zipcock_44.domain.chat.repository;

import org.danpoong.zipcock_44.domain.chat.dto.response.ChatMessageInfo;

public interface ChatMessageRepositoryCustom {
    ChatMessageInfo findMessageInfoByChatRoomId(Long chatRoomId, Long userId);
}
