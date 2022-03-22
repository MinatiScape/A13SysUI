package com.android.systemui.decor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;
/* compiled from: DecorProvider.kt */
/* loaded from: classes.dex */
public abstract class DecorProvider {
    public abstract List<Integer> getAlignedBounds();

    public abstract int getViewId();

    public abstract View inflateView(LayoutInflater layoutInflater, ViewGroup viewGroup);
}
