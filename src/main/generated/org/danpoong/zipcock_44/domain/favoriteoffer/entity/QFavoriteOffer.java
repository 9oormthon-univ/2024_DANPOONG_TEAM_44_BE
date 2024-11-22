package org.danpoong.zipcock_44.domain.favoriteoffer.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QFavoriteOffer is a Querydsl query type for FavoriteOffer
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QFavoriteOffer extends EntityPathBase<FavoriteOffer> {

    private static final long serialVersionUID = 11968693L;

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

    public QFavoriteOffer(String variable) {
        super(FavoriteOffer.class, forVariable(variable));
    }

    public QFavoriteOffer(Path<? extends FavoriteOffer> path) {
        super(path.getType(), path.getMetadata());
    }

    public QFavoriteOffer(PathMetadata metadata) {
        super(FavoriteOffer.class, metadata);
    }

}

