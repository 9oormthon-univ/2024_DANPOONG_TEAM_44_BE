package org.danpoong.zipcock_44.domain.favoriteoffer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QFavoriteOffer is a Querydsl query type for FavoriteOffer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFavoriteOffer extends EntityPathBase<FavoriteOffer> {

    private static final long serialVersionUID = 11968693L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QFavoriteOffer favoriteOffer = new QFavoriteOffer("favoriteOffer");

    public final StringPath alias = createString("alias");

    public final StringPath archYr = createString("archYr");

    public final StringPath bfrGrfe = createString("bfrGrfe");

    public final StringPath bfrRtfe = createString("bfrRtfe");

    public final StringPath bldgNm = createString("bldgNm");

    public final StringPath bldgUsg = createString("bldgUsg");

    public final StringPath cggNm = createString("cggNm");

    public final StringPath ctrtDay = createString("ctrtDay");

    public final StringPath ctrtPrd = createString("ctrtPrd");

    public final StringPath grfe = createString("grfe");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath mno = createString("mno");

    public final StringPath newUpdtYn = createString("newUpdtYn");

    public final NumberPath<Double> rentArea = createNumber("rentArea", Double.class);

    public final StringPath rentSe = createString("rentSe");

    public final StringPath rtfe = createString("rtfe");

    public final StringPath sno = createString("sno");

    public final StringPath stdgNm = createString("stdgNm");

    public final org.danpoong.zipcock_44.domain.user.entity.QUser user;

    public QFavoriteOffer(String variable) {
        this(FavoriteOffer.class, forVariable(variable), INITS);
    }

    public QFavoriteOffer(Path<? extends FavoriteOffer> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QFavoriteOffer(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QFavoriteOffer(PathMetadata metadata, PathInits inits) {
        this(FavoriteOffer.class, metadata, inits);
    }

    public QFavoriteOffer(Class<? extends FavoriteOffer> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.user = inits.isInitialized("user") ? new org.danpoong.zipcock_44.domain.user.entity.QUser(forProperty("user")) : null;
    }

}

