package androidx.swiperefreshlayout.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ListView;
import androidx.core.view.NestedScrollingChild;
import androidx.core.view.NestedScrollingChildHelper;
import androidx.core.view.NestedScrollingParent2;
import androidx.core.view.NestedScrollingParent3;
import androidx.core.view.NestedScrollingParentHelper;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.swiperefreshlayout.widget.CircularProgressDrawable;
import com.android.wifitrackerlib.StandardWifiEntry$$ExternalSyntheticLambda0;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class SwipeRefreshLayout extends ViewGroup implements NestedScrollingParent3, NestedScrollingParent2, NestedScrollingChild {
    public static final int CIRCLE_DIAMETER = 40;
    public static final int CIRCLE_DIAMETER_LARGE = 56;
    public static final int[] LAYOUT_ATTRS = {16842766};
    public AnonymousClass4 mAlphaMaxAnimation;
    public AnonymousClass4 mAlphaStartAnimation;
    public int mCircleDiameter;
    public int mCurrentTargetOffsetTop;
    public int mFrom;
    public float mInitialDownY;
    public float mInitialMotionY;
    public boolean mIsBeingDragged;
    public boolean mNestedScrollInProgress;
    public boolean mNotify;
    public int mOriginalOffsetTop;
    public CircularProgressDrawable mProgress;
    public AnonymousClass2 mScaleAnimation;
    public AnonymousClass3 mScaleDownAnimation;
    public int mSpinnerOffsetEnd;
    public View mTarget;
    public float mTotalDragDistance;
    public float mTotalUnconsumed;
    public int mTouchSlop;
    public boolean mRefreshing = false;
    public final int[] mParentScrollConsumed = new int[2];
    public final int[] mParentOffsetInWindow = new int[2];
    public final int[] mNestedScrollingV2ConsumedCompat = new int[2];
    public int mActivePointerId = -1;
    public int mCircleViewIndex = -1;
    public AnonymousClass1 mRefreshListener = new Animation.AnimationListener() { // from class: androidx.swiperefreshlayout.widget.SwipeRefreshLayout.1
        @Override // android.view.animation.Animation.AnimationListener
        public final void onAnimationRepeat(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public final void onAnimationStart(Animation animation) {
        }

        @Override // android.view.animation.Animation.AnimationListener
        public final void onAnimationEnd(Animation animation) {
            SwipeRefreshLayout swipeRefreshLayout = SwipeRefreshLayout.this;
            if (swipeRefreshLayout.mRefreshing) {
                swipeRefreshLayout.mProgress.setAlpha(255);
                SwipeRefreshLayout.this.mProgress.start();
                SwipeRefreshLayout swipeRefreshLayout2 = SwipeRefreshLayout.this;
                if (swipeRefreshLayout2.mNotify) {
                    Objects.requireNonNull(swipeRefreshLayout2);
                }
                SwipeRefreshLayout swipeRefreshLayout3 = SwipeRefreshLayout.this;
                swipeRefreshLayout3.mCurrentTargetOffsetTop = swipeRefreshLayout3.mCircleView.getTop();
                return;
            }
            swipeRefreshLayout.reset();
        }
    };
    public final AnonymousClass6 mAnimateToCorrectPosition = new Animation() { // from class: androidx.swiperefreshlayout.widget.SwipeRefreshLayout.6
        @Override // android.view.animation.Animation
        public final void applyTransformation(float f, Transformation transformation) {
            Objects.requireNonNull(SwipeRefreshLayout.this);
            SwipeRefreshLayout swipeRefreshLayout = SwipeRefreshLayout.this;
            int abs = swipeRefreshLayout.mSpinnerOffsetEnd - Math.abs(swipeRefreshLayout.mOriginalOffsetTop);
            SwipeRefreshLayout swipeRefreshLayout2 = SwipeRefreshLayout.this;
            int i = swipeRefreshLayout2.mFrom;
            SwipeRefreshLayout.this.setTargetOffsetTopAndBottom((i + ((int) ((abs - i) * f))) - swipeRefreshLayout2.mCircleView.getTop());
            CircularProgressDrawable circularProgressDrawable = SwipeRefreshLayout.this.mProgress;
            float f2 = 1.0f - f;
            Objects.requireNonNull(circularProgressDrawable);
            CircularProgressDrawable.Ring ring = circularProgressDrawable.mRing;
            Objects.requireNonNull(ring);
            if (f2 != ring.mArrowScale) {
                ring.mArrowScale = f2;
            }
            circularProgressDrawable.invalidateSelf();
        }
    };
    public final AnonymousClass7 mAnimateToStartPosition = new Animation() { // from class: androidx.swiperefreshlayout.widget.SwipeRefreshLayout.7
        @Override // android.view.animation.Animation
        public final void applyTransformation(float f, Transformation transformation) {
            SwipeRefreshLayout.this.moveToStart(f);
        }
    };
    public int mMediumAnimationDuration = getResources().getInteger(17694721);
    public final DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator(2.0f);
    public CircleImageView mCircleView = new CircleImageView(getContext());
    public final NestedScrollingParentHelper mNestedScrollingParentHelper = new NestedScrollingParentHelper();
    public final NestedScrollingChildHelper mNestedScrollingChildHelper = new NestedScrollingChildHelper(this);

    @Override // androidx.core.view.NestedScrollingParent2
    public final void onNestedPreScroll(View view, int i, int i2, int[] iArr, int i3) {
        if (i3 == 0) {
            onNestedPreScroll(view, i, i2, iArr);
        }
    }

    @Override // androidx.core.view.NestedScrollingParent3
    public final void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5, int[] iArr) {
        if (i5 == 0) {
            int i6 = iArr[1];
            int[] iArr2 = this.mParentOffsetInWindow;
            if (i5 == 0) {
                this.mNestedScrollingChildHelper.dispatchNestedScroll(i, i2, i3, i4, iArr2, i5, iArr);
            }
            int i7 = i4 - (iArr[1] - i6);
            int i8 = i7 == 0 ? i4 + this.mParentOffsetInWindow[1] : i7;
            if (i8 < 0 && !canChildScrollUp()) {
                float abs = this.mTotalUnconsumed + Math.abs(i8);
                this.mTotalUnconsumed = abs;
                moveSpinner(abs);
                iArr[1] = iArr[1] + i7;
            }
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final void onNestedScrollAccepted(View view, View view2, int i, int i2) {
        if (i2 == 0) {
            onNestedScrollAccepted(view, view2, i);
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final boolean onStartNestedScroll(View view, View view2, int i, int i2) {
        if (i2 == 0) {
            return onStartNestedScroll(view, view2, i);
        }
        return false;
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final void onStopNestedScroll(View view, int i) {
        if (i == 0) {
            onStopNestedScroll(view);
        }
    }

    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.swiperefreshlayout.widget.SwipeRefreshLayout.SavedState.1
            @Override // android.os.Parcelable.Creator
            public final SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public final SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        public final boolean mRefreshing;

        public SavedState(Parcelable parcelable, boolean z) {
            super(parcelable);
            this.mRefreshing = z;
        }

        public SavedState(Parcel parcel) {
            super(parcel);
            this.mRefreshing = parcel.readByte() != 0;
        }

        @Override // android.view.View.BaseSavedState, android.view.AbsSavedState, android.os.Parcelable
        public final void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeByte(this.mRefreshing ? (byte) 1 : (byte) 0);
        }
    }

    public final boolean canChildScrollUp() {
        View view = this.mTarget;
        if (view instanceof ListView) {
            return ((ListView) view).canScrollList(-1);
        }
        return view.canScrollVertically(-1);
    }

    @Override // android.view.View
    public final boolean dispatchNestedFling(float f, float f2, boolean z) {
        return this.mNestedScrollingChildHelper.dispatchNestedFling(f, f2, z);
    }

    @Override // android.view.View
    public final boolean dispatchNestedPreFling(float f, float f2) {
        return this.mNestedScrollingChildHelper.dispatchNestedPreFling(f, f2);
    }

    @Override // android.view.View
    public final boolean dispatchNestedPreScroll(int i, int i2, int[] iArr, int[] iArr2) {
        NestedScrollingChildHelper nestedScrollingChildHelper = this.mNestedScrollingChildHelper;
        Objects.requireNonNull(nestedScrollingChildHelper);
        return nestedScrollingChildHelper.dispatchNestedPreScroll(i, i2, iArr, iArr2, 0);
    }

    @Override // android.view.View
    public final boolean dispatchNestedScroll(int i, int i2, int i3, int i4, int[] iArr) {
        NestedScrollingChildHelper nestedScrollingChildHelper = this.mNestedScrollingChildHelper;
        Objects.requireNonNull(nestedScrollingChildHelper);
        return nestedScrollingChildHelper.dispatchNestedScrollInternal(i, i2, i3, i4, iArr, 0, null);
    }

    public final void ensureTarget() {
        if (this.mTarget == null) {
            for (int i = 0; i < getChildCount(); i++) {
                View childAt = getChildAt(i);
                if (!childAt.equals(this.mCircleView)) {
                    this.mTarget = childAt;
                    return;
                }
            }
        }
    }

    public final void finishSpinner(float f) {
        if (f > this.mTotalDragDistance) {
            setRefreshing(true, true);
            return;
        }
        this.mRefreshing = false;
        CircularProgressDrawable circularProgressDrawable = this.mProgress;
        Objects.requireNonNull(circularProgressDrawable);
        CircularProgressDrawable.Ring ring = circularProgressDrawable.mRing;
        Objects.requireNonNull(ring);
        ring.mStartTrim = 0.0f;
        CircularProgressDrawable.Ring ring2 = circularProgressDrawable.mRing;
        Objects.requireNonNull(ring2);
        ring2.mEndTrim = 0.0f;
        circularProgressDrawable.invalidateSelf();
        Animation.AnimationListener animationListener = new Animation.AnimationListener() { // from class: androidx.swiperefreshlayout.widget.SwipeRefreshLayout.5
            @Override // android.view.animation.Animation.AnimationListener
            public final void onAnimationRepeat(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public final void onAnimationStart(Animation animation) {
            }

            @Override // android.view.animation.Animation.AnimationListener
            public final void onAnimationEnd(Animation animation) {
                Objects.requireNonNull(SwipeRefreshLayout.this);
                SwipeRefreshLayout.this.startScaleDownAnimation(null);
            }
        };
        this.mFrom = this.mCurrentTargetOffsetTop;
        reset();
        setDuration(200L);
        setInterpolator(this.mDecelerateInterpolator);
        CircleImageView circleImageView = this.mCircleView;
        Objects.requireNonNull(circleImageView);
        circleImageView.mListener = animationListener;
        this.mCircleView.clearAnimation();
        this.mCircleView.startAnimation(this.mAnimateToStartPosition);
        CircularProgressDrawable circularProgressDrawable2 = this.mProgress;
        Objects.requireNonNull(circularProgressDrawable2);
        CircularProgressDrawable.Ring ring3 = circularProgressDrawable2.mRing;
        Objects.requireNonNull(ring3);
        if (ring3.mShowArrow) {
            ring3.mShowArrow = false;
        }
        circularProgressDrawable2.invalidateSelf();
    }

    @Override // android.view.ViewGroup
    public final int getChildDrawingOrder(int i, int i2) {
        int i3 = this.mCircleViewIndex;
        if (i3 < 0) {
            return i2;
        }
        if (i2 == i - 1) {
            return i3;
        }
        if (i2 >= i3) {
            return i2 + 1;
        }
        return i2;
    }

    @Override // android.view.ViewGroup
    public final int getNestedScrollAxes() {
        NestedScrollingParentHelper nestedScrollingParentHelper = this.mNestedScrollingParentHelper;
        Objects.requireNonNull(nestedScrollingParentHelper);
        return nestedScrollingParentHelper.mNestedScrollAxesNonTouch | nestedScrollingParentHelper.mNestedScrollAxesTouch;
    }

    @Override // android.view.View
    public final boolean hasNestedScrollingParent() {
        NestedScrollingChildHelper nestedScrollingChildHelper = this.mNestedScrollingChildHelper;
        Objects.requireNonNull(nestedScrollingChildHelper);
        if (nestedScrollingChildHelper.getNestedScrollingParentForType(0) != null) {
            return true;
        }
        return false;
    }

    @Override // android.view.View
    public final boolean isNestedScrollingEnabled() {
        NestedScrollingChildHelper nestedScrollingChildHelper = this.mNestedScrollingChildHelper;
        Objects.requireNonNull(nestedScrollingChildHelper);
        return nestedScrollingChildHelper.mIsNestedScrollingEnabled;
    }

    public final void moveSpinner(float f) {
        CircularProgressDrawable circularProgressDrawable = this.mProgress;
        Objects.requireNonNull(circularProgressDrawable);
        CircularProgressDrawable.Ring ring = circularProgressDrawable.mRing;
        Objects.requireNonNull(ring);
        boolean z = true;
        if (!ring.mShowArrow) {
            ring.mShowArrow = true;
        }
        circularProgressDrawable.invalidateSelf();
        float min = Math.min(1.0f, Math.abs(f / this.mTotalDragDistance));
        float max = (((float) Math.max(min - 0.4d, 0.0d)) * 5.0f) / 3.0f;
        float abs = Math.abs(f) - this.mTotalDragDistance;
        float f2 = this.mSpinnerOffsetEnd;
        double max2 = Math.max(0.0f, Math.min(abs, f2 * 2.0f) / f2) / 4.0f;
        float pow = ((float) (max2 - Math.pow(max2, 2.0d))) * 2.0f;
        int i = this.mOriginalOffsetTop + ((int) ((f2 * min) + (f2 * pow * 2.0f)));
        if (this.mCircleView.getVisibility() != 0) {
            this.mCircleView.setVisibility(0);
        }
        this.mCircleView.setScaleX(1.0f);
        this.mCircleView.setScaleY(1.0f);
        if (f < this.mTotalDragDistance) {
            if (this.mProgress.getAlpha() > 76) {
                AnonymousClass4 r13 = this.mAlphaStartAnimation;
                if (r13 == null || !r13.hasStarted() || r13.hasEnded()) {
                    z = false;
                }
                if (!z) {
                    this.mAlphaStartAnimation = startAlphaAnimation(this.mProgress.getAlpha(), 76);
                }
            }
        } else if (this.mProgress.getAlpha() < 255) {
            AnonymousClass4 r132 = this.mAlphaMaxAnimation;
            if (r132 == null || !r132.hasStarted() || r132.hasEnded()) {
                z = false;
            }
            if (!z) {
                this.mAlphaMaxAnimation = startAlphaAnimation(this.mProgress.getAlpha(), 255);
            }
        }
        CircularProgressDrawable circularProgressDrawable2 = this.mProgress;
        float min2 = Math.min(0.8f, max * 0.8f);
        Objects.requireNonNull(circularProgressDrawable2);
        CircularProgressDrawable.Ring ring2 = circularProgressDrawable2.mRing;
        Objects.requireNonNull(ring2);
        ring2.mStartTrim = 0.0f;
        CircularProgressDrawable.Ring ring3 = circularProgressDrawable2.mRing;
        Objects.requireNonNull(ring3);
        ring3.mEndTrim = min2;
        circularProgressDrawable2.invalidateSelf();
        CircularProgressDrawable circularProgressDrawable3 = this.mProgress;
        float min3 = Math.min(1.0f, max);
        Objects.requireNonNull(circularProgressDrawable3);
        CircularProgressDrawable.Ring ring4 = circularProgressDrawable3.mRing;
        Objects.requireNonNull(ring4);
        if (min3 != ring4.mArrowScale) {
            ring4.mArrowScale = min3;
        }
        circularProgressDrawable3.invalidateSelf();
        CircularProgressDrawable circularProgressDrawable4 = this.mProgress;
        Objects.requireNonNull(circularProgressDrawable4);
        CircularProgressDrawable.Ring ring5 = circularProgressDrawable4.mRing;
        Objects.requireNonNull(ring5);
        ring5.mRotation = ((pow * 2.0f) + ((max * 0.4f) - 0.25f)) * 0.5f;
        circularProgressDrawable4.invalidateSelf();
        setTargetOffsetTopAndBottom(i - this.mCurrentTargetOffsetTop);
    }

    public final void moveToStart(float f) {
        int i = this.mFrom;
        setTargetOffsetTopAndBottom((i + ((int) ((this.mOriginalOffsetTop - i) * f))) - this.mCircleView.getTop());
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void onNestedPreScroll(View view, int i, int i2, int[] iArr) {
        if (i2 > 0) {
            float f = this.mTotalUnconsumed;
            if (f > 0.0f) {
                float f2 = i2;
                if (f2 > f) {
                    iArr[1] = (int) f;
                    this.mTotalUnconsumed = 0.0f;
                } else {
                    this.mTotalUnconsumed = f - f2;
                    iArr[1] = i2;
                }
                moveSpinner(this.mTotalUnconsumed);
            }
        }
        int[] iArr2 = this.mParentScrollConsumed;
        if (dispatchNestedPreScroll(i - iArr[0], i2 - iArr[1], iArr2, null)) {
            iArr[0] = iArr[0] + iArr2[0];
            iArr[1] = iArr[1] + iArr2[1];
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void onNestedScrollAccepted(View view, View view2, int i) {
        NestedScrollingParentHelper nestedScrollingParentHelper = this.mNestedScrollingParentHelper;
        Objects.requireNonNull(nestedScrollingParentHelper);
        nestedScrollingParentHelper.mNestedScrollAxesTouch = i;
        startNestedScroll(i & 2);
        this.mTotalUnconsumed = 0.0f;
        this.mNestedScrollInProgress = true;
    }

    /* JADX WARN: Type inference failed for: r0v3, types: [androidx.swiperefreshlayout.widget.SwipeRefreshLayout$2, android.view.animation.Animation] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onRestoreInstanceState(android.os.Parcelable r4) {
        /*
            r3 = this;
            androidx.swiperefreshlayout.widget.SwipeRefreshLayout$SavedState r4 = (androidx.swiperefreshlayout.widget.SwipeRefreshLayout.SavedState) r4
            android.os.Parcelable r0 = r4.getSuperState()
            super.onRestoreInstanceState(r0)
            boolean r4 = r4.mRefreshing
            r0 = 0
            if (r4 == 0) goto L_0x0052
            boolean r1 = r3.mRefreshing
            if (r1 == r4) goto L_0x0052
            r3.mRefreshing = r4
            int r4 = r3.mSpinnerOffsetEnd
            int r1 = r3.mOriginalOffsetTop
            int r4 = r4 + r1
            int r1 = r3.mCurrentTargetOffsetTop
            int r4 = r4 - r1
            r3.setTargetOffsetTopAndBottom(r4)
            r3.mNotify = r0
            androidx.swiperefreshlayout.widget.SwipeRefreshLayout$1 r4 = r3.mRefreshListener
            androidx.swiperefreshlayout.widget.CircleImageView r1 = r3.mCircleView
            r1.setVisibility(r0)
            androidx.swiperefreshlayout.widget.CircularProgressDrawable r0 = r3.mProgress
            r1 = 255(0xff, float:3.57E-43)
            r0.setAlpha(r1)
            androidx.swiperefreshlayout.widget.SwipeRefreshLayout$2 r0 = new androidx.swiperefreshlayout.widget.SwipeRefreshLayout$2
            r0.<init>()
            r3.mScaleAnimation = r0
            int r1 = r3.mMediumAnimationDuration
            long r1 = (long) r1
            r0.setDuration(r1)
            if (r4 == 0) goto L_0x0045
            androidx.swiperefreshlayout.widget.CircleImageView r0 = r3.mCircleView
            java.util.Objects.requireNonNull(r0)
            r0.mListener = r4
        L_0x0045:
            androidx.swiperefreshlayout.widget.CircleImageView r4 = r3.mCircleView
            r4.clearAnimation()
            androidx.swiperefreshlayout.widget.CircleImageView r4 = r3.mCircleView
            androidx.swiperefreshlayout.widget.SwipeRefreshLayout$2 r3 = r3.mScaleAnimation
            r4.startAnimation(r3)
            goto L_0x0055
        L_0x0052:
            r3.setRefreshing(r4, r0)
        L_0x0055:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.swiperefreshlayout.widget.SwipeRefreshLayout.onRestoreInstanceState(android.os.Parcelable):void");
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final boolean onStartNestedScroll(View view, View view2, int i) {
        return isEnabled() && !this.mRefreshing && (i & 2) != 0;
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void onStopNestedScroll(View view) {
        NestedScrollingParentHelper nestedScrollingParentHelper = this.mNestedScrollingParentHelper;
        Objects.requireNonNull(nestedScrollingParentHelper);
        nestedScrollingParentHelper.mNestedScrollAxesTouch = 0;
        this.mNestedScrollInProgress = false;
        float f = this.mTotalUnconsumed;
        if (f > 0.0f) {
            finishSpinner(f);
            this.mTotalUnconsumed = 0.0f;
        } else {
            post(new StandardWifiEntry$$ExternalSyntheticLambda0(this, 1));
        }
        stopNestedScroll();
    }

    public final void reset() {
        this.mCircleView.clearAnimation();
        this.mProgress.stop();
        this.mCircleView.setVisibility(8);
        this.mCircleView.getBackground().setAlpha(255);
        this.mProgress.setAlpha(255);
        setTargetOffsetTopAndBottom(this.mOriginalOffsetTop - this.mCurrentTargetOffsetTop);
        this.mCurrentTargetOffsetTop = this.mCircleView.getTop();
    }

    @Override // android.view.View
    public final void setNestedScrollingEnabled(boolean z) {
        NestedScrollingChildHelper nestedScrollingChildHelper = this.mNestedScrollingChildHelper;
        Objects.requireNonNull(nestedScrollingChildHelper);
        if (nestedScrollingChildHelper.mIsNestedScrollingEnabled) {
            View view = nestedScrollingChildHelper.mView;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            ViewCompat.Api21Impl.stopNestedScroll(view);
        }
        nestedScrollingChildHelper.mIsNestedScrollingEnabled = z;
    }

    public final void setRefreshing(boolean z, boolean z2) {
        if (this.mRefreshing != z) {
            this.mNotify = z2;
            ensureTarget();
            this.mRefreshing = z;
            if (z) {
                int i = this.mCurrentTargetOffsetTop;
                AnonymousClass1 r4 = this.mRefreshListener;
                this.mFrom = i;
                reset();
                setDuration(200L);
                setInterpolator(this.mDecelerateInterpolator);
                if (r4 != null) {
                    CircleImageView circleImageView = this.mCircleView;
                    Objects.requireNonNull(circleImageView);
                    circleImageView.mListener = r4;
                }
                this.mCircleView.clearAnimation();
                this.mCircleView.startAnimation(this.mAnimateToCorrectPosition);
                return;
            }
            startScaleDownAnimation(this.mRefreshListener);
        }
    }

    public final void setTargetOffsetTopAndBottom(int i) {
        this.mCircleView.bringToFront();
        CircleImageView circleImageView = this.mCircleView;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        circleImageView.offsetTopAndBottom(i);
        this.mCurrentTargetOffsetTop = this.mCircleView.getTop();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [androidx.swiperefreshlayout.widget.SwipeRefreshLayout$4, android.view.animation.Animation] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final androidx.swiperefreshlayout.widget.SwipeRefreshLayout.AnonymousClass4 startAlphaAnimation(final int r2, final int r3) {
        /*
            r1 = this;
            androidx.swiperefreshlayout.widget.SwipeRefreshLayout$4 r0 = new androidx.swiperefreshlayout.widget.SwipeRefreshLayout$4
            r0.<init>()
            r2 = 300(0x12c, double:1.48E-321)
            r0.setDuration(r2)
            androidx.swiperefreshlayout.widget.CircleImageView r2 = r1.mCircleView
            java.util.Objects.requireNonNull(r2)
            r3 = 0
            r2.mListener = r3
            androidx.swiperefreshlayout.widget.CircleImageView r2 = r1.mCircleView
            r2.clearAnimation()
            androidx.swiperefreshlayout.widget.CircleImageView r1 = r1.mCircleView
            r1.startAnimation(r0)
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.swiperefreshlayout.widget.SwipeRefreshLayout.startAlphaAnimation(int, int):androidx.swiperefreshlayout.widget.SwipeRefreshLayout$4");
    }

    public final void startDragging(float f) {
        float f2 = this.mInitialDownY;
        int i = this.mTouchSlop;
        if (f - f2 > i && !this.mIsBeingDragged) {
            this.mInitialMotionY = f2 + i;
            this.mIsBeingDragged = true;
            this.mProgress.setAlpha(76);
        }
    }

    @Override // android.view.View
    public final boolean startNestedScroll(int i) {
        NestedScrollingChildHelper nestedScrollingChildHelper = this.mNestedScrollingChildHelper;
        Objects.requireNonNull(nestedScrollingChildHelper);
        return nestedScrollingChildHelper.startNestedScroll(i, 0);
    }

    /* JADX WARN: Type inference failed for: r0v0, types: [androidx.swiperefreshlayout.widget.SwipeRefreshLayout$3, android.view.animation.Animation] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void startScaleDownAnimation(android.view.animation.Animation.AnimationListener r4) {
        /*
            r3 = this;
            androidx.swiperefreshlayout.widget.SwipeRefreshLayout$3 r0 = new androidx.swiperefreshlayout.widget.SwipeRefreshLayout$3
            r0.<init>()
            r3.mScaleDownAnimation = r0
            r1 = 150(0x96, double:7.4E-322)
            r0.setDuration(r1)
            androidx.swiperefreshlayout.widget.CircleImageView r0 = r3.mCircleView
            java.util.Objects.requireNonNull(r0)
            r0.mListener = r4
            androidx.swiperefreshlayout.widget.CircleImageView r4 = r3.mCircleView
            r4.clearAnimation()
            androidx.swiperefreshlayout.widget.CircleImageView r4 = r3.mCircleView
            androidx.swiperefreshlayout.widget.SwipeRefreshLayout$3 r3 = r3.mScaleDownAnimation
            r4.startAnimation(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.swiperefreshlayout.widget.SwipeRefreshLayout.startScaleDownAnimation(android.view.animation.Animation$AnimationListener):void");
    }

    @Override // android.view.View
    public final void stopNestedScroll() {
        NestedScrollingChildHelper nestedScrollingChildHelper = this.mNestedScrollingChildHelper;
        Objects.requireNonNull(nestedScrollingChildHelper);
        nestedScrollingChildHelper.stopNestedScroll(0);
    }

    /* JADX WARN: Type inference failed for: r1v4, types: [androidx.swiperefreshlayout.widget.SwipeRefreshLayout$1] */
    /* JADX WARN: Type inference failed for: r1v5, types: [androidx.swiperefreshlayout.widget.SwipeRefreshLayout$6] */
    /* JADX WARN: Type inference failed for: r1v6, types: [androidx.swiperefreshlayout.widget.SwipeRefreshLayout$7] */
    public SwipeRefreshLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mTotalDragDistance = -1.0f;
        this.mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        setWillNotDraw(false);
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        this.mCircleDiameter = (int) (displayMetrics.density * 40.0f);
        CircularProgressDrawable circularProgressDrawable = new CircularProgressDrawable(getContext());
        this.mProgress = circularProgressDrawable;
        CircularProgressDrawable.Ring ring = circularProgressDrawable.mRing;
        float f = circularProgressDrawable.mResources.getDisplayMetrics().density;
        float f2 = 2.5f * f;
        Objects.requireNonNull(ring);
        ring.mStrokeWidth = f2;
        ring.mPaint.setStrokeWidth(f2);
        ring.mRingCenterRadius = 7.5f * f;
        ring.mColorIndex = 0;
        ring.mCurrentColor = ring.mColors[0];
        ring.mArrowWidth = (int) (10.0f * f);
        ring.mArrowHeight = (int) (5.0f * f);
        circularProgressDrawable.invalidateSelf();
        this.mCircleView.setImageDrawable(this.mProgress);
        this.mCircleView.setVisibility(8);
        addView(this.mCircleView);
        setChildrenDrawingOrderEnabled(true);
        int i = (int) (displayMetrics.density * 64.0f);
        this.mSpinnerOffsetEnd = i;
        this.mTotalDragDistance = i;
        setNestedScrollingEnabled(true);
        int i2 = -this.mCircleDiameter;
        this.mCurrentTargetOffsetTop = i2;
        this.mOriginalOffsetTop = i2;
        moveToStart(1.0f);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, LAYOUT_ATTRS);
        setEnabled(obtainStyledAttributes.getBoolean(0, true));
        obtainStyledAttributes.recycle();
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        reset();
    }

    @Override // android.view.ViewGroup
    public final boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        ensureTarget();
        int actionMasked = motionEvent.getActionMasked();
        int i = 0;
        if (!isEnabled() || canChildScrollUp() || this.mRefreshing || this.mNestedScrollInProgress) {
            return false;
        }
        if (actionMasked != 0) {
            if (actionMasked != 1) {
                if (actionMasked == 2) {
                    int i2 = this.mActivePointerId;
                    if (i2 == -1) {
                        Log.e("SwipeRefreshLayout", "Got ACTION_MOVE event but don't have an active pointer id.");
                        return false;
                    }
                    int findPointerIndex = motionEvent.findPointerIndex(i2);
                    if (findPointerIndex < 0) {
                        return false;
                    }
                    startDragging(motionEvent.getY(findPointerIndex));
                } else if (actionMasked != 3) {
                    if (actionMasked == 6) {
                        int actionIndex = motionEvent.getActionIndex();
                        if (motionEvent.getPointerId(actionIndex) == this.mActivePointerId) {
                            if (actionIndex == 0) {
                                i = 1;
                            }
                            this.mActivePointerId = motionEvent.getPointerId(i);
                        }
                    }
                }
            }
            this.mIsBeingDragged = false;
            this.mActivePointerId = -1;
        } else {
            setTargetOffsetTopAndBottom(this.mOriginalOffsetTop - this.mCircleView.getTop());
            int pointerId = motionEvent.getPointerId(0);
            this.mActivePointerId = pointerId;
            this.mIsBeingDragged = false;
            int findPointerIndex2 = motionEvent.findPointerIndex(pointerId);
            if (findPointerIndex2 < 0) {
                return false;
            }
            this.mInitialDownY = motionEvent.getY(findPointerIndex2);
        }
        return this.mIsBeingDragged;
    }

    @Override // android.view.ViewGroup, android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        int measuredWidth = getMeasuredWidth();
        int measuredHeight = getMeasuredHeight();
        if (getChildCount() != 0) {
            if (this.mTarget == null) {
                ensureTarget();
            }
            View view = this.mTarget;
            if (view != null) {
                int paddingLeft = getPaddingLeft();
                int paddingTop = getPaddingTop();
                view.layout(paddingLeft, paddingTop, ((measuredWidth - getPaddingLeft()) - getPaddingRight()) + paddingLeft, ((measuredHeight - getPaddingTop()) - getPaddingBottom()) + paddingTop);
                int measuredWidth2 = this.mCircleView.getMeasuredWidth();
                int measuredHeight2 = this.mCircleView.getMeasuredHeight();
                int i5 = measuredWidth / 2;
                int i6 = measuredWidth2 / 2;
                int i7 = this.mCurrentTargetOffsetTop;
                this.mCircleView.layout(i5 - i6, i7, i5 + i6, measuredHeight2 + i7);
            }
        }
    }

    @Override // android.view.View
    public final void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mTarget == null) {
            ensureTarget();
        }
        View view = this.mTarget;
        if (view != null) {
            view.measure(View.MeasureSpec.makeMeasureSpec((getMeasuredWidth() - getPaddingLeft()) - getPaddingRight(), 1073741824), View.MeasureSpec.makeMeasureSpec((getMeasuredHeight() - getPaddingTop()) - getPaddingBottom(), 1073741824));
            this.mCircleView.measure(View.MeasureSpec.makeMeasureSpec(this.mCircleDiameter, 1073741824), View.MeasureSpec.makeMeasureSpec(this.mCircleDiameter, 1073741824));
            this.mCircleViewIndex = -1;
            for (int i3 = 0; i3 < getChildCount(); i3++) {
                if (getChildAt(i3) == this.mCircleView) {
                    this.mCircleViewIndex = i3;
                    return;
                }
            }
        }
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final boolean onNestedFling(View view, float f, float f2, boolean z) {
        return dispatchNestedFling(f, f2, z);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final boolean onNestedPreFling(View view, float f, float f2) {
        return dispatchNestedPreFling(f, f2);
    }

    @Override // android.view.View
    public final Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), this.mRefreshing);
    }

    @Override // android.view.View
    public final boolean onTouchEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        int i = 0;
        if (!isEnabled() || canChildScrollUp() || this.mRefreshing || this.mNestedScrollInProgress) {
            return false;
        }
        if (actionMasked == 0) {
            this.mActivePointerId = motionEvent.getPointerId(0);
            this.mIsBeingDragged = false;
        } else if (actionMasked == 1) {
            int findPointerIndex = motionEvent.findPointerIndex(this.mActivePointerId);
            if (findPointerIndex < 0) {
                Log.e("SwipeRefreshLayout", "Got ACTION_UP event but don't have an active pointer id.");
                return false;
            }
            if (this.mIsBeingDragged) {
                this.mIsBeingDragged = false;
                finishSpinner((motionEvent.getY(findPointerIndex) - this.mInitialMotionY) * 0.5f);
            }
            this.mActivePointerId = -1;
            return false;
        } else if (actionMasked == 2) {
            int findPointerIndex2 = motionEvent.findPointerIndex(this.mActivePointerId);
            if (findPointerIndex2 < 0) {
                Log.e("SwipeRefreshLayout", "Got ACTION_MOVE event but have an invalid active pointer id.");
                return false;
            }
            float y = motionEvent.getY(findPointerIndex2);
            startDragging(y);
            if (this.mIsBeingDragged) {
                float f = (y - this.mInitialMotionY) * 0.5f;
                if (f <= 0.0f) {
                    return false;
                }
                getParent().requestDisallowInterceptTouchEvent(true);
                moveSpinner(f);
            }
        } else if (actionMasked == 3) {
            return false;
        } else {
            if (actionMasked == 5) {
                int actionIndex = motionEvent.getActionIndex();
                if (actionIndex < 0) {
                    Log.e("SwipeRefreshLayout", "Got ACTION_POINTER_DOWN event but have an invalid action index.");
                    return false;
                }
                this.mActivePointerId = motionEvent.getPointerId(actionIndex);
            } else if (actionMasked == 6) {
                int actionIndex2 = motionEvent.getActionIndex();
                if (motionEvent.getPointerId(actionIndex2) == this.mActivePointerId) {
                    if (actionIndex2 == 0) {
                        i = 1;
                    }
                    this.mActivePointerId = motionEvent.getPointerId(i);
                }
            }
        }
        return true;
    }

    @Override // android.view.View
    public final void setEnabled(boolean z) {
        super.setEnabled(z);
        if (!z) {
            reset();
        }
    }

    @Override // androidx.core.view.NestedScrollingParent2
    public final void onNestedScroll(View view, int i, int i2, int i3, int i4, int i5) {
        onNestedScroll(view, i, i2, i3, i4, i5, this.mNestedScrollingV2ConsumedCompat);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void onNestedScroll(View view, int i, int i2, int i3, int i4) {
        onNestedScroll(view, i, i2, i3, i4, 0, this.mNestedScrollingV2ConsumedCompat);
    }

    @Override // android.view.ViewGroup, android.view.ViewParent
    public final void requestDisallowInterceptTouchEvent(boolean z) {
        super.requestDisallowInterceptTouchEvent(z);
    }
}
