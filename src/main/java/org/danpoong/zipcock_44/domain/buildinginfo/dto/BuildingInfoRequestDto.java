package org.danpoong.zipcock_44.domain.buildinginfo.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BuildingInfoRequestDto {
    // 전체 주소 (시/구/동)
    private String address;

    // 번
    private String bun;

    // 지
    private String ji;

    // 페이지 번호
    private String pageNo;
}
