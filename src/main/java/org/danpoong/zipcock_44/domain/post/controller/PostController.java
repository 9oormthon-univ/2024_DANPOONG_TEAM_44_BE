package org.danpoong.zipcock_44.domain.post.controller;

import org.danpoong.zipcock_44.domain.post.dto.request.PostRequestDTO;
import org.danpoong.zipcock_44.domain.post.dto.request.PostUpdateRequestDTO;
import org.danpoong.zipcock_44.domain.post.dto.response.PostIdSearchResponseDTO;
import org.danpoong.zipcock_44.domain.post.dto.response.PostSearchResponseDTO;
import org.danpoong.zipcock_44.domain.post.entity.Image;
import org.danpoong.zipcock_44.domain.post.entity.Post;
import org.danpoong.zipcock_44.domain.post.service.PostService;
import org.danpoong.zipcock_44.domain.user.entity.User;
import org.danpoong.zipcock_44.global.common.response.ApiResponse;
import org.danpoong.zipcock_44.global.security.entity.UserDetailsImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    //게시글 생성
    @PostMapping
    public ApiResponse<?> createPostWithJson(@RequestBody PostRequestDTO dto, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
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

        Post post = postService.createPostWithJson(user.getId(), dto.getContent(), images, dto.getTitle(), dto.getLatitude(), dto.getLongitude(), representativeImage, dto.getDomain());
        return ApiResponse.ok(null);
    }

    //게시글 조회
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
                    .authorName(post.getUser().getUsername()) // 작성자 이름
                    .createdDate(post.getCreatedDate()) // 작성 날짜
                    .domain(post.getDomain())
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
                .authorName(post.getUser().getUsername()) // 작성자 이름
                .authorId(post.getUser().getId())
                .createdDate(post.getCreatedDate()) // 작성일
                .representativeImageFileName(representativeImage.getFileName())
                .representativeImageFileData(Base64.getEncoder().encodeToString(representativeImage.getImageData())) // Base64 인코딩된 데이터
                .latitude(post.getLatitude())
                .longitude(post.getLongitude())
                .domain(post.getDomain())
                .imageFileNames(otherImages.stream()
                        .map(Image::getFileName) // 파일명 리스트
                        .toList())
                .imageFileData(otherImages.stream()
                        .map(image -> Base64.getEncoder().encodeToString(image.getImageData())) // Base64로 인코딩된 데이터 리스트
                        .toList())
                .build();

        return ApiResponse.ok(responseDTO);
    }

    //키워드를 통한 게시글 서치
    @GetMapping("/search")
    public ApiResponse<Page<PostSearchResponseDTO>> searchPostsWithRepresentativeImageByTitle(
            @RequestParam String keyword, Pageable pageable
    ) {
        Page<Post> posts = postService.findAllWithRepresentativeImageByTitle(keyword, pageable);

        Page<PostSearchResponseDTO> responseDTOs = posts.map(post -> {
            Image representativeImage = post.getImages().stream()
                    .filter(Image::isRepresentative)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Representative image not found for post id: " + post.getId()));

            return PostSearchResponseDTO.builder()
                    .title(post.getTitle())
                    .id(post.getId())
                    .content(post.getContent())
                    .domain(post.getDomain())
                    .authorName(post.getUser().getUsername()) // 작성자 이름
                    .createdDate(post.getCreatedDate()) // 작성 날짜
                    .image(PostSearchResponseDTO.ImageDTO.builder()
                            .fileName(representativeImage.getFileName()) // 파일 이름
                            .fileData(Base64.getEncoder().encodeToString(representativeImage.getImageData())) // Base64 인코딩된 데이터
                            .build())
                    .build();
        });

        return ApiResponse.ok(responseDTOs);
    }


    //게시글 수정
    @PutMapping
    public ApiResponse<PostIdSearchResponseDTO> updatePost(@RequestBody PostUpdateRequestDTO requestDTO, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        postService.updatePost(requestDTO, user.getId());
        return ApiResponse.ok(null);
    }

    //게시글 삭제
    @DeleteMapping("/{postId}")
    public ApiResponse<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        User user = userDetails.getUser();
        postService.deletePost(postId, user.getId());
        return ApiResponse.ok(null); // 성공 응답
    }


    //내가 작성한 글 서치
    @GetMapping("/my-posts")
    public ApiResponse<Page<PostSearchResponseDTO>> getMyPosts(@RequestParam String username,
                                                               @PageableDefault(size = 10, page = 0, sort = "createdDate", direction = Sort.Direction.DESC) Pageable pageable) {

        Page<Post> posts = postService.findPostsByUser(username, pageable);

        // DTO로 변환
        Page<PostSearchResponseDTO> responseDTOs = posts.map(post -> {
            Image representativeImage = post.getImages().stream()
                    .filter(Image::isRepresentative)
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Representative image not found for post id: " + post.getId()));

            return PostSearchResponseDTO.builder()
                    .title(post.getTitle())
                    .username(post.getUser().getUsername())
                    .content(post.getContent())
                    .authorName(post.getUser().getUsername())
                    .createdDate(post.getCreatedDate())
                    .domain(post.getDomain())
                    .image(PostSearchResponseDTO.ImageDTO.builder()
                            .fileName(representativeImage.getFileName())
                            .fileData(Base64.getEncoder().encodeToString(representativeImage.getImageData()))
                            .build())
                    .build();
        });

        return ApiResponse.ok(responseDTOs);
    }

}
