package org.danpoong.zipcock_44.domain.chat.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QChatMessageRead is a Querydsl query type for ChatMessageRead
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QChatMessageRead extends EntityPathBase<ChatMessageRead> {

    private static final long serialVersionUID = 1814929566L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QChatMessageRead chatMessageRead = new QChatMessageRead("chatMessageRead");

    public final org.danpoong.zipcock_44.global.common.QBaseTimeEntity _super = new org.danpoong.zipcock_44.global.common.QBaseTimeEntity(this);

    public final QChatMessage chatMessage;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final BooleanPath isRead = createBoolean("isRead");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> lastModifiedDate = _super.lastModifiedDate;

    public final org.danpoong.zipcock_44.domain.user.QUser user;

    public QChatMessageRead(String variable) {
        this(ChatMessageRead.class, forVariable(variable), INITS);
    }

    public QChatMessageRead(Path<? extends ChatMessageRead> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QChatMessageRead(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QChatMessageRead(PathMetadata metadata, PathInits inits) {
        this(ChatMessageRead.class, metadata, inits);
    }

    public QChatMessageRead(Class<? extends ChatMessageRead> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.chatMessage = inits.isInitialized("chatMessage") ? new QChatMessage(forProperty("chatMessage"), inits.get("chatMessage")) : null;
        this.user = inits.isInitialized("user") ? new org.danpoong.zipcock_44.domain.user.QUser(forProperty("user")) : null;
    }

}

