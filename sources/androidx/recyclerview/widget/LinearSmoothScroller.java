package androidx.recyclerview.widget;

import android.content.Context;
import android.graphics.PointF;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.LinearInterpolator;
import androidx.recyclerview.widget.RecyclerView;
/* loaded from: classes.dex */
public class LinearSmoothScroller extends RecyclerView.SmoothScroller {
    public final DisplayMetrics mDisplayMetrics;
    public float mMillisPerPixel;
    public PointF mTargetVector;
    public final LinearInterpolator mLinearInterpolator = new LinearInterpolator();
    public final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator();
    public boolean mHasCalculatedMillisPerPixel = false;
    public int mInterimTargetDx = 0;
    public int mInterimTargetDy = 0;

    public static int calculateDtToFit(int i, int i2, int i3, int i4, int i5) {
        if (i5 == -1) {
            return i3 - i;
        }
        if (i5 == 0) {
            int i6 = i3 - i;
            if (i6 > 0) {
                return i6;
            }
            int i7 = i4 - i2;
            if (i7 < 0) {
                return i7;
            }
            return 0;
        } else if (i5 == 1) {
            return i4 - i2;
        } else {
            throw new IllegalArgumentException("snap preference should be one of the constants defined in SmoothScroller, starting with SNAP_");
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
    public void onStop() {
        this.mInterimTargetDy = 0;
        this.mInterimTargetDx = 0;
        this.mTargetVector = null;
    }

    public float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
        return 25.0f / displayMetrics.densityDpi;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.SmoothScroller
    public void onTargetFound(View view, RecyclerView.SmoothScroller.Action action) {
        int i;
        int i2;
        int i3;
        int i4;
        PointF pointF = this.mTargetVector;
        int i5 = 1;
        int i6 = 0;
        if (pointF == null || pointF.x == 0.0f) {
            i = 0;
        } else if (i4 > 0) {
            i = 1;
        } else {
            i = -1;
        }
        RecyclerView.LayoutManager layoutManager = this.mLayoutManager;
        if (layoutManager == null || !layoutManager.canScrollHorizontally()) {
            i2 = 0;
        } else {
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
            i2 = calculateDtToFit(layoutManager.getDecoratedLeft(view) - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin, layoutManager.getDecoratedRight(view) + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin, layoutManager.getPaddingLeft(), layoutManager.mWidth - layoutManager.getPaddingRight(), i);
        }
        PointF pointF2 = this.mTargetVector;
        if (pointF2 == null || pointF2.y == 0.0f) {
            i5 = 0;
        } else if (i3 <= 0) {
            i5 = -1;
        }
        RecyclerView.LayoutManager layoutManager2 = this.mLayoutManager;
        if (layoutManager2 != null && layoutManager2.canScrollVertically()) {
            RecyclerView.LayoutParams layoutParams2 = (RecyclerView.LayoutParams) view.getLayoutParams();
            i6 = calculateDtToFit(layoutManager2.getDecoratedTop(view) - ((ViewGroup.MarginLayoutParams) layoutParams2).topMargin, layoutManager2.getDecoratedBottom(view) + ((ViewGroup.MarginLayoutParams) layoutParams2).bottomMargin, layoutManager2.getPaddingTop(), layoutManager2.mHeight - layoutManager2.getPaddingBottom(), i5);
        }
        int calculateTimeForDeceleration = calculateTimeForDeceleration((int) Math.sqrt((i6 * i6) + (i2 * i2)));
        if (calculateTimeForDeceleration > 0) {
            action.update(-i2, -i6, calculateTimeForDeceleration, this.mDecelerateInterpolator);
        }
    }

    public LinearSmoothScroller(Context context) {
        this.mDisplayMetrics = context.getResources().getDisplayMetrics();
    }

    public final int calculateTimeForDeceleration(int i) {
        return (int) Math.ceil(calculateTimeForScrolling(i) / 0.3356d);
    }

    public int calculateTimeForScrolling(int i) {
        float abs = Math.abs(i);
        if (!this.mHasCalculatedMillisPerPixel) {
            this.mMillisPerPixel = calculateSpeedPerPixel(this.mDisplayMetrics);
            this.mHasCalculatedMillisPerPixel = true;
        }
        return (int) Math.ceil(abs * this.mMillisPerPixel);
    }
}
