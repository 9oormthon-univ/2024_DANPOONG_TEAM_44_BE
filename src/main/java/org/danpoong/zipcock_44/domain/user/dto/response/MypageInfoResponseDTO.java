package org.danpoong.zipcock_44.domain.user.dto.response;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class MypageInfoResponseDTO {

    String username;
    String sido;
    String sigungu;
    String roadname;

    public MypageInfoResponseDTO(String username,String sido,String sigungu,String roadname){
        this.username = username;
        this.sido = sido;
        this.sigungu = sigungu;
        this.roadname = roadname;
    }
}
