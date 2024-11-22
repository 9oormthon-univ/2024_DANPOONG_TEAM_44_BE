package org.danpoong.zipcock_44.domain.favoriteoffer.repository;

import org.danpoong.zipcock_44.domain.favoriteoffer.entity.FavoriteOffer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FavoriteOfferRepository extends JpaRepository<FavoriteOffer, Long> {
    void deleteById(Long id);

    @Query("SELECT fo FROM FavoriteOffer fo ORDER BY fo.id DESC")
    List<FavoriteOffer> findAllOrderByIdDesc();
}
