package com.android.systemui.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import kotlin.Triple;
/* compiled from: NeverExactlyLinearLayout.kt */
/* loaded from: classes.dex */
public final class NeverExactlyLinearLayout extends LinearLayout {
    public NeverExactlyLinearLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet, 0);
    }

    public static Triple getNonExactlyMeasureSpec(int i) {
        boolean z;
        if (View.MeasureSpec.getMode(i) == 1073741824) {
            z = true;
        } else {
            z = false;
        }
        int size = View.MeasureSpec.getSize(i);
        if (z) {
            i = View.MeasureSpec.makeMeasureSpec(size, Integer.MIN_VALUE);
        }
        return new Triple(Boolean.valueOf(z), Integer.valueOf(i), Integer.valueOf(size));
    }

    @Override // android.widget.LinearLayout, android.view.View
    public final void onMeasure(int i, int i2) {
        Triple nonExactlyMeasureSpec = getNonExactlyMeasureSpec(i);
        boolean booleanValue = ((Boolean) nonExactlyMeasureSpec.component1()).booleanValue();
        int intValue = ((Number) nonExactlyMeasureSpec.component2()).intValue();
        int intValue2 = ((Number) nonExactlyMeasureSpec.component3()).intValue();
        Triple nonExactlyMeasureSpec2 = getNonExactlyMeasureSpec(i2);
        boolean booleanValue2 = ((Boolean) nonExactlyMeasureSpec2.component1()).booleanValue();
        int intValue3 = ((Number) nonExactlyMeasureSpec2.component2()).intValue();
        int intValue4 = ((Number) nonExactlyMeasureSpec2.component3()).intValue();
        super.onMeasure(intValue, intValue3);
        if (booleanValue || booleanValue2) {
            if (!booleanValue) {
                intValue2 = getMeasuredWidth();
            }
            if (!booleanValue2) {
                intValue4 = getMeasuredHeight();
            }
            setMeasuredDimension(intValue2, intValue4);
        }
    }
}
