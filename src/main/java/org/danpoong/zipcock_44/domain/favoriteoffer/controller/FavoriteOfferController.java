package org.danpoong.zipcock_44.domain.favoriteoffer.controller;

import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.domain.favoriteoffer.dto.FavoriteOfferAddRequestDto;
import org.danpoong.zipcock_44.domain.favoriteoffer.dto.FavoriteOfferResponseDto;
import org.danpoong.zipcock_44.domain.favoriteoffer.service.FavoriteOfferService;
import org.danpoong.zipcock_44.global.common.response.ApiResponse;
import org.danpoong.zipcock_44.global.security.entity.UserDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite-offer")
public class FavoriteOfferController {
    private final FavoriteOfferService favoriteOfferService;

    @PostMapping()
    public ApiResponse<String> addFavoriteOffer(@RequestBody FavoriteOfferAddRequestDto req, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        favoriteOfferService.addFavoriteOffer(req, userDetails.getUser());
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteFavoriteOffer(@PathVariable("id") Long id, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        favoriteOfferService.deleteFavoriteOffer(id, userDetails.getUser());
        return ApiResponse.ok(null);
    }

    @GetMapping()
    public ApiResponse<List<FavoriteOfferResponseDto>> getFavoriteOffers(@AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(favoriteOfferService.getFavoriteOffers(userDetails.getUser()));
    }

    @GetMapping("/{id}")
    public ApiResponse<FavoriteOfferResponseDto> getFavoriteOfferById(@PathVariable("id") Long id) {
        return ApiResponse.ok(favoriteOfferService.getFavoriteOfferById(id));
    }
}
