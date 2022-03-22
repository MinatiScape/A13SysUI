package com.android.systemui.decor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
/* compiled from: PrivacyDotDecorProviderFactory.kt */
/* loaded from: classes.dex */
public final class PrivacyDotCornerDecorProviderImpl extends CornerDecorProvider {
    public final int alignedBound1;
    public final int alignedBound2;
    public final int layoutId;
    public final int viewId;

    @Override // com.android.systemui.decor.DecorProvider
    public final View inflateView(LayoutInflater layoutInflater, ViewGroup viewGroup) {
        layoutInflater.inflate(this.layoutId, viewGroup, true);
        return viewGroup.getChildAt(viewGroup.getChildCount() - 1);
    }

    public PrivacyDotCornerDecorProviderImpl(int i, int i2, int i3, int i4) {
        this.viewId = i;
        this.alignedBound1 = i2;
        this.alignedBound2 = i3;
        this.layoutId = i4;
    }

    @Override // com.android.systemui.decor.DecorProvider
    public final int getViewId() {
        return this.viewId;
    }
}
