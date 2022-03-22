package com.android.wm.shell.pip.phone;

import android.graphics.Rect;
import android.os.SystemClock;
import android.util.Range;
import android.view.View;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.log.SessionTracker;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.statusbar.phone.BiometricUnlockController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.util.sensors.ThresholdSensorEvent;
import com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda9;
import com.android.wm.shell.recents.RecentTasksController;
import com.google.android.systemui.elmyra.sensors.config.Adjustment;
import com.google.android.systemui.elmyra.sensors.config.GestureConfiguration;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipMotionHelper$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ PipMotionHelper$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        Boolean bool = null;
        switch (this.$r8$classId) {
            case 0:
                PipMotionHelper pipMotionHelper = (PipMotionHelper) this.f$0;
                Rect rect = (Rect) obj;
                Objects.requireNonNull(pipMotionHelper);
                if (!pipMotionHelper.mPipBoundsState.getBounds().equals(rect)) {
                    pipMotionHelper.mMenuController.updateMenuLayout();
                    pipMotionHelper.mPipBoundsState.setBounds(rect);
                    return;
                }
                return;
            case 1:
                final NavigationBarView navigationBarView = (NavigationBarView) this.f$0;
                final Rect rect2 = (Rect) obj;
                int i = NavigationBarView.$r8$clinit;
                Objects.requireNonNull(navigationBarView);
                navigationBarView.post(new Runnable() { // from class: com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        NavigationBarView navigationBarView2 = NavigationBarView.this;
                        Rect rect3 = rect2;
                        int i2 = NavigationBarView.$r8$clinit;
                        Objects.requireNonNull(navigationBarView2);
                        EdgeBackGestureHandler edgeBackGestureHandler = navigationBarView2.mEdgeBackGestureHandler;
                        Objects.requireNonNull(edgeBackGestureHandler);
                        edgeBackGestureHandler.mPipExcludedBounds.set(rect3);
                    }
                });
                return;
            case 2:
                ((StatusBar) obj).wakeUpIfDozing(SystemClock.uptimeMillis(), (View) this.f$0, "NOTIFICATION_CLICK");
                return;
            case 3:
                BiometricUnlockController biometricUnlockController = (BiometricUnlockController) this.f$0;
                UiEventLoggerImpl uiEventLoggerImpl = BiometricUnlockController.UI_EVENT_LOGGER;
                Objects.requireNonNull(biometricUnlockController);
                UiEventLoggerImpl uiEventLoggerImpl2 = BiometricUnlockController.UI_EVENT_LOGGER;
                SessionTracker sessionTracker = biometricUnlockController.mSessionTracker;
                Objects.requireNonNull(sessionTracker);
                uiEventLoggerImpl2.log((BiometricUnlockController.BiometricUiEvent) obj, (InstanceId) sessionTracker.mSessionToInstanceId.getOrDefault(1, null));
                return;
            case 4:
                ThresholdSensorEvent thresholdSensorEvent = (ThresholdSensorEvent) this.f$0;
                Consumer consumer = (Consumer) obj;
                if (thresholdSensorEvent != null) {
                    bool = Boolean.valueOf(thresholdSensorEvent.mBelow);
                }
                consumer.accept(bool);
                return;
            case 5:
                RecentTasksController.IRecentTasksImpl iRecentTasksImpl = (RecentTasksController.IRecentTasksImpl) this.f$0;
                RecentTasksController recentTasksController = (RecentTasksController) obj;
                int i2 = RecentTasksController.IRecentTasksImpl.$r8$clinit;
                Objects.requireNonNull(iRecentTasksImpl);
                RecentTasksController.IRecentTasksImpl.AnonymousClass1 r3 = iRecentTasksImpl.mRecentTasksListener;
                Objects.requireNonNull(recentTasksController);
                recentTasksController.mCallbacks.remove(r3);
                return;
            default:
                GestureConfiguration gestureConfiguration = (GestureConfiguration) this.f$0;
                Adjustment adjustment = (Adjustment) obj;
                Range<Float> range = GestureConfiguration.SENSITIVITY_RANGE;
                Objects.requireNonNull(gestureConfiguration);
                BubbleController$$ExternalSyntheticLambda9 bubbleController$$ExternalSyntheticLambda9 = gestureConfiguration.mAdjustmentCallback;
                Objects.requireNonNull(adjustment);
                adjustment.mCallback = bubbleController$$ExternalSyntheticLambda9;
                return;
        }
    }
}
