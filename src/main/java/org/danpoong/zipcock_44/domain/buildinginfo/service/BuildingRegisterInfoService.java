package org.danpoong.zipcock_44.domain.buildinginfo.service;

import lombok.extern.slf4j.Slf4j;
import org.danpoong.zipcock_44.domain.buildinginfo.dto.BuildingInfoRequestDto;
import org.danpoong.zipcock_44.domain.buildinginfo.dto.BuildingInfoResponseDto;
import org.danpoong.zipcock_44.domain.legaldongcode.LegalDongCode;
import org.danpoong.zipcock_44.global.common.code.ErrorCode;
import org.danpoong.zipcock_44.global.common.response.CustomException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class BuildingRegisterInfoService {
    private static final String API_URI = "https://apis.data.go.kr/1613000/BldRgstHubService/getBrTitleInfo";
    private static final String REQUEST_TYPE = "json";
    private static final int DEFAULT_PAGE_SIZE = 10;

    @Value("${api.building-register-info.key}")
    private String API_KEY;

    public List<BuildingInfoResponseDto> getBuildingInfoData(BuildingInfoRequestDto req) {
        String codeByAddress = LegalDongCode.getCodeByAddress(req.getAddress());
        String cggCd = codeByAddress.substring(0, 5); // 시군구코드
        String stdgCd = codeByAddress.substring(5, 10); // 법정동코드

        String bun = String.format("%04d", Integer.parseInt(req.getBun()));
        String ji = String.format("%04d", Integer.parseInt(req.getJi()));

        log.info("Sending Request to OpenAPI Server...");

        String uriString = API_URI + "?serviceKey=" + API_KEY +
                "&sigunguCd=" + cggCd +
                "&bjdongCd=" + stdgCd +
                "&bun=" + bun +
                "&ji=" + ji +
                "&_type=" + REQUEST_TYPE +
                "&numOfRows=" + DEFAULT_PAGE_SIZE +
                "&pageNo=" + req.getPageNo();

        URI uri = URI.create(uriString);

        WebClient webClient = WebClient.builder()
                .baseUrl(API_URI)
                .defaultHeader("Content-Type", "application/json")
                .build();

        String response = webClient.get()
                .uri(uri)
                .retrieve()
                .bodyToMono(String.class)
                .block();

        JSONObject jsonObject = new JSONObject(response);

        JSONObject jsonBody = jsonObject.getJSONObject("response").getJSONObject("body");

        if (jsonBody.optInt("totalCount") == 0)
            throw new CustomException(ErrorCode.REQUEST_NOT_VALID);

        JSONArray responseArray = jsonBody.getJSONObject("items").getJSONArray("item");
        if (responseArray.isEmpty())
            throw new CustomException(ErrorCode.EMPTY_PAGE);

        List<BuildingInfoResponseDto> responseList = new ArrayList<>();
        responseArray.forEach(item -> {
            JSONObject jsonItem = (JSONObject) item;
            BuildingInfoResponseDto dto = BuildingInfoResponseDto.builder()
                    .crtnDay(jsonItem.optString("crtnDay"))
                    .archArea(jsonItem.optInt("archArea"))
                    .mainPurpsCdNm(jsonItem.optString("mainPurpsCdNm"))
                    .etcPurps(jsonItem.optString("etcPurps"))
                    .hhldCnt(jsonItem.optInt("hhldCnt"))
                    .fmlyCnt(jsonItem.optInt("fmlyCnt"))
                    .platPlc(jsonItem.optString("platPlc"))
                    .mgmBldrgstPk(jsonItem.optString("mgmBldrgstPk"))
                    .newPlatPlc(jsonItem.optString("newPlatPlc"))
                    .bldNm(jsonItem.optString("bldNm"))
                    .build();
            responseList.add(dto);
        });

        return responseList;
    }

}
