package com.android.systemui.qs;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.widget.ScrollView;
/* loaded from: classes.dex */
public class NonInterceptingScrollView extends ScrollView {
    public float mDownY;
    public boolean mScrollEnabled = true;
    public final int mTouchSlop;

    @Override // android.view.View
    public final boolean canScrollHorizontally(int i) {
        if (!this.mScrollEnabled || !super.canScrollHorizontally(i)) {
            return false;
        }
        return true;
    }

    @Override // android.view.View
    public final boolean canScrollVertically(int i) {
        if (!this.mScrollEnabled || !super.canScrollVertically(i)) {
            return false;
        }
        return true;
    }

    public NonInterceptingScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }

    public final int getScrollRange() {
        if (getChildCount() > 0) {
            return Math.max(0, getChildAt(0).getHeight() - ((getHeight() - ((ScrollView) this).mPaddingBottom) - ((ScrollView) this).mPaddingTop));
        }
        return 0;
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        ViewParent parent;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            if (canScrollVertically(1) && (parent = getParent()) != null) {
                parent.requestDisallowInterceptTouchEvent(true);
            }
            this.mDownY = motionEvent.getY();
        } else if (actionMasked == 2 && ((int) motionEvent.getY()) - this.mDownY < (-this.mTouchSlop) && !canScrollVertically(1)) {
            return false;
        }
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.ScrollView, android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getActionMasked() == 0) {
            if (canScrollVertically(1)) {
                ViewParent parent = getParent();
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true);
                }
            } else if (!canScrollVertically(-1)) {
                return false;
            }
        }
        return super.onTouchEvent(motionEvent);
    }
}
