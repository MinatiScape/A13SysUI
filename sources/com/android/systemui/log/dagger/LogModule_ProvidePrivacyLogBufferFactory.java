package com.android.systemui.log.dagger;

import android.content.Context;
import android.os.UserManager;
import android.view.LayoutInflater;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.statusbar.notification.interruption.HeadsUpViewBinderLogger;
import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import com.android.systemui.statusbar.phone.userswitcher.StatusBarUserSwitcherContainer;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LogModule_ProvidePrivacyLogBufferFactory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider factoryProvider;

    public /* synthetic */ LogModule_ProvidePrivacyLogBufferFactory(Provider provider, int i) {
        this.$r8$classId = i;
        this.factoryProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return ((LogBufferFactory) this.factoryProvider.mo144get()).create("PrivacyLog", 100);
            case 1:
                StatusBarUserSwitcherContainer statusBarUserSwitcherContainer = (StatusBarUserSwitcherContainer) ((KeyguardStatusBarView) this.factoryProvider.mo144get()).findViewById(2131429173);
                Objects.requireNonNull(statusBarUserSwitcherContainer, "Cannot return null from a non-@Nullable @Provides method");
                return statusBarUserSwitcherContainer;
            case 2:
                UserManager userManager = (UserManager) ((Context) this.factoryProvider.mo144get()).getSystemService(UserManager.class);
                Objects.requireNonNull(userManager, "Cannot return null from a non-@Nullable @Provides method");
                return userManager;
            case 3:
                LayoutInflater from = LayoutInflater.from((Context) this.factoryProvider.mo144get());
                Objects.requireNonNull(from, "Cannot return null from a non-@Nullable @Provides method");
                return from;
            default:
                return new HeadsUpViewBinderLogger((LogBuffer) this.factoryProvider.mo144get());
        }
    }
}
