package com.android.systemui.screenshot;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class ScreenshotSmartActions_Factory implements Factory<ScreenshotSmartActions> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final ScreenshotSmartActions_Factory INSTANCE = new ScreenshotSmartActions_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ScreenshotSmartActions();
    }
}
