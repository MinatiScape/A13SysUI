package com.android.systemui.media;

import android.content.Context;
import android.content.res.Resources;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.statusbar.notification.collection.coordinator.HeadsUpCoordinatorLogger;
import com.android.systemui.util.Utils;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaBrowserFactory_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;

    public /* synthetic */ MediaBrowserFactory_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new MediaBrowserFactory((Context) this.contextProvider.mo144get());
            case 1:
                return Float.valueOf(((Resources) this.contextProvider.mo144get()).getDimension(2131165678));
            case 2:
                return Long.valueOf(((Resources) this.contextProvider.mo144get()).getInteger(2131492892));
            case 3:
                return Boolean.valueOf(Utils.useQsMediaPlayer((Context) this.contextProvider.mo144get()));
            default:
                return new HeadsUpCoordinatorLogger((LogBuffer) this.contextProvider.mo144get());
        }
    }
}
