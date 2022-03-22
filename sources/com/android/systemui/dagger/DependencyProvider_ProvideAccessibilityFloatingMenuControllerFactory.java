package com.android.systemui.dagger;

import android.content.Context;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.accessibility.AccessibilityButtonModeObserver;
import com.android.systemui.accessibility.AccessibilityButtonTargetsObserver;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuController;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DependencyProvider_ProvideAccessibilityFloatingMenuControllerFactory implements Factory<AccessibilityFloatingMenuController> {
    public final Provider<AccessibilityButtonModeObserver> accessibilityButtonModeObserverProvider;
    public final Provider<AccessibilityButtonTargetsObserver> accessibilityButtonTargetsObserverProvider;
    public final Provider<Context> contextProvider;
    public final Provider<KeyguardUpdateMonitor> keyguardUpdateMonitorProvider;
    public final DependencyProvider module;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        Objects.requireNonNull(this.module);
        return new AccessibilityFloatingMenuController(this.contextProvider.mo144get(), this.accessibilityButtonTargetsObserverProvider.mo144get(), this.accessibilityButtonModeObserverProvider.mo144get(), this.keyguardUpdateMonitorProvider.mo144get());
    }

    public DependencyProvider_ProvideAccessibilityFloatingMenuControllerFactory(DependencyProvider dependencyProvider, Provider<Context> provider, Provider<AccessibilityButtonTargetsObserver> provider2, Provider<AccessibilityButtonModeObserver> provider3, Provider<KeyguardUpdateMonitor> provider4) {
        this.module = dependencyProvider;
        this.contextProvider = provider;
        this.accessibilityButtonTargetsObserverProvider = provider2;
        this.accessibilityButtonModeObserverProvider = provider3;
        this.keyguardUpdateMonitorProvider = provider4;
    }
}
