package com.android.systemui.media;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class MediaHostStatesManager_Factory implements Factory<MediaHostStatesManager> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final MediaHostStatesManager_Factory INSTANCE = new MediaHostStatesManager_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new MediaHostStatesManager();
    }
}
