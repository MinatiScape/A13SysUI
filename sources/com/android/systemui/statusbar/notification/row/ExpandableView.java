package com.android.systemui.statusbar.notification.row;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import androidx.fragment.R$id;
import com.android.systemui.Dumpable;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.notification.stack.ExpandableViewState;
import com.android.wm.shell.bubbles.BubbleController$BubblesImpl$$ExternalSyntheticLambda2;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public abstract class ExpandableView extends FrameLayout implements Dumpable {
    public static Rect mClipRect = new Rect();
    public int mActualHeight;
    public int mClipBottomAmount;
    public int mClipTopAmount;
    public float mContentTransformationAmount;
    public float mContentTranslation;
    public boolean mFirstInSection;
    public boolean mInShelf;
    public boolean mIsLastChild;
    public boolean mLastInSection;
    public OnHeightChangedListener mOnHeightChangedListener;
    public boolean mTransformingInShelf;
    public ViewGroup mTransientContainer;
    public boolean mWillBeGone;
    public int mMinimumHeightForClipping = 0;
    public float mExtraWidthForClipping = 0.0f;
    public ArrayList<View> mMatchParentViews = new ArrayList<>();
    public boolean mClipToActualHeight = true;
    public boolean mChangingPosition = false;
    public final ExpandableViewState mViewState = createExpandableViewState();
    public int mContentShift = getResources().getDimensionPixelSize(2131167013);

    /* loaded from: classes.dex */
    public interface OnHeightChangedListener {
        void onHeightChanged(ExpandableView expandableView, boolean z);

        void onReset(ExpandableNotificationRow expandableNotificationRow);
    }

    public boolean areChildrenExpanded() {
        return false;
    }

    public void getExtraBottomPadding() {
    }

    public float getHeaderVisibleAmount() {
        return 1.0f;
    }

    public float getOutlineAlpha() {
        return 0.0f;
    }

    public int getOutlineTranslation() {
        return 0;
    }

    public final int getRelativeTopPadding(View view) {
        int i = 0;
        while (view.getParent() instanceof ViewGroup) {
            i += view.getTop();
            view = (View) view.getParent();
            if (view == this) {
                break;
            }
        }
        return i;
    }

    public StatusBarIconView getShelfIcon() {
        return null;
    }

    public View getShelfTransformationTarget() {
        return null;
    }

    public boolean hasExpandingChild() {
        return false;
    }

    public boolean isAboveShelf() {
        return false;
    }

    public boolean isChildInGroup() {
        return false;
    }

    public boolean isContentExpandable() {
        return false;
    }

    public boolean isExpandAnimationRunning() {
        return false;
    }

    public boolean isGroupExpanded() {
        return false;
    }

    public boolean isGroupExpansionChanging() {
        return false;
    }

    public boolean isHeadsUpAnimatingAway() {
        return false;
    }

    public boolean isPinned() {
        return false;
    }

    public boolean isRemoved() {
        return false;
    }

    public boolean isSummaryWithChildren() {
        return false;
    }

    public boolean isTransparent() {
        return false;
    }

    public boolean mustStayOnScreen() {
        return false;
    }

    public boolean needsClippingToShelf() {
        return !(this instanceof NotificationShelf);
    }

    public void performAddAnimation(long j, long j2) {
        performAddAnimation(j, j2, false);
    }

    public abstract void performAddAnimation(long j, long j2, boolean z);

    public abstract long performRemoveAnimation(long j, long j2, float f, boolean z, float f2, Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter);

    public void setActualHeightAnimating(boolean z) {
    }

    public void setBelowSpeedBump(boolean z) {
    }

    public boolean setBottomRoundness(float f, boolean z) {
        return false;
    }

    public void setFakeShadowIntensity(float f, float f2, int i, int i2) {
    }

    public void setHeadsUpIsVisible() {
    }

    public void setHideSensitive(boolean z, boolean z2, long j, long j2) {
    }

    public void setHideSensitiveForIntrinsicHeight(boolean z) {
    }

    public boolean setTopRoundness(float f, boolean z) {
        return false;
    }

    public boolean shouldClipToActualHeight() {
        return true;
    }

    public boolean showingPulsing() {
        return false;
    }

    public ExpandableViewState createExpandableViewState() {
        return new ExpandableViewState();
    }

    public void notifyHeightChanged(boolean z) {
        OnHeightChangedListener onHeightChangedListener = this.mOnHeightChangedListener;
        if (onHeightChangedListener != null) {
            onHeightChangedListener.onHeightChanged(this, z);
        }
    }

    public boolean pointInView(float f, float f2, float f3) {
        float f4 = this.mClipTopAmount;
        float f5 = this.mActualHeight;
        if (f < (-f3) || f2 < f4 - f3 || f >= (((FrameLayout) this).mRight - ((FrameLayout) this).mLeft) + f3 || f2 >= f5 + f3) {
            return false;
        }
        return true;
    }

    public final void removeFromTransientContainer() {
        ViewGroup viewGroup = this.mTransientContainer;
        if (viewGroup != null) {
            ViewParent parent = getParent();
            if (parent != viewGroup) {
                Log.w("ExpandableView", "Expandable view " + this + " has transient container " + viewGroup + " but different parent " + parent);
                this.mTransientContainer = null;
                return;
            }
            viewGroup.removeTransientView(this);
            this.mTransientContainer = null;
        }
    }

    public final ExpandableViewState resetViewState() {
        boolean z;
        this.mViewState.height = getIntrinsicHeight();
        ExpandableViewState expandableViewState = this.mViewState;
        if (getVisibility() == 8) {
            z = true;
        } else {
            z = false;
        }
        expandableViewState.gone = z;
        ExpandableViewState expandableViewState2 = this.mViewState;
        expandableViewState2.alpha = 1.0f;
        expandableViewState2.notGoneIndex = -1;
        expandableViewState2.xTranslation = getTranslationX();
        ExpandableViewState expandableViewState3 = this.mViewState;
        expandableViewState3.hidden = false;
        expandableViewState3.scaleX = getScaleX();
        this.mViewState.scaleY = getScaleY();
        ExpandableViewState expandableViewState4 = this.mViewState;
        expandableViewState4.inShelf = false;
        expandableViewState4.headsUpIsVisible = false;
        if (this instanceof ExpandableNotificationRow) {
            ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) this;
            ArrayList<ExpandableNotificationRow> attachedChildren = expandableNotificationRow.getAttachedChildren();
            if (expandableNotificationRow.mIsSummaryWithChildren && attachedChildren != null) {
                for (ExpandableNotificationRow expandableNotificationRow2 : attachedChildren) {
                    expandableNotificationRow2.resetViewState();
                }
            }
        }
        return this.mViewState;
    }

    public void setActualHeight(int i, boolean z) {
        this.mActualHeight = i;
        updateClipping();
        if (z) {
            notifyHeightChanged(false);
        }
    }

    public void setClipBottomAmount(int i) {
        this.mClipBottomAmount = i;
        updateClipping();
    }

    public void setClipTopAmount(int i) {
        this.mClipTopAmount = i;
        updateClipping();
    }

    @Override // android.view.View
    public final void setLayerType(int i, Paint paint) {
        if (i == 0 || hasOverlappingRendering()) {
            super.setLayerType(i, paint);
        }
    }

    public void updateClipping() {
        if (!this.mClipToActualHeight || !shouldClipToActualHeight()) {
            setClipBounds(null);
            return;
        }
        int i = this.mClipTopAmount;
        int i2 = this.mActualHeight;
        getExtraBottomPadding();
        int max = Math.max(Math.max((0 + i2) - this.mClipBottomAmount, i), this.mMinimumHeightForClipping);
        int i3 = (int) (this.mExtraWidthForClipping / 2.0f);
        mClipRect.set(-i3, i, getWidth() + i3, max);
        setClipBounds(mClipRect);
    }

    public ExpandableView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        IndentingPrintWriter asIndenting = R$id.asIndenting(printWriter);
        asIndenting.println(getClass().getSimpleName());
        R$id.withIncreasedIndent(asIndenting, new BubbleController$BubblesImpl$$ExternalSyntheticLambda2(this, asIndenting, fileDescriptor, strArr));
    }

    public final void getBoundsOnScreen(Rect rect, boolean z) {
        super.getBoundsOnScreen(rect, z);
        if (getTranslationY() + getTop() < 0.0f) {
            rect.top = (int) (getTranslationY() + getTop() + rect.top);
        }
        int i = rect.top;
        rect.bottom = this.mActualHeight + i;
        rect.top = i + this.mClipTopAmount;
    }

    public int getCollapsedHeight() {
        return getHeight();
    }

    @Override // android.view.View
    public final void getDrawingRect(Rect rect) {
        super.getDrawingRect(rect);
        rect.left = (int) (getTranslationX() + rect.left);
        rect.right = (int) (getTranslationX() + rect.right);
        rect.bottom = (int) (getTranslationY() + rect.top + this.mActualHeight);
        rect.top = (int) (getTranslationY() + this.mClipTopAmount + rect.top);
    }

    public int getHeadsUpHeightWithoutHeader() {
        return getHeight();
    }

    public int getIntrinsicHeight() {
        return getHeight();
    }

    public int getMaxContentHeight() {
        return getHeight();
    }

    public int getMinHeight(boolean z) {
        return getHeight();
    }

    public int getPinnedHeadsUpHeight() {
        return getIntrinsicHeight();
    }

    public float getTranslation() {
        return getTranslationX();
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        if (!super.hasOverlappingRendering() || this.mActualHeight > getHeight()) {
            return false;
        }
        return true;
    }

    @Override // android.view.View
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mContentShift = getResources().getDimensionPixelSize(2131167013);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateClipping();
    }

    @Override // android.widget.FrameLayout, android.view.View
    public void onMeasure(int i, int i2) {
        int i3;
        int size = View.MeasureSpec.getSize(i2);
        int paddingEnd = getPaddingEnd() + getPaddingStart();
        int mode = View.MeasureSpec.getMode(i2);
        int i4 = Integer.MAX_VALUE;
        if (!(mode == 0 || size == 0)) {
            i4 = Math.min(size, Integer.MAX_VALUE);
        }
        int makeMeasureSpec = View.MeasureSpec.makeMeasureSpec(i4, Integer.MIN_VALUE);
        int childCount = getChildCount();
        int i5 = 0;
        for (int i6 = 0; i6 < childCount; i6++) {
            View childAt = getChildAt(i6);
            if (childAt.getVisibility() != 8) {
                ViewGroup.LayoutParams layoutParams = childAt.getLayoutParams();
                int i7 = layoutParams.height;
                if (i7 != -1) {
                    if (i7 >= 0) {
                        i3 = View.MeasureSpec.makeMeasureSpec(Math.min(i7, i4), 1073741824);
                    } else {
                        i3 = makeMeasureSpec;
                    }
                    childAt.measure(ViewGroup.getChildMeasureSpec(i, paddingEnd, layoutParams.width), i3);
                    i5 = Math.max(i5, childAt.getMeasuredHeight());
                } else {
                    this.mMatchParentViews.add(childAt);
                }
            }
        }
        if (mode != 1073741824) {
            size = Math.min(i4, i5);
        }
        int makeMeasureSpec2 = View.MeasureSpec.makeMeasureSpec(size, 1073741824);
        Iterator<View> it = this.mMatchParentViews.iterator();
        while (it.hasNext()) {
            View next = it.next();
            next.measure(ViewGroup.getChildMeasureSpec(i, paddingEnd, next.getLayoutParams().width), makeMeasureSpec2);
        }
        this.mMatchParentViews.clear();
        setMeasuredDimension(View.MeasureSpec.getSize(i), size);
    }

    public final void removeFromTransientContainerForAdditionTo(ViewGroup viewGroup) {
        ViewParent parent = getParent();
        ViewGroup viewGroup2 = this.mTransientContainer;
        if (parent == null || parent == viewGroup) {
            removeFromTransientContainer();
        } else if (viewGroup2 == null) {
            throw new IllegalStateException("Can't add view " + this + " to container " + viewGroup + "; current parent " + parent + " is not a transient container");
        } else if (viewGroup2 == parent) {
            Log.w("ExpandableView", "Removing view " + this + " from transient container " + viewGroup2 + " in preparation for moving to parent " + viewGroup);
            viewGroup2.removeTransientView(this);
            this.mTransientContainer = null;
        } else {
            throw new IllegalStateException("Expandable view " + this + " has transient container " + viewGroup2 + " but different parent " + parent);
        }
    }
}
