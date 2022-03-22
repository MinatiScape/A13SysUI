package com.android.systemui.statusbar.notification.row;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.phone.StatusBar$$ExternalSyntheticLambda18;
import java.util.Objects;
/* loaded from: classes.dex */
public class NotificationGuts extends FrameLayout {
    public int mActualHeight;
    public Drawable mBackground;
    public int mClipBottomAmount;
    public int mClipTopAmount;
    public OnGutsClosedListener mClosedListener;
    public boolean mExposed;
    public AnonymousClass2 mFalsingCheck;
    public GutsContent mGutsContent;
    public AnonymousClass1 mGutsContentAccessibilityDelegate;
    public Handler mHandler;
    public OnHeightChangedListener mHeightListener;
    public boolean mNeedsFalsingProtection;

    /* loaded from: classes.dex */
    public class AnimateCloseListener extends AnimatorListenerAdapter {
        public final GutsContent mGutsContent;
        public final View mView;

        public AnimateCloseListener(View view, GutsContent gutsContent) {
            this.mView = view;
            this.mGutsContent = gutsContent;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            NotificationGuts notificationGuts = NotificationGuts.this;
            Objects.requireNonNull(notificationGuts);
            if (!notificationGuts.mExposed) {
                this.mView.setVisibility(8);
                this.mGutsContent.onFinishedClosing();
            }
        }
    }

    /* loaded from: classes.dex */
    public interface GutsContent {
        int getActualHeight();

        View getContentView();

        boolean handleCloseControls(boolean z, boolean z2);

        default boolean isLeavebehind() {
            return false;
        }

        boolean needsFalsingProtection();

        default void onFinishedClosing() {
        }

        void setAccessibilityDelegate(View.AccessibilityDelegate accessibilityDelegate);

        void setGutsParent(NotificationGuts notificationGuts);

        boolean shouldBeSaved();

        boolean willBeRemoved();
    }

    /* loaded from: classes.dex */
    public interface OnGutsClosedListener {
    }

    /* loaded from: classes.dex */
    public interface OnHeightChangedListener {
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.notification.row.NotificationGuts$1] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.statusbar.notification.row.NotificationGuts$2] */
    public NotificationGuts(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mGutsContentAccessibilityDelegate = new View.AccessibilityDelegate() { // from class: com.android.systemui.statusbar.notification.row.NotificationGuts.1
            @Override // android.view.View.AccessibilityDelegate
            public final void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_LONG_CLICK);
            }

            @Override // android.view.View.AccessibilityDelegate
            public final boolean performAccessibilityAction(View view, int i, Bundle bundle) {
                if (super.performAccessibilityAction(view, i, bundle)) {
                    return true;
                }
                if (i != 32) {
                    return false;
                }
                NotificationGuts.this.closeControls(view, false);
                return true;
            }
        };
        setWillNotDraw(false);
        this.mHandler = new Handler();
        this.mFalsingCheck = new Runnable() { // from class: com.android.systemui.statusbar.notification.row.NotificationGuts.2
            @Override // java.lang.Runnable
            public final void run() {
                NotificationGuts notificationGuts = NotificationGuts.this;
                if (notificationGuts.mNeedsFalsingProtection && notificationGuts.mExposed) {
                    notificationGuts.closeControls(-1, -1, false, false);
                }
            }
        };
    }

    public final void closeControls(View view, boolean z) {
        int[] iArr = new int[2];
        int[] iArr2 = new int[2];
        getLocationOnScreen(iArr);
        view.getLocationOnScreen(iArr2);
        closeControls((iArr2[0] - iArr[0]) + (view.getWidth() / 2), (iArr2[1] - iArr[1]) + (view.getHeight() / 2), z, false);
    }

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    /* loaded from: classes.dex */
    public static class AnimateOpenListener extends AnimatorListenerAdapter {
        public final Runnable mOnAnimationEnd;

        public AnimateOpenListener(StatusBar$$ExternalSyntheticLambda18 statusBar$$ExternalSyntheticLambda18) {
            this.mOnAnimationEnd = statusBar$$ExternalSyntheticLambda18;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            super.onAnimationEnd(animator);
            Runnable runnable = this.mOnAnimationEnd;
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    @Override // android.view.View
    public final void drawableHotspotChanged(float f, float f2) {
        Drawable drawable = this.mBackground;
        if (drawable != null) {
            drawable.setHotspot(f, f2);
        }
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void drawableStateChanged() {
        Drawable drawable = this.mBackground;
        if (drawable != null && drawable.isStateful()) {
            drawable.setState(getDrawableState());
        }
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        Drawable drawable = this.mBackground;
        int i = this.mClipTopAmount;
        int i2 = this.mActualHeight - this.mClipBottomAmount;
        if (drawable != null && i < i2) {
            drawable.setBounds(0, i, getWidth(), i2);
            drawable.draw(canvas);
        }
    }

    public final void resetFalsingCheck() {
        this.mHandler.removeCallbacks(this.mFalsingCheck);
        if (this.mNeedsFalsingProtection && this.mExposed) {
            this.mHandler.postDelayed(this.mFalsingCheck, 8000L);
        }
    }

    @VisibleForTesting
    public void setExposed(boolean z, boolean z2) {
        GutsContent gutsContent;
        boolean z3 = this.mExposed;
        this.mExposed = z;
        this.mNeedsFalsingProtection = z2;
        if (!z || !z2) {
            this.mHandler.removeCallbacks(this.mFalsingCheck);
        } else {
            resetFalsingCheck();
        }
        if (z3 != this.mExposed && (gutsContent = this.mGutsContent) != null) {
            View contentView = gutsContent.getContentView();
            contentView.sendAccessibilityEvent(32);
            if (this.mExposed) {
                contentView.requestAccessibilityFocus();
            }
        }
    }

    @VisibleForTesting
    public void animateClose(int i, int i2, boolean z) {
        if (!isAttachedToWindow()) {
            Log.w("NotificationGuts", "Failed to animate guts close");
            this.mGutsContent.onFinishedClosing();
        } else if (z) {
            if (i == -1 || i2 == -1) {
                i = (getRight() + getLeft()) / 2;
                i2 = getTop() + (getHeight() / 2);
            }
            Animator createCircularReveal = ViewAnimationUtils.createCircularReveal(this, i, i2, (float) Math.hypot(Math.max(getWidth() - i, i), Math.max(getHeight() - i2, i2)), 0.0f);
            createCircularReveal.setDuration(360L);
            createCircularReveal.setInterpolator(Interpolators.FAST_OUT_LINEAR_IN);
            createCircularReveal.addListener(new AnimateCloseListener(this, this.mGutsContent));
            createCircularReveal.start();
        } else {
            animate().alpha(0.0f).setDuration(240L).setInterpolator(Interpolators.ALPHA_OUT).setListener(new AnimateCloseListener(this, this.mGutsContent)).start();
        }
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        Drawable drawable = ((FrameLayout) this).mContext.getDrawable(2131232523);
        this.mBackground = drawable;
        if (drawable != null) {
            drawable.setCallback(this);
        }
    }

    @Override // android.view.View
    public final boolean verifyDrawable(Drawable drawable) {
        if (super.verifyDrawable(drawable) || drawable == this.mBackground) {
            return true;
        }
        return false;
    }

    public NotificationGuts(Context context) {
        this(context, null);
    }

    public final void closeControls(int i, int i2, boolean z, boolean z2) {
        if (getWindowToken() == null) {
            OnGutsClosedListener onGutsClosedListener = this.mClosedListener;
            if (onGutsClosedListener != null) {
                ((NotificationGutsManager$$ExternalSyntheticLambda1) onGutsClosedListener).onGutsClosed(this);
                return;
            }
            return;
        }
        GutsContent gutsContent = this.mGutsContent;
        if (gutsContent == null || !gutsContent.handleCloseControls(z, z2)) {
            animateClose(i, i2, true);
            setExposed(false, this.mNeedsFalsingProtection);
            OnGutsClosedListener onGutsClosedListener2 = this.mClosedListener;
            if (onGutsClosedListener2 != null) {
                ((NotificationGutsManager$$ExternalSyntheticLambda1) onGutsClosedListener2).onGutsClosed(this);
            }
        }
    }
}
