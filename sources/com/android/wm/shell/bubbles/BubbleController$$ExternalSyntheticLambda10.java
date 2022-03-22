package com.android.wm.shell.bubbles;

import android.net.Uri;
import android.provider.Settings;
import android.util.Range;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.log.SessionTracker;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.util.sensors.ProximitySensorImpl;
import com.android.systemui.util.sensors.ThresholdSensor;
import com.android.systemui.util.sensors.ThresholdSensorEvent;
import com.android.systemui.wmshell.BubblesManager;
import com.android.systemui.wmshell.BubblesManager$5$$ExternalSyntheticLambda1;
import com.android.wm.shell.bubbles.Bubbles;
import com.google.android.systemui.elmyra.actions.LaunchOpa;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleController$$ExternalSyntheticLambda10 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BubbleController$$ExternalSyntheticLambda10(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        boolean z = true;
        switch (this.$r8$classId) {
            case 0:
                BubblesManager.AnonymousClass5 r4 = (BubblesManager.AnonymousClass5) ((Bubbles.SysuiProxy) this.f$0);
                Objects.requireNonNull(r4);
                r4.val$sysuiMainExecutor.execute(new BubblesManager$5$$ExternalSyntheticLambda1(r4, (String) obj, 0));
                return;
            case 1:
                BiometricUnlockController biometricUnlockController = (BiometricUnlockController) this.f$0;
                UiEventLoggerImpl uiEventLoggerImpl = BiometricUnlockController.UI_EVENT_LOGGER;
                Objects.requireNonNull(biometricUnlockController);
                UiEventLoggerImpl uiEventLoggerImpl2 = BiometricUnlockController.UI_EVENT_LOGGER;
                SessionTracker sessionTracker = biometricUnlockController.mSessionTracker;
                Objects.requireNonNull(sessionTracker);
                uiEventLoggerImpl2.log((BiometricUnlockController.BiometricUiEvent) obj, (InstanceId) sessionTracker.mSessionToInstanceId.getOrDefault(1, null));
                return;
            case 2:
                boolean z2 = ProximitySensorImpl.DEBUG;
                ((ThresholdSensor.Listener) obj).onThresholdCrossed((ThresholdSensorEvent) this.f$0);
                return;
            case 3:
                LaunchOpa launchOpa = (LaunchOpa) this.f$0;
                Uri uri = (Uri) obj;
                Objects.requireNonNull(launchOpa);
                if (Settings.Secure.getIntForUser(launchOpa.mContext.getContentResolver(), "assist_gesture_enabled", 1, -2) == 0) {
                    z = false;
                }
                if (launchOpa.mIsGestureEnabled != z) {
                    launchOpa.mIsGestureEnabled = z;
                    launchOpa.notifyListener();
                    return;
                }
                return;
            default:
                GestureConfiguration gestureConfiguration = (GestureConfiguration) this.f$0;
                Uri uri2 = (Uri) obj;
                Range<Float> range = GestureConfiguration.SENSITIVITY_RANGE;
                Objects.requireNonNull(gestureConfiguration);
                gestureConfiguration.mSensitivity = gestureConfiguration.getUserSensitivity();
                GestureConfiguration.Listener listener = gestureConfiguration.mListener;
                if (listener != null) {
                    listener.onGestureConfigurationChanged(gestureConfiguration);
                    return;
                }
                return;
        }
    }
}
