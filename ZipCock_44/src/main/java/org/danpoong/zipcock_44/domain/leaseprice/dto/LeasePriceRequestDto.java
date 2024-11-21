package org.danpoong.zipcock_44.domain.leaseprice.dto;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LeasePriceRequestDto {
    // 요청시작위치 (정수 입력, 데이터 행 시작번호)
    private String startIndex;

    // 요청종료위치 (정수 입력, 데이터 행 끝번호)
    private String endIndex;

    // 접수연도 (YYYY)
    private String rcptYr;

    // 자치구명 (문자열)
//    private String cggNm;

    // 전체 주소 (시/구/동)
    private String address;

    // 본번 (4자리 정수)
    private String mno;

    // 부번 (4자리 정수)
    private String sno;

    // 계약일 (YYYYMMDD)
//    private String ctrtDay;

    // 건물명 (문자열)
//    private String bldgNm;

    // 건물용도 (아파트/단독다가구/연립다세대/오피스텔 택1)
//    private String bldgUsg;
}
