package org.danpoong.zipcock_44.domain.chat.dto.response;

import lombok.Builder;
import lombok.Data;
import org.danpoong.zipcock_44.domain.chat.entity.ChatRoom;

import java.time.LocalDateTime;

@Data
@Builder
public class ChatRoomDto {

    private Long id;
    private Long postId;
    private Long buyerId;
    private Long sellerId;
    private String postTitle; // 예시: 게시글 제목
    private String buyerName; // 예시: 구매자 이름
    private String sellerName; // 예시: 판매자 이름
    private LocalDateTime createdAt;

    public static ChatRoomDto fromEntity(ChatRoom chatRoom) {
        return ChatRoomDto.builder()
                .id(chatRoom.getId())
                .postId(chatRoom.getPost().getId())
                .buyerId(chatRoom.getBuyer().getId())
                .sellerId(chatRoom.getSeller().getId())
                .postTitle(chatRoom.getPost().getTitle())
                .buyerName(chatRoom.getBuyer().getUsername())
                .sellerName(chatRoom.getSeller().getUsername())
                .createdAt(chatRoom.getCreatedDate())
                .build();
    }
}
