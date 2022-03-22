package com.android.systemui.qs;

import com.android.systemui.qs.PathInterpolatorBuilder;
import java.util.Objects;
/* compiled from: QSExpansionPathInterpolator.kt */
/* loaded from: classes.dex */
public final class QSExpansionPathInterpolator {
    public PathInterpolatorBuilder pathInterpolatorBuilder = new PathInterpolatorBuilder();

    public final PathInterpolatorBuilder.PathInterpolator getYInterpolator() {
        PathInterpolatorBuilder pathInterpolatorBuilder = this.pathInterpolatorBuilder;
        Objects.requireNonNull(pathInterpolatorBuilder);
        return new PathInterpolatorBuilder.PathInterpolator(pathInterpolatorBuilder.mDist, pathInterpolatorBuilder.mY);
    }
}
