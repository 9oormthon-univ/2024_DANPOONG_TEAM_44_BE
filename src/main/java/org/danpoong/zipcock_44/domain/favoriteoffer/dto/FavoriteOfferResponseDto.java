package org.danpoong.zipcock_44.domain.favoriteoffer.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FavoriteOfferResponseDto {
    private Long id;

    // 별칭
    private String alias;

    // 등록한 사용자의 id
    private Long userId;

    // 자치구명
    private String cggNm;

    // 법정동명
    private String stdgNm;

    // 본번
    private String mno;

    // 부번
    private String sno;

    // 계약일
    private String ctrtDay;

    // 전월세 구분
    private String rentSe;

    // 임대 면적
    private double rentArea;

    // 보증금 (만원 단위)
    private String grfe;

    // 임대료 (만원 단위)
    private String rtfe;

    // 건물명
    private String bldgNm;

    // 건물용도
    private String bldgUsg;

    // 건축연도
    private String archYr;

    // 계약 기간
    private String ctrtPrd;

    // 신규 갱신 여부
    private String newUpdtYn;

    // 종전 보증금 (만원 단위)
    private String bfrGrfe;

    // 종전 임대료 (만원 단위)
    private String bfrRtfe;

}
