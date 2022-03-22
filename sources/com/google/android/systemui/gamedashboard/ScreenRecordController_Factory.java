package com.google.android.systemui.gamedashboard;

import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Handler;
import com.android.systemui.media.systemsounds.HomeSoundEffectController;
import com.android.systemui.screenrecord.RecordingController;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ScreenRecordController_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider controllerProvider;
    public final Provider keyguardDismissUtilProvider;
    public final Provider mainHandlerProvider;
    public final Provider toastProvider;

    public /* synthetic */ ScreenRecordController_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, int i) {
        this.$r8$classId = i;
        this.controllerProvider = provider;
        this.mainHandlerProvider = provider2;
        this.keyguardDismissUtilProvider = provider3;
        this.contextProvider = provider4;
        this.toastProvider = provider5;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ScreenRecordController((RecordingController) this.controllerProvider.mo144get(), (Handler) this.mainHandlerProvider.mo144get(), (KeyguardDismissUtil) this.keyguardDismissUtilProvider.mo144get(), (Context) this.contextProvider.mo144get(), (ToastController) this.toastProvider.mo144get());
            default:
                return new HomeSoundEffectController((Context) this.controllerProvider.mo144get(), (AudioManager) this.mainHandlerProvider.mo144get(), (TaskStackChangeListeners) this.keyguardDismissUtilProvider.mo144get(), (ActivityManagerWrapper) this.contextProvider.mo144get(), (PackageManager) this.toastProvider.mo144get());
        }
    }
}
