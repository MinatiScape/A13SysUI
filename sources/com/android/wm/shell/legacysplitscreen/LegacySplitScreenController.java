package com.android.wm.shell.legacysplitscreen;

import android.animation.AnimationHandler;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.Binder;
import android.provider.Settings;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;
import android.window.WindowContainerTransaction;
import com.android.internal.policy.DividerSnapAlgorithm;
import com.android.systemui.qs.external.TileServices$$ExternalSyntheticLambda0;
import com.android.systemui.recents.OverviewProxyService$$ExternalSyntheticLambda3;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda4;
import com.android.systemui.statusbar.phone.NotificationIconAreaController$$ExternalSyntheticLambda0;
import com.android.systemui.theme.ThemeOverlayApplier;
import com.android.systemui.util.sensors.AsyncSensorManager$$ExternalSyntheticLambda0;
import com.android.systemui.wmshell.WMShell$8$$ExternalSyntheticLambda0;
import com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda0;
import com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda1;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda22;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerCallback;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.legacysplitscreen.ForcedResizableInfoActivityController;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.onehanded.OneHandedController$$ExternalSyntheticLambda1;
import com.android.wm.shell.splitscreen.StageCoordinator$1$$ExternalSyntheticLambda0;
import com.android.wm.shell.splitscreen.StageCoordinator$2$$ExternalSyntheticLambda0;
import com.android.wm.shell.transition.Transitions;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final class LegacySplitScreenController implements DisplayController.OnDisplaysChangedListener {
    public final Context mContext;
    public final DisplayController mDisplayController;
    public final ForcedResizableInfoActivityController mForcedResizableController;
    public final DisplayImeController mImeController;
    public final DividerImeController mImePositionProcessor;
    public boolean mIsKeyguardShowing;
    public final ShellExecutor mMainExecutor;
    public LegacySplitDisplayLayout mRotateSplitLayout;
    public final AnimationHandler mSfVsyncAnimationHandler;
    public LegacySplitDisplayLayout mSplitLayout;
    public final LegacySplitScreenTaskListener mSplits;
    public final ShellTaskOrganizer mTaskOrganizer;
    public final TransactionPool mTransactionPool;
    public DividerView mView;
    public DividerWindowManager mWindowManager;
    public final WindowManagerProxy mWindowManagerProxy;
    public final DividerState mDividerState = new DividerState();
    public final SplitScreenImpl mImpl = new SplitScreenImpl();
    public final CopyOnWriteArrayList<WeakReference<Consumer<Boolean>>> mDockedStackExistsListeners = new CopyOnWriteArrayList<>();
    public final ArrayList<WeakReference<BiConsumer<Rect, Rect>>> mBoundsChangedListeners = new ArrayList<>();
    public boolean mVisible = false;
    public volatile boolean mMinimized = false;
    public volatile boolean mAdjustedForIme = false;
    public boolean mHomeStackResizable = false;
    public final LegacySplitScreenController$$ExternalSyntheticLambda0 mRotationController = new DisplayChangeController.OnDisplayChangingListener() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$$ExternalSyntheticLambda0
        @Override // com.android.wm.shell.common.DisplayChangeController.OnDisplayChangingListener
        public final void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
            int i4;
            int i5;
            LegacySplitScreenController legacySplitScreenController = LegacySplitScreenController.this;
            Objects.requireNonNull(legacySplitScreenController);
            LegacySplitScreenTaskListener legacySplitScreenTaskListener = legacySplitScreenController.mSplits;
            Objects.requireNonNull(legacySplitScreenTaskListener);
            if (legacySplitScreenTaskListener.mSplitScreenSupported && legacySplitScreenController.mWindowManagerProxy != null) {
                WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
                LegacySplitDisplayLayout legacySplitDisplayLayout = new LegacySplitDisplayLayout(legacySplitScreenController.mContext, new DisplayLayout(legacySplitScreenController.mDisplayController.getDisplayLayout(i)), legacySplitScreenController.mSplits);
                legacySplitDisplayLayout.mDisplayLayout.rotateTo(legacySplitDisplayLayout.mContext.getResources(), i3);
                Configuration configuration = new Configuration();
                configuration.unset();
                DisplayLayout displayLayout = legacySplitDisplayLayout.mDisplayLayout;
                Objects.requireNonNull(displayLayout);
                if (displayLayout.mWidth > displayLayout.mHeight) {
                    i4 = 2;
                } else {
                    i4 = 1;
                }
                configuration.orientation = i4;
                DisplayLayout displayLayout2 = legacySplitDisplayLayout.mDisplayLayout;
                Objects.requireNonNull(displayLayout2);
                int i6 = displayLayout2.mWidth;
                DisplayLayout displayLayout3 = legacySplitDisplayLayout.mDisplayLayout;
                Objects.requireNonNull(displayLayout3);
                Rect rect = new Rect(0, 0, i6, displayLayout3.mHeight);
                DisplayLayout displayLayout4 = legacySplitDisplayLayout.mDisplayLayout;
                Objects.requireNonNull(displayLayout4);
                rect.inset(displayLayout4.mNonDecorInsets);
                configuration.windowConfiguration.setAppBounds(rect);
                DisplayLayout displayLayout5 = legacySplitDisplayLayout.mDisplayLayout;
                Objects.requireNonNull(displayLayout5);
                int i7 = displayLayout5.mWidth;
                DisplayLayout displayLayout6 = legacySplitDisplayLayout.mDisplayLayout;
                Objects.requireNonNull(displayLayout6);
                rect.set(0, 0, i7, displayLayout6.mHeight);
                DisplayLayout displayLayout7 = legacySplitDisplayLayout.mDisplayLayout;
                Objects.requireNonNull(displayLayout7);
                rect.inset(displayLayout7.mStableInsets);
                configuration.screenWidthDp = (int) (rect.width() / legacySplitDisplayLayout.mDisplayLayout.density());
                configuration.screenHeightDp = (int) (rect.height() / legacySplitDisplayLayout.mDisplayLayout.density());
                legacySplitDisplayLayout.mContext = legacySplitDisplayLayout.mContext.createConfigurationContext(configuration);
                legacySplitDisplayLayout.mSnapAlgorithm = null;
                legacySplitDisplayLayout.mMinimizedSnapAlgorithm = null;
                legacySplitDisplayLayout.mResourcesValid = false;
                legacySplitScreenController.mRotateSplitLayout = legacySplitDisplayLayout;
                if (legacySplitScreenController.mMinimized) {
                    i5 = legacySplitScreenController.mView.mSnapTargetBeforeMinimized.position;
                } else {
                    i5 = legacySplitDisplayLayout.getSnapAlgorithm().getMiddleTarget().position;
                }
                legacySplitDisplayLayout.resizeSplits(legacySplitDisplayLayout.getSnapAlgorithm().calculateNonDismissingSnapTarget(i5).position, windowContainerTransaction2);
                if (legacySplitScreenController.isSplitActive() && legacySplitScreenController.mHomeStackResizable) {
                    legacySplitScreenController.mWindowManagerProxy.applyHomeTasksMinimized(legacySplitDisplayLayout, legacySplitScreenController.mSplits.mSecondary.token, windowContainerTransaction2);
                }
                if (legacySplitScreenController.mWindowManagerProxy.queueSyncTransactionIfWaiting(windowContainerTransaction2)) {
                    Slog.w("SplitScreenCtrl", "Screen rotated while other operations were pending, this may result in some graphical artifacts.");
                } else {
                    windowContainerTransaction.merge(windowContainerTransaction2, true);
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public class SplitScreenImpl implements LegacySplitScreen {
        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public final boolean isDividerVisible() {
            boolean[] zArr = new boolean[1];
            try {
                LegacySplitScreenController.this.mMainExecutor.executeBlocking$1(new TileServices$$ExternalSyntheticLambda0(this, zArr, 1));
            } catch (InterruptedException unused) {
                Slog.e("SplitScreenCtrl", "Failed to get divider visible");
            }
            return zArr[0];
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public final boolean splitPrimaryTask() {
            boolean[] zArr = new boolean[1];
            try {
                LegacySplitScreenController.this.mMainExecutor.executeBlocking$1(new BubbleStackView$$ExternalSyntheticLambda22(this, zArr, 2));
            } catch (InterruptedException unused) {
                Slog.e("SplitScreenCtrl", "Failed to split primary task");
            }
            return zArr[0];
        }

        public SplitScreenImpl() {
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public final DividerView getDividerView() {
            LegacySplitScreenController legacySplitScreenController = LegacySplitScreenController.this;
            Objects.requireNonNull(legacySplitScreenController);
            return legacySplitScreenController.mView;
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public final boolean isHomeStackResizable() {
            return LegacySplitScreenController.this.mHomeStackResizable;
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public final boolean isMinimized() {
            return LegacySplitScreenController.this.mMinimized;
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public final void onAppTransitionFinished() {
            LegacySplitScreenController.this.mMainExecutor.execute(new OneHandedController$$ExternalSyntheticLambda1(this, 6));
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public final void onKeyguardVisibilityChanged(final boolean z) {
            LegacySplitScreenController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    DividerView dividerView;
                    LegacySplitScreenController.SplitScreenImpl splitScreenImpl = LegacySplitScreenController.SplitScreenImpl.this;
                    boolean z2 = z;
                    Objects.requireNonNull(splitScreenImpl);
                    LegacySplitScreenController legacySplitScreenController = LegacySplitScreenController.this;
                    Objects.requireNonNull(legacySplitScreenController);
                    if (legacySplitScreenController.isSplitActive() && (dividerView = legacySplitScreenController.mView) != null) {
                        if (dividerView.mSurfaceHidden != z2) {
                            dividerView.mSurfaceHidden = z2;
                            dividerView.post(new DividerView$$ExternalSyntheticLambda1(dividerView, z2));
                        }
                        legacySplitScreenController.mIsKeyguardShowing = z2;
                    }
                }
            });
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public final void onUndockingTask() {
            LegacySplitScreenController.this.mMainExecutor.execute(new WMShell$8$$ExternalSyntheticLambda0(this, 7));
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public final void registerBoundsChangeListener(OverviewProxyService$$ExternalSyntheticLambda3 overviewProxyService$$ExternalSyntheticLambda3) {
            LegacySplitScreenController.this.mMainExecutor.execute(new NotificationIconAreaController$$ExternalSyntheticLambda0(this, overviewProxyService$$ExternalSyntheticLambda3, 2));
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public final void registerInSplitScreenListener(Consumer<Boolean> consumer) {
            LegacySplitScreenController.this.mMainExecutor.execute(new AsyncSensorManager$$ExternalSyntheticLambda0(this, consumer, 1));
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public final void setMinimized(final boolean z) {
            LegacySplitScreenController.this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    LegacySplitScreenController.SplitScreenImpl splitScreenImpl = LegacySplitScreenController.SplitScreenImpl.this;
                    boolean z2 = z;
                    Objects.requireNonNull(splitScreenImpl);
                    LegacySplitScreenController legacySplitScreenController = LegacySplitScreenController.this;
                    Objects.requireNonNull(legacySplitScreenController);
                    legacySplitScreenController.mMainExecutor.execute(new OverviewProxyService$1$$ExternalSyntheticLambda4(legacySplitScreenController, z2, 1));
                }
            });
        }
    }

    public final void ensureMinimizedSplit() {
        setHomeMinimized(true);
        if (this.mView != null && !isDividerVisible()) {
            updateVisibility(true);
        }
    }

    public final void ensureNormalSplit() {
        setHomeMinimized(false);
        if (this.mView != null && !isDividerVisible()) {
            updateVisibility(true);
        }
    }

    public final void onDismissSplit() {
        updateVisibility(false);
        this.mMinimized = false;
        this.mDividerState.mRatioPositionBeforeMinimized = 0.0f;
        removeDivider();
        DividerImeController dividerImeController = this.mImePositionProcessor;
        Objects.requireNonNull(dividerImeController);
        dividerImeController.mPaused = true;
        dividerImeController.mPausedTargetAdjusted = false;
        dividerImeController.mAnimation = null;
        dividerImeController.mTargetAdjusted = false;
        dividerImeController.mAdjusted = false;
        dividerImeController.mTargetShown = false;
        dividerImeController.mImeWasShown = false;
        dividerImeController.mLastSecondaryDim = 0.0f;
        dividerImeController.mLastPrimaryDim = 0.0f;
        dividerImeController.mTargetSecondaryDim = 0.0f;
        dividerImeController.mTargetPrimaryDim = 0.0f;
        dividerImeController.mSecondaryHasFocus = false;
        dividerImeController.mLastAdjustTop = -1;
    }

    public final boolean isDividerVisible() {
        DividerView dividerView = this.mView;
        if (dividerView == null || dividerView.getVisibility() != 0) {
            return false;
        }
        return true;
    }

    public final boolean isSplitActive() {
        ActivityManager.RunningTaskInfo runningTaskInfo;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mSplits;
        ActivityManager.RunningTaskInfo runningTaskInfo2 = legacySplitScreenTaskListener.mPrimary;
        if (runningTaskInfo2 == null || (runningTaskInfo = legacySplitScreenTaskListener.mSecondary) == null || (runningTaskInfo2.topActivityType == 0 && runningTaskInfo.topActivityType == 0)) {
            return false;
        }
        return true;
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public final void onDisplayAdded(int i) {
        if (i == 0) {
            this.mSplitLayout = new LegacySplitDisplayLayout(this.mDisplayController.getDisplayContext(i), this.mDisplayController.getDisplayLayout(i), this.mSplits);
            this.mImeController.addPositionProcessor(this.mImePositionProcessor);
            this.mDisplayController.addDisplayChangingController(this.mRotationController);
            if (!ActivityTaskManager.supportsSplitScreenMultiWindow(this.mContext)) {
                removeDivider();
                return;
            }
            try {
                LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mSplits;
                Objects.requireNonNull(legacySplitScreenTaskListener);
                synchronized (legacySplitScreenTaskListener) {
                    try {
                        legacySplitScreenTaskListener.mTaskOrganizer.createRootTask(3, legacySplitScreenTaskListener);
                        legacySplitScreenTaskListener.mTaskOrganizer.createRootTask(4, legacySplitScreenTaskListener);
                    } catch (Exception e) {
                        legacySplitScreenTaskListener.mTaskOrganizer.removeListener(legacySplitScreenTaskListener);
                        throw e;
                    }
                }
            } catch (Exception e2) {
                Slog.e("SplitScreenCtrl", "Failed to register docked stack listener", e2);
                removeDivider();
            }
        }
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public final void onDisplayConfigurationChanged(int i, Configuration configuration) {
        if (i == 0) {
            LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mSplits;
            Objects.requireNonNull(legacySplitScreenTaskListener);
            if (legacySplitScreenTaskListener.mSplitScreenSupported) {
                LegacySplitDisplayLayout legacySplitDisplayLayout = new LegacySplitDisplayLayout(this.mDisplayController.getDisplayContext(i), this.mDisplayController.getDisplayLayout(i), this.mSplits);
                this.mSplitLayout = legacySplitDisplayLayout;
                if (this.mRotateSplitLayout == null) {
                    int i2 = legacySplitDisplayLayout.getSnapAlgorithm().getMiddleTarget().position;
                    WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                    this.mSplitLayout.resizeSplits(i2, windowContainerTransaction);
                    this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
                } else {
                    DisplayLayout displayLayout = legacySplitDisplayLayout.mDisplayLayout;
                    Objects.requireNonNull(displayLayout);
                    int i3 = displayLayout.mRotation;
                    DisplayLayout displayLayout2 = this.mRotateSplitLayout.mDisplayLayout;
                    Objects.requireNonNull(displayLayout2);
                    if (i3 == displayLayout2.mRotation) {
                        this.mSplitLayout.mPrimary = new Rect(this.mRotateSplitLayout.mPrimary);
                        this.mSplitLayout.mSecondary = new Rect(this.mRotateSplitLayout.mSecondary);
                        this.mRotateSplitLayout = null;
                    }
                }
                if (isSplitActive()) {
                    update(configuration);
                }
            }
        }
    }

    public final void onUndockingTask() {
        boolean z;
        DividerSnapAlgorithm.SnapTarget snapTarget;
        DividerView dividerView = this.mView;
        if (dividerView != null) {
            int primarySplitSide = dividerView.mSplitLayout.getPrimarySplitSide();
            if (dividerView.getVisibility() == 0) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                dividerView.startDragging(false, false);
                if (DividerView.dockSideTopLeft(primarySplitSide)) {
                    snapTarget = dividerView.mSplitLayout.getSnapAlgorithm().getDismissEndTarget();
                } else {
                    snapTarget = dividerView.mSplitLayout.getSnapAlgorithm().getDismissStartTarget();
                }
                dividerView.mExitAnimationRunning = true;
                int currentPosition = dividerView.getCurrentPosition();
                dividerView.mExitStartPosition = currentPosition;
                dividerView.stopDragging(currentPosition, snapTarget, 336L, 100L, Interpolators.FAST_OUT_SLOW_IN);
            }
        }
    }

    public final void removeDivider() {
        DividerView dividerView = this.mView;
        if (dividerView != null) {
            dividerView.mRemoved = true;
            dividerView.mCallback = null;
        }
        DividerWindowManager dividerWindowManager = this.mWindowManager;
        Objects.requireNonNull(dividerWindowManager);
        View view = dividerWindowManager.mView;
        if (view != null) {
            SystemWindows systemWindows = dividerWindowManager.mSystemWindows;
            Objects.requireNonNull(systemWindows);
            systemWindows.mViewRoots.remove(view).release();
        }
        dividerWindowManager.mView = null;
    }

    public final void setHomeMinimized(boolean z) {
        boolean z2;
        int i;
        DividerSnapAlgorithm.SnapTarget snapTarget;
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        if (this.mMinimized != z) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            this.mMinimized = z;
        }
        windowContainerTransaction.setFocusable(this.mSplits.mPrimary.token, !this.mMinimized);
        DividerView dividerView = this.mView;
        if (dividerView != null) {
            if (dividerView.getDisplay() != null) {
                this.mView.getDisplay().getDisplayId();
            }
            if (this.mMinimized) {
                DividerImeController dividerImeController = this.mImePositionProcessor;
                Objects.requireNonNull(dividerImeController);
                dividerImeController.mMainExecutor.execute(new BaseWifiTracker$$ExternalSyntheticLambda0(dividerImeController, 5));
            }
            if (z2) {
                DividerView dividerView2 = this.mView;
                long j = Settings.Global.getFloat(this.mContext.getContentResolver(), "transition_animation_scale", this.mContext.getResources().getFloat(17105061)) * 336.0f;
                boolean z3 = this.mHomeStackResizable;
                Objects.requireNonNull(dividerView2);
                dividerView2.mHomeStackResizable = z3;
                dividerView2.updateDockSide();
                if (dividerView2.mDockedStackMinimized != z) {
                    dividerView2.mIsInMinimizeInteraction = true;
                    dividerView2.mDockedStackMinimized = z;
                    if (z) {
                        i = dividerView2.mSnapTargetBeforeMinimized.position;
                    } else {
                        i = dividerView2.getCurrentPosition();
                    }
                    if (z) {
                        snapTarget = dividerView2.mSplitLayout.getMinimizedSnapAlgorithm(dividerView2.mHomeStackResizable).getMiddleTarget();
                    } else {
                        snapTarget = dividerView2.mSnapTargetBeforeMinimized;
                    }
                    dividerView2.stopDragging(i, snapTarget, j, 0L, Interpolators.FAST_OUT_SLOW_IN);
                    dividerView2.setAdjustedForIme(false, j);
                }
                if (!z) {
                    dividerView2.mBackground.animate().withEndAction(dividerView2.mResetBackgroundRunnable);
                }
                dividerView2.mBackground.animate().setInterpolator(Interpolators.FAST_OUT_SLOW_IN).setDuration(j).start();
            }
            if (!this.mMinimized) {
                DividerImeController dividerImeController2 = this.mImePositionProcessor;
                Objects.requireNonNull(dividerImeController2);
                dividerImeController2.mMainExecutor.execute(new BaseWifiTracker$$ExternalSyntheticLambda1(dividerImeController2, 7));
            }
        }
        updateTouchable();
        if (!this.mWindowManagerProxy.queueSyncTransactionIfWaiting(windowContainerTransaction)) {
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        }
    }

    public final void startDismissSplit(boolean z, final boolean z2) {
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mSplits;
            Objects.requireNonNull(legacySplitScreenTaskListener);
            final LegacySplitScreenTransitions legacySplitScreenTransitions = legacySplitScreenTaskListener.mSplitTransitions;
            Objects.requireNonNull(legacySplitScreenTransitions);
            final WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            WindowManagerProxy.buildDismissSplit(windowContainerTransaction, this.mSplits, this.mSplitLayout, !z);
            Transitions transitions = legacySplitScreenTransitions.mTransitions;
            Objects.requireNonNull(transitions);
            transitions.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenTransitions$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    LegacySplitScreenTransitions legacySplitScreenTransitions2 = LegacySplitScreenTransitions.this;
                    boolean z3 = z2;
                    WindowContainerTransaction windowContainerTransaction2 = windowContainerTransaction;
                    Objects.requireNonNull(legacySplitScreenTransitions2);
                    legacySplitScreenTransitions2.mDismissFromSnap = z3;
                    legacySplitScreenTransitions2.mPendingDismiss = legacySplitScreenTransitions2.mTransitions.startTransition(22, windowContainerTransaction2, legacySplitScreenTransitions2);
                }
            });
            return;
        }
        WindowManagerProxy windowManagerProxy = this.mWindowManagerProxy;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener2 = this.mSplits;
        Objects.requireNonNull(windowManagerProxy);
        WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
        windowContainerTransaction2.setLaunchRoot(legacySplitScreenTaskListener2.mSecondary.token, (int[]) null, (int[]) null);
        WindowManagerProxy.buildDismissSplit(windowContainerTransaction2, legacySplitScreenTaskListener2, this.mSplitLayout, !z);
        windowManagerProxy.mSyncTransactionQueue.queue(windowContainerTransaction2);
        onDismissSplit();
    }

    public final void update(Configuration configuration) {
        boolean z;
        int i;
        boolean z2;
        int i2;
        if (this.mView == null || !this.mIsKeyguardShowing) {
            z = false;
        } else {
            z = true;
        }
        removeDivider();
        Context displayContext = this.mDisplayController.getDisplayContext(this.mContext.getDisplayId());
        DividerView dividerView = (DividerView) LayoutInflater.from(displayContext).inflate(2131624086, (ViewGroup) null);
        this.mView = dividerView;
        AnimationHandler animationHandler = this.mSfVsyncAnimationHandler;
        Objects.requireNonNull(dividerView);
        dividerView.mSfVsyncAnimationHandler = animationHandler;
        DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(this.mContext.getDisplayId());
        DividerView dividerView2 = this.mView;
        DividerWindowManager dividerWindowManager = this.mWindowManager;
        DividerState dividerState = this.mDividerState;
        ForcedResizableInfoActivityController forcedResizableInfoActivityController = this.mForcedResizableController;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mSplits;
        LegacySplitDisplayLayout legacySplitDisplayLayout = this.mSplitLayout;
        DividerImeController dividerImeController = this.mImePositionProcessor;
        WindowManagerProxy windowManagerProxy = this.mWindowManagerProxy;
        Objects.requireNonNull(dividerView2);
        dividerView2.mSplitScreenController = this;
        dividerView2.mWindowManager = dividerWindowManager;
        dividerView2.mState = dividerState;
        dividerView2.mCallback = forcedResizableInfoActivityController;
        dividerView2.mTiles = legacySplitScreenTaskListener;
        dividerView2.mSplitLayout = legacySplitDisplayLayout;
        dividerView2.mImeController = dividerImeController;
        dividerView2.mWindowManagerProxy = windowManagerProxy;
        if (dividerState.mRatioPositionBeforeMinimized == 0.0f) {
            dividerView2.mSnapTargetBeforeMinimized = legacySplitDisplayLayout.getSnapAlgorithm().getMiddleTarget();
        } else {
            dividerView2.repositionSnapTargetBeforeMinimized();
        }
        DividerView dividerView3 = this.mView;
        if (this.mVisible) {
            i = 0;
        } else {
            i = 4;
        }
        dividerView3.setVisibility(i);
        this.mView.setMinimizedDockStack(this.mMinimized, this.mHomeStackResizable, null);
        int dimensionPixelSize = displayContext.getResources().getDimensionPixelSize(17105203);
        if (configuration.orientation == 2) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (z2) {
            i2 = dimensionPixelSize;
        } else {
            Objects.requireNonNull(displayLayout);
            i2 = displayLayout.mWidth;
        }
        if (z2) {
            Objects.requireNonNull(displayLayout);
            dimensionPixelSize = displayLayout.mHeight;
        }
        DividerWindowManager dividerWindowManager2 = this.mWindowManager;
        DividerView dividerView4 = this.mView;
        int displayId = this.mContext.getDisplayId();
        Objects.requireNonNull(dividerWindowManager2);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(i2, dimensionPixelSize, 2034, 545521704, -3);
        dividerWindowManager2.mLp = layoutParams;
        layoutParams.token = new Binder();
        dividerWindowManager2.mLp.setTitle("DockedStackDivider");
        WindowManager.LayoutParams layoutParams2 = dividerWindowManager2.mLp;
        layoutParams2.privateFlags |= 536870976;
        layoutParams2.layoutInDisplayCutoutMode = 3;
        dividerView4.setSystemUiVisibility(1792);
        dividerWindowManager2.mSystemWindows.addView(dividerView4, dividerWindowManager2.mLp, displayId, 0);
        dividerWindowManager2.mView = dividerView4;
        if (this.mMinimized) {
            this.mView.setMinimizedDockStack(true, this.mHomeStackResizable, null);
            updateTouchable();
        }
        DividerView dividerView5 = this.mView;
        Objects.requireNonNull(dividerView5);
        if (dividerView5.mSurfaceHidden != z) {
            dividerView5.mSurfaceHidden = z;
            dividerView5.post(new DividerView$$ExternalSyntheticLambda1(dividerView5, z));
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0030  */
    /* JADX WARN: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateTouchable() {
        /*
            r7 = this;
            com.android.wm.shell.legacysplitscreen.DividerWindowManager r0 = r7.mWindowManager
            boolean r7 = r7.mAdjustedForIme
            r1 = 1
            r7 = r7 ^ r1
            java.util.Objects.requireNonNull(r0)
            android.view.View r2 = r0.mView
            if (r2 != 0) goto L_0x000e
            goto L_0x0037
        L_0x000e:
            r3 = 0
            if (r7 != 0) goto L_0x001e
            android.view.WindowManager$LayoutParams r4 = r0.mLp
            int r5 = r4.flags
            r6 = r5 & 16
            if (r6 != 0) goto L_0x001e
            r7 = r5 | 16
            r4.flags = r7
            goto L_0x002e
        L_0x001e:
            if (r7 == 0) goto L_0x002d
            android.view.WindowManager$LayoutParams r7 = r0.mLp
            int r4 = r7.flags
            r5 = r4 & 16
            if (r5 == 0) goto L_0x002d
            r3 = r4 & (-17)
            r7.flags = r3
            goto L_0x002e
        L_0x002d:
            r1 = r3
        L_0x002e:
            if (r1 == 0) goto L_0x0037
            com.android.wm.shell.common.SystemWindows r7 = r0.mSystemWindows
            android.view.WindowManager$LayoutParams r0 = r0.mLp
            r7.updateViewLayout(r2, r0)
        L_0x0037:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController.updateTouchable():void");
    }

    public final void updateVisibility(final boolean z) {
        int i;
        if (this.mVisible != z) {
            this.mVisible = z;
            DividerView dividerView = this.mView;
            if (z) {
                i = 0;
            } else {
                i = 4;
            }
            dividerView.setVisibility(i);
            if (z) {
                DividerView dividerView2 = this.mView;
                boolean z2 = this.mHomeStackResizable;
                Objects.requireNonNull(dividerView2);
                if (dividerView2.mSurfaceHidden) {
                    dividerView2.mSurfaceHidden = false;
                    dividerView2.post(new DividerView$$ExternalSyntheticLambda1(dividerView2, false));
                }
                DividerSnapAlgorithm.SnapTarget middleTarget = dividerView2.mSplitLayout.getMinimizedSnapAlgorithm(z2).getMiddleTarget();
                if (dividerView2.mDockedStackMinimized) {
                    int i2 = middleTarget.position;
                    dividerView2.mDividerPositionX = i2;
                    dividerView2.mDividerPositionY = i2;
                }
                WindowManagerProxy windowManagerProxy = this.mWindowManagerProxy;
                StageCoordinator$1$$ExternalSyntheticLambda0 stageCoordinator$1$$ExternalSyntheticLambda0 = new StageCoordinator$1$$ExternalSyntheticLambda0(this, 1);
                Objects.requireNonNull(windowManagerProxy);
                windowManagerProxy.mSyncTransactionQueue.runInSync(stageCoordinator$1$$ExternalSyntheticLambda0);
            } else {
                DividerView dividerView3 = this.mView;
                Objects.requireNonNull(dividerView3);
                SurfaceControl viewSurface = dividerView3.mWindowManager.mSystemWindows.getViewSurface(dividerView3);
                if (viewSurface != null) {
                    SurfaceControl.Transaction transaction = dividerView3.mTiles.getTransaction();
                    transaction.hide(viewSurface);
                    dividerView3.mImeController.setDimsHidden(transaction, true);
                    transaction.apply();
                    dividerView3.mTiles.releaseTransaction(transaction);
                    int i3 = dividerView3.mSplitLayout.getSnapAlgorithm().getMiddleTarget().position;
                    WindowManagerProxy windowManagerProxy2 = dividerView3.mWindowManagerProxy;
                    LegacySplitDisplayLayout legacySplitDisplayLayout = dividerView3.mSplitLayout;
                    Objects.requireNonNull(windowManagerProxy2);
                    WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                    legacySplitDisplayLayout.resizeSplits(i3, windowContainerTransaction);
                    windowManagerProxy2.mSyncTransactionQueue.queue(windowContainerTransaction);
                }
                WindowManagerProxy windowManagerProxy3 = this.mWindowManagerProxy;
                StageCoordinator$2$$ExternalSyntheticLambda0 stageCoordinator$2$$ExternalSyntheticLambda0 = new StageCoordinator$2$$ExternalSyntheticLambda0(this, 1);
                Objects.requireNonNull(windowManagerProxy3);
                windowManagerProxy3.mSyncTransactionQueue.runInSync(stageCoordinator$2$$ExternalSyntheticLambda0);
            }
            synchronized (this.mDockedStackExistsListeners) {
                this.mDockedStackExistsListeners.removeIf(new Predicate() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$$ExternalSyntheticLambda2
                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        boolean z3 = z;
                        Consumer consumer = (Consumer) ((WeakReference) obj).get();
                        if (consumer != null) {
                            consumer.accept(Boolean.valueOf(z3));
                        }
                        if (consumer == null) {
                            return true;
                        }
                        return false;
                    }
                });
            }
        }
    }

    /* JADX WARN: Type inference failed for: r5v4, types: [com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$$ExternalSyntheticLambda0] */
    public LegacySplitScreenController(Context context, DisplayController displayController, SystemWindows systemWindows, DisplayImeController displayImeController, TransactionPool transactionPool, ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, TaskStackListenerImpl taskStackListenerImpl, Transitions transitions, ShellExecutor shellExecutor, AnimationHandler animationHandler) {
        this.mContext = context;
        this.mDisplayController = displayController;
        this.mImeController = displayImeController;
        this.mMainExecutor = shellExecutor;
        this.mSfVsyncAnimationHandler = animationHandler;
        this.mForcedResizableController = new ForcedResizableInfoActivityController(context, this, shellExecutor);
        this.mTransactionPool = transactionPool;
        this.mWindowManagerProxy = new WindowManagerProxy(syncTransactionQueue, shellTaskOrganizer);
        this.mTaskOrganizer = shellTaskOrganizer;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = new LegacySplitScreenTaskListener(this, shellTaskOrganizer, transitions, syncTransactionQueue);
        this.mSplits = legacySplitScreenTaskListener;
        this.mImePositionProcessor = new DividerImeController(legacySplitScreenTaskListener, transactionPool, shellExecutor, shellTaskOrganizer);
        this.mWindowManager = new DividerWindowManager(systemWindows);
        if (context.getResources().getBoolean(17891798)) {
            displayController.addDisplayWindowListener(this);
            taskStackListenerImpl.addListener(new TaskStackListenerCallback() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController.1
                @Override // com.android.wm.shell.common.TaskStackListenerCallback
                public final void onActivityDismissingDockedStack() {
                    ForcedResizableInfoActivityController forcedResizableInfoActivityController = LegacySplitScreenController.this.mForcedResizableController;
                    Objects.requireNonNull(forcedResizableInfoActivityController);
                    Toast.makeText(forcedResizableInfoActivityController.mContext, 2131952296, 0).show();
                }

                @Override // com.android.wm.shell.common.TaskStackListenerCallback
                public final void onActivityForcedResizable(String str, int i, int i2) {
                    boolean z;
                    ForcedResizableInfoActivityController forcedResizableInfoActivityController = LegacySplitScreenController.this.mForcedResizableController;
                    Objects.requireNonNull(forcedResizableInfoActivityController);
                    if (str == null) {
                        z = false;
                    } else if (ThemeOverlayApplier.SYSUI_PACKAGE.equals(str)) {
                        z = true;
                    } else {
                        boolean contains = forcedResizableInfoActivityController.mPackagesShownInSession.contains(str);
                        forcedResizableInfoActivityController.mPackagesShownInSession.add(str);
                        z = contains;
                    }
                    if (!z) {
                        forcedResizableInfoActivityController.mPendingTasks.add(new ForcedResizableInfoActivityController.PendingTaskRecord(i, i2));
                        forcedResizableInfoActivityController.mMainExecutor.removeCallbacks(forcedResizableInfoActivityController.mTimeoutRunnable);
                        forcedResizableInfoActivityController.mMainExecutor.executeDelayed(forcedResizableInfoActivityController.mTimeoutRunnable, 1000L);
                    }
                }

                @Override // com.android.wm.shell.common.TaskStackListenerCallback
                public final void onActivityLaunchOnSecondaryDisplayFailed() {
                    ForcedResizableInfoActivityController forcedResizableInfoActivityController = LegacySplitScreenController.this.mForcedResizableController;
                    Objects.requireNonNull(forcedResizableInfoActivityController);
                    Toast.makeText(forcedResizableInfoActivityController.mContext, 2131951834, 0).show();
                }

                @Override // com.android.wm.shell.common.TaskStackListenerCallback
                public final void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2) {
                    if (z2 && runningTaskInfo.getWindowingMode() == 3) {
                        LegacySplitScreenTaskListener legacySplitScreenTaskListener2 = LegacySplitScreenController.this.mSplits;
                        Objects.requireNonNull(legacySplitScreenTaskListener2);
                        if (legacySplitScreenTaskListener2.mSplitScreenSupported) {
                            LegacySplitScreenController legacySplitScreenController = LegacySplitScreenController.this;
                            Objects.requireNonNull(legacySplitScreenController);
                            if (legacySplitScreenController.mMinimized) {
                                LegacySplitScreenController.this.onUndockingTask();
                            }
                        }
                    }
                }
            });
        }
    }

    public final void registerInSplitScreenListener(Consumer<Boolean> consumer) {
        consumer.accept(Boolean.valueOf(isDividerVisible()));
        synchronized (this.mDockedStackExistsListeners) {
            this.mDockedStackExistsListeners.add(new WeakReference<>(consumer));
        }
    }
}
