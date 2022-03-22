package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.util.IndentingPrintWriter;
import android.util.MathUtils;
import android.view.View;
import android.view.animation.PathInterpolator;
import androidx.leanback.R$raw;
import com.android.keyguard.KeyguardVisibilityHelper;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.UdfpsKeyguardViewController;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.communal.CommunalHostView;
import com.android.systemui.communal.CommunalHostViewController;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.LSShadeTransitionLogger;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.statusbar.phone.ScrimState;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.phone.StatusBarKeyguardViewManager;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.Utils;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LockscreenShadeTransitionController.kt */
/* loaded from: classes.dex */
public final class LockscreenShadeTransitionController implements Dumpable {
    public final AmbientState ambientState;
    public Function1<? super Long, Unit> animationHandlerOnKeyguardDismiss;
    public final Context context;
    public final NotificationShadeDepthController depthController;
    public float dragDownAmount;
    public ValueAnimator dragDownAnimator;
    public NotificationEntry draggedDownEntry;
    public final FalsingCollector falsingCollector;
    public boolean forceApplyAmount;
    public int fullTransitionDistance;
    public boolean isWakingToShadeLocked;
    public final KeyguardBypassController keyguardBypassController;
    public final NotificationLockscreenUserManager lockScreenUserManager;
    public final LSShadeTransitionLogger logger;
    public final MediaHierarchyManager mediaHierarchyManager;
    public boolean nextHideKeyguardNeedsNoAnimation;
    public NotificationPanelViewController notificationPanelController;
    public NotificationStackScrollLayoutController nsslController;
    public float pulseHeight;
    public ValueAnimator pulseHeightAnimator;
    public QS qS;
    public float qSDragProgress;
    public final ScrimController scrimController;
    public int scrimTransitionDistance;
    public final SysuiStatusBarStateController statusBarStateController;
    public StatusBar statusbar;
    public final DragDownHelper touchHelper;
    public UdfpsKeyguardViewController udfpsKeyguardViewController;
    public boolean useSplitShade;

    public static /* synthetic */ void getDragDownAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public static /* synthetic */ void getPulseHeightAnimator$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
    }

    public final void setPulseHeight(float f, boolean z) {
        if (z) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(this.pulseHeight, f);
            ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofFloat.setDuration(375L);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$setPulseHeight$1
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    LockscreenShadeTransitionController lockscreenShadeTransitionController = LockscreenShadeTransitionController.this;
                    Object animatedValue = valueAnimator.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                    lockscreenShadeTransitionController.setPulseHeight(((Float) animatedValue).floatValue(), false);
                }
            });
            ofFloat.start();
            this.pulseHeightAnimator = ofFloat;
            return;
        }
        this.pulseHeight = f;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        if (notificationStackScrollLayoutController == null) {
            notificationStackScrollLayoutController = null;
        }
        Objects.requireNonNull(notificationStackScrollLayoutController);
        float pulseHeight = notificationStackScrollLayoutController.mView.setPulseHeight(f);
        NotificationPanelViewController notificationPanelController = getNotificationPanelController();
        Objects.requireNonNull(notificationPanelController);
        float height = pulseHeight / notificationPanelController.mView.getHeight();
        PathInterpolator pathInterpolator = Interpolators.EMPHASIZED;
        notificationPanelController.mOverStretchAmount = MathUtils.max(0.0f, (float) (1.0d - Math.exp(height * (-4.0f)))) * notificationPanelController.mMaxOverscrollAmountForPulse;
        notificationPanelController.positionClockAndNotifications(true);
        if (!this.keyguardBypassController.getBypassEnabled()) {
            f = 0.0f;
        }
        transitionToShadeAmountCommon(f);
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0018, code lost:
        if (r0.mDynamicPrivacyController.isInLockedDownShade() != false) goto L_0x001a;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean canDragDown$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        /*
            r3 = this;
            com.android.systemui.statusbar.SysuiStatusBarStateController r0 = r3.statusBarStateController
            int r0 = r0.getState()
            r1 = 0
            r2 = 1
            if (r0 == r2) goto L_0x001a
            com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController r0 = r3.nsslController
            if (r0 != 0) goto L_0x000f
            r0 = r1
        L_0x000f:
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.notification.DynamicPrivacyController r0 = r0.mDynamicPrivacyController
            boolean r0 = r0.isInLockedDownShade()
            if (r0 == 0) goto L_0x002a
        L_0x001a:
            com.android.systemui.plugins.qs.QS r0 = r3.qS
            if (r0 == 0) goto L_0x001f
            r1 = r0
        L_0x001f:
            boolean r0 = r1.isFullyCollapsed()
            if (r0 != 0) goto L_0x002b
            boolean r3 = r3.useSplitShade
            if (r3 == 0) goto L_0x002a
            goto L_0x002b
        L_0x002a:
            r2 = 0
        L_0x002b:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.LockscreenShadeTransitionController.canDragDown$frameworks__base__packages__SystemUI__android_common__SystemUI_core():boolean");
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        boolean z;
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("LSShadeTransitionController:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(Intrinsics.stringPlus("pulseHeight: ", Float.valueOf(this.pulseHeight)));
        indentingPrintWriter.println(Intrinsics.stringPlus("useSplitShade: ", Boolean.valueOf(this.useSplitShade)));
        indentingPrintWriter.println(Intrinsics.stringPlus("dragDownAmount: ", Float.valueOf(this.dragDownAmount)));
        indentingPrintWriter.println(Intrinsics.stringPlus("qSDragProgress: ", Float.valueOf(this.qSDragProgress)));
        indentingPrintWriter.println(Intrinsics.stringPlus("isDragDownAnywhereEnabled: ", Boolean.valueOf(isDragDownAnywhereEnabled$frameworks__base__packages__SystemUI__android_common__SystemUI_core())));
        boolean z2 = false;
        if (this.statusBarStateController.getState() == 1) {
            z = true;
        } else {
            z = false;
        }
        indentingPrintWriter.println(Intrinsics.stringPlus("isFalsingCheckNeeded: ", Boolean.valueOf(z)));
        indentingPrintWriter.println(Intrinsics.stringPlus("isWakingToShadeLocked: ", Boolean.valueOf(this.isWakingToShadeLocked)));
        if (this.animationHandlerOnKeyguardDismiss != null) {
            z2 = true;
        }
        indentingPrintWriter.println(Intrinsics.stringPlus("hasPendingHandlerOnKeyguardDismiss: ", Boolean.valueOf(z2)));
    }

    public final void finishPulseAnimation(boolean z) {
        this.logger.logPulseExpansionFinished(z);
        if (z) {
            setPulseHeight(0.0f, true);
            return;
        }
        NotificationPanelViewController notificationPanelController = getNotificationPanelController();
        Objects.requireNonNull(notificationPanelController);
        notificationPanelController.mAnimateNextNotificationBounds = true;
        notificationPanelController.mNotificationBoundsAnimationDuration = 448L;
        notificationPanelController.mNotificationBoundsAnimationDelay = 0L;
        notificationPanelController.mIsPulseExpansionResetAnimator = true;
        setPulseHeight(0.0f, false);
    }

    public final NotificationPanelViewController getNotificationPanelController() {
        NotificationPanelViewController notificationPanelViewController = this.notificationPanelController;
        if (notificationPanelViewController != null) {
            return notificationPanelViewController;
        }
        return null;
    }

    public final void goToLockedShade(View view, boolean z) {
        LockscreenShadeTransitionController$goToLockedShade$1 lockscreenShadeTransitionController$goToLockedShade$1;
        boolean z2 = true;
        if (this.statusBarStateController.getState() != 1) {
            z2 = false;
        }
        this.logger.logTryGoToLockedShade(z2);
        if (z2) {
            if (z) {
                lockscreenShadeTransitionController$goToLockedShade$1 = null;
            } else {
                lockscreenShadeTransitionController$goToLockedShade$1 = new LockscreenShadeTransitionController$goToLockedShade$1(this);
            }
            goToLockedShadeInternal(view, lockscreenShadeTransitionController$goToLockedShade$1, null);
        }
    }

    public final void goToLockedShadeInternal(View view, final Function1 function1, final LockscreenShadeTransitionController$onDraggedDown$cancelRunnable$1 lockscreenShadeTransitionController$onDraggedDown$cancelRunnable$1) {
        boolean z;
        NotificationEntry notificationEntry;
        boolean z2;
        ActivityStarter.OnDismissAction onDismissAction;
        StatusBar statusBar = this.statusbar;
        if (statusBar == null) {
            statusBar = null;
        }
        Objects.requireNonNull(statusBar);
        boolean z3 = false;
        if ((statusBar.mDisabled2 & 4) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            if (lockscreenShadeTransitionController$onDraggedDown$cancelRunnable$1 != null) {
                lockscreenShadeTransitionController$onDraggedDown$cancelRunnable$1.run();
            }
            this.logger.logShadeDisabledOnGoToLockedShade();
            return;
        }
        int currentUserId = this.lockScreenUserManager.getCurrentUserId();
        if (view instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
            Objects.requireNonNull(expandableNotificationRow);
            notificationEntry = expandableNotificationRow.mEntry;
            Objects.requireNonNull(notificationEntry);
            ExpandableNotificationRow expandableNotificationRow2 = notificationEntry.row;
            if (expandableNotificationRow2 != null) {
                expandableNotificationRow2.setUserExpanded(true, true);
            }
            ExpandableNotificationRow expandableNotificationRow3 = notificationEntry.row;
            if (expandableNotificationRow3 != null) {
                expandableNotificationRow3.mGroupExpansionChanging = true;
            }
            currentUserId = notificationEntry.mSbn.getUserId();
        } else {
            notificationEntry = null;
        }
        NotificationLockscreenUserManager notificationLockscreenUserManager = this.lockScreenUserManager;
        if (!notificationLockscreenUserManager.userAllowsPrivateNotificationsInPublic(notificationLockscreenUserManager.getCurrentUserId()) || !this.lockScreenUserManager.shouldShowLockscreenNotifications()) {
            z2 = true;
        } else {
            this.falsingCollector.shouldEnforceBouncer();
            z2 = false;
        }
        if (this.keyguardBypassController.getBypassEnabled()) {
            z2 = false;
        }
        if (!this.lockScreenUserManager.isLockscreenPublicMode(currentUserId) || !z2) {
            LSShadeTransitionLogger lSShadeTransitionLogger = this.logger;
            if (function1 != null) {
                z3 = true;
            }
            lSShadeTransitionLogger.logGoingToLockedShade(z3);
            if (this.statusBarStateController.isDozing()) {
                this.isWakingToShadeLocked = true;
            }
            this.statusBarStateController.setState(2);
            if (function1 != null) {
                function1.invoke(0L);
            } else {
                performDefaultGoToFullShadeAnimation(0L);
            }
        } else {
            this.statusBarStateController.setLeaveOpenOnKeyguardHide(true);
            if (function1 != null) {
                onDismissAction = new ActivityStarter.OnDismissAction() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$goToLockedShadeInternal$1
                    @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                    public final boolean onDismiss() {
                        LockscreenShadeTransitionController.this.animationHandlerOnKeyguardDismiss = function1;
                        return false;
                    }
                };
            } else {
                onDismissAction = null;
            }
            Runnable lockscreenShadeTransitionController$goToLockedShadeInternal$cancelHandler$1 = new Runnable() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$goToLockedShadeInternal$cancelHandler$1
                @Override // java.lang.Runnable
                public final void run() {
                    LockscreenShadeTransitionController lockscreenShadeTransitionController = LockscreenShadeTransitionController.this;
                    NotificationEntry notificationEntry2 = lockscreenShadeTransitionController.draggedDownEntry;
                    if (notificationEntry2 != null) {
                        ExpandableNotificationRow expandableNotificationRow4 = notificationEntry2.row;
                        if (expandableNotificationRow4 != null) {
                            expandableNotificationRow4.setUserLocked(false);
                        }
                        ExpandableNotificationRow expandableNotificationRow5 = notificationEntry2.row;
                        if (expandableNotificationRow5 != null) {
                            expandableNotificationRow5.notifyHeightChanged(false);
                        }
                        lockscreenShadeTransitionController.draggedDownEntry = null;
                    }
                    Runnable runnable = lockscreenShadeTransitionController$onDraggedDown$cancelRunnable$1;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            };
            this.logger.logShowBouncerOnGoToLockedShade();
            StatusBar statusBar2 = this.statusbar;
            if (statusBar2 == null) {
                statusBar2 = null;
            }
            Objects.requireNonNull(statusBar2);
            int i = statusBar2.mState;
            if (i == 1 || i == 2) {
                KeyguardViewMediator keyguardViewMediator = statusBar2.mKeyguardViewMediator;
                Objects.requireNonNull(keyguardViewMediator);
                if (!keyguardViewMediator.mHiding) {
                    StatusBarKeyguardViewManager statusBarKeyguardViewManager = statusBar2.mStatusBarKeyguardViewManager;
                    Objects.requireNonNull(statusBarKeyguardViewManager);
                    statusBarKeyguardViewManager.dismissWithAction(onDismissAction, lockscreenShadeTransitionController$goToLockedShadeInternal$cancelHandler$1, false, null);
                    this.draggedDownEntry = notificationEntry;
                }
            }
            lockscreenShadeTransitionController$goToLockedShadeInternal$cancelHandler$1.run();
            this.draggedDownEntry = notificationEntry;
        }
    }

    public final boolean isDragDownAnywhereEnabled$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        if (this.statusBarStateController.getState() == 1 && !this.keyguardBypassController.getBypassEnabled()) {
            QS qs = this.qS;
            if (qs == null) {
                qs = null;
            }
            if (qs.isFullyCollapsed() || this.useSplitShade) {
                return true;
            }
        }
        return false;
    }

    public final void performDefaultGoToFullShadeAnimation(long j) {
        this.logger.logDefaultGoToFullShadeAnimation(j);
        getNotificationPanelController().animateToFullShade(j);
        this.forceApplyAmount = true;
        setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(1.0f);
        setDragDownAmountAnimated(this.fullTransitionDistance, j, new LockscreenShadeTransitionController$animateAppear$1(this));
    }

    public final void setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(float f) {
        boolean z;
        float f2;
        float f3;
        boolean z2;
        boolean z3;
        boolean z4 = true;
        if (this.dragDownAmount == f) {
            z = true;
        } else {
            z = false;
        }
        if (!z || this.forceApplyAmount) {
            this.dragDownAmount = f;
            NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
            QS qs = null;
            if (notificationStackScrollLayoutController == null) {
                notificationStackScrollLayoutController = null;
            }
            Objects.requireNonNull(notificationStackScrollLayoutController);
            if (notificationStackScrollLayoutController.mDynamicPrivacyController.isInLockedDownShade()) {
                if (this.dragDownAmount == 0.0f) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (!z3 && !this.forceApplyAmount) {
                    return;
                }
            }
            float saturate = MathUtils.saturate(this.dragDownAmount / this.scrimTransitionDistance);
            this.qSDragProgress = saturate;
            NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.nsslController;
            if (notificationStackScrollLayoutController2 == null) {
                notificationStackScrollLayoutController2 = null;
            }
            float f4 = this.dragDownAmount;
            Objects.requireNonNull(notificationStackScrollLayoutController2);
            NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController2.mView;
            Objects.requireNonNull(notificationStackScrollLayout);
            NotificationShelf notificationShelf = notificationStackScrollLayout.mShelf;
            Objects.requireNonNull(notificationShelf);
            notificationShelf.mFractionToShade = saturate;
            notificationStackScrollLayout.requestChildrenUpdate();
            if (notificationStackScrollLayoutController2.mStatusBarStateController.getState() == 1) {
                float saturate2 = MathUtils.saturate(f4 / notificationStackScrollLayoutController2.mView.getHeight());
                float height = notificationStackScrollLayoutController2.mTotalDistanceForFullShadeTransition / notificationStackScrollLayoutController2.mView.getHeight();
                PathInterpolator pathInterpolator = Interpolators.EMPHASIZED;
                if (height != 0.0f) {
                    f2 = MathUtils.max(0.0f, ((float) (1.0d - Math.exp((-(MathUtils.log(2.6666665f) / height)) * saturate2))) * 1.6f) * notificationStackScrollLayoutController2.mNotificationDragDownMovement;
                } else {
                    throw new IllegalArgumentException("Invalid values for overshoot");
                }
            } else {
                f2 = 0.0f;
            }
            NotificationStackScrollLayout notificationStackScrollLayout2 = notificationStackScrollLayoutController2.mView;
            Objects.requireNonNull(notificationStackScrollLayout2);
            notificationStackScrollLayout2.mExtraTopInsetForFullShadeTransition = f2;
            notificationStackScrollLayout2.updateStackPosition(false);
            notificationStackScrollLayout2.requestChildrenUpdate();
            QS qs2 = this.qS;
            if (qs2 != null) {
                qs = qs2;
            }
            qs.setTransitionToFullShadeAmount(this.dragDownAmount, this.qSDragProgress);
            getNotificationPanelController().setTransitionToFullShadeAmount(this.dragDownAmount, false, 0L);
            if (this.useSplitShade) {
                f3 = 0.0f;
            } else {
                f3 = this.dragDownAmount;
            }
            MediaHierarchyManager mediaHierarchyManager = this.mediaHierarchyManager;
            Objects.requireNonNull(mediaHierarchyManager);
            float saturate3 = MathUtils.saturate(f3 / mediaHierarchyManager.distanceForFullShadeTransition);
            if (mediaHierarchyManager.fullShadeTransitionProgress == saturate3) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!z2) {
                mediaHierarchyManager.fullShadeTransitionProgress = saturate3;
                if (!mediaHierarchyManager.bypassController.getBypassEnabled() && mediaHierarchyManager.statusbarState == 1) {
                    MediaHierarchyManager.updateDesiredLocation$default(mediaHierarchyManager, mediaHierarchyManager.isCurrentlyFading(), 2);
                    if (saturate3 >= 0.0f) {
                        mediaHierarchyManager.updateTargetState();
                        float f5 = mediaHierarchyManager.fullShadeTransitionProgress;
                        float f6 = 1.0f;
                        if (f5 <= 0.5f) {
                            f6 = 1.0f - (f5 / 0.5f);
                        }
                        if (mediaHierarchyManager.carouselAlpha != f6) {
                            z4 = false;
                        }
                        if (!z4) {
                            mediaHierarchyManager.carouselAlpha = f6;
                            R$raw.fadeIn((View) mediaHierarchyManager.getMediaFrame(), f6, false);
                        }
                        mediaHierarchyManager.applyTargetStateIfNotAnimating();
                    }
                }
            }
            transitionToShadeAmountCommon(this.dragDownAmount);
        }
    }

    public final void setDragDownAmountAnimated(float f, long j, final Function0<Unit> function0) {
        this.logger.logDragDownAnimation(f);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(this.dragDownAmount, f);
        ofFloat.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat.setDuration(375L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$setDragDownAmountAnimated$1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                LockscreenShadeTransitionController lockscreenShadeTransitionController = LockscreenShadeTransitionController.this;
                Object animatedValue = valueAnimator.getAnimatedValue();
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Float");
                lockscreenShadeTransitionController.setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(((Float) animatedValue).floatValue());
            }
        });
        if (j > 0) {
            ofFloat.setStartDelay(j);
        }
        if (function0 != null) {
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController$setDragDownAmountAnimated$2
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public final void onAnimationEnd(Animator animator) {
                    function0.invoke();
                }
            });
        }
        ofFloat.start();
        this.dragDownAnimator = ofFloat;
    }

    public final void transitionToShadeAmountCommon(float f) {
        boolean z;
        float saturate = MathUtils.saturate(f / this.scrimTransitionDistance);
        ScrimController scrimController = this.scrimController;
        Objects.requireNonNull(scrimController);
        boolean z2 = false;
        if (saturate != scrimController.mTransitionToFullShadeProgress) {
            scrimController.mTransitionToFullShadeProgress = saturate;
            if (saturate > 0.0f) {
                z = true;
            } else {
                z = false;
            }
            if (z != scrimController.mTransitioningToFullShade) {
                scrimController.mTransitioningToFullShade = z;
                if (z) {
                    ScrimState.SHADE_LOCKED.prepare(scrimController.mState);
                }
            }
            scrimController.applyAndDispatchState();
        }
        NotificationPanelViewController notificationPanelController = getNotificationPanelController();
        Objects.requireNonNull(notificationPanelController);
        float interpolation = Interpolators.ALPHA_IN.getInterpolation(1.0f - saturate);
        notificationPanelController.mKeyguardOnlyContentAlpha = interpolation;
        if (notificationPanelController.mBarState == 1) {
            notificationPanelController.mBottomAreaShadeAlpha = interpolation;
            notificationPanelController.updateKeyguardBottomAreaAlpha();
        }
        notificationPanelController.updateClock();
        CommunalHostViewController communalHostViewController = notificationPanelController.mCommunalViewController;
        if (communalHostViewController != null) {
            float f2 = notificationPanelController.mKeyguardOnlyContentAlpha;
            KeyguardVisibilityHelper keyguardVisibilityHelper = communalHostViewController.mKeyguardVisibilityHelper;
            Objects.requireNonNull(keyguardVisibilityHelper);
            if (!keyguardVisibilityHelper.mKeyguardViewVisibilityAnimating) {
                ((CommunalHostView) communalHostViewController.mView).setAlpha(f2);
                int childCount = ((CommunalHostView) communalHostViewController.mView).getChildCount();
                while (true) {
                    childCount--;
                    if (childCount < 0) {
                        break;
                    }
                    ((CommunalHostView) communalHostViewController.mView).getChildAt(childCount).setAlpha(f2);
                }
            }
        }
        NotificationShadeDepthController notificationShadeDepthController = this.depthController;
        Objects.requireNonNull(notificationShadeDepthController);
        if (notificationShadeDepthController.transitionToFullShadeProgress == saturate) {
            z2 = true;
        }
        StatusBar statusBar = null;
        if (!z2) {
            notificationShadeDepthController.transitionToFullShadeProgress = saturate;
            notificationShadeDepthController.scheduleUpdate(null);
        }
        UdfpsKeyguardViewController udfpsKeyguardViewController = this.udfpsKeyguardViewController;
        if (udfpsKeyguardViewController != null) {
            udfpsKeyguardViewController.mTransitionToFullShadeProgress = saturate;
            udfpsKeyguardViewController.updateAlpha();
        }
        StatusBar statusBar2 = this.statusbar;
        if (statusBar2 != null) {
            statusBar = statusBar2;
        }
        Objects.requireNonNull(statusBar);
        statusBar.mTransitionToFullShadeProgress = saturate;
    }

    public final void updateResources() {
        this.scrimTransitionDistance = this.context.getResources().getDimensionPixelSize(2131166126);
        this.fullTransitionDistance = this.context.getResources().getDimensionPixelSize(2131166125);
        this.useSplitShade = Utils.shouldUseSplitNotificationShade(this.context.getResources());
    }

    public LockscreenShadeTransitionController(SysuiStatusBarStateController sysuiStatusBarStateController, LSShadeTransitionLogger lSShadeTransitionLogger, KeyguardBypassController keyguardBypassController, NotificationLockscreenUserManager notificationLockscreenUserManager, FalsingCollector falsingCollector, AmbientState ambientState, MediaHierarchyManager mediaHierarchyManager, ScrimController scrimController, NotificationShadeDepthController notificationShadeDepthController, Context context, WakefulnessLifecycle wakefulnessLifecycle, ConfigurationController configurationController, FalsingManager falsingManager, DumpManager dumpManager) {
        this.statusBarStateController = sysuiStatusBarStateController;
        this.logger = lSShadeTransitionLogger;
        this.keyguardBypassController = keyguardBypassController;
        this.lockScreenUserManager = notificationLockscreenUserManager;
        this.falsingCollector = falsingCollector;
        this.ambientState = ambientState;
        this.mediaHierarchyManager = mediaHierarchyManager;
        this.scrimController = scrimController;
        this.depthController = notificationShadeDepthController;
        this.context = context;
        this.touchHelper = new DragDownHelper(falsingManager, falsingCollector, this, context);
        updateResources();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController.1
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public final void onConfigChanged(Configuration configuration) {
                LockscreenShadeTransitionController.this.updateResources();
                LockscreenShadeTransitionController lockscreenShadeTransitionController = LockscreenShadeTransitionController.this;
                Objects.requireNonNull(lockscreenShadeTransitionController);
                lockscreenShadeTransitionController.touchHelper.updateResources(LockscreenShadeTransitionController.this.context);
            }
        });
        dumpManager.registerDumpable(this);
        sysuiStatusBarStateController.addCallback(new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController.2
            @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
            public final void onExpandedChanged(boolean z) {
                boolean z2;
                boolean z3;
                boolean z4;
                if (!z) {
                    LockscreenShadeTransitionController lockscreenShadeTransitionController = LockscreenShadeTransitionController.this;
                    Objects.requireNonNull(lockscreenShadeTransitionController);
                    boolean z5 = true;
                    if (lockscreenShadeTransitionController.dragDownAmount == 0.0f) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (!z2) {
                        LockscreenShadeTransitionController lockscreenShadeTransitionController2 = LockscreenShadeTransitionController.this;
                        Objects.requireNonNull(lockscreenShadeTransitionController2);
                        ValueAnimator valueAnimator = lockscreenShadeTransitionController2.dragDownAnimator;
                        if (valueAnimator != null && valueAnimator.isRunning()) {
                            z4 = true;
                        } else {
                            z4 = false;
                        }
                        if (!z4) {
                            LockscreenShadeTransitionController.this.logger.logDragDownAmountResetWhenFullyCollapsed();
                            LockscreenShadeTransitionController.this.setDragDownAmount$frameworks__base__packages__SystemUI__android_common__SystemUI_core(0.0f);
                        }
                    }
                    LockscreenShadeTransitionController lockscreenShadeTransitionController3 = LockscreenShadeTransitionController.this;
                    if (lockscreenShadeTransitionController3.pulseHeight == 0.0f) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (!z3) {
                        Objects.requireNonNull(lockscreenShadeTransitionController3);
                        ValueAnimator valueAnimator2 = lockscreenShadeTransitionController3.pulseHeightAnimator;
                        if (valueAnimator2 == null || !valueAnimator2.isRunning()) {
                            z5 = false;
                        }
                        if (!z5) {
                            LockscreenShadeTransitionController.this.logger.logPulseHeightNotResetWhenFullyCollapsed();
                            LockscreenShadeTransitionController.this.setPulseHeight(0.0f, false);
                        }
                    }
                }
            }
        });
        wakefulnessLifecycle.mObservers.add(new WakefulnessLifecycle.Observer() { // from class: com.android.systemui.statusbar.LockscreenShadeTransitionController.3
            @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
            public final void onPostFinishedWakingUp() {
                LockscreenShadeTransitionController.this.isWakingToShadeLocked = false;
            }
        });
    }

    public final boolean isDragDownEnabledForView$frameworks__base__packages__SystemUI__android_common__SystemUI_core(ExpandableView expandableView) {
        if (isDragDownAnywhereEnabled$frameworks__base__packages__SystemUI__android_common__SystemUI_core()) {
            return true;
        }
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.nsslController;
        if (notificationStackScrollLayoutController == null) {
            notificationStackScrollLayoutController = null;
        }
        Objects.requireNonNull(notificationStackScrollLayoutController);
        if (!notificationStackScrollLayoutController.mDynamicPrivacyController.isInLockedDownShade()) {
            return false;
        }
        if (expandableView == null) {
            return true;
        }
        if (!(expandableView instanceof ExpandableNotificationRow)) {
            return false;
        }
        NotificationEntry notificationEntry = ((ExpandableNotificationRow) expandableView).mEntry;
        Objects.requireNonNull(notificationEntry);
        return notificationEntry.mSensitive;
    }
}
