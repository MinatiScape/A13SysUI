package androidx.customview.widget;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.OverScroller;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.Arrays;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public final class ViewDragHelper {
    public static final AnonymousClass1 sInterpolator = new Interpolator() { // from class: androidx.customview.widget.ViewDragHelper.1
        @Override // android.animation.TimeInterpolator
        public final float getInterpolation(float f) {
            float f2 = f - 1.0f;
            return (f2 * f2 * f2 * f2 * f2) + 1.0f;
        }
    };
    public final Callback mCallback;
    public View mCapturedView;
    public final int mDefaultEdgeSize;
    public int mDragState;
    public int[] mEdgeDragsInProgress;
    public int[] mEdgeDragsLocked;
    public int mEdgeSize;
    public int[] mInitialEdgesTouched;
    public float[] mInitialMotionX;
    public float[] mInitialMotionY;
    public float[] mLastMotionX;
    public float[] mLastMotionY;
    public final float mMaxVelocity;
    public float mMinVelocity;
    public final ViewGroup mParentView;
    public int mPointersDown;
    public boolean mReleaseInProgress;
    public final OverScroller mScroller;
    public int mTouchSlop;
    public int mTrackingEdges;
    public VelocityTracker mVelocityTracker;
    public int mActivePointerId = -1;
    public final AnonymousClass2 mSetIdleRunnable = new Runnable() { // from class: androidx.customview.widget.ViewDragHelper.2
        @Override // java.lang.Runnable
        public final void run() {
            ViewDragHelper.this.setDragState(0);
        }
    };

    /* loaded from: classes.dex */
    public static abstract class Callback {
        public abstract int clampViewPositionHorizontal(View view, int i);

        public abstract int clampViewPositionVertical(View view, int i);

        public int getViewHorizontalDragRange(View view) {
            return 0;
        }

        public int getViewVerticalDragRange() {
            return 0;
        }

        public void onEdgeDragStarted(int i, int i2) {
        }

        public void onEdgeTouched() {
        }

        public void onViewCaptured(View view, int i) {
        }

        public abstract void onViewDragStateChanged(int i);

        public abstract void onViewPositionChanged(View view, int i, int i2);

        public abstract void onViewReleased(View view, float f, float f2);

        public abstract boolean tryCaptureView(View view, int i);
    }

    public final void cancel() {
        this.mActivePointerId = -1;
        float[] fArr = this.mInitialMotionX;
        if (fArr != null) {
            Arrays.fill(fArr, 0.0f);
            Arrays.fill(this.mInitialMotionY, 0.0f);
            Arrays.fill(this.mLastMotionX, 0.0f);
            Arrays.fill(this.mLastMotionY, 0.0f);
            Arrays.fill(this.mInitialEdgesTouched, 0);
            Arrays.fill(this.mEdgeDragsInProgress, 0);
            Arrays.fill(this.mEdgeDragsLocked, 0);
            this.mPointersDown = 0;
        }
        VelocityTracker velocityTracker = this.mVelocityTracker;
        if (velocityTracker != null) {
            velocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public final boolean checkTouchSlop(View view, float f, float f2) {
        boolean z;
        boolean z2;
        if (view == null) {
            return false;
        }
        if (this.mCallback.getViewHorizontalDragRange(view) > 0) {
            z = true;
        } else {
            z = false;
        }
        if (this.mCallback.getViewVerticalDragRange() > 0) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z || !z2) {
            return z ? Math.abs(f) > ((float) this.mTouchSlop) : z2 && Math.abs(f2) > ((float) this.mTouchSlop);
        }
        float f3 = f2 * f2;
        int i = this.mTouchSlop;
        return f3 + (f * f) > ((float) (i * i));
    }

    public final void reportNewEdgeDrags(float f, float f2, int i) {
        boolean checkNewEdgeDrag = checkNewEdgeDrag(f, f2, i, 1);
        if (checkNewEdgeDrag(f2, f, i, 4)) {
            checkNewEdgeDrag |= true;
        }
        if (checkNewEdgeDrag(f, f2, i, 2)) {
            boolean z = checkNewEdgeDrag ? 1 : 0;
            char c = checkNewEdgeDrag ? 1 : 0;
            checkNewEdgeDrag = z | true;
        }
        if (checkNewEdgeDrag(f2, f, i, 8)) {
            boolean z2 = checkNewEdgeDrag ? 1 : 0;
            char c2 = checkNewEdgeDrag ? 1 : 0;
            checkNewEdgeDrag = z2 | true;
        }
        if (checkNewEdgeDrag) {
            int[] iArr = this.mEdgeDragsInProgress;
            iArr[i] = iArr[i] | (checkNewEdgeDrag ? 1 : 0);
            Callback callback = this.mCallback;
            int i2 = checkNewEdgeDrag ? 1 : 0;
            int i3 = checkNewEdgeDrag ? 1 : 0;
            int i4 = checkNewEdgeDrag ? 1 : 0;
            callback.onEdgeDragStarted(i2, i);
        }
    }

    public final void clearMotionHistory(int i) {
        float[] fArr = this.mInitialMotionX;
        if (fArr != null) {
            int i2 = this.mPointersDown;
            boolean z = true;
            int i3 = 1 << i;
            if ((i2 & i3) == 0) {
                z = false;
            }
            if (z) {
                fArr[i] = 0.0f;
                this.mInitialMotionY[i] = 0.0f;
                this.mLastMotionX[i] = 0.0f;
                this.mLastMotionY[i] = 0.0f;
                this.mInitialEdgesTouched[i] = 0;
                this.mEdgeDragsInProgress[i] = 0;
                this.mEdgeDragsLocked[i] = 0;
                this.mPointersDown = (~i3) & i2;
            }
        }
    }

    public final int computeAxisDuration(int i, int i2, int i3) {
        int width;
        int i4;
        if (i == 0) {
            return 0;
        }
        float width2 = this.mParentView.getWidth() / 2;
        float sin = (((float) Math.sin((Math.min(1.0f, Math.abs(i) / width) - 0.5f) * 0.47123894f)) * width2) + width2;
        int abs = Math.abs(i2);
        if (abs > 0) {
            i4 = Math.round(Math.abs(sin / abs) * 1000.0f) * 4;
        } else {
            i4 = (int) (((Math.abs(i) / i3) + 1.0f) * 256.0f);
        }
        return Math.min(i4, 600);
    }

    public final boolean continueSettling() {
        if (this.mDragState == 2) {
            boolean computeScrollOffset = this.mScroller.computeScrollOffset();
            int currX = this.mScroller.getCurrX();
            int currY = this.mScroller.getCurrY();
            int left = currX - this.mCapturedView.getLeft();
            int top = currY - this.mCapturedView.getTop();
            if (left != 0) {
                View view = this.mCapturedView;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                view.offsetLeftAndRight(left);
            }
            if (top != 0) {
                View view2 = this.mCapturedView;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                view2.offsetTopAndBottom(top);
            }
            if (!(left == 0 && top == 0)) {
                this.mCallback.onViewPositionChanged(this.mCapturedView, currX, currY);
            }
            if (computeScrollOffset && currX == this.mScroller.getFinalX() && currY == this.mScroller.getFinalY()) {
                this.mScroller.abortAnimation();
                computeScrollOffset = false;
            }
            if (!computeScrollOffset) {
                this.mParentView.post(this.mSetIdleRunnable);
            }
        }
        if (this.mDragState == 2) {
            return true;
        }
        return false;
    }

    public final View findTopChildUnder(int i, int i2) {
        for (int childCount = this.mParentView.getChildCount() - 1; childCount >= 0; childCount--) {
            ViewGroup viewGroup = this.mParentView;
            Objects.requireNonNull(this.mCallback);
            View childAt = viewGroup.getChildAt(childCount);
            if (i >= childAt.getLeft() && i < childAt.getRight() && i2 >= childAt.getTop() && i2 < childAt.getBottom()) {
                return childAt;
            }
        }
        return null;
    }

    /* JADX WARN: Removed duplicated region for block: B:23:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:24:0x0063  */
    /* JADX WARN: Removed duplicated region for block: B:27:0x0068  */
    /* JADX WARN: Removed duplicated region for block: B:28:0x006b  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean forceSettleCapturedViewAt(int r10, int r11, int r12, int r13) {
        /*
            r9 = this;
            android.view.View r0 = r9.mCapturedView
            int r2 = r0.getLeft()
            android.view.View r0 = r9.mCapturedView
            int r3 = r0.getTop()
            int r4 = r10 - r2
            int r5 = r11 - r3
            r10 = 0
            if (r4 != 0) goto L_0x001e
            if (r5 != 0) goto L_0x001e
            android.widget.OverScroller r11 = r9.mScroller
            r11.abortAnimation()
            r9.setDragState(r10)
            return r10
        L_0x001e:
            android.view.View r11 = r9.mCapturedView
            float r0 = r9.mMinVelocity
            int r0 = (int) r0
            float r1 = r9.mMaxVelocity
            int r1 = (int) r1
            int r6 = java.lang.Math.abs(r12)
            if (r6 >= r0) goto L_0x002e
            r12 = r10
            goto L_0x0035
        L_0x002e:
            if (r6 <= r1) goto L_0x0035
            if (r12 <= 0) goto L_0x0034
            r12 = r1
            goto L_0x0035
        L_0x0034:
            int r12 = -r1
        L_0x0035:
            float r0 = r9.mMinVelocity
            int r0 = (int) r0
            float r1 = r9.mMaxVelocity
            int r1 = (int) r1
            int r6 = java.lang.Math.abs(r13)
            if (r6 >= r0) goto L_0x0042
            goto L_0x0049
        L_0x0042:
            if (r6 <= r1) goto L_0x004a
            if (r13 <= 0) goto L_0x0048
            r13 = r1
            goto L_0x004a
        L_0x0048:
            int r10 = -r1
        L_0x0049:
            r13 = r10
        L_0x004a:
            int r10 = java.lang.Math.abs(r4)
            int r0 = java.lang.Math.abs(r5)
            int r1 = java.lang.Math.abs(r12)
            int r6 = java.lang.Math.abs(r13)
            int r7 = r1 + r6
            int r8 = r10 + r0
            if (r12 == 0) goto L_0x0063
            float r10 = (float) r1
            float r1 = (float) r7
            goto L_0x0065
        L_0x0063:
            float r10 = (float) r10
            float r1 = (float) r8
        L_0x0065:
            float r10 = r10 / r1
            if (r13 == 0) goto L_0x006b
            float r0 = (float) r6
            float r1 = (float) r7
            goto L_0x006d
        L_0x006b:
            float r0 = (float) r0
            float r1 = (float) r8
        L_0x006d:
            float r0 = r0 / r1
            androidx.customview.widget.ViewDragHelper$Callback r1 = r9.mCallback
            int r11 = r1.getViewHorizontalDragRange(r11)
            int r11 = r9.computeAxisDuration(r4, r12, r11)
            androidx.customview.widget.ViewDragHelper$Callback r12 = r9.mCallback
            int r12 = r12.getViewVerticalDragRange()
            int r12 = r9.computeAxisDuration(r5, r13, r12)
            float r11 = (float) r11
            float r11 = r11 * r10
            float r10 = (float) r12
            float r10 = r10 * r0
            float r10 = r10 + r11
            int r6 = (int) r10
            android.widget.OverScroller r1 = r9.mScroller
            r1.startScroll(r2, r3, r4, r5, r6)
            r10 = 2
            r9.setDragState(r10)
            r9 = 1
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.customview.widget.ViewDragHelper.forceSettleCapturedViewAt(int, int, int, int):boolean");
    }

    public final void releaseViewForPointerUp() {
        this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaxVelocity);
        float xVelocity = this.mVelocityTracker.getXVelocity(this.mActivePointerId);
        float f = this.mMinVelocity;
        float f2 = this.mMaxVelocity;
        float abs = Math.abs(xVelocity);
        float f3 = 0.0f;
        if (abs < f) {
            xVelocity = 0.0f;
        } else if (abs > f2) {
            if (xVelocity > 0.0f) {
                xVelocity = f2;
            } else {
                xVelocity = -f2;
            }
        }
        float yVelocity = this.mVelocityTracker.getYVelocity(this.mActivePointerId);
        float f4 = this.mMinVelocity;
        float f5 = this.mMaxVelocity;
        float abs2 = Math.abs(yVelocity);
        if (abs2 >= f4) {
            if (abs2 > f5) {
                if (yVelocity > 0.0f) {
                    f3 = f5;
                } else {
                    yVelocity = -f5;
                }
            }
            f3 = yVelocity;
        }
        this.mReleaseInProgress = true;
        this.mCallback.onViewReleased(this.mCapturedView, xVelocity, f3);
        this.mReleaseInProgress = false;
        if (this.mDragState == 1) {
            setDragState(0);
        }
    }

    public final void saveInitialMotion(float f, float f2, int i) {
        float[] fArr = this.mInitialMotionX;
        int i2 = 0;
        if (fArr == null || fArr.length <= i) {
            int i3 = i + 1;
            float[] fArr2 = new float[i3];
            float[] fArr3 = new float[i3];
            float[] fArr4 = new float[i3];
            float[] fArr5 = new float[i3];
            int[] iArr = new int[i3];
            int[] iArr2 = new int[i3];
            int[] iArr3 = new int[i3];
            if (fArr != null) {
                System.arraycopy(fArr, 0, fArr2, 0, fArr.length);
                float[] fArr6 = this.mInitialMotionY;
                System.arraycopy(fArr6, 0, fArr3, 0, fArr6.length);
                float[] fArr7 = this.mLastMotionX;
                System.arraycopy(fArr7, 0, fArr4, 0, fArr7.length);
                float[] fArr8 = this.mLastMotionY;
                System.arraycopy(fArr8, 0, fArr5, 0, fArr8.length);
                int[] iArr4 = this.mInitialEdgesTouched;
                System.arraycopy(iArr4, 0, iArr, 0, iArr4.length);
                int[] iArr5 = this.mEdgeDragsInProgress;
                System.arraycopy(iArr5, 0, iArr2, 0, iArr5.length);
                int[] iArr6 = this.mEdgeDragsLocked;
                System.arraycopy(iArr6, 0, iArr3, 0, iArr6.length);
            }
            this.mInitialMotionX = fArr2;
            this.mInitialMotionY = fArr3;
            this.mLastMotionX = fArr4;
            this.mLastMotionY = fArr5;
            this.mInitialEdgesTouched = iArr;
            this.mEdgeDragsInProgress = iArr2;
            this.mEdgeDragsLocked = iArr3;
        }
        float[] fArr9 = this.mInitialMotionX;
        this.mLastMotionX[i] = f;
        fArr9[i] = f;
        float[] fArr10 = this.mInitialMotionY;
        this.mLastMotionY[i] = f2;
        fArr10[i] = f2;
        int[] iArr7 = this.mInitialEdgesTouched;
        int i4 = (int) f;
        int i5 = (int) f2;
        if (i4 < this.mParentView.getLeft() + this.mEdgeSize) {
            i2 = 1;
        }
        if (i5 < this.mParentView.getTop() + this.mEdgeSize) {
            i2 |= 4;
        }
        if (i4 > this.mParentView.getRight() - this.mEdgeSize) {
            i2 |= 2;
        }
        if (i5 > this.mParentView.getBottom() - this.mEdgeSize) {
            i2 |= 8;
        }
        iArr7[i] = i2;
        this.mPointersDown |= 1 << i;
    }

    public final void setDragState(int i) {
        this.mParentView.removeCallbacks(this.mSetIdleRunnable);
        if (this.mDragState != i) {
            this.mDragState = i;
            this.mCallback.onViewDragStateChanged(i);
            if (this.mDragState == 0) {
                this.mCapturedView = null;
            }
        }
    }

    public final boolean settleCapturedViewAt(int i, int i2) {
        if (this.mReleaseInProgress) {
            return forceSettleCapturedViewAt(i, i2, (int) this.mVelocityTracker.getXVelocity(this.mActivePointerId), (int) this.mVelocityTracker.getYVelocity(this.mActivePointerId));
        }
        throw new IllegalStateException("Cannot settleCapturedViewAt outside of a call to Callback#onViewReleased");
    }

    /* JADX WARN: Code restructure failed: missing block: B:44:0x00da, code lost:
        if (r12 != r11) goto L_0x00e3;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean shouldInterceptTouchEvent(android.view.MotionEvent r17) {
        /*
            Method dump skipped, instructions count: 310
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.customview.widget.ViewDragHelper.shouldInterceptTouchEvent(android.view.MotionEvent):boolean");
    }

    public final boolean smoothSlideViewTo(View view, int i, int i2) {
        this.mCapturedView = view;
        this.mActivePointerId = -1;
        boolean forceSettleCapturedViewAt = forceSettleCapturedViewAt(i, i2, 0, 0);
        if (!forceSettleCapturedViewAt && this.mDragState == 0 && this.mCapturedView != null) {
            this.mCapturedView = null;
        }
        return forceSettleCapturedViewAt;
    }

    public final boolean tryCaptureViewForDrag(View view, int i) {
        if (view == this.mCapturedView && this.mActivePointerId == i) {
            return true;
        }
        if (view == null || !this.mCallback.tryCaptureView(view, i)) {
            return false;
        }
        this.mActivePointerId = i;
        captureChildView(view, i);
        return true;
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [androidx.customview.widget.ViewDragHelper$2] */
    public ViewDragHelper(Context context, ViewGroup viewGroup, Callback callback) {
        Objects.requireNonNull(viewGroup, "Parent view may not be null");
        Objects.requireNonNull(callback, "Callback may not be null");
        this.mParentView = viewGroup;
        this.mCallback = callback;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(context);
        int i = (int) ((context.getResources().getDisplayMetrics().density * 20.0f) + 0.5f);
        this.mDefaultEdgeSize = i;
        this.mEdgeSize = i;
        this.mTouchSlop = viewConfiguration.getScaledTouchSlop();
        this.mMaxVelocity = viewConfiguration.getScaledMaximumFlingVelocity();
        this.mMinVelocity = viewConfiguration.getScaledMinimumFlingVelocity();
        this.mScroller = new OverScroller(context, sInterpolator);
    }

    public final void captureChildView(View view, int i) {
        if (view.getParent() == this.mParentView) {
            this.mCapturedView = view;
            this.mActivePointerId = i;
            this.mCallback.onViewCaptured(view, i);
            setDragState(1);
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("captureChildView: parameter must be a descendant of the ViewDragHelper's tracked parent view (");
        m.append(this.mParentView);
        m.append(")");
        throw new IllegalArgumentException(m.toString());
    }

    public final boolean checkNewEdgeDrag(float f, float f2, int i, int i2) {
        float abs = Math.abs(f);
        float abs2 = Math.abs(f2);
        if ((this.mInitialEdgesTouched[i] & i2) != i2 || (this.mTrackingEdges & i2) == 0 || (this.mEdgeDragsLocked[i] & i2) == i2 || (this.mEdgeDragsInProgress[i] & i2) == i2) {
            return false;
        }
        int i3 = this.mTouchSlop;
        if (abs <= i3 && abs2 <= i3) {
            return false;
        }
        if (abs < abs2 * 0.5f) {
            Objects.requireNonNull(this.mCallback);
        }
        if ((this.mEdgeDragsInProgress[i] & i2) != 0 || abs <= this.mTouchSlop) {
            return false;
        }
        return true;
    }

    public final void processTouchEvent(MotionEvent motionEvent) {
        boolean z;
        int findPointerIndex;
        int i;
        int actionMasked = motionEvent.getActionMasked();
        int actionIndex = motionEvent.getActionIndex();
        if (actionMasked == 0) {
            cancel();
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int i2 = 0;
        if (actionMasked == 0) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            int pointerId = motionEvent.getPointerId(0);
            View findTopChildUnder = findTopChildUnder((int) x, (int) y);
            saveInitialMotion(x, y, pointerId);
            tryCaptureViewForDrag(findTopChildUnder, pointerId);
            if ((this.mInitialEdgesTouched[pointerId] & this.mTrackingEdges) != 0) {
                this.mCallback.onEdgeTouched();
            }
        } else if (actionMasked == 1) {
            if (this.mDragState == 1) {
                releaseViewForPointerUp();
            }
            cancel();
        } else if (actionMasked == 2) {
            if (this.mDragState == 1) {
                int i3 = this.mActivePointerId;
                if ((this.mPointersDown & (1 << i3)) != 0) {
                    i2 = 1;
                }
                if (i2 != 0 && (findPointerIndex = motionEvent.findPointerIndex(i3)) != -1) {
                    float x2 = motionEvent.getX(findPointerIndex);
                    float y2 = motionEvent.getY(findPointerIndex);
                    float[] fArr = this.mLastMotionX;
                    int i4 = this.mActivePointerId;
                    int i5 = (int) (x2 - fArr[i4]);
                    int i6 = (int) (y2 - this.mLastMotionY[i4]);
                    int left = this.mCapturedView.getLeft() + i5;
                    int top = this.mCapturedView.getTop() + i6;
                    int left2 = this.mCapturedView.getLeft();
                    int top2 = this.mCapturedView.getTop();
                    if (i5 != 0) {
                        left = this.mCallback.clampViewPositionHorizontal(this.mCapturedView, left);
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                        this.mCapturedView.offsetLeftAndRight(left - left2);
                    }
                    if (i6 != 0) {
                        top = this.mCallback.clampViewPositionVertical(this.mCapturedView, top);
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                        this.mCapturedView.offsetTopAndBottom(top - top2);
                    }
                    if (!(i5 == 0 && i6 == 0)) {
                        this.mCallback.onViewPositionChanged(this.mCapturedView, left, top);
                    }
                } else {
                    return;
                }
            } else {
                int pointerCount = motionEvent.getPointerCount();
                for (int i7 = 0; i7 < pointerCount; i7++) {
                    int pointerId2 = motionEvent.getPointerId(i7);
                    if ((this.mPointersDown & (1 << pointerId2)) != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (z) {
                        float x3 = motionEvent.getX(i7);
                        float y3 = motionEvent.getY(i7);
                        float f = x3 - this.mInitialMotionX[pointerId2];
                        float f2 = y3 - this.mInitialMotionY[pointerId2];
                        reportNewEdgeDrags(f, f2, pointerId2);
                        if (this.mDragState == 1) {
                            break;
                        }
                        View findTopChildUnder2 = findTopChildUnder((int) x3, (int) y3);
                        if (checkTouchSlop(findTopChildUnder2, f, f2) && tryCaptureViewForDrag(findTopChildUnder2, pointerId2)) {
                            break;
                        }
                    }
                }
            }
            saveLastMotion(motionEvent);
        } else if (actionMasked == 3) {
            if (this.mDragState == 1) {
                this.mReleaseInProgress = true;
                this.mCallback.onViewReleased(this.mCapturedView, 0.0f, 0.0f);
                this.mReleaseInProgress = false;
                if (this.mDragState == 1) {
                    setDragState(0);
                }
            }
            cancel();
        } else if (actionMasked == 5) {
            int pointerId3 = motionEvent.getPointerId(actionIndex);
            float x4 = motionEvent.getX(actionIndex);
            float y4 = motionEvent.getY(actionIndex);
            saveInitialMotion(x4, y4, pointerId3);
            if (this.mDragState == 0) {
                tryCaptureViewForDrag(findTopChildUnder((int) x4, (int) y4), pointerId3);
                if ((this.mInitialEdgesTouched[pointerId3] & this.mTrackingEdges) != 0) {
                    this.mCallback.onEdgeTouched();
                    return;
                }
                return;
            }
            int i8 = (int) x4;
            int i9 = (int) y4;
            View view = this.mCapturedView;
            if (view != null && i8 >= view.getLeft() && i8 < view.getRight() && i9 >= view.getTop() && i9 < view.getBottom()) {
                i2 = 1;
            }
            if (i2 != 0) {
                tryCaptureViewForDrag(this.mCapturedView, pointerId3);
            }
        } else if (actionMasked == 6) {
            int pointerId4 = motionEvent.getPointerId(actionIndex);
            if (this.mDragState == 1 && pointerId4 == this.mActivePointerId) {
                int pointerCount2 = motionEvent.getPointerCount();
                while (true) {
                    if (i2 >= pointerCount2) {
                        i = -1;
                        break;
                    }
                    int pointerId5 = motionEvent.getPointerId(i2);
                    if (pointerId5 != this.mActivePointerId) {
                        View findTopChildUnder3 = findTopChildUnder((int) motionEvent.getX(i2), (int) motionEvent.getY(i2));
                        View view2 = this.mCapturedView;
                        if (findTopChildUnder3 == view2 && tryCaptureViewForDrag(view2, pointerId5)) {
                            i = this.mActivePointerId;
                            break;
                        }
                    }
                    i2++;
                }
                if (i == -1) {
                    releaseViewForPointerUp();
                }
            }
            clearMotionHistory(pointerId4);
        }
    }

    public final void saveLastMotion(MotionEvent motionEvent) {
        int pointerCount = motionEvent.getPointerCount();
        for (int i = 0; i < pointerCount; i++) {
            int pointerId = motionEvent.getPointerId(i);
            boolean z = true;
            if ((this.mPointersDown & (1 << pointerId)) == 0) {
                z = false;
            }
            if (z) {
                float x = motionEvent.getX(i);
                float y = motionEvent.getY(i);
                this.mLastMotionX[pointerId] = x;
                this.mLastMotionY[pointerId] = y;
            }
        }
    }
}
