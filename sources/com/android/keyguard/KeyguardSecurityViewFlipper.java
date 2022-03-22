package com.android.keyguard;

import android.content.Context;
import android.content.res.TypedArray;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewDebug;
import android.view.ViewGroup;
import android.view.ViewHierarchyEncoder;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;
import com.android.systemui.R$styleable;
/* loaded from: classes.dex */
public class KeyguardSecurityViewFlipper extends ViewFlipper {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public Rect mTempRect;

    /* loaded from: classes.dex */
    public static class LayoutParams extends FrameLayout.LayoutParams {
        @ViewDebug.ExportedProperty(category = "layout")
        public int maxHeight;
        @ViewDebug.ExportedProperty(category = "layout")
        public int maxWidth;

        public LayoutParams(ViewGroup.LayoutParams layoutParams) {
            super(layoutParams);
        }

        public LayoutParams(LayoutParams layoutParams) {
            super((FrameLayout.LayoutParams) layoutParams);
            this.maxWidth = layoutParams.maxWidth;
            this.maxHeight = layoutParams.maxHeight;
        }

        public final void encodeProperties(ViewHierarchyEncoder viewHierarchyEncoder) {
            super.encodeProperties(viewHierarchyEncoder);
            viewHierarchyEncoder.addProperty("layout:maxWidth", this.maxWidth);
            viewHierarchyEncoder.addProperty("layout:maxHeight", this.maxHeight);
        }

        public LayoutParams(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.KeyguardSecurityViewFlipper_Layout, 0, 0);
            this.maxWidth = obtainStyledAttributes.getDimensionPixelSize(1, 0);
            this.maxHeight = obtainStyledAttributes.getDimensionPixelSize(0, 0);
            obtainStyledAttributes.recycle();
        }
    }

    public KeyguardSecurityViewFlipper(Context context) {
        this(context, null);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams ? new LayoutParams((LayoutParams) layoutParams) : new LayoutParams(layoutParams);
    }

    public KeyguardSecurityViewFlipper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTempRect = new Rect();
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public final ViewGroup.LayoutParams generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    /* renamed from: generateLayoutParams  reason: collision with other method in class */
    public final FrameLayout.LayoutParams mo19generateLayoutParams(AttributeSet attributeSet) {
        return new LayoutParams(getContext(), attributeSet);
    }

    public final KeyguardInputView getSecurityView() {
        View childAt = getChildAt(getDisplayedChild());
        if (childAt instanceof KeyguardInputView) {
            return (KeyguardInputView) childAt;
        }
        return null;
    }

    @Override // android.widget.FrameLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8;
        int mode = View.MeasureSpec.getMode(i);
        int mode2 = View.MeasureSpec.getMode(i2);
        boolean z = DEBUG;
        if (z && mode != Integer.MIN_VALUE) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onMeasure: widthSpec ");
            m.append(View.MeasureSpec.toString(i));
            m.append(" should be AT_MOST");
            Log.w("KeyguardSecurityViewFlipper", m.toString());
        }
        if (z && mode2 != Integer.MIN_VALUE) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("onMeasure: heightSpec ");
            m2.append(View.MeasureSpec.toString(i2));
            m2.append(" should be AT_MOST");
            Log.w("KeyguardSecurityViewFlipper", m2.toString());
        }
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        int childCount = getChildCount();
        int i9 = size;
        int i10 = size2;
        for (int i11 = 0; i11 < childCount; i11++) {
            View childAt = getChildAt(i11);
            if (childAt.getVisibility() == 0) {
                LayoutParams layoutParams = (LayoutParams) childAt.getLayoutParams();
                int i12 = layoutParams.maxWidth;
                if (i12 > 0 && i12 < i9) {
                    i9 = i12;
                }
                int i13 = layoutParams.maxHeight;
                if (i13 > 0 && i13 < i10) {
                    i10 = i13;
                }
            }
        }
        int paddingRight = getPaddingRight() + getPaddingLeft();
        int paddingBottom = getPaddingBottom() + getPaddingTop();
        int max = Math.max(0, i9 - paddingRight);
        int max2 = Math.max(0, i10 - paddingBottom);
        if (mode == 1073741824) {
            i3 = size;
        } else {
            i3 = 0;
        }
        if (mode2 == 1073741824) {
            i4 = size2;
        } else {
            i4 = 0;
        }
        for (int i14 = 0; i14 < childCount; i14++) {
            View childAt2 = getChildAt(i14);
            LayoutParams layoutParams2 = (LayoutParams) childAt2.getLayoutParams();
            int i15 = ((FrameLayout.LayoutParams) layoutParams2).width;
            if (i15 != -2) {
                if (i15 != -1) {
                    i6 = Math.min(max, i15);
                } else {
                    i6 = max;
                }
                i5 = 1073741824;
            } else {
                i5 = Integer.MIN_VALUE;
                i6 = max;
            }
            int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i6, i5);
            int i16 = ((FrameLayout.LayoutParams) layoutParams2).height;
            if (i16 != -2) {
                if (i16 != -1) {
                    i8 = Math.min(max2, i16);
                } else {
                    i8 = max2;
                }
                i7 = 1073741824;
            } else {
                i7 = Integer.MIN_VALUE;
                i8 = max2;
            }
            childAt2.measure(makeMeasureSpec, View.MeasureSpec.makeMeasureSpec(i8, i7));
            i3 = Math.max(i3, Math.min(childAt2.getMeasuredWidth(), size - paddingRight));
            i4 = Math.max(i4, Math.min(childAt2.getMeasuredHeight(), size2 - paddingBottom));
        }
        setMeasuredDimension(i3 + paddingRight, i4 + paddingBottom);
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        boolean onTouchEvent = super.onTouchEvent(motionEvent);
        this.mTempRect.set(0, 0, 0, 0);
        for (int i = 0; i < getChildCount(); i++) {
            View childAt = getChildAt(i);
            if (childAt.getVisibility() == 0) {
                offsetRectIntoDescendantCoords(childAt, this.mTempRect);
                Rect rect = this.mTempRect;
                motionEvent.offsetLocation(rect.left, rect.top);
                if (childAt.dispatchTouchEvent(motionEvent) || onTouchEvent) {
                    onTouchEvent = true;
                } else {
                    onTouchEvent = false;
                }
                Rect rect2 = this.mTempRect;
                motionEvent.offsetLocation(-rect2.left, -rect2.top);
            }
        }
        return onTouchEvent;
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup
    public final boolean checkLayoutParams(ViewGroup.LayoutParams layoutParams) {
        return layoutParams instanceof LayoutParams;
    }
}
