package com.android.systemui.screenshot;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class LongScreenshotData_Factory implements Factory<LongScreenshotData> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final LongScreenshotData_Factory INSTANCE = new LongScreenshotData_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new LongScreenshotData();
    }
}
