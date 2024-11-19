package org.danpoong.zipcock_44.domain.post.controller;

import org.danpoong.zipcock_44.domain.post.dto.request.PostRequestDTO;
import org.danpoong.zipcock_44.domain.post.dto.response.PostIdSearchResponseDTO;
import org.danpoong.zipcock_44.domain.post.dto.response.PostSearchResponseDTO;
import org.danpoong.zipcock_44.domain.post.entity.Image;
import org.danpoong.zipcock_44.domain.post.entity.Post;
import org.danpoong.zipcock_44.domain.post.service.PostService;
import org.danpoong.zipcock_44.global.common.response.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.Base64;
import java.util.List;

@RestController
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ApiResponse<?> createPostWithJson(@RequestBody PostRequestDTO dto) {
        List<Image> images = dto.getFileData().stream()
                .map(fileData -> Image.builder()
                        .imageData(Base64.getDecoder().decode(fileData.getFileContent()))
                        .fileName(fileData.getFileName())
                        .isRepresentative(false)
                        .build())
                .toList();

        Image representativeImage = Image.builder().imageData(Base64.getDecoder().decode(dto.getRepresentativeFileData().getFileContent()))
                .fileName(dto.getRepresentativeFileData().getFileName())
                .isRepresentative(true)
                .build();

        Post post = postService.createPostWithJson(dto.getUserId(), dto.getContent(), images, dto.getTitle(), dto.getLatitude(), dto.getLongitude(), representativeImage);
        return ApiResponse.ok(null);
    }

    @GetMapping
    public ApiResponse<Page<PostSearchResponseDTO>> getPostsWithRepresentativeImage(Pageable pageable) {
        Page<Post> posts = postService.getPostsWithRepresentativeImage(pageable);
        Page<PostSearchResponseDTO> responseDTOs = posts.map(post -> {
            Image representativeImage = post.getImages().stream()
                    .filter(Image::isRepresentative)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Representative image not found for post id: " + post.getId()));

            return PostSearchResponseDTO.builder()
                    .title(post.getTitle())
                    .id(post.getId())
                    .content(post.getContent())
                    .authorName(post.getUser().getName()) // 작성자 이름
                    .createdDate(post.getCreatedDate()) // 작성 날짜
                    .image(PostSearchResponseDTO.ImageDTO.builder()
                            .fileName(representativeImage.getFileName()) // 파일 이름
                            .fileData(Base64.getEncoder().encodeToString(representativeImage.getImageData())) // Base64 인코딩된 데이터
                            .build())
                    .build();
        });
        return ApiResponse.ok(responseDTOs);
    }

    // 특정 게시글 조회
    @GetMapping("/{postId}")
    public ApiResponse<PostIdSearchResponseDTO> getPostById(@PathVariable Long postId) {
        Post post = postService.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 대표 이미지 추출
        Image representativeImage = post.getImages().stream()
                .filter(Image::isRepresentative)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Representative image not found for post id: " + post.getId()));

        // 일반 이미지 데이터 추출
        List<Image> otherImages = post.getImages().stream()
                .filter(image -> !image.isRepresentative())
                .toList();

        // DTO 생성
        PostIdSearchResponseDTO responseDTO = PostIdSearchResponseDTO.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .authorName(post.getUser().getName()) // 작성자 이름
                .createdDate(post.getCreatedDate()) // 작성일
                .representativeImageFileName(representativeImage.getFileName())
                .representativeImageFileData(Base64.getEncoder().encodeToString(representativeImage.getImageData())) // Base64 인코딩된 데이터
                .imageFileNames(otherImages.stream()
                        .map(Image::getFileName) // 파일명 리스트
                        .toList())
                .imageFileData(otherImages.stream()
                        .map(image -> Base64.getEncoder().encodeToString(image.getImageData())) // Base64로 인코딩된 데이터 리스트
                        .toList())
                .build();

        return ApiResponse.ok(responseDTO);
    }
}
