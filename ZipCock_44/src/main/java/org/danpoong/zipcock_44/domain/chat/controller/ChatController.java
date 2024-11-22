package org.danpoong.zipcock_44.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.domain.chat.dto.request.STOMPChatMessageDto;
import org.danpoong.zipcock_44.domain.chat.dto.request.UserDto;
import org.danpoong.zipcock_44.domain.chat.dto.response.ChatMessageDto;
import org.danpoong.zipcock_44.domain.chat.service.ChatService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.nio.file.AccessDeniedException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @MessageMapping("/chat/message")
    public void sendMessage(STOMPChatMessageDto messageDto) {
        chatService.sendMessage(messageDto);
    }

    //채팅방 입장시 메세지 가져오기
    @GetMapping("/rooms/{chatRoomId}/messages/{userId}")
    public ResponseEntity<List<ChatMessageDto>> getChatMessages(
            @PathVariable Long chatRoomId,
            @PathVariable Long userId
    ) throws AccessDeniedException {
        List<ChatMessageDto> messages = chatService.getChatMessages(chatRoomId, userId);
        return ResponseEntity.ok(messages);
    }
}

