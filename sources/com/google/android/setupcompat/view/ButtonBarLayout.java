package com.google.android.setupcompat.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import com.google.android.setupcompat.template.FooterActionButton;
import java.util.Objects;
/* loaded from: classes.dex */
public class ButtonBarLayout extends LinearLayout {
    public int originalPaddingLeft;
    public int originalPaddingRight;
    public boolean stacked = false;

    public ButtonBarLayout(Context context) {
        super(context);
    }

    public final void setStacked(boolean z) {
        if (this.stacked != z) {
            this.stacked = z;
            int childCount = getChildCount();
            for (int i = 0; i < childCount; i++) {
                View childAt = getChildAt(i);
                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) childAt.getLayoutParams();
                if (z) {
                    childAt.setTag(2131428949, Float.valueOf(layoutParams.weight));
                    layoutParams.weight = 0.0f;
                    layoutParams.leftMargin = 0;
                } else {
                    Float f = (Float) childAt.getTag(2131428949);
                    if (f != null) {
                        layoutParams.weight = f.floatValue();
                    }
                }
                childAt.setLayoutParams(layoutParams);
            }
            setOrientation(z ? 1 : 0);
            for (int i2 = childCount - 1; i2 >= 0; i2--) {
                bringChildToFront(getChildAt(i2));
            }
            if (z) {
                setHorizontalGravity(17);
                this.originalPaddingLeft = getPaddingLeft();
                int paddingRight = getPaddingRight();
                this.originalPaddingRight = paddingRight;
                int max = Math.max(this.originalPaddingLeft, paddingRight);
                setPadding(max, getPaddingTop(), max, getPaddingBottom());
                return;
            }
            setPadding(this.originalPaddingLeft, getPaddingTop(), this.originalPaddingRight, getPaddingBottom());
        }
    }

    public ButtonBarLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        boolean z;
        int i3;
        int size = View.MeasureSpec.getSize(i);
        boolean z2 = false;
        setStacked(false);
        boolean z3 = true;
        if (View.MeasureSpec.getMode(i) == 1073741824) {
            i3 = View.MeasureSpec.makeMeasureSpec(0, 0);
            z = true;
        } else {
            i3 = i;
            z = false;
        }
        super.onMeasure(i3, i2);
        Context context = getContext();
        int childCount = getChildCount();
        int i4 = 0;
        for (int i5 = 0; i5 < childCount; i5++) {
            View childAt = getChildAt(i5);
            if (childAt instanceof FooterActionButton) {
                FooterActionButton footerActionButton = (FooterActionButton) childAt;
                Objects.requireNonNull(footerActionButton);
                if (footerActionButton.isPrimaryButtonStyle) {
                    i4++;
                }
            }
        }
        if (i4 == 2 && context.getResources().getConfiguration().smallestScreenWidthDp >= 600 && PartnerConfigHelper.shouldApplyExtendedPartnerConfig(context)) {
            z2 = true;
        }
        if (z2 || getMeasuredWidth() <= size) {
            z3 = z;
        } else {
            setStacked(true);
        }
        if (z3) {
            super.onMeasure(i, i2);
        }
    }
}
