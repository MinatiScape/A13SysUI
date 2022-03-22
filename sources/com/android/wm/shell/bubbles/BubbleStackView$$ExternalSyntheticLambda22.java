package com.android.wm.shell.bubbles;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.content.ComponentName;
import android.service.quickaccesswallet.QuickAccessWalletClient;
import android.view.Choreographer;
import android.view.SurfaceControl;
import android.widget.Toast;
import android.window.WindowContainerTransaction;
import com.android.systemui.wallet.controller.QuickAccessWalletController;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.legacysplitscreen.WindowManagerProxy;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda22 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda22(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        ActivityManager.RunningTaskInfo runningTaskInfo;
        int activityType;
        switch (this.$r8$classId) {
            case 0:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                Consumer consumer = (Consumer) this.f$1;
                Objects.requireNonNull(bubbleStackView);
                SurfaceControl.ScreenshotHardwareBuffer screenshotHardwareBuffer = bubbleStackView.mAnimatingOutBubbleBuffer;
                if (screenshotHardwareBuffer == null || screenshotHardwareBuffer.getHardwareBuffer() == null || bubbleStackView.mAnimatingOutBubbleBuffer.getHardwareBuffer().isClosed()) {
                    consumer.accept(Boolean.FALSE);
                    return;
                } else if (!bubbleStackView.mIsExpanded || !bubbleStackView.mAnimatingOutSurfaceReady) {
                    consumer.accept(Boolean.FALSE);
                    return;
                } else {
                    bubbleStackView.mAnimatingOutSurfaceView.getHolder().getSurface().attachAndQueueBufferWithColorSpace(bubbleStackView.mAnimatingOutBubbleBuffer.getHardwareBuffer(), bubbleStackView.mAnimatingOutBubbleBuffer.getColorSpace());
                    bubbleStackView.mAnimatingOutSurfaceView.setAlpha(1.0f);
                    bubbleStackView.mExpandedViewContainer.setVisibility(8);
                    BubbleStackView.SurfaceSynchronizer surfaceSynchronizer = bubbleStackView.mSurfaceSynchronizer;
                    BubbleStackView$$ExternalSyntheticLambda21 bubbleStackView$$ExternalSyntheticLambda21 = new BubbleStackView$$ExternalSyntheticLambda21(bubbleStackView, consumer, 0);
                    Objects.requireNonNull((BubbleStackView.AnonymousClass1) surfaceSynchronizer);
                    Choreographer.getInstance().postFrameCallback(new BubbleStackView.AnonymousClass1.Choreographer$FrameCallbackC00071(bubbleStackView$$ExternalSyntheticLambda21));
                    return;
                }
            case 1:
                QuickAccessWalletController.AnonymousClass1 r0 = (QuickAccessWalletController.AnonymousClass1) this.f$0;
                int i = QuickAccessWalletController.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r0);
                QuickAccessWalletController.this.reCreateWalletClient();
                QuickAccessWalletController.this.updateWalletPreference();
                QuickAccessWalletController.this.queryWalletCards((QuickAccessWalletClient.OnWalletCardsRetrievedCallback) this.f$1);
                return;
            case 2:
                LegacySplitScreenController.SplitScreenImpl splitScreenImpl = (LegacySplitScreenController.SplitScreenImpl) this.f$0;
                boolean[] zArr = (boolean[]) this.f$1;
                Objects.requireNonNull(splitScreenImpl);
                LegacySplitScreenController legacySplitScreenController = LegacySplitScreenController.this;
                Objects.requireNonNull(legacySplitScreenController);
                if (!(ActivityTaskManager.getService().getLockTaskModeState() == 2 || legacySplitScreenController.isSplitActive() || legacySplitScreenController.mSplits.mPrimary == null)) {
                    z = true;
                    List tasks = ActivityTaskManager.getInstance().getTasks(1);
                    if (!(tasks == null || tasks.isEmpty() || (activityType = (runningTaskInfo = (ActivityManager.RunningTaskInfo) tasks.get(0)).getActivityType()) == 2 || activityType == 3)) {
                        if (!runningTaskInfo.supportsSplitScreenMultiWindow) {
                            Toast.makeText(legacySplitScreenController.mContext, 2131952296, 0).show();
                        } else {
                            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                            windowContainerTransaction.setWindowingMode(runningTaskInfo.token, 0);
                            windowContainerTransaction.reparent(runningTaskInfo.token, legacySplitScreenController.mSplits.mPrimary.token, true);
                            WindowManagerProxy windowManagerProxy = legacySplitScreenController.mWindowManagerProxy;
                            Objects.requireNonNull(windowManagerProxy);
                            windowManagerProxy.mSyncTransactionQueue.queue(windowContainerTransaction);
                            zArr[0] = z;
                            return;
                        }
                    }
                }
                z = false;
                zArr[0] = z;
                return;
            default:
                PinnedStackListenerForwarder.PinnedTaskListenerImpl pinnedTaskListenerImpl = (PinnedStackListenerForwarder.PinnedTaskListenerImpl) this.f$0;
                ComponentName componentName = (ComponentName) this.f$1;
                int i2 = PinnedStackListenerForwarder.PinnedTaskListenerImpl.$r8$clinit;
                Objects.requireNonNull(pinnedTaskListenerImpl);
                PinnedStackListenerForwarder pinnedStackListenerForwarder = PinnedStackListenerForwarder.this;
                Objects.requireNonNull(pinnedStackListenerForwarder);
                Iterator<PinnedStackListenerForwarder.PinnedTaskListener> it = pinnedStackListenerForwarder.mListeners.iterator();
                while (it.hasNext()) {
                    it.next().onActivityHidden(componentName);
                }
                return;
        }
    }
}
