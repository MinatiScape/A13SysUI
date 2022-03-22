package com.airbnb.lottie.model.content;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.airbnb.lottie.LottieDrawable;
import com.airbnb.lottie.animation.content.Content;
import com.airbnb.lottie.animation.content.MergePathsContent;
import com.airbnb.lottie.model.layer.BaseLayer;
import com.airbnb.lottie.utils.Logger;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MergePaths implements ContentModel {
    public final boolean hidden;
    public final MergePathsMode mode;
    public final String name;

    /* loaded from: classes.dex */
    public enum MergePathsMode {
        MERGE,
        ADD,
        SUBTRACT,
        INTERSECT,
        EXCLUDE_INTERSECTIONS
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("MergePaths{mode=");
        m.append(this.mode);
        m.append('}');
        return m.toString();
    }

    public MergePaths(String str, MergePathsMode mergePathsMode, boolean z) {
        this.name = str;
        this.mode = mergePathsMode;
        this.hidden = z;
    }

    @Override // com.airbnb.lottie.model.content.ContentModel
    public final Content toContent(LottieDrawable lottieDrawable, BaseLayer baseLayer) {
        Objects.requireNonNull(lottieDrawable);
        if (lottieDrawable.enableMergePaths) {
            return new MergePathsContent(this);
        }
        Logger.warning("Animation contains merge paths but they are disabled.");
        return null;
    }
}
