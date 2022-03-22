package com.android.systemui.statusbar;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class MediaArtworkProcessor_Factory implements Factory<MediaArtworkProcessor> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final MediaArtworkProcessor_Factory INSTANCE = new MediaArtworkProcessor_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new MediaArtworkProcessor();
    }
}
