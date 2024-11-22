package org.danpoong.zipcock_44.domain.user.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SignUpRequest {

    private Long id;
    private String loginId;
    private String username;
    private String password;
    private String sido;
    private String sigungu;
    private String roadname;
    private String email;
    private String role;

}
