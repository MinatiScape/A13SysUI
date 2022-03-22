package androidx.leanback.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import androidx.leanback.R$styleable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class BaseGridView extends RecyclerView {
    public boolean mHasOverlappingRendering = true;
    public int mInitialPrefetchItemCount = 4;
    public GridLayoutManager mLayoutManager;
    public int mPrivateFlag;

    /* loaded from: classes.dex */
    public interface OnLayoutCompletedListener {
        void onLayoutCompleted();
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public final void setLayoutManager(RecyclerView.LayoutManager layoutManager) {
        if (layoutManager == null) {
            super.setLayoutManager(null);
            GridLayoutManager gridLayoutManager = this.mLayoutManager;
            if (gridLayoutManager != null) {
                gridLayoutManager.mBaseGridView = null;
                gridLayoutManager.mGrid = null;
            }
            this.mLayoutManager = null;
            return;
        }
        GridLayoutManager gridLayoutManager2 = (GridLayoutManager) layoutManager;
        this.mLayoutManager = gridLayoutManager2;
        gridLayoutManager2.mBaseGridView = this;
        gridLayoutManager2.mGrid = null;
        super.setLayoutManager(layoutManager);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public final void smoothScrollBy(int i, int i2) {
        smoothScrollBy$1(i, i2, false);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public final void smoothScrollBy$1(int i, int i2) {
        smoothScrollBy$1(i, i2, false);
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    public final int getChildDrawingOrder(int i, int i2) {
        int indexOfChild;
        GridLayoutManager gridLayoutManager = this.mLayoutManager;
        Objects.requireNonNull(gridLayoutManager);
        View findViewByPosition = gridLayoutManager.findViewByPosition(gridLayoutManager.mFocusPosition);
        if (findViewByPosition == null || i2 < (indexOfChild = indexOfChild(findViewByPosition))) {
            return i2;
        }
        if (i2 < i - 1) {
            indexOfChild = ((indexOfChild + i) - 1) - i2;
        }
        return indexOfChild;
    }

    @SuppressLint({"CustomViewStyleable"})
    public final void initBaseGridViewAttributes(Context context, AttributeSet attributeSet) {
        int i;
        int i2;
        int i3;
        int i4;
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.lbBaseGridView);
        boolean z = obtainStyledAttributes.getBoolean(4, false);
        boolean z2 = obtainStyledAttributes.getBoolean(3, false);
        GridLayoutManager gridLayoutManager = this.mLayoutManager;
        Objects.requireNonNull(gridLayoutManager);
        int i5 = gridLayoutManager.mFlag & (-6145);
        if (z) {
            i = 2048;
        } else {
            i = 0;
        }
        int i6 = i | i5;
        if (z2) {
            i2 = 4096;
        } else {
            i2 = 0;
        }
        gridLayoutManager.mFlag = i6 | i2;
        boolean z3 = obtainStyledAttributes.getBoolean(6, true);
        boolean z4 = obtainStyledAttributes.getBoolean(5, true);
        GridLayoutManager gridLayoutManager2 = this.mLayoutManager;
        Objects.requireNonNull(gridLayoutManager2);
        int i7 = gridLayoutManager2.mFlag & (-24577);
        if (z3) {
            i3 = 8192;
        } else {
            i3 = 0;
        }
        int i8 = i3 | i7;
        if (z4) {
            i4 = 16384;
        } else {
            i4 = 0;
        }
        gridLayoutManager2.mFlag = i8 | i4;
        GridLayoutManager gridLayoutManager3 = this.mLayoutManager;
        int dimensionPixelSize = obtainStyledAttributes.getDimensionPixelSize(2, obtainStyledAttributes.getDimensionPixelSize(8, 0));
        Objects.requireNonNull(gridLayoutManager3);
        if (gridLayoutManager3.mOrientation == 1) {
            gridLayoutManager3.mVerticalSpacing = dimensionPixelSize;
            gridLayoutManager3.mSpacingPrimary = dimensionPixelSize;
        } else {
            gridLayoutManager3.mVerticalSpacing = dimensionPixelSize;
            gridLayoutManager3.mSpacingSecondary = dimensionPixelSize;
        }
        GridLayoutManager gridLayoutManager4 = this.mLayoutManager;
        int dimensionPixelSize2 = obtainStyledAttributes.getDimensionPixelSize(1, obtainStyledAttributes.getDimensionPixelSize(7, 0));
        Objects.requireNonNull(gridLayoutManager4);
        if (gridLayoutManager4.mOrientation == 0) {
            gridLayoutManager4.mSpacingPrimary = dimensionPixelSize2;
        } else {
            gridLayoutManager4.mSpacingSecondary = dimensionPixelSize2;
        }
        if (obtainStyledAttributes.hasValue(0)) {
            int i9 = obtainStyledAttributes.getInt(0, 0);
            GridLayoutManager gridLayoutManager5 = this.mLayoutManager;
            Objects.requireNonNull(gridLayoutManager5);
            gridLayoutManager5.mGravity = i9;
            requestLayout();
        }
        obtainStyledAttributes.recycle();
    }

    @Override // androidx.recyclerview.widget.RecyclerView, android.view.ViewGroup
    public final boolean onRequestFocusInDescendants(int i, Rect rect) {
        if ((this.mPrivateFlag & 1) == 1) {
            return false;
        }
        GridLayoutManager gridLayoutManager = this.mLayoutManager;
        Objects.requireNonNull(gridLayoutManager);
        View findViewByPosition = gridLayoutManager.findViewByPosition(gridLayoutManager.mFocusPosition);
        if (findViewByPosition != null) {
            return findViewByPosition.requestFocus(i, rect);
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:13:0x0020  */
    /* JADX WARN: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onRtlPropertiesChanged(int r6) {
        /*
            r5 = this;
            androidx.leanback.widget.GridLayoutManager r5 = r5.mLayoutManager
            if (r5 == 0) goto L_0x0035
            java.util.Objects.requireNonNull(r5)
            int r0 = r5.mOrientation
            r1 = 0
            r2 = 1
            if (r0 != 0) goto L_0x0012
            if (r6 != r2) goto L_0x0017
            r0 = 262144(0x40000, float:3.67342E-40)
            goto L_0x0018
        L_0x0012:
            if (r6 != r2) goto L_0x0017
            r0 = 524288(0x80000, float:7.34684E-40)
            goto L_0x0018
        L_0x0017:
            r0 = r1
        L_0x0018:
            int r3 = r5.mFlag
            r4 = 786432(0xc0000, float:1.102026E-39)
            r4 = r4 & r3
            if (r4 != r0) goto L_0x0020
            goto L_0x0035
        L_0x0020:
            r4 = -786433(0xfffffffffff3ffff, float:NaN)
            r3 = r3 & r4
            r0 = r0 | r3
            r0 = r0 | 256(0x100, float:3.59E-43)
            r5.mFlag = r0
            androidx.leanback.widget.WindowAlignment r5 = r5.mWindowAlignment
            androidx.leanback.widget.WindowAlignment$Axis r5 = r5.horizontal
            if (r6 != r2) goto L_0x0030
            r1 = r2
        L_0x0030:
            java.util.Objects.requireNonNull(r5)
            r5.mReversedFlow = r1
        L_0x0035:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.BaseGridView.onRtlPropertiesChanged(int):void");
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public final void scrollToPosition(int i) {
        boolean z;
        GridLayoutManager gridLayoutManager = this.mLayoutManager;
        Objects.requireNonNull(gridLayoutManager);
        if ((gridLayoutManager.mFlag & 64) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            GridLayoutManager gridLayoutManager2 = this.mLayoutManager;
            Objects.requireNonNull(gridLayoutManager2);
            gridLayoutManager2.setSelection(i, false);
            return;
        }
        super.scrollToPosition(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView
    public final void smoothScrollToPosition(int i) {
        boolean z;
        GridLayoutManager gridLayoutManager = this.mLayoutManager;
        Objects.requireNonNull(gridLayoutManager);
        if ((gridLayoutManager.mFlag & 64) != 0) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            GridLayoutManager gridLayoutManager2 = this.mLayoutManager;
            Objects.requireNonNull(gridLayoutManager2);
            gridLayoutManager2.setSelection(i, false);
            return;
        }
        super.smoothScrollToPosition(i);
    }

    public BaseGridView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this);
        this.mLayoutManager = gridLayoutManager;
        setLayoutManager(gridLayoutManager);
        this.mPreserveFocusAfterLayout = false;
        setDescendantFocusability(262144);
        this.mHasFixedSize = true;
        setChildrenDrawingOrderEnabled(true);
        setWillNotDraw(true);
        setOverScrollMode(2);
        SimpleItemAnimator simpleItemAnimator = (SimpleItemAnimator) this.mItemAnimator;
        Objects.requireNonNull(simpleItemAnimator);
        simpleItemAnimator.mSupportsChangeAnimations = false;
        this.mRecyclerListeners.add(new RecyclerView.RecyclerListener() { // from class: androidx.leanback.widget.BaseGridView.1
            @Override // androidx.recyclerview.widget.RecyclerView.RecyclerListener
            public final void onViewRecycled(RecyclerView.ViewHolder viewHolder) {
                GridLayoutManager gridLayoutManager2 = BaseGridView.this.mLayoutManager;
                Objects.requireNonNull(gridLayoutManager2);
                if (viewHolder.getAbsoluteAdapterPosition() != -1) {
                    Objects.requireNonNull(gridLayoutManager2.mChildrenStates);
                }
            }
        });
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchGenericFocusedEvent(MotionEvent motionEvent) {
        return super.dispatchGenericFocusedEvent(motionEvent);
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchKeyEvent(KeyEvent keyEvent) {
        if (super.dispatchKeyEvent(keyEvent)) {
            return true;
        }
        return false;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final boolean dispatchTouchEvent(MotionEvent motionEvent) {
        return super.dispatchTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public final View focusSearch(int i) {
        if (isFocused()) {
            GridLayoutManager gridLayoutManager = this.mLayoutManager;
            Objects.requireNonNull(gridLayoutManager);
            View findViewByPosition = gridLayoutManager.findViewByPosition(gridLayoutManager.mFocusPosition);
            if (findViewByPosition != null) {
                return focusSearch(findViewByPosition, i);
            }
        }
        return super.focusSearch(i);
    }

    public final boolean isChildrenDrawingOrderEnabledInternal() {
        return isChildrenDrawingOrderEnabled();
    }

    @Override // android.view.View
    public final void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        GridLayoutManager gridLayoutManager = this.mLayoutManager;
        if (z) {
            int i2 = gridLayoutManager.mFocusPosition;
            while (true) {
                View findViewByPosition = gridLayoutManager.findViewByPosition(i2);
                if (findViewByPosition != null) {
                    if (findViewByPosition.getVisibility() != 0 || !findViewByPosition.hasFocusable()) {
                        i2++;
                    } else {
                        findViewByPosition.requestFocus();
                        return;
                    }
                } else {
                    return;
                }
            }
        } else {
            Objects.requireNonNull(gridLayoutManager);
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewManager
    public final void removeView(View view) {
        boolean z;
        if (!view.hasFocus() || !isFocusable()) {
            z = false;
        } else {
            z = true;
        }
        if (z) {
            this.mPrivateFlag = 1 | this.mPrivateFlag;
            requestFocus();
        }
        super.removeView(view);
        if (z) {
            this.mPrivateFlag ^= -2;
        }
    }

    @Override // android.view.ViewGroup
    public final void removeViewAt(int i) {
        boolean hasFocus = getChildAt(i).hasFocus();
        if (hasFocus) {
            this.mPrivateFlag |= 1;
            requestFocus();
        }
        super.removeViewAt(i);
        if (hasFocus) {
            this.mPrivateFlag ^= -2;
        }
    }

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return this.mHasOverlappingRendering;
    }
}
