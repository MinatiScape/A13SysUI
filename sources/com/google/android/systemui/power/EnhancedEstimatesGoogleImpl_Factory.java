package com.google.android.systemui.power;

import android.content.Context;
import android.content.pm.LauncherApps;
import com.android.launcher3.icons.IconProvider;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.statusbar.phone.PhoneStatusBarView;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherContainer;
import com.google.android.systemui.columbus.feedback.AssistInvocationEffect;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class EnhancedEstimatesGoogleImpl_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;

    public /* synthetic */ EnhancedEstimatesGoogleImpl_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new EnhancedEstimatesGoogleImpl((Context) this.contextProvider.mo144get());
            case 1:
                LauncherApps launcherApps = (LauncherApps) ((Context) this.contextProvider.mo144get()).getSystemService(LauncherApps.class);
                Objects.requireNonNull(launcherApps, "Cannot return null from a non-@Nullable @Provides method");
                return launcherApps;
            case 2:
                StatusBarUserSwitcherContainer statusBarUserSwitcherContainer = (StatusBarUserSwitcherContainer) ((PhoneStatusBarView) this.contextProvider.mo144get()).findViewById(2131429173);
                Objects.requireNonNull(statusBarUserSwitcherContainer, "Cannot return null from a non-@Nullable @Provides method");
                return statusBarUserSwitcherContainer;
            case 3:
                return new IconProvider((Context) this.contextProvider.mo144get());
            default:
                return new AssistInvocationEffect((AssistManager) this.contextProvider.mo144get());
        }
    }
}
