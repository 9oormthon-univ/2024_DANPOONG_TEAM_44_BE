package org.danpoong.zipcock_44.domain.post.controller;

import org.danpoong.zipcock_44.domain.post.dto.request.PostRequestDTO;
import org.danpoong.zipcock_44.domain.post.entity.Image;
import org.danpoong.zipcock_44.domain.post.entity.Post;
import org.danpoong.zipcock_44.domain.post.service.PostService;
import org.danpoong.zipcock_44.global.common.response.ApiResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
