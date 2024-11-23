package org.danpoong.zipcock_44.domain.post.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PostRequestDTO {
    private String title;
    private String content;
    private double latitude;
    private double longitude;
    private FileDataDTO representativeFileData;
    private List<FileDataDTO> fileData;
}
