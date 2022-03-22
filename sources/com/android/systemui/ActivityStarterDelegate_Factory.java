package com.android.systemui;

import android.hardware.SensorPrivacyManager;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import com.android.systemui.classifier.HistoryTracker;
import com.android.systemui.sensorprivacy.television.TvUnblockSensorActivity;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.TapAgainView;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.SensorPrivacyController;
import com.android.systemui.statusbar.policy.SensorPrivacyControllerImpl;
import com.android.systemui.statusbar.policy.WalletControllerImpl;
import com.android.systemui.util.time.SystemClock;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ActivityStarterDelegate_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider statusBarOptionalLazyProvider;

    @Override // javax.inject.Provider
    /* renamed from: get  reason: collision with other method in class */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new ActivityStarterDelegate(DoubleCheck.lazy(this.statusBarOptionalLazyProvider));
            case 1:
                return new HistoryTracker((SystemClock) this.statusBarOptionalLazyProvider.mo144get());
            case 2:
                return mo144get();
            case 3:
                return new TvUnblockSensorActivity((IndividualSensorPrivacyController) this.statusBarOptionalLazyProvider.mo144get());
            case 4:
                NotificationPanelView notificationPanelView = (NotificationPanelView) this.statusBarOptionalLazyProvider.mo144get();
                Objects.requireNonNull(notificationPanelView);
                TapAgainView tapAgainView = (TapAgainView) notificationPanelView.findViewById(2131428853);
                Objects.requireNonNull(tapAgainView, "Cannot return null from a non-@Nullable @Provides method");
                return tapAgainView;
            case 5:
                return new WalletControllerImpl((QuickAccessWalletClient) this.statusBarOptionalLazyProvider.mo144get());
            default:
                return mo144get();
        }
    }

    public /* synthetic */ ActivityStarterDelegate_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.statusBarOptionalLazyProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final SensorPrivacyController mo144get() {
        switch (this.$r8$classId) {
            case 2:
                SensorPrivacyControllerImpl sensorPrivacyControllerImpl = new SensorPrivacyControllerImpl((SensorPrivacyManager) this.statusBarOptionalLazyProvider.mo144get());
                sensorPrivacyControllerImpl.mSensorPrivacyEnabled = sensorPrivacyControllerImpl.mSensorPrivacyManager.isAllSensorPrivacyEnabled();
                sensorPrivacyControllerImpl.mSensorPrivacyManager.addAllSensorPrivacyListener(sensorPrivacyControllerImpl);
                return sensorPrivacyControllerImpl;
            default:
                SensorPrivacyControllerImpl sensorPrivacyControllerImpl2 = new SensorPrivacyControllerImpl((SensorPrivacyManager) this.statusBarOptionalLazyProvider.mo144get());
                sensorPrivacyControllerImpl2.mSensorPrivacyEnabled = sensorPrivacyControllerImpl2.mSensorPrivacyManager.isAllSensorPrivacyEnabled();
                sensorPrivacyControllerImpl2.mSensorPrivacyManager.addAllSensorPrivacyListener(sensorPrivacyControllerImpl2);
                return sensorPrivacyControllerImpl2;
        }
    }
}
