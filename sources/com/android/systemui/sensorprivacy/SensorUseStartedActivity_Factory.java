package com.android.systemui.sensorprivacy;

import android.os.Handler;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SensorUseStartedActivity_Factory implements Factory<SensorUseStartedActivity> {
    public final Provider<Handler> bgHandlerProvider;
    public final Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
    public final Provider<KeyguardStateController> keyguardStateControllerProvider;
    public final Provider<IndividualSensorPrivacyController> sensorPrivacyControllerProvider;

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        return new SensorUseStartedActivity(this.sensorPrivacyControllerProvider.mo144get(), this.keyguardStateControllerProvider.mo144get(), this.keyguardDismissUtilProvider.mo144get(), this.bgHandlerProvider.mo144get());
    }

    public SensorUseStartedActivity_Factory(Provider<IndividualSensorPrivacyController> provider, Provider<KeyguardStateController> provider2, Provider<KeyguardDismissUtil> provider3, Provider<Handler> provider4) {
        this.sensorPrivacyControllerProvider = provider;
        this.keyguardStateControllerProvider = provider2;
        this.keyguardDismissUtilProvider = provider3;
        this.bgHandlerProvider = provider4;
    }
}
