package com.android.systemui.statusbar.notification.stack;

import android.animation.ObjectAnimator;
import android.graphics.Rect;
import android.view.View;
import com.android.systemui.statusbar.notification.row.ExpandableView;
/* loaded from: classes.dex */
public final class NotificationSection {
    public int mBucket;
    public ExpandableView mFirstVisibleChild;
    public ExpandableView mLastVisibleChild;
    public View mOwningView;
    public Rect mBounds = new Rect();
    public Rect mCurrentBounds = new Rect(-1, -1, -1, -1);
    public Rect mStartAnimationRect = new Rect();
    public Rect mEndAnimationRect = new Rect();
    public ObjectAnimator mTopAnimator = null;
    public ObjectAnimator mBottomAnimator = null;

    public final int updateBounds(int i, int i2, boolean z) {
        int i3;
        int i4;
        boolean z2;
        int i5;
        Rect rect;
        Rect rect2;
        ExpandableView expandableView = this.mFirstVisibleChild;
        boolean z3 = true;
        if (expandableView != null) {
            int ceil = (int) Math.ceil(ViewState.getFinalTranslationY(expandableView));
            ObjectAnimator objectAnimator = this.mTopAnimator;
            if (!(objectAnimator == null && this.mCurrentBounds.top == ceil) && (objectAnimator == null || this.mEndAnimationRect.top != ceil)) {
                z2 = false;
            } else {
                z2 = true;
            }
            if (z2) {
                i5 = ceil;
            } else {
                i5 = (int) Math.ceil(expandableView.getTranslationY());
            }
            i3 = Math.max(i5, i);
            if (expandableView.showingPulsing()) {
                i4 = Math.max(i, ExpandableViewState.getFinalActualHeight(expandableView) + ceil);
                if (z) {
                    this.mBounds.left = (int) (Math.max(expandableView.getTranslation(), 0.0f) + rect.left);
                    this.mBounds.right = (int) (Math.min(expandableView.getTranslation(), 0.0f) + rect2.right);
                }
            } else {
                i4 = i;
            }
        } else {
            i4 = i;
            i3 = i4;
        }
        int max = Math.max(i, i3);
        ExpandableView expandableView2 = this.mLastVisibleChild;
        if (expandableView2 != null) {
            int floor = (int) Math.floor((ViewState.getFinalTranslationY(expandableView2) + ExpandableViewState.getFinalActualHeight(expandableView2)) - expandableView2.mClipBottomAmount);
            ObjectAnimator objectAnimator2 = this.mBottomAnimator;
            if (!(objectAnimator2 == null && this.mCurrentBounds.bottom == floor) && (objectAnimator2 == null || this.mEndAnimationRect.bottom != floor)) {
                z3 = false;
            }
            if (!z3) {
                floor = (int) ((expandableView2.getTranslationY() + expandableView2.mActualHeight) - expandableView2.mClipBottomAmount);
                i2 = (int) Math.min(expandableView2.getTranslationY() + expandableView2.mActualHeight, i2);
            }
            i4 = Math.max(i4, Math.max(floor, i2));
        }
        int max2 = Math.max(max, i4);
        Rect rect3 = this.mBounds;
        rect3.top = max;
        rect3.bottom = max2;
        return max2;
    }

    public NotificationSection(NotificationStackScrollLayout notificationStackScrollLayout, int i) {
        this.mOwningView = notificationStackScrollLayout;
        this.mBucket = i;
    }
}
