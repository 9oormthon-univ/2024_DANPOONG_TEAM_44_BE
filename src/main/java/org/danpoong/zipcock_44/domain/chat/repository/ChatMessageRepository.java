package org.danpoong.zipcock_44.domain.chat.repository;

import org.danpoong.zipcock_44.domain.chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    ChatMessage findTopByChatRoomIdOrderByCreatedDateDesc(Long chatRoomId);
    List<ChatMessage> findByChatRoomIdOrderByCreatedDateAsc(Long chatRoomId);
}
