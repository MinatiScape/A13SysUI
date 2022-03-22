package com.android.wm.shell.onehanded;

import android.graphics.Insets;
import android.os.RemoteException;
import android.view.DisplayCutout;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.systemui.accessibility.MagnificationModeSwitch;
import com.android.systemui.clipboardoverlay.ClipboardOverlayController;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QSFgsManagerFooter$$ExternalSyntheticLambda0;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.screenshot.ScrollCaptureClient;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.wm.shell.legacysplitscreen.ForcedResizableInfoActivityController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.google.android.systemui.assist.uihints.GlowView;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class OneHandedController$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ OneHandedController$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ((OneHandedController) this.f$0).onActivatedActionChanged();
                return;
            case 1:
                MagnificationModeSwitch magnificationModeSwitch = (MagnificationModeSwitch) this.f$0;
                Objects.requireNonNull(magnificationModeSwitch);
                magnificationModeSwitch.mImageView.animate().alpha(1.0f).setDuration(300L).start();
                return;
            case 2:
                ClipboardOverlayController clipboardOverlayController = (ClipboardOverlayController) this.f$0;
                Objects.requireNonNull(clipboardOverlayController);
                clipboardOverlayController.mWindow.setContentView(clipboardOverlayController.mContainer);
                WindowInsets windowInsets = clipboardOverlayController.mWindowManager.getCurrentWindowMetrics().getWindowInsets();
                int i = clipboardOverlayController.mContext.getResources().getConfiguration().orientation;
                FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) clipboardOverlayController.mView.getLayoutParams();
                if (layoutParams != null) {
                    DisplayCutout displayCutout = windowInsets.getDisplayCutout();
                    Insets insets = windowInsets.getInsets(WindowInsets.Type.navigationBars());
                    if (displayCutout == null) {
                        layoutParams.setMargins(0, 0, 0, insets.bottom);
                    } else {
                        Insets waterfallInsets = displayCutout.getWaterfallInsets();
                        if (i == 1) {
                            layoutParams.setMargins(waterfallInsets.left, Math.max(displayCutout.getSafeInsetTop(), waterfallInsets.top), waterfallInsets.right, Math.max(displayCutout.getSafeInsetBottom(), Math.max(insets.bottom, waterfallInsets.bottom)));
                        } else {
                            layoutParams.setMargins(Math.max(displayCutout.getSafeInsetLeft(), waterfallInsets.left), waterfallInsets.top, Math.max(displayCutout.getSafeInsetRight(), waterfallInsets.right), Math.max(insets.bottom, waterfallInsets.bottom));
                        }
                    }
                    clipboardOverlayController.mView.setLayoutParams(layoutParams);
                    clipboardOverlayController.mView.requestLayout();
                }
                clipboardOverlayController.mView.requestLayout();
                clipboardOverlayController.mView.post(new QSFgsManagerFooter$$ExternalSyntheticLambda0(clipboardOverlayController, 2));
                return;
            case 3:
                OverviewProxyService.AnonymousClass1 r8 = (OverviewProxyService.AnonymousClass1) this.f$0;
                int i2 = OverviewProxyService.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r8);
                OverviewProxyService overviewProxyService = OverviewProxyService.this;
                Objects.requireNonNull(overviewProxyService);
                int size = overviewProxyService.mConnectionCallbacks.size();
                while (true) {
                    size--;
                    if (size >= 0) {
                        ((OverviewProxyService.OverviewProxyListener) overviewProxyService.mConnectionCallbacks.get(size)).onSwipeUpGestureStarted();
                    } else {
                        return;
                    }
                }
            case 4:
                ScrollCaptureClient.SessionWrapper sessionWrapper = (ScrollCaptureClient.SessionWrapper) this.f$0;
                int i3 = ScrollCaptureClient.SessionWrapper.$r8$clinit;
                Objects.requireNonNull(sessionWrapper);
                try {
                    sessionWrapper.mCancellationSignal.cancel();
                    return;
                } catch (RemoteException unused) {
                    return;
                }
            case 5:
                NotificationStackScrollLayout notificationStackScrollLayout = (NotificationStackScrollLayout) this.f$0;
                boolean z = NotificationStackScrollLayout.SPEW;
                Objects.requireNonNull(notificationStackScrollLayout);
                notificationStackScrollLayout.mFlingAfterUpEvent = false;
                InteractionJankMonitor.getInstance().end(2);
                notificationStackScrollLayout.mFinishScrollingCallback = null;
                return;
            case FalsingManager.VERSION /* 6 */:
                LegacySplitScreenController.SplitScreenImpl splitScreenImpl = (LegacySplitScreenController.SplitScreenImpl) this.f$0;
                Objects.requireNonNull(splitScreenImpl);
                LegacySplitScreenController legacySplitScreenController = LegacySplitScreenController.this;
                Objects.requireNonNull(legacySplitScreenController);
                if (legacySplitScreenController.mView != null) {
                    ForcedResizableInfoActivityController forcedResizableInfoActivityController = legacySplitScreenController.mForcedResizableController;
                    Objects.requireNonNull(forcedResizableInfoActivityController);
                    if (!forcedResizableInfoActivityController.mDividerDragging) {
                        forcedResizableInfoActivityController.showPending();
                        return;
                    }
                    return;
                }
                return;
            default:
                GlowView glowView = (GlowView) this.f$0;
                int i4 = GlowView.$r8$clinit;
                Objects.requireNonNull(glowView);
                glowView.setGlowsY(glowView.mTranslationY, glowView.mMinimumHeightPx, glowView.mEdgeLights);
                return;
        }
    }
}
