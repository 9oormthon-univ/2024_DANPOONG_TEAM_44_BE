package org.danpoong.zipcock_44.domain.buildinginfo.controller;

import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.domain.buildinginfo.dto.BuildingInfoRequestDto;
import org.danpoong.zipcock_44.domain.buildinginfo.dto.BuildingInfoResponseDto;
import org.danpoong.zipcock_44.domain.buildinginfo.service.BuildingRegisterInfoService;
import org.danpoong.zipcock_44.global.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class BuildingRegisterInfoController {
    private final BuildingRegisterInfoService buildingRegisterInfoService;

    @GetMapping("/building-info")
    public ApiResponse<List<BuildingInfoResponseDto>> getBuildingInfo(@ModelAttribute BuildingInfoRequestDto req) {
        return ApiResponse.ok(buildingRegisterInfoService.getBuildingInfoData(req));
    }
}
