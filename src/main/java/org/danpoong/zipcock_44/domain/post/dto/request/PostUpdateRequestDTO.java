package org.danpoong.zipcock_44.domain.post.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostUpdateRequestDTO {
    private Long postId;
    private String title;
    private String content;
    private double latitude;
    private double longitude;
    private String domain;
    private FileDataDTO representativeFileData;
    private List<FileDataDTO> fileData;
}
