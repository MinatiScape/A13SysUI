package com.android.wm.shell.legacysplitscreen;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.RotationUtils;
import android.util.TypedValue;
import android.window.WindowContainerTransaction;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import com.android.internal.policy.DividerSnapAlgorithm;
import com.android.internal.policy.DockedDividerUtils;
import com.android.wm.shell.common.DisplayLayout;
import java.util.Objects;
/* loaded from: classes.dex */
public final class LegacySplitDisplayLayout {
    public Context mContext;
    public DisplayLayout mDisplayLayout;
    public int mDividerSize;
    public int mDividerSizeInactive;
    public LegacySplitScreenTaskListener mTiles;
    public boolean mResourcesValid = false;
    public DividerSnapAlgorithm mSnapAlgorithm = null;
    public DividerSnapAlgorithm mMinimizedSnapAlgorithm = null;
    public Rect mPrimary = null;
    public Rect mSecondary = null;
    public Rect mAdjustedPrimary = null;
    public Rect mAdjustedSecondary = null;
    public final Rect mTmpBounds = new Rect();

    public final boolean resizeSplits(int i) {
        Rect rect = this.mPrimary;
        if (rect == null) {
            rect = new Rect();
        }
        this.mPrimary = rect;
        Rect rect2 = this.mSecondary;
        if (rect2 == null) {
            rect2 = new Rect();
        }
        this.mSecondary = rect2;
        int primarySplitSide = getPrimarySplitSide();
        this.mTmpBounds.set(this.mPrimary);
        Rect rect3 = this.mPrimary;
        DisplayLayout displayLayout = this.mDisplayLayout;
        Objects.requireNonNull(displayLayout);
        int i2 = displayLayout.mWidth;
        DisplayLayout displayLayout2 = this.mDisplayLayout;
        Objects.requireNonNull(displayLayout2);
        DockedDividerUtils.calculateBoundsForPosition(i, primarySplitSide, rect3, i2, displayLayout2.mHeight, this.mDividerSize);
        this.mTmpBounds.set(this.mSecondary);
        int invertDockSide = DockedDividerUtils.invertDockSide(primarySplitSide);
        Rect rect4 = this.mSecondary;
        DisplayLayout displayLayout3 = this.mDisplayLayout;
        Objects.requireNonNull(displayLayout3);
        int i3 = displayLayout3.mWidth;
        DisplayLayout displayLayout4 = this.mDisplayLayout;
        Objects.requireNonNull(displayLayout4);
        DockedDividerUtils.calculateBoundsForPosition(i, invertDockSide, rect4, i3, displayLayout4.mHeight, this.mDividerSize);
        return (!this.mSecondary.equals(this.mTmpBounds)) | (!this.mPrimary.equals(this.mTmpBounds));
    }

    public static int getSmallestWidthDpForBounds(Context context, DisplayLayout displayLayout, Rect rect) {
        int i;
        boolean z;
        char c;
        int i2;
        int dividerSize = DockedDividerUtils.getDividerSize(context.getResources(), DockedDividerUtils.getDividerInsets(context.getResources()));
        Rect rect2 = new Rect();
        Rect rect3 = new Rect();
        Objects.requireNonNull(displayLayout);
        Rect rect4 = new Rect(0, 0, displayLayout.mWidth, displayLayout.mHeight);
        DisplayLayout displayLayout2 = new DisplayLayout();
        int i3 = Integer.MAX_VALUE;
        for (int i4 = 0; i4 < 4; i4++) {
            displayLayout2.set(displayLayout);
            displayLayout2.rotateTo(context.getResources(), i4);
            Configuration configuration = new Configuration();
            configuration.unset();
            if (displayLayout2.mWidth > displayLayout2.mHeight) {
                i = 2;
            } else {
                i = 1;
            }
            configuration.orientation = i;
            Rect rect5 = new Rect(0, 0, displayLayout2.mWidth, displayLayout2.mHeight);
            rect5.inset(displayLayout2.mNonDecorInsets);
            configuration.windowConfiguration.setAppBounds(rect5);
            rect5.set(0, 0, displayLayout2.mWidth, displayLayout2.mHeight);
            rect5.inset(displayLayout2.mStableInsets);
            configuration.screenWidthDp = (int) (rect5.width() / displayLayout2.density());
            configuration.screenHeightDp = (int) (rect5.height() / displayLayout2.density());
            Resources resources = context.createConfigurationContext(configuration).getResources();
            int i5 = displayLayout2.mWidth;
            int i6 = displayLayout2.mHeight;
            if (configuration.orientation == 1) {
                z = true;
            } else {
                z = false;
            }
            DividerSnapAlgorithm dividerSnapAlgorithm = new DividerSnapAlgorithm(resources, i5, i6, dividerSize, z, displayLayout2.mStableInsets);
            rect2.set(rect);
            RotationUtils.rotateBounds(rect2, rect4, displayLayout.mRotation, i4);
            rect3.set(0, 0, displayLayout2.mWidth, displayLayout2.mHeight);
            if (displayLayout2.mWidth > displayLayout2.mHeight) {
                c = 2;
            } else {
                c = 1;
            }
            int i7 = 1;
            if (c != 1) {
                if (c != 2) {
                    i7 = -1;
                } else if ((rect3.right - rect2.right) - (rect2.left - rect3.left) < 0) {
                    i7 = 3;
                }
                i2 = i7;
            } else if ((rect3.bottom - rect2.bottom) - (rect2.top - rect3.top) < 0) {
                i2 = 4;
            } else {
                i2 = 2;
            }
            DockedDividerUtils.calculateBoundsForPosition(dividerSnapAlgorithm.calculateNonDismissingSnapTarget(DockedDividerUtils.calculatePositionForBounds(rect2, i2, dividerSize)).position, i2, rect2, displayLayout2.mWidth, displayLayout2.mHeight, dividerSize);
            Rect rect6 = new Rect(rect3);
            rect6.inset(displayLayout2.mStableInsets);
            rect2.intersect(rect6);
            i3 = Math.min(rect2.width(), i3);
        }
        return (int) (i3 / displayLayout.density());
    }

    public final DividerSnapAlgorithm getMinimizedSnapAlgorithm(boolean z) {
        if (this.mMinimizedSnapAlgorithm == null) {
            updateResources();
            Resources resources = this.mContext.getResources();
            DisplayLayout displayLayout = this.mDisplayLayout;
            Objects.requireNonNull(displayLayout);
            int i = displayLayout.mWidth;
            DisplayLayout displayLayout2 = this.mDisplayLayout;
            Objects.requireNonNull(displayLayout2);
            int i2 = displayLayout2.mHeight;
            int i3 = this.mDividerSize;
            DisplayLayout displayLayout3 = this.mDisplayLayout;
            Objects.requireNonNull(displayLayout3);
            this.mMinimizedSnapAlgorithm = new DividerSnapAlgorithm(resources, i, i2, i3, !this.mDisplayLayout.isLandscape(), displayLayout3.mStableInsets, getPrimarySplitSide(), true, z);
        }
        return this.mMinimizedSnapAlgorithm;
    }

    public final int getPrimarySplitSide() {
        boolean z;
        boolean z2;
        DisplayLayout displayLayout = this.mDisplayLayout;
        Resources resources = this.mContext.getResources();
        Objects.requireNonNull(displayLayout);
        int i = displayLayout.mWidth;
        int i2 = displayLayout.mHeight;
        int i3 = displayLayout.mRotation;
        if (i == i2 || !resources.getBoolean(17891701)) {
            z = false;
        } else {
            z = true;
        }
        if (!z || i <= i2) {
            z2 = true;
        } else if (i3 == 1) {
            z2 = true;
        } else {
            z2 = true;
        }
        if (z2) {
            return 3;
        }
        if (z2) {
            return 1;
        }
        if (!z2) {
            return -1;
        }
        if (this.mDisplayLayout.isLandscape()) {
            return 1;
        }
        return 2;
    }

    public final DividerSnapAlgorithm getSnapAlgorithm() {
        if (this.mSnapAlgorithm == null) {
            updateResources();
            Resources resources = this.mContext.getResources();
            DisplayLayout displayLayout = this.mDisplayLayout;
            Objects.requireNonNull(displayLayout);
            int i = displayLayout.mWidth;
            DisplayLayout displayLayout2 = this.mDisplayLayout;
            Objects.requireNonNull(displayLayout2);
            int i2 = displayLayout2.mHeight;
            int i3 = this.mDividerSize;
            DisplayLayout displayLayout3 = this.mDisplayLayout;
            Objects.requireNonNull(displayLayout3);
            this.mSnapAlgorithm = new DividerSnapAlgorithm(resources, i, i2, i3, !this.mDisplayLayout.isLandscape(), displayLayout3.mStableInsets, getPrimarySplitSide());
        }
        return this.mSnapAlgorithm;
    }

    public final void updateAdjustedBounds(int i, int i2, int i3) {
        DisplayLayout displayLayout = this.mDisplayLayout;
        int i4 = this.mDividerSize;
        int i5 = this.mDividerSizeInactive;
        Rect rect = this.mPrimary;
        Rect rect2 = this.mSecondary;
        if (this.mAdjustedPrimary == null) {
            this.mAdjustedPrimary = new Rect();
            this.mAdjustedSecondary = new Rect();
        }
        Rect rect3 = new Rect();
        Objects.requireNonNull(displayLayout);
        rect3.set(0, 0, displayLayout.mWidth, displayLayout.mHeight);
        rect3.inset(displayLayout.mStableInsets);
        float f = (i - i2) / (i3 - i2);
        int i6 = rect3.top;
        int i7 = this.mPrimary.bottom;
        int max = Math.max(0, displayLayout.mHeight - (Math.max(0, (i2 - i3) - (i7 - (i6 + ((int) ((i7 - i6) * 0.3f))))) + i));
        this.mAdjustedPrimary.set(rect);
        int i8 = -max;
        this.mAdjustedPrimary.offset(0, (i4 - ((int) MotionController$$ExternalSyntheticOutline0.m(1.0f, f, i4, i5 * f))) + i8);
        this.mAdjustedSecondary.set(rect2);
        this.mAdjustedSecondary.offset(0, i8);
    }

    public final void updateResources() {
        if (!this.mResourcesValid) {
            this.mResourcesValid = true;
            Resources resources = this.mContext.getResources();
            this.mDividerSize = DockedDividerUtils.getDividerSize(resources, DockedDividerUtils.getDividerInsets(resources));
            this.mDividerSizeInactive = (int) TypedValue.applyDimension(1, 4.0f, resources.getDisplayMetrics());
        }
    }

    public LegacySplitDisplayLayout(Context context, DisplayLayout displayLayout, LegacySplitScreenTaskListener legacySplitScreenTaskListener) {
        this.mTiles = legacySplitScreenTaskListener;
        this.mDisplayLayout = displayLayout;
        this.mContext = context;
    }

    public final void resizeSplits(int i, WindowContainerTransaction windowContainerTransaction) {
        if (resizeSplits(i)) {
            windowContainerTransaction.setBounds(this.mTiles.mPrimary.token, this.mPrimary);
            windowContainerTransaction.setBounds(this.mTiles.mSecondary.token, this.mSecondary);
            windowContainerTransaction.setSmallestScreenWidthDp(this.mTiles.mPrimary.token, getSmallestWidthDpForBounds(this.mContext, this.mDisplayLayout, this.mPrimary));
            windowContainerTransaction.setSmallestScreenWidthDp(this.mTiles.mSecondary.token, getSmallestWidthDpForBounds(this.mContext, this.mDisplayLayout, this.mSecondary));
        }
    }
}
