package org.danpoong.zipcock_44.domain.chat.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.danpoong.zipcock_44.domain.chat.dto.request.ChatRoomIdRequestDto;

@Getter
@AllArgsConstructor
public class ChatRoomIdResponseDto {
    private Long roomId;
    private String type;
    private Long sender;

    public ChatRoomIdResponseDto(ChatRoomIdRequestDto chatRoomIdRequestDto) {
        this.roomId = chatRoomIdRequestDto.getRoomId();
        this.type = "SUB";
        this.sender = chatRoomIdRequestDto.getSender();
    }
}