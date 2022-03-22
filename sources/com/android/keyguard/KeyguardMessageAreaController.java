package com.android.keyguard;

import android.content.res.Configuration;
import android.view.ViewGroup;
import com.android.internal.policy.SystemBarUtils;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.ViewController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class KeyguardMessageAreaController extends ViewController<KeyguardMessageArea> {
    public final ConfigurationController mConfigurationController;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public AnonymousClass1 mInfoCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.KeyguardMessageAreaController.1
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onFinishedGoingToSleep(int i) {
            ((KeyguardMessageArea) KeyguardMessageAreaController.this.mView).setSelected(false);
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onKeyguardBouncerChanged(boolean z) {
            KeyguardMessageArea keyguardMessageArea = (KeyguardMessageArea) KeyguardMessageAreaController.this.mView;
            Objects.requireNonNull(keyguardMessageArea);
            keyguardMessageArea.mBouncerVisible = z;
            ((KeyguardMessageArea) KeyguardMessageAreaController.this.mView).update();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onStartedWakingUp() {
            ((KeyguardMessageArea) KeyguardMessageAreaController.this.mView).setSelected(true);
        }
    };
    public AnonymousClass2 mConfigurationListener = new ConfigurationController.ConfigurationListener() { // from class: com.android.keyguard.KeyguardMessageAreaController.2
        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onConfigChanged(Configuration configuration) {
            int statusBarHeight;
            KeyguardMessageArea keyguardMessageArea = (KeyguardMessageArea) KeyguardMessageAreaController.this.mView;
            Objects.requireNonNull(keyguardMessageArea);
            if (keyguardMessageArea.mContainer != null && keyguardMessageArea.mTopMargin != (statusBarHeight = SystemBarUtils.getStatusBarHeight(keyguardMessageArea.getContext()))) {
                keyguardMessageArea.mTopMargin = statusBarHeight;
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) keyguardMessageArea.mContainer.getLayoutParams();
                marginLayoutParams.topMargin = keyguardMessageArea.mTopMargin;
                keyguardMessageArea.mContainer.setLayoutParams(marginLayoutParams);
            }
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onDensityOrFontScaleChanged() {
            ((KeyguardMessageArea) KeyguardMessageAreaController.this.mView).onDensityOrFontScaleChanged();
        }

        @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
        public final void onThemeChanged() {
            ((KeyguardMessageArea) KeyguardMessageAreaController.this.mView).onThemeChanged();
        }
    };

    public final void setMessage(CharSequence charSequence) {
        ((KeyguardMessageArea) this.mView).setMessage(charSequence);
    }

    /* loaded from: classes.dex */
    public static class Factory {
        public final ConfigurationController mConfigurationController;
        public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;

        public Factory(KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController) {
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
            this.mConfigurationController = configurationController;
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mConfigurationController.addCallback(this.mConfigurationListener);
        this.mKeyguardUpdateMonitor.registerCallback(this.mInfoCallback);
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        ((KeyguardMessageArea) this.mView).setSelected(keyguardUpdateMonitor.mDeviceInteractive);
        ((KeyguardMessageArea) this.mView).onThemeChanged();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mConfigurationController.removeCallback(this.mConfigurationListener);
        this.mKeyguardUpdateMonitor.removeCallback(this.mInfoCallback);
    }

    public final void setMessage(int i) {
        KeyguardMessageArea keyguardMessageArea = (KeyguardMessageArea) this.mView;
        Objects.requireNonNull(keyguardMessageArea);
        keyguardMessageArea.setMessage(i != 0 ? keyguardMessageArea.getContext().getResources().getText(i) : null);
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.keyguard.KeyguardMessageAreaController$1] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.keyguard.KeyguardMessageAreaController$2] */
    public KeyguardMessageAreaController(KeyguardMessageArea keyguardMessageArea, KeyguardUpdateMonitor keyguardUpdateMonitor, ConfigurationController configurationController) {
        super(keyguardMessageArea);
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        this.mConfigurationController = configurationController;
    }
}
