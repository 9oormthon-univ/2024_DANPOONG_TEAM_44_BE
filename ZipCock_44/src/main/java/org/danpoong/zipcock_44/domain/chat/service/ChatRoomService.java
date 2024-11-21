package org.danpoong.zipcock_44.domain.chat.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.domain.chat.dto.response.ChatRoomDto;
import org.danpoong.zipcock_44.domain.chat.entity.ChatRoom;
import org.danpoong.zipcock_44.domain.chat.repository.ChatRoomRepository;
import org.danpoong.zipcock_44.domain.post.entity.Post;
import org.danpoong.zipcock_44.domain.post.repository.PostRepository;
import org.danpoong.zipcock_44.domain.user.User;
import org.danpoong.zipcock_44.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public ChatRoomDto createChatRoom(Long postId, Long buyerId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("게시글을 찾을 수 없습니다."));
        User buyer = userRepository.findById(buyerId)
                .orElseThrow(() -> new EntityNotFoundException("구매자를 찾을 수 없습니다."));
        User seller = post.getUser();

        //수정할사항
        if(buyer.getId() == seller.getId()){
            throw new IllegalArgumentException("판매자와 구매자가 똑같습니다.");
        }

        // 이미 존재하는 채팅방인지 확인
        Optional<ChatRoom> existingChatRoom = chatRoomRepository.findByPostIdAndBuyerId(postId, buyerId);
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
}
