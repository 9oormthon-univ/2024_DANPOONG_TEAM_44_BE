package org.danpoong.zipcock_44.domain.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatRoom is a Querydsl query type for ChatRoom
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatRoom extends EntityPathBase<ChatRoom> {

    private static final long serialVersionUID = -1197519462L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatRoom chatRoom = new QChatRoom("chatRoom");

    public final org.danpoong.zipcock_44.global.common.QBaseTimeEntity _super = new org.danpoong.zipcock_44.global.common.QBaseTimeEntity(this);

    public final org.danpoong.zipcock_44.domain.user.entity.QUser buyer;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final ListPath<ChatMessage, QChatMessage> messages = this.<ChatMessage, QChatMessage>createList("messages", ChatMessage.class, QChatMessage.class, PathInits.DIRECT2);

    public final org.danpoong.zipcock_44.domain.post.entity.QPost post;

    public final org.danpoong.zipcock_44.domain.user.entity.QUser seller;

    public QChatRoom(String variable) {
        this(ChatRoom.class, forVariable(variable), INITS);
    }

    public QChatRoom(Path<? extends ChatRoom> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatRoom(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatRoom(PathMetadata metadata, PathInits inits) {
        this(ChatRoom.class, metadata, inits);
    }

    public QChatRoom(Class<? extends ChatRoom> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.buyer = inits.isInitialized("buyer") ? new org.danpoong.zipcock_44.domain.user.entity.QUser(forProperty("buyer")) : null;
        this.post = inits.isInitialized("post") ? new org.danpoong.zipcock_44.domain.post.entity.QPost(forProperty("post"), inits.get("post")) : null;
        this.seller = inits.isInitialized("seller") ? new org.danpoong.zipcock_44.domain.user.entity.QUser(forProperty("seller")) : null;
    }

}

