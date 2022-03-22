package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import android.util.MathUtils;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.stack.StackScrollAlgorithm;
/* loaded from: classes.dex */
public final class AmbientState {
    public ActivatableNotificationView mActivatedChild;
    public float mAppearFraction;
    public final StackScrollAlgorithm.BypassController mBypassController;
    public boolean mClearAllInProgress;
    public int mContentHeight;
    public float mCurrentScrollVelocity;
    public boolean mDimmed;
    public boolean mDozing;
    public float mExpandingVelocity;
    public boolean mExpansionChanging;
    public float mExpansionFraction;
    public float mHideAmount;
    public boolean mHideSensitive;
    public boolean mIsShadeOpening;
    public boolean mIsSwipingUp;
    public ExpandableView mLastVisibleBackgroundChild;
    public int mLayoutHeight;
    public int mLayoutMaxHeight;
    public int mLayoutMinHeight;
    public float mMaxHeadsUpTranslation;
    public Runnable mOnPulseHeightChangedListener;
    public float mOverExpansion;
    public float mOverScrollBottomAmount;
    public float mOverScrollTopAmount;
    public boolean mPanelTracking;
    public boolean mPulsing;
    public int mScrollY;
    public final StackScrollAlgorithm.SectionProvider mSectionProvider;
    public boolean mShadeExpanded;
    public NotificationShelf mShelf;
    public float mStackEndHeight;
    public int mStackTopMargin;
    public float mStackTranslation;
    public int mStatusBarState;
    public int mTopPadding;
    public ExpandableNotificationRow mTrackedHeadsUpRow;
    public boolean mUnlockHintRunning;
    public int mZDistanceBetweenElements;
    public float mPulseHeight = 100000.0f;
    public float mDozeAmount = 0.0f;
    public float mStackY = 0.0f;
    public float mStackHeight = 0.0f;

    public final int getInnerHeight(boolean z) {
        if (this.mDozeAmount == 1.0f && !isPulseExpanding()) {
            return this.mShelf.getHeight();
        }
        int max = Math.max(this.mLayoutMinHeight, Math.min(this.mLayoutHeight, this.mContentHeight) - this.mTopPadding);
        if (z) {
            return max;
        }
        float f = max;
        return (int) MathUtils.lerp(f, Math.min(this.mPulseHeight, f), this.mDozeAmount);
    }

    public final ExpandableNotificationRow getTrackedHeadsUpRow() {
        ExpandableNotificationRow expandableNotificationRow = this.mTrackedHeadsUpRow;
        if (expandableNotificationRow == null || !expandableNotificationRow.isAboveShelf()) {
            return null;
        }
        return this.mTrackedHeadsUpRow;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0021  */
    /* JADX WARN: Removed duplicated region for block: B:17:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean isDozingAndNotPulsing(com.android.systemui.statusbar.notification.row.ExpandableView r4) {
        /*
            r3 = this;
            boolean r0 = r4 instanceof com.android.systemui.statusbar.notification.row.ExpandableNotificationRow
            r1 = 0
            if (r0 == 0) goto L_0x0022
            com.android.systemui.statusbar.notification.row.ExpandableNotificationRow r4 = (com.android.systemui.statusbar.notification.row.ExpandableNotificationRow) r4
            boolean r0 = r3.mDozing
            r2 = 1
            if (r0 == 0) goto L_0x0022
            java.util.Objects.requireNonNull(r4)
            com.android.systemui.statusbar.notification.collection.NotificationEntry r4 = r4.mEntry
            boolean r3 = r3.mPulsing
            if (r3 == 0) goto L_0x001e
            java.util.Objects.requireNonNull(r4)
            boolean r3 = r4.mIsAlerting
            if (r3 == 0) goto L_0x001e
            r3 = r2
            goto L_0x001f
        L_0x001e:
            r3 = r1
        L_0x001f:
            if (r3 != 0) goto L_0x0022
            r1 = r2
        L_0x0022:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.stack.AmbientState.isDozingAndNotPulsing(com.android.systemui.statusbar.notification.row.ExpandableView):boolean");
    }

    public final boolean isFullyHidden() {
        if (this.mHideAmount == 1.0f) {
            return true;
        }
        return false;
    }

    public final boolean isHiddenAtAll() {
        if (this.mHideAmount != 0.0f) {
            return true;
        }
        return false;
    }

    public final boolean isOnKeyguard() {
        if (this.mStatusBarState == 1) {
            return true;
        }
        return false;
    }

    public final boolean isPulseExpanding() {
        if (this.mPulseHeight == 100000.0f || this.mDozeAmount == 0.0f || this.mHideAmount == 1.0f) {
            return false;
        }
        return true;
    }

    public AmbientState(Context context, StackScrollAlgorithm.SectionProvider sectionProvider, StackScrollAlgorithm.BypassController bypassController) {
        this.mSectionProvider = sectionProvider;
        this.mBypassController = bypassController;
        this.mZDistanceBetweenElements = Math.max(1, context.getResources().getDimensionPixelSize(2131167339));
    }
}
