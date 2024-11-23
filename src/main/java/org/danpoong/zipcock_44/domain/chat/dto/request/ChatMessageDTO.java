package org.danpoong.zipcock_44.domain.chat.dto.request;
public class ChatMessageDTO {
    private String senderId;
    private String receiverId;
    private String content;
    private Long roomId;

    // Getters and Setters
    public String getSenderId() {
        return senderId;
    }
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
    public String getReceiverId() {
        return receiverId;
    }
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
