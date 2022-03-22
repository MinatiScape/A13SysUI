package androidx.recyclerview.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.MotionEvent;
import android.view.View;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.widget.RecyclerView;
import java.util.Objects;
import java.util.WeakHashMap;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class FastScroller extends RecyclerView.ItemDecoration implements RecyclerView.OnItemTouchListener {
    public final AnonymousClass1 mHideRunnable;
    public float mHorizontalDragX;
    public int mHorizontalThumbCenterX;
    public final StateListDrawable mHorizontalThumbDrawable;
    public final int mHorizontalThumbHeight;
    public int mHorizontalThumbWidth;
    public final Drawable mHorizontalTrackDrawable;
    public final int mHorizontalTrackHeight;
    public final int mMargin;
    public final AnonymousClass2 mOnScrollListener;
    public RecyclerView mRecyclerView;
    public final int mScrollbarMinimumRange;
    public final ValueAnimator mShowHideAnimator;
    public float mVerticalDragY;
    public int mVerticalThumbCenterY;
    public final StateListDrawable mVerticalThumbDrawable;
    public int mVerticalThumbHeight;
    public final int mVerticalThumbWidth;
    public final Drawable mVerticalTrackDrawable;
    public final int mVerticalTrackWidth;
    public static final int[] PRESSED_STATE_SET = {16842919};
    public static final int[] EMPTY_STATE_SET = new int[0];
    public int mRecyclerViewWidth = 0;
    public int mRecyclerViewHeight = 0;
    public boolean mNeedVerticalScrollbar = false;
    public boolean mNeedHorizontalScrollbar = false;
    public int mState = 0;
    public int mDragState = 0;
    public final int[] mVerticalRange = new int[2];
    public final int[] mHorizontalRange = new int[2];
    public int mAnimationState = 0;

    /* loaded from: classes.dex */
    public class AnimatorListener extends AnimatorListenerAdapter {
        public boolean mCanceled = false;

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationCancel(Animator animator) {
            this.mCanceled = true;
        }

        public AnimatorListener() {
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public final void onAnimationEnd(Animator animator) {
            if (this.mCanceled) {
                this.mCanceled = false;
            } else if (((Float) FastScroller.this.mShowHideAnimator.getAnimatedValue()).floatValue() == 0.0f) {
                FastScroller fastScroller = FastScroller.this;
                fastScroller.mAnimationState = 0;
                fastScroller.setState(0);
            } else {
                FastScroller fastScroller2 = FastScroller.this;
                fastScroller2.mAnimationState = 2;
                fastScroller2.mRecyclerView.invalidate();
            }
        }
    }

    /* loaded from: classes.dex */
    public class AnimatorUpdater implements ValueAnimator.AnimatorUpdateListener {
        public AnimatorUpdater() {
        }

        @Override // android.animation.ValueAnimator.AnimatorUpdateListener
        public final void onAnimationUpdate(ValueAnimator valueAnimator) {
            int floatValue = (int) (((Float) valueAnimator.getAnimatedValue()).floatValue() * 255.0f);
            FastScroller.this.mVerticalThumbDrawable.setAlpha(floatValue);
            FastScroller.this.mVerticalTrackDrawable.setAlpha(floatValue);
            FastScroller fastScroller = FastScroller.this;
            Objects.requireNonNull(fastScroller);
            fastScroller.mRecyclerView.invalidate();
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public final void onRequestDisallowInterceptTouchEvent(boolean z) {
    }

    public final void setState(int i) {
        if (i == 2 && this.mState != 2) {
            this.mVerticalThumbDrawable.setState(PRESSED_STATE_SET);
            this.mRecyclerView.removeCallbacks(this.mHideRunnable);
        }
        if (i == 0) {
            this.mRecyclerView.invalidate();
        } else {
            show();
        }
        if (this.mState == 2 && i != 2) {
            this.mVerticalThumbDrawable.setState(EMPTY_STATE_SET);
            this.mRecyclerView.removeCallbacks(this.mHideRunnable);
            this.mRecyclerView.postDelayed(this.mHideRunnable, 1200);
        } else if (i == 1) {
            this.mRecyclerView.removeCallbacks(this.mHideRunnable);
            this.mRecyclerView.postDelayed(this.mHideRunnable, 1500);
        }
        this.mState = i;
    }

    public void hide(int i) {
        int i2 = this.mAnimationState;
        if (i2 == 1) {
            this.mShowHideAnimator.cancel();
        } else if (i2 != 2) {
            return;
        }
        this.mAnimationState = 3;
        ValueAnimator valueAnimator = this.mShowHideAnimator;
        valueAnimator.setFloatValues(((Float) valueAnimator.getAnimatedValue()).floatValue(), 0.0f);
        this.mShowHideAnimator.setDuration(i);
        this.mShowHideAnimator.start();
    }

    public boolean isPointInsideHorizontalThumb(float f, float f2) {
        if (f2 >= this.mRecyclerViewHeight - this.mHorizontalThumbHeight) {
            int i = this.mHorizontalThumbCenterX;
            int i2 = this.mHorizontalThumbWidth;
            if (f >= i - (i2 / 2) && f <= (i2 / 2) + i) {
                return true;
            }
        }
        return false;
    }

    public boolean isPointInsideVerticalThumb(float f, float f2) {
        boolean z;
        RecyclerView recyclerView = this.mRecyclerView;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        if (ViewCompat.Api17Impl.getLayoutDirection(recyclerView) == 1) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            if (f > this.mVerticalThumbWidth) {
                return false;
            }
        } else if (f < this.mRecyclerViewWidth - this.mVerticalThumbWidth) {
            return false;
        }
        int i = this.mVerticalThumbCenterY;
        int i2 = this.mVerticalThumbHeight / 2;
        if (f2 < i - i2 || f2 > i2 + i) {
            return false;
        }
        return true;
    }

    public boolean isVisible() {
        if (this.mState == 1) {
            return true;
        }
        return false;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public final void onDrawOver(Canvas canvas, RecyclerView recyclerView) {
        int i;
        int i2;
        if (this.mRecyclerViewWidth != this.mRecyclerView.getWidth() || this.mRecyclerViewHeight != this.mRecyclerView.getHeight()) {
            this.mRecyclerViewWidth = this.mRecyclerView.getWidth();
            this.mRecyclerViewHeight = this.mRecyclerView.getHeight();
            setState(0);
        } else if (this.mAnimationState != 0) {
            if (this.mNeedVerticalScrollbar) {
                int i3 = this.mRecyclerViewWidth;
                int i4 = this.mVerticalThumbWidth;
                int i5 = i3 - i4;
                int i6 = this.mVerticalThumbCenterY;
                int i7 = this.mVerticalThumbHeight;
                int i8 = i6 - (i7 / 2);
                this.mVerticalThumbDrawable.setBounds(0, 0, i4, i7);
                this.mVerticalTrackDrawable.setBounds(0, 0, this.mVerticalTrackWidth, this.mRecyclerViewHeight);
                RecyclerView recyclerView2 = this.mRecyclerView;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                boolean z = true;
                if (ViewCompat.Api17Impl.getLayoutDirection(recyclerView2) != 1) {
                    z = false;
                }
                if (z) {
                    this.mVerticalTrackDrawable.draw(canvas);
                    canvas.translate(this.mVerticalThumbWidth, i8);
                    canvas.scale(-1.0f, 1.0f);
                    this.mVerticalThumbDrawable.draw(canvas);
                    canvas.scale(-1.0f, 1.0f);
                    canvas.translate(-this.mVerticalThumbWidth, -i8);
                } else {
                    canvas.translate(i5, 0.0f);
                    this.mVerticalTrackDrawable.draw(canvas);
                    canvas.translate(0.0f, i8);
                    this.mVerticalThumbDrawable.draw(canvas);
                    canvas.translate(-i5, -i8);
                }
            }
            if (this.mNeedHorizontalScrollbar) {
                int i9 = this.mRecyclerViewHeight;
                int i10 = this.mHorizontalThumbHeight;
                int i11 = this.mHorizontalThumbCenterX;
                int i12 = this.mHorizontalThumbWidth;
                this.mHorizontalThumbDrawable.setBounds(0, 0, i12, i10);
                this.mHorizontalTrackDrawable.setBounds(0, 0, this.mRecyclerViewWidth, this.mHorizontalTrackHeight);
                canvas.translate(0.0f, i9 - i10);
                this.mHorizontalTrackDrawable.draw(canvas);
                canvas.translate(i11 - (i12 / 2), 0.0f);
                this.mHorizontalThumbDrawable.draw(canvas);
                canvas.translate(-i2, -i);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    public final boolean onInterceptTouchEvent$1(MotionEvent motionEvent) {
        int i = this.mState;
        if (i == 1) {
            boolean isPointInsideVerticalThumb = isPointInsideVerticalThumb(motionEvent.getX(), motionEvent.getY());
            boolean isPointInsideHorizontalThumb = isPointInsideHorizontalThumb(motionEvent.getX(), motionEvent.getY());
            if (motionEvent.getAction() == 0 && (isPointInsideVerticalThumb || isPointInsideHorizontalThumb)) {
                if (isPointInsideHorizontalThumb) {
                    this.mDragState = 1;
                    this.mHorizontalDragX = (int) motionEvent.getX();
                } else if (isPointInsideVerticalThumb) {
                    this.mDragState = 2;
                    this.mVerticalDragY = (int) motionEvent.getY();
                }
                setState(2);
                return true;
            }
        } else if (i == 2) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Code restructure failed: missing block: B:33:0x00bf, code lost:
        if (r8 >= 0) goto L_0x00c1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:46:0x011c, code lost:
        if (r5 >= 0) goto L_0x011e;
     */
    @Override // androidx.recyclerview.widget.RecyclerView.OnItemTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void onTouchEvent(android.view.MotionEvent r12) {
        /*
            Method dump skipped, instructions count: 296
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.FastScroller.onTouchEvent(android.view.MotionEvent):void");
    }

    public final void show() {
        int i = this.mAnimationState;
        if (i != 0) {
            if (i == 3) {
                this.mShowHideAnimator.cancel();
            } else {
                return;
            }
        }
        this.mAnimationState = 1;
        ValueAnimator valueAnimator = this.mShowHideAnimator;
        valueAnimator.setFloatValues(((Float) valueAnimator.getAnimatedValue()).floatValue(), 1.0f);
        this.mShowHideAnimator.setDuration(500L);
        this.mShowHideAnimator.setStartDelay(0L);
        this.mShowHideAnimator.start();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [java.lang.Runnable, androidx.recyclerview.widget.FastScroller$1] */
    /* JADX WARN: Type inference failed for: r2v2, types: [java.lang.Object, androidx.recyclerview.widget.RecyclerView$OnScrollListener, androidx.recyclerview.widget.FastScroller$2] */
    /* JADX WARN: Unknown variable types count: 2 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public FastScroller(androidx.recyclerview.widget.RecyclerView r5, android.graphics.drawable.StateListDrawable r6, android.graphics.drawable.Drawable r7, android.graphics.drawable.StateListDrawable r8, android.graphics.drawable.Drawable r9, int r10, int r11, int r12) {
        /*
            r4 = this;
            r4.<init>()
            r0 = 0
            r4.mRecyclerViewWidth = r0
            r4.mRecyclerViewHeight = r0
            r4.mNeedVerticalScrollbar = r0
            r4.mNeedHorizontalScrollbar = r0
            r4.mState = r0
            r4.mDragState = r0
            r1 = 2
            int[] r2 = new int[r1]
            r4.mVerticalRange = r2
            int[] r2 = new int[r1]
            r4.mHorizontalRange = r2
            float[] r1 = new float[r1]
            r1 = {x00c4: FILL_ARRAY_DATA  , data: [0, 1065353216} // fill-array
            android.animation.ValueAnimator r1 = android.animation.ValueAnimator.ofFloat(r1)
            r4.mShowHideAnimator = r1
            r4.mAnimationState = r0
            androidx.recyclerview.widget.FastScroller$1 r0 = new androidx.recyclerview.widget.FastScroller$1
            r0.<init>()
            r4.mHideRunnable = r0
            androidx.recyclerview.widget.FastScroller$2 r2 = new androidx.recyclerview.widget.FastScroller$2
            r2.<init>()
            r4.mOnScrollListener = r2
            r4.mVerticalThumbDrawable = r6
            r4.mVerticalTrackDrawable = r7
            r4.mHorizontalThumbDrawable = r8
            r4.mHorizontalTrackDrawable = r9
            int r3 = r6.getIntrinsicWidth()
            int r3 = java.lang.Math.max(r10, r3)
            r4.mVerticalThumbWidth = r3
            int r3 = r7.getIntrinsicWidth()
            int r3 = java.lang.Math.max(r10, r3)
            r4.mVerticalTrackWidth = r3
            int r8 = r8.getIntrinsicWidth()
            int r8 = java.lang.Math.max(r10, r8)
            r4.mHorizontalThumbHeight = r8
            int r8 = r9.getIntrinsicWidth()
            int r8 = java.lang.Math.max(r10, r8)
            r4.mHorizontalTrackHeight = r8
            r4.mScrollbarMinimumRange = r11
            r4.mMargin = r12
            r8 = 255(0xff, float:3.57E-43)
            r6.setAlpha(r8)
            r7.setAlpha(r8)
            androidx.recyclerview.widget.FastScroller$AnimatorListener r6 = new androidx.recyclerview.widget.FastScroller$AnimatorListener
            r6.<init>()
            r1.addListener(r6)
            androidx.recyclerview.widget.FastScroller$AnimatorUpdater r6 = new androidx.recyclerview.widget.FastScroller$AnimatorUpdater
            r6.<init>()
            r1.addUpdateListener(r6)
            androidx.recyclerview.widget.RecyclerView r6 = r4.mRecyclerView
            if (r6 != r5) goto L_0x0085
            goto L_0x00c2
        L_0x0085:
            if (r6 == 0) goto L_0x00ac
            r6.removeItemDecoration(r4)
            androidx.recyclerview.widget.RecyclerView r6 = r4.mRecyclerView
            java.util.Objects.requireNonNull(r6)
            java.util.ArrayList<androidx.recyclerview.widget.RecyclerView$OnItemTouchListener> r7 = r6.mOnItemTouchListeners
            r7.remove(r4)
            androidx.recyclerview.widget.RecyclerView$OnItemTouchListener r7 = r6.mInterceptingOnItemTouchListener
            if (r7 != r4) goto L_0x009b
            r7 = 0
            r6.mInterceptingOnItemTouchListener = r7
        L_0x009b:
            androidx.recyclerview.widget.RecyclerView r6 = r4.mRecyclerView
            java.util.Objects.requireNonNull(r6)
            java.util.ArrayList r6 = r6.mScrollListeners
            if (r6 == 0) goto L_0x00a7
            r6.remove(r2)
        L_0x00a7:
            androidx.recyclerview.widget.RecyclerView r6 = r4.mRecyclerView
            r6.removeCallbacks(r0)
        L_0x00ac:
            r4.mRecyclerView = r5
            if (r5 == 0) goto L_0x00c2
            r5.addItemDecoration(r4)
            androidx.recyclerview.widget.RecyclerView r5 = r4.mRecyclerView
            java.util.Objects.requireNonNull(r5)
            java.util.ArrayList<androidx.recyclerview.widget.RecyclerView$OnItemTouchListener> r5 = r5.mOnItemTouchListeners
            r5.add(r4)
            androidx.recyclerview.widget.RecyclerView r4 = r4.mRecyclerView
            r4.addOnScrollListener(r2)
        L_0x00c2:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.recyclerview.widget.FastScroller.<init>(androidx.recyclerview.widget.RecyclerView, android.graphics.drawable.StateListDrawable, android.graphics.drawable.Drawable, android.graphics.drawable.StateListDrawable, android.graphics.drawable.Drawable, int, int, int):void");
    }

    public Drawable getHorizontalThumbDrawable() {
        return this.mHorizontalThumbDrawable;
    }

    public Drawable getHorizontalTrackDrawable() {
        return this.mHorizontalTrackDrawable;
    }

    public Drawable getVerticalThumbDrawable() {
        return this.mVerticalThumbDrawable;
    }

    public Drawable getVerticalTrackDrawable() {
        return this.mVerticalTrackDrawable;
    }
}
