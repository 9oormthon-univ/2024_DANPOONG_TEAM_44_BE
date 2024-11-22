package org.danpoong.zipcock_44.domain.post.repository;

import org.danpoong.zipcock_44.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {

    Page<Post> findAllWithRepresentativeImage(Pageable pageable);   //대표사진만 포함해서 page반환

    Page<Post> findAllWithRepresentativeImageByTitle(String keyword, Pageable pageable);   //Title
}
