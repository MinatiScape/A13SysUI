package com.android.systemui.qs;

import android.animation.AnimatorSet;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;
import androidx.viewpager.widget.ViewPager;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.qs.QSPanel;
import com.android.systemui.qs.QSPanelControllerBase;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public class PagedTileLayout extends ViewPager implements QSPanel.QSTileLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public final AnonymousClass3 mAdapter;
    public AnimatorSet mBounceAnimatorSet;
    public int mExcessHeight;
    public int mLastExcessHeight;
    public float mLastExpansion;
    public boolean mListening;
    public final AnonymousClass2 mOnPageChangeListener;
    public PageIndicator mPageIndicator;
    public float mPageIndicatorPosition;
    public PageListener mPageListener;
    public Scroller mScroller;
    public final ArrayList<QSPanelControllerBase.TileRecord> mTiles = new ArrayList<>();
    public final ArrayList<TileLayout> mPages = new ArrayList<>();
    public boolean mDistributeTiles = false;
    public int mPageToRestore = -1;
    public final UiEventLoggerImpl mUiEventLogger = QSEvents.qsUiEventsLogger;
    public int mMinRows = 1;
    public int mMaxColumns = 100;
    public int mLastMaxHeight = -1;
    public int mLayoutOrientation = getResources().getConfiguration().orientation;
    public int mLayoutDirection = getLayoutDirection();

    /* loaded from: classes.dex */
    public interface PageListener {
    }

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final boolean updateResources() {
        boolean z = false;
        for (int i = 0; i < this.mPages.size(); i++) {
            z |= this.mPages.get(i).updateResources();
        }
        if (z) {
            this.mDistributeTiles = true;
            requestLayout();
        }
        return z;
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final void addTile(QSPanelControllerBase.TileRecord tileRecord) {
        this.mTiles.add(tileRecord);
        this.mDistributeTiles = true;
        requestLayout();
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public final void computeScroll() {
        if (!this.mScroller.isFinished() && this.mScroller.computeScrollOffset()) {
            if (!this.mFakeDragging) {
                beginFakeDrag();
            }
            try {
                super.fakeDragBy(getScrollX() - this.mScroller.getCurrX());
                postInvalidateOnAnimation();
            } catch (NullPointerException e) {
                Log.e("PagedTileLayout", "FakeDragBy called before begin", e);
                final int size = this.mPages.size() - 1;
                post(new Runnable() { // from class: com.android.systemui.qs.PagedTileLayout$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        PagedTileLayout pagedTileLayout = PagedTileLayout.this;
                        int i = size;
                        int i2 = PagedTileLayout.$r8$clinit;
                        Objects.requireNonNull(pagedTileLayout);
                        pagedTileLayout.setCurrentItem(i, true);
                        AnimatorSet animatorSet = pagedTileLayout.mBounceAnimatorSet;
                        if (animatorSet != null) {
                            animatorSet.start();
                        }
                        pagedTileLayout.setOffscreenPageLimit(1);
                    }
                });
            }
        } else if (this.mFakeDragging) {
            endFakeDrag();
            AnimatorSet animatorSet = this.mBounceAnimatorSet;
            if (animatorSet != null) {
                animatorSet.start();
            }
            setOffscreenPageLimit(1);
        }
        super.computeScroll();
    }

    public final int getNumPages() {
        int size = this.mTiles.size();
        TileLayout tileLayout = this.mPages.get(0);
        Objects.requireNonNull(tileLayout);
        int max = Math.max(size / Math.max(tileLayout.mColumns * tileLayout.mRows, 1), 1);
        TileLayout tileLayout2 = this.mPages.get(0);
        Objects.requireNonNull(tileLayout2);
        if (size > Math.max(tileLayout2.mColumns * tileLayout2.mRows, 1) * max) {
            return max + 1;
        }
        return max;
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final int getNumVisibleTiles() {
        if (this.mPages.size() == 0) {
            return 0;
        }
        ArrayList<TileLayout> arrayList = this.mPages;
        int i = this.mCurItem;
        if (this.mLayoutDirection == 1) {
            i = (arrayList.size() - 1) - i;
        }
        return arrayList.get(i).mRecords.size();
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final int getTilesHeight() {
        TileLayout tileLayout = this.mPages.get(0);
        if (tileLayout == null) {
            return 0;
        }
        return tileLayout.getTilesHeight();
    }

    @Override // androidx.viewpager.widget.ViewPager, android.view.View
    public final void onMeasure(int i, int i2) {
        ArrayList<TileLayout> arrayList;
        int size = this.mTiles.size();
        if (!(!this.mDistributeTiles && this.mLastMaxHeight == View.MeasureSpec.getSize(i2) && this.mLastExcessHeight == this.mExcessHeight)) {
            int size2 = View.MeasureSpec.getSize(i2);
            this.mLastMaxHeight = size2;
            int i3 = this.mExcessHeight;
            this.mLastExcessHeight = i3;
            if (this.mPages.get(0).updateMaxRows(size2 - i3, size) || this.mDistributeTiles) {
                this.mDistributeTiles = false;
                int numPages = getNumPages();
                int size3 = this.mPages.size();
                for (int i4 = 0; i4 < size3; i4++) {
                    this.mPages.get(i4).removeAllViews();
                }
                if (size3 != numPages) {
                    while (this.mPages.size() < numPages) {
                        this.mPages.add(createTileLayout());
                    }
                    while (this.mPages.size() > numPages) {
                        this.mPages.remove(arrayList.size() - 1);
                    }
                    this.mPageIndicator.setNumPages(this.mPages.size());
                    setAdapter(this.mAdapter);
                    notifyDataSetChanged();
                    int i5 = this.mPageToRestore;
                    if (i5 != -1) {
                        setCurrentItem(i5, false);
                        this.mPageToRestore = -1;
                    }
                }
                TileLayout tileLayout = this.mPages.get(0);
                Objects.requireNonNull(tileLayout);
                int max = Math.max(tileLayout.mColumns * tileLayout.mRows, 1);
                int size4 = this.mTiles.size();
                int i6 = 0;
                for (int i7 = 0; i7 < size4; i7++) {
                    QSPanelControllerBase.TileRecord tileRecord = this.mTiles.get(i7);
                    if (this.mPages.get(i6).mRecords.size() == max) {
                        i6++;
                    }
                    this.mPages.get(i6).addTile(tileRecord);
                }
            }
            int i8 = this.mPages.get(0).mRows;
            for (int i9 = 0; i9 < this.mPages.size(); i9++) {
                this.mPages.get(i9).mRows = i8;
            }
        }
        super.onMeasure(i, i2);
        int childCount = getChildCount();
        int i10 = 0;
        for (int i11 = 0; i11 < childCount; i11++) {
            int measuredHeight = getChildAt(i11).getMeasuredHeight();
            if (measuredHeight > i10) {
                i10 = measuredHeight;
            }
        }
        setMeasuredDimension(getMeasuredWidth(), getPaddingBottom() + i10);
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final void removeTile(QSPanelControllerBase.TileRecord tileRecord) {
        if (this.mTiles.remove(tileRecord)) {
            this.mDistributeTiles = true;
            requestLayout();
        }
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final void restoreInstanceState(Bundle bundle) {
        this.mPageToRestore = bundle.getInt("current_page", -1);
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final void saveInstanceState(Bundle bundle) {
        bundle.putInt("current_page", this.mCurItem);
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final void setExpansion(float f, float f2) {
        this.mLastExpansion = f;
        updateSelected();
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final void setListening(boolean z, UiEventLogger uiEventLogger) {
        if (this.mListening != z) {
            this.mListening = z;
            updateListening();
        }
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final boolean setMaxColumns(int i) {
        this.mMaxColumns = i;
        boolean z = false;
        for (int i2 = 0; i2 < this.mPages.size(); i2++) {
            if (this.mPages.get(i2).setMaxColumns(i)) {
                this.mDistributeTiles = true;
                z = true;
            }
        }
        return z;
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final boolean setMinRows(int i) {
        this.mMinRows = i;
        boolean z = false;
        for (int i2 = 0; i2 < this.mPages.size(); i2++) {
            if (this.mPages.get(i2).setMinRows(i)) {
                this.mDistributeTiles = true;
                z = true;
            }
        }
        return z;
    }

    @Override // com.android.systemui.qs.QSPanel.QSTileLayout
    public final void setSquishinessFraction(float f) {
        int size = this.mPages.size();
        for (int i = 0; i < size; i++) {
            this.mPages.get(i).setSquishinessFraction(f);
        }
    }

    public final void updateListening() {
        boolean z;
        Iterator<TileLayout> it = this.mPages.iterator();
        while (it.hasNext()) {
            TileLayout next = it.next();
            if (next.getParent() == null || !this.mListening) {
                z = false;
            } else {
                z = true;
            }
            next.setListening(z, null);
        }
    }

    public final void updateSelected() {
        boolean z;
        boolean z2;
        float f = this.mLastExpansion;
        if (f <= 0.0f || f >= 1.0f) {
            if (f == 1.0f) {
                z = true;
            } else {
                z = false;
            }
            setImportantForAccessibility(4);
            int i = this.mCurItem;
            if (this.mLayoutDirection == 1) {
                i = (this.mPages.size() - 1) - i;
            }
            for (int i2 = 0; i2 < this.mPages.size(); i2++) {
                TileLayout tileLayout = this.mPages.get(i2);
                if (i2 == i) {
                    z2 = z;
                } else {
                    z2 = false;
                }
                tileLayout.setSelected(z2);
                if (tileLayout.isSelected()) {
                    for (int i3 = 0; i3 < tileLayout.mRecords.size(); i3++) {
                        QSTile qSTile = tileLayout.mRecords.get(i3).tile;
                        this.mUiEventLogger.logWithInstanceId(QSEvent.QS_TILE_VISIBLE, 0, qSTile.getMetricsSpec(), qSTile.getInstanceId());
                    }
                }
            }
            setImportantForAccessibility(0);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.systemui.qs.PagedTileLayout$2, androidx.viewpager.widget.ViewPager$OnPageChangeListener] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.qs.PagedTileLayout$3, androidx.viewpager.widget.PagerAdapter] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public PagedTileLayout(android.content.Context r5, android.util.AttributeSet r6) {
        /*
            r4 = this;
            r4.<init>(r5, r6)
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            r4.mTiles = r6
            java.util.ArrayList r6 = new java.util.ArrayList
            r6.<init>()
            r4.mPages = r6
            r6 = 0
            r4.mDistributeTiles = r6
            r0 = -1
            r4.mPageToRestore = r0
            com.android.internal.logging.UiEventLoggerImpl r1 = com.android.systemui.qs.QSEvents.qsUiEventsLogger
            r4.mUiEventLogger = r1
            r1 = 1
            r4.mMinRows = r1
            r1 = 100
            r4.mMaxColumns = r1
            r4.mLastMaxHeight = r0
            com.android.systemui.qs.PagedTileLayout$2 r0 = new com.android.systemui.qs.PagedTileLayout$2
            r0.<init>()
            r4.mOnPageChangeListener = r0
            com.android.systemui.qs.PagedTileLayout$3 r1 = new com.android.systemui.qs.PagedTileLayout$3
            r1.<init>()
            r4.mAdapter = r1
            android.widget.Scroller r2 = new android.widget.Scroller
            com.android.systemui.qs.PagedTileLayout$$ExternalSyntheticLambda0 r3 = com.android.systemui.qs.PagedTileLayout$$ExternalSyntheticLambda0.INSTANCE
            r2.<init>(r5, r3)
            r4.mScroller = r2
            r4.setAdapter(r1)
            r4.mOnPageChangeListener = r0
            r4.setCurrentItem(r6, r6)
            android.content.res.Resources r5 = r4.getResources()
            android.content.res.Configuration r5 = r5.getConfiguration()
            int r5 = r5.orientation
            r4.mLayoutOrientation = r5
            int r5 = r4.getLayoutDirection()
            r4.mLayoutDirection = r5
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.PagedTileLayout.<init>(android.content.Context, android.util.AttributeSet):void");
    }

    public final TileLayout createTileLayout() {
        TileLayout tileLayout = (TileLayout) LayoutInflater.from(getContext()).inflate(2131624428, (ViewGroup) this, false);
        tileLayout.setMinRows(this.mMinRows);
        tileLayout.setMaxColumns(this.mMaxColumns);
        return tileLayout;
    }

    @Override // androidx.viewpager.widget.ViewPager
    public final void endFakeDrag() {
        try {
            super.endFakeDrag();
        } catch (NullPointerException e) {
            Log.e("PagedTileLayout", "endFakeDrag called without velocityTracker", e);
        }
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int size = this.mPages.size();
        for (int i = 0; i < size; i++) {
            TileLayout tileLayout = this.mPages.get(i);
            if (tileLayout.getParent() == null) {
                tileLayout.dispatchConfigurationChanged(configuration);
            }
        }
        int i2 = this.mLayoutOrientation;
        int i3 = configuration.orientation;
        if (i2 != i3) {
            this.mLayoutOrientation = i3;
            this.mDistributeTiles = true;
            setCurrentItem(0, false);
            this.mPageToRestore = 0;
        }
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mPages.add(createTileLayout());
        notifyDataSetChanged();
    }

    @Override // android.view.View
    public final void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        if (this.mLayoutDirection != i) {
            this.mLayoutDirection = i;
            setAdapter(this.mAdapter);
            setCurrentItem(0, false);
            this.mPageToRestore = 0;
        }
    }

    @Override // androidx.viewpager.widget.ViewPager
    public final void setCurrentItem(int i, boolean z) {
        if (isLayoutRtl()) {
            i = (this.mPages.size() - 1) - i;
        }
        super.setCurrentItem(i, z);
    }
}
