package org.danpoong.zipcock_44.domain.chat.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.domain.chat.dto.response.ChatRoomDto;
import org.danpoong.zipcock_44.domain.chat.dto.response.ChatRoomSearchDto;
import org.danpoong.zipcock_44.domain.chat.entity.ChatMessage;
import org.danpoong.zipcock_44.domain.chat.entity.ChatRoom;
import org.danpoong.zipcock_44.domain.chat.repository.ChatMessageReadRepository;
import org.danpoong.zipcock_44.domain.chat.repository.ChatMessageRepository;
import org.danpoong.zipcock_44.domain.chat.repository.ChatRoomRepository;
import org.danpoong.zipcock_44.domain.post.entity.Post;
import org.danpoong.zipcock_44.domain.post.repository.PostRepository;
import org.danpoong.zipcock_44.domain.user.entity.User;
import org.danpoong.zipcock_44.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageReadRepository chatMessageReadRepository;

    public ChatRoomDto createChatRoom(Long postId, String username) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        User buyer = userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("구매자를 찾을 수 없습니다."));
        User seller = post.getUser();

        //수정할사항
        if(buyer.getId() == seller.getId()){
            throw new IllegalArgumentException("판매자와 구매자가 똑같습니다.");
        }

        // 이미 존재하는 채팅방인지 확인
        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findByPost_IdAndBuyer_Username(postId, username);
        if (existingChatRoom.isPresent()) {
            return ChatRoomDto.fromEntity(existingChatRoom.get());
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .post(post)
                .buyer(buyer)
                .seller(seller)
                .build();
        chatRoomRepository.save(chatRoom);

        return ChatRoomDto.fromEntity(chatRoom);
    }

    public List<ChatRoomSearchDto> getUserChatRooms(Long userId) {
        // 사용자가 구매자 또는 판매자로 참여한 채팅방 목록 조회
        List<ChatRoom> chatRooms = chatRoomRepository.findByBuyerIdOrSellerId(userId, userId);

        return chatRooms.stream()
                .map(chatRoom -> {
                    // 상대방 이름
                    String opponentName = getOpponentName(chatRoom, userId);

                    // 마지막 메시지 및 시간
                    ChatMessage lastMessage = chatMessageRepository.findTopByChatRoomIdOrderByCreatedDateDesc(chatRoom.getId());
                    String lastMessageContent = lastMessage != null ? lastMessage.getContent() : "";
                    String timeSinceLastMessage = calculateTimeSinceLastMessage(lastMessage);

                    // 읽지 않은 메시지 수
                    int unreadMessageCount = chatMessageReadRepository.countUnreadMessages(chatRoom.getId(), userId);

                    return ChatRoomSearchDto.builder()
                            .chatRoomId(chatRoom.getId())
                            .opponentName(opponentName)
                            .postTitle(chatRoom.getPost().getTitle())
                            .lastMessageContent(lastMessageContent)
                            .unreadMessageCount(unreadMessageCount)
                            .timeSinceLastMessage(timeSinceLastMessage)
                            .sellerId(chatRoom.getSeller().getId())
                            .buyerId(chatRoom.getBuyer().getId())
                            .postId(chatRoom.getPost().getId())
                            .build();
                })
                .collect(Collectors.toList());
    }

    private String getOpponentName(ChatRoom chatRoom, Long userId) {
        if (chatRoom.getBuyer().getId() == (userId)) {
            return chatRoom.getSeller().getUsername();
        } else {
            return chatRoom.getBuyer().getUsername();
        }
    }

    private String calculateTimeSinceLastMessage(ChatMessage lastMessage) {
        if (lastMessage == null) {
            return "";
        }
        Duration duration = Duration.between(lastMessage.getCreatedDate(), LocalDateTime.now());
        long minutes = duration.toMinutes();

        if (minutes < 1) {
            return "방금 전";
        } else if (minutes < 60) {
            return minutes + "분 전";
        } else if (minutes < 1440) {
            return (minutes / 60) + "시간 전";
        } else {
            return (minutes / 1440) + "일 전";
        }
    }
}
