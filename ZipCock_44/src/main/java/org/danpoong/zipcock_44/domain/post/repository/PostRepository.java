package org.danpoong.zipcock_44.domain.post.repository;


import org.danpoong.zipcock_44.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {

}
