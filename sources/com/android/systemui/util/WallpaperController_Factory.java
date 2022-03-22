package com.android.systemui.util;

import android.app.KeyguardManager;
import android.app.WallpaperManager;
import android.content.Context;
import android.content.res.Resources;
import com.android.systemui.broadcast.logging.BroadcastDispatcherLogger;
import com.android.systemui.log.LogBuffer;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WallpaperController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider wallpaperManagerProvider;

    public /* synthetic */ WallpaperController_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.wallpaperManagerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new WallpaperController((WallpaperManager) this.wallpaperManagerProvider.mo144get());
            case 1:
                return new BroadcastDispatcherLogger((LogBuffer) this.wallpaperManagerProvider.mo144get());
            case 2:
                KeyguardManager keyguardManager = (KeyguardManager) ((Context) this.wallpaperManagerProvider.mo144get()).getSystemService(KeyguardManager.class);
                Objects.requireNonNull(keyguardManager, "Cannot return null from a non-@Nullable @Provides method");
                return keyguardManager;
            default:
                String[] stringArray = ((Resources) this.wallpaperManagerProvider.mo144get()).getStringArray(17236100);
                Objects.requireNonNull(stringArray, "Cannot return null from a non-@Nullable @Provides method");
                return stringArray;
        }
    }
}
