package com.android.systemui;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class ImageWallpaper_Factory implements Factory<ImageWallpaper> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final ImageWallpaper_Factory INSTANCE = new ImageWallpaper_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new ImageWallpaper();
    }
}
