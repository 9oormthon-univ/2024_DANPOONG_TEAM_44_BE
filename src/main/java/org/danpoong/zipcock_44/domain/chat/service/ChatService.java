package org.danpoong.zipcock_44.domain.chat.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.domain.chat.dto.request.STOMPChatMessageDto;
import org.danpoong.zipcock_44.domain.chat.dto.response.ChatMessageDto;
import org.danpoong.zipcock_44.domain.chat.entity.ChatMessage;
import org.danpoong.zipcock_44.domain.chat.entity.ChatMessageRead;
import org.danpoong.zipcock_44.domain.chat.entity.ChatRoom;
import org.danpoong.zipcock_44.domain.chat.repository.ChatMessageReadRepository;
import org.danpoong.zipcock_44.domain.chat.repository.ChatMessageRepository;
import org.danpoong.zipcock_44.domain.chat.repository.ChatRoomRepository;
import org.danpoong.zipcock_44.domain.user.entity.User;
import org.danpoong.zipcock_44.domain.user.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.AccessDeniedException;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageReadRepository chatMessageReadRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    /**
     * 메시지 전송 메서드
     * @param messageDto 전송할 메시지 정보
     */
    @Transactional
    public void sendMessage(STOMPChatMessageDto messageDto) {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(messageDto.getChatRoomId())
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));

        // 발신자 조회
        User sender = userRepository.findByUsername(messageDto.getSenderUsername())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 메시지 생성 및 저장
        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .sender(sender)
                .content(messageDto.getContent())
                .build();
        chatMessageRepository.save(chatMessage);

        // 수신자 조회 (1:1 채팅 assumed)
        User recipient = getRecipient(chatRoom, sender);

        // 수신자에 대한 읽음 상태 설정 (isRead=false)
        if (recipient != null) {
            ChatMessageRead recipientRead = ChatMessageRead.builder()
                    .chatMessage(chatMessage)
                    .user(recipient)
                    .isRead(false) // 수신자는 읽지 않음으로 설정
                    .build();
            chatMessageReadRepository.save(recipientRead);
        }

        // 발신자에 대한 읽음 상태 설정 (isRead=true)
        ChatMessageRead senderRead = ChatMessageRead.builder()
                .chatMessage(chatMessage)
                .user(sender)
                .isRead(true) // 발신자는 메시지를 보냈으므로 읽음으로 설정
                .build();
        chatMessageReadRepository.save(senderRead);
        Long chatMessageReadId = senderRead.getId();
        HashMap<String, String> map = new HashMap<>();
        map.put("chatMessageReadId", String.valueOf(chatMessageReadId));
        List<HashMap<String, String>> list = messageDto.getList();
        list.add(map);

        //특정 유저한테 보내기
        messagingTemplate.convertAndSendToUser(messageDto.getReceiveUsername(), "/queue/messages",list);
    }

    /**
     * 특정 채팅방의 메시지 목록을 조회하고 읽지 않은 메시지를 읽음 처리
     * @param chatRoomId 채팅방 ID
     * @param userId 사용자 ID
     * @return 메시지 목록
     * @throws AccessDeniedException 접근 권한이 없을 경우 예외
     */
    @Transactional
    public List<ChatMessageDto> getChatMessages(Long chatRoomId, Long userId) throws AccessDeniedException {
        // 채팅방 조회
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new EntityNotFoundException("채팅방을 찾을 수 없습니다."));

        // 사용자 권한 확인
        if (!(chatRoom.getBuyer().getId() == (userId)) && !(chatRoom.getSeller().getId() == (userId))) {
            throw new AccessDeniedException("채팅방에 접근할 권한이 없습니다.");
        }

        // 메시지 목록 조회
        List<ChatMessage> messages = chatMessageRepository.findByChatRoomIdOrderByCreatedDateAsc(chatRoomId);

        // 읽지 않은 메시지의 읽음 상태 업데이트
        List<ChatMessageRead> unreadMessageReads = chatMessageReadRepository.findUnreadByChatRoomIdAndUserId(chatRoomId, userId);
        for (ChatMessageRead messageRead : unreadMessageReads) {
            messageRead.markAsRead();
        }
        chatMessageReadRepository.saveAll(unreadMessageReads);

        // 메시지 DTO로 변환하여 반환
        return messages.stream()
                .map(ChatMessageDto::fromEntity)
                .collect(Collectors.toList());
    }



    /**
     * 발신자를 제외한 수신자 조회 (1:1 채팅 assumed)
     * @param chatRoom 채팅방 엔티티
     * @param sender 발신자 엔티티
     * @return 수신자 엔티티
     */
    private User getRecipient(ChatRoom chatRoom, User sender) {
        if (chatRoom.getBuyer().equals(sender)) {
            return chatRoom.getSeller();
        } else {
            return chatRoom.getBuyer();
        }
    }
}
