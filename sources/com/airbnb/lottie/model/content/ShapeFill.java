package com.airbnb.lottie.model.content;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Path;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.FillContent;
import com.airbnb.lottie.model.animatable.AnimatableColorValue;
import com.airbnb.lottie.model.animatable.AnimatableIntegerValue;
import com.airbnb.lottie.model.layer.BaseLayer;
/* loaded from: classes.dex */
public final class ShapeFill implements ContentModel {
    public final AnimatableColorValue color;
    public final boolean fillEnabled;
    public final Path.FillType fillType;
    public final boolean hidden;
    public final String name;
    public final AnimatableIntegerValue opacity;

    @Override // com.airbnb.lottie.model.content.ContentModel
    public final Content toContent(LottieDrawable lottieDrawable, BaseLayer baseLayer) {
        return new FillContent(lottieDrawable, baseLayer, this);
    }

    public final String toString() {
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(VendorAtomValue$$ExternalSyntheticOutline1.m("ShapeFill{color=, fillEnabled="), this.fillEnabled, '}');
    }

    public ShapeFill(String str, boolean z, Path.FillType fillType, AnimatableColorValue animatableColorValue, AnimatableIntegerValue animatableIntegerValue, boolean z2) {
        this.name = str;
        this.fillEnabled = z;
        this.fillType = fillType;
        this.color = animatableColorValue;
        this.opacity = animatableIntegerValue;
        this.hidden = z2;
    }
}
