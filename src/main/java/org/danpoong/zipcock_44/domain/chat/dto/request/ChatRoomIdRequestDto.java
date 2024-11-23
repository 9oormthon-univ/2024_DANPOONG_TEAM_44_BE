package org.danpoong.zipcock_44.domain.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
public class ChatRoomIdRequestDto {
    private Long roomId; // 채팅방 ID
    private Long sender;     // 요청을 보낸 사용자 ID
    private Long receiver;   // 요청 대상 사용자 ID

    public Long getRoomId() {
        return roomId;
    }

    public Long getSender() {
        return sender;
    }

    public Long getReceiver() {
        return receiver;
    }

    @Override
    public String toString() {
        return "ChatRoomIdRequestDto{" +
                "roomId=" + roomId +
                ", sender=" + sender +
                ", receiver=" + receiver +
                '}';
    }
}
