package org.danpoong.zipcock_44.domain.post.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "IMAGE")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Lob
    @Column(name = "image_data")
    private byte[] imageData;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Column(name = "is_representative", nullable = false)
    private boolean isRepresentative; // 대표 사진 여부

    public void setPost(Post post) {
        this.post = post;
    }

    @Builder
    public Image(byte[] imageData, String fileName, boolean isRepresentative) {
        this.imageData = imageData;
        this.fileName = fileName;
        this.isRepresentative = isRepresentative;
    }
}
