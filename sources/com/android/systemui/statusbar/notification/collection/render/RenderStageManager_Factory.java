package com.android.systemui.statusbar.notification.collection.render;

import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class RenderStageManager_Factory implements Factory<RenderStageManager> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final RenderStageManager_Factory INSTANCE = new RenderStageManager_Factory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new RenderStageManager();
    }
}
