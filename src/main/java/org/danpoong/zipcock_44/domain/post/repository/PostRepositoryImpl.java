package org.danpoong.zipcock_44.domain.post.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.danpoong.zipcock_44.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static org.danpoong.zipcock_44.domain.post.entity.QPost.*;
import static org.danpoong.zipcock_44.domain.post.entity.QImage.image;

@Repository
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }


    @Override
    public Page<Post> findAllWithRepresentativeImage(Pageable pageable) {
        // Fetch data with pagination
        List<Post> posts = queryFactory
                .selectFrom(post)
                .leftJoin(post.images, image).fetchJoin()
                .where(image.isRepresentative.eq(true)) // 조건: 대표 이미지
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdDate.desc()) // 정렬
                .fetch();

        // Total count
        long total = queryFactory
                .selectFrom(post)
                .leftJoin(post.images, image)
                .where(image.isRepresentative.eq(true))
                .fetchCount();

        return new PageImpl<>(posts, pageable, total);
    }


    @Override
    public Page<Post> findAllWithRepresentativeImageByTitle(String keyword, Pageable pageable) {
        // Fetch 검색 결과
        List<Post> results = queryFactory
                .selectFrom(post)
                .leftJoin(post.images, image).fetchJoin() // 이미지와 조인
                .where(post.title.containsIgnoreCase(keyword)
                        .and(image.isRepresentative.eq(true))) // 제목 조건 + 대표 이미지 조건
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .orderBy(post.createdDate.desc()) // 정렬
                .fetch();

        // Total Count
        long total = queryFactory
                .select(post.count())
                .from(post)
                .leftJoin(post.images, image)
                .where(post.title.containsIgnoreCase(keyword)
                        .and(image.isRepresentative.eq(true))) // 제목 조건 + 대표 이미지 조건
                .fetchOne();

        return new PageImpl<>(results, pageable, total);
    }

}

