package com.android.wm.shell.common.split;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Property;
import android.view.GestureDetector;
import android.view.InsetsController;
import android.view.InsetsSource;
import android.view.InsetsState;
import android.view.MotionEvent;
import android.view.SurfaceControlViewHost;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import com.android.wm.shell.animation.Interpolators;
import java.util.Objects;
/* loaded from: classes.dex */
public class DividerView extends FrameLayout implements View.OnTouchListener {
    public static final AnonymousClass1 DIVIDER_HEIGHT_PROPERTY = new AnonymousClass1();
    public View mBackground;
    public FrameLayout mDividerBar;
    public GestureDetector mDoubleTapDetector;
    public float mExpandedTaskBarHeight;
    public DividerHandleView mHandle;
    public boolean mInteractive;
    public boolean mMoving;
    public SplitLayout mSplitLayout;
    public SplitWindowManager mSplitWindowManager;
    public int mStartPos;
    public int mTouchElevation;
    public VelocityTracker mVelocityTracker;
    public SurfaceControlViewHost mViewHost;
    public final int mTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    public boolean mSetTouchRegion = true;
    public final Rect mDividerBounds = new Rect();
    public final Rect mTempRect = new Rect();
    public AnonymousClass2 mAnimatorListener = new AnimatorListenerAdapter() { // from class: com.android.wm.shell.common.split.DividerView.2
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationCancel(Animator animator) {
            DividerView.this.mSetTouchRegion = true;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            DividerView.this.mSetTouchRegion = true;
        }
    };

    /* renamed from: com.android.wm.shell.common.split.DividerView$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 extends Property<DividerView, Integer> {
        public AnonymousClass1() {
            super(Integer.class, "height");
        }

        @Override // android.util.Property
        public final Integer get(DividerView dividerView) {
            return Integer.valueOf(dividerView.mDividerBar.getLayoutParams().height);
        }

        @Override // android.util.Property
        public final void set(DividerView dividerView, Integer num) {
            DividerView dividerView2 = dividerView;
            ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) dividerView2.mDividerBar.getLayoutParams();
            marginLayoutParams.height = num.intValue();
            dividerView2.mDividerBar.setLayoutParams(marginLayoutParams);
        }
    }

    /* loaded from: classes.dex */
    public class DoubleTapListener extends GestureDetector.SimpleOnGestureListener {
        public DoubleTapListener() {
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnDoubleTapListener
        public final boolean onDoubleTap(MotionEvent motionEvent) {
            SplitLayout splitLayout = DividerView.this.mSplitLayout;
            if (splitLayout == null) {
                return true;
            }
            Objects.requireNonNull(splitLayout);
            splitLayout.mSplitLayoutHandler.onDoubleTappedDivider();
            return true;
        }
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.android.wm.shell.common.split.DividerView$2] */
    public DividerView(Context context) {
        super(context);
    }

    public final void releaseTouching() {
        setSlippery(true);
        this.mHandle.setTouching(false, true);
        this.mHandle.animate().setInterpolator(Interpolators.FAST_OUT_SLOW_IN).setDuration(200L).translationZ(0.0f).start();
    }

    public final void onInsetsChanged(InsetsState insetsState, boolean z) {
        this.mTempRect.set(this.mSplitLayout.getDividerBounds());
        InsetsSource source = insetsState.getSource(21);
        if (source.getFrame().height() >= this.mExpandedTaskBarHeight) {
            Rect rect = this.mTempRect;
            rect.inset(source.calculateVisibleInsets(rect));
        }
        if (!this.mTempRect.equals(this.mDividerBounds)) {
            if (z) {
                ObjectAnimator ofInt = ObjectAnimator.ofInt(this, DIVIDER_HEIGHT_PROPERTY, this.mDividerBounds.height(), this.mTempRect.height());
                ofInt.setInterpolator(InsetsController.RESIZE_INTERPOLATOR);
                ofInt.setDuration(300L);
                ofInt.addListener(this.mAnimatorListener);
                ofInt.start();
            } else {
                DIVIDER_HEIGHT_PROPERTY.set(this, Integer.valueOf(this.mTempRect.height()));
                this.mSetTouchRegion = true;
            }
            this.mDividerBounds.set(this.mTempRect);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:21:0x0049, code lost:
        if (r7 != 3) goto L_0x0121;
     */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onTouch(android.view.View r7, android.view.MotionEvent r8) {
        /*
            Method dump skipped, instructions count: 291
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.common.split.DividerView.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    public final void setSlippery(boolean z) {
        boolean z2;
        if (this.mViewHost != null) {
            WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) getLayoutParams();
            int i = layoutParams.flags;
            if ((i & 536870912) != 0) {
                z2 = true;
            } else {
                z2 = false;
            }
            if (z2 != z) {
                if (z) {
                    layoutParams.flags = i | 536870912;
                } else {
                    layoutParams.flags = (-536870913) & i;
                }
                this.mViewHost.relayout(layoutParams);
            }
        }
    }

    @Override // android.view.View
    public final void onFinishInflate() {
        super.onFinishInflate();
        this.mDividerBar = (FrameLayout) findViewById(2131427859);
        this.mHandle = (DividerHandleView) findViewById(2131427863);
        this.mBackground = findViewById(2131427862);
        this.mExpandedTaskBarHeight = getResources().getDimensionPixelSize(17105563);
        this.mTouchElevation = getResources().getDimensionPixelSize(2131165677);
        this.mDoubleTapDetector = new GestureDetector(getContext(), new DoubleTapListener());
        this.mInteractive = true;
        setOnTouchListener(this);
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mSetTouchRegion) {
            this.mTempRect.set(this.mHandle.getLeft(), this.mHandle.getTop(), this.mHandle.getRight(), this.mHandle.getBottom());
            this.mSplitWindowManager.setTouchRegion(this.mTempRect);
            this.mSetTouchRegion = false;
        }
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.android.wm.shell.common.split.DividerView$2] */
    public DividerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.android.wm.shell.common.split.DividerView$2] */
    public DividerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    /* JADX WARN: Type inference failed for: r1v7, types: [com.android.wm.shell.common.split.DividerView$2] */
    public DividerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }
}
