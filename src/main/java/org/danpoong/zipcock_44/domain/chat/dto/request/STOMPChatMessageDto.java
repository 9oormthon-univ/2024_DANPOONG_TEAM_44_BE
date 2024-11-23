package org.danpoong.zipcock_44.domain.chat.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.danpoong.zipcock_44.domain.chat.entity.ChatMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class STOMPChatMessageDto {

    private Long chatRoomId;

    private String senderUsername;
    private String receiveUsername;

    private String content;

    public List<HashMap<String, String>> list = new ArrayList<>();

    public List<HashMap<String, String>> getList() {
        HashMap<String, String> map = new HashMap<>();
        map.put("chatRoomId", String.valueOf(chatRoomId));
        map.put("content", content);
        list.add(map);
        return list;
    }
}
