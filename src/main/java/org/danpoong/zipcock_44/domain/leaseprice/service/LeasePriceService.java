package org.danpoong.zipcock_44.domain.leaseprice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.danpoong.zipcock_44.domain.leaseprice.domain.LegalDongCode;
import org.danpoong.zipcock_44.domain.leaseprice.dto.LeasePriceRequestDto;
import org.danpoong.zipcock_44.domain.leaseprice.dto.LeasePriceResponseDto;
import org.danpoong.zipcock_44.global.common.code.ErrorCode;
import org.danpoong.zipcock_44.global.common.response.CustomException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class LeasePriceService {
    private static final String API_URI = "http://openapi.seoul.go.kr:8088";
    private static final String BLANK = "%20";
    private static final String TYPE = "json";
    private static final String SERVICE_ID = "tbLnOpendataRentV";

    @Value("${api.lease-price.key}")
    private String API_KEY;

    public List<LeasePriceResponseDto> getLeasePriceData(LeasePriceRequestDto req) {
        String codeByAddress = LegalDongCode.getCodeByAddress(req.getAddress());
        String cggCd = codeByAddress.substring(0, 5); // 시군구코드
        String stdgCd = codeByAddress.substring(5, 10); // 법정동코드

        String mno = String.format("%04d", Integer.parseInt(req.getMno()));
        String sno = String.format("%04d", Integer.parseInt(req.getSno()));

        log.info("Sending Request to OpenAPI Server...");

        int pageNo = Integer.parseInt(req.getPageNo());
        int startIndex = (pageNo - 1) * 10 + 1;
        int endIndex = pageNo * 10;

        String url = UriComponentsBuilder.fromHttpUrl(API_URI)
                .pathSegment(API_KEY, TYPE, SERVICE_ID, String.valueOf(startIndex),
                        String.valueOf(endIndex), req.getRcptYr(), cggCd, BLANK,
                        stdgCd, BLANK, mno, sno, BLANK, BLANK, BLANK)
                .toUriString();

        RestTemplate restTemplate = new RestTemplate();
        String jsonResponse = restTemplate.getForObject(url, String.class);
        List<LeasePriceResponseDto> responseList = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode;
        try {
            rootNode = objectMapper.readTree(jsonResponse);
        } catch (JsonProcessingException e) {
            throw new CustomException(ErrorCode.ERROR_WHILE_PARSING_JSON);
        }

        if (!rootNode.has("tbLnOpendataRentV") || rootNode.path("tbLnOpendataRentV").isEmpty())
            throw new CustomException(ErrorCode.REQUEST_NOT_VALID);

        String resultCode = rootNode.path("tbLnOpendataRentV").path("RESULT").path("CODE").asText();
        if (resultCode.equals("INFO-000")) {
            JsonNode rows = rootNode.path("tbLnOpendataRentV").path("row");
            for (JsonNode row : rows) {
                LeasePriceResponseDto dto = LeasePriceResponseDto.builder()
                        .cggNm(row.path("CGG_NM").asText())
                        .stdgNm(row.path("STDG_NM").asText())
                        .mno(row.path("MNO").asText())
                        .sno(row.path("SNO").asText())
                        .ctrtDay(row.path("CTRT_DAY").asText())
                        .rentSe(row.path("RENT_SE").asText())
                        .rentArea(row.path("RENT_AREA").asDouble())
                        .grfe(row.path("GRFE").asText())
                        .rtfe(row.path("RTFE").asText())
                        .bldgNm(row.path("BLDG_NM").asText())
                        .bldgUsg(row.path("BLDG_USG").asText())
                        .archYr(row.path("ARCH_YR").asText())
                        .ctrtPrd(row.path("CTRT_PRD").asText())
                        .newUpdtYn(row.path("NEW_UPDT_YN").asText())
                        .bfrGrfe(row.path("BFR_GRFE").asText())
                        .bfrRtfe(row.path("BFR_RTFE").asText())
                        .build();
                responseList.add(dto);
            }
        } else {
            throw new CustomException(ErrorCode.REQUEST_NOT_VALID);
        }
        log.info("Response Complete.");
        return responseList;
    }
}
