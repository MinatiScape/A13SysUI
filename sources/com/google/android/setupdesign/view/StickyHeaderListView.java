package com.google.android.setupdesign.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ListView;
import com.google.android.setupdesign.R$styleable;
/* loaded from: classes.dex */
public class StickyHeaderListView extends ListView {
    public View sticky;
    public View stickyContainer;
    public int statusBarInset = 0;
    public final RectF stickyRect = new RectF();

    public StickyHeaderListView(Context context) {
        super(context);
        init(null, 16842868);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
        if (!this.stickyRect.contains(motionEvent.getX(), motionEvent.getY())) {
            return super.dispatchTouchEvent(motionEvent);
        }
        RectF rectF = this.stickyRect;
        motionEvent.offsetLocation(-rectF.left, -rectF.top);
        return this.stickyContainer.dispatchTouchEvent(motionEvent);
    }

    @Override // android.widget.AbsListView, android.view.View
    public final void draw(Canvas canvas) {
        View view;
        int i;
        super.draw(canvas);
        if (this.sticky != null) {
            int save = canvas.save();
            View view2 = this.stickyContainer;
            if (view2 != null) {
                view = view2;
            } else {
                view = this.sticky;
            }
            if (view2 != null) {
                i = this.sticky.getTop();
            } else {
                i = 0;
            }
            if (view.getTop() + i < this.statusBarInset || !view.isShown()) {
                this.stickyRect.set(0.0f, (-i) + this.statusBarInset, view.getWidth(), (view.getHeight() - i) + this.statusBarInset);
                canvas.translate(0.0f, this.stickyRect.top);
                canvas.clipRect(0, 0, view.getWidth(), view.getHeight());
                view.draw(canvas);
            } else {
                this.stickyRect.setEmpty();
            }
            canvas.restoreToCount(save);
        }
    }

    public final void init(AttributeSet attributeSet, int i) {
        if (!isInEditMode()) {
            TypedArray obtainStyledAttributes = getContext().obtainStyledAttributes(attributeSet, R$styleable.SudStickyHeaderListView, i, 0);
            int resourceId = obtainStyledAttributes.getResourceId(0, 0);
            if (resourceId != 0) {
                addHeaderView(LayoutInflater.from(getContext()).inflate(resourceId, (ViewGroup) this, false), null, false);
            }
            obtainStyledAttributes.recycle();
        }
    }

    @Override // android.view.View
    @TargetApi(21)
    public final WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        if (getFitsSystemWindows()) {
            this.statusBarInset = windowInsets.getSystemWindowInsetTop();
            windowInsets.replaceSystemWindowInsets(windowInsets.getSystemWindowInsetLeft(), 0, windowInsets.getSystemWindowInsetRight(), windowInsets.getSystemWindowInsetBottom());
        }
        return windowInsets;
    }

    @Override // android.view.View
    public final void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        int i;
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (this.sticky != null) {
            i = 1;
        } else {
            i = 0;
        }
        accessibilityEvent.setItemCount(accessibilityEvent.getItemCount() - i);
        accessibilityEvent.setFromIndex(Math.max(accessibilityEvent.getFromIndex() - i, 0));
        accessibilityEvent.setToIndex(Math.max(accessibilityEvent.getToIndex() - i, 0));
    }

    @Override // android.widget.AbsListView, android.widget.AdapterView, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.sticky == null) {
            this.sticky = findViewWithTag("sticky");
            this.stickyContainer = findViewWithTag("stickyContainer");
        }
    }

    public StickyHeaderListView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(attributeSet, 16842868);
    }

    public StickyHeaderListView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init(attributeSet, i);
    }
}
