package com.airbnb.lottie.model.content;

import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.StrokeContent;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableFloatValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.layer.BaseLayer;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public final class ShapeStroke implements ContentModel {
    public final LineCapType capType;
    public final AnimatableColorValue color;
    public final boolean hidden;
    public final LineJoinType joinType;
    public final List<AnimatableFloatValue> lineDashPattern;
    public final float miterLimit;
    public final String name;
    public final AnimatableFloatValue offset;
    public final AnimatableIntegerValue opacity;
    public final AnimatableFloatValue width;

    /* loaded from: classes.dex */
    public enum LineCapType {
        /* JADX INFO: Fake field, exist only in values array */
        BUTT,
        /* JADX INFO: Fake field, exist only in values array */
        ROUND,
        /* JADX INFO: Fake field, exist only in values array */
        UNKNOWN
    }

    /* loaded from: classes.dex */
    public enum LineJoinType {
        /* JADX INFO: Fake field, exist only in values array */
        MITER,
        /* JADX INFO: Fake field, exist only in values array */
        ROUND,
        /* JADX INFO: Fake field, exist only in values array */
        BEVEL
    }

    @Override // com.airbnb.lottie.model.content.ContentModel
    public final Content toContent(LottieDrawable lottieDrawable, BaseLayer baseLayer) {
        return new StrokeContent(lottieDrawable, baseLayer, this);
    }

    public ShapeStroke(String str, AnimatableFloatValue animatableFloatValue, ArrayList arrayList, AnimatableColorValue animatableColorValue, AnimatableIntegerValue animatableIntegerValue, AnimatableFloatValue animatableFloatValue2, LineCapType lineCapType, LineJoinType lineJoinType, float f, boolean z) {
        this.name = str;
        this.offset = animatableFloatValue;
        this.lineDashPattern = arrayList;
        this.color = animatableColorValue;
        this.opacity = animatableIntegerValue;
        this.width = animatableFloatValue2;
        this.capType = lineCapType;
        this.joinType = lineJoinType;
        this.miterLimit = f;
        this.hidden = z;
    }
}
