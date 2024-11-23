package org.danpoong.zipcock_44.domain.favoriteoffer.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.danpoong.zipcock_44.domain.favoriteoffer.dto.FavoriteOfferAddRequestDto;
import org.danpoong.zipcock_44.domain.favoriteoffer.dto.FavoriteOfferResponseDto;
import org.danpoong.zipcock_44.domain.favoriteoffer.entity.FavoriteOffer;
import org.danpoong.zipcock_44.domain.favoriteoffer.repository.FavoriteOfferRepository;
import org.danpoong.zipcock_44.domain.user.entity.User;
import org.danpoong.zipcock_44.global.common.code.ErrorCode;
import org.danpoong.zipcock_44.global.common.response.CustomException;
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
                .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_VALID));

        if (favoriteOffer.getUser() != user)
            throw new CustomException(ErrorCode.AUTHORITY_FORBIDDEN);

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
                .orElseThrow(() -> new CustomException(ErrorCode.REQUEST_NOT_VALID));
    }

}
