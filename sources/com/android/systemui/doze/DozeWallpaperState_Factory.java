package com.android.systemui.doze;

import android.app.IWallpaperManager;
import android.os.Looper;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.android.systemui.statusbar.notification.row.NotifBindPipeline;
import com.android.systemui.statusbar.notification.row.NotifBindPipelineLogger;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.DozeParameters;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeWallpaperState_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider biometricUnlockControllerProvider;
    public final Provider parametersProvider;
    public final Provider wallpaperManagerServiceProvider;

    public /* synthetic */ DozeWallpaperState_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.wallpaperManagerServiceProvider = provider;
        this.biometricUnlockControllerProvider = provider2;
        this.parametersProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new DozeWallpaperState((IWallpaperManager) this.wallpaperManagerServiceProvider.mo144get(), (BiometricUnlockController) this.biometricUnlockControllerProvider.mo144get(), (DozeParameters) this.parametersProvider.mo144get());
            default:
                return new NotifBindPipeline((CommonNotifCollection) this.wallpaperManagerServiceProvider.mo144get(), (NotifBindPipelineLogger) this.biometricUnlockControllerProvider.mo144get(), (Looper) this.parametersProvider.mo144get());
        }
    }
}
