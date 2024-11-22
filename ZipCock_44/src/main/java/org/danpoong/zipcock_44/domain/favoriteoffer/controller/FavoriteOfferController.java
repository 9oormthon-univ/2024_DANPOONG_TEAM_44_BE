package org.danpoong.zipcock_44.domain.favoriteoffer.controller;

import lombok.RequiredArgsConstructor;
import org.danpoong.zipcock_44.domain.favoriteoffer.dto.FavoriteOfferAddRequestDto;
import org.danpoong.zipcock_44.domain.favoriteoffer.entity.FavoriteOffer;
import org.danpoong.zipcock_44.domain.favoriteoffer.service.FavoriteOfferService;
import org.danpoong.zipcock_44.global.common.response.ApiResponse;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/favorite-offer")
public class FavoriteOfferController {
    private final FavoriteOfferService favoriteOfferService;

    @PostMapping()
    public ApiResponse<String> addFavoriteOffer(@RequestBody FavoriteOfferAddRequestDto req) {
        favoriteOfferService.addFavoriteOffer(req);
        return ApiResponse.ok(null);
    }

    @DeleteMapping("/{id}")
    public ApiResponse<String> deleteFavoriteOffer(@PathVariable("id") Long id) {
        favoriteOfferService.deleteFavoriteOffer(id);
        return ApiResponse.ok(null);
    }

    @GetMapping()
    public ApiResponse<List<FavoriteOffer>> getFavoriteOffers() {
        return ApiResponse.ok(favoriteOfferService.getFavoriteOffers());
    }

    @GetMapping("/{id}")
    public ApiResponse<FavoriteOffer> getFavoriteOfferById(@PathVariable("id") Long id) {
        return ApiResponse.ok(favoriteOfferService.getFavoriteOfferById(id));
    }
}
