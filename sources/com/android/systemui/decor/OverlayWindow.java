package com.android.systemui.decor;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.HashMap;
import java.util.Objects;
/* compiled from: OverlayWindow.kt */
/* loaded from: classes.dex */
public final class OverlayWindow {
    public final LayoutInflater layoutInflater;
    public final ViewGroup rootView;
    public final HashMap viewProviderMap;

    public OverlayWindow(LayoutInflater layoutInflater, int i) {
        int i2;
        this.layoutInflater = layoutInflater;
        if (i == 0 || i == 1) {
            i2 = 2131624451;
        } else {
            i2 = 2131624450;
        }
        View inflate = layoutInflater.inflate(i2, (ViewGroup) null);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
        this.rootView = (ViewGroup) inflate;
        this.viewProviderMap = new HashMap();
    }
}
