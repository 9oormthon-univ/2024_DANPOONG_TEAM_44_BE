package org.danpoong.zipcock_44.domain.leaseprice.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LeasePriceResponseDto {
    @Schema(description = "자치구명")
    private String cggNm;

    @Schema(description = "법정동명")
    private String stdgNm;

    @Schema(description = "본번")
    private String mno;

    @Schema(description = "부번")
    private String sno;

    @Schema(description = "계약일")
    private String ctrtDay;

    @Schema(description = "전월세 구분")
    private String rentSe;

    @Schema(description = "임대 면적")
    private double rentArea;

    @Schema(description = "보증금 (만원 단위)")
    private String grfe;

    @Schema(description = "임대료 (만원 단위)")
    private String rtfe;

    @Schema(description = "건물명")
    private String bldgNm;

    @Schema(description = "건물용도")
    private String bldgUsg;

    @Schema(description = "건축연도")
    private String archYr;

    @Schema(description = "계약 기간")
    private String ctrtPrd;

    @Schema(description = "신규 갱신 여부")
    private String newUpdtYn;

    @Schema(description = "종전 보증금 (만원 단위)")
    private String bfrGrfe;

    @Schema(description = "종전 임대료 (만원 단위)")
    private String bfrRtfe;
}
