package org.danpoong.zipcock_44.domain.user.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 128720005L;

    public static final QUser user = new QUser("user");

    public final org.danpoong.zipcock_44.global.common.QBaseTimeEntity _super = new org.danpoong.zipcock_44.global.common.QBaseTimeEntity(this);

    public final ListPath<org.danpoong.zipcock_44.domain.chat.entity.ChatRoom, org.danpoong.zipcock_44.domain.chat.entity.QChatRoom> buyerChatRooms = this.<org.danpoong.zipcock_44.domain.chat.entity.ChatRoom, org.danpoong.zipcock_44.domain.chat.entity.QChatRoom>createList("buyerChatRooms", org.danpoong.zipcock_44.domain.chat.entity.ChatRoom.class, org.danpoong.zipcock_44.domain.chat.entity.QChatRoom.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final StringPath loginId = createString("loginId");

    public final StringPath password = createString("password");

    public final ListPath<org.danpoong.zipcock_44.domain.post.entity.Post, org.danpoong.zipcock_44.domain.post.entity.QPost> posts = this.<org.danpoong.zipcock_44.domain.post.entity.Post, org.danpoong.zipcock_44.domain.post.entity.QPost>createList("posts", org.danpoong.zipcock_44.domain.post.entity.Post.class, org.danpoong.zipcock_44.domain.post.entity.QPost.class, PathInits.DIRECT2);

    public final StringPath roadname = createString("roadname");

    public final StringPath role = createString("role");

    public final ListPath<org.danpoong.zipcock_44.domain.chat.entity.ChatRoom, org.danpoong.zipcock_44.domain.chat.entity.QChatRoom> sellerChatRooms = this.<org.danpoong.zipcock_44.domain.chat.entity.ChatRoom, org.danpoong.zipcock_44.domain.chat.entity.QChatRoom>createList("sellerChatRooms", org.danpoong.zipcock_44.domain.chat.entity.ChatRoom.class, org.danpoong.zipcock_44.domain.chat.entity.QChatRoom.class, PathInits.DIRECT2);

    public final StringPath sido = createString("sido");

    public final StringPath sigungu = createString("sigungu");

    public final StringPath username = createString("username");

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

