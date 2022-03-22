package com.android.systemui;

import android.app.ActivityOptions;
import com.android.internal.app.LocalePicker;
import com.android.keyguard.KeyguardPatternView$$ExternalSyntheticLambda0;
import com.android.settingslib.wifi.WifiTracker;
import com.android.systemui.hdmi.HdmiCecSetMenuLanguageHelper;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QSTileHost;
import com.android.systemui.qs.customize.TileQueryHelper;
import com.android.systemui.qs.customize.TileQueryHelper$$ExternalSyntheticLambda0;
import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.statusbar.NotificationViewHierarchyManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.wifitrackerlib.WifiPickerTracker;
import com.android.wm.shell.bubbles.BubbleStackView;
import java.util.ArrayList;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenDecorations$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ScreenDecorations$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ScreenDecorations screenDecorations = (ScreenDecorations) this.f$0;
                boolean z = ScreenDecorations.DEBUG_DISABLE_SCREEN_DECORATIONS;
                Objects.requireNonNull(screenDecorations);
                screenDecorations.mPendingRotationChange = false;
                screenDecorations.updateOrientation();
                screenDecorations.updateRoundedCornerRadii();
                screenDecorations.setupDecorations();
                if (screenDecorations.mOverlays != null) {
                    screenDecorations.updateLayoutParams();
                    return;
                }
                return;
            case 1:
                ((WifiTracker.WifiListener) this.f$0).onConnectedChanged();
                return;
            case 2:
                HdmiCecSetMenuLanguageHelper hdmiCecSetMenuLanguageHelper = (HdmiCecSetMenuLanguageHelper) this.f$0;
                Objects.requireNonNull(hdmiCecSetMenuLanguageHelper);
                LocalePicker.updateLocale(hdmiCecSetMenuLanguageHelper.mLocale);
                return;
            case 3:
                TileQueryHelper.TileCollector tileCollector = (TileQueryHelper.TileCollector) this.f$0;
                Objects.requireNonNull(tileCollector);
                TileQueryHelper tileQueryHelper = TileQueryHelper.this;
                Objects.requireNonNull(tileQueryHelper);
                tileQueryHelper.mMainExecutor.execute(new TileQueryHelper$$ExternalSyntheticLambda0(tileQueryHelper, new ArrayList(tileQueryHelper.mTiles), false));
                TileQueryHelper tileQueryHelper2 = TileQueryHelper.this;
                QSTileHost qSTileHost = tileCollector.mQSTileHost;
                Objects.requireNonNull(tileQueryHelper2);
                tileQueryHelper2.mBgExecutor.execute(new KeyguardPatternView$$ExternalSyntheticLambda0(tileQueryHelper2, qSTileHost, 3));
                return;
            case 4:
                ScreenshotController screenshotController = (ScreenshotController) this.f$0;
                ScreenshotController.AnonymousClass1 r0 = ScreenshotController.SCREENSHOT_REMOTE_RUNNER;
                Objects.requireNonNull(screenshotController);
                ActivityOptions.stopSharedElementAnimation(screenshotController.mWindow);
                return;
            case 5:
                NotificationViewHierarchyManager notificationViewHierarchyManager = (NotificationViewHierarchyManager) this.f$0;
                Objects.requireNonNull(notificationViewHierarchyManager);
                notificationViewHierarchyManager.mIsHandleDynamicPrivacyChangeScheduled = false;
                notificationViewHierarchyManager.updateNotificationViews();
                return;
            case FalsingManager.VERSION /* 6 */:
                StatusBar statusBar = (StatusBar) this.f$0;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.mShadeController.animateCollapsePanels$1(0);
                return;
            case 7:
                Objects.requireNonNull((WifiPickerTracker.WifiPickerTrackerCallback) this.f$0);
                return;
            default:
                BubbleStackView bubbleStackView = (BubbleStackView) this.f$0;
                int i = BubbleStackView.FLYOUT_HIDE_AFTER;
                Objects.requireNonNull(bubbleStackView);
                bubbleStackView.updatePointerPosition(false);
                bubbleStackView.mIsExpansionAnimating = false;
                bubbleStackView.updateExpandedView();
                bubbleStackView.requestUpdate();
                return;
        }
    }
}
