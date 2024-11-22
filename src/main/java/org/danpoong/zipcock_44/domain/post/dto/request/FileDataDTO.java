package org.danpoong.zipcock_44.domain.post.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDataDTO {
    private String fileName;
    private String fileContent; // Base64로 인코딩된 파일 내용
}
