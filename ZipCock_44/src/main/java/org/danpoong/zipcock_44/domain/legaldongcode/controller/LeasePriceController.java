package org.danpoong.zipcock_44.domain.legaldongcode.controller;

import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.domain.legaldongcode.dto.LeasePriceRequestDto;
import org.danpoong.zipcock_44.domain.legaldongcode.dto.LeasePriceResponseDto;
import org.danpoong.zipcock_44.domain.legaldongcode.service.LeasePriceService;
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
