package org.danpoong.zipcock_44.domain.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.danpoong.zipcock_44.domain.chat.dto.request.ChatRoomIdRequestDto;
import org.danpoong.zipcock_44.domain.chat.dto.response.ChatRoomIdResponseDto;
import org.danpoong.zipcock_44.domain.chat.dto.request.CreateChatRoomRequest;
import org.danpoong.zipcock_44.domain.chat.dto.response.ChatRoomDto;
import org.danpoong.zipcock_44.domain.chat.dto.response.ChatRoomSearchDto;
import org.danpoong.zipcock_44.domain.chat.service.ChatRoomService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/chat")
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final SimpMessagingTemplate messagingTemplate;

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

    @MessageMapping("/chat/subscribe")
    @SendTo("/sub/chat/queue")
    public void subscribeUser(ChatRoomIdRequestDto chatRoomIdRequestDto) throws Exception {
        log.info("dto = {}", chatRoomIdRequestDto.toString());
        Thread.sleep(200); // simulated delay
        messagingTemplate.convertAndSend("/sub/chat/queue" + chatRoomIdRequestDto.getReceiver(), new ChatRoomIdResponseDto(chatRoomIdRequestDto));
    }
}