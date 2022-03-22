package com.android.systemui.media;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaViewController.kt */
/* loaded from: classes.dex */
public final class CacheKey {
    public float expansion;
    public boolean gutsVisible;
    public int heightMeasureSpec;
    public int widthMeasureSpec;

    public CacheKey() {
        this(0);
    }

    public /* synthetic */ CacheKey(int i) {
        this(-1, -1, 0.0f, false);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CacheKey)) {
            return false;
        }
        CacheKey cacheKey = (CacheKey) obj;
        return this.widthMeasureSpec == cacheKey.widthMeasureSpec && this.heightMeasureSpec == cacheKey.heightMeasureSpec && Intrinsics.areEqual(Float.valueOf(this.expansion), Float.valueOf(cacheKey.expansion)) && this.gutsVisible == cacheKey.gutsVisible;
    }

    public CacheKey(int i, int i2, float f, boolean z) {
        this.widthMeasureSpec = i;
        this.heightMeasureSpec = i2;
        this.expansion = f;
        this.gutsVisible = z;
    }

    public final int hashCode() {
        int hashCode = (Float.hashCode(this.expansion) + FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.heightMeasureSpec, Integer.hashCode(this.widthMeasureSpec) * 31, 31)) * 31;
        boolean z = this.gutsVisible;
        if (z) {
            z = true;
        }
        int i = z ? 1 : 0;
        int i2 = z ? 1 : 0;
        return hashCode + i;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("CacheKey(widthMeasureSpec=");
        m.append(this.widthMeasureSpec);
        m.append(", heightMeasureSpec=");
        m.append(this.heightMeasureSpec);
        m.append(", expansion=");
        m.append(this.expansion);
        m.append(", gutsVisible=");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(m, this.gutsVisible, ')');
    }
}
