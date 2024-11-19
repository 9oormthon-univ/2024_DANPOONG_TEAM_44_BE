package org.danpoong.zipcock_44.domain.post.repository;


import org.danpoong.zipcock_44.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> , PostRepositoryCustom{

    Optional<Post> findById(Long id);
    Page<Post> findAllByUserId(Long userId, Pageable pageable);
}
