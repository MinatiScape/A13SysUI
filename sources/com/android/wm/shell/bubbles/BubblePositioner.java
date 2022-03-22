package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Insets;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.UserHandle;
import android.util.Log;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.WindowMetrics;
import com.android.wm.shell.bubbles.BubbleStackView;
import java.util.Objects;
/* loaded from: classes.dex */
public final class BubblePositioner {
    public int mBubbleSize;
    public Context mContext;
    public int mDefaultMaxBubbles;
    public int mExpandedViewLargeScreenInset;
    public int mExpandedViewLargeScreenWidth;
    public int mExpandedViewMinHeight;
    public int mExpandedViewPadding;
    public int mImeHeight;
    public boolean mImeVisible;
    public Insets mInsets;
    public boolean mIsLargeScreen;
    public int mManageButtonHeight;
    public int mMaxBubbles;
    public int mMinimumFlyoutWidthLargeScreen;
    public int mOverflowHeight;
    public int mOverflowWidth;
    public int mPointerHeight;
    public int mPointerMargin;
    public int mPointerOverlap;
    public int mPointerWidth;
    public Rect mPositionRect;
    public PointF mRestingStackPosition;
    public Rect mScreenRect;
    public int mSpacingBetweenBubbles;
    public WindowManager mWindowManager;
    public int mRotation = 0;
    public int[] mPaddings = new int[4];

    public final float getExpandedViewHeight(BubbleViewProvider bubbleViewProvider) {
        boolean z;
        float f;
        boolean z2 = true;
        if (bubbleViewProvider == null || "Overflow".equals(bubbleViewProvider.getKey())) {
            z = true;
        } else {
            z = false;
        }
        if (z && showBubblesVertically() && !this.mIsLargeScreen) {
            return -1.0f;
        }
        if (z) {
            f = this.mOverflowHeight;
        } else {
            Bubble bubble = (Bubble) bubbleViewProvider;
            Context context = this.mContext;
            Objects.requireNonNull(bubble);
            int i = bubble.mDesiredHeightResId;
            if (i == 0) {
                z2 = false;
            }
            if (z2) {
                String str = bubble.mPackageName;
                int identifier = bubble.mUser.getIdentifier();
                if (str != null) {
                    if (identifier == -1) {
                        identifier = 0;
                    }
                    try {
                        context.createContextAsUser(UserHandle.of(identifier), 0).getPackageManager().getResourcesForApplication(str).getDimensionPixelSize(i);
                    } catch (PackageManager.NameNotFoundException unused) {
                    } catch (Resources.NotFoundException e) {
                        Log.e("Bubble", "Couldn't find desired height res id", e);
                    }
                }
                f = 0;
            } else {
                f = bubble.mDesiredHeight * context.getResources().getDisplayMetrics().density;
            }
        }
        float max = Math.max(f, this.mExpandedViewMinHeight);
        if (max > getMaxExpandedViewHeight(z)) {
            return -1.0f;
        }
        return max;
    }

    public final PointF getDefaultStartPosition() {
        float f;
        boolean z = true;
        if (this.mContext.getResources().getConfiguration().getLayoutDirection() == 1) {
            z = false;
        }
        float max = Math.max(0.0f, Math.min(1.0f, this.mContext.getResources().getDimensionPixelOffset(2131165456) / this.mPositionRect.height()));
        RectF rectF = new RectF(this.mPositionRect);
        if (z) {
            f = rectF.left;
        } else {
            f = rectF.right;
        }
        return new PointF(f, (rectF.height() * max) + rectF.top);
    }

    public final PointF getExpandedBubbleXY(int i, BubbleStackView.StackViewState stackViewState) {
        int i2;
        float f;
        float f2;
        int i3;
        int i4;
        int i5;
        int i6;
        int i7;
        int i8 = this.mBubbleSize;
        int i9 = this.mSpacingBetweenBubbles;
        float f3 = (i8 + i9) * i;
        int i10 = stackViewState.numberOfBubbles;
        float f4 = ((i10 - 1) * i9) + (i8 * i10);
        if (showBubblesVertically()) {
            i2 = this.mPositionRect.centerY();
        } else {
            i2 = this.mPositionRect.centerX();
        }
        float f5 = i2 - (f4 / 2.0f);
        if (showBubblesVertically()) {
            f = f5 + f3;
            boolean z = this.mIsLargeScreen;
            if (z) {
                i6 = (this.mExpandedViewLargeScreenInset - this.mExpandedViewPadding) - this.mBubbleSize;
            } else {
                i6 = this.mPositionRect.left;
            }
            if (z) {
                i7 = (this.mPositionRect.right - this.mExpandedViewLargeScreenInset) + this.mExpandedViewPadding;
            } else {
                i7 = this.mPositionRect.right - this.mBubbleSize;
            }
            if (stackViewState.onLeft) {
                f2 = i6;
            } else {
                f2 = i7;
            }
        } else {
            float f6 = f5 + f3;
            f = this.mPositionRect.top + this.mExpandedViewPadding;
            f2 = f6;
        }
        if (!showBubblesVertically() || !this.mImeVisible) {
            return new PointF(f2, f);
        }
        int i11 = stackViewState.numberOfBubbles;
        float f7 = this.mPositionRect.top + this.mExpandedViewPadding;
        if (showBubblesVertically()) {
            if (this.mImeVisible) {
                i3 = this.mImeHeight;
            } else {
                i3 = 0;
            }
            int i12 = i3 + this.mInsets.bottom;
            int i13 = this.mSpacingBetweenBubbles;
            float f8 = i12 - (i13 * 2);
            int i14 = this.mBubbleSize * i11;
            int i15 = i11 - 1;
            float f9 = (i13 * i15) + i14;
            if (showBubblesVertically()) {
                i4 = this.mPositionRect.centerY();
            } else {
                i4 = this.mPositionRect.centerX();
            }
            float f10 = i4;
            float f11 = f9 / 2.0f;
            float f12 = f10 + f11;
            float f13 = f10 - f11;
            if (f12 > f8) {
                float f14 = f13 - (f12 - f8);
                float max = Math.max(f14, f7);
                if (f14 < f7) {
                    float f15 = ((i15 - 1) * this.mSpacingBetweenBubbles) + (this.mBubbleSize * i15);
                    if (showBubblesVertically()) {
                        i5 = this.mPositionRect.centerY();
                    } else {
                        i5 = this.mPositionRect.centerX();
                    }
                    float f16 = i5;
                    float f17 = f15 / 2.0f;
                    f13 = (f16 - f17) - ((f16 + f17) - f8);
                } else {
                    f13 = max;
                }
            }
            f7 = f13 + ((this.mBubbleSize + this.mSpacingBetweenBubbles) * i);
        }
        return new PointF(f2, f7);
    }

    public final float getExpandedViewY(BubbleViewProvider bubbleViewProvider, float f) {
        boolean z;
        int i;
        if (bubbleViewProvider == null || "Overflow".equals(bubbleViewProvider.getKey())) {
            z = true;
        } else {
            z = false;
        }
        float expandedViewHeight = getExpandedViewHeight(bubbleViewProvider);
        float expandedViewYTopAligned = getExpandedViewYTopAligned();
        if (!showBubblesVertically() || expandedViewHeight == -1.0f) {
            return expandedViewYTopAligned;
        }
        if (z) {
            i = this.mExpandedViewPadding;
        } else {
            i = this.mManageButtonHeight;
        }
        float pointerPosition = getPointerPosition(f);
        float f2 = expandedViewHeight / 2.0f;
        float f3 = pointerPosition + f2 + i;
        float f4 = pointerPosition - f2;
        Rect rect = this.mPositionRect;
        int i2 = rect.top;
        if (f4 > i2 && rect.bottom > f3) {
            return (pointerPosition - this.mPointerWidth) - f2;
        }
        if (f4 <= i2) {
            return expandedViewYTopAligned;
        }
        return ((rect.bottom - i) - expandedViewHeight) - this.mPointerWidth;
    }

    public final float getExpandedViewYTopAligned() {
        int i;
        int i2;
        int i3 = this.mPositionRect.top;
        if (showBubblesVertically()) {
            i = i3 - this.mPointerWidth;
            i2 = this.mExpandedViewPadding;
        } else {
            i = i3 + this.mBubbleSize;
            i2 = this.mPointerMargin;
        }
        return i + i2;
    }

    public final float getPointerPosition(float f) {
        int i = this.mBubbleSize;
        float round = (int) Math.round(Math.sqrt((((i * i) * 0.6597222f) * 4.0f) / 3.141592653589793d));
        if (showBubblesVertically()) {
            return (this.mBubbleSize / 2.0f) + f;
        }
        return ((round / 2.0f) + f) - this.mPointerWidth;
    }

    public final PointF getRestingPosition() {
        PointF pointF = this.mRestingStackPosition;
        if (pointF == null) {
            return getDefaultStartPosition();
        }
        return pointF;
    }

    public final void setRestingPosition(PointF pointF) {
        PointF pointF2 = this.mRestingStackPosition;
        if (pointF2 == null) {
            this.mRestingStackPosition = new PointF(pointF);
        } else {
            pointF2.set(pointF);
        }
    }

    public final boolean showBubblesVertically() {
        boolean z;
        if (this.mContext.getResources().getConfiguration().orientation == 2) {
            z = true;
        } else {
            z = false;
        }
        if (z || this.mIsLargeScreen) {
            return true;
        }
        return false;
    }

    public final void update() {
        boolean z;
        WindowMetrics currentWindowMetrics = this.mWindowManager.getCurrentWindowMetrics();
        if (currentWindowMetrics != null) {
            Insets insetsIgnoringVisibility = currentWindowMetrics.getWindowInsets().getInsetsIgnoringVisibility(WindowInsets.Type.navigationBars() | WindowInsets.Type.statusBars() | WindowInsets.Type.displayCutout());
            if (this.mContext.getResources().getConfiguration().smallestScreenWidthDp >= 600) {
                z = true;
            } else {
                z = false;
            }
            this.mIsLargeScreen = z;
            updateInternal(this.mRotation, insetsIgnoringVisibility, currentWindowMetrics.getBounds());
        }
    }

    public void updateInternal(int i, Insets insets, Rect rect) {
        int i2;
        int i3;
        int i4;
        this.mRotation = i;
        this.mInsets = insets;
        this.mScreenRect = new Rect(rect);
        Rect rect2 = new Rect(rect);
        this.mPositionRect = rect2;
        int i5 = rect2.left;
        Insets insets2 = this.mInsets;
        rect2.left = i5 + insets2.left;
        rect2.top += insets2.top;
        rect2.right -= insets2.right;
        rect2.bottom -= insets2.bottom;
        Resources resources = this.mContext.getResources();
        this.mBubbleSize = resources.getDimensionPixelSize(2131165452);
        this.mSpacingBetweenBubbles = resources.getDimensionPixelSize(2131165453);
        this.mDefaultMaxBubbles = resources.getInteger(2131492874);
        this.mExpandedViewPadding = resources.getDimensionPixelSize(2131165421);
        this.mExpandedViewLargeScreenWidth = (int) (rect.width() * 0.72f);
        if (this.mIsLargeScreen) {
            i2 = (rect.width() - this.mExpandedViewLargeScreenWidth) / 2;
        } else {
            i2 = this.mExpandedViewPadding;
        }
        this.mExpandedViewLargeScreenInset = i2;
        if (this.mIsLargeScreen) {
            i3 = this.mExpandedViewLargeScreenWidth;
        } else {
            i3 = resources.getDimensionPixelSize(2131165422);
        }
        this.mOverflowWidth = i3;
        this.mPointerWidth = resources.getDimensionPixelSize(2131165451);
        this.mPointerHeight = resources.getDimensionPixelSize(2131165447);
        this.mPointerMargin = resources.getDimensionPixelSize(2131165448);
        this.mPointerOverlap = resources.getDimensionPixelSize(2131165449);
        this.mManageButtonHeight = resources.getDimensionPixelSize(2131165432);
        this.mExpandedViewMinHeight = resources.getDimensionPixelSize(2131165420);
        this.mOverflowHeight = resources.getDimensionPixelSize(2131165442);
        this.mMinimumFlyoutWidthLargeScreen = resources.getDimensionPixelSize(2131165459);
        if (showBubblesVertically()) {
            i4 = 0;
        } else {
            i4 = this.mExpandedViewPadding * 2;
        }
        int min = Math.min(this.mPositionRect.width(), this.mPositionRect.height()) - i4;
        int i6 = this.mBubbleSize;
        int i7 = (min - i6) / (i6 + this.mSpacingBetweenBubbles);
        int i8 = this.mDefaultMaxBubbles;
        if (i7 >= i8) {
            i7 = i8;
        }
        this.mMaxBubbles = i7;
    }

    public BubblePositioner(Context context, WindowManager windowManager) {
        this.mContext = context;
        this.mWindowManager = windowManager;
        update();
    }

    public final int getMaxExpandedViewHeight(boolean z) {
        int i;
        int i2;
        int i3;
        int expandedViewYTopAligned = ((int) getExpandedViewYTopAligned()) - this.mInsets.top;
        if (showBubblesVertically()) {
            i = 0;
        } else {
            i = this.mPointerHeight;
        }
        if (showBubblesVertically()) {
            i2 = this.mPointerWidth;
        } else {
            i2 = this.mPointerHeight + this.mPointerMargin;
        }
        if (z) {
            i3 = this.mExpandedViewPadding;
        } else {
            i3 = this.mManageButtonHeight;
        }
        return (((this.mPositionRect.height() - expandedViewYTopAligned) - i) - i2) - i3;
    }
}
