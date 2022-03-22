package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Resources;
import android.provider.Settings;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSPanelControllerBase;
import com.android.systemui.qs.tileimpl.HeightOverrideable;
import com.android.systemui.util.Utils;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public class TileLayout extends ViewGroup implements QSPanel.QSTileLayout {
    public int mCellHeight;
    public int mCellHeightResId;
    public int mCellMarginHorizontal;
    public int mCellMarginVertical;
    public int mCellWidth;
    public int mColumns;
    public int mLastTileBottom;
    public final boolean mLessRows;
    public boolean mListening;
    public int mMaxAllowedRows;
    public int mMaxCellHeight;
    public int mMaxColumns;
    public int mMinRows;
    public final ArrayList<QSPanelControllerBase.TileRecord> mRecords;
    public int mResourceColumns;
    public int mRows;
    public int mSidePadding;
    public float mSquishinessFraction;

    public TileLayout(Context context) {
        this(context, null);
    }

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    public boolean useSidePadding() {
        return true;
    }

    public TileLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCellHeightResId = 2131166911;
        boolean z = true;
        this.mRows = 1;
        this.mRecords = new ArrayList<>();
        this.mMaxAllowedRows = 3;
        this.mMinRows = 1;
        this.mMaxColumns = 100;
        this.mSquishinessFraction = 1.0f;
        setFocusableInTouchMode(true);
        if (Settings.System.getInt(context.getContentResolver(), "qs_less_rows", 0) == 0 && !Utils.useQsMediaPlayer(context)) {
            z = false;
        }
        this.mLessRows = z;
        updateResources();
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final void addTile(QSPanelControllerBase.TileRecord tileRecord) {
        this.mRecords.add(tileRecord);
        tileRecord.tile.setListening(this, this.mListening);
        addView(tileRecord.tileView);
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final int getNumVisibleTiles() {
        return this.mRecords.size();
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final int getTilesHeight() {
        return getPaddingBottom() + this.mLastTileBottom;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        layoutTileRecords(this.mRecords.size(), true);
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        int size = this.mRecords.size();
        int size2 = View.MeasureSpec.getSize(i);
        int paddingStart = (size2 - getPaddingStart()) - getPaddingEnd();
        if (View.MeasureSpec.getMode(i2) == 0) {
            int i3 = this.mColumns;
            this.mRows = ((size + i3) - 1) / i3;
        }
        int i4 = this.mColumns;
        this.mCellWidth = ((paddingStart - (this.mCellMarginHorizontal * (i4 - 1))) - (this.mSidePadding * 2)) / i4;
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(this.mMaxCellHeight, 1073741824);
        Iterator<QSPanelControllerBase.TileRecord> it = this.mRecords.iterator();
        View view = this;
        while (it.hasNext()) {
            QSPanelControllerBase.TileRecord next = it.next();
            if (next.tileView.getVisibility() != 8) {
                next.tileView.measure(View.MeasureSpec.makeMeasureSpec(this.mCellWidth, 1073741824), makeMeasureSpec);
                view = next.tileView.updateAccessibilityOrder(view);
                this.mCellHeight = next.tileView.getMeasuredHeight();
            }
        }
        int i5 = this.mCellHeight;
        int i6 = this.mCellMarginVertical;
        int i7 = ((i5 + i6) * this.mRows) - i6;
        if (i7 < 0) {
            i7 = 0;
        }
        setMeasuredDimension(size2, i7);
    }

    @Override // android.view.ViewGroup
    public final void removeAllViews() {
        Iterator<QSPanelControllerBase.TileRecord> it = this.mRecords.iterator();
        while (it.hasNext()) {
            it.next().tile.setListening(this, false);
        }
        this.mRecords.clear();
        super.removeAllViews();
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final void removeTile(QSPanelControllerBase.TileRecord tileRecord) {
        this.mRecords.remove(tileRecord);
        tileRecord.tile.setListening(this, false);
        removeView(tileRecord.tileView);
    }

    public void setListening(boolean z, UiEventLogger uiEventLogger) {
        if (this.mListening != z) {
            this.mListening = z;
            Iterator<QSPanelControllerBase.TileRecord> it = this.mRecords.iterator();
            while (it.hasNext()) {
                it.next().tile.setListening(this, this.mListening);
            }
        }
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final boolean setMaxColumns(int i) {
        this.mMaxColumns = i;
        return updateColumns();
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final boolean setMinRows(int i) {
        if (this.mMinRows == i) {
            return false;
        }
        this.mMinRows = i;
        updateResources();
        return true;
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final void setSquishinessFraction(float f) {
        if (Float.compare(this.mSquishinessFraction, f) != 0) {
            this.mSquishinessFraction = f;
            layoutTileRecords(this.mRecords.size(), false);
            Iterator<QSPanelControllerBase.TileRecord> it = this.mRecords.iterator();
            while (it.hasNext()) {
                QSTileView qSTileView = it.next().tileView;
                if (qSTileView instanceof HeightOverrideable) {
                    ((HeightOverrideable) qSTileView).setSquishinessFraction(this.mSquishinessFraction);
                }
            }
        }
    }

    public final boolean updateColumns() {
        int i = this.mColumns;
        int min = Math.min(this.mResourceColumns, this.mMaxColumns);
        this.mColumns = min;
        if (i != min) {
            return true;
        }
        return false;
    }

    public boolean updateMaxRows(int i, int i2) {
        int i3 = this.mCellMarginVertical;
        int i4 = this.mRows;
        int i5 = (i + i3) / (this.mMaxCellHeight + i3);
        this.mRows = i5;
        int i6 = this.mMinRows;
        if (i5 < i6) {
            this.mRows = i6;
        } else {
            int i7 = this.mMaxAllowedRows;
            if (i5 >= i7) {
                this.mRows = i7;
            }
        }
        int i8 = this.mRows;
        int i9 = this.mColumns;
        int i10 = ((i2 + i9) - 1) / i9;
        if (i8 > i10) {
            this.mRows = i10;
        }
        if (i4 != this.mRows) {
            return true;
        }
        return false;
    }

    public boolean updateResources() {
        int i;
        Resources resources = ((ViewGroup) this).mContext.getResources();
        this.mResourceColumns = Math.max(1, resources.getInteger(2131493032));
        updateColumns();
        this.mMaxCellHeight = ((ViewGroup) this).mContext.getResources().getDimensionPixelSize(this.mCellHeightResId);
        this.mCellMarginHorizontal = resources.getDimensionPixelSize(2131166912);
        if (useSidePadding()) {
            i = this.mCellMarginHorizontal / 2;
        } else {
            i = 0;
        }
        this.mSidePadding = i;
        this.mCellMarginVertical = resources.getDimensionPixelSize(2131166914);
        int max = Math.max(1, getResources().getInteger(2131493030));
        this.mMaxAllowedRows = max;
        if (this.mLessRows) {
            this.mMaxAllowedRows = Math.max(this.mMinRows, max - 1);
        }
        if (!updateColumns()) {
            return false;
        }
        requestLayout();
        return true;
    }

    public final void layoutTileRecords(int i, boolean z) {
        boolean z2;
        int i2;
        if (getLayoutDirection() == 1) {
            z2 = true;
        } else {
            z2 = false;
        }
        this.mLastTileBottom = 0;
        int min = Math.min(i, this.mRows * this.mColumns);
        int i3 = 0;
        int i4 = 0;
        int i5 = 0;
        while (i3 < min) {
            if (i4 == this.mColumns) {
                i5++;
                i4 = 0;
            }
            QSPanelControllerBase.TileRecord tileRecord = this.mRecords.get(i3);
            int i6 = (int) (((this.mCellHeight * this.mSquishinessFraction) + this.mCellMarginVertical) * i5);
            if (z2) {
                i2 = (this.mColumns - i4) - 1;
            } else {
                i2 = i4;
            }
            int paddingStart = getPaddingStart() + this.mSidePadding;
            int i7 = this.mCellWidth;
            int i8 = ((this.mCellMarginHorizontal + i7) * i2) + paddingStart;
            int i9 = i7 + i8;
            int measuredHeight = tileRecord.tileView.getMeasuredHeight() + i6;
            if (z) {
                tileRecord.tileView.layout(i8, i6, i9, measuredHeight);
            } else {
                tileRecord.tileView.setLeftTopRightBottom(i8, i6, i9, measuredHeight);
            }
            this.mLastTileBottom = measuredHeight;
            i3++;
            i4++;
        }
    }
}
