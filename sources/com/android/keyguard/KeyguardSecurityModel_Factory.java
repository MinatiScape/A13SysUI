package com.android.keyguard;

import android.content.Context;
import android.content.res.Resources;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.notification.collection.notifcollection.CommonNotifCollection;
import com.google.android.systemui.statusbar.phone.WallpaperNotifier;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardSecurityModel_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider keyguardUpdateMonitorProvider;
    public final Provider lockPatternUtilsProvider;
    public final Provider resourcesProvider;

    public /* synthetic */ KeyguardSecurityModel_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.resourcesProvider = provider;
        this.lockPatternUtilsProvider = provider2;
        this.keyguardUpdateMonitorProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new KeyguardSecurityModel((Resources) this.resourcesProvider.mo144get(), (LockPatternUtils) this.lockPatternUtilsProvider.mo144get(), (KeyguardUpdateMonitor) this.keyguardUpdateMonitorProvider.mo144get());
            default:
                return new WallpaperNotifier((Context) this.resourcesProvider.mo144get(), (CommonNotifCollection) this.lockPatternUtilsProvider.mo144get(), (BroadcastDispatcher) this.keyguardUpdateMonitorProvider.mo144get());
        }
    }
}
