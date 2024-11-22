package org.danpoong.zipcock_44.domain.favoriteoffer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.danpoong.zipcock_44.domain.favoriteoffer.dto.FavoriteOfferAddRequestDto;
import org.danpoong.zipcock_44.domain.favoriteoffer.entity.FavoriteOffer;
import org.danpoong.zipcock_44.domain.favoriteoffer.repository.FavoriteOfferRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteOfferService {
    private final FavoriteOfferRepository favoriteOfferRepository;

    @Transactional
    public void addFavoriteOffer(FavoriteOfferAddRequestDto req) {
        favoriteOfferRepository.save(req.toEntity());
        log.info("Added into Favorite List");
    }

    @Transactional
    public void deleteFavoriteOffer(Long id) {
        FavoriteOffer favoriteOffer = favoriteOfferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("정보에 해당하는 응답이 없습니다."));

        favoriteOfferRepository.delete(favoriteOffer);
        log.info("Deleted from Favorite List");
    }

    public List<FavoriteOffer> getFavoriteOffers() {
        return favoriteOfferRepository.findAllOrderByIdDesc();
    }

    public FavoriteOffer getFavoriteOfferById(Long id) {
        return favoriteOfferRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("정보에 해당하는 응답이 없습니다."));
    }

}
