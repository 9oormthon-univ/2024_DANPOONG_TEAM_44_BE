package org.danpoong.zipcock_44.domain.user.dto.response;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class KakaoLogoutResponseDTO {
    private String id; // 사용자 ID
    private String message; // 응답 메시지
}
