package org.danpoong.zipcock_44.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostSearchResponseDTO {
    private long id;
    private String username;
    private String title;
    private String content;
    private String domain;
    private String authorName; // 작성자 이름 추가
    private LocalDateTime createdDate; // 작성 날짜 추가
    private ImageDTO image; // 이미지 정보 리스트

    @Getter
    @Builder
    public static class ImageDTO {
        private String fileName; // 파일 이름
        private String fileData; // Base64 인코딩된 이미지 데이터
    }
}
