package com.android.systemui.statusbar;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.os.PowerManager;
import android.util.IndentingPrintWriter;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import com.android.systemui.Dumpable;
import com.android.systemui.Gefingerpoken;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.NotificationRoundnessManager;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.HeadsUpManagerPhone;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PulseExpansionHandler.kt */
/* loaded from: classes.dex */
public final class PulseExpansionHandler implements Gefingerpoken, Dumpable {
    public boolean bouncerShowing;
    public final KeyguardBypassController bypassController;
    public final ConfigurationController configurationController;
    public final FalsingCollector falsingCollector;
    public final FalsingManager falsingManager;
    public final HeadsUpManagerPhone headsUpManager;
    public boolean isExpanding;
    public boolean leavingLockscreen;
    public final LockscreenShadeTransitionController lockscreenShadeTransitionController;
    public float mInitialTouchX;
    public float mInitialTouchY;
    public final PowerManager mPowerManager;
    public boolean mPulsing;
    public ExpandableView mStartingChild;
    public final int[] mTemp2 = new int[2];
    public Runnable pulseExpandAbortListener;
    public boolean qsExpanded;
    public final NotificationRoundnessManager roundnessManager;
    public NotificationStackScrollLayoutController stackScrollerController;
    public final StatusBarStateController statusBarStateController;
    public float touchSlop;
    public VelocityTracker velocityTracker;
    public final NotificationWakeUpCoordinator wakeUpCoordinator;

    public final void cancelExpansion() {
        setExpanding(false);
        this.falsingCollector.onExpansionFromPulseStopped();
        final ExpandableView expandableView = this.mStartingChild;
        if (expandableView != null) {
            if (expandableView.mActualHeight != expandableView.getCollapsedHeight()) {
                ObjectAnimator ofInt = ObjectAnimator.ofInt(expandableView, "actualHeight", expandableView.mActualHeight, expandableView.getCollapsedHeight());
                ofInt.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
                ofInt.setDuration(375);
                ofInt.addListener(new AnimatorListenerAdapter() { // from class: com.android.systemui.statusbar.PulseExpansionHandler$reset$1
                    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                    public final void onAnimationEnd(Animator animator) {
                        PulseExpansionHandler pulseExpansionHandler = PulseExpansionHandler.this;
                        ExpandableView expandableView2 = expandableView;
                        Objects.requireNonNull(pulseExpansionHandler);
                        if (expandableView2 instanceof ExpandableNotificationRow) {
                            ((ExpandableNotificationRow) expandableView2).setUserLocked(false);
                        }
                    }
                });
                ofInt.start();
            } else if (expandableView instanceof ExpandableNotificationRow) {
                ((ExpandableNotificationRow) expandableView).setUserLocked(false);
            }
            this.mStartingChild = null;
        }
        this.lockscreenShadeTransitionController.finishPulseAnimation(true);
        this.wakeUpCoordinator.setNotificationsVisibleForExpansion(false, true, false);
    }

    @Override // com.android.systemui.Dumpable
    public final void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("PulseExpansionHandler:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(Intrinsics.stringPlus("isExpanding: ", Boolean.valueOf(this.isExpanding)));
        indentingPrintWriter.println(Intrinsics.stringPlus("leavingLockscreen: ", Boolean.valueOf(this.leavingLockscreen)));
        indentingPrintWriter.println(Intrinsics.stringPlus("mPulsing: ", Boolean.valueOf(this.mPulsing)));
        indentingPrintWriter.println(Intrinsics.stringPlus("qsExpanded: ", Boolean.valueOf(this.qsExpanded)));
        indentingPrintWriter.println(Intrinsics.stringPlus("bouncerShowing: ", Boolean.valueOf(this.bouncerShowing)));
    }

    @Override // com.android.systemui.Gefingerpoken
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        boolean z;
        if (!this.wakeUpCoordinator.getCanShowPulsingHuns() || this.qsExpanded || this.bouncerShowing) {
            z = false;
        } else {
            z = true;
        }
        if (!z || !startExpansion(motionEvent)) {
            return false;
        }
        return true;
    }

    public final void setExpanding(boolean z) {
        boolean z2;
        if (this.isExpanding != z) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.isExpanding = z;
        KeyguardBypassController keyguardBypassController = this.bypassController;
        Objects.requireNonNull(keyguardBypassController);
        keyguardBypassController.isPulseExpanding = z;
        if (z2) {
            if (z) {
                NotificationEntry topEntry = this.headsUpManager.getTopEntry();
                if (topEntry != null) {
                    NotificationRoundnessManager notificationRoundnessManager = this.roundnessManager;
                    ExpandableNotificationRow expandableNotificationRow = topEntry.row;
                    Objects.requireNonNull(notificationRoundnessManager);
                    ExpandableNotificationRow expandableNotificationRow2 = notificationRoundnessManager.mTrackedHeadsUp;
                    notificationRoundnessManager.mTrackedHeadsUp = expandableNotificationRow;
                    if (expandableNotificationRow2 != null) {
                        notificationRoundnessManager.updateView(expandableNotificationRow2, true);
                    }
                }
                LockscreenShadeTransitionController lockscreenShadeTransitionController = this.lockscreenShadeTransitionController;
                Objects.requireNonNull(lockscreenShadeTransitionController);
                lockscreenShadeTransitionController.logger.logPulseExpansionStarted();
                ValueAnimator valueAnimator = lockscreenShadeTransitionController.pulseHeightAnimator;
                if (valueAnimator != null && valueAnimator.isRunning()) {
                    lockscreenShadeTransitionController.logger.logAnimationCancelled(true);
                    valueAnimator.cancel();
                }
            } else {
                NotificationRoundnessManager notificationRoundnessManager2 = this.roundnessManager;
                Objects.requireNonNull(notificationRoundnessManager2);
                ExpandableNotificationRow expandableNotificationRow3 = notificationRoundnessManager2.mTrackedHeadsUp;
                notificationRoundnessManager2.mTrackedHeadsUp = null;
                if (expandableNotificationRow3 != null) {
                    notificationRoundnessManager2.updateView(expandableNotificationRow3, true);
                }
                if (!this.leavingLockscreen) {
                    this.bypassController.maybePerformPendingUnlock();
                    Runnable runnable = this.pulseExpandAbortListener;
                    if (runnable != null) {
                        runnable.run();
                    }
                }
            }
            this.headsUpManager.unpinAll();
        }
    }

    public final boolean startExpansion(MotionEvent motionEvent) {
        if (this.velocityTracker == null) {
            this.velocityTracker = VelocityTracker.obtain();
        }
        VelocityTracker velocityTracker = this.velocityTracker;
        Intrinsics.checkNotNull(velocityTracker);
        velocityTracker.addMovement(motionEvent);
        float x = motionEvent.getX();
        float y = motionEvent.getY();
        int actionMasked = motionEvent.getActionMasked();
        ExpandableNotificationRow expandableNotificationRow = null;
        if (actionMasked == 0) {
            setExpanding(false);
            this.leavingLockscreen = false;
            this.mStartingChild = null;
            this.mInitialTouchY = y;
            this.mInitialTouchX = x;
        } else if (actionMasked == 1) {
            VelocityTracker velocityTracker2 = this.velocityTracker;
            if (velocityTracker2 != null) {
                velocityTracker2.recycle();
            }
            this.velocityTracker = null;
            setExpanding(false);
        } else if (actionMasked == 2) {
            float f = y - this.mInitialTouchY;
            if (f > this.touchSlop && f > Math.abs(x - this.mInitialTouchX)) {
                this.falsingCollector.onStartExpandingFromPulse();
                setExpanding(true);
                float f2 = this.mInitialTouchX;
                float f3 = this.mInitialTouchY;
                if (this.mStartingChild == null && !this.bypassController.getBypassEnabled()) {
                    NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.stackScrollerController;
                    if (notificationStackScrollLayoutController == null) {
                        notificationStackScrollLayoutController = null;
                    }
                    int[] iArr = this.mTemp2;
                    Objects.requireNonNull(notificationStackScrollLayoutController);
                    notificationStackScrollLayoutController.mView.getLocationOnScreen(iArr);
                    int[] iArr2 = this.mTemp2;
                    float f4 = f2 + iArr2[0];
                    float f5 = f3 + iArr2[1];
                    NotificationStackScrollLayoutController notificationStackScrollLayoutController2 = this.stackScrollerController;
                    if (notificationStackScrollLayoutController2 == null) {
                        notificationStackScrollLayoutController2 = null;
                    }
                    Objects.requireNonNull(notificationStackScrollLayoutController2);
                    ExpandableView childAtRawPosition = notificationStackScrollLayoutController2.mView.getChildAtRawPosition(f4, f5);
                    if (childAtRawPosition != null && childAtRawPosition.isContentExpandable()) {
                        expandableNotificationRow = childAtRawPosition;
                    }
                    this.mStartingChild = expandableNotificationRow;
                    if (expandableNotificationRow != null && (expandableNotificationRow instanceof ExpandableNotificationRow)) {
                        expandableNotificationRow.setUserLocked(true);
                    }
                }
                this.mInitialTouchY = y;
                this.mInitialTouchX = x;
                return true;
            }
        } else if (actionMasked == 3) {
            VelocityTracker velocityTracker3 = this.velocityTracker;
            if (velocityTracker3 != null) {
                velocityTracker3.recycle();
            }
            this.velocityTracker = null;
            setExpanding(false);
        }
        return false;
    }

    public PulseExpansionHandler(final Context context, NotificationWakeUpCoordinator notificationWakeUpCoordinator, KeyguardBypassController keyguardBypassController, HeadsUpManagerPhone headsUpManagerPhone, NotificationRoundnessManager notificationRoundnessManager, ConfigurationController configurationController, StatusBarStateController statusBarStateController, FalsingManager falsingManager, LockscreenShadeTransitionController lockscreenShadeTransitionController, FalsingCollector falsingCollector, DumpManager dumpManager) {
        this.wakeUpCoordinator = notificationWakeUpCoordinator;
        this.bypassController = keyguardBypassController;
        this.headsUpManager = headsUpManagerPhone;
        this.roundnessManager = notificationRoundnessManager;
        this.configurationController = configurationController;
        this.statusBarStateController = statusBarStateController;
        this.falsingManager = falsingManager;
        this.lockscreenShadeTransitionController = lockscreenShadeTransitionController;
        this.falsingCollector = falsingCollector;
        context.getResources().getDimensionPixelSize(2131165845);
        this.touchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        configurationController.addCallback(new ConfigurationController.ConfigurationListener() { // from class: com.android.systemui.statusbar.PulseExpansionHandler.1
            @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
            public final void onConfigChanged(Configuration configuration) {
                PulseExpansionHandler pulseExpansionHandler = PulseExpansionHandler.this;
                Context context2 = context;
                Objects.requireNonNull(pulseExpansionHandler);
                context2.getResources().getDimensionPixelSize(2131165845);
                pulseExpansionHandler.touchSlop = ViewConfiguration.get(context2).getScaledTouchSlop();
            }
        });
        this.mPowerManager = (PowerManager) context.getSystemService(PowerManager.class);
        dumpManager.registerDumpable(this);
    }

    /* JADX WARN: Removed duplicated region for block: B:78:0x013a  */
    @Override // com.android.systemui.Gefingerpoken
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onTouchEvent(android.view.MotionEvent r9) {
        /*
            Method dump skipped, instructions count: 327
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.PulseExpansionHandler.onTouchEvent(android.view.MotionEvent):boolean");
    }
}
