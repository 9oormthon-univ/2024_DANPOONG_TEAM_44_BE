package org.danpoong.zipcock_44.domain.buildinginfo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BuildingInfoResponseDto {
    // 전유부 생성일자
    private String crtnDay;

    // 건축면적
    private int archArea; // m^2

    // 주 용도 코드명
    private String mainPurpsCdNm;

    // 기타 용도
    private String etcPurps;

    // 세대수
    private int hhldCnt;

    // 가구 수
    private int fmlyCnt;

    // 대지 위치
    private String platPlc;

    // 관리 건축물 대장 PK
    private String mgmBldrgstPk;

    // 도로명 대지위
    private String newPlatPlc;

    // 건물명
    private String bldNm;
}
