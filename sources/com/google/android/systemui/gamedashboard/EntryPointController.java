package com.google.android.systemui.gamedashboard;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.ActivityManager;
import android.app.ActivityOptions;
import android.app.GameManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Rect;
import android.provider.Settings;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;
import android.widget.FrameLayout;
import androidx.leanback.R$color;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.navigationbar.NavigationBarOverlayController;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.TaskStackChangeListener;
import com.android.systemui.shared.system.TaskStackChangeListeners;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda32;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper;
import com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class EntryPointController extends NavigationBarOverlayController implements NavigationModeController.ModeChangedListener {
    public final AccessibilityManager mAccessibilityManager;
    public boolean mAlwaysOn;
    public final AnonymousClass3 mAlwaysOnObserver;
    public int mCurrentUserId;
    public final FloatingEntryButton mEntryPoint;
    public final GameManager mGameManager;
    public final GameModeDndController mGameModeDndController;
    public String mGamePackageName;
    public ActivityManager.RunningTaskInfo mGameTaskInfo;
    public final boolean mHasGameOverlay;
    public ObjectAnimator mHideAnimator;
    public boolean mInSplitScreen;
    public boolean mIsImmersive;
    public int mNavBarMode;
    public final AnonymousClass1 mOverviewProxyListener;
    public boolean mRecentsAnimationRunning;
    public final ShortcutBarController mShortcutBarController;
    public boolean mShouldShow;
    public final TaskStackListenerImpl mTaskStackListener;
    public final Optional<TaskSurfaceHelper> mTaskSurfaceHelper;
    public final ToastController mToast;
    public final int mTranslateDownAnimationDuration;
    public final int mTranslateUpAnimationDuration;
    public final GameDashboardUiEventLogger mUiEventLogger;
    public final AnonymousClass2 mUserTracker;
    public boolean mListenersRegistered = false;
    public final StatusBar$$ExternalSyntheticLambda32 mInSplitScreenCallback = new StatusBar$$ExternalSyntheticLambda32(this, 4);

    /* loaded from: classes.dex */
    public class TaskStackListenerImpl extends TaskStackChangeListener {
        public TaskStackListenerImpl() {
        }

        @Override // com.android.systemui.shared.system.TaskStackChangeListener
        public final void onTaskStackChanged() {
            EntryPointController.this.onRunningTaskChange();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r3v4, types: [com.google.android.systemui.gamedashboard.EntryPointController$2, com.android.systemui.settings.CurrentUserTracker] */
    /* JADX WARN: Type inference failed for: r3v6, types: [com.google.android.systemui.gamedashboard.EntryPointController$3, android.database.ContentObserver] */
    /* JADX WARN: Type inference failed for: r5v0, types: [com.android.systemui.recents.OverviewProxyService$OverviewProxyListener, com.google.android.systemui.gamedashboard.EntryPointController$1] */
    /* JADX WARN: Unknown variable types count: 3 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public EntryPointController(android.content.Context r10, android.view.accessibility.AccessibilityManager r11, com.android.systemui.broadcast.BroadcastDispatcher r12, com.android.systemui.statusbar.CommandQueue r13, com.google.android.systemui.gamedashboard.GameModeDndController r14, android.os.Handler r15, com.android.systemui.navigationbar.NavigationModeController r16, java.util.Optional<com.android.wm.shell.legacysplitscreen.LegacySplitScreen> r17, com.android.systemui.recents.OverviewProxyService r18, android.content.pm.PackageManager r19, com.google.android.systemui.gamedashboard.ShortcutBarController r20, com.google.android.systemui.gamedashboard.ToastController r21, com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger r22, java.util.Optional<com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper> r23) {
        /*
            Method dump skipped, instructions count: 243
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.gamedashboard.EntryPointController.<init>(android.content.Context, android.view.accessibility.AccessibilityManager, com.android.systemui.broadcast.BroadcastDispatcher, com.android.systemui.statusbar.CommandQueue, com.google.android.systemui.gamedashboard.GameModeDndController, android.os.Handler, com.android.systemui.navigationbar.NavigationModeController, java.util.Optional, com.android.systemui.recents.OverviewProxyService, android.content.pm.PackageManager, com.google.android.systemui.gamedashboard.ShortcutBarController, com.google.android.systemui.gamedashboard.ToastController, com.google.android.systemui.gamedashboard.GameDashboardUiEventLogger, java.util.Optional):void");
    }

    public final void checkAlwaysOn() {
        boolean z;
        int i;
        boolean z2 = false;
        if (Settings.Secure.getIntForUser(this.mContext.getContentResolver(), "game_dashboard_always_on", 0, this.mCurrentUserId) == 1) {
            z = true;
        } else {
            z = false;
        }
        this.mAlwaysOn = z;
        ShortcutBarController shortcutBarController = this.mShortcutBarController;
        boolean z3 = this.mShouldShow;
        Objects.requireNonNull(shortcutBarController);
        ShortcutBarView shortcutBarView = shortcutBarController.mView;
        Objects.requireNonNull(shortcutBarView);
        shortcutBarView.mIsEntryPointVisible = z;
        ShortcutBarButton shortcutBarButton = shortcutBarView.mEntryPointButton;
        if (z) {
            i = 0;
        } else {
            i = 8;
        }
        shortcutBarButton.setVisibility(i);
        if (z && z3) {
            z2 = true;
        }
        shortcutBarController.onButtonVisibilityChange(z2);
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public final View getCurrentView() {
        FloatingEntryButton floatingEntryButton = this.mEntryPoint;
        Objects.requireNonNull(floatingEntryButton);
        return floatingEntryButton.mFloatingView;
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public final void init(Consumer<Boolean> consumer, Consumer<Rect> consumer2) {
        FloatingEntryButton floatingEntryButton = this.mEntryPoint;
        Objects.requireNonNull(floatingEntryButton);
        floatingEntryButton.mVisibilityChangedCallback = consumer;
        ShortcutBarController shortcutBarController = this.mShortcutBarController;
        Objects.requireNonNull(shortcutBarController);
        ShortcutBarView shortcutBarView = shortcutBarController.mView;
        Objects.requireNonNull(shortcutBarView);
        shortcutBarView.mExcludeBackRegionCallback = consumer2;
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public final boolean isVisible() {
        FloatingEntryButton floatingEntryButton = this.mEntryPoint;
        Objects.requireNonNull(floatingEntryButton);
        if (floatingEntryButton.mFloatingView != null) {
            FloatingEntryButton floatingEntryButton2 = this.mEntryPoint;
            Objects.requireNonNull(floatingEntryButton2);
            if (floatingEntryButton2.mIsShowing) {
                return true;
            }
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0061, code lost:
        if (r2.mIsShowing == false) goto L_0x0063;
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x0065, code lost:
        if (r19.mAlwaysOn != false) goto L_0x0067;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0067, code lost:
        r19.mEntryPoint.hide();
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x006c, code lost:
        r2 = r1.topActivity.getClassName().equals(com.google.android.systemui.gamedashboard.GameMenuActivity.class.getName());
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x007e, code lost:
        if (r19.mShouldShow != false) goto L_0x0085;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0080, code lost:
        if (r2 == false) goto L_0x0083;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0083, code lost:
        r4 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:29:0x0085, code lost:
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0086, code lost:
        r7 = r19.mGameModeDndController;
     */
    /* JADX WARN: Code restructure failed: missing block: B:31:0x0088, code lost:
        if (r4 == false) goto L_0x0090;
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x008c, code lost:
        if (r19.mHasGameOverlay == false) goto L_0x0090;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x008e, code lost:
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x0090, code lost:
        r4 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0091, code lost:
        java.util.Objects.requireNonNull(r7);
        r7.mGameActive = r4;
        r7.updateRule();
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x009b, code lost:
        if (r19.mShouldShow == false) goto L_0x0198;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x009d, code lost:
        r2 = r1.taskId;
        r4 = r19.mGameManager.getGameMode(r19.mGamePackageName);
        r7 = r19.mGameModeDndController.isGameModeDndOn();
        r19.mTaskSurfaceHelper.ifPresent(new com.google.android.systemui.gamedashboard.EntryPointController$$ExternalSyntheticLambda0(r2, r4));
        r8 = r19.mToast;
        java.util.Objects.requireNonNull(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x00be, code lost:
        if (r8.mGameTaskId != r2) goto L_0x00c2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:40:0x00c2, code lost:
        r8.mGameTaskId = r2;
        r8.mLaunchDndMessageView.setVisibility(8);
        r8.mLaunchGameModeMessageView.setVisibility(8);
        r2 = new java.lang.StringBuilder();
        r3 = new java.lang.StringBuilder();
     */
    /* JADX WARN: Code restructure failed: missing block: B:41:0x00d8, code lost:
        if (r7 == false) goto L_0x00f2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x00da, code lost:
        r8.mLaunchDndMessageView.setVisibility(0);
        r2.append(r8.mContext.getString(2131952381));
        r8.mLaunchDndMessageView.setText(r2);
        r7 = 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x00f2, code lost:
        r7 = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:45:0x00f5, code lost:
        if (r4 != 2) goto L_0x0110;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x00f7, code lost:
        r7 = r7 + 1;
        r8.mLaunchGameModeMessageView.setVisibility(0);
        r3.append(r8.mContext.getString(2131952382));
        r8.mLaunchGameModeMessageView.setText(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:47:0x0110, code lost:
        if (r4 != 3) goto L_0x012a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:48:0x0112, code lost:
        r7 = r7 + 1;
        r8.mLaunchGameModeMessageView.setVisibility(0);
        r3.append(r8.mContext.getString(2131952379));
        r8.mLaunchGameModeMessageView.setText(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x012a, code lost:
        if (r7 <= 0) goto L_0x0170;
     */
    /* JADX WARN: Code restructure failed: missing block: B:50:0x012c, code lost:
        r4 = r8.getMargin();
        r8.mLaunchLayout.measure(0, 0);
        r8.removeViewImmediate();
        r4 = new android.view.WindowManager.LayoutParams(-2, r8.mLaunchLayout.getMeasuredHeight() + r4, 0, 0, 2024, 8, -3);
        r4.privateFlags |= 16;
        r4.layoutInDisplayCutoutMode = 3;
        r4.setTitle("ToastText");
        r4.setFitInsetsTypes(0);
        r4.gravity = 80;
        r8.show(r4, 1);
        r8.mLaunchDndMessageView.announceForAccessibility(r2);
        r8.mLaunchGameModeMessageView.announceForAccessibility(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:51:0x0170, code lost:
        r2 = r19.mShortcutBarController;
     */
    /* JADX WARN: Code restructure failed: missing block: B:52:0x0174, code lost:
        if (r19.mIsImmersive != false) goto L_0x017a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:54:0x0178, code lost:
        if (r19.mAlwaysOn == false) goto L_0x017f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:56:0x017c, code lost:
        if (r19.mInSplitScreen != false) goto L_0x017f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:57:0x017e, code lost:
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:58:0x017f, code lost:
        r2.updateVisibility(r5);
        r2 = r19.mShortcutBarController;
        java.util.Objects.requireNonNull(r2);
        r2 = r2.mView;
        java.util.Objects.requireNonNull(r2);
     */
    /* JADX WARN: Code restructure failed: missing block: B:59:0x018e, code lost:
        if (r2.mIsFpsVisible == false) goto L_0x01be;
     */
    /* JADX WARN: Code restructure failed: missing block: B:60:0x0190, code lost:
        r19.mShortcutBarController.registerFps(r1.taskId);
     */
    /* JADX WARN: Code restructure failed: missing block: B:61:0x0198, code lost:
        if (r2 == false) goto L_0x01a3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:62:0x019a, code lost:
        r19.mShortcutBarController.updateVisibility(!r19.mInSplitScreen);
     */
    /* JADX WARN: Code restructure failed: missing block: B:63:0x01a3, code lost:
        r2 = r19.mShortcutBarController;
        java.util.Objects.requireNonNull(r2);
        r2.hideUI();
        r2 = r2.mFpsController;
        java.util.Objects.requireNonNull(r2);
        r2.mWindowManager.unregisterTaskFpsCallback(r2.mListener);
     */
    /* JADX WARN: Code restructure failed: missing block: B:64:0x01b9, code lost:
        if (r2.mCallback == null) goto L_0x01be;
     */
    /* JADX WARN: Code restructure failed: missing block: B:65:0x01bb, code lost:
        r2.mCallback = null;
     */
    /* JADX WARN: Code restructure failed: missing block: B:66:0x01be, code lost:
        r19.mGameTaskInfo = r1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:67:0x01c0, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onRunningTaskChange() {
        /*
            Method dump skipped, instructions count: 449
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.systemui.gamedashboard.EntryPointController.onRunningTaskChange():void");
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public final void registerListeners() {
        if (!this.mListenersRegistered) {
            this.mListenersRegistered = true;
            TaskStackChangeListeners.INSTANCE.registerTaskStackListener(this.mTaskStackListener);
        }
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public final void setButtonState(boolean z, boolean z2) {
        ShortcutBarController shortcutBarController = this.mShortcutBarController;
        Objects.requireNonNull(shortcutBarController);
        ShortcutBarView shortcutBarView = shortcutBarController.mView;
        Objects.requireNonNull(shortcutBarView);
        if (shortcutBarView.mIsAttached && shortcutBarView.mShiftForTransientBar != 0) {
            if (z) {
                if (shortcutBarView.mRevealButton.getVisibility() == 0) {
                    RevealButton revealButton = shortcutBarView.mRevealButton;
                    Objects.requireNonNull(revealButton);
                    if (revealButton.mRightSide) {
                        shortcutBarView.autoUndock(shortcutBarView.mShiftForTransientBar);
                    }
                } else {
                    float width = ((shortcutBarView.getWidth() - shortcutBarView.mBar.getWidth()) - shortcutBarView.mBarMarginEnd) - shortcutBarView.mShiftForTransientBar;
                    if (!shortcutBarView.mIsDragging && shortcutBarView.mBar.getTranslationX() >= width) {
                        ObjectAnimator ofFloat = ObjectAnimator.ofFloat(shortcutBarView.mBar, FrameLayout.TRANSLATION_X, width);
                        ofFloat.setDuration(100L);
                        ofFloat.start();
                    }
                }
            } else if (shortcutBarView.mRevealButton.getVisibility() != 0 && !shortcutBarView.mIsDragging) {
                shortcutBarView.snapBarBackIfNecessary();
            }
        }
        if (!z) {
            FloatingEntryButton floatingEntryButton = this.mEntryPoint;
            Objects.requireNonNull(floatingEntryButton);
            if (!floatingEntryButton.mIsShowing) {
                return;
            }
        }
        if (this.mShouldShow && !this.mAlwaysOn) {
            FloatingEntryButton floatingEntryButton2 = this.mEntryPoint;
            Objects.requireNonNull(floatingEntryButton2);
            View view = floatingEntryButton2.mFloatingView;
            if (view != null) {
                float f = -view.getHeight();
                if (z) {
                    view.setTranslationY(f);
                    view.animate().translationY(0.0f).setDuration(this.mTranslateUpAnimationDuration);
                    final FloatingEntryButton floatingEntryButton3 = this.mEntryPoint;
                    boolean isGesturalMode = R$color.isGesturalMode(this.mNavBarMode);
                    Objects.requireNonNull(floatingEntryButton3);
                    if (floatingEntryButton3.mCanShow && !floatingEntryButton3.mIsShowing) {
                        floatingEntryButton3.mIsShowing = true;
                        int i = floatingEntryButton3.mContext.getResources().getConfiguration().orientation;
                        int dimensionPixelSize = floatingEntryButton3.mContext.getResources().getDimensionPixelSize(2131166595);
                        int dimensionPixelSize2 = floatingEntryButton3.mContext.getResources().getDimensionPixelSize(2131167060);
                        int i2 = floatingEntryButton3.mMargin;
                        int i3 = dimensionPixelSize2 + i2;
                        if (i == 2 && !isGesturalMode) {
                            i2 += dimensionPixelSize;
                        }
                        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-2, floatingEntryButton3.mButtonHeight + i3, i2, 0, 2024, 8, -3);
                        layoutParams.privateFlags |= 16;
                        layoutParams.setTitle("FloatingEntryButton");
                        layoutParams.setFitInsetsTypes(0);
                        layoutParams.gravity = 8388661;
                        floatingEntryButton3.mWindowManager.addView(floatingEntryButton3.mFloatingView, layoutParams);
                        floatingEntryButton3.mFloatingView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() { // from class: com.google.android.systemui.gamedashboard.FloatingEntryButton.1
                            @Override // android.view.View.OnLayoutChangeListener
                            public final void onLayoutChange(View view2, int i4, int i5, int i6, int i7, int i8, int i9, int i10, int i11) {
                                Consumer<Boolean> consumer;
                                FloatingEntryButton floatingEntryButton4 = floatingEntryButton3;
                                if (floatingEntryButton4.mIsShowing && (consumer = floatingEntryButton4.mVisibilityChangedCallback) != null) {
                                    consumer.accept(Boolean.TRUE);
                                }
                                floatingEntryButton3.mFloatingView.removeOnLayoutChangeListener(this);
                            }
                        });
                        return;
                    }
                    return;
                }
                view.animate().cancel();
                if (z2) {
                    ObjectAnimator objectAnimator = this.mHideAnimator;
                    if (objectAnimator != null && objectAnimator.isRunning()) {
                        this.mHideAnimator.end();
                    }
                    this.mEntryPoint.hide();
                    return;
                }
                ObjectAnimator objectAnimator2 = this.mHideAnimator;
                if (objectAnimator2 == null || !objectAnimator2.isRunning()) {
                    ObjectAnimator ofFloat2 = ObjectAnimator.ofFloat(view, "translationY", f);
                    if (this.mAccessibilityManager.isEnabled()) {
                        ofFloat2.setStartDelay(this.mAccessibilityManager.getRecommendedTimeoutMillis(this.mTranslateDownAnimationDuration, 1));
                    }
                    ofFloat2.setDuration(this.mTranslateDownAnimationDuration);
                    ofFloat2.setInterpolator(Interpolators.LINEAR);
                    ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.systemui.gamedashboard.EntryPointController.5
                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationEnd(Animator animator) {
                            EntryPointController.this.mEntryPoint.hide();
                        }
                    });
                    this.mHideAnimator = ofFloat2;
                    ofFloat2.start();
                }
            }
        }
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public final void setCanShow(boolean z) {
        FloatingEntryButton floatingEntryButton = this.mEntryPoint;
        Objects.requireNonNull(floatingEntryButton);
        floatingEntryButton.mCanShow = z;
        if (!z) {
            floatingEntryButton.hide();
        }
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public final void unregisterListeners() {
        if (this.mListenersRegistered) {
            this.mListenersRegistered = false;
            TaskStackChangeListeners.INSTANCE.unregisterTaskStackListener(this.mTaskStackListener);
        }
    }

    public static void $r8$lambda$VcVcWE2qGRM_B0Sxv8eSSgrgnCU(EntryPointController entryPointController) {
        Objects.requireNonNull(entryPointController);
        entryPointController.mEntryPoint.hide();
        entryPointController.mUiEventLogger.log(GameDashboardUiEventLogger.GameDashboardEvent.GAME_DASHBOARD_LAUNCH);
        Context context = entryPointController.mContext;
        IntentFilter intentFilter = GameMenuActivity.DND_FILTER;
        Intent intent = new Intent(context, GameMenuActivity.class);
        ActivityOptions makeCustomTaskAnimation = ActivityOptions.makeCustomTaskAnimation(entryPointController.mContext, 2130772507, 2130772508, null, null, null);
        makeCustomTaskAnimation.setLaunchTaskId(ActivityManagerWrapper.sInstance.getRunningTask().taskId);
        entryPointController.mContext.startActivity(intent, makeCustomTaskAnimation.toBundle());
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public final void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
    }

    @Override // com.android.systemui.navigationbar.NavigationBarOverlayController
    public final boolean isNavigationBarOverlayEnabled() {
        return this.mHasGameOverlay;
    }
}
