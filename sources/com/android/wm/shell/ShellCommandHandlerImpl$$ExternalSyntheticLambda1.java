package com.android.wm.shell;

import android.content.ContentResolver;
import android.graphics.Rect;
import android.graphics.Region;
import android.provider.Settings;
import android.window.SplashScreenView;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.systemui.accessibility.floatingmenu.AccessibilityFloatingMenuView;
import com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator;
import com.android.systemui.statusbar.phone.NotificationPanelUnfoldAnimationController;
import com.android.systemui.statusbar.phone.NotificationPanelView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.legacysplitscreen.ForcedResizableInfoActivityController;
import com.android.wm.shell.onehanded.BackgroundWindowManager;
import com.android.wm.shell.onehanded.OneHandedAccessibilityUtil;
import com.android.wm.shell.onehanded.OneHandedAnimationController;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.onehanded.OneHandedDisplayAreaOrganizer;
import com.android.wm.shell.onehanded.OneHandedSettingsUtil;
import com.android.wm.shell.onehanded.OneHandedState;
import com.android.wm.shell.onehanded.OneHandedSurfaceTransactionHelper;
import com.android.wm.shell.onehanded.OneHandedTimeoutHandler;
import com.android.wm.shell.onehanded.OneHandedTouchHandler;
import com.android.wm.shell.onehanded.OneHandedTutorialHandler;
import com.android.wm.shell.startingsurface.StartingSurfaceDrawer;
import com.google.android.systemui.assist.uihints.NgaUiController;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShellCommandHandlerImpl$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ShellCommandHandlerImpl$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        boolean z;
        boolean z2 = true;
        switch (this.$r8$classId) {
            case 0:
                PrintWriter printWriter = (PrintWriter) this.f$0;
                OneHandedController oneHandedController = (OneHandedController) obj;
                Objects.requireNonNull(oneHandedController);
                printWriter.println();
                printWriter.println("OneHandedController");
                printWriter.print("  mOffSetFraction=");
                printWriter.println(oneHandedController.mOffSetFraction);
                printWriter.print("  mLockedDisabled=");
                printWriter.println(oneHandedController.mLockedDisabled);
                printWriter.print("  mUserId=");
                printWriter.println(oneHandedController.mUserId);
                printWriter.print("  isShortcutEnabled=");
                printWriter.println(oneHandedController.isShortcutEnabled());
                printWriter.print("  mIsSwipeToNotificationEnabled=");
                printWriter.println(oneHandedController.mIsSwipeToNotificationEnabled);
                OneHandedDisplayAreaOrganizer oneHandedDisplayAreaOrganizer = oneHandedController.mDisplayAreaOrganizer;
                if (oneHandedDisplayAreaOrganizer != null) {
                    printWriter.println("OneHandedDisplayAreaOrganizer");
                    printWriter.print("  mDisplayLayout.rotation()=");
                    DisplayLayout displayLayout = oneHandedDisplayAreaOrganizer.mDisplayLayout;
                    Objects.requireNonNull(displayLayout);
                    printWriter.println(displayLayout.mRotation);
                    printWriter.print("  mDisplayAreaTokenMap=");
                    printWriter.println(oneHandedDisplayAreaOrganizer.mDisplayAreaTokenMap);
                    printWriter.print("  mDefaultDisplayBounds=");
                    printWriter.println(oneHandedDisplayAreaOrganizer.mDefaultDisplayBounds);
                    printWriter.print("  mIsReady=");
                    printWriter.println(oneHandedDisplayAreaOrganizer.mIsReady);
                    printWriter.print("  mLastVisualDisplayBounds=");
                    printWriter.println(oneHandedDisplayAreaOrganizer.mLastVisualDisplayBounds);
                    printWriter.print("  mLastVisualOffset=");
                    printWriter.println(oneHandedDisplayAreaOrganizer.mLastVisualOffset);
                    OneHandedAnimationController oneHandedAnimationController = oneHandedDisplayAreaOrganizer.mAnimationController;
                    if (oneHandedAnimationController != null) {
                        printWriter.println("OneHandedAnimationControllerstates: ");
                        printWriter.print("  mAnimatorMap=");
                        printWriter.println(oneHandedAnimationController.mAnimatorMap);
                        OneHandedSurfaceTransactionHelper oneHandedSurfaceTransactionHelper = oneHandedAnimationController.mSurfaceTransactionHelper;
                        if (oneHandedSurfaceTransactionHelper != null) {
                            printWriter.println("OneHandedSurfaceTransactionHelperstates: ");
                            printWriter.print("  mEnableCornerRadius=");
                            printWriter.println(oneHandedSurfaceTransactionHelper.mEnableCornerRadius);
                            printWriter.print("  mCornerRadiusAdjustment=");
                            printWriter.println(oneHandedSurfaceTransactionHelper.mCornerRadiusAdjustment);
                            printWriter.print("  mCornerRadius=");
                            printWriter.println(oneHandedSurfaceTransactionHelper.mCornerRadius);
                        }
                    }
                }
                OneHandedTouchHandler oneHandedTouchHandler = oneHandedController.mTouchHandler;
                if (oneHandedTouchHandler != null) {
                    printWriter.println("OneHandedTouchHandler");
                    printWriter.print("  mLastUpdatedBounds=");
                    printWriter.println(oneHandedTouchHandler.mLastUpdatedBounds);
                }
                OneHandedTimeoutHandler oneHandedTimeoutHandler = oneHandedController.mTimeoutHandler;
                if (oneHandedTimeoutHandler != null) {
                    printWriter.println("OneHandedTimeoutHandler");
                    printWriter.print("  sTimeout=");
                    printWriter.println(oneHandedTimeoutHandler.mTimeout);
                    printWriter.print("  sListeners=");
                    printWriter.println(oneHandedTimeoutHandler.mListeners);
                }
                if (oneHandedController.mState != null) {
                    StringBuilder m = LockIconView$$ExternalSyntheticOutline0.m(printWriter, "OneHandedState", "  sCurrentState=");
                    m.append(OneHandedState.sCurrentState);
                    printWriter.println(m.toString());
                }
                OneHandedTutorialHandler oneHandedTutorialHandler = oneHandedController.mTutorialHandler;
                if (oneHandedTutorialHandler != null) {
                    printWriter.println("OneHandedTutorialHandler");
                    printWriter.print("  isAttached=");
                    printWriter.println(oneHandedTutorialHandler.isAttached());
                    printWriter.print("  mCurrentState=");
                    printWriter.println(oneHandedTutorialHandler.mCurrentState);
                    printWriter.print("  mDisplayBounds=");
                    printWriter.println(oneHandedTutorialHandler.mDisplayBounds);
                    printWriter.print("  mTutorialAreaHeight=");
                    printWriter.println(oneHandedTutorialHandler.mTutorialAreaHeight);
                    printWriter.print("  mAlphaTransitionStart=");
                    printWriter.println(oneHandedTutorialHandler.mAlphaTransitionStart);
                    printWriter.print("  mAlphaAnimationDurationMs=");
                    printWriter.println(oneHandedTutorialHandler.mAlphaAnimationDurationMs);
                    BackgroundWindowManager backgroundWindowManager = oneHandedTutorialHandler.mBackgroundWindowManager;
                    if (backgroundWindowManager != null) {
                        printWriter.println("BackgroundWindowManager");
                        printWriter.print("  mDisplayBounds=");
                        printWriter.println(backgroundWindowManager.mDisplayBounds);
                        printWriter.print("  mViewHost=");
                        printWriter.println(backgroundWindowManager.mViewHost);
                        printWriter.print("  mLeash=");
                        printWriter.println(backgroundWindowManager.mLeash);
                        printWriter.print("  mBackgroundView=");
                        printWriter.println(backgroundWindowManager.mBackgroundView);
                    }
                }
                OneHandedAccessibilityUtil oneHandedAccessibilityUtil = oneHandedController.mOneHandedAccessibilityUtil;
                if (oneHandedAccessibilityUtil != null) {
                    printWriter.println("OneHandedAccessibilityUtil");
                    printWriter.print("  mPackageName=");
                    printWriter.println(oneHandedAccessibilityUtil.mPackageName);
                    printWriter.print("  mDescription=");
                    printWriter.println(oneHandedAccessibilityUtil.mDescription);
                }
                OneHandedSettingsUtil oneHandedSettingsUtil = oneHandedController.mOneHandedSettingsUtil;
                ContentResolver contentResolver = oneHandedController.mContext.getContentResolver();
                int i = oneHandedController.mUserId;
                Objects.requireNonNull(oneHandedSettingsUtil);
                printWriter.println("OneHandedSettingsUtil");
                printWriter.print("  isOneHandedModeEnable=");
                printWriter.println(OneHandedSettingsUtil.getSettingsOneHandedModeEnabled(contentResolver, i));
                printWriter.print("  isSwipeToNotificationEnabled=");
                printWriter.println(OneHandedSettingsUtil.getSettingsSwipeToNotificationEnabled(contentResolver, i));
                printWriter.print("  oneHandedTimeOut=");
                printWriter.println(Settings.Secure.getIntForUser(contentResolver, "one_handed_mode_timeout", 8, i));
                printWriter.print("  tapsAppToExit=");
                if (Settings.Secure.getIntForUser(contentResolver, "taps_app_to_exit", 1, i) == 1) {
                    z = true;
                } else {
                    z = false;
                }
                printWriter.println(z);
                printWriter.print("  shortcutActivated=");
                if (Settings.Secure.getIntForUser(contentResolver, "one_handed_mode_activated", 0, i) != 1) {
                    z2 = false;
                }
                printWriter.println(z2);
                printWriter.print("  tutorialShownCounts=");
                printWriter.println(Settings.Secure.getIntForUser(contentResolver, "one_handed_tutorial_show_count", 0, i));
                return;
            case 1:
                AccessibilityFloatingMenuView.AnonymousClass1 r4 = (AccessibilityFloatingMenuView.AnonymousClass1) this.f$0;
                int i2 = AccessibilityFloatingMenuView.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r4);
                ((AccessibilityFloatingMenuView.OnDragEndListener) obj).onDragEnd(AccessibilityFloatingMenuView.this.mPosition);
                return;
            case 2:
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this.f$0;
                NotificationPanelUnfoldAnimationController notificationPanelUnfoldAnimationController = (NotificationPanelUnfoldAnimationController) obj;
                Rect rect = NotificationPanelViewController.M_DUMMY_DIRTY_RECT;
                Objects.requireNonNull(notificationPanelViewController);
                NotificationPanelView notificationPanelView = notificationPanelViewController.mView;
                Objects.requireNonNull(notificationPanelUnfoldAnimationController);
                UnfoldConstantTranslateAnimator unfoldConstantTranslateAnimator = (UnfoldConstantTranslateAnimator) notificationPanelUnfoldAnimationController.translateAnimator$delegate.getValue();
                Objects.requireNonNull(unfoldConstantTranslateAnimator);
                unfoldConstantTranslateAnimator.rootView = notificationPanelView;
                unfoldConstantTranslateAnimator.translationMax = notificationPanelUnfoldAnimationController.context.getResources().getDimensionPixelSize(2131166677);
                unfoldConstantTranslateAnimator.progressProvider.addCallback(unfoldConstantTranslateAnimator);
                return;
            case 3:
                ForcedResizableInfoActivityController forcedResizableInfoActivityController = (ForcedResizableInfoActivityController) this.f$0;
                Objects.requireNonNull(forcedResizableInfoActivityController);
                if (!((Boolean) obj).booleanValue()) {
                    forcedResizableInfoActivityController.mPackagesShownInSession.clear();
                    return;
                }
                return;
            case 4:
                StartingSurfaceDrawer.SplashScreenViewSupplier splashScreenViewSupplier = (StartingSurfaceDrawer.SplashScreenViewSupplier) this.f$0;
                SplashScreenView splashScreenView = (SplashScreenView) obj;
                Objects.requireNonNull(splashScreenViewSupplier);
                synchronized (splashScreenViewSupplier) {
                    splashScreenViewSupplier.mView = splashScreenView;
                    splashScreenViewSupplier.mIsViewSet = true;
                    splashScreenViewSupplier.notify();
                }
                return;
            default:
                Region region = (Region) this.f$0;
                Region region2 = (Region) obj;
                boolean z3 = NgaUiController.VERBOSE;
                if (region.isEmpty()) {
                    region.op(region2, Region.Op.UNION);
                    return;
                } else if (region.quickReject(region2)) {
                    Rect bounds = region.getBounds();
                    bounds.top = region2.getBounds().top;
                    region.set(bounds);
                    return;
                } else {
                    region.op(region2, Region.Op.UNION);
                    return;
                }
        }
    }
}
