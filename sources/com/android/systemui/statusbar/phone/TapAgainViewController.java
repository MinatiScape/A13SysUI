package com.android.systemui.statusbar.phone;

import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ViewController;
import com.android.systemui.util.concurrency.DelayableExecutor;
/* loaded from: classes.dex */
public final class TapAgainViewController extends ViewController<TapAgainView> {
    public final ConfigurationController mConfigurationController;
    @VisibleForTesting
    public final ConfigurationController.ConfigurationListener mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.phone.TapAgainViewController.1
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onThemeChanged() {
            ((TapAgainView) TapAgainViewController.this.mView).updateColor();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onUiModeChanged() {
            ((TapAgainView) TapAgainViewController.this.mView).updateColor();
        }
    };
    public final DelayableExecutor mDelayableExecutor;
    public final long mDoubleTapTimeMs;
    public Runnable mHideCanceler;

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mConfigurationController.addCallback(this.mConfigurationListener);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
    }

    public TapAgainViewController(TapAgainView tapAgainView, DelayableExecutor delayableExecutor, ConfigurationController configurationController, long j) {
        super(tapAgainView);
        this.mDelayableExecutor = delayableExecutor;
        this.mConfigurationController = configurationController;
        this.mDoubleTapTimeMs = j;
    }
}
