package com.google.android.systemui.elmyra.feedback;

import android.content.Context;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.dagger.DependencyProvider;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class OpaLockscreen_Factory implements Factory {
    public final /* synthetic */ int $r8$classId = 1;
    public final Object keyguardStateControllerProvider;
    public final Provider statusBarProvider;

    public OpaLockscreen_Factory(Provider provider, Provider provider2) {
        this.statusBarProvider = provider;
        this.keyguardStateControllerProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new OpaLockscreen((StatusBar) this.statusBarProvider.mo144get(), (KeyguardStateController) ((Provider) this.keyguardStateControllerProvider).mo144get());
            default:
                Objects.requireNonNull((DependencyProvider) this.keyguardStateControllerProvider);
                return new LockPatternUtils((Context) this.statusBarProvider.mo144get());
        }
    }

    public OpaLockscreen_Factory(DependencyProvider dependencyProvider, Provider provider) {
        this.keyguardStateControllerProvider = dependencyProvider;
        this.statusBarProvider = provider;
    }
}
