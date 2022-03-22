package com.android.systemui.statusbar.phone;

import android.graphics.Rect;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.BatteryControllerImpl;
import com.android.systemui.util.service.ObservableServiceConnection;
import com.android.wm.shell.pip.phone.PipResizeGestureHandler;
import com.android.wm.shell.startingsurface.StartingSurface;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda33 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda33(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                StatusBar statusBar = (StatusBar) this.f$0;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                ((StartingSurface) obj).setSysuiProxy(new StatusBar$$ExternalSyntheticLambda4(statusBar));
                return;
            case 1:
                NotificationContentView notificationContentView = (NotificationContentView) this.f$0;
                boolean booleanValue = ((Boolean) obj).booleanValue();
                Objects.requireNonNull(notificationContentView);
                notificationContentView.mRemoteInputVisible = booleanValue;
                notificationContentView.setClipChildren(!booleanValue);
                return;
            case 2:
                BatteryControllerImpl batteryControllerImpl = (BatteryControllerImpl) this.f$0;
                boolean z = BatteryControllerImpl.DEBUG;
                Objects.requireNonNull(batteryControllerImpl);
                ((BatteryController.BatteryStateChangeCallback) obj).onWirelessChargingChanged(batteryControllerImpl.mWirelessCharging);
                return;
            case 3:
                ObservableServiceConnection observableServiceConnection = (ObservableServiceConnection) this.f$0;
                boolean z2 = ObservableServiceConnection.DEBUG;
                Objects.requireNonNull(observableServiceConnection);
                observableServiceConnection.mLastDisconnectReason.get().intValue();
                ((ObservableServiceConnection.Callback) obj).onDisconnected();
                return;
            default:
                PipResizeGestureHandler pipResizeGestureHandler = (PipResizeGestureHandler) this.f$0;
                Rect rect = (Rect) obj;
                Objects.requireNonNull(pipResizeGestureHandler);
                pipResizeGestureHandler.mUserResizeBounds.set(pipResizeGestureHandler.mLastResizeBounds);
                pipResizeGestureHandler.mMotionHelper.synchronizePinnedStackBounds();
                pipResizeGestureHandler.mUpdateMovementBoundsRunnable.run();
                pipResizeGestureHandler.mCtrlType = 0;
                pipResizeGestureHandler.mAngle = 0.0f;
                pipResizeGestureHandler.mOngoingPinchToResize = false;
                pipResizeGestureHandler.mAllowGesture = false;
                pipResizeGestureHandler.mThresholdCrossed = false;
                return;
        }
    }
}
