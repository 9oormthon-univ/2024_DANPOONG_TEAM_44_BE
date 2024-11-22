package org.danpoong.zipcock_44.domain.chat.dto.response;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.danpoong.zipcock_44.domain.chat.entity.ChatRoom;

@Data
@NoArgsConstructor
public class ChatRoomSearchDto {

    private Long chatRoomId;
    private String opponentName; // 상대방 이름
    private String postTitle; // 채팅방의 게시글 제목
    private String lastMessageContent; // 마지막 메시지 내용
    private int unreadMessageCount; // 읽지 않은 메시지 수
    private String timeSinceLastMessage; // 마지막 메시지 이후 시간 차이

    private Long sellerId;
    private Long buyerId;
    private Long postId;

    @Builder
    public ChatRoomSearchDto(Long chatRoomId, String opponentName, String postTitle, String lastMessageContent,
                       int unreadMessageCount, String timeSinceLastMessage, Long sellerId, Long buyerId, Long postId) {
        this.chatRoomId = chatRoomId;
        this.opponentName = opponentName;
        this.postTitle = postTitle;
        this.lastMessageContent = lastMessageContent;
        this.unreadMessageCount = unreadMessageCount;
        this.timeSinceLastMessage = timeSinceLastMessage;
        this.sellerId = sellerId;
        this.buyerId = buyerId;
        this.postId = postId;
    }

    public static ChatRoomSearchDto fromEntity(ChatRoom chatRoom, Long userId) {
        String opponentName;
        if (chatRoom.getBuyer().getId() == (userId)) {
            opponentName = chatRoom.getSeller().getUsername(); // 판매자 이름
        } else {
            opponentName = chatRoom.getBuyer().getUsername(); // 구매자 이름
        }

        String postTitle = chatRoom.getPost().getTitle();
        String lastMessageContent = "";
        String timeSinceLastMessage = "";
        int unreadMessageCount = 0;

        return ChatRoomSearchDto.builder()
                .chatRoomId(chatRoom.getId())
                .opponentName(opponentName)
                .postTitle(postTitle)
                .lastMessageContent(lastMessageContent)
                .unreadMessageCount(unreadMessageCount)
                .timeSinceLastMessage(timeSinceLastMessage)
                .sellerId(chatRoom.getSeller().getId())
                .buyerId(chatRoom.getBuyer().getId())
                .postId(chatRoom.getPost().getId())
                .build();
    }


}
