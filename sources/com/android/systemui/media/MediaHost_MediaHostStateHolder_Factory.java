package com.android.systemui.media;

import com.android.systemui.media.MediaHost;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class MediaHost_MediaHostStateHolder_Factory implements Factory<MediaHost.MediaHostStateHolder> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final MediaHost_MediaHostStateHolder_Factory INSTANCE = new MediaHost_MediaHostStateHolder_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new MediaHost.MediaHostStateHolder();
    }
}
