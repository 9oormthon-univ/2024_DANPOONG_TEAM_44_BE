package org.danpoong.zipcock_44.domain.chat.repository;

import org.danpoong.zipcock_44.domain.chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByPostIdAndBuyerId(Long postId, Long buyerId);
    List<ChatRoom> findByBuyerIdOrSellerId(Long buyerId, Long sellerId);
}
