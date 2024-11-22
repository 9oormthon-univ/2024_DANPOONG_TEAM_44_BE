package org.danpoong.zipcock_44.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.domain.chat.dto.request.CreateChatRoomRequest;
import org.danpoong.zipcock_44.domain.chat.dto.request.UserDto;
import org.danpoong.zipcock_44.domain.chat.dto.response.ChatRoomDto;
import org.danpoong.zipcock_44.domain.chat.dto.response.ChatRoomSearchDto;
import org.danpoong.zipcock_44.domain.chat.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    //채팅방 생성
    @PostMapping("/rooms")
    public ResponseEntity<ChatRoomDto> createChatRoom(@RequestBody CreateChatRoomRequest request) {
        ChatRoomDto chatRoomDto = chatRoomService.createChatRoom(request.getPostId(), request.getBuyerId());
        return ResponseEntity.ok(chatRoomDto);
    }

    //채팅방리스트 가져오기
    @GetMapping("/rooms/{userId}")
    public ResponseEntity<List<ChatRoomSearchDto>> getUserChatRooms(@PathVariable Long userId) {
        List<ChatRoomSearchDto> chatRooms = chatRoomService.getUserChatRooms(userId);
        return ResponseEntity.ok(chatRooms);
    }
}