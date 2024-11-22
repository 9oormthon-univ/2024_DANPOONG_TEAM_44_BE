package org.danpoong.zipcock_44.domain.post.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class PostIdSearchResponseDTO {
    private Long id; // 게시물 ID
    private String title;   //제목
    private String content; // 게시물 내용
    private String authorName; // 작성자 이름
    private Long authorId; // 작성자 아이디
    private LocalDateTime createdDate; // 작성일
    private double latitude; // 위도
    private double longitude; // 경도
    private String representativeImageFileName;
    private String representativeImageFileData;
    private List<String> imageFileNames; // 이미지 파일명 리스트
    private List<String> imageFileData; // Base64로 인코딩된 이미지 데이터 리스트
}
