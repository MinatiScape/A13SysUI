package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ScrollView;
/* loaded from: classes.dex */
public class ObservableScrollView extends ScrollView {
    public boolean mTouchCancelled;
    public boolean mTouchEnabled = true;

    @Override // android.view.View
    public final boolean overScrollBy(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8, boolean z) {
        int i9;
        int i10 = i4 + i2;
        if (getChildCount() > 0) {
            i9 = Math.max(0, getChildAt(0).getHeight() - ((getHeight() - ((ScrollView) this).mPaddingBottom) - ((ScrollView) this).mPaddingTop));
        } else {
            i9 = 0;
        }
        Math.max(0, i10 - i9);
        return super.overScrollBy(i, i2, i3, i4, i5, i6, i7, i8, z);
    }

    public ObservableScrollView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() == 0) {
            if (!this.mTouchEnabled) {
                this.mTouchCancelled = true;
                return false;
            }
            this.mTouchCancelled = false;
        } else if (this.mTouchCancelled) {
            return false;
        } else {
            if (!this.mTouchEnabled) {
                MotionEvent obtain = MotionEvent.obtain(motionEvent);
                obtain.setAction(3);
                super.dispatchTouchEvent(obtain);
                obtain.recycle();
                this.mTouchCancelled = true;
                return false;
            }
        }
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.widget.ScrollView, android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        motionEvent.getX();
        motionEvent.getY();
        return super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.widget.ScrollView, android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        motionEvent.getX();
        motionEvent.getY();
        return super.onTouchEvent(motionEvent);
    }

    @Override // android.widget.ScrollView, android.view.View
    public final void onOverScrolled(int i, int i2, boolean z, boolean z2) {
        super.onOverScrolled(i, i2, z, z2);
    }

    @Override // android.view.View
    public final void onScrollChanged(int i, int i2, int i3, int i4) {
        super.onScrollChanged(i, i2, i3, i4);
    }

    @Override // android.widget.ScrollView
    public final void fling(int i) {
        super.fling(i);
    }
}
