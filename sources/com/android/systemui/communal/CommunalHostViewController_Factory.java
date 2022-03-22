package com.android.systemui.communal;

import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class CommunalHostViewController_Factory implements Factory<CommunalHostViewController> {
    public static CommunalHostViewController newInstance(Executor executor, CommunalStateController communalStateController, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardStateController keyguardStateController, ScreenOffAnimationController screenOffAnimationController, StatusBarStateController statusBarStateController, CommunalHostView communalHostView) {
        return new CommunalHostViewController(executor, communalStateController, keyguardUpdateMonitor, keyguardStateController, screenOffAnimationController, statusBarStateController, communalHostView);
    }
}
