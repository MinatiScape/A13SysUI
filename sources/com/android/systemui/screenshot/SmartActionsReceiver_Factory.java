package com.android.systemui.screenshot;

import android.app.smartspace.SmartspaceManager;
import android.content.Context;
import android.hardware.SensorPrivacyManager;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import com.android.systemui.log.LogBuffer;
import com.android.systemui.log.LogBufferFactory;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.NotificationListener;
import com.android.systemui.statusbar.gesture.SwipeStatusBarAwayGestureLogger;
import com.android.systemui.statusbar.notification.init.NotificationsControllerStub;
import com.android.systemui.statusbar.policy.SensorPrivacyControllerImpl;
import com.android.systemui.statusbar.window.StatusBarWindowView;
import dagger.internal.Factory;
import java.util.Objects;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SmartActionsReceiver_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider screenshotSmartActionsProvider;

    public /* synthetic */ SmartActionsReceiver_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.screenshotSmartActionsProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new SmartActionsReceiver((ScreenshotSmartActions) this.screenshotSmartActionsProvider.mo144get());
            case 1:
                SmartspaceManager smartspaceManager = (SmartspaceManager) ((Context) this.screenshotSmartActionsProvider.mo144get()).getSystemService(SmartspaceManager.class);
                Objects.requireNonNull(smartspaceManager, "Cannot return null from a non-@Nullable @Provides method");
                return smartspaceManager;
            case 2:
                return ((LogBufferFactory) this.screenshotSmartActionsProvider.mo144get()).create("LSShadeTransitionLog", 50);
            case 3:
                return new SwipeStatusBarAwayGestureLogger((LogBuffer) this.screenshotSmartActionsProvider.mo144get());
            case 4:
                return new NotificationsControllerStub((NotificationListener) this.screenshotSmartActionsProvider.mo144get());
            case 5:
                StatusBarWindowView statusBarWindowView = (StatusBarWindowView) ((LayoutInflater) this.screenshotSmartActionsProvider.mo144get()).inflate(2131624601, (ViewGroup) null);
                if (statusBarWindowView != null) {
                    return statusBarWindowView;
                }
                throw new IllegalStateException("R.layout.super_status_bar could not be properly inflated");
            case FalsingManager.VERSION /* 6 */:
                return new Handler((Looper) this.screenshotSmartActionsProvider.mo144get());
            default:
                SensorPrivacyControllerImpl sensorPrivacyControllerImpl = new SensorPrivacyControllerImpl((SensorPrivacyManager) this.screenshotSmartActionsProvider.mo144get());
                sensorPrivacyControllerImpl.mSensorPrivacyEnabled = sensorPrivacyControllerImpl.mSensorPrivacyManager.isAllSensorPrivacyEnabled();
                sensorPrivacyControllerImpl.mSensorPrivacyManager.addAllSensorPrivacyListener(sensorPrivacyControllerImpl);
                return sensorPrivacyControllerImpl;
        }
    }
}
