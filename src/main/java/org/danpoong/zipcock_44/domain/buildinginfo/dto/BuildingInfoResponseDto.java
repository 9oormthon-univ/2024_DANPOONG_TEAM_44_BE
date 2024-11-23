package org.danpoong.zipcock_44.domain.buildinginfo.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingInfoResponseDto {
    @Schema(description = "전유부 생성일자")
    private String crtnDay;

    @Schema(description = "건축면적 (m^2)")
    private int archArea;

    @Schema(description = "주 용도 코드명")
    private String mainPurpsCdNm;

    @Schema(description = "기타 용도")
    private String etcPurps;

    @Schema(description = "세대수")
    private int hhldCnt;

    @Schema(description = "가구 수")
    private int fmlyCnt;

    @Schema(description = "대지 위치")
    private String platPlc;

    @Schema(description = "관리 건축물 대장 PK")
    private String mgmBldrgstPk;

    @Schema(description = "도로명 대지위")
    private String newPlatPlc;

    @Schema(description = "건물명")
    private String bldNm;
}
