package com.android.systemui.qs;

import android.content.Context;
import android.util.AttributeSet;
/* compiled from: SideLabelTileLayout.kt */
/* loaded from: classes.dex */
public class SideLabelTileLayout extends TileLayout {
    @Override // com.android.systemui.qs.TileLayout
    public final boolean useSidePadding() {
        return false;
    }

    @Override // com.android.systemui.qs.TileLayout
    public final boolean updateMaxRows(int i, int i2) {
        int i3 = this.mRows;
        int i4 = this.mMaxAllowedRows;
        this.mRows = i4;
        int i5 = this.mColumns;
        if (i4 > ((i2 + i5) - 1) / i5) {
            this.mRows = ((i2 + i5) - 1) / i5;
        }
        if (i3 != this.mRows) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.qs.TileLayout, com.android.systemui.qs.QSPanel.QSTileLayout
    public boolean updateResources() {
        boolean updateResources = super.updateResources();
        this.mMaxAllowedRows = getContext().getResources().getInteger(2131493030);
        return updateResources;
    }

    public SideLabelTileLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
