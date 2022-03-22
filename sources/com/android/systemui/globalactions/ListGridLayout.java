package com.android.systemui.globalactions;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import com.android.internal.annotations.VisibleForTesting;
/* loaded from: classes.dex */
public class ListGridLayout extends LinearLayout {
    public int mExpectedCount;
    public boolean mReverseItems;
    public boolean mReverseSublists;
    public boolean mSwapRowsAndColumns;
    public int mCurrentCount = 0;
    public final int[][] mConfigs = {new int[]{0, 0}, new int[]{1, 1}, new int[]{1, 2}, new int[]{1, 3}, new int[]{2, 2}, new int[]{2, 3}, new int[]{2, 3}, new int[]{3, 3}, new int[]{3, 3}, new int[]{3, 3}};

    public final int getRowCount() {
        int[] iArr;
        int i = this.mExpectedCount;
        if (i < 0) {
            iArr = this.mConfigs[0];
        } else {
            iArr = this.mConfigs[Math.min(this.mConfigs.length - 1, i)];
        }
        return iArr[0];
    }

    public ListGridLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @VisibleForTesting
    public ViewGroup getParentView(int i, boolean z, boolean z2) {
        int i2;
        if (getRowCount() == 0 || i < 0) {
            return null;
        }
        int min = Math.min(i, (this.mConfigs.length - 1) - 1);
        int rowCount = getRowCount();
        if (z2) {
            i2 = (int) Math.floor(min / rowCount);
        } else {
            i2 = min % rowCount;
        }
        if (z) {
            i2 = getChildCount() - (i2 + 1);
        }
        return getSublist(i2);
    }

    @VisibleForTesting
    public ViewGroup getSublist(int i) {
        return (ViewGroup) getChildAt(i);
    }
}
