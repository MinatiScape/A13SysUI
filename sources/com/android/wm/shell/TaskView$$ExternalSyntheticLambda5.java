package com.android.wm.shell;

import android.util.Log;
import android.view.View;
import android.window.RemoteTransition;
import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.biometrics.AuthBiometricView;
import com.android.systemui.classifier.FalsingDataProvider;
import com.android.systemui.classifier.TimeLimitedMotionEventBuffer;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.SensorPrivacyToggleTile;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.compatui.letterboxedu.LetterboxEduWindowManager;
import com.android.wm.shell.transition.RemoteTransitionHandler;
import com.google.android.systemui.dreamliner.DockGestureController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class TaskView$$ExternalSyntheticLambda5 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ TaskView$$ExternalSyntheticLambda5(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                TaskView taskView = (TaskView) this.f$0;
                int i = TaskView.$r8$clinit;
                Objects.requireNonNull(taskView);
                if (taskView.mTaskToken != null) {
                    if (taskView.isUsingShellTransitions()) {
                        taskView.mTaskViewTransitions.setTaskViewVisible(taskView, false);
                        return;
                    }
                    taskView.mTransaction.reparent(taskView.mTaskLeash, null).apply();
                    taskView.updateTaskVisibility();
                    return;
                }
                return;
            case 1:
                KeyguardUpdateMonitor keyguardUpdateMonitor = (KeyguardUpdateMonitor) this.f$0;
                boolean z = KeyguardUpdateMonitor.DEBUG;
                Objects.requireNonNull(keyguardUpdateMonitor);
                Log.e("KeyguardUpdateMonitor", "Fp cancellation not received, transitioning to STOPPED");
                keyguardUpdateMonitor.mFingerprintRunningState = 0;
                keyguardUpdateMonitor.updateFingerprintListeningState(1);
                return;
            case 2:
                AuthBiometricView.$r8$lambda$VegE5yOsiHWU0vJWzcmzXwUISuc((AuthBiometricView) this.f$0);
                return;
            case 3:
                FalsingDataProvider falsingDataProvider = (FalsingDataProvider) this.f$0;
                Objects.requireNonNull(falsingDataProvider);
                if (!falsingDataProvider.mRecentMotionEvents.isEmpty()) {
                    TimeLimitedMotionEventBuffer timeLimitedMotionEventBuffer = falsingDataProvider.mRecentMotionEvents;
                    int actionMasked = timeLimitedMotionEventBuffer.mMotionEvents.get(timeLimitedMotionEventBuffer.size() - 1).getActionMasked();
                    if (actionMasked == 1 || actionMasked == 3) {
                        falsingDataProvider.completePriorGesture();
                        return;
                    }
                    return;
                }
                return;
            case 4:
                SensorPrivacyToggleTile sensorPrivacyToggleTile = (SensorPrivacyToggleTile) this.f$0;
                int i2 = SensorPrivacyToggleTile.$r8$clinit;
                Objects.requireNonNull(sensorPrivacyToggleTile);
                sensorPrivacyToggleTile.mSensorPrivacyController.setSensorBlocked(1, sensorPrivacyToggleTile.getSensorId(), !sensorPrivacyToggleTile.mSensorPrivacyController.isSensorBlocked(sensorPrivacyToggleTile.getSensorId()));
                return;
            case 5:
                BubbleStackView.m152$$Nest$mdismissMagnetizedObject((BubbleStackView) this.f$0);
                return;
            case FalsingManager.VERSION /* 6 */:
                LetterboxEduWindowManager.$r8$lambda$dlwl1DggfpDk0GTTamsPQUWfcQI((LetterboxEduWindowManager) this.f$0);
                return;
            case 7:
                RemoteTransitionHandler.RemoteDeathHandler remoteDeathHandler = (RemoteTransitionHandler.RemoteDeathHandler) this.f$0;
                Objects.requireNonNull(remoteDeathHandler);
                int size = RemoteTransitionHandler.this.mFilters.size();
                while (true) {
                    size--;
                    if (size < 0) {
                        int size2 = RemoteTransitionHandler.this.mRequestedRemotes.size();
                        while (true) {
                            size2--;
                            if (size2 < 0) {
                                int size3 = remoteDeathHandler.mPendingFinishCallbacks.size();
                                while (true) {
                                    size3--;
                                    if (size3 >= 0) {
                                        remoteDeathHandler.mPendingFinishCallbacks.get(size3).onTransitionFinished(null);
                                    } else {
                                        remoteDeathHandler.mPendingFinishCallbacks.clear();
                                        return;
                                    }
                                }
                            } else if (remoteDeathHandler.mRemote.equals(RemoteTransitionHandler.this.mRequestedRemotes.valueAt(size2).asBinder())) {
                                RemoteTransitionHandler.this.mRequestedRemotes.removeAt(size2);
                            }
                        }
                    } else if (remoteDeathHandler.mRemote.equals(((RemoteTransition) RemoteTransitionHandler.this.mFilters.get(size).second).asBinder())) {
                        RemoteTransitionHandler.this.mFilters.remove(size);
                    }
                }
            default:
                DockGestureController dockGestureController = (DockGestureController) this.f$0;
                int i3 = DockGestureController.$r8$clinit;
                Objects.requireNonNull(dockGestureController);
                PhysicsAnimator<View> physicsAnimator = dockGestureController.mPreviewTargetAnimator;
                physicsAnimator.spring(DynamicAnimation.TRANSLATION_X, 0.0f, dockGestureController.mVelocityX, dockGestureController.mTargetSpringConfig);
                physicsAnimator.start();
                return;
        }
    }
}
