package com.android.systemui.statusbar.phone;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.res.Resources;
import android.util.Log;
import android.util.MathUtils;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.animation.PathInterpolator;
import com.android.internal.jank.InteractionJankMonitor;
import com.android.internal.util.LatencyTracker;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.DejankUtils;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.doze.DozeLog;
import com.android.systemui.media.MediaControlPanel;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.media.MediaPlayerData;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.QSPanelController$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.SysuiStatusBarStateController;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.notification.stack.NotificationSwipeHelper;
import com.android.systemui.statusbar.phone.LockscreenGestureLogger;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.PanelView;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManager;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionStateManagerKt;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda6;
import com.android.wm.shell.animation.FlingAnimationUtils;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class PanelViewController {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final AmbientState mAmbientState;
    public boolean mAnimateAfterExpanding;
    public boolean mAnimatingOnDown;
    public boolean mClosing;
    public boolean mCollapsedAndHeadsUpOnDown;
    public long mDownTime;
    public final DozeLog mDozeLog;
    public boolean mExpandLatencyTracking;
    public boolean mExpanding;
    public final FalsingManager mFalsingManager;
    public FlingAnimationUtils mFlingAnimationUtils;
    public FlingAnimationUtils mFlingAnimationUtilsClosing;
    public FlingAnimationUtils mFlingAnimationUtilsDismissing;
    public final AnonymousClass5 mFlingCollapseRunnable;
    public boolean mGestureWaitForTouchSlop;
    public boolean mHandlingPointerUp;
    public boolean mHasLayoutedSinceDown;
    public HeadsUpManagerPhone mHeadsUpManager;
    public ValueAnimator mHeightAnimator;
    public boolean mHintAnimationRunning;
    public float mHintDistance;
    public boolean mIgnoreXTouchSlop;
    public float mInitialOffsetOnTouch;
    public float mInitialTouchX;
    public float mInitialTouchY;
    public boolean mInstantExpanding;
    public final InteractionJankMonitor mInteractionJankMonitor;
    public boolean mIsLaunchAnimationRunning;
    public boolean mIsSpringBackAnimation;
    public KeyguardBottomAreaView mKeyguardBottomArea;
    public final KeyguardStateController mKeyguardStateController;
    public final LatencyTracker mLatencyTracker;
    public final LockscreenGestureLogger mLockscreenGestureLogger;
    public float mMinExpandHeight;
    public boolean mMotionAborted;
    public final NotificationShadeWindowController mNotificationShadeWindowController;
    public boolean mNotificationsDragEnabled;
    public float mOverExpansion;
    public boolean mPanelClosedOnDown;
    public final PanelExpansionStateManager mPanelExpansionStateManager;
    public float mPanelFlingOvershootAmount;
    public boolean mPanelUpdateWhenAnimatorEnds;
    public final Resources mResources;
    public float mSlopMultiplier;
    public StatusBar mStatusBar;
    public final StatusBarKeyguardViewManager mStatusBarKeyguardViewManager;
    public final SysuiStatusBarStateController mStatusBarStateController;
    public final StatusBarTouchableRegionManager mStatusBarTouchableRegionManager;
    public boolean mTouchAboveFalsingThreshold;
    public boolean mTouchDisabled;
    public final NotificationPanelViewController.AnonymousClass17 mTouchHandler;
    public int mTouchSlop;
    public boolean mTouchSlopExceeded;
    public boolean mTouchSlopExceededBeforeDown;
    public boolean mTouchStartedInEmptyArea;
    public boolean mTracking;
    public int mTrackingPointer;
    public int mUnlockFalsingThreshold;
    public boolean mUpdateFlingOnLayout;
    public float mUpdateFlingVelocity;
    public boolean mUpwardsWhenThresholdReached;
    public boolean mVibrateOnOpening;
    public final VibratorHelper mVibratorHelper;
    public final PanelView mView;
    public int mFixedDuration = -1;
    public float mLastGesturedOverExpansion = -1.0f;
    public float mExpandedFraction = 0.0f;
    public float mExpandedHeight = 0.0f;
    public final VelocityTracker mVelocityTracker = VelocityTracker.obtain();
    public float mNextCollapseSpeedUpFactor = 1.0f;
    public BounceInterpolator mBounceInterpolator = new BounceInterpolator();

    /* loaded from: classes.dex */
    public class OnConfigurationChangedListener implements PanelView.OnConfigurationChangedListener {
        public OnConfigurationChangedListener() {
            PanelViewController.this = r1;
        }
    }

    /* loaded from: classes.dex */
    public class OnLayoutChangeListener implements View.OnLayoutChangeListener {
        public OnLayoutChangeListener() {
            PanelViewController.this = r1;
        }

        @Override // android.view.View.OnLayoutChangeListener
        public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            PanelViewController.this.requestPanelHeightUpdate();
            PanelViewController panelViewController = PanelViewController.this;
            panelViewController.mHasLayoutedSinceDown = true;
            if (panelViewController.mUpdateFlingOnLayout) {
                panelViewController.cancelHeightAnimator();
                panelViewController.mView.removeCallbacks(panelViewController.mFlingCollapseRunnable);
                PanelViewController panelViewController2 = PanelViewController.this;
                float f = panelViewController2.mUpdateFlingVelocity;
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) panelViewController2;
                Objects.requireNonNull(notificationPanelViewController.mStatusBar);
                notificationPanelViewController.fling(f, true, 1.0f, false);
                PanelViewController.this.mUpdateFlingOnLayout = false;
            }
        }
    }

    /* loaded from: classes.dex */
    public class TouchHandler implements View.OnTouchListener {
        public TouchHandler() {
            PanelViewController.this = r1;
        }

        /* JADX WARN: Removed duplicated region for block: B:155:0x02a0  */
        /* JADX WARN: Removed duplicated region for block: B:42:0x0091  */
        @Override // android.view.View.OnTouchListener
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public boolean onTouch(android.view.View r9, android.view.MotionEvent r10) {
            /*
                Method dump skipped, instructions count: 833
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.PanelViewController.TouchHandler.onTouch(android.view.View, android.view.MotionEvent):boolean");
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:48:0x00e5, code lost:
        if ((android.os.SystemClock.uptimeMillis() - r5.mDownTime) <= 300) goto L_0x00ea;
     */
    /* JADX WARN: Code restructure failed: missing block: B:49:0x00e8, code lost:
        if (r9 > 0) goto L_0x00ea;
     */
    /* JADX WARN: Code restructure failed: missing block: B:53:0x00ef, code lost:
        if (r5.mQsExpansionAnimator == null) goto L_0x010d;
     */
    /* JADX WARN: Removed duplicated region for block: B:64:0x0122 A[ADDED_TO_REGION] */
    /* JADX WARN: Removed duplicated region for block: B:68:0x0153  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x0155  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0173  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x0181  */
    /* renamed from: -$$Nest$mendMotionEvent */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static void m106$$Nest$mendMotionEvent(com.android.systemui.statusbar.phone.PanelViewController r15, android.view.MotionEvent r16, float r17, float r18, boolean r19) {
        /*
            Method dump skipped, instructions count: 400
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.phone.PanelViewController.m106$$Nest$mendMotionEvent(com.android.systemui.statusbar.phone.PanelViewController, android.view.MotionEvent, float, float, boolean):void");
    }

    /* JADX WARN: Type inference failed for: r4v5, types: [com.android.systemui.statusbar.phone.PanelViewController$5] */
    public PanelViewController(PanelView panelView, FalsingManager falsingManager, DozeLog dozeLog, KeyguardStateController keyguardStateController, SysuiStatusBarStateController sysuiStatusBarStateController, NotificationShadeWindowController notificationShadeWindowController, VibratorHelper vibratorHelper, StatusBarKeyguardViewManager statusBarKeyguardViewManager, LatencyTracker latencyTracker, FlingAnimationUtils.Builder builder, StatusBarTouchableRegionManager statusBarTouchableRegionManager, LockscreenGestureLogger lockscreenGestureLogger, PanelExpansionStateManager panelExpansionStateManager, AmbientState ambientState, InteractionJankMonitor interactionJankMonitor) {
        final NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this;
        this.mFlingCollapseRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.PanelViewController.5
            @Override // java.lang.Runnable
            public final void run() {
                PanelViewController panelViewController = notificationPanelViewController;
                panelViewController.fling(0.0f, false, panelViewController.mNextCollapseSpeedUpFactor, false);
            }
        };
        keyguardStateController.addCallback(new KeyguardStateController.Callback() { // from class: com.android.systemui.statusbar.phone.PanelViewController.1
            @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
            public final void onKeyguardFadingAwayChanged() {
                notificationPanelViewController.requestPanelHeightUpdate();
            }
        });
        this.mAmbientState = ambientState;
        this.mView = panelView;
        this.mStatusBarKeyguardViewManager = statusBarKeyguardViewManager;
        this.mLockscreenGestureLogger = lockscreenGestureLogger;
        this.mPanelExpansionStateManager = panelExpansionStateManager;
        NotificationPanelViewController.AnonymousClass17 r4 = new NotificationPanelViewController.AnonymousClass17();
        this.mTouchHandler = r4;
        panelView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.statusbar.phone.PanelViewController.2
            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewDetachedFromWindow(View view) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewAttachedToWindow(View view) {
                PanelViewController panelViewController = notificationPanelViewController;
                panelViewController.mResources.getResourceName(panelViewController.mView.getId());
                Objects.requireNonNull(panelViewController);
            }
        });
        panelView.addOnLayoutChangeListener(new NotificationPanelViewController.OnLayoutChangeListener());
        panelView.setOnTouchListener(r4);
        panelView.mOnConfigurationChangedListener = new NotificationPanelViewController.OnConfigurationChangedListener();
        Resources resources = panelView.getResources();
        this.mResources = resources;
        this.mKeyguardStateController = keyguardStateController;
        this.mStatusBarStateController = sysuiStatusBarStateController;
        this.mNotificationShadeWindowController = notificationShadeWindowController;
        builder.reset();
        builder.mMaxLengthSeconds = 0.6f;
        builder.mSpeedUpFactor = 0.6f;
        this.mFlingAnimationUtils = builder.build();
        builder.reset();
        builder.mMaxLengthSeconds = 0.6f;
        builder.mSpeedUpFactor = 0.6f;
        this.mFlingAnimationUtilsClosing = builder.build();
        builder.reset();
        builder.mMaxLengthSeconds = 0.5f;
        builder.mSpeedUpFactor = 0.6f;
        builder.mX2 = 0.6f;
        builder.mY2 = 0.84f;
        this.mFlingAnimationUtilsDismissing = builder.build();
        this.mLatencyTracker = latencyTracker;
        this.mFalsingManager = falsingManager;
        this.mDozeLog = dozeLog;
        this.mNotificationsDragEnabled = resources.getBoolean(2131034131);
        this.mVibratorHelper = vibratorHelper;
        this.mVibrateOnOpening = resources.getBoolean(2131034180);
        this.mStatusBarTouchableRegionManager = statusBarTouchableRegionManager;
        this.mInteractionJankMonitor = interactionJankMonitor;
    }

    public abstract void expand(boolean z);

    public abstract int getMaxPanelHeight();

    public abstract void loadDimens();

    public abstract void onExpandingStarted();

    public abstract void onFlingEnd(boolean z);

    public abstract void onTrackingStarted();

    public abstract void onTrackingStopped(boolean z);

    public abstract void setIsClosing(boolean z);

    public abstract void setOverExpansion(float f);

    public final void beginJankMonitoring() {
        String str;
        if (this.mInteractionJankMonitor != null) {
            InteractionJankMonitor.Configuration.Builder withView = InteractionJankMonitor.Configuration.Builder.withView(0, this.mView);
            if (isFullyCollapsed()) {
                str = "Expand";
            } else {
                str = "Collapse";
            }
            this.mInteractionJankMonitor.begin(withView.setTag(str));
        }
    }

    public final void cancelHeightAnimator() {
        ValueAnimator valueAnimator = this.mHeightAnimator;
        if (valueAnimator != null) {
            if (valueAnimator.isRunning()) {
                this.mPanelUpdateWhenAnimatorEnds = false;
            }
            this.mHeightAnimator.cancel();
        }
        endClosing();
    }

    public final ValueAnimator createHeightAnimator(final float f, final float f2) {
        final float f3 = this.mOverExpansion;
        final ValueAnimator ofFloat = ValueAnimator.ofFloat(this.mExpandedHeight, f);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.PanelViewController$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                PanelViewController panelViewController = PanelViewController.this;
                float f4 = f2;
                float f5 = f;
                float f6 = f3;
                ValueAnimator valueAnimator2 = ofFloat;
                Objects.requireNonNull(panelViewController);
                if (f4 > 0.0f || (f5 == 0.0f && f6 != 0.0f)) {
                    panelViewController.setOverExpansionInternal(MathUtils.lerp(f6, panelViewController.mPanelFlingOvershootAmount * f4, Interpolators.FAST_OUT_SLOW_IN.getInterpolation(valueAnimator2.getAnimatedFraction())), false);
                }
                panelViewController.setExpandedHeightInternal(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        return ofFloat;
    }

    public final void endClosing() {
        if (this.mClosing) {
            setIsClosing(false);
            NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this;
            notificationPanelViewController.mStatusBar.onClosingFinished();
            notificationPanelViewController.mClosingWithAlphaFadeOut = false;
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController);
            NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
            Objects.requireNonNull(notificationStackScrollLayout);
            notificationStackScrollLayout.mForceNoOverlappingRendering = false;
            MediaHierarchyManager mediaHierarchyManager = notificationPanelViewController.mMediaHierarchyManager;
            Objects.requireNonNull(mediaHierarchyManager);
            Objects.requireNonNull(mediaHierarchyManager.mediaCarouselController);
            Objects.requireNonNull(MediaPlayerData.INSTANCE);
            for (MediaControlPanel mediaControlPanel : MediaPlayerData.players()) {
                mediaControlPanel.closeGuts(true);
            }
        }
    }

    public final void fling(float f, boolean z, float f2, boolean z2) {
        float f3;
        boolean z3;
        boolean z4;
        final boolean z5;
        float f4;
        float f5;
        if (z) {
            f3 = getMaxPanelHeight();
        } else {
            f3 = 0.0f;
        }
        boolean z6 = true;
        if (!z) {
            setIsClosing(true);
        }
        NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this;
        HeadsUpTouchHelper headsUpTouchHelper = notificationPanelViewController.mHeadsUpTouchHelper;
        boolean z7 = !z;
        Objects.requireNonNull(headsUpTouchHelper);
        if (z7 && headsUpTouchHelper.mCollapseSnoozes) {
            headsUpTouchHelper.mHeadsUpManager.snooze();
        }
        headsUpTouchHelper.mCollapseSnoozes = false;
        notificationPanelViewController.mKeyguardStateController.notifyPanelFlingStart(z7);
        if (z || notificationPanelViewController.isOnKeyguard() || notificationPanelViewController.getFadeoutAlpha() != 1.0f) {
            z3 = false;
        } else {
            z3 = true;
        }
        notificationPanelViewController.mClosingWithAlphaFadeOut = z3;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        notificationStackScrollLayout.mForceNoOverlappingRendering = z3;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = notificationPanelViewController.mNotificationStackScrollLayoutController;
        Objects.requireNonNull(notificationStackScrollLayoutController2);
        NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController2.mView;
        Objects.requireNonNull(notificationStackScrollLayout2);
        notificationStackScrollLayout2.mIsFlinging = true;
        if (f3 == notificationPanelViewController.mExpandedHeight && notificationPanelViewController.mOverExpansion == 0.0f) {
            if (((PanelViewController) notificationPanelViewController).mInteractionJankMonitor != null) {
                InteractionJankMonitor.getInstance().end(0);
            }
            notificationPanelViewController.mKeyguardStateController.notifyPanelFlingEnd();
            notificationPanelViewController.notifyExpandingFinished();
            return;
        }
        if (!z || notificationPanelViewController.mStatusBarStateController.getState() == 1 || notificationPanelViewController.mOverExpansion != 0.0f || f < 0.0f) {
            z4 = false;
        } else {
            z4 = true;
        }
        if (z4 || (notificationPanelViewController.mOverExpansion != 0.0f && z)) {
            z5 = true;
        } else {
            z5 = false;
        }
        if (z4) {
            FlingAnimationUtils flingAnimationUtils = ((PanelViewController) notificationPanelViewController).mFlingAnimationUtils;
            Objects.requireNonNull(flingAnimationUtils);
            f4 = (notificationPanelViewController.mOverExpansion / notificationPanelViewController.mPanelFlingOvershootAmount) + MathUtils.lerp(0.2f, 1.0f, MathUtils.saturate(f / (flingAnimationUtils.mHighVelocityPxPerSecond * 0.5f)));
        } else {
            f4 = 0.0f;
        }
        ValueAnimator createHeightAnimator = notificationPanelViewController.createHeightAnimator(f3, f4);
        if (z) {
            if (!z2 || f >= 0.0f) {
                f5 = f;
            } else {
                f5 = 0.0f;
            }
            ((PanelViewController) notificationPanelViewController).mFlingAnimationUtils.apply(createHeightAnimator, notificationPanelViewController.mExpandedHeight, (f4 * notificationPanelViewController.mPanelFlingOvershootAmount) + f3, f5, ((PanelViewController) notificationPanelViewController).mView.getHeight());
            if (f5 == 0.0f) {
                createHeightAnimator.setDuration(350L);
            }
        } else {
            if (notificationPanelViewController.mBarState == 0 || (!notificationPanelViewController.mKeyguardStateController.canDismissLockScreen() && notificationPanelViewController.mTracking)) {
                z6 = false;
            }
            if (!z6) {
                notificationPanelViewController.mFlingAnimationUtilsClosing.apply(createHeightAnimator, notificationPanelViewController.mExpandedHeight, f3, f, ((PanelViewController) notificationPanelViewController).mView.getHeight());
            } else if (f == 0.0f) {
                createHeightAnimator.setInterpolator(Interpolators.PANEL_CLOSE_ACCELERATED);
                createHeightAnimator.setDuration(((notificationPanelViewController.mExpandedHeight / ((PanelViewController) notificationPanelViewController).mView.getHeight()) * 100.0f) + 200.0f);
            } else {
                notificationPanelViewController.mFlingAnimationUtilsDismissing.apply(createHeightAnimator, notificationPanelViewController.mExpandedHeight, f3, f, ((PanelViewController) notificationPanelViewController).mView.getHeight());
            }
            if (f == 0.0f) {
                createHeightAnimator.setDuration(((float) createHeightAnimator.getDuration()) / f2);
            }
            int i = notificationPanelViewController.mFixedDuration;
            if (i != -1) {
                createHeightAnimator.setDuration(i);
            }
        }
        createHeightAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.PanelViewController.3
            public boolean mCancelled;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationCancel(Animator animator) {
                this.mCancelled = true;
            }

            {
                PanelViewController.this = notificationPanelViewController;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationEnd(Animator animator) {
                if (!z5 || this.mCancelled) {
                    PanelViewController.this.onFlingEnd(this.mCancelled);
                    return;
                }
                final PanelViewController panelViewController = PanelViewController.this;
                Objects.requireNonNull(panelViewController);
                float f6 = panelViewController.mOverExpansion;
                if (f6 == 0.0f) {
                    panelViewController.onFlingEnd(false);
                    return;
                }
                panelViewController.mIsSpringBackAnimation = true;
                ValueAnimator ofFloat = ValueAnimator.ofFloat(f6, 0.0f);
                ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.phone.PanelViewController$$ExternalSyntheticLambda0
                    @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                    public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                        PanelViewController panelViewController2 = PanelViewController.this;
                        Objects.requireNonNull(panelViewController2);
                        panelViewController2.setOverExpansionInternal(((Float) valueAnimator.getAnimatedValue()).floatValue(), false);
                    }
                });
                ofFloat.setDuration(400L);
                ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.PanelViewController.4
                    public boolean mCancelled;

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationCancel(Animator animator2) {
                        this.mCancelled = true;
                    }

                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator2) {
                        PanelViewController panelViewController2 = panelViewController;
                        panelViewController2.mIsSpringBackAnimation = false;
                        panelViewController2.onFlingEnd(this.mCancelled);
                    }
                });
                panelViewController.setAnimator(ofFloat);
                ofFloat.start();
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public final void onAnimationStart(Animator animator) {
                PanelViewController.this.beginJankMonitoring();
            }
        });
        notificationPanelViewController.setAnimator(createHeightAnimator);
        createHeightAnimator.start();
    }

    public final boolean isCollapsing() {
        if (this.mClosing || this.mIsLaunchAnimationRunning) {
            return true;
        }
        return false;
    }

    public final boolean isExpanded() {
        boolean z;
        if (this.mExpandedFraction <= 0.0f && !this.mInstantExpanding) {
            NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this;
            HeadsUpManagerPhone headsUpManagerPhone = notificationPanelViewController.mHeadsUpManager;
            Objects.requireNonNull(headsUpManagerPhone);
            if ((headsUpManagerPhone.mHasPinnedNotification || notificationPanelViewController.mHeadsUpAnimatingAway) && notificationPanelViewController.mBarState == 0) {
                z = true;
            } else {
                z = false;
            }
            if (!z && !this.mTracking && (this.mHeightAnimator == null || this.mIsSpringBackAnimation)) {
                return false;
            }
        }
        return true;
    }

    public final boolean isFalseTouch(float f, float f2, int i) {
        Objects.requireNonNull(this.mStatusBar);
        if (this.mFalsingManager.isClassifierEnabled()) {
            return this.mFalsingManager.isFalseTouch(i);
        }
        if (!this.mTouchAboveFalsingThreshold) {
            return true;
        }
        boolean z = false;
        if (this.mUpwardsWhenThresholdReached) {
            return false;
        }
        float f3 = f - this.mInitialTouchX;
        float f4 = f2 - this.mInitialTouchY;
        if (f4 < 0.0f && Math.abs(f4) >= Math.abs(f3)) {
            z = true;
        }
        return !z;
    }

    public final boolean isFullyCollapsed() {
        if (this.mExpandedFraction <= 0.0f) {
            return true;
        }
        return false;
    }

    public final boolean isFullyExpanded() {
        if (this.mExpandedHeight >= getMaxPanelHeight()) {
            return true;
        }
        return false;
    }

    public final void notifyExpandingStarted() {
        if (!this.mExpanding) {
            this.mExpanding = true;
            onExpandingStarted();
        }
    }

    public final void onEmptySpaceClick() {
        boolean z;
        if (!this.mHintAnimationRunning) {
            NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this;
            int i = notificationPanelViewController.mBarState;
            if (i != 1) {
                if (i == 2 && !notificationPanelViewController.mQsExpanded) {
                    notificationPanelViewController.mStatusBarStateController.setState(1);
                }
            } else if (!notificationPanelViewController.mDozingOnDown) {
                KeyguardUpdateMonitor keyguardUpdateMonitor = notificationPanelViewController.mUpdateMonitor;
                Objects.requireNonNull(keyguardUpdateMonitor);
                if (keyguardUpdateMonitor.mIsFaceEnrolled) {
                    KeyguardUpdateMonitor keyguardUpdateMonitor2 = notificationPanelViewController.mUpdateMonitor;
                    Objects.requireNonNull(keyguardUpdateMonitor2);
                    if (keyguardUpdateMonitor2.mFaceRunningState == 1) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (!z && !notificationPanelViewController.mUpdateMonitor.getUserCanSkipBouncer(KeyguardUpdateMonitor.getCurrentUser())) {
                        notificationPanelViewController.mUpdateMonitor.requestFaceAuth(true);
                        return;
                    }
                }
                notificationPanelViewController.mLockscreenGestureLogger.write(188, 0, 0);
                LockscreenGestureLogger lockscreenGestureLogger = notificationPanelViewController.mLockscreenGestureLogger;
                LockscreenGestureLogger.LockscreenUiEvent lockscreenUiEvent = LockscreenGestureLogger.LockscreenUiEvent.LOCKSCREEN_LOCK_SHOW_HINT;
                Objects.requireNonNull(lockscreenGestureLogger);
                LockscreenGestureLogger.log(lockscreenUiEvent);
                if (notificationPanelViewController.mPowerManager.isPowerSaveMode()) {
                    StatusBar statusBar = notificationPanelViewController.mStatusBar;
                    Objects.requireNonNull(statusBar);
                    statusBar.mFalsingCollector.onUnlockHintStarted();
                    statusBar.mKeyguardIndicationController.showActionToUnlock();
                    NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController.mNotificationStackScrollLayoutController;
                    Objects.requireNonNull(notificationStackScrollLayoutController);
                    NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
                    Objects.requireNonNull(notificationStackScrollLayout);
                    AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
                    Objects.requireNonNull(ambientState);
                    ambientState.mUnlockHintRunning = true;
                    StatusBar statusBar2 = notificationPanelViewController.mStatusBar;
                    Objects.requireNonNull(statusBar2);
                    KeyguardIndicationController keyguardIndicationController = statusBar2.mKeyguardIndicationController;
                    Objects.requireNonNull(keyguardIndicationController);
                    KeyguardIndicationController.AnonymousClass5 r0 = keyguardIndicationController.mHandler;
                    r0.sendMessageDelayed(r0.obtainMessage(1), 1200L);
                    NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = notificationPanelViewController.mNotificationStackScrollLayoutController;
                    Objects.requireNonNull(notificationStackScrollLayoutController2);
                    NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController2.mView;
                    Objects.requireNonNull(notificationStackScrollLayout2);
                    AmbientState ambientState2 = notificationStackScrollLayout2.mAmbientState;
                    Objects.requireNonNull(ambientState2);
                    ambientState2.mUnlockHintRunning = false;
                } else if (notificationPanelViewController.mHeightAnimator == null && !notificationPanelViewController.mTracking) {
                    notificationPanelViewController.notifyExpandingStarted();
                    final StatusBar$$ExternalSyntheticLambda19 statusBar$$ExternalSyntheticLambda19 = new StatusBar$$ExternalSyntheticLambda19(notificationPanelViewController, 4);
                    ValueAnimator createHeightAnimator = notificationPanelViewController.createHeightAnimator(Math.max(0.0f, notificationPanelViewController.getMaxPanelHeight() - notificationPanelViewController.mHintDistance), 0.0f);
                    createHeightAnimator.setDuration(250L);
                    createHeightAnimator.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                    createHeightAnimator.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.PanelViewController.7
                        public boolean mCancelled;

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationCancel(Animator animator) {
                            this.mCancelled = true;
                        }

                        {
                            PanelViewController.this = notificationPanelViewController;
                        }

                        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                        public final void onAnimationEnd(Animator animator) {
                            if (this.mCancelled) {
                                PanelViewController.this.setAnimator(null);
                                statusBar$$ExternalSyntheticLambda19.run();
                                return;
                            }
                            final PanelViewController panelViewController = PanelViewController.this;
                            final Runnable runnable = statusBar$$ExternalSyntheticLambda19;
                            Objects.requireNonNull(panelViewController);
                            ValueAnimator createHeightAnimator2 = panelViewController.createHeightAnimator(panelViewController.getMaxPanelHeight(), 0.0f);
                            createHeightAnimator2.setDuration(450L);
                            createHeightAnimator2.setInterpolator(panelViewController.mBounceInterpolator);
                            createHeightAnimator2.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.phone.PanelViewController.8
                                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                                public final void onAnimationEnd(Animator animator2) {
                                    panelViewController.setAnimator(null);
                                    runnable.run();
                                    panelViewController.updatePanelExpansionAndVisibility();
                                }
                            });
                            createHeightAnimator2.start();
                            panelViewController.setAnimator(createHeightAnimator2);
                        }
                    });
                    createHeightAnimator.start();
                    notificationPanelViewController.setAnimator(createHeightAnimator);
                    KeyguardBottomAreaView keyguardBottomAreaView = notificationPanelViewController.mKeyguardBottomArea;
                    Objects.requireNonNull(keyguardBottomAreaView);
                    StatusBar statusBar3 = notificationPanelViewController.mStatusBar;
                    Objects.requireNonNull(statusBar3);
                    View[] viewArr = {keyguardBottomAreaView.mIndicationArea, statusBar3.mAmbientIndicationContainer};
                    for (int i2 = 0; i2 < 2; i2++) {
                        View view = viewArr[i2];
                        if (view != null) {
                            view.animate().translationY(-notificationPanelViewController.mHintDistance).setDuration(250L).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).withEndAction(new QSPanelController$$ExternalSyntheticLambda0(notificationPanelViewController, view, 1)).start();
                        }
                    }
                    StatusBar statusBar4 = notificationPanelViewController.mStatusBar;
                    Objects.requireNonNull(statusBar4);
                    statusBar4.mFalsingCollector.onUnlockHintStarted();
                    statusBar4.mKeyguardIndicationController.showActionToUnlock();
                    NotificationStackScrollLayoutController notificationStackScrollLayoutController3 = notificationPanelViewController.mNotificationStackScrollLayoutController;
                    Objects.requireNonNull(notificationStackScrollLayoutController3);
                    NotificationStackScrollLayout notificationStackScrollLayout3 = notificationStackScrollLayoutController3.mView;
                    Objects.requireNonNull(notificationStackScrollLayout3);
                    AmbientState ambientState3 = notificationStackScrollLayout3.mAmbientState;
                    Objects.requireNonNull(ambientState3);
                    ambientState3.mUnlockHintRunning = true;
                    notificationPanelViewController.mHintAnimationRunning = true;
                }
            }
        }
    }

    public final void setAnimator(ValueAnimator valueAnimator) {
        this.mHeightAnimator = valueAnimator;
        if (valueAnimator == null && this.mPanelUpdateWhenAnimatorEnds) {
            this.mPanelUpdateWhenAnimatorEnds = false;
            requestPanelHeightUpdate();
        }
    }

    public final void setOverExpansionInternal(float f, boolean z) {
        if (!z) {
            this.mLastGesturedOverExpansion = -1.0f;
            setOverExpansion(f);
        } else if (this.mLastGesturedOverExpansion != f) {
            this.mLastGesturedOverExpansion = f;
            float saturate = MathUtils.saturate(f / (this.mView.getHeight() / 3.0f));
            PathInterpolator pathInterpolator = Interpolators.EMPHASIZED;
            setOverExpansion(MathUtils.max(0.0f, (float) (1.0d - Math.exp(saturate * (-4.0f)))) * this.mPanelFlingOvershootAmount * 2.0f);
        }
    }

    public final void startExpandMotion(float f, float f2, boolean z, float f3) {
        if (!this.mHandlingPointerUp) {
            beginJankMonitoring();
        }
        this.mInitialOffsetOnTouch = f3;
        this.mInitialTouchY = f2;
        this.mInitialTouchX = f;
        if (z) {
            this.mTouchSlopExceeded = true;
            setExpandedHeightInternal(f3);
            onTrackingStarted();
        }
    }

    public final void updatePanelExpansionAndVisibility() {
        boolean z;
        PanelExpansionStateManager panelExpansionStateManager = this.mPanelExpansionStateManager;
        float f = this.mExpandedFraction;
        boolean isExpanded = isExpanded();
        boolean z2 = this.mTracking;
        Objects.requireNonNull(panelExpansionStateManager);
        boolean z3 = true;
        if (!Float.isNaN(f)) {
            int i = panelExpansionStateManager.state;
            panelExpansionStateManager.fraction = f;
            panelExpansionStateManager.expanded = isExpanded;
            panelExpansionStateManager.tracking = z2;
            if (isExpanded) {
                if (i == 0) {
                    panelExpansionStateManager.updateStateInternal(1);
                }
                if (f < 1.0f) {
                    z3 = false;
                }
                z = false;
            } else {
                z = true;
                z3 = false;
            }
            if (z3 && !z2) {
                panelExpansionStateManager.updateStateInternal(2);
            } else if (z && !z2 && panelExpansionStateManager.state != 0) {
                panelExpansionStateManager.updateStateInternal(0);
            }
            PanelExpansionStateManagerKt.access$stateToString(i);
            PanelExpansionStateManagerKt.access$stateToString(panelExpansionStateManager.state);
            Iterator it = panelExpansionStateManager.expansionListeners.iterator();
            while (it.hasNext()) {
                ((PanelExpansionListener) it.next()).onPanelExpansionChanged(f, isExpanded, z2);
            }
            updateVisibility();
            return;
        }
        throw new IllegalArgumentException("fraction cannot be NaN".toString());
    }

    public final void updateVisibility() {
        boolean z;
        PanelView panelView = this.mView;
        NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this;
        boolean z2 = true;
        int i = 0;
        if (notificationPanelViewController.mHeadsUpAnimatingAway || notificationPanelViewController.mHeadsUpPinnedMode) {
            z = true;
        } else {
            z = false;
        }
        if (!z && !notificationPanelViewController.isExpanded() && !notificationPanelViewController.mBouncerShowing) {
            z2 = false;
        }
        if (!z2) {
            i = 4;
        }
        panelView.setVisibility(i);
    }

    /* renamed from: -$$Nest$maddMovement */
    public static void m105$$Nest$maddMovement(PanelViewController panelViewController, MotionEvent motionEvent) {
        Objects.requireNonNull(panelViewController);
        float rawX = motionEvent.getRawX() - motionEvent.getX();
        float rawY = motionEvent.getRawY() - motionEvent.getY();
        motionEvent.offsetLocation(rawX, rawY);
        panelViewController.mVelocityTracker.addMovement(motionEvent);
        motionEvent.offsetLocation(-rawX, -rawY);
    }

    public final boolean canPanelBeCollapsed() {
        if (isFullyCollapsed() || this.mTracking || this.mClosing) {
            return false;
        }
        return true;
    }

    public final float getTouchSlop(MotionEvent motionEvent) {
        if (motionEvent.getClassification() == 1) {
            return this.mTouchSlop * this.mSlopMultiplier;
        }
        return this.mTouchSlop;
    }

    public final void notifyExpandingFinished() {
        endClosing();
        if (this.mExpanding) {
            this.mExpanding = false;
            final NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this;
            ScrimController scrimController = notificationPanelViewController.mScrimController;
            Objects.requireNonNull(scrimController);
            scrimController.mTracking = false;
            scrimController.mUnOcclusionAnimationRunning = false;
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController.mNotificationStackScrollLayoutController;
            Objects.requireNonNull(notificationStackScrollLayoutController);
            NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
            Objects.requireNonNull(notificationStackScrollLayout);
            notificationStackScrollLayout.mCheckForLeavebehind = false;
            NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController.mView;
            Objects.requireNonNull(notificationStackScrollLayout2);
            notificationStackScrollLayout2.mIsExpansionChanging = false;
            AmbientState ambientState = notificationStackScrollLayout2.mAmbientState;
            Objects.requireNonNull(ambientState);
            ambientState.mExpansionChanging = false;
            if (!notificationStackScrollLayout2.mIsExpanded) {
                notificationStackScrollLayout2.mScroller.abortAnimation();
                notificationStackScrollLayout2.setOwnScrollY(0, false);
                StatusBar statusBar = notificationStackScrollLayout2.mStatusBar;
                Objects.requireNonNull(statusBar);
                statusBar.mNotificationsController.resetUserExpandedStates();
                NotificationStackScrollLayout.clearTemporaryViewsInGroup(notificationStackScrollLayout2);
                for (int i = 0; i < notificationStackScrollLayout2.getChildCount(); i++) {
                    ExpandableView expandableView = (ExpandableView) notificationStackScrollLayout2.getChildAt(i);
                    if (expandableView instanceof ExpandableNotificationRow) {
                        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
                        Objects.requireNonNull(expandableNotificationRow);
                        NotificationStackScrollLayout.clearTemporaryViewsInGroup(expandableNotificationRow.mChildrenContainer);
                    }
                }
                for (int i2 = 0; i2 < notificationStackScrollLayout2.getChildCount(); i2++) {
                    ExpandableView expandableView2 = (ExpandableView) notificationStackScrollLayout2.getChildAt(i2);
                    if (expandableView2 instanceof ExpandableNotificationRow) {
                        ((ExpandableNotificationRow) expandableView2).setUserLocked(false);
                    }
                }
                NotificationSwipeHelper notificationSwipeHelper = notificationStackScrollLayout2.mSwipeHelper;
                Objects.requireNonNull(notificationSwipeHelper);
                if (notificationSwipeHelper.mIsSwiping) {
                    NotificationSwipeHelper notificationSwipeHelper2 = notificationStackScrollLayout2.mSwipeHelper;
                    Objects.requireNonNull(notificationSwipeHelper2);
                    notificationSwipeHelper2.mTouchedView = null;
                    notificationSwipeHelper2.mIsSwiping = false;
                    notificationStackScrollLayout2.updateContinuousShadowDrawing();
                }
            }
            HeadsUpManagerPhone headsUpManagerPhone = notificationPanelViewController.mHeadsUpManager;
            Objects.requireNonNull(headsUpManagerPhone);
            if (headsUpManagerPhone.mReleaseOnExpandFinish) {
                headsUpManagerPhone.releaseAllImmediately();
                headsUpManagerPhone.mReleaseOnExpandFinish = false;
            } else {
                Iterator<NotificationEntry> it = headsUpManagerPhone.mEntriesToRemoveAfterExpand.iterator();
                while (it.hasNext()) {
                    NotificationEntry next = it.next();
                    Objects.requireNonNull(next);
                    if (headsUpManagerPhone.isAlerting(next.mKey)) {
                        headsUpManagerPhone.removeAlertEntry(next.mKey);
                    }
                }
            }
            headsUpManagerPhone.mEntriesToRemoveAfterExpand.clear();
            notificationPanelViewController.mConversationNotificationManager.onNotificationPanelExpandStateChanged(notificationPanelViewController.isFullyCollapsed());
            notificationPanelViewController.mIsExpanding = false;
            MediaHierarchyManager mediaHierarchyManager = notificationPanelViewController.mMediaHierarchyManager;
            Objects.requireNonNull(mediaHierarchyManager);
            if (mediaHierarchyManager.collapsingShadeFromQS) {
                mediaHierarchyManager.collapsingShadeFromQS = false;
                MediaHierarchyManager.updateDesiredLocation$default(mediaHierarchyManager, true, 2);
            }
            notificationPanelViewController.mMediaHierarchyManager.setQsExpanded(notificationPanelViewController.mQsExpanded);
            if (notificationPanelViewController.isFullyCollapsed()) {
                DejankUtils.postAfterTraversal(new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.11
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationPanelViewController.this.setListening(false);
                    }
                });
                notificationPanelViewController.mView.postOnAnimation(new Runnable() { // from class: com.android.systemui.statusbar.phone.NotificationPanelViewController.12
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationPanelViewController.this.mView.getParent().invalidateChild(NotificationPanelViewController.this.mView, NotificationPanelViewController.M_DUMMY_DIRTY_RECT);
                    }
                });
            } else {
                notificationPanelViewController.setListening(true);
            }
            notificationPanelViewController.mQsExpandImmediate = false;
            notificationPanelViewController.mNotificationStackScrollLayoutController.setShouldShowShelfOnly(false);
            notificationPanelViewController.mTwoFingerQsExpandPossible = false;
            notificationPanelViewController.mTrackedHeadsUpNotification = null;
            for (int i3 = 0; i3 < notificationPanelViewController.mTrackingHeadsUpListeners.size(); i3++) {
                notificationPanelViewController.mTrackingHeadsUpListeners.get(i3).accept(null);
            }
            notificationPanelViewController.mExpandingFromHeadsUp = false;
            notificationPanelViewController.setPanelScrimMinFraction(0.0f);
        }
    }

    public final void requestPanelHeightUpdate() {
        boolean z;
        float maxPanelHeight = getMaxPanelHeight();
        if (!isFullyCollapsed() && maxPanelHeight != this.mExpandedHeight) {
            if (this.mTracking) {
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this;
                if ((!notificationPanelViewController.mConflictingQsExpansionGesture || !notificationPanelViewController.mQsExpanded) && !notificationPanelViewController.mBlockingExpansionForCurrentTouch) {
                    z = false;
                } else {
                    z = true;
                }
                if (!z) {
                    return;
                }
            }
            if (this.mHeightAnimator == null || this.mIsSpringBackAnimation) {
                setExpandedHeightInternal(maxPanelHeight);
            } else {
                this.mPanelUpdateWhenAnimatorEnds = true;
            }
        }
    }

    public final void setExpandedHeightInternal(final float f) {
        if (Float.isNaN(f)) {
            Log.wtf("PanelView", "ExpandedHeight set to NaN");
        }
        this.mNotificationShadeWindowController.batchApplyWindowLayoutParams(new Runnable() { // from class: com.android.systemui.statusbar.phone.PanelViewController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                float f2;
                int i;
                NotificationStackScrollLayout notificationStackScrollLayout;
                NotificationStackScrollLayoutController notificationStackScrollLayoutController;
                PanelViewController panelViewController = PanelViewController.this;
                float f3 = f;
                Objects.requireNonNull(panelViewController);
                float f4 = 0.0f;
                if (panelViewController.mExpandLatencyTracking && f3 != 0.0f) {
                    DejankUtils.postAfterTraversal(new TaskView$$ExternalSyntheticLambda6(panelViewController, 4));
                    panelViewController.mExpandLatencyTracking = false;
                }
                float maxPanelHeight = panelViewController.getMaxPanelHeight();
                if (panelViewController.mHeightAnimator == null) {
                    if (panelViewController.mTracking) {
                        panelViewController.setOverExpansionInternal(Math.max(0.0f, f3 - maxPanelHeight), true);
                    }
                    panelViewController.mExpandedHeight = Math.min(f3, maxPanelHeight);
                } else {
                    panelViewController.mExpandedHeight = f3;
                }
                float f5 = panelViewController.mExpandedHeight;
                if (f5 < 1.0f && f5 != 0.0f && panelViewController.mClosing) {
                    panelViewController.mExpandedHeight = 0.0f;
                    ValueAnimator valueAnimator = panelViewController.mHeightAnimator;
                    if (valueAnimator != null) {
                        valueAnimator.end();
                    }
                }
                if (maxPanelHeight != 0.0f) {
                    f4 = panelViewController.mExpandedHeight / maxPanelHeight;
                }
                panelViewController.mExpandedFraction = Math.min(1.0f, f4);
                float f6 = panelViewController.mExpandedHeight;
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) panelViewController;
                if ((!notificationPanelViewController.mQsExpanded || notificationPanelViewController.mQsExpandImmediate || (notificationPanelViewController.mIsExpanding && notificationPanelViewController.mQsExpandedWhenExpandingStarted)) && notificationPanelViewController.mStackScrollerMeasuringPass <= 2) {
                    notificationPanelViewController.positionClockAndNotifications(false);
                }
                if (notificationPanelViewController.mQsExpandImmediate || (notificationPanelViewController.mQsExpanded && !notificationPanelViewController.mQsTracking && notificationPanelViewController.mQsExpansionAnimator == null && !notificationPanelViewController.mQsExpansionFromOverscroll)) {
                    if (notificationPanelViewController.mKeyguardShowing) {
                        f2 = f6 / notificationPanelViewController.getMaxPanelHeight();
                    } else {
                        NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = notificationPanelViewController.mNotificationStackScrollLayoutController;
                        Objects.requireNonNull(notificationStackScrollLayoutController2);
                        Objects.requireNonNull(notificationStackScrollLayoutController2.mView);
                        Objects.requireNonNull(notificationPanelViewController.mNotificationStackScrollLayoutController);
                        float layoutMinHeight = notificationStackScrollLayoutController.mView.getLayoutMinHeight() + notificationStackScrollLayout.mIntrinsicPadding;
                        f2 = (f6 - layoutMinHeight) / (notificationPanelViewController.calculatePanelHeightQsExpanded() - layoutMinHeight);
                    }
                    notificationPanelViewController.setQsExpansion((f2 * (notificationPanelViewController.mQsMaxExpansionHeight - i)) + notificationPanelViewController.mQsMinExpansionHeight);
                }
                notificationPanelViewController.updateExpandedHeight(f6);
                if (notificationPanelViewController.mBarState == 1) {
                    notificationPanelViewController.mKeyguardStatusBarViewController.updateViewState();
                }
                notificationPanelViewController.updateQsExpansion$1();
                notificationPanelViewController.updateNotificationTranslucency();
                notificationPanelViewController.updatePanelExpanded();
                notificationPanelViewController.updateGestureExclusionRect();
                panelViewController.updatePanelExpansionAndVisibility();
            }
        });
    }
}
