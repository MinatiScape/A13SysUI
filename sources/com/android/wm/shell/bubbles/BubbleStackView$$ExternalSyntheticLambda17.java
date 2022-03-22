package com.android.wm.shell.bubbles;

import android.content.Intent;
import android.os.SystemClock;
import android.os.UserHandle;
import android.util.Log;
import android.view.ScrollCaptureResponse;
import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.internal.accessibility.dialog.AccessibilityButtonChooserActivity;
import com.android.systemui.accessibility.WindowMagnificationController;
import com.android.systemui.biometrics.AuthBiometricView;
import com.android.systemui.biometrics.Utils;
import com.android.systemui.clipboardoverlay.ClipboardOverlayController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.screenshot.ScrollCaptureController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowViewController;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.util.concurrency.RepeatableExecutorImpl;
import com.android.wm.shell.TaskView;
import com.android.wm.shell.draganddrop.DragAndDropController;
import com.android.wm.shell.draganddrop.DragLayout;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class BubbleStackView$$ExternalSyntheticLambda17 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ BubbleStackView$$ExternalSyntheticLambda17(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                Objects.requireNonNull(bubbleStackView);
                bubbleStackView.mExpandedViewContainer.setAnimationMatrix(null);
                bubbleStackView.mIsExpansionAnimating = false;
                bubbleStackView.updateExpandedView();
                bubbleStackView.requestUpdate();
                BubbleViewProvider bubbleViewProvider = bubbleStackView.mExpandedBubble;
                if (!(bubbleViewProvider == null || bubbleViewProvider.getExpandedView() == null)) {
                    BubbleExpandedView expandedView = bubbleStackView.mExpandedBubble.getExpandedView();
                    Objects.requireNonNull(expandedView);
                    TaskView taskView = expandedView.mTaskView;
                    if (taskView != null) {
                        taskView.setZOrderedOnTop(false, true);
                        return;
                    }
                    return;
                }
                return;
            case 1:
                WindowMagnificationController windowMagnificationController = (WindowMagnificationController) this.f$0;
                boolean z = WindowMagnificationController.DEBUG;
                Objects.requireNonNull(windowMagnificationController);
                if (windowMagnificationController.isWindowVisible()) {
                    windowMagnificationController.mMirrorView.setStateDescription(windowMagnificationController.formatStateDescription(windowMagnificationController.mScale));
                    return;
                }
                return;
            case 2:
                AuthBiometricView authBiometricView = (AuthBiometricView) this.f$0;
                int i = AuthBiometricView.$r8$clinit;
                Objects.requireNonNull(authBiometricView);
                authBiometricView.updateState(2);
                authBiometricView.handleResetAfterHelp();
                Utils.notifyAccessibilityContentChanged(authBiometricView.mAccessibilityManager, authBiometricView);
                return;
            case 3:
                ((ClipboardOverlayController) this.f$0).animateOut();
                return;
            case 4:
                OverviewProxyService.AnonymousClass1 r6 = (OverviewProxyService.AnonymousClass1) this.f$0;
                int i2 = OverviewProxyService.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r6);
                Intent intent = new Intent("com.android.internal.intent.action.CHOOSE_ACCESSIBILITY_BUTTON");
                intent.setClassName(ThemeOverlayApplier.ANDROID_PACKAGE, AccessibilityButtonChooserActivity.class.getName());
                intent.addFlags(268468224);
                OverviewProxyService.this.mContext.startActivityAsUser(intent, UserHandle.CURRENT);
                return;
            case 5:
                ScreenshotController screenshotController = (ScreenshotController) this.f$0;
                ScreenshotController.AnonymousClass1 r0 = ScreenshotController.SCREENSHOT_REMOTE_RUNNER;
                Objects.requireNonNull(screenshotController);
                CallbackToFutureAdapter.SafeFuture safeFuture = screenshotController.mLastScrollCaptureRequest;
                try {
                    try {
                        ScrollCaptureResponse scrollCaptureResponse = screenshotController.mLastScrollCaptureResponse;
                        if (scrollCaptureResponse != null) {
                            scrollCaptureResponse.close();
                        }
                        ScrollCaptureResponse scrollCaptureResponse2 = (ScrollCaptureResponse) safeFuture.get();
                        screenshotController.mLastScrollCaptureResponse = scrollCaptureResponse2;
                        if (!scrollCaptureResponse2.isConnected()) {
                            Log.d("Screenshot", "ScrollCapture: " + screenshotController.mLastScrollCaptureResponse.getDescription() + " [" + screenshotController.mLastScrollCaptureResponse.getWindowTitle() + "]");
                            return;
                        }
                        Log.d("Screenshot", "ScrollCapture: connected to window [" + screenshotController.mLastScrollCaptureResponse.getWindowTitle() + "]");
                        ScrollCaptureResponse scrollCaptureResponse3 = screenshotController.mLastScrollCaptureResponse;
                        screenshotController.mScreenshotView.showScrollChip(scrollCaptureResponse3.getPackageName(), new BubbleController$$ExternalSyntheticLambda5(screenshotController, scrollCaptureResponse3, 1));
                        return;
                    } catch (InterruptedException | ExecutionException e) {
                        Log.e("Screenshot", "requestScrollCapture failed", e);
                        return;
                    }
                } catch (CancellationException unused) {
                    return;
                }
            case FalsingManager.VERSION /* 6 */:
                ScrollCaptureController scrollCaptureController = (ScrollCaptureController) this.f$0;
                Objects.requireNonNull(scrollCaptureController);
                try {
                    scrollCaptureController.onCaptureResult(scrollCaptureController.mTileFuture.get());
                    return;
                } catch (InterruptedException | ExecutionException e2) {
                    Log.e("Screenshot", "requestTile failed!", e2);
                    scrollCaptureController.mCaptureCompleter.setException(e2);
                    return;
                } catch (CancellationException unused2) {
                    Log.e("Screenshot", "requestTile cancelled");
                    return;
                }
            case 7:
                NotificationShadeWindowViewController.AnonymousClass2 r62 = (NotificationShadeWindowViewController.AnonymousClass2) this.f$0;
                Objects.requireNonNull(r62);
                NotificationShadeWindowViewController.this.mService.wakeUpIfDozing(SystemClock.uptimeMillis(), NotificationShadeWindowViewController.this.mView, "LOCK_ICON_TOUCH");
                return;
            case 8:
                RepeatableExecutorImpl.ExecutionToken executionToken = (RepeatableExecutorImpl.ExecutionToken) this.f$0;
                Objects.requireNonNull(executionToken);
                synchronized (executionToken.mLock) {
                    Runnable runnable = executionToken.mCancel;
                    if (runnable != null) {
                        runnable.run();
                        executionToken.mCancel = null;
                    }
                }
                return;
            default:
                DragAndDropController.DragAndDropImpl dragAndDropImpl = (DragAndDropController.DragAndDropImpl) this.f$0;
                Objects.requireNonNull(dragAndDropImpl);
                DragAndDropController dragAndDropController = DragAndDropController.this;
                Objects.requireNonNull(dragAndDropController);
                for (int i3 = 0; i3 < dragAndDropController.mDisplayDropTargets.size(); i3++) {
                    DragLayout dragLayout = dragAndDropController.mDisplayDropTargets.get(i3).dragLayout;
                    Objects.requireNonNull(dragLayout);
                    dragLayout.mDropZoneView1.onThemeChange();
                    dragLayout.mDropZoneView2.onThemeChange();
                }
                return;
        }
    }
}
