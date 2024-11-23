package org.danpoong.zipcock_44.domain.chat.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.danpoong.zipcock_44.domain.chat.entity.ChatMessageRead;
import org.danpoong.zipcock_44.domain.chat.entity.ChatRoom;
import org.danpoong.zipcock_44.domain.chat.repository.ChatMessageReadRepository;
import org.danpoong.zipcock_44.domain.chat.repository.ChatMessageRepository;
import org.danpoong.zipcock_44.domain.chat.repository.ChatRoomRepository;
import org.danpoong.zipcock_44.domain.user.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@AllArgsConstructor
public class ChatMessageService {
    private final ChatMessageReadRepository chatMessageReadRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void isReadMessage(Long chatMessageReadId) {
        ChatMessageRead chatMessageRead = chatMessageReadRepository.findById(chatMessageReadId).get();
        chatMessageRead.markAsRead();
    }
}
