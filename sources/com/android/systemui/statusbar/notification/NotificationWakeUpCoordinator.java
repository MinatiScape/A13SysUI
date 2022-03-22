package com.android.systemui.statusbar.notification;

import android.animation.ObjectAnimator;
import android.util.FloatProperty;
import android.view.animation.PathInterpolator;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.AmbientState;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.phone.ScreenOffAnimationController;
import com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Objects;
/* compiled from: NotificationWakeUpCoordinator.kt */
/* loaded from: classes.dex */
public final class NotificationWakeUpCoordinator implements OnHeadsUpChangedListener, StatusBarStateController.StateListener, PanelExpansionListener {
    public final KeyguardBypassController bypassController;
    public boolean collapsedEnoughToHide;
    public final DozeParameters dozeParameters;
    public boolean fullyAwake;
    public float mDozeAmount;
    public final HeadsUpManager mHeadsUpManager;
    public float mLinearDozeAmount;
    public float mLinearVisibilityAmount;
    public boolean mNotificationsVisible;
    public boolean mNotificationsVisibleForExpansion;
    public NotificationStackScrollLayoutController mStackScrollerController;
    public float mVisibilityAmount;
    public ObjectAnimator mVisibilityAnimator;
    public boolean notificationsFullyHidden;
    public boolean pulseExpanding;
    public boolean pulsing;
    public final ScreenOffAnimationController screenOffAnimationController;
    public final StatusBarStateController statusBarStateController;
    public final ArrayList<WakeUpListener> wakeUpListeners;
    public boolean wakingUp;
    public boolean willWakeUp;
    public final NotificationWakeUpCoordinator$mNotificationVisibility$1 mNotificationVisibility = new FloatProperty<NotificationWakeUpCoordinator>() { // from class: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator$mNotificationVisibility$1
        @Override // android.util.Property
        public final Float get(Object obj) {
            return Float.valueOf(((NotificationWakeUpCoordinator) obj).mLinearVisibilityAmount);
        }

        @Override // android.util.FloatProperty
        public final void setValue(NotificationWakeUpCoordinator notificationWakeUpCoordinator, float f) {
            notificationWakeUpCoordinator.setVisibilityAmount(f);
        }
    };
    public PathInterpolator mVisibilityInterpolator = Interpolators.FAST_OUT_SLOW_IN_REVERSE;
    public final LinkedHashSet mEntrySetToClearWhenFinished = new LinkedHashSet();
    public int state = 1;

    /* compiled from: NotificationWakeUpCoordinator.kt */
    /* loaded from: classes.dex */
    public interface WakeUpListener {
        default void onFullyHiddenChanged(boolean z) {
        }

        default void onPulseExpansionChanged(boolean z) {
        }
    }

    public final boolean getCanShowPulsingHuns() {
        boolean z;
        boolean z2 = this.pulsing;
        if (!this.bypassController.getBypassEnabled()) {
            return z2;
        }
        if (z2 || ((this.wakingUp || this.willWakeUp || this.fullyAwake) && this.statusBarStateController.getState() == 1)) {
            z = true;
        } else {
            z = false;
        }
        if (this.collapsedEnoughToHide) {
            return false;
        }
        return z;
    }

    public final void notifyAnimationStart(boolean z) {
        float f;
        PathInterpolator pathInterpolator;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController == null) {
            notificationStackScrollLayoutController = null;
        }
        boolean z2 = !z;
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        float f2 = notificationStackScrollLayout.mInterpolatedHideAmount;
        if (f2 == 0.0f || f2 == 1.0f) {
            if (z2) {
                f = 1.8f;
            } else {
                f = 1.5f;
            }
            notificationStackScrollLayout.mBackgroundXFactor = f;
            if (z2) {
                pathInterpolator = Interpolators.FAST_OUT_SLOW_IN_REVERSE;
            } else {
                pathInterpolator = Interpolators.FAST_OUT_SLOW_IN;
            }
            notificationStackScrollLayout.mHideXInterpolator = pathInterpolator;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:30:0x0041, code lost:
        if (r0 != false) goto L_0x0043;
     */
    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onDozeAmountChanged(float r6, float r7) {
        /*
            r5 = this;
            com.android.systemui.statusbar.phone.ScreenOffAnimationController r0 = r5.screenOffAnimationController
            boolean r0 = r0.overrideNotificationsFullyDozingOnKeyguard()
            r1 = 1
            r2 = 1065353216(0x3f800000, float:1.0)
            r3 = 0
            if (r0 == 0) goto L_0x0011
            r5.setDozeAmount(r2, r2)
            r0 = r1
            goto L_0x0012
        L_0x0011:
            r0 = r3
        L_0x0012:
            if (r0 == 0) goto L_0x0015
            return
        L_0x0015:
            boolean r0 = r5.overrideDozeAmountIfBypass()
            if (r0 == 0) goto L_0x001c
            return
        L_0x001c:
            int r0 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x0022
            r0 = r1
            goto L_0x0023
        L_0x0022:
            r0 = r3
        L_0x0023:
            if (r0 != 0) goto L_0x004c
            r0 = 0
            int r4 = (r6 > r0 ? 1 : (r6 == r0 ? 0 : -1))
            if (r4 != 0) goto L_0x002c
            r4 = r1
            goto L_0x002d
        L_0x002c:
            r4 = r3
        L_0x002d:
            if (r4 != 0) goto L_0x004c
            float r4 = r5.mLinearDozeAmount
            int r0 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r0 != 0) goto L_0x0037
            r0 = r1
            goto L_0x0038
        L_0x0037:
            r0 = r3
        L_0x0038:
            if (r0 != 0) goto L_0x0043
            int r0 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x0040
            r0 = r1
            goto L_0x0041
        L_0x0040:
            r0 = r3
        L_0x0041:
            if (r0 == 0) goto L_0x004c
        L_0x0043:
            int r0 = (r4 > r2 ? 1 : (r4 == r2 ? 0 : -1))
            if (r0 != 0) goto L_0x0048
            goto L_0x0049
        L_0x0048:
            r1 = r3
        L_0x0049:
            r5.notifyAnimationStart(r1)
        L_0x004c:
            r5.setDozeAmount(r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.onDozeAmountChanged(float, float):void");
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onDozingChanged(boolean z) {
        if (z) {
            setNotificationsVisible(false, false, false);
        }
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public final void onStateChanged(int i) {
        boolean z;
        if (this.screenOffAnimationController.overrideNotificationsFullyDozingOnKeyguard() && this.state == 1 && i == 0) {
            setDozeAmount(0.0f, 0.0f);
        }
        if (this.screenOffAnimationController.overrideNotificationsFullyDozingOnKeyguard()) {
            setDozeAmount(1.0f, 1.0f);
            z = true;
        } else {
            z = false;
        }
        if (!z && !overrideDozeAmountIfBypass()) {
            if (this.bypassController.getBypassEnabled() && i == 1 && this.state == 2 && (!this.statusBarStateController.isDozing() || shouldAnimateVisibility())) {
                setNotificationsVisible(true, false, false);
                setNotificationsVisible(false, true, false);
            }
            this.state = i;
        }
    }

    public final boolean overrideDozeAmountIfBypass() {
        if (!this.bypassController.getBypassEnabled()) {
            return false;
        }
        float f = 1.0f;
        if (this.statusBarStateController.getState() == 0 || this.statusBarStateController.getState() == 2) {
            f = 0.0f;
        }
        setDozeAmount(f, f);
        return true;
    }

    public final void setDozeAmount(float f, float f2) {
        boolean z;
        boolean z2 = true;
        if (f == this.mLinearDozeAmount) {
            z = true;
        } else {
            z = false;
        }
        boolean z3 = !z;
        this.mLinearDozeAmount = f;
        this.mDozeAmount = f2;
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController == null) {
            notificationStackScrollLayoutController = null;
        }
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
        Objects.requireNonNull(ambientState);
        if (f2 != ambientState.mDozeAmount) {
            ambientState.mDozeAmount = f2;
            if ((f2 == 0.0f || f2 == 1.0f) && 100000.0f != ambientState.mPulseHeight) {
                ambientState.mPulseHeight = 100000.0f;
                Runnable runnable = ambientState.mOnPulseHeightChangedListener;
                if (runnable != null) {
                    runnable.run();
                }
            }
        }
        notificationStackScrollLayout.updateContinuousBackgroundDrawing();
        notificationStackScrollLayout.requestChildrenUpdate();
        updateHideAmount();
        if (z3) {
            if (f != 0.0f) {
                z2 = false;
            }
            if (z2) {
                setNotificationsVisible(false, false, false);
                setNotificationsVisibleForExpansion(false, false, false);
            }
        }
    }

    public final void setNotificationsVisible(boolean z, boolean z2, boolean z3) {
        PathInterpolator pathInterpolator;
        if (this.mNotificationsVisible != z) {
            this.mNotificationsVisible = z;
            ObjectAnimator objectAnimator = this.mVisibilityAnimator;
            if (objectAnimator != null) {
                objectAnimator.cancel();
            }
            float f = 1.0f;
            if (z2) {
                notifyAnimationStart(z);
                boolean z4 = this.mNotificationsVisible;
                if (z4) {
                    pathInterpolator = Interpolators.TOUCH_RESPONSE;
                } else {
                    pathInterpolator = Interpolators.FAST_OUT_SLOW_IN_REVERSE;
                }
                this.mVisibilityInterpolator = pathInterpolator;
                if (!z4) {
                    f = 0.0f;
                }
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(this, this.mNotificationVisibility, f);
                ofFloat.setInterpolator(Interpolators.LINEAR);
                long j = 500;
                if (z3) {
                    j = ((float) 500) / 1.5f;
                }
                ofFloat.setDuration(j);
                ofFloat.start();
                this.mVisibilityAnimator = ofFloat;
                return;
            }
            if (!z) {
                f = 0.0f;
            }
            setVisibilityAmount(f);
        }
    }

    public final void setNotificationsVisibleForExpansion(boolean z, boolean z2, boolean z3) {
        this.mNotificationsVisibleForExpansion = z;
        updateNotificationVisibility(z2, z3);
        if (!z && this.mNotificationsVisible) {
            this.mHeadsUpManager.releaseAllImmediately();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:10:0x0020, code lost:
        if (r1 != false) goto L_0x0022;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setVisibilityAmount(float r4) {
        /*
            r3 = this;
            r3.mLinearVisibilityAmount = r4
            android.view.animation.PathInterpolator r0 = r3.mVisibilityInterpolator
            float r4 = r0.getInterpolation(r4)
            r3.mVisibilityAmount = r4
            float r4 = r3.mLinearDozeAmount
            r0 = 0
            int r4 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            r1 = 1
            r2 = 0
            if (r4 != 0) goto L_0x0015
            r4 = r1
            goto L_0x0016
        L_0x0015:
            r4 = r2
        L_0x0016:
            if (r4 != 0) goto L_0x0022
            float r4 = r3.mLinearVisibilityAmount
            int r4 = (r4 > r0 ? 1 : (r4 == r0 ? 0 : -1))
            if (r4 != 0) goto L_0x001f
            goto L_0x0020
        L_0x001f:
            r1 = r2
        L_0x0020:
            if (r1 == 0) goto L_0x0044
        L_0x0022:
            java.util.LinkedHashSet r4 = r3.mEntrySetToClearWhenFinished
            java.util.Iterator r4 = r4.iterator()
        L_0x0028:
            boolean r0 = r4.hasNext()
            if (r0 == 0) goto L_0x003f
            java.lang.Object r0 = r4.next()
            com.android.systemui.statusbar.notification.collection.NotificationEntry r0 = (com.android.systemui.statusbar.notification.collection.NotificationEntry) r0
            java.util.Objects.requireNonNull(r0)
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r0 = r0.row
            if (r0 == 0) goto L_0x0028
            r0.setHeadsUpAnimatingAway(r2)
            goto L_0x0028
        L_0x003f:
            java.util.LinkedHashSet r4 = r3.mEntrySetToClearWhenFinished
            r4.clear()
        L_0x0044:
            r3.updateHideAmount()
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.setVisibilityAmount(float):void");
    }

    public final void setWakingUp(boolean z) {
        float f;
        boolean z2;
        NotificationShelf notificationShelf;
        int i;
        this.wakingUp = z;
        this.willWakeUp = false;
        if (z) {
            if (this.mNotificationsVisible && !this.mNotificationsVisibleForExpansion && !this.bypassController.getBypassEnabled()) {
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
                if (notificationStackScrollLayoutController == null) {
                    notificationStackScrollLayoutController = null;
                }
                Objects.requireNonNull(notificationStackScrollLayoutController);
                NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
                Objects.requireNonNull(notificationStackScrollLayout);
                ExpandableView firstChildWithBackground = notificationStackScrollLayout.getFirstChildWithBackground();
                if (firstChildWithBackground != null) {
                    if (notificationStackScrollLayout.mKeyguardBypassEnabled) {
                        i = firstChildWithBackground.getHeadsUpHeightWithoutHeader();
                    } else {
                        i = firstChildWithBackground.getCollapsedHeight();
                    }
                    f = i;
                } else {
                    f = 0.0f;
                }
                notificationStackScrollLayout.setPulseHeight(f);
                float f2 = -1.0f;
                int childCount = notificationStackScrollLayout.getChildCount();
                boolean z3 = true;
                for (int i2 = 0; i2 < childCount; i2++) {
                    ExpandableView expandableView = (ExpandableView) notificationStackScrollLayout.getChildAt(i2);
                    if (expandableView.getVisibility() != 8) {
                        if (expandableView == notificationStackScrollLayout.mShelf) {
                            z2 = true;
                        } else {
                            z2 = false;
                        }
                        if ((expandableView instanceof ExpandableNotificationRow) || z2) {
                            if (expandableView.getVisibility() != 0 || z2) {
                                if (!z3) {
                                    expandableView.setTranslationY(f2);
                                }
                            } else if (z3) {
                                float translationY = expandableView.getTranslationY() + expandableView.mActualHeight;
                                Objects.requireNonNull(notificationStackScrollLayout.mShelf);
                                f2 = translationY - notificationShelf.getHeight();
                                z3 = false;
                            }
                        }
                    }
                }
                notificationStackScrollLayout.mDimmedNeedsAnimation = true;
            }
            if (this.bypassController.getBypassEnabled() && !this.mNotificationsVisible) {
                updateNotificationVisibility(shouldAnimateVisibility(), false);
            }
        }
    }

    public final boolean shouldAnimateVisibility() {
        if (!this.dozeParameters.getAlwaysOn() || this.dozeParameters.getDisplayNeedsBlanking()) {
            return false;
        }
        return true;
    }

    public final void updateHideAmount() {
        boolean z;
        int i;
        float min = Math.min(1.0f - this.mLinearVisibilityAmount, this.mLinearDozeAmount);
        float min2 = Math.min(1.0f - this.mVisibilityAmount, this.mDozeAmount);
        NotificationStackScrollLayoutController notificationStackScrollLayoutController = this.mStackScrollerController;
        if (notificationStackScrollLayoutController == null) {
            notificationStackScrollLayoutController = null;
        }
        Objects.requireNonNull(notificationStackScrollLayoutController);
        NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
        Objects.requireNonNull(notificationStackScrollLayout);
        notificationStackScrollLayout.mLinearHideAmount = min;
        notificationStackScrollLayout.mInterpolatedHideAmount = min2;
        boolean isFullyHidden = notificationStackScrollLayout.mAmbientState.isFullyHidden();
        boolean isHiddenAtAll = notificationStackScrollLayout.mAmbientState.isHiddenAtAll();
        AmbientState ambientState = notificationStackScrollLayout.mAmbientState;
        Objects.requireNonNull(ambientState);
        if (!(min2 != 1.0f || ambientState.mHideAmount == min2 || 100000.0f == ambientState.mPulseHeight)) {
            ambientState.mPulseHeight = 100000.0f;
            Runnable runnable = ambientState.mOnPulseHeightChangedListener;
            if (runnable != null) {
                runnable.run();
            }
        }
        ambientState.mHideAmount = min2;
        boolean isFullyHidden2 = notificationStackScrollLayout.mAmbientState.isFullyHidden();
        boolean isHiddenAtAll2 = notificationStackScrollLayout.mAmbientState.isHiddenAtAll();
        boolean z2 = false;
        if (isFullyHidden2 != isFullyHidden) {
            if (!notificationStackScrollLayout.mAmbientState.isFullyHidden() || !notificationStackScrollLayout.onKeyguard()) {
                z = true;
            } else {
                z = false;
            }
            if (z) {
                i = 0;
            } else {
                i = 4;
            }
            notificationStackScrollLayout.setVisibility(i);
        }
        if (!isHiddenAtAll && isHiddenAtAll2) {
            notificationStackScrollLayout.mSwipeHelper.resetExposedMenuView(true, true);
        }
        if (!(isFullyHidden2 == isFullyHidden && isHiddenAtAll == isHiddenAtAll2)) {
            notificationStackScrollLayout.invalidateOutline();
        }
        notificationStackScrollLayout.updateAlgorithmHeightAndPadding();
        notificationStackScrollLayout.updateBackgroundDimming();
        notificationStackScrollLayout.requestChildrenUpdate();
        notificationStackScrollLayout.updateOwnTranslationZ();
        if (min == 1.0f) {
            z2 = true;
        }
        if (this.notificationsFullyHidden != z2) {
            this.notificationsFullyHidden = z2;
            Iterator<WakeUpListener> it = this.wakeUpListeners.iterator();
            while (it.hasNext()) {
                it.next().onFullyHiddenChanged(z2);
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:22:0x0039  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x003c A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void updateNotificationVisibility(boolean r6, boolean r7) {
        /*
            r5 = this;
            boolean r0 = r5.mNotificationsVisibleForExpansion
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L_0x0017
            com.android.systemui.statusbar.policy.HeadsUpManager r0 = r5.mHeadsUpManager
            java.util.Objects.requireNonNull(r0)
            android.util.ArrayMap<java.lang.String, com.android.systemui.statusbar.AlertingNotificationManager$AlertEntry> r0 = r0.mAlertEntries
            boolean r0 = r0.isEmpty()
            r0 = r0 ^ r1
            if (r0 == 0) goto L_0x0015
            goto L_0x0017
        L_0x0015:
            r0 = r2
            goto L_0x0018
        L_0x0017:
            r0 = r1
        L_0x0018:
            if (r0 == 0) goto L_0x0022
            boolean r0 = r5.getCanShowPulsingHuns()
            if (r0 == 0) goto L_0x0022
            r0 = r1
            goto L_0x0023
        L_0x0022:
            r0 = r2
        L_0x0023:
            if (r0 != 0) goto L_0x003d
            boolean r3 = r5.mNotificationsVisible
            if (r3 == 0) goto L_0x003d
            boolean r3 = r5.wakingUp
            if (r3 != 0) goto L_0x0031
            boolean r3 = r5.willWakeUp
            if (r3 == 0) goto L_0x003d
        L_0x0031:
            float r3 = r5.mDozeAmount
            r4 = 0
            int r3 = (r3 > r4 ? 1 : (r3 == r4 ? 0 : -1))
            if (r3 != 0) goto L_0x0039
            goto L_0x003a
        L_0x0039:
            r1 = r2
        L_0x003a:
            if (r1 != 0) goto L_0x003d
            return
        L_0x003d:
            r5.setNotificationsVisible(r0, r6, r7)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.updateNotificationVisibility(boolean, boolean):void");
    }

    /* JADX WARN: Type inference failed for: r3v1, types: [com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator$mNotificationVisibility$1] */
    public NotificationWakeUpCoordinator(HeadsUpManager headsUpManager, StatusBarStateController statusBarStateController, KeyguardBypassController keyguardBypassController, DozeParameters dozeParameters, ScreenOffAnimationController screenOffAnimationController) {
        this.mHeadsUpManager = headsUpManager;
        this.statusBarStateController = statusBarStateController;
        this.bypassController = keyguardBypassController;
        this.dozeParameters = dozeParameters;
        this.screenOffAnimationController = screenOffAnimationController;
        ArrayList<WakeUpListener> arrayList = new ArrayList<>();
        this.wakeUpListeners = arrayList;
        headsUpManager.addListener(this);
        statusBarStateController.addCallback(this);
        arrayList.add(new WakeUpListener() { // from class: com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.1
            @Override // com.android.systemui.statusbar.notification.NotificationWakeUpCoordinator.WakeUpListener
            public final void onFullyHiddenChanged(boolean z) {
                if (z) {
                    NotificationWakeUpCoordinator notificationWakeUpCoordinator = NotificationWakeUpCoordinator.this;
                    if (notificationWakeUpCoordinator.mNotificationsVisibleForExpansion) {
                        notificationWakeUpCoordinator.setNotificationsVisibleForExpansion(false, false, false);
                    }
                }
            }
        });
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public final void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        boolean z2;
        boolean z3;
        boolean shouldAnimateVisibility = shouldAnimateVisibility();
        if (!z) {
            if (this.mLinearDozeAmount == 0.0f) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (!z2) {
                if (this.mLinearVisibilityAmount == 0.0f) {
                    z3 = true;
                } else {
                    z3 = false;
                }
                if (!z3) {
                    if (notificationEntry.isRowDismissed()) {
                        shouldAnimateVisibility = false;
                    } else if (!this.wakingUp && !this.willWakeUp) {
                        ExpandableNotificationRow expandableNotificationRow = notificationEntry.row;
                        if (expandableNotificationRow != null) {
                            expandableNotificationRow.setHeadsUpAnimatingAway(true);
                        }
                        this.mEntrySetToClearWhenFinished.add(notificationEntry);
                    }
                }
            }
        } else if (this.mEntrySetToClearWhenFinished.contains(notificationEntry)) {
            this.mEntrySetToClearWhenFinished.remove(notificationEntry);
            ExpandableNotificationRow expandableNotificationRow2 = notificationEntry.row;
            if (expandableNotificationRow2 != null) {
                expandableNotificationRow2.setHeadsUpAnimatingAway(false);
            }
        }
        updateNotificationVisibility(shouldAnimateVisibility, false);
    }

    @Override // com.android.systemui.statusbar.phone.panelstate.PanelExpansionListener
    public final void onPanelExpansionChanged(float f, boolean z, boolean z2) {
        boolean z3;
        if (f <= 0.9f) {
            z3 = true;
        } else {
            z3 = false;
        }
        if (z3 != this.collapsedEnoughToHide) {
            boolean canShowPulsingHuns = getCanShowPulsingHuns();
            this.collapsedEnoughToHide = z3;
            if (canShowPulsingHuns && !getCanShowPulsingHuns()) {
                updateNotificationVisibility(true, true);
                this.mHeadsUpManager.releaseAllImmediately();
            }
        }
    }
}
