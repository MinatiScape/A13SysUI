package androidx.core.widget;

import android.content.res.Resources;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnimationUtils;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public abstract class AutoScrollHelper implements View.OnTouchListener {
    public static final int DEFAULT_ACTIVATION_DELAY = ViewConfiguration.getTapTimeout();
    public boolean mAlreadyDelayed;
    public boolean mAnimating;
    public boolean mEnabled;
    public boolean mNeedsCancel;
    public boolean mNeedsReset;
    public ScrollAnimationRunnable mRunnable;
    public final ClampedScroller mScroller;
    public final View mTarget;
    public final AccelerateInterpolator mEdgeInterpolator = new AccelerateInterpolator();
    public float[] mRelativeEdges = {0.0f, 0.0f};
    public float[] mMaximumEdges = {Float.MAX_VALUE, Float.MAX_VALUE};
    public float[] mRelativeVelocity = {0.0f, 0.0f};
    public float[] mMinimumVelocity = {0.0f, 0.0f};
    public float[] mMaximumVelocity = {Float.MAX_VALUE, Float.MAX_VALUE};
    public int mEdgeType = 1;
    public int mActivationDelay = DEFAULT_ACTIVATION_DELAY;

    /* loaded from: classes.dex */
    public static class ClampedScroller {
        public int mEffectiveRampDown;
        public int mRampDownDuration;
        public int mRampUpDuration;
        public float mStopValue;
        public float mTargetVelocityX;
        public float mTargetVelocityY;
        public long mStartTime = Long.MIN_VALUE;
        public long mStopTime = -1;
        public long mDeltaTime = 0;

        public final float getValueAt(long j) {
            long j2 = this.mStartTime;
            if (j < j2) {
                return 0.0f;
            }
            long j3 = this.mStopTime;
            if (j3 < 0 || j < j3) {
                return AutoScrollHelper.constrain(((float) (j - j2)) / this.mRampUpDuration, 0.0f, 1.0f) * 0.5f;
            }
            float f = this.mStopValue;
            return (AutoScrollHelper.constrain(((float) (j - j3)) / this.mEffectiveRampDown, 0.0f, 1.0f) * f) + (1.0f - f);
        }
    }

    /* loaded from: classes.dex */
    public class ScrollAnimationRunnable implements Runnable {
        public ScrollAnimationRunnable() {
        }

        @Override // java.lang.Runnable
        public final void run() {
            boolean z;
            AutoScrollHelper autoScrollHelper = AutoScrollHelper.this;
            if (autoScrollHelper.mAnimating) {
                if (autoScrollHelper.mNeedsReset) {
                    autoScrollHelper.mNeedsReset = false;
                    ClampedScroller clampedScroller = autoScrollHelper.mScroller;
                    Objects.requireNonNull(clampedScroller);
                    long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
                    clampedScroller.mStartTime = currentAnimationTimeMillis;
                    clampedScroller.mStopTime = -1L;
                    clampedScroller.mDeltaTime = currentAnimationTimeMillis;
                    clampedScroller.mStopValue = 0.5f;
                }
                ClampedScroller clampedScroller2 = AutoScrollHelper.this.mScroller;
                Objects.requireNonNull(clampedScroller2);
                if (clampedScroller2.mStopTime <= 0 || AnimationUtils.currentAnimationTimeMillis() <= clampedScroller2.mStopTime + clampedScroller2.mEffectiveRampDown) {
                    z = false;
                } else {
                    z = true;
                }
                if (z || !AutoScrollHelper.this.shouldAnimate()) {
                    AutoScrollHelper.this.mAnimating = false;
                    return;
                }
                AutoScrollHelper autoScrollHelper2 = AutoScrollHelper.this;
                if (autoScrollHelper2.mNeedsCancel) {
                    autoScrollHelper2.mNeedsCancel = false;
                    long uptimeMillis = SystemClock.uptimeMillis();
                    MotionEvent obtain = MotionEvent.obtain(uptimeMillis, uptimeMillis, 3, 0.0f, 0.0f, 0);
                    autoScrollHelper2.mTarget.onTouchEvent(obtain);
                    obtain.recycle();
                }
                if (clampedScroller2.mDeltaTime != 0) {
                    long currentAnimationTimeMillis2 = AnimationUtils.currentAnimationTimeMillis();
                    float valueAt = clampedScroller2.getValueAt(currentAnimationTimeMillis2);
                    clampedScroller2.mDeltaTime = currentAnimationTimeMillis2;
                    int i = (int) (((float) (currentAnimationTimeMillis2 - clampedScroller2.mDeltaTime)) * ((valueAt * 4.0f) + ((-4.0f) * valueAt * valueAt)) * clampedScroller2.mTargetVelocityY);
                    ListViewAutoScrollHelper listViewAutoScrollHelper = (ListViewAutoScrollHelper) AutoScrollHelper.this;
                    Objects.requireNonNull(listViewAutoScrollHelper);
                    listViewAutoScrollHelper.mTarget.scrollListBy(i);
                    View view = AutoScrollHelper.this.mTarget;
                    WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                    ViewCompat.Api16Impl.postOnAnimation(view, this);
                    return;
                }
                throw new RuntimeException("Cannot compute scroll delta before calling start()");
            }
        }
    }

    public static float constrain(float f, float f2, float f3) {
        return f > f3 ? f3 : f < f2 ? f2 : f;
    }

    public final float constrainEdgeValue(float f, float f2) {
        if (f2 == 0.0f) {
            return 0.0f;
        }
        int i = this.mEdgeType;
        if (i == 0 || i == 1) {
            if (f < f2) {
                if (f >= 0.0f) {
                    return 1.0f - (f / f2);
                }
                if (this.mAnimating && i == 1) {
                    return 1.0f;
                }
            }
        } else if (i == 2 && f < 0.0f) {
            return f / (-f2);
        }
        return 0.0f;
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x003d A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:13:0x003e  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final float computeTargetVelocity(int r4, float r5, float r6, float r7) {
        /*
            r3 = this;
            float[] r0 = r3.mRelativeEdges
            r0 = r0[r4]
            float[] r1 = r3.mMaximumEdges
            r1 = r1[r4]
            float r0 = r0 * r6
            r2 = 0
            float r0 = constrain(r0, r2, r1)
            float r1 = r3.constrainEdgeValue(r5, r0)
            float r6 = r6 - r5
            float r5 = r3.constrainEdgeValue(r6, r0)
            float r5 = r5 - r1
            int r6 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
            if (r6 >= 0) goto L_0x0025
            android.view.animation.AccelerateInterpolator r6 = r3.mEdgeInterpolator
            float r5 = -r5
            float r5 = r6.getInterpolation(r5)
            float r5 = -r5
            goto L_0x002f
        L_0x0025:
            int r6 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
            if (r6 <= 0) goto L_0x0038
            android.view.animation.AccelerateInterpolator r6 = r3.mEdgeInterpolator
            float r5 = r6.getInterpolation(r5)
        L_0x002f:
            r6 = -1082130432(0xffffffffbf800000, float:-1.0)
            r0 = 1065353216(0x3f800000, float:1.0)
            float r5 = constrain(r5, r6, r0)
            goto L_0x0039
        L_0x0038:
            r5 = r2
        L_0x0039:
            int r6 = (r5 > r2 ? 1 : (r5 == r2 ? 0 : -1))
            if (r6 != 0) goto L_0x003e
            return r2
        L_0x003e:
            float[] r0 = r3.mRelativeVelocity
            r0 = r0[r4]
            float[] r1 = r3.mMinimumVelocity
            r1 = r1[r4]
            float[] r3 = r3.mMaximumVelocity
            r3 = r3[r4]
            float r0 = r0 * r7
            if (r6 <= 0) goto L_0x0053
            float r5 = r5 * r0
            float r3 = constrain(r5, r1, r3)
            return r3
        L_0x0053:
            float r4 = -r5
            float r4 = r4 * r0
            float r3 = constrain(r4, r1, r3)
            float r3 = -r3
            return r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.widget.AutoScrollHelper.computeTargetVelocity(int, float, float, float):float");
    }

    /* JADX WARN: Code restructure failed: missing block: B:11:0x0013, code lost:
        if (r0 != 3) goto L_0x0082;
     */
    @Override // android.view.View.OnTouchListener
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean onTouch(android.view.View r6, android.view.MotionEvent r7) {
        /*
            r5 = this;
            boolean r0 = r5.mEnabled
            r1 = 0
            if (r0 != 0) goto L_0x0006
            return r1
        L_0x0006:
            int r0 = r7.getActionMasked()
            r2 = 1
            if (r0 == 0) goto L_0x001a
            if (r0 == r2) goto L_0x0016
            r3 = 2
            if (r0 == r3) goto L_0x001e
            r6 = 3
            if (r0 == r6) goto L_0x0016
            goto L_0x0082
        L_0x0016:
            r5.requestStop()
            goto L_0x0082
        L_0x001a:
            r5.mNeedsCancel = r2
            r5.mAlreadyDelayed = r1
        L_0x001e:
            float r0 = r7.getX()
            int r3 = r6.getWidth()
            float r3 = (float) r3
            android.view.View r4 = r5.mTarget
            int r4 = r4.getWidth()
            float r4 = (float) r4
            float r0 = r5.computeTargetVelocity(r1, r0, r3, r4)
            float r7 = r7.getY()
            int r6 = r6.getHeight()
            float r6 = (float) r6
            android.view.View r3 = r5.mTarget
            int r3 = r3.getHeight()
            float r3 = (float) r3
            float r6 = r5.computeTargetVelocity(r2, r7, r6, r3)
            androidx.core.widget.AutoScrollHelper$ClampedScroller r7 = r5.mScroller
            java.util.Objects.requireNonNull(r7)
            r7.mTargetVelocityX = r0
            r7.mTargetVelocityY = r6
            boolean r6 = r5.mAnimating
            if (r6 != 0) goto L_0x0082
            boolean r6 = r5.shouldAnimate()
            if (r6 == 0) goto L_0x0082
            androidx.core.widget.AutoScrollHelper$ScrollAnimationRunnable r6 = r5.mRunnable
            if (r6 != 0) goto L_0x0064
            androidx.core.widget.AutoScrollHelper$ScrollAnimationRunnable r6 = new androidx.core.widget.AutoScrollHelper$ScrollAnimationRunnable
            r6.<init>()
            r5.mRunnable = r6
        L_0x0064:
            r5.mAnimating = r2
            r5.mNeedsReset = r2
            boolean r6 = r5.mAlreadyDelayed
            if (r6 != 0) goto L_0x007b
            int r6 = r5.mActivationDelay
            if (r6 <= 0) goto L_0x007b
            android.view.View r7 = r5.mTarget
            androidx.core.widget.AutoScrollHelper$ScrollAnimationRunnable r0 = r5.mRunnable
            long r3 = (long) r6
            java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r6 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
            androidx.core.view.ViewCompat.Api16Impl.postOnAnimationDelayed(r7, r0, r3)
            goto L_0x0080
        L_0x007b:
            androidx.core.widget.AutoScrollHelper$ScrollAnimationRunnable r6 = r5.mRunnable
            r6.run()
        L_0x0080:
            r5.mAlreadyDelayed = r2
        L_0x0082:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.widget.AutoScrollHelper.onTouch(android.view.View, android.view.MotionEvent):boolean");
    }

    public final void requestStop() {
        int i = 0;
        if (this.mNeedsReset) {
            this.mAnimating = false;
            return;
        }
        ClampedScroller clampedScroller = this.mScroller;
        Objects.requireNonNull(clampedScroller);
        long currentAnimationTimeMillis = AnimationUtils.currentAnimationTimeMillis();
        int i2 = (int) (currentAnimationTimeMillis - clampedScroller.mStartTime);
        int i3 = clampedScroller.mRampDownDuration;
        if (i2 > i3) {
            i = i3;
        } else if (i2 >= 0) {
            i = i2;
        }
        clampedScroller.mEffectiveRampDown = i;
        clampedScroller.mStopValue = clampedScroller.getValueAt(currentAnimationTimeMillis);
        clampedScroller.mStopTime = currentAnimationTimeMillis;
    }

    /* JADX WARN: Removed duplicated region for block: B:20:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean shouldAnimate() {
        /*
            r8 = this;
            androidx.core.widget.AutoScrollHelper$ClampedScroller r0 = r8.mScroller
            java.util.Objects.requireNonNull(r0)
            float r1 = r0.mTargetVelocityY
            float r2 = java.lang.Math.abs(r1)
            float r1 = r1 / r2
            int r1 = (int) r1
            float r0 = r0.mTargetVelocityX
            float r2 = java.lang.Math.abs(r0)
            float r0 = r0 / r2
            int r0 = (int) r0
            r2 = 1
            r3 = 0
            if (r1 == 0) goto L_0x0055
            androidx.core.widget.ListViewAutoScrollHelper r8 = (androidx.core.widget.ListViewAutoScrollHelper) r8
            android.widget.ListView r8 = r8.mTarget
            int r4 = r8.getCount()
            if (r4 != 0) goto L_0x0025
        L_0x0023:
            r8 = r3
            goto L_0x0053
        L_0x0025:
            int r5 = r8.getChildCount()
            int r6 = r8.getFirstVisiblePosition()
            int r7 = r6 + r5
            if (r1 <= 0) goto L_0x0043
            if (r7 < r4) goto L_0x0052
            int r5 = r5 - r2
            android.view.View r1 = r8.getChildAt(r5)
            int r1 = r1.getBottom()
            int r8 = r8.getHeight()
            if (r1 > r8) goto L_0x0052
            goto L_0x0023
        L_0x0043:
            if (r1 >= 0) goto L_0x0023
            if (r6 > 0) goto L_0x0052
            android.view.View r8 = r8.getChildAt(r3)
            int r8 = r8.getTop()
            if (r8 < 0) goto L_0x0052
            goto L_0x0023
        L_0x0052:
            r8 = r2
        L_0x0053:
            if (r8 != 0) goto L_0x0056
        L_0x0055:
            r2 = r3
        L_0x0056:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.widget.AutoScrollHelper.shouldAnimate():boolean");
    }

    public AutoScrollHelper(View view) {
        ClampedScroller clampedScroller = new ClampedScroller();
        this.mScroller = clampedScroller;
        this.mTarget = view;
        float f = Resources.getSystem().getDisplayMetrics().density;
        float[] fArr = this.mMaximumVelocity;
        float f2 = ((int) ((1575.0f * f) + 0.5f)) / 1000.0f;
        fArr[0] = f2;
        fArr[1] = f2;
        float[] fArr2 = this.mMinimumVelocity;
        float f3 = ((int) ((f * 315.0f) + 0.5f)) / 1000.0f;
        fArr2[0] = f3;
        fArr2[1] = f3;
        float[] fArr3 = this.mMaximumEdges;
        fArr3[0] = Float.MAX_VALUE;
        fArr3[1] = Float.MAX_VALUE;
        float[] fArr4 = this.mRelativeEdges;
        fArr4[0] = 0.2f;
        fArr4[1] = 0.2f;
        float[] fArr5 = this.mRelativeVelocity;
        fArr5[0] = 0.001f;
        fArr5[1] = 0.001f;
        clampedScroller.mRampUpDuration = 500;
        clampedScroller.mRampDownDuration = 500;
    }
}
