package com.airbnb.lottie.model.content;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.ContentGroup;
import com.airbnb.lottie.model.layer.BaseLayer;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public final class ShapeGroup implements ContentModel {
    public final boolean hidden;
    public final List<ContentModel> items;
    public final String name;

    @Override // com.airbnb.lottie.model.content.ContentModel
    public final Content toContent(LottieDrawable lottieDrawable, BaseLayer baseLayer) {
        return new ContentGroup(lottieDrawable, baseLayer, this);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ShapeGroup{name='");
        m.append(this.name);
        m.append("' Shapes: ");
        m.append(Arrays.toString(this.items.toArray()));
        m.append('}');
        return m.toString();
    }

    public ShapeGroup(String str, List<ContentModel> list, boolean z) {
        this.name = str;
        this.items = list;
        this.hidden = z;
    }
}
