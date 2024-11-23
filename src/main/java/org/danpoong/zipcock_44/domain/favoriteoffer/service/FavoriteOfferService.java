package org.danpoong.zipcock_44.domain.favoriteoffer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.danpoong.zipcock_44.domain.favoriteoffer.dto.FavoriteOfferAddRequestDto;
import org.danpoong.zipcock_44.domain.favoriteoffer.dto.FavoriteOfferResponseDto;
import org.danpoong.zipcock_44.domain.favoriteoffer.entity.FavoriteOffer;
import org.danpoong.zipcock_44.domain.favoriteoffer.repository.FavoriteOfferRepository;
import org.danpoong.zipcock_44.domain.user.entity.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteOfferService {
    private final FavoriteOfferRepository favoriteOfferRepository;

    @Transactional
    public void addFavoriteOffer(FavoriteOfferAddRequestDto req, User user) {
        favoriteOfferRepository.save(req.toEntity(user));
        log.info("Added into Favorite List");
    }

    @Transactional
    public void deleteFavoriteOffer(Long id, User user) {
        FavoriteOffer favoriteOffer = favoriteOfferRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("정보에 해당하는 응답이 없습니다."));

        if (favoriteOffer.getUser() != user)
            new IllegalArgumentException("사용자가 등록하지 않은 관심 매물은 제거할 수 없습니다.");

        favoriteOfferRepository.delete(favoriteOffer);
        log.info("Deleted from Favorite List");
    }

    public List<FavoriteOfferResponseDto> getFavoriteOffers(User user) {
        return favoriteOfferRepository.findAllOrderByIdDesc(user).stream()
                .map(FavoriteOffer::toResponseDto).toList();
    }

    public FavoriteOfferResponseDto getFavoriteOfferById(Long id) {
        return favoriteOfferRepository.findById(id).stream()
                .findFirst().map(FavoriteOffer::toResponseDto)
                .orElseThrow(() -> new IllegalArgumentException("정보에 해당하는 응답이 없습니다."));
    }

}
