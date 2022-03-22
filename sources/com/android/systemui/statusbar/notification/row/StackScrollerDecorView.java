package com.android.systemui.statusbar.notification.row;

import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.PathInterpolator;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.biometrics.UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda0;
import com.android.systemui.statusbar.notification.stack.PeopleHubView;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda2;
import com.android.wm.shell.bubbles.BubbleStackView$$ExternalSyntheticLambda19;
/* loaded from: classes.dex */
public abstract class StackScrollerDecorView extends ExpandableView {
    public static final /* synthetic */ int $r8$clinit = 0;
    public View mContent;
    public boolean mContentAnimating;
    public View mSecondaryView;
    public boolean mIsVisible = true;
    public boolean mContentVisible = true;
    public boolean mIsSecondaryVisible = true;
    public int mDuration = 260;
    public final BubbleStackView$$ExternalSyntheticLambda19 mContentVisibilityEndRunnable = new BubbleStackView$$ExternalSyntheticLambda19(this, 5);
    public boolean mSecondaryAnimating = false;
    public final TaskView$$ExternalSyntheticLambda2 mSecondaryVisibilityEndRunnable = new TaskView$$ExternalSyntheticLambda2(this, 5);

    public abstract View findContentView();

    public abstract View findSecondaryView();

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean isTransparent() {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public boolean needsClippingToShelf() {
        return this instanceof PeopleHubView;
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final void performAddAnimation(long j, long j2) {
        setContentVisible(true);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final long performRemoveAnimation(long j, long j2, float f, boolean z, float f2, Runnable runnable, AnimatorListenerAdapter animatorListenerAdapter) {
        setContentVisible(false, true, runnable);
        return 0L;
    }

    public final void setContentVisible(boolean z) {
        setContentVisible(z, true, null);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView
    public final void performAddAnimation(long j, long j2, boolean z) {
        setContentVisible(true);
    }

    public final void setContentVisible(boolean z, boolean z2, Runnable runnable) {
        if (this.mContentVisible != z) {
            this.mContentAnimating = z2;
            this.mContentVisible = z;
            setViewVisible(this.mContent, z, z2, runnable == null ? this.mContentVisibilityEndRunnable : new UdfpsController$UdfpsOverlayController$$ExternalSyntheticLambda0(this, runnable, 1));
        } else if (runnable != null) {
            runnable.run();
        }
        if (!this.mContentAnimating) {
            this.mContentVisibilityEndRunnable.run();
        }
    }

    public final void setSecondaryVisible(boolean z, boolean z2) {
        if (this.mIsSecondaryVisible != z) {
            this.mSecondaryAnimating = z2;
            this.mIsSecondaryVisible = z;
            setViewVisible(this.mSecondaryView, z, z2, this.mSecondaryVisibilityEndRunnable);
        }
        if (!this.mSecondaryAnimating) {
            this.mSecondaryVisibilityEndRunnable.run();
        }
    }

    public final void setViewVisible(View view, boolean z, boolean z2, Runnable runnable) {
        float f;
        PathInterpolator pathInterpolator;
        if (view != null) {
            if (view.getVisibility() != 0) {
                view.setVisibility(0);
            }
            view.animate().cancel();
            if (z) {
                f = 1.0f;
            } else {
                f = 0.0f;
            }
            if (!z2) {
                view.setAlpha(f);
                if (runnable != null) {
                    runnable.run();
                    return;
                }
                return;
            }
            if (z) {
                pathInterpolator = Interpolators.ALPHA_IN;
            } else {
                pathInterpolator = Interpolators.ALPHA_OUT;
            }
            view.animate().alpha(f).setInterpolator(pathInterpolator).setDuration(this.mDuration).withEndAction(runnable);
        }
    }

    public void setVisible(boolean z, boolean z2) {
        int i;
        if (this.mIsVisible != z) {
            this.mIsVisible = z;
            if (z2) {
                if (z) {
                    setVisibility(0);
                    this.mWillBeGone = false;
                    notifyHeightChanged(false);
                } else {
                    this.mWillBeGone = true;
                }
                setContentVisible(z, true, null);
                return;
            }
            if (z) {
                i = 0;
            } else {
                i = 8;
            }
            setVisibility(i);
            setContentVisible(z, false, null);
            this.mWillBeGone = false;
            notifyHeightChanged(false);
        }
    }

    public StackScrollerDecorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setClipChildren(false);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mContent = findContentView();
        this.mSecondaryView = findSecondaryView();
        setVisible(false, false);
        setSecondaryVisible(false, false);
    }

    @Override // com.android.systemui.statusbar.notification.row.ExpandableView, android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        setOutlineProvider(null);
    }

    @VisibleForTesting
    public boolean isSecondaryVisible() {
        return this.mIsSecondaryVisible;
    }
}
