package com.airbnb.lottie.animation.content;

import android.graphics.Path;
import androidx.appcompat.app.LayoutIncludeDetector;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation;
import com.airbnb.lottie.animation.keyframe.ShapeKeyframeAnimation;
import com.airbnb.lottie.model.content.ShapeData;
import com.airbnb.lottie.model.content.ShapePath;
import com.airbnb.lottie.model.content.ShapeTrimPath;
import com.airbnb.lottie.model.layer.BaseLayer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ShapeContent implements PathContent, BaseKeyframeAnimation.AnimationListener {
    public final boolean hidden;
    public boolean isPathValid;
    public final LottieDrawable lottieDrawable;
    public final ShapeKeyframeAnimation shapeAnimation;
    public final Path path = new Path();
    public LayoutIncludeDetector trimPaths = new LayoutIncludeDetector();

    @Override // com.airbnb.lottie.animation.keyframe.BaseKeyframeAnimation.AnimationListener
    public final void onValueChanged() {
        this.isPathValid = false;
        this.lottieDrawable.invalidateSelf();
    }

    @Override // com.airbnb.lottie.animation.content.Content
    public final void setContents(List<Content> list, List<Content> list2) {
        int i = 0;
        while (true) {
            ArrayList arrayList = (ArrayList) list;
            if (i < arrayList.size()) {
                Content content = (Content) arrayList.get(i);
                if (content instanceof TrimPathContent) {
                    TrimPathContent trimPathContent = (TrimPathContent) content;
                    Objects.requireNonNull(trimPathContent);
                    if (trimPathContent.type == ShapeTrimPath.Type.SIMULTANEOUSLY) {
                        LayoutIncludeDetector layoutIncludeDetector = this.trimPaths;
                        Objects.requireNonNull(layoutIncludeDetector);
                        ((List) layoutIncludeDetector.mXmlParserStack).add(trimPathContent);
                        trimPathContent.addListener(this);
                    }
                }
                i++;
            } else {
                return;
            }
        }
    }

    @Override // com.airbnb.lottie.animation.content.PathContent
    public final Path getPath() {
        if (this.isPathValid) {
            return this.path;
        }
        this.path.reset();
        if (this.hidden) {
            this.isPathValid = true;
            return this.path;
        }
        this.path.set(this.shapeAnimation.getValue());
        this.path.setFillType(Path.FillType.EVEN_ODD);
        this.trimPaths.apply(this.path);
        this.isPathValid = true;
        return this.path;
    }

    public ShapeContent(LottieDrawable lottieDrawable, BaseLayer baseLayer, ShapePath shapePath) {
        Objects.requireNonNull(shapePath);
        this.hidden = shapePath.hidden;
        this.lottieDrawable = lottieDrawable;
        BaseKeyframeAnimation<ShapeData, Path> createAnimation = shapePath.shapePath.createAnimation();
        this.shapeAnimation = (ShapeKeyframeAnimation) createAnimation;
        baseLayer.addAnimation(createAnimation);
        createAnimation.addUpdateListener(this);
    }
}
