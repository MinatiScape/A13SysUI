package com.android.systemui.statusbar.phone;

import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.window.StartingWindowRemovalInfo;
import androidx.lifecycle.Lifecycle;
import com.android.internal.policy.PhoneWindow;
import com.android.systemui.dreams.DreamOverlayContainerViewController;
import com.android.systemui.dreams.DreamOverlayService;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.dreams.DreamOverlayStateController$$ExternalSyntheticLambda1;
import com.android.wm.shell.bubbles.Bubble$$ExternalSyntheticLambda1;
import com.android.wm.shell.startingsurface.StartingWindowController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda21 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda21(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                StatusBar statusBar = (StatusBar) this.f$0;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.mStatusBarStateController.setLeaveOpenOnKeyguardHide(true);
                statusBar.executeRunnableDismissingKeyguard(new StatusBar$$ExternalSyntheticLambda20(statusBar, (Runnable) this.f$1, 0), false, false, false);
                return;
            case 1:
                DreamOverlayService dreamOverlayService = (DreamOverlayService) this.f$0;
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) this.f$1;
                boolean z = DreamOverlayService.DEBUG;
                Objects.requireNonNull(dreamOverlayService);
                DreamOverlayStateController dreamOverlayStateController = dreamOverlayService.mStateController;
                boolean shouldShowComplications = dreamOverlayService.shouldShowComplications();
                Objects.requireNonNull(dreamOverlayStateController);
                dreamOverlayStateController.mExecutor.execute(new DreamOverlayStateController$$ExternalSyntheticLambda1(dreamOverlayStateController, shouldShowComplications, 0));
                PhoneWindow phoneWindow = new PhoneWindow(dreamOverlayService.mContext);
                dreamOverlayService.mWindow = phoneWindow;
                phoneWindow.setAttributes(layoutParams);
                dreamOverlayService.mWindow.setWindowManager(null, layoutParams.token, "DreamOverlay", true);
                dreamOverlayService.mWindow.setBackgroundDrawable(new ColorDrawable(0));
                dreamOverlayService.mWindow.clearFlags(Integer.MIN_VALUE);
                dreamOverlayService.mWindow.addFlags(8);
                dreamOverlayService.mWindow.requestFeature(1);
                dreamOverlayService.mWindow.getDecorView().getWindowInsetsController().hide(WindowInsets.Type.systemBars());
                dreamOverlayService.mWindow.setDecorFitsSystemWindows(false);
                if (DreamOverlayService.DEBUG) {
                    Log.d("DreamOverlayService", "adding overlay window to dream");
                }
                dreamOverlayService.mDreamOverlayContainerViewController.init();
                PhoneWindow phoneWindow2 = dreamOverlayService.mWindow;
                DreamOverlayContainerViewController dreamOverlayContainerViewController = dreamOverlayService.mDreamOverlayContainerViewController;
                Objects.requireNonNull(dreamOverlayContainerViewController);
                phoneWindow2.setContentView(dreamOverlayContainerViewController.mView);
                ((WindowManager) dreamOverlayService.mContext.getSystemService(WindowManager.class)).addView(dreamOverlayService.mWindow.getDecorView(), dreamOverlayService.mWindow.getAttributes());
                dreamOverlayService.mExecutor.execute(new Bubble$$ExternalSyntheticLambda1(dreamOverlayService, Lifecycle.State.RESUMED, 1));
                dreamOverlayService.mStateController.setOverlayActive(true);
                return;
            default:
                StartingWindowController startingWindowController = (StartingWindowController) this.f$0;
                StartingWindowRemovalInfo startingWindowRemovalInfo = (StartingWindowRemovalInfo) this.f$1;
                Objects.requireNonNull(startingWindowController);
                synchronized (startingWindowController.mTaskBackgroundColors) {
                    startingWindowController.mTaskBackgroundColors.delete(startingWindowRemovalInfo.taskId);
                }
                return;
        }
    }
}
