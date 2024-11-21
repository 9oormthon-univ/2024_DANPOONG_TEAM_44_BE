package org.danpoong.zipcock_44.domain.leaseprice.controller;

import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.domain.leaseprice.dto.LeasePriceRequestDto;
import org.danpoong.zipcock_44.domain.leaseprice.dto.LeasePriceResponseDto;
import org.danpoong.zipcock_44.domain.leaseprice.service.LeasePriceService;
import org.danpoong.zipcock_44.global.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class LeasePriceController {
    private final LeasePriceService leasePriceService;

    @GetMapping("/lease")
    public ApiResponse<List<LeasePriceResponseDto>> getLeasePrice(@ModelAttribute LeasePriceRequestDto req) {
        return ApiResponse.ok(leasePriceService.getLeasePriceData(req));
    }
}
