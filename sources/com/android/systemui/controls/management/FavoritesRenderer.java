package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.res.Resources;
import kotlin.jvm.functions.Function1;
/* compiled from: AppAdapter.kt */
/* loaded from: classes.dex */
public final class FavoritesRenderer {
    public final Function1<ComponentName, Integer> favoriteFunction;
    public final Resources resources;

    /* JADX WARN: Multi-variable type inference failed */
    public FavoritesRenderer(Resources resources, Function1<? super ComponentName, Integer> function1) {
        this.resources = resources;
        this.favoriteFunction = function1;
    }
}
