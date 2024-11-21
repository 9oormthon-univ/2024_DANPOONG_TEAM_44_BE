package org.danpoong.zipcock_44.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.domain.chat.dto.request.CreateChatRoomRequest;
import org.danpoong.zipcock_44.domain.chat.dto.response.ChatRoomDto;
import org.danpoong.zipcock_44.domain.chat.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomDto> createChatRoom(@RequestBody CreateChatRoomRequest request) {
        ChatRoomDto chatRoomDto = chatRoomService.createChatRoom(request.getPostId(), request.getBuyerId());
        return ResponseEntity.ok(chatRoomDto);
    }
}