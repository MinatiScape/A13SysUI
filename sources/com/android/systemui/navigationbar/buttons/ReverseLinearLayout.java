package com.android.systemui.navigationbar.buttons;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class ReverseLinearLayout extends LinearLayout {
    public boolean mIsAlternativeOrder;
    public boolean mIsLayoutReverse;

    /* loaded from: classes.dex */
    public interface Reversable {
        void reverse(boolean z);
    }

    /* loaded from: classes.dex */
    public static class ReverseRelativeLayout extends RelativeLayout implements Reversable {
        public int mDefaultGravity = 0;

        @Override // com.android.systemui.navigationbar.buttons.ReverseLinearLayout.Reversable
        public final void reverse(boolean z) {
            int i = this.mDefaultGravity;
            if (i == 48 || i == 80) {
                if (z) {
                    if (i == 48) {
                        i = 80;
                    } else {
                        i = 48;
                    }
                }
                if (getGravity() != i) {
                    setGravity(i);
                }
            }
            ReverseLinearLayout.reverseGroup(this, z);
        }

        public ReverseRelativeLayout(Context context) {
            super(context);
        }
    }

    public static void reverseGroup(ViewGroup viewGroup, boolean z) {
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View childAt = viewGroup.getChildAt(i);
            reverseParams(childAt.getLayoutParams(), childAt, z);
            if (childAt instanceof ViewGroup) {
                reverseGroup((ViewGroup) childAt, z);
            }
        }
    }

    @Override // android.view.ViewGroup
    public final void addView(View view) {
        reverseParams(view.getLayoutParams(), view, this.mIsLayoutReverse);
        if (this.mIsLayoutReverse) {
            super.addView(view, 0);
        } else {
            super.addView(view);
        }
    }

    public static void reverseParams(ViewGroup.LayoutParams layoutParams, View view, boolean z) {
        if (view instanceof Reversable) {
            ((Reversable) view).reverse(z);
        }
        if (view.getPaddingLeft() == view.getPaddingRight() && view.getPaddingTop() == view.getPaddingBottom()) {
            view.setPadding(view.getPaddingTop(), view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingLeft());
        }
        if (layoutParams != null) {
            int i = layoutParams.width;
            layoutParams.width = layoutParams.height;
            layoutParams.height = i;
        }
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        updateOrder();
    }

    @Override // android.widget.LinearLayout, android.view.View
    public final void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        updateOrder();
    }

    public final void updateOrder() {
        boolean z;
        if (getLayoutDirection() == 1) {
            z = true;
        } else {
            z = false;
        }
        boolean z2 = z ^ this.mIsAlternativeOrder;
        if (this.mIsLayoutReverse != z2) {
            int childCount = getChildCount();
            ArrayList arrayList = new ArrayList(childCount);
            for (int i = 0; i < childCount; i++) {
                arrayList.add(getChildAt(i));
            }
            removeAllViews();
            for (int i2 = childCount - 1; i2 >= 0; i2--) {
                super.addView((View) arrayList.get(i2));
            }
            this.mIsLayoutReverse = z2;
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public final void addView(View view, ViewGroup.LayoutParams layoutParams) {
        reverseParams(layoutParams, view, this.mIsLayoutReverse);
        if (this.mIsLayoutReverse) {
            super.addView(view, 0, layoutParams);
        } else {
            super.addView(view, layoutParams);
        }
    }

    public ReverseLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
