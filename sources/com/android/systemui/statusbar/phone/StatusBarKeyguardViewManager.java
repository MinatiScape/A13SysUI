package com.android.systemui.statusbar.phone;

import android.content.res.ColorStateList;
import android.hardware.biometrics.BiometricSourceType;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.os.Trace;
import android.util.Log;
import android.view.View;
import android.view.ViewRootImpl;
import android.view.WindowInsets;
import android.view.WindowManagerGlobal;
import androidx.leanback.R$color;
import com.android.internal.util.LatencyTracker;
import com.android.keyguard.CarrierTextManager$$ExternalSyntheticLambda0;
import com.android.keyguard.KeyguardHostView;
import com.android.keyguard.KeyguardHostViewController;
import com.android.keyguard.KeyguardMessageArea;
import com.android.keyguard.KeyguardMessageAreaController;
import com.android.keyguard.KeyguardSecurityContainer;
import com.android.keyguard.KeyguardSecurityContainerController;
import com.android.keyguard.KeyguardSecurityModel;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.keyguard.KeyguardViewController;
import com.android.keyguard.ViewMediatorCallback;
import com.android.systemui.DejankUtils;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.biometrics.UdfpsKeyguardView;
import com.android.systemui.biometrics.UdfpsKeyguardViewController;
import com.android.systemui.dock.DockManager;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.keyguard.DismissCallbackRegistry;
import com.android.systemui.keyguard.DismissCallbackWrapper;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager$$ExternalSyntheticLambda6;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.notification.ViewGroupFadeHelper;
import com.android.systemui.statusbar.phone.KeyguardBouncer;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.KeyguardQsUserSwitchController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.KeyguardUserSwitcherController;
import com.android.systemui.unfold.FoldAodAnimationController;
import com.android.systemui.util.Assert;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda6;
import dagger.Lazy;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class StatusBarKeyguardViewManager implements RemoteInputController.Callback, StatusBarStateController.StateListener, ConfigurationController.ConfigurationListener, PanelExpansionListener, NavigationModeController.ModeChangedListener, KeyguardViewController, FoldAodAnimationController.FoldAodAnimationStatus {
    public ActivityStarter.OnDismissAction mAfterKeyguardGoneAction;
    public AlternateAuthInterceptor mAlternateAuthInterceptor;
    public BiometricUnlockController mBiometricUnlockController;
    public KeyguardBouncer mBouncer;
    public KeyguardBypassController mBypassController;
    public final ConfigurationController mConfigurationController;
    public boolean mDismissActionWillAnimateOnKeyguard;
    public final DockManager mDockManager;
    public boolean mDozing;
    public final DreamOverlayStateController mDreamOverlayStateController;
    public final FoldAodAnimationController mFoldAodAnimationController;
    public boolean mGesturalNav;
    public boolean mIsDocked;
    public final KeyguardBouncer.Factory mKeyguardBouncerFactory;
    public Runnable mKeyguardGoneCancelAction;
    public KeyguardMessageAreaController mKeyguardMessageAreaController;
    public final KeyguardMessageAreaController.Factory mKeyguardMessageAreaFactory;
    public final KeyguardStateController mKeyguardStateController;
    public final KeyguardUpdateMonitor mKeyguardUpdateManager;
    public int mLastBiometricMode;
    public boolean mLastBouncerDismissible;
    public boolean mLastBouncerIsOrWillBeShowing;
    public boolean mLastBouncerShowing;
    public boolean mLastDozing;
    public boolean mLastGesturalNav;
    public boolean mLastIsDocked;
    public boolean mLastOccluded;
    public boolean mLastPulsing;
    public boolean mLastRemoteInputActive;
    public boolean mLastScreenOffAnimationPlaying;
    public boolean mLastShowing;
    public final LatencyTracker mLatencyTracker;
    public final NotificationMediaManager mMediaManager;
    public final NavigationModeController mNavigationModeController;
    public View mNotificationContainer;
    public NotificationPanelViewController mNotificationPanelViewController;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public boolean mOccluded;
    public DismissWithActionRequest mPendingWakeupAction;
    public boolean mPulsing;
    public boolean mQsExpanded;
    public boolean mRemoteInputActive;
    public boolean mScreenOffAnimationPlaying;
    public final Lazy<ShadeController> mShadeController;
    public boolean mShowing;
    public StatusBar mStatusBar;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public ViewMediatorCallback mViewMediatorCallback;
    public final AnonymousClass1 mExpansionCallback = new KeyguardBouncer.BouncerExpansionCallback() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.1
        @Override // com.android.systemui.statusbar.phone.KeyguardBouncer.BouncerExpansionCallback
        public final void onFullyHidden() {
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardBouncer.BouncerExpansionCallback
        public final void onExpansionChanged(float f) {
            AlternateAuthInterceptor alternateAuthInterceptor = StatusBarKeyguardViewManager.this.mAlternateAuthInterceptor;
            if (alternateAuthInterceptor != null) {
                UdfpsKeyguardViewController.AnonymousClass2 r0 = (UdfpsKeyguardViewController.AnonymousClass2) alternateAuthInterceptor;
                UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
                udfpsKeyguardViewController.mInputBouncerHiddenAmount = f;
                udfpsKeyguardViewController.updateAlpha();
                UdfpsKeyguardViewController.this.updatePauseAuth();
            }
            StatusBarKeyguardViewManager.this.updateStates();
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardBouncer.BouncerExpansionCallback
        public final void onFullyShown() {
            StatusBarKeyguardViewManager.this.updateStates();
            StatusBarKeyguardViewManager.this.mStatusBar.wakeUpIfDozing(SystemClock.uptimeMillis(), StatusBarKeyguardViewManager.this.mStatusBar.getBouncerContainer(), "BOUNCER_VISIBLE");
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardBouncer.BouncerExpansionCallback
        public final void onStartingToHide() {
            StatusBarKeyguardViewManager.this.updateStates();
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardBouncer.BouncerExpansionCallback
        public final void onStartingToShow() {
            StatusBarKeyguardViewManager.this.updateStates();
        }

        @Override // com.android.systemui.statusbar.phone.KeyguardBouncer.BouncerExpansionCallback
        public final void onVisibilityChanged(boolean z) {
            if (!z) {
                StatusBarKeyguardViewManager statusBarKeyguardViewManager = StatusBarKeyguardViewManager.this;
                Objects.requireNonNull(statusBarKeyguardViewManager);
                if (!statusBarKeyguardViewManager.bouncerIsOrWillBeShowing()) {
                    statusBarKeyguardViewManager.mAfterKeyguardGoneAction = null;
                    statusBarKeyguardViewManager.mDismissActionWillAnimateOnKeyguard = false;
                    Runnable runnable = statusBarKeyguardViewManager.mKeyguardGoneCancelAction;
                    if (runnable != null) {
                        runnable.run();
                        statusBarKeyguardViewManager.mKeyguardGoneCancelAction = null;
                    }
                }
            }
            AlternateAuthInterceptor alternateAuthInterceptor = StatusBarKeyguardViewManager.this.mAlternateAuthInterceptor;
            if (alternateAuthInterceptor != null) {
                UdfpsKeyguardViewController.AnonymousClass2 r2 = (UdfpsKeyguardViewController.AnonymousClass2) alternateAuthInterceptor;
                UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
                udfpsKeyguardViewController.mIsBouncerVisible = udfpsKeyguardViewController.mKeyguardViewManager.isBouncerShowing();
                UdfpsKeyguardViewController udfpsKeyguardViewController2 = UdfpsKeyguardViewController.this;
                if (!udfpsKeyguardViewController2.mIsBouncerVisible) {
                    udfpsKeyguardViewController2.mInputBouncerHiddenAmount = 1.0f;
                } else if (udfpsKeyguardViewController2.mKeyguardViewManager.isBouncerShowing()) {
                    UdfpsKeyguardViewController.this.mInputBouncerHiddenAmount = 0.0f;
                }
                UdfpsKeyguardViewController.this.updateAlpha();
                UdfpsKeyguardViewController.this.updatePauseAuth();
            }
        }
    };
    public final AnonymousClass2 mDockEventListener = new DockManager.DockEventListener() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.2
        @Override // com.android.systemui.dock.DockManager.DockEventListener
        public final void onEvent(int i) {
            boolean isDocked = StatusBarKeyguardViewManager.this.mDockManager.isDocked();
            StatusBarKeyguardViewManager statusBarKeyguardViewManager = StatusBarKeyguardViewManager.this;
            if (isDocked != statusBarKeyguardViewManager.mIsDocked) {
                statusBarKeyguardViewManager.mIsDocked = isDocked;
                statusBarKeyguardViewManager.updateStates();
            }
        }
    };
    public boolean mGlobalActionsVisible = false;
    public boolean mLastGlobalActionsVisible = false;
    public boolean mFirstUpdate = true;
    public final ArrayList<Runnable> mAfterKeyguardGoneRunnables = new ArrayList<>();
    public final AnonymousClass3 mUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.3
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onEmergencyCallAction() {
            StatusBarKeyguardViewManager statusBarKeyguardViewManager = StatusBarKeyguardViewManager.this;
            if (statusBarKeyguardViewManager.mOccluded) {
                statusBarKeyguardViewManager.reset(true);
            }
        }
    };
    public AnonymousClass7 mMakeNavigationBarVisibleRunnable = new AnonymousClass7();

    /* renamed from: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$7  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass7 implements Runnable {
        public AnonymousClass7() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            NavigationBarView navigationBarView = StatusBarKeyguardViewManager.this.mStatusBar.getNavigationBarView();
            if (navigationBarView != null) {
                navigationBarView.setVisibility(0);
            }
            StatusBar statusBar = StatusBarKeyguardViewManager.this.mStatusBar;
            Objects.requireNonNull(statusBar);
            statusBar.mNotificationShadeWindowView.getWindowInsetsController().show(WindowInsets.Type.navigationBars());
        }
    }

    /* loaded from: classes.dex */
    public interface AlternateAuthInterceptor {
    }

    /* JADX WARN: Type inference failed for: r1v0, types: [com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$1] */
    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$2] */
    /* JADX WARN: Type inference failed for: r1v5, types: [com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$3] */
    public StatusBarKeyguardViewManager(ViewMediatorCallback viewMediatorCallback, SysuiStatusBarStateController sysuiStatusBarStateController, ConfigurationController configurationController, KeyguardUpdateMonitor keyguardUpdateMonitor, DreamOverlayStateController dreamOverlayStateController, NavigationModeController navigationModeController, DockManager dockManager, NotificationShadeWindowController notificationShadeWindowController, KeyguardStateController keyguardStateController, NotificationMediaManager notificationMediaManager, KeyguardBouncer.Factory factory, KeyguardMessageAreaController.Factory factory2, Optional optional, Lazy lazy, LatencyTracker latencyTracker) {
        this.mViewMediatorCallback = viewMediatorCallback;
        this.mConfigurationController = configurationController;
        this.mNavigationModeController = navigationModeController;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        this.mDreamOverlayStateController = dreamOverlayStateController;
        this.mKeyguardStateController = keyguardStateController;
        this.mMediaManager = notificationMediaManager;
        this.mKeyguardUpdateManager = keyguardUpdateMonitor;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mDockManager = dockManager;
        this.mKeyguardBouncerFactory = factory;
        this.mKeyguardMessageAreaFactory = factory2;
        this.mShadeController = lazy;
        this.mLatencyTracker = latencyTracker;
        this.mFoldAodAnimationController = (FoldAodAnimationController) optional.map(PeopleSpaceWidgetManager$$ExternalSyntheticLambda6.INSTANCE$3).orElse(null);
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void onCancelClicked() {
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onDensityOrFontScaleChanged() {
        hideBouncer(true);
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void showBouncer(boolean z) {
        resetAlternateAuth(false);
        if (this.mShowing && !this.mBouncer.isShowing()) {
            this.mBouncer.show(false, z);
        }
        updateStates();
    }

    /* loaded from: classes.dex */
    public static class DismissWithActionRequest {
        public final boolean afterKeyguardGone;
        public final Runnable cancelAction;
        public final ActivityStarter.OnDismissAction dismissAction;
        public final String message;

        public DismissWithActionRequest(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z, String str) {
            this.dismissAction = onDismissAction;
            this.cancelAction = runnable;
            this.afterKeyguardGone = z;
            this.message = str;
        }
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void blockPanelExpansionFromCurrentTouch() {
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        notificationPanelViewController.mBlockingExpansionForCurrentTouch = notificationPanelViewController.mTracking;
    }

    /* JADX WARN: Removed duplicated region for block: B:21:0x0031  */
    /* JADX WARN: Removed duplicated region for block: B:34:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:37:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:39:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean bouncerNeedsScrimming() {
        /*
            r4 = this;
            boolean r0 = r4.mOccluded
            r1 = 1
            r2 = 0
            if (r0 == 0) goto L_0x0015
            com.android.systemui.dreams.DreamOverlayStateController r0 = r4.mDreamOverlayStateController
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.mState
            r0 = r0 & r1
            if (r0 == 0) goto L_0x0012
            r0 = r1
            goto L_0x0013
        L_0x0012:
            r0 = r2
        L_0x0013:
            if (r0 == 0) goto L_0x0061
        L_0x0015:
            com.android.systemui.statusbar.phone.KeyguardBouncer r0 = r4.mBouncer
            java.util.Objects.requireNonNull(r0)
            com.android.keyguard.KeyguardHostViewController r0 = r0.mKeyguardViewController
            if (r0 == 0) goto L_0x002e
            com.android.systemui.plugins.ActivityStarter$OnDismissAction r3 = r0.mDismissAction
            if (r3 != 0) goto L_0x0029
            java.lang.Runnable r0 = r0.mCancelAction
            if (r0 == 0) goto L_0x0027
            goto L_0x0029
        L_0x0027:
            r0 = r2
            goto L_0x002a
        L_0x0029:
            r0 = r1
        L_0x002a:
            if (r0 == 0) goto L_0x002e
            r0 = r1
            goto L_0x002f
        L_0x002e:
            r0 = r2
        L_0x002f:
            if (r0 != 0) goto L_0x0061
            com.android.systemui.statusbar.phone.KeyguardBouncer r0 = r4.mBouncer
            boolean r0 = r0.isShowing()
            if (r0 == 0) goto L_0x0042
            com.android.systemui.statusbar.phone.KeyguardBouncer r0 = r4.mBouncer
            java.util.Objects.requireNonNull(r0)
            boolean r0 = r0.mIsScrimmed
            if (r0 != 0) goto L_0x0061
        L_0x0042:
            com.android.systemui.statusbar.phone.KeyguardBouncer r4 = r4.mBouncer
            java.util.Objects.requireNonNull(r4)
            com.android.keyguard.KeyguardHostViewController r4 = r4.mKeyguardViewController
            if (r4 == 0) goto L_0x005c
            com.android.keyguard.KeyguardSecurityContainerController r4 = r4.mKeyguardSecurityContainerController
            java.util.Objects.requireNonNull(r4)
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r4 = r4.mCurrentSecurityMode
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r0 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.SimPin
            if (r4 == r0) goto L_0x005a
            com.android.keyguard.KeyguardSecurityModel$SecurityMode r0 = com.android.keyguard.KeyguardSecurityModel.SecurityMode.SimPuk
            if (r4 != r0) goto L_0x005c
        L_0x005a:
            r4 = r1
            goto L_0x005d
        L_0x005c:
            r4 = r2
        L_0x005d:
            if (r4 == 0) goto L_0x0060
            goto L_0x0061
        L_0x0060:
            r1 = r2
        L_0x0061:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.bouncerNeedsScrimming():boolean");
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void dismissAndCollapse() {
        this.mStatusBar.executeRunnableDismissingKeyguard(null, true, false, true);
    }

    public final void dismissWithAction(ActivityStarter.OnDismissAction onDismissAction, Runnable runnable, boolean z, String str) {
        boolean z2;
        boolean z3;
        boolean z4;
        Runnable runnable2;
        if (this.mShowing) {
            DismissWithActionRequest dismissWithActionRequest = this.mPendingWakeupAction;
            this.mPendingWakeupAction = null;
            if (!(dismissWithActionRequest == null || (runnable2 = dismissWithActionRequest.cancelAction) == null)) {
                runnable2.run();
            }
            if (this.mDozing) {
                BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
                Objects.requireNonNull(biometricUnlockController);
                int i = biometricUnlockController.mMode;
                if (i == 1 || i == 2) {
                    z4 = true;
                } else {
                    z4 = false;
                }
                if (!z4) {
                    this.mPendingWakeupAction = new DismissWithActionRequest(onDismissAction, runnable, z, str);
                    return;
                }
            }
            this.mAfterKeyguardGoneAction = onDismissAction;
            this.mKeyguardGoneCancelAction = runnable;
            if (onDismissAction == null || !onDismissAction.willRunAnimationOnKeyguard()) {
                z2 = false;
            } else {
                z2 = true;
            }
            this.mDismissActionWillAnimateOnKeyguard = z2;
            if (this.mAlternateAuthInterceptor == null || !this.mKeyguardUpdateManager.isUnlockingWithBiometricAllowed(true)) {
                z3 = false;
            } else {
                z3 = true;
            }
            if (z3) {
                if (!z) {
                    KeyguardBouncer keyguardBouncer = this.mBouncer;
                    ActivityStarter.OnDismissAction onDismissAction2 = this.mAfterKeyguardGoneAction;
                    Runnable runnable3 = this.mKeyguardGoneCancelAction;
                    Objects.requireNonNull(keyguardBouncer);
                    KeyguardHostViewController keyguardHostViewController = keyguardBouncer.mKeyguardViewController;
                    Objects.requireNonNull(keyguardHostViewController);
                    Runnable runnable4 = keyguardHostViewController.mCancelAction;
                    if (runnable4 != null) {
                        runnable4.run();
                        keyguardHostViewController.mCancelAction = null;
                    }
                    keyguardHostViewController.mDismissAction = onDismissAction2;
                    keyguardHostViewController.mCancelAction = runnable3;
                    this.mAfterKeyguardGoneAction = null;
                    this.mKeyguardGoneCancelAction = null;
                }
                UdfpsKeyguardViewController.AnonymousClass2 r6 = (UdfpsKeyguardViewController.AnonymousClass2) this.mAlternateAuthInterceptor;
                Objects.requireNonNull(r6);
                updateAlternateAuthShowing(UdfpsKeyguardViewController.m31$$Nest$mshowUdfpsBouncer(UdfpsKeyguardViewController.this, true));
                return;
            } else if (z) {
                KeyguardBouncer keyguardBouncer2 = this.mBouncer;
                Objects.requireNonNull(keyguardBouncer2);
                keyguardBouncer2.show(false, true);
            } else {
                KeyguardBouncer keyguardBouncer3 = this.mBouncer;
                ActivityStarter.OnDismissAction onDismissAction3 = this.mAfterKeyguardGoneAction;
                Runnable runnable5 = this.mKeyguardGoneCancelAction;
                Objects.requireNonNull(keyguardBouncer3);
                keyguardBouncer3.ensureView();
                KeyguardHostViewController keyguardHostViewController2 = keyguardBouncer3.mKeyguardViewController;
                Objects.requireNonNull(keyguardHostViewController2);
                Runnable runnable6 = keyguardHostViewController2.mCancelAction;
                if (runnable6 != null) {
                    runnable6.run();
                    keyguardHostViewController2.mCancelAction = null;
                }
                keyguardHostViewController2.mDismissAction = onDismissAction3;
                keyguardHostViewController2.mCancelAction = runnable5;
                keyguardBouncer3.show(false, true);
                this.mAfterKeyguardGoneAction = null;
                this.mKeyguardGoneCancelAction = null;
            }
        }
        updateStates();
    }

    public final void executeAfterKeyguardGoneAction() {
        ActivityStarter.OnDismissAction onDismissAction = this.mAfterKeyguardGoneAction;
        if (onDismissAction != null) {
            onDismissAction.onDismiss();
            this.mAfterKeyguardGoneAction = null;
        }
        this.mKeyguardGoneCancelAction = null;
        this.mDismissActionWillAnimateOnKeyguard = false;
        for (int i = 0; i < this.mAfterKeyguardGoneRunnables.size(); i++) {
            this.mAfterKeyguardGoneRunnables.get(i).run();
        }
        this.mAfterKeyguardGoneRunnables.clear();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final ViewRootImpl getViewRootImpl() {
        return this.mNotificationShadeWindowController.getNotificationShadeView().getViewRootImpl();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:23:0x0088  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x008a  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x00bd  */
    /* JADX WARN: Removed duplicated region for block: B:31:0x00ed  */
    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$5] */
    @Override // com.android.keyguard.KeyguardViewController
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void hide(long r28, long r30) {
        /*
            Method dump skipped, instructions count: 367
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.hide(long, long):void");
    }

    public void hideBouncer(boolean z) {
        Runnable runnable;
        if (this.mBouncer != null) {
            if (this.mShowing && !bouncerIsOrWillBeShowing()) {
                this.mAfterKeyguardGoneAction = null;
                this.mDismissActionWillAnimateOnKeyguard = false;
                Runnable runnable2 = this.mKeyguardGoneCancelAction;
                if (runnable2 != null) {
                    runnable2.run();
                    this.mKeyguardGoneCancelAction = null;
                }
            }
            KeyguardBouncer keyguardBouncer = this.mBouncer;
            Objects.requireNonNull(keyguardBouncer);
            if (keyguardBouncer.isShowing()) {
                SysUiStatsLog.write(63, 1);
                DismissCallbackRegistry dismissCallbackRegistry = keyguardBouncer.mDismissCallbackRegistry;
                Objects.requireNonNull(dismissCallbackRegistry);
                int size = dismissCallbackRegistry.mDismissCallbacks.size();
                while (true) {
                    size--;
                    if (size < 0) {
                        break;
                    }
                    DismissCallbackWrapper dismissCallbackWrapper = dismissCallbackRegistry.mDismissCallbacks.get(size);
                    Executor executor = dismissCallbackRegistry.mUiBgExecutor;
                    Objects.requireNonNull(dismissCallbackWrapper);
                    executor.execute(new CarrierTextManager$$ExternalSyntheticLambda0(dismissCallbackWrapper, 4));
                }
                dismissCallbackRegistry.mDismissCallbacks.clear();
            }
            keyguardBouncer.mIsScrimmed = false;
            keyguardBouncer.mFalsingCollector.onBouncerHidden();
            keyguardBouncer.mCallback.onBouncerVisiblityChanged(false);
            KeyguardBouncer.AnonymousClass2 r3 = keyguardBouncer.mShowRunnable;
            boolean z2 = DejankUtils.STRICT_MODE_ENABLED;
            Assert.isMainThread();
            DejankUtils.sPendingRunnables.remove(r3);
            DejankUtils.sHandler.removeCallbacks(r3);
            keyguardBouncer.mHandler.removeCallbacks(keyguardBouncer.mShowRunnable);
            keyguardBouncer.mShowingSoon = false;
            KeyguardHostViewController keyguardHostViewController = keyguardBouncer.mKeyguardViewController;
            if (keyguardHostViewController != null) {
                Runnable runnable3 = keyguardHostViewController.mCancelAction;
                if (runnable3 != null) {
                    runnable3.run();
                    keyguardHostViewController.mCancelAction = null;
                }
                keyguardHostViewController.mDismissAction = null;
                keyguardHostViewController.mCancelAction = null;
                KeyguardHostViewController keyguardHostViewController2 = keyguardBouncer.mKeyguardViewController;
                Objects.requireNonNull(keyguardHostViewController2);
                keyguardHostViewController2.mKeyguardSecurityContainerController.onPause();
            }
            keyguardBouncer.mIsAnimatingAway = false;
            keyguardBouncer.setVisibility(4);
            if (z) {
                keyguardBouncer.mHandler.postDelayed(keyguardBouncer.mRemoveViewRunnable, 50L);
            }
            DismissWithActionRequest dismissWithActionRequest = this.mPendingWakeupAction;
            this.mPendingWakeupAction = null;
            if (dismissWithActionRequest != null && (runnable = dismissWithActionRequest.cancelAction) != null) {
                runnable.run();
            }
        }
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final boolean isBouncerShowing() {
        if (this.mBouncer.isShowing() || isShowingAlternateAuth()) {
            return true;
        }
        return false;
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final boolean isGoingToNotificationShade() {
        return this.mStatusBarStateController.leaveOpenOnKeyguardHide();
    }

    public final boolean isNavBarVisible() {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        boolean z5;
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
        if (biometricUnlockController != null) {
            Objects.requireNonNull(biometricUnlockController);
            if (biometricUnlockController.mMode == 2) {
                z = true;
                if (this.mShowing || this.mOccluded) {
                    z2 = false;
                } else {
                    z2 = true;
                }
                z3 = this.mDozing;
                if (z3 || z) {
                    z4 = false;
                } else {
                    z4 = true;
                }
                if (((z2 || z3) && (!this.mPulsing || this.mIsDocked)) || !this.mGesturalNav) {
                    z5 = false;
                } else {
                    z5 = true;
                }
                if ((!z2 || z4 || this.mScreenOffAnimationPlaying) && !this.mBouncer.isShowing() && !this.mRemoteInputActive && !z5 && !this.mGlobalActionsVisible) {
                    return false;
                }
                return true;
            }
        }
        z = false;
        if (this.mShowing) {
        }
        z2 = false;
        z3 = this.mDozing;
        if (z3) {
        }
        z4 = false;
        if (z2) {
        }
        z5 = false;
        if (!z2) {
        }
        return false;
    }

    public final boolean isShowingAlternateAuth() {
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        if (alternateAuthInterceptor != null) {
            UdfpsKeyguardViewController.AnonymousClass2 r0 = (UdfpsKeyguardViewController.AnonymousClass2) alternateAuthInterceptor;
            Objects.requireNonNull(r0);
            if (UdfpsKeyguardViewController.this.mShowingUdfpsBouncer) {
                return true;
            }
        }
        return false;
    }

    public final boolean isShowingAlternateAuthOrAnimating() {
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        if (alternateAuthInterceptor != null) {
            UdfpsKeyguardViewController.AnonymousClass2 r0 = (UdfpsKeyguardViewController.AnonymousClass2) alternateAuthInterceptor;
            Objects.requireNonNull(r0);
            if (UdfpsKeyguardViewController.this.mShowingUdfpsBouncer) {
                return true;
            }
            Objects.requireNonNull(this.mAlternateAuthInterceptor);
        }
        return false;
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final boolean isUnlockWithWallpaper() {
        return this.mNotificationShadeWindowController.isShowingWallpaper();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void keyguardGoingAway() {
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        statusBar.mKeyguardStateController.notifyKeyguardGoingAway();
        CommandQueue commandQueue = statusBar.mCommandQueue;
        int i = statusBar.mDisplayId;
        Objects.requireNonNull(commandQueue);
        synchronized (commandQueue.mLock) {
            commandQueue.mHandler.obtainMessage(1245184, i, 1).sendToTarget();
        }
        statusBar.updateScrimController();
    }

    public final void launchPendingWakeupAction() {
        DismissWithActionRequest dismissWithActionRequest = this.mPendingWakeupAction;
        this.mPendingWakeupAction = null;
        if (dismissWithActionRequest == null) {
            return;
        }
        if (this.mShowing) {
            dismissWithAction(dismissWithActionRequest.dismissAction, dismissWithActionRequest.cancelAction, dismissWithActionRequest.afterKeyguardGone, dismissWithActionRequest.message);
            return;
        }
        ActivityStarter.OnDismissAction onDismissAction = dismissWithActionRequest.dismissAction;
        if (onDismissAction != null) {
            onDismissAction.onDismiss();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:7:0x001c, code lost:
        if (r0.mMode == 1) goto L_0x001e;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean needsBypassFading() {
        /*
            r3 = this;
            com.android.systemui.statusbar.phone.BiometricUnlockController r0 = r3.mBiometricUnlockController
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.mMode
            r1 = 7
            r2 = 1
            if (r0 == r1) goto L_0x001e
            com.android.systemui.statusbar.phone.BiometricUnlockController r0 = r3.mBiometricUnlockController
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.mMode
            r1 = 2
            if (r0 == r1) goto L_0x001e
            com.android.systemui.statusbar.phone.BiometricUnlockController r0 = r3.mBiometricUnlockController
            java.util.Objects.requireNonNull(r0)
            int r0 = r0.mMode
            if (r0 != r2) goto L_0x0027
        L_0x001e:
            com.android.systemui.statusbar.phone.KeyguardBypassController r3 = r3.mBypassController
            boolean r3 = r3.getBypassEnabled()
            if (r3 == 0) goto L_0x0027
            goto L_0x0028
        L_0x0027:
            r2 = 0
        L_0x0028:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.needsBypassFading():boolean");
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void notifyKeyguardAuthenticated() {
        KeyguardBouncer keyguardBouncer = this.mBouncer;
        Objects.requireNonNull(keyguardBouncer);
        keyguardBouncer.ensureView();
        KeyguardHostViewController keyguardHostViewController = keyguardBouncer.mKeyguardViewController;
        int currentUser = KeyguardUpdateMonitor.getCurrentUser();
        Objects.requireNonNull(keyguardHostViewController);
        keyguardHostViewController.mSecurityCallback.finish(false, currentUser);
        if (this.mAlternateAuthInterceptor != null && isShowingAlternateAuthOrAnimating()) {
            resetAlternateAuth(false);
            executeAfterKeyguardGoneAction();
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onDozingChanged(boolean z) {
        if (this.mDozing != z) {
            this.mDozing = z;
            if (z || this.mBouncer.needsFullscreenBouncer() || this.mOccluded) {
                reset(z);
            }
            updateStates();
            if (!z) {
                launchPendingWakeupAction();
            }
        }
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void onFinishedGoingToSleep() {
        KeyguardBouncer keyguardBouncer = this.mBouncer;
        Objects.requireNonNull(keyguardBouncer);
        if (keyguardBouncer.mKeyguardViewController != null && keyguardBouncer.mContainer.getVisibility() == 0) {
            KeyguardHostViewController keyguardHostViewController = keyguardBouncer.mKeyguardViewController;
            Objects.requireNonNull(keyguardHostViewController);
            if (KeyguardHostViewController.DEBUG) {
                Log.d("KeyguardViewBase", String.format("screen off, instance %s at %s", Integer.toHexString(keyguardHostViewController.hashCode()), Long.valueOf(SystemClock.uptimeMillis())));
            }
            keyguardHostViewController.mKeyguardSecurityContainerController.showPrimarySecurityScreen(true);
            keyguardHostViewController.mKeyguardSecurityContainerController.onPause();
            ((KeyguardHostView) keyguardHostViewController.mView).clearFocus();
        }
    }

    @Override // com.android.systemui.unfold.FoldAodAnimationController.FoldAodAnimationStatus
    public final void onFoldToAodAnimationChanged() {
        FoldAodAnimationController foldAodAnimationController = this.mFoldAodAnimationController;
        if (foldAodAnimationController != null) {
            Objects.requireNonNull(foldAodAnimationController);
            this.mScreenOffAnimationPlaying = foldAodAnimationController.shouldPlayAnimation;
        }
    }

    public final void onKeyguardFadedAway() {
        this.mNotificationContainer.postDelayed(new TaskView$$ExternalSyntheticLambda6(this, 5), 100L);
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        ViewGroupFadeHelper.reset(((PanelViewController) notificationPanelViewController).mView);
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        statusBar.mKeyguardStateController.notifyKeyguardDoneFading();
        ScrimController scrimController = statusBar.mScrimController;
        Objects.requireNonNull(scrimController);
        scrimController.mExpansionAffectsAlpha = true;
        BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
        Objects.requireNonNull(biometricUnlockController);
        if (biometricUnlockController.isWakeAndUnlock()) {
            biometricUnlockController.mFadedAwayAfterWakeAndUnlock = true;
        }
        biometricUnlockController.resetMode();
        WindowManagerGlobal.getInstance().trimMemory(20);
    }

    @Override // com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener
    public final void onPanelExpansionChanged(float f, boolean z, boolean z2) {
        NotificationPanelViewController notificationPanelViewController = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController);
        if (notificationPanelViewController.mHintAnimationRunning) {
            this.mBouncer.setExpansion(1.0f);
        } else if (this.mStatusBarStateController.getState() == 2 && this.mKeyguardUpdateManager.isUdfpsEnrolled()) {
        } else {
            if (bouncerNeedsScrimming()) {
                this.mBouncer.setExpansion(0.0f);
            } else if (this.mShowing) {
                BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
                Objects.requireNonNull(biometricUnlockController);
                int i = biometricUnlockController.mMode;
                boolean z3 = true;
                if (!(i == 1 || i == 2)) {
                    z3 = false;
                }
                if (!z3 && !this.mStatusBar.isInLaunchTransition()) {
                    this.mBouncer.setExpansion(f);
                }
                if (f != 1.0f && z2 && !this.mKeyguardStateController.canDismissLockScreen() && !this.mBouncer.isShowing()) {
                    KeyguardBouncer keyguardBouncer = this.mBouncer;
                    Objects.requireNonNull(keyguardBouncer);
                    if (!keyguardBouncer.mIsAnimatingAway) {
                        this.mBouncer.show(false, false);
                    }
                }
            } else if (this.mPulsing && f == 0.0f) {
                this.mStatusBar.wakeUpIfDozing(SystemClock.uptimeMillis(), this.mStatusBar.getBouncerContainer(), "BOUNCER_VISIBLE");
            }
        }
    }

    @Override // com.android.systemui.statusbar.RemoteInputController.Callback
    public final void onRemoteInputActive(boolean z) {
        this.mRemoteInputActive = z;
        updateStates();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void onStartedGoingToSleep() {
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        statusBar.mNotificationShadeWindowView.getWindowInsetsController().setAnimationsDisabled(true);
        NavigationBarView navigationBarView = this.mStatusBar.getNavigationBarView();
        if (navigationBarView != null) {
            View view = navigationBarView.mVertical;
            if (view != null) {
                view.animate().alpha(0.0f).setDuration(125L).start();
            }
            View view2 = navigationBarView.mHorizontal;
            if (view2 != null) {
                view2.animate().alpha(0.0f).setDuration(125L).start();
            }
        }
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void onStartedWakingUp() {
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        statusBar.mNotificationShadeWindowView.getWindowInsetsController().setAnimationsDisabled(false);
        NavigationBarView navigationBarView = this.mStatusBar.getNavigationBarView();
        if (navigationBarView != null) {
            View view = navigationBarView.mVertical;
            if (view != null) {
                view.animate().alpha(1.0f).setDuration(125L).start();
            }
            View view2 = navigationBarView.mHorizontal;
            if (view2 != null) {
                view2.animate().alpha(1.0f).setDuration(125L).start();
            }
        }
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public final void onThemeChanged() {
        boolean isShowing = this.mBouncer.isShowing();
        KeyguardBouncer keyguardBouncer = this.mBouncer;
        Objects.requireNonNull(keyguardBouncer);
        boolean z = keyguardBouncer.mIsScrimmed;
        hideBouncer(true);
        KeyguardBouncer keyguardBouncer2 = this.mBouncer;
        Objects.requireNonNull(keyguardBouncer2);
        boolean z2 = keyguardBouncer2.mInitialized;
        keyguardBouncer2.ensureView();
        if (z2) {
            KeyguardHostViewController keyguardHostViewController = keyguardBouncer2.mKeyguardViewController;
            if (KeyguardHostViewController.DEBUG) {
                Objects.requireNonNull(keyguardHostViewController);
                Log.d("KeyguardViewBase", "show()");
            }
            keyguardHostViewController.mKeyguardSecurityContainerController.showPrimarySecurityScreen(false);
        }
        keyguardBouncer2.mBouncerPromptReason = keyguardBouncer2.mCallback.getBouncerPromptReason();
        if (isShowing) {
            showBouncer(z);
        }
    }

    public final void requestFp(boolean z) {
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateManager;
        Objects.requireNonNull(keyguardUpdateMonitor);
        keyguardUpdateMonitor.mOccludingAppRequestingFp = z;
        keyguardUpdateMonitor.updateFingerprintListeningState(2);
        AlternateAuthInterceptor alternateAuthInterceptor = this.mAlternateAuthInterceptor;
        if (alternateAuthInterceptor != null) {
            UdfpsKeyguardViewController.AnonymousClass2 r2 = (UdfpsKeyguardViewController.AnonymousClass2) alternateAuthInterceptor;
            UdfpsKeyguardViewController udfpsKeyguardViewController = UdfpsKeyguardViewController.this;
            udfpsKeyguardViewController.mUdfpsRequested = z;
            UdfpsKeyguardView udfpsKeyguardView = (UdfpsKeyguardView) udfpsKeyguardViewController.mView;
            Objects.requireNonNull(udfpsKeyguardView);
            udfpsKeyguardView.mUdfpsRequested = z;
            UdfpsKeyguardViewController.this.updateAlpha();
            UdfpsKeyguardViewController.this.updatePauseAuth();
        }
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void reset(boolean z) {
        if (this.mShowing) {
            this.mNotificationPanelViewController.resetViews(true);
            if (!this.mOccluded || this.mDozing) {
                if (!this.mBouncer.needsFullscreenBouncer() || this.mDozing) {
                    StatusBar statusBar = this.mStatusBar;
                    Objects.requireNonNull(statusBar);
                    statusBar.mStatusBarStateController.setKeyguardRequested(true);
                    statusBar.mStatusBarStateController.setLeaveOpenOnKeyguardHide(false);
                    statusBar.updateIsKeyguard();
                    final AssistManager assistManager = statusBar.mAssistManagerLazy.get();
                    Objects.requireNonNull(assistManager);
                    AsyncTask.execute(new Runnable() { // from class: com.android.systemui.assist.AssistManager.4
                        @Override // java.lang.Runnable
                        public final void run() {
                            assistManager.mAssistUtils.onLockscreenShown();
                        }
                    });
                    if (z) {
                        hideBouncer(false);
                        KeyguardBouncer keyguardBouncer = this.mBouncer;
                        Objects.requireNonNull(keyguardBouncer);
                        boolean z2 = keyguardBouncer.mInitialized;
                        keyguardBouncer.ensureView();
                        if (z2) {
                            KeyguardHostViewController keyguardHostViewController = keyguardBouncer.mKeyguardViewController;
                            if (KeyguardHostViewController.DEBUG) {
                                Objects.requireNonNull(keyguardHostViewController);
                                Log.d("KeyguardViewBase", "show()");
                            }
                            keyguardHostViewController.mKeyguardSecurityContainerController.showPrimarySecurityScreen(false);
                        }
                        keyguardBouncer.mBouncerPromptReason = keyguardBouncer.mCallback.getBouncerPromptReason();
                    }
                } else {
                    this.mStatusBar.hideKeyguard();
                    KeyguardBouncer keyguardBouncer2 = this.mBouncer;
                    Objects.requireNonNull(keyguardBouncer2);
                    keyguardBouncer2.show(true, true);
                }
                updateStates();
            } else {
                this.mStatusBar.hideKeyguard();
                if (z || this.mBouncer.needsFullscreenBouncer()) {
                    hideBouncer(false);
                }
            }
            resetAlternateAuth(false);
            KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateManager;
            Objects.requireNonNull(keyguardUpdateMonitor);
            keyguardUpdateMonitor.mHandler.obtainMessage(312).sendToTarget();
            updateStates();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:5:0x0010, code lost:
        if (com.android.systemui.biometrics.UdfpsKeyguardViewController.m31$$Nest$mshowUdfpsBouncer(r0.this$0, false) == false) goto L_0x0012;
     */
    /* JADX WARN: Code restructure failed: missing block: B:6:0x0012, code lost:
        if (r3 != false) goto L_0x0014;
     */
    /* JADX WARN: Code restructure failed: missing block: B:7:0x0014, code lost:
        r1 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:8:0x0015, code lost:
        updateAlternateAuthShowing(r1);
     */
    /* JADX WARN: Code restructure failed: missing block: B:9:0x0018, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void resetAlternateAuth(boolean r3) {
        /*
            r2 = this;
            com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager$AlternateAuthInterceptor r0 = r2.mAlternateAuthInterceptor
            r1 = 0
            if (r0 == 0) goto L_0x0012
            com.android.systemui.biometrics.UdfpsKeyguardViewController$2 r0 = (com.android.systemui.biometrics.UdfpsKeyguardViewController.AnonymousClass2) r0
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.biometrics.UdfpsKeyguardViewController r0 = com.android.systemui.biometrics.UdfpsKeyguardViewController.this
            boolean r0 = com.android.systemui.biometrics.UdfpsKeyguardViewController.m31$$Nest$mshowUdfpsBouncer(r0, r1)
            if (r0 != 0) goto L_0x0014
        L_0x0012:
            if (r3 == 0) goto L_0x0015
        L_0x0014:
            r1 = 1
        L_0x0015:
            r2.updateAlternateAuthShowing(r1)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.resetAlternateAuth(boolean):void");
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void setKeyguardGoingAwayState(boolean z) {
        this.mNotificationShadeWindowController.setKeyguardGoingAway(z);
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void setNeedsInput(boolean z) {
        this.mNotificationShadeWindowController.setKeyguardNeedsInput(z);
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void setOccluded(boolean z, boolean z2) {
        boolean z3;
        boolean z4;
        StatusBar statusBar = this.mStatusBar;
        Objects.requireNonNull(statusBar);
        statusBar.mIsOccluded = z;
        StatusBarHideIconsForBouncerManager statusBarHideIconsForBouncerManager = statusBar.mStatusBarHideIconsForBouncerManager;
        Objects.requireNonNull(statusBarHideIconsForBouncerManager);
        statusBarHideIconsForBouncerManager.isOccluded = z;
        statusBarHideIconsForBouncerManager.updateHideIconsForBouncer(false);
        ScrimController scrimController = statusBar.mScrimController;
        Objects.requireNonNull(scrimController);
        scrimController.mKeyguardOccluded = z;
        scrimController.updateScrims();
        if (z && !this.mOccluded && this.mShowing) {
            SysUiStatsLog.write(62, 3);
            if (this.mStatusBar.isInLaunchTransition()) {
                this.mOccluded = true;
                updateStates();
                this.mStatusBar.fadeKeyguardAfterLaunchTransition(null, new Runnable() { // from class: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.4
                    @Override // java.lang.Runnable
                    public final void run() {
                        StatusBarKeyguardViewManager statusBarKeyguardViewManager = StatusBarKeyguardViewManager.this;
                        statusBarKeyguardViewManager.mNotificationShadeWindowController.setKeyguardOccluded(statusBarKeyguardViewManager.mOccluded);
                        StatusBarKeyguardViewManager.this.reset(true);
                    }
                });
                return;
            }
            StatusBar statusBar2 = this.mStatusBar;
            Objects.requireNonNull(statusBar2);
            if (statusBar2.mIsLaunchingActivityOverLockscreen) {
                this.mOccluded = true;
                updateStates();
                this.mShadeController.get().addPostCollapseAction(new CarrierTextManager$$ExternalSyntheticLambda0(this, 6));
                return;
            }
        } else if (!z && this.mOccluded && this.mShowing) {
            SysUiStatsLog.write(62, 2);
        }
        if (this.mOccluded || !z) {
            z3 = false;
        } else {
            z3 = true;
        }
        this.mOccluded = z;
        updateStates();
        if (this.mShowing) {
            NotificationMediaManager notificationMediaManager = this.mMediaManager;
            if (!z2 || z) {
                z4 = false;
            } else {
                z4 = true;
            }
            notificationMediaManager.updateMediaMetaData(false, z4);
        }
        this.mNotificationShadeWindowController.setKeyguardOccluded(z);
        if (!this.mDozing) {
            reset(z3);
        }
        if (z2 && !z && this.mShowing && !this.mBouncer.isShowing()) {
            StatusBar statusBar3 = this.mStatusBar;
            Objects.requireNonNull(statusBar3);
            NotificationPanelViewController notificationPanelViewController = statusBar3.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController);
            notificationPanelViewController.setExpandedHeightInternal(notificationPanelViewController.getMaxPanelHeight() * 0.0f);
            statusBar3.mCommandQueueCallbacks.animateExpandNotificationsPanel();
            ScrimController scrimController2 = statusBar3.mScrimController;
            Objects.requireNonNull(scrimController2);
            scrimController2.mUnOcclusionAnimationRunning = true;
        }
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final boolean shouldDisableWindowAnimationsForUnlock() {
        return this.mStatusBar.isInLaunchTransition();
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void show$2() {
        Trace.beginSection("StatusBarKeyguardViewManager#show");
        this.mShowing = true;
        this.mNotificationShadeWindowController.setKeyguardShowing(true);
        KeyguardStateController keyguardStateController = this.mKeyguardStateController;
        keyguardStateController.notifyKeyguardState(this.mShowing, keyguardStateController.isOccluded());
        reset(true);
        SysUiStatsLog.write(62, 2);
        Trace.endSection();
    }

    public final void showGenericBouncer() {
        boolean z;
        if (this.mAlternateAuthInterceptor == null || !this.mKeyguardUpdateManager.isUnlockingWithBiometricAllowed(true)) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            UdfpsKeyguardViewController.AnonymousClass2 r0 = (UdfpsKeyguardViewController.AnonymousClass2) this.mAlternateAuthInterceptor;
            Objects.requireNonNull(r0);
            updateAlternateAuthShowing(UdfpsKeyguardViewController.m31$$Nest$mshowUdfpsBouncer(UdfpsKeyguardViewController.this, true));
            return;
        }
        showBouncer(true);
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final void startPreHideAnimation(QSTileImpl$$ExternalSyntheticLambda0 qSTileImpl$$ExternalSyntheticLambda0) {
        boolean z;
        if (this.mBouncer.isShowing()) {
            KeyguardBouncer keyguardBouncer = this.mBouncer;
            Objects.requireNonNull(keyguardBouncer);
            keyguardBouncer.mIsAnimatingAway = true;
            KeyguardHostViewController keyguardHostViewController = keyguardBouncer.mKeyguardViewController;
            if (keyguardHostViewController != null) {
                KeyguardSecurityContainerController keyguardSecurityContainerController = keyguardHostViewController.mKeyguardSecurityContainerController;
                Objects.requireNonNull(keyguardSecurityContainerController);
                KeyguardSecurityModel.SecurityMode securityMode = keyguardSecurityContainerController.mCurrentSecurityMode;
                if (securityMode != KeyguardSecurityModel.SecurityMode.None) {
                    KeyguardSecurityContainer keyguardSecurityContainer = (KeyguardSecurityContainer) keyguardSecurityContainerController.mView;
                    Objects.requireNonNull(keyguardSecurityContainer);
                    keyguardSecurityContainer.mDisappearAnimRunning = true;
                    keyguardSecurityContainer.mViewMode.startDisappearAnimation(securityMode);
                    z = keyguardSecurityContainerController.getCurrentSecurityController().startDisappearAnimation(qSTileImpl$$ExternalSyntheticLambda0);
                } else {
                    z = false;
                }
                if (!z && qSTileImpl$$ExternalSyntheticLambda0 != null) {
                    qSTileImpl$$ExternalSyntheticLambda0.run();
                }
            } else if (qSTileImpl$$ExternalSyntheticLambda0 != null) {
                qSTileImpl$$ExternalSyntheticLambda0.run();
            }
            StatusBar statusBar = this.mStatusBar;
            Objects.requireNonNull(statusBar);
            NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController);
            KeyguardQsUserSwitchController keyguardQsUserSwitchController = notificationPanelViewController.mKeyguardQsUserSwitchController;
            if (keyguardQsUserSwitchController != null) {
                int i = notificationPanelViewController.mBarState;
                keyguardQsUserSwitchController.mKeyguardVisibilityHelper.setViewVisibility(i, true, false, i);
            }
            KeyguardUserSwitcherController keyguardUserSwitcherController = notificationPanelViewController.mKeyguardUserSwitcherController;
            if (keyguardUserSwitcherController != null) {
                int i2 = notificationPanelViewController.mBarState;
                keyguardUserSwitcherController.mKeyguardVisibilityHelper.setViewVisibility(i2, true, false, i2);
            }
            if (this.mDismissActionWillAnimateOnKeyguard) {
                updateStates();
            }
        } else if (qSTileImpl$$ExternalSyntheticLambda0 != null) {
            qSTileImpl$$ExternalSyntheticLambda0.run();
        }
        NotificationPanelViewController notificationPanelViewController2 = this.mNotificationPanelViewController;
        Objects.requireNonNull(notificationPanelViewController2);
        notificationPanelViewController2.mBlockingExpansionForCurrentTouch = notificationPanelViewController2.mTracking;
    }

    public final void updateAlternateAuthShowing(boolean z) {
        KeyguardMessageAreaController keyguardMessageAreaController = this.mKeyguardMessageAreaController;
        if (keyguardMessageAreaController != null) {
            boolean isShowingAlternateAuth = isShowingAlternateAuth();
            Objects.requireNonNull(keyguardMessageAreaController);
            KeyguardMessageArea keyguardMessageArea = (KeyguardMessageArea) keyguardMessageAreaController.mView;
            Objects.requireNonNull(keyguardMessageArea);
            if (keyguardMessageArea.mAltBouncerShowing != isShowingAlternateAuth) {
                keyguardMessageArea.mAltBouncerShowing = isShowingAlternateAuth;
                keyguardMessageArea.update();
            }
        }
        KeyguardBypassController keyguardBypassController = this.mBypassController;
        boolean isShowingAlternateAuth2 = isShowingAlternateAuth();
        Objects.requireNonNull(keyguardBypassController);
        keyguardBypassController.altBouncerShowing = isShowingAlternateAuth2;
        if (z) {
            this.mStatusBar.updateScrimController();
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x0197  */
    /* JADX WARN: Removed duplicated region for block: B:125:0x01ee  */
    /* JADX WARN: Removed duplicated region for block: B:128:0x01f4  */
    /* JADX WARN: Removed duplicated region for block: B:132:0x01ff  */
    /* JADX WARN: Removed duplicated region for block: B:152:0x0252  */
    /* JADX WARN: Removed duplicated region for block: B:73:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0106  */
    /* JADX WARN: Removed duplicated region for block: B:91:0x015c  */
    /* JADX WARN: Removed duplicated region for block: B:98:0x0177  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateStates() {
        /*
            Method dump skipped, instructions count: 691
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager.updateStates():void");
    }

    public final void wakeAndUnlockDejank() {
        int i;
        if (this.mBiometricUnlockController.isWakeAndUnlock() && this.mLatencyTracker.isEnabled()) {
            BiometricUnlockController biometricUnlockController = this.mBiometricUnlockController;
            Objects.requireNonNull(biometricUnlockController);
            BiometricSourceType biometricSourceType = biometricUnlockController.mBiometricType;
            LatencyTracker latencyTracker = this.mLatencyTracker;
            if (biometricSourceType == BiometricSourceType.FACE) {
                i = 7;
            } else {
                i = 2;
            }
            latencyTracker.onActionEnd(i);
        }
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final boolean bouncerIsOrWillBeShowing() {
        if (!isBouncerShowing()) {
            KeyguardBouncer keyguardBouncer = this.mBouncer;
            Objects.requireNonNull(keyguardBouncer);
            if (!keyguardBouncer.mShowingSoon) {
                return false;
            }
        }
        return true;
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public final void onNavigationModeChanged(int i) {
        boolean isGesturalMode = R$color.isGesturalMode(i);
        if (isGesturalMode != this.mGesturalNav) {
            this.mGesturalNav = isGesturalMode;
            updateStates();
        }
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final boolean shouldSubtleWindowAnimationsForUnlock() {
        return needsBypassFading();
    }

    public final void showBouncerMessage(String str, ColorStateList colorStateList) {
        if (isShowingAlternateAuth()) {
            KeyguardMessageAreaController keyguardMessageAreaController = this.mKeyguardMessageAreaController;
            if (keyguardMessageAreaController != null) {
                keyguardMessageAreaController.setMessage(str);
                return;
            }
            return;
        }
        KeyguardBouncer keyguardBouncer = this.mBouncer;
        Objects.requireNonNull(keyguardBouncer);
        KeyguardHostViewController keyguardHostViewController = keyguardBouncer.mKeyguardViewController;
        if (keyguardHostViewController != null) {
            KeyguardSecurityContainerController keyguardSecurityContainerController = keyguardHostViewController.mKeyguardSecurityContainerController;
            Objects.requireNonNull(keyguardSecurityContainerController);
            if (keyguardSecurityContainerController.mCurrentSecurityMode != KeyguardSecurityModel.SecurityMode.None) {
                keyguardSecurityContainerController.getCurrentSecurityController().showMessage(str, colorStateList);
                return;
            }
            return;
        }
        Log.w("KeyguardBouncer", "Trying to show message on empty bouncer");
    }

    @Override // com.android.keyguard.KeyguardViewController
    public final boolean isShowing() {
        return this.mShowing;
    }
}
