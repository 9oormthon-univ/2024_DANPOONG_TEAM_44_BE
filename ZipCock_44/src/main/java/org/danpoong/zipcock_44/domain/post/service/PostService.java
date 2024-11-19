package org.danpoong.zipcock_44.domain.post.service;

import org.danpoong.zipcock_44.domain.post.entity.Post;
import org.danpoong.zipcock_44.domain.post.repository.PostRepository;
import org.danpoong.zipcock_44.domain.user.User;
import org.danpoong.zipcock_44.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.danpoong.zipcock_44.domain.post.entity.Image;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public Post createPostWithJson(Long userId, String content, List<Image> images, String title, double latitude, double longitude, Image representativeImage) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = new Post(user, title, content, latitude, longitude);
        postRepository.save(post);

        post.addImage(representativeImage); // 대표 이미지 추가

        // 이미지와 게시글 간의 연관 관계 설정
        for (Image image : images) {
            post.addImage(image); // Post에 이미지 추가 및 연관 설정
        }


        return postRepository.save(post); // Post 및 연관된 Image 저장
    }


    public Page<Post> getPostsWithRepresentativeImage(Pageable pageable) {
        return postRepository.findAllWithRepresentativeImage(pageable);
    }
}
