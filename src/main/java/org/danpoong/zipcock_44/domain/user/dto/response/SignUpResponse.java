package org.danpoong.zipcock_44.domain.user.dto.response;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SignUpResponse {

    private Long id;
    private String loginId;
    private String username;
    private String sido;
    private String sigungu;
    private String roadname;
    private String email;
}
