package com.android.systemui.statusbar.phone;

import android.os.SystemClock;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import com.android.internal.annotations.VisibleForTesting;
import com.android.keyguard.LockIconViewController;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dock.DockManager;
import com.android.systemui.lowlightclock.LowLightClockController;
import com.android.systemui.statusbar.DragDownHelper;
import com.android.systemui.statusbar.LockscreenShadeTransitionController;
import com.android.systemui.statusbar.NotificationShadeDepthController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.window.StatusBarWindowStateController;
import com.android.systemui.tuner.TunerService;
import java.util.Optional;
/* loaded from: classes.dex */
public final class NotificationShadeWindowViewController {
    public View mBrightnessMirror;
    public final NotificationShadeDepthController mDepthController;
    public final DockManager mDockManager;
    public boolean mDoubleTapEnabled;
    public DragDownHelper mDragDownHelper;
    public boolean mExpandAnimationRunning;
    public boolean mExpandingBelowNotch;
    public final FalsingCollector mFalsingCollector;
    public GestureDetector mGestureDetector;
    public boolean mIsTrackingBarGesture = false;
    public final LockIconViewController mLockIconViewController;
    public final LockscreenShadeTransitionController mLockscreenShadeTransitionController;
    public final Optional<LowLightClockController> mLowLightClockController;
    public final NotificationPanelViewController mNotificationPanelViewController;
    public NotificationShadeWindowController mNotificationShadeWindowController;
    public final NotificationStackScrollLayoutController mNotificationStackScrollLayoutController;
    public final PanelExpansionStateManager mPanelExpansionStateManager;
    public StatusBar mService;
    public boolean mSingleTapEnabled;
    public NotificationStackScrollLayout mStackScrollLayout;
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public PhoneStatusBarViewController mStatusBarViewController;
    public final StatusBarWindowStateController mStatusBarWindowStateController;
    public boolean mTouchActive;
    public boolean mTouchCancelled;
    public final TunerService mTunerService;
    public final NotificationShadeWindowView mView;

    /* renamed from: com.android.systemui.statusbar.phone.NotificationShadeWindowViewController$2  reason: invalid class name */
    /* loaded from: classes.dex */
    public final class AnonymousClass2 implements NotificationShadeWindowView.InteractionEventHandler {
        public AnonymousClass2() {
        }
    }

    public final void cancelCurrentTouch() {
        if (this.mTouchActive) {
            long uptimeMillis = SystemClock.uptimeMillis();
            MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
            obtain.setSource(4098);
            this.mView.dispatchTouchEvent(obtain);
            obtain.recycle();
            this.mTouchCancelled = true;
        }
    }

    public NotificationShadeWindowViewController(LockscreenShadeTransitionController lockscreenShadeTransitionController, FalsingCollector falsingCollector, TunerService tunerService, SysuiStatusBarStateController sysuiStatusBarStateController, DockManager dockManager, NotificationShadeDepthController notificationShadeDepthController, NotificationShadeWindowView notificationShadeWindowView, NotificationPanelViewController notificationPanelViewController, PanelExpansionStateManager panelExpansionStateManager, NotificationStackScrollLayoutController notificationStackScrollLayoutController, StatusBarKeyguardViewManager statusBarKeyguardViewManager, StatusBarWindowStateController statusBarWindowStateController, LockIconViewController lockIconViewController, Optional<LowLightClockController> optional) {
        this.mLockscreenShadeTransitionController = lockscreenShadeTransitionController;
        this.mFalsingCollector = falsingCollector;
        this.mTunerService = tunerService;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mView = notificationShadeWindowView;
        this.mDockManager = dockManager;
        this.mNotificationPanelViewController = notificationPanelViewController;
        this.mPanelExpansionStateManager = panelExpansionStateManager;
        this.mDepthController = notificationShadeDepthController;
        this.mNotificationStackScrollLayoutController = notificationStackScrollLayoutController;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mStatusBarWindowStateController = statusBarWindowStateController;
        this.mLockIconViewController = lockIconViewController;
        this.mLowLightClockController = optional;
        this.mBrightnessMirror = notificationShadeWindowView.findViewById(2131427604);
    }

    @VisibleForTesting
    public void setDragDownHelper(DragDownHelper dragDownHelper) {
        this.mDragDownHelper = dragDownHelper;
    }
}
