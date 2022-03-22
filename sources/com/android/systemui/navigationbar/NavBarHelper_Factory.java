package com.android.systemui.navigationbar;

import android.content.Context;
import android.view.accessibility.AccessibilityManager;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.SystemActions;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NavBarHelper_Factory implements Factory<NavBarHelper> {
    public final Provider<AccessibilityButtonModeObserver> accessibilityButtonModeObserverProvider;
    public final Provider<AccessibilityButtonTargetsObserver> accessibilityButtonTargetsObserverProvider;
    public final Provider<AccessibilityManager> accessibilityManagerProvider;
    public final Provider<AssistManager> assistManagerLazyProvider;
    public final Provider<Context> contextProvider;
    public final Provider<DumpManager> dumpManagerProvider;
    public final Provider<NavigationModeController> navigationModeControllerProvider;
    public final Provider<OverviewProxyService> overviewProxyServiceProvider;
    public final Provider<Optional<StatusBar>> statusBarOptionalLazyProvider;
    public final Provider<SystemActions> systemActionsProvider;
    public final Provider<UserTracker> userTrackerProvider;

    public static NavBarHelper_Factory create(Provider<Context> provider, Provider<AccessibilityManager> provider2, Provider<AccessibilityButtonModeObserver> provider3, Provider<AccessibilityButtonTargetsObserver> provider4, Provider<SystemActions> provider5, Provider<OverviewProxyService> provider6, Provider<AssistManager> provider7, Provider<Optional<StatusBar>> provider8, Provider<NavigationModeController> provider9, Provider<UserTracker> provider10, Provider<DumpManager> provider11) {
        return new NavBarHelper_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8, provider9, provider10, provider11);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new NavBarHelper(this.contextProvider.mo144get(), this.accessibilityManagerProvider.mo144get(), this.accessibilityButtonModeObserverProvider.mo144get(), this.accessibilityButtonTargetsObserverProvider.mo144get(), this.systemActionsProvider.mo144get(), this.overviewProxyServiceProvider.mo144get(), DoubleCheck.lazy(this.assistManagerLazyProvider), DoubleCheck.lazy(this.statusBarOptionalLazyProvider), this.navigationModeControllerProvider.mo144get(), this.userTrackerProvider.mo144get(), this.dumpManagerProvider.mo144get());
    }

    public NavBarHelper_Factory(Provider<Context> provider, Provider<AccessibilityManager> provider2, Provider<AccessibilityButtonModeObserver> provider3, Provider<AccessibilityButtonTargetsObserver> provider4, Provider<SystemActions> provider5, Provider<OverviewProxyService> provider6, Provider<AssistManager> provider7, Provider<Optional<StatusBar>> provider8, Provider<NavigationModeController> provider9, Provider<UserTracker> provider10, Provider<DumpManager> provider11) {
        this.contextProvider = provider;
        this.accessibilityManagerProvider = provider2;
        this.accessibilityButtonModeObserverProvider = provider3;
        this.accessibilityButtonTargetsObserverProvider = provider4;
        this.systemActionsProvider = provider5;
        this.overviewProxyServiceProvider = provider6;
        this.assistManagerLazyProvider = provider7;
        this.statusBarOptionalLazyProvider = provider8;
        this.navigationModeControllerProvider = provider9;
        this.userTrackerProvider = provider10;
        this.dumpManagerProvider = provider11;
    }
}
