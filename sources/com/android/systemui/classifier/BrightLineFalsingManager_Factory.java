package com.android.systemui.classifier;

import android.view.accessibility.AccessibilityManager;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.dagger.GlobalModule_ProvideIsTestHarnessFactory;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class BrightLineFalsingManager_Factory implements Factory<BrightLineFalsingManager> {
    public final Provider<AccessibilityManager> accessibilityManagerProvider;
    public final Provider<Set<FalsingClassifier>> classifiersProvider;
    public final Provider<DoubleTapClassifier> doubleTapClassifierProvider;
    public final Provider<FalsingDataProvider> falsingDataProvider;
    public final Provider<HistoryTracker> historyTrackerProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<MetricsLogger> metricsLoggerProvider;
    public final Provider<SingleTapClassifier> singleTapClassifierProvider;
    public final Provider<Boolean> testHarnessProvider;

    public BrightLineFalsingManager_Factory(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8) {
        GlobalModule_ProvideIsTestHarnessFactory globalModule_ProvideIsTestHarnessFactory = GlobalModule_ProvideIsTestHarnessFactory.InstanceHolder.INSTANCE;
        this.falsingDataProvider = provider;
        this.metricsLoggerProvider = provider2;
        this.classifiersProvider = provider3;
        this.singleTapClassifierProvider = provider4;
        this.doubleTapClassifierProvider = provider5;
        this.historyTrackerProvider = provider6;
        this.keyguardStateControllerProvider = provider7;
        this.accessibilityManagerProvider = provider8;
        this.testHarnessProvider = globalModule_ProvideIsTestHarnessFactory;
    }

    public static BrightLineFalsingManager_Factory create(Provider provider, Provider provider2, Provider provider3, Provider provider4, Provider provider5, Provider provider6, Provider provider7, Provider provider8) {
        return new BrightLineFalsingManager_Factory(provider, provider2, provider3, provider4, provider5, provider6, provider7, provider8);
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new BrightLineFalsingManager(this.falsingDataProvider.mo144get(), this.metricsLoggerProvider.mo144get(), this.classifiersProvider.mo144get(), this.singleTapClassifierProvider.mo144get(), this.doubleTapClassifierProvider.mo144get(), this.historyTrackerProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), this.accessibilityManagerProvider.mo144get(), this.testHarnessProvider.mo144get().booleanValue());
    }
}
