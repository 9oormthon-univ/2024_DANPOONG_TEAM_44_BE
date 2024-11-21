package org.danpoong.zipcock_44.domain.chat.repository;

import org.danpoong.zipcock_44.domain.chat.entity.ChatMessage;
import org.danpoong.zipcock_44.domain.chat.entity.ChatMessageRead;
import org.danpoong.zipcock_44.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChatMessageReadRepository extends JpaRepository<ChatMessageRead, Long> {
    Optional<ChatMessageRead> findByChatMessageAndUser(ChatMessage chatMessage, User user);

    @Query("SELECT COUNT(cmr) FROM ChatMessageRead cmr WHERE cmr.chatMessage.chatRoom.id = :chatRoomId AND cmr.user.id = :userId AND cmr.isRead = false")
    int countUnreadMessages(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);

    @Query("SELECT cmr FROM ChatMessageRead cmr WHERE cmr.chatMessage.chatRoom.id = :chatRoomId AND cmr.user.id = :userId AND cmr.isRead = false")
    List<ChatMessageRead> findUnreadByChatRoomIdAndUserId(@Param("chatRoomId") Long chatRoomId, @Param("userId") Long userId);

}
