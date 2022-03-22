package com.android.wm.shell.dagger;

import android.content.Context;
import android.content.res.Resources;
import android.service.dreams.IDreamManager;
import com.android.internal.util.LatencyTracker;
import com.android.systemui.animation.DialogLaunchAnimator;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.statusbar.notification.collection.coordinator.SharedCoordinatorLogger;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import dagger.internal.Factory;
import java.util.Objects;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class WMShellBaseModule_ProvideHideDisplayCutoutFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider hideDisplayCutoutControllerProvider;

    public /* synthetic */ WMShellBaseModule_ProvideHideDisplayCutoutFactory(Provider provider, int i) {
        this.$r8$classId = i;
        this.hideDisplayCutoutControllerProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                Optional map = ((Optional) this.hideDisplayCutoutControllerProvider.mo144get()).map(WMShellBaseModule$$ExternalSyntheticLambda2.INSTANCE);
                Objects.requireNonNull(map, "Cannot return null from a non-@Nullable @Provides method");
                return map;
            case 1:
                LatencyTracker instance = LatencyTracker.getInstance((Context) this.hideDisplayCutoutControllerProvider.mo144get());
                Objects.requireNonNull(instance, "Cannot return null from a non-@Nullable @Provides method");
                return instance;
            case 2:
                return new DialogLaunchAnimator((IDreamManager) this.hideDisplayCutoutControllerProvider.mo144get());
            case 3:
                return new SharedCoordinatorLogger((LogBuffer) this.hideDisplayCutoutControllerProvider.mo144get());
            case 4:
                PhoneStatusBarView phoneStatusBarView = (PhoneStatusBarView) ((CollapsedStatusBarFragment) this.hideDisplayCutoutControllerProvider.mo144get()).getView();
                Objects.requireNonNull(phoneStatusBarView, "Cannot return null from a non-@Nullable @Provides method");
                return phoneStatusBarView;
            default:
                return Boolean.valueOf(((Resources) this.hideDisplayCutoutControllerProvider.mo144get()).getBoolean(17891644));
        }
    }
}
