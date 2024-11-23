package org.danpoong.zipcock_44.domain.post.service;

import org.danpoong.zipcock_44.domain.post.dto.request.PostUpdateRequestDTO;
import org.danpoong.zipcock_44.domain.post.entity.Post;
import org.danpoong.zipcock_44.domain.post.repository.PostRepository;
import org.danpoong.zipcock_44.domain.user.entity.User;
import org.danpoong.zipcock_44.domain.user.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.danpoong.zipcock_44.domain.post.entity.Image;

import java.util.Base64;
import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }


    @Transactional
    public Post createPostWithJson(Long userId, String content, List<Image> images, String title, double latitude, double longitude, Image representativeImage, String domain) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Post post = new Post(user, title, content, latitude, longitude, domain);
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

    public Optional<Post> findPostById(Long postId) {
        return postRepository.findById(postId);
    }

    // 제목 검색 및 대표 이미지 포함
    public Page<Post> findAllWithRepresentativeImageByTitle(String keyword, Pageable pageable) {
        return postRepository.findAllWithRepresentativeImageByTitle(keyword, pageable);
    }

    @Transactional
    public Post updatePost(PostUpdateRequestDTO requestDTO, long userId) {
        // 게시글 조회
        Post post = postRepository.findById(requestDTO.getPostId())
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + requestDTO.getPostId()));

        // 작성자 검증
        if (!(post.getUser().getId() == userId)) {
            throw new SecurityException("You do not have permission to modify this post.");
        }

        // 제목 및 내용 수정
        if (requestDTO.getTitle() != null) {
            post.setTitle(requestDTO.getTitle());
        }
        if (requestDTO.getContent() != null) {
            post.setContent(requestDTO.getContent());
        }
        if (requestDTO.getDomain() != null) {
            post.setDomain(requestDTO.getDomain());
        }

        // 기존 이미지 삭제 후 새로운 이미지 설정
        post.getImages().clear();

        // 새로운 대표 이미지 추가
        if (requestDTO.getRepresentativeFileData() != null) {
            Image representativeImage = Image.builder()
                    .fileName(requestDTO.getRepresentativeFileData().getFileName())
                    .imageData(Base64.getDecoder().decode(requestDTO.getRepresentativeFileData().getFileContent()))
                    .isRepresentative(true)
                    .build();
            post.addImage(representativeImage);
        }

        // 새로운 기본 이미지 추가
        if (requestDTO.getFileData() != null && !requestDTO.getFileData().isEmpty()) {
            List<Image> newImages = requestDTO.getFileData().stream()
                    .map(fileData -> Image.builder()
                            .fileName(fileData.getFileName())
                            .imageData(Base64.getDecoder().decode(fileData.getFileContent()))
                            .isRepresentative(false)
                            .build())
                    .toList();
            newImages.forEach(post::addImage);
        }

        return post;
    }

    @Transactional
    public void deletePost(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + postId));

        // 작성자 검증
        if (!(post.getUser().getId() == userId)) {
            throw new SecurityException("You do not have permission to delete this post.");
        }

        // 게시글 삭제
        postRepository.delete(post);
    }


    @Transactional(readOnly = true)
    public Page<Post> findPostsByUser(String username, Pageable pageable) {
        return postRepository.findAllByUserUsername(username, pageable);
    }

}
