package com.android.keyguard;

import android.view.LayoutInflater;
import com.android.keyguard.EmergencyButtonController;
import com.android.keyguard.KeyguardInputViewController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class KeyguardSecurityViewFlipperController_Factory implements Factory<KeyguardSecurityViewFlipperController> {
    public final Provider<EmergencyButtonController.Factory> emergencyButtonControllerFactoryProvider;
    public final Provider<KeyguardInputViewController.Factory> keyguardSecurityViewControllerFactoryProvider;
    public final Provider<LayoutInflater> layoutInflaterProvider;
    public final Provider<KeyguardSecurityViewFlipper> viewProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new KeyguardSecurityViewFlipperController(this.viewProvider.mo144get(), this.layoutInflaterProvider.mo144get(), this.keyguardSecurityViewControllerFactoryProvider.mo144get(), this.emergencyButtonControllerFactoryProvider.mo144get());
    }

    public KeyguardSecurityViewFlipperController_Factory(Provider provider, Provider provider2, KeyguardInputViewController_Factory_Factory keyguardInputViewController_Factory_Factory, Provider provider3) {
        this.viewProvider = provider;
        this.layoutInflaterProvider = provider2;
        this.keyguardSecurityViewControllerFactoryProvider = keyguardInputViewController_Factory_Factory;
        this.emergencyButtonControllerFactoryProvider = provider3;
    }
}
