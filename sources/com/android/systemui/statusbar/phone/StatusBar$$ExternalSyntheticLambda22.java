package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.os.SystemClock;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.shared.plugins.PluginActionManager;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda1;
import com.android.systemui.util.service.ObservableServiceConnection;
import com.android.wifitrackerlib.WifiPickerTracker$$ExternalSyntheticLambda13;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda22 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda22(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                StatusBar statusBar = (StatusBar) this.f$0;
                Runnable runnable = (Runnable) this.f$1;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.mKeyguardStateController.setLaunchTransitionFadingAway(true);
                if (runnable != null) {
                    runnable.run();
                }
                statusBar.updateScrimController();
                statusBar.mPresenter.updateMediaMetaData(false, true);
                NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
                Objects.requireNonNull(notificationPanelViewController);
                notificationPanelViewController.mView.setAlpha(1.0f);
                statusBar.mNotificationPanelViewController.fadeOut(100L, 300L, new CreateUserActivity$$ExternalSyntheticLambda1(statusBar, 4));
                statusBar.mCommandQueue.appTransitionStarting(statusBar.mDisplayId, SystemClock.uptimeMillis(), 120L, true);
                return;
            case 1:
                final ScreenshotController screenshotController = (ScreenshotController) this.f$0;
                final ScreenshotController.QuickShareData quickShareData = (ScreenshotController.QuickShareData) this.f$1;
                ScreenshotController.AnonymousClass1 r1 = ScreenshotController.SCREENSHOT_REMOTE_RUNNER;
                Objects.requireNonNull(screenshotController);
                AnimatorSet animatorSet = screenshotController.mScreenshotAnimation;
                if (animatorSet == null || !animatorSet.isRunning()) {
                    screenshotController.mScreenshotView.addQuickShareChip(quickShareData.quickShareAction);
                    return;
                } else {
                    screenshotController.mScreenshotAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.screenshot.ScreenshotController.8
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationEnd(Animator animator) {
                            super.onAnimationEnd(animator);
                            screenshotController.mScreenshotView.addQuickShareChip(quickShareData.quickShareAction);
                        }
                    });
                    return;
                }
            case 2:
                PluginActionManager pluginActionManager = (PluginActionManager) this.f$0;
                Objects.requireNonNull(pluginActionManager);
                pluginActionManager.removePkg((String) this.f$1);
                return;
            default:
                ObservableServiceConnection observableServiceConnection = (ObservableServiceConnection) this.f$0;
                boolean z = ObservableServiceConnection.DEBUG;
                Objects.requireNonNull(observableServiceConnection);
                observableServiceConnection.mCallbacks.removeIf(new WifiPickerTracker$$ExternalSyntheticLambda13((ObservableServiceConnection.Callback) this.f$1, 1));
                return;
        }
    }
}
