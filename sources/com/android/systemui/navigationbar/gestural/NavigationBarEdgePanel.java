package com.android.systemui.navigationbar.gestural;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.util.MathUtils;
import android.view.ContextThemeWrapper;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.PathInterpolator;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import androidx.core.graphics.ColorUtils;
import androidx.dynamicanimation.animation.DynamicAnimation;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import androidx.dynamicanimation.animation.SpringAnimation;
import androidx.dynamicanimation.animation.SpringForce;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline0;
import com.android.keyguard.KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0;
import com.android.keyguard.LockIconView$$ExternalSyntheticOutline0;
import com.android.settingslib.Utils;
import com.android.systemui.Dependency;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.NavigationEdgeBackPlugin;
import com.android.systemui.qs.QSAnimator$$ExternalSyntheticLambda0;
import com.android.systemui.shared.navigationbar.RegionSamplingHelper;
import com.android.systemui.statusbar.VibratorHelper;
import com.android.wifitrackerlib.BaseWifiTracker$$ExternalSyntheticLambda1;
import com.android.wm.shell.back.BackAnimation;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public final class NavigationBarEdgePanel extends View implements NavigationEdgeBackPlugin {
    public final SpringAnimation mAngleAnimation;
    public final SpringForce mAngleAppearForce;
    public final SpringForce mAngleDisappearForce;
    public float mAngleOffset;
    public int mArrowColor;
    public final ValueAnimator mArrowColorAnimator;
    public int mArrowColorDark;
    public int mArrowColorLight;
    public final ValueAnimator mArrowDisappearAnimation;
    public final float mArrowLength;
    public int mArrowPaddingEnd;
    public int mArrowStartColor;
    public final float mArrowThickness;
    public boolean mArrowsPointLeft;
    public final BackAnimation mBackAnimation;
    public NavigationEdgeBackPlugin.BackCallback mBackCallback;
    public final float mBaseTranslation;
    public float mCurrentAngle;
    public int mCurrentArrowColor;
    public float mCurrentTranslation;
    public final float mDensity;
    public float mDesiredAngle;
    public float mDesiredTranslation;
    public float mDesiredVerticalTranslation;
    public float mDisappearAmount;
    public boolean mDragSlopPassed;
    public int mFingerOffset;
    public boolean mIsLeftPanel;
    public WindowManager.LayoutParams mLayoutParams;
    public int mLeftInset;
    public float mMaxTranslation;
    public int mMinArrowPosition;
    public final float mMinDeltaForSwitch;
    public final Paint mPaint;
    public float mPreviousTouchTranslation;
    public int mProtectionColorDark;
    public int mProtectionColorLight;
    public final Paint mProtectionPaint;
    public RegionSamplingHelper mRegionSamplingHelper;
    public final SpringForce mRegularTranslationSpring;
    public int mRightInset;
    public int mScreenSize;
    public boolean mShowProtection;
    public float mStartX;
    public float mStartY;
    public final float mSwipeTriggerThreshold;
    public float mTotalTouchDelta;
    public final SpringAnimation mTranslationAnimation;
    public boolean mTriggerBack;
    public final SpringForce mTriggerBackSpring;
    public VelocityTracker mVelocityTracker;
    public float mVerticalTranslation;
    public final SpringAnimation mVerticalTranslationAnimation;
    public long mVibrationTime;
    public final WindowManager mWindowManager;
    public static final PathInterpolator RUBBER_BAND_INTERPOLATOR = new PathInterpolator(0.2f, 1.0f, 1.0f, 1.0f);
    public static final PathInterpolator RUBBER_BAND_INTERPOLATOR_APPEAR = new PathInterpolator(0.25f, 1.0f, 1.0f, 1.0f);
    public static final AnonymousClass2 CURRENT_ANGLE = new FloatPropertyCompat<NavigationBarEdgePanel>() { // from class: com.android.systemui.navigationbar.gestural.NavigationBarEdgePanel.2
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(NavigationBarEdgePanel navigationBarEdgePanel) {
            NavigationBarEdgePanel navigationBarEdgePanel2 = navigationBarEdgePanel;
            Objects.requireNonNull(navigationBarEdgePanel2);
            return navigationBarEdgePanel2.mCurrentAngle;
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(NavigationBarEdgePanel navigationBarEdgePanel, float f) {
            NavigationBarEdgePanel navigationBarEdgePanel2 = navigationBarEdgePanel;
            Objects.requireNonNull(navigationBarEdgePanel2);
            navigationBarEdgePanel2.mCurrentAngle = f;
            navigationBarEdgePanel2.invalidate();
        }
    };
    public static final AnonymousClass3 CURRENT_TRANSLATION = new FloatPropertyCompat<NavigationBarEdgePanel>() { // from class: com.android.systemui.navigationbar.gestural.NavigationBarEdgePanel.3
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(NavigationBarEdgePanel navigationBarEdgePanel) {
            NavigationBarEdgePanel navigationBarEdgePanel2 = navigationBarEdgePanel;
            Objects.requireNonNull(navigationBarEdgePanel2);
            return navigationBarEdgePanel2.mCurrentTranslation;
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(NavigationBarEdgePanel navigationBarEdgePanel, float f) {
            NavigationBarEdgePanel navigationBarEdgePanel2 = navigationBarEdgePanel;
            Objects.requireNonNull(navigationBarEdgePanel2);
            navigationBarEdgePanel2.mCurrentTranslation = f;
            navigationBarEdgePanel2.invalidate();
        }
    };
    public static final AnonymousClass4 CURRENT_VERTICAL_TRANSLATION = new FloatPropertyCompat<NavigationBarEdgePanel>() { // from class: com.android.systemui.navigationbar.gestural.NavigationBarEdgePanel.4
        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final float getValue(NavigationBarEdgePanel navigationBarEdgePanel) {
            NavigationBarEdgePanel navigationBarEdgePanel2 = navigationBarEdgePanel;
            Objects.requireNonNull(navigationBarEdgePanel2);
            return navigationBarEdgePanel2.mVerticalTranslation;
        }

        @Override // androidx.dynamicanimation.animation.FloatPropertyCompat
        public final void setValue(NavigationBarEdgePanel navigationBarEdgePanel, float f) {
            NavigationBarEdgePanel navigationBarEdgePanel2 = navigationBarEdgePanel;
            Objects.requireNonNull(navigationBarEdgePanel2);
            navigationBarEdgePanel2.mVerticalTranslation = f;
            navigationBarEdgePanel2.invalidate();
        }
    };
    public final Path mArrowPath = new Path();
    public final Point mDisplaySize = new Point();
    public boolean mIsDark = false;
    public final Rect mSamplingRect = new Rect();
    public final Handler mHandler = new Handler();
    public final QSAnimator$$ExternalSyntheticLambda0 mFailsafeRunnable = new QSAnimator$$ExternalSyntheticLambda0(this, 1);
    public AnonymousClass1 mSetGoneEndListener = new DynamicAnimation.OnAnimationEndListener() { // from class: com.android.systemui.navigationbar.gestural.NavigationBarEdgePanel.1
        @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
        public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z, float f, float f2) {
            Objects.requireNonNull(dynamicAnimation);
            ArrayList<DynamicAnimation.OnAnimationEndListener> arrayList = dynamicAnimation.mEndListeners;
            int indexOf = arrayList.indexOf(this);
            if (indexOf >= 0) {
                arrayList.set(indexOf, null);
            }
            if (!z) {
                NavigationBarEdgePanel.this.setVisibility(8);
            }
        }
    };
    public final VibratorHelper mVibratorHelper = (VibratorHelper) Dependency.get(VibratorHelper.class);

    @Override // android.view.View
    public final boolean hasOverlappingRendering() {
        return false;
    }

    public final void cancelBack() {
        this.mBackCallback.cancelBack();
        SpringAnimation springAnimation = this.mTranslationAnimation;
        Objects.requireNonNull(springAnimation);
        if (springAnimation.mRunning) {
            this.mTranslationAnimation.addEndListener(this.mSetGoneEndListener);
            this.mHandler.removeCallbacks(this.mFailsafeRunnable);
            this.mHandler.postDelayed(this.mFailsafeRunnable, 200L);
            return;
        }
        setVisibility(8);
    }

    @Override // com.android.systemui.plugins.NavigationEdgeBackPlugin
    public final void dump(PrintWriter printWriter) {
        StringBuilder m = KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(KeyguardUpdateMonitor$$ExternalSyntheticOutline0.m(LockIconView$$ExternalSyntheticOutline0.m(printWriter, "NavigationBarEdgePanel:", "  mIsLeftPanel="), this.mIsLeftPanel, printWriter, "  mTriggerBack="), this.mTriggerBack, printWriter, "  mDragSlopPassed="), this.mDragSlopPassed, printWriter, "  mCurrentAngle=");
        m.append(this.mCurrentAngle);
        printWriter.println(m.toString());
        printWriter.println("  mDesiredAngle=" + this.mDesiredAngle);
        printWriter.println("  mCurrentTranslation=" + this.mCurrentTranslation);
        printWriter.println("  mDesiredTranslation=" + this.mDesiredTranslation);
        StringBuilder sb = new StringBuilder();
        sb.append("  mTranslationAnimation running=");
        SpringAnimation springAnimation = this.mTranslationAnimation;
        Objects.requireNonNull(springAnimation);
        sb.append(springAnimation.mRunning);
        printWriter.println(sb.toString());
        this.mRegionSamplingHelper.dump(printWriter);
    }

    @Override // com.android.systemui.plugins.Plugin
    public final void onDestroy() {
        this.mHandler.removeCallbacks(this.mFailsafeRunnable);
        this.mWindowManager.removeView(this);
        this.mRegionSamplingHelper.stop();
        this.mRegionSamplingHelper = null;
    }

    @Override // android.view.View
    public final void onDraw(Canvas canvas) {
        float f = this.mCurrentTranslation - (this.mArrowThickness / 2.0f);
        canvas.save();
        if (!this.mIsLeftPanel) {
            f = getWidth() - f;
        }
        canvas.translate(f, (getHeight() * 0.5f) + this.mVerticalTranslation);
        float cos = ((float) Math.cos(Math.toRadians(this.mCurrentAngle))) * this.mArrowLength;
        float sin = ((float) Math.sin(Math.toRadians(this.mCurrentAngle))) * this.mArrowLength;
        if (!this.mArrowsPointLeft) {
            cos = -cos;
        }
        float lerp = MathUtils.lerp(1.0f, 0.75f, this.mDisappearAmount);
        float f2 = cos * lerp;
        float f3 = sin * lerp;
        this.mArrowPath.reset();
        this.mArrowPath.moveTo(f2, f3);
        this.mArrowPath.lineTo(0.0f, 0.0f);
        this.mArrowPath.lineTo(f2, -f3);
        Path path = this.mArrowPath;
        if (this.mShowProtection) {
            canvas.drawPath(path, this.mProtectionPaint);
        }
        canvas.drawPath(path, this.mPaint);
        canvas.restore();
    }

    @Override // com.android.systemui.plugins.NavigationEdgeBackPlugin
    public final void onMotionEvent(MotionEvent motionEvent) {
        WindowManager.LayoutParams layoutParams;
        Object[] objArr;
        float f;
        BackAnimation backAnimation = this.mBackAnimation;
        if (backAnimation != null) {
            backAnimation.onBackMotion(motionEvent, !this.mIsLeftPanel ? 1 : 0);
        }
        if (this.mVelocityTracker == null) {
            this.mVelocityTracker = VelocityTracker.obtain();
        }
        this.mVelocityTracker.addMovement(motionEvent);
        int actionMasked = motionEvent.getActionMasked();
        boolean z = false;
        if (actionMasked == 0) {
            this.mDragSlopPassed = false;
            animate().cancel();
            this.mAngleAnimation.cancel();
            this.mTranslationAnimation.cancel();
            this.mVerticalTranslationAnimation.cancel();
            this.mArrowDisappearAnimation.cancel();
            this.mAngleOffset = 0.0f;
            SpringAnimation springAnimation = this.mTranslationAnimation;
            SpringForce springForce = this.mRegularTranslationSpring;
            Objects.requireNonNull(springAnimation);
            springAnimation.mSpring = springForce;
            Log.d("NoBackGesture", "reset mTriggerBack=false");
            setTriggerBack(false, false);
            setDesiredTranslation(0.0f, false);
            this.mCurrentTranslation = 0.0f;
            invalidate();
            updateAngle(false);
            this.mPreviousTouchTranslation = 0.0f;
            this.mTotalTouchDelta = 0.0f;
            this.mVibrationTime = 0L;
            if (this.mDesiredVerticalTranslation != 0.0f) {
                this.mDesiredVerticalTranslation = 0.0f;
                this.mVerticalTranslation = 0.0f;
                invalidate();
                invalidate();
            }
            this.mHandler.removeCallbacks(this.mFailsafeRunnable);
            this.mStartX = motionEvent.getX();
            this.mStartY = motionEvent.getY();
            setVisibility(0);
            float max = Math.max(motionEvent.getY() - this.mFingerOffset, this.mMinArrowPosition);
            this.mLayoutParams.y = MathUtils.constrain((int) (max - (layoutParams.height / 2.0f)), 0, this.mDisplaySize.y);
            updateSamplingRect();
            this.mRegionSamplingHelper.start(this.mSamplingRect);
            this.mWindowManager.updateViewLayout(this, this.mLayoutParams);
        } else if (actionMasked == 1) {
            KeyguardUpdateMonitor$18$$ExternalSyntheticOutline0.m(VendorAtomValue$$ExternalSyntheticOutline1.m("NavigationBarEdgePanel ACTION_UP, mTriggerBack="), this.mTriggerBack, "NoBackGesture");
            if (this.mTriggerBack) {
                this.mBackCallback.triggerBack();
                if (this.mVelocityTracker == null) {
                    this.mVelocityTracker = VelocityTracker.obtain();
                }
                this.mVelocityTracker.computeCurrentVelocity(1000);
                if (Math.abs(this.mVelocityTracker.getXVelocity()) < 500.0f) {
                    objArr = 1;
                } else {
                    objArr = null;
                }
                if (objArr != null || SystemClock.uptimeMillis() - this.mVibrationTime >= 400) {
                    this.mVibratorHelper.vibrate(0);
                }
                float f2 = this.mAngleOffset;
                if (f2 > -4.0f) {
                    this.mAngleOffset = Math.max(-8.0f, f2 - 8.0f);
                    updateAngle(true);
                }
                final BaseWifiTracker$$ExternalSyntheticLambda1 baseWifiTracker$$ExternalSyntheticLambda1 = new BaseWifiTracker$$ExternalSyntheticLambda1(this, 3);
                SpringAnimation springAnimation2 = this.mTranslationAnimation;
                Objects.requireNonNull(springAnimation2);
                if (springAnimation2.mRunning) {
                    this.mTranslationAnimation.addEndListener(new DynamicAnimation.OnAnimationEndListener() { // from class: com.android.systemui.navigationbar.gestural.NavigationBarEdgePanel.6
                        @Override // androidx.dynamicanimation.animation.DynamicAnimation.OnAnimationEndListener
                        public final void onAnimationEnd(DynamicAnimation dynamicAnimation, boolean z2, float f3, float f4) {
                            Objects.requireNonNull(dynamicAnimation);
                            ArrayList<DynamicAnimation.OnAnimationEndListener> arrayList = dynamicAnimation.mEndListeners;
                            int indexOf = arrayList.indexOf(this);
                            if (indexOf >= 0) {
                                arrayList.set(indexOf, null);
                            }
                            if (!z2) {
                                baseWifiTracker$$ExternalSyntheticLambda1.run();
                            }
                        }
                    });
                    this.mHandler.removeCallbacks(this.mFailsafeRunnable);
                    this.mHandler.postDelayed(this.mFailsafeRunnable, 200L);
                } else {
                    baseWifiTracker$$ExternalSyntheticLambda1.run();
                }
            } else {
                cancelBack();
            }
            this.mRegionSamplingHelper.stop();
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        } else if (actionMasked == 2) {
            float x = motionEvent.getX();
            float y = motionEvent.getY();
            float abs = MathUtils.abs(x - this.mStartX);
            float f3 = y - this.mStartY;
            float f4 = abs - this.mPreviousTouchTranslation;
            if (Math.abs(f4) > 0.0f) {
                if (Math.signum(f4) == Math.signum(this.mTotalTouchDelta)) {
                    this.mTotalTouchDelta += f4;
                } else {
                    this.mTotalTouchDelta = f4;
                }
            }
            this.mPreviousTouchTranslation = abs;
            if (!this.mDragSlopPassed && abs > this.mSwipeTriggerThreshold) {
                this.mDragSlopPassed = true;
                this.mVibratorHelper.vibrate(2);
                this.mVibrationTime = SystemClock.uptimeMillis();
                this.mDisappearAmount = 0.0f;
                setAlpha(1.0f);
                Log.d("NoBackGesture", "set mTriggerBack=true");
                setTriggerBack(true, true);
            }
            float f5 = this.mBaseTranslation;
            if (abs > f5) {
                float interpolation = RUBBER_BAND_INTERPOLATOR.getInterpolation(MathUtils.saturate((abs - f5) / (this.mScreenSize - f5)));
                float f6 = this.mMaxTranslation;
                float f7 = this.mBaseTranslation;
                f = MotionController$$ExternalSyntheticOutline0.m(f6, f7, interpolation, f7);
            } else {
                float interpolation2 = RUBBER_BAND_INTERPOLATOR_APPEAR.getInterpolation(MathUtils.saturate((f5 - abs) / f5));
                float f8 = this.mBaseTranslation;
                f = f8 - ((f8 / 4.0f) * interpolation2);
            }
            boolean z2 = this.mTriggerBack;
            if (Math.abs(this.mTotalTouchDelta) > this.mMinDeltaForSwitch) {
                if (this.mTotalTouchDelta > 0.0f) {
                    z2 = true;
                } else {
                    z2 = false;
                }
            }
            this.mVelocityTracker.computeCurrentVelocity(1000);
            float xVelocity = this.mVelocityTracker.getXVelocity();
            float signum = Math.signum(xVelocity) * Math.min((MathUtils.mag(xVelocity, this.mVelocityTracker.getYVelocity()) / 1000.0f) * 4.0f, 4.0f);
            this.mAngleOffset = signum;
            boolean z3 = this.mIsLeftPanel;
            if ((z3 && this.mArrowsPointLeft) || (!z3 && !this.mArrowsPointLeft)) {
                this.mAngleOffset = signum * (-1.0f);
            }
            if (Math.abs(f3) <= Math.abs(x - this.mStartX) * 2.0f) {
                z = z2;
            }
            if (this.mTriggerBack != z) {
                Log.d("NoBackGesture", "set mTriggerBack=" + z + ", mTotalTouchDelta=" + this.mTotalTouchDelta + ", mMinDeltaForSwitch=" + this.mMinDeltaForSwitch + ", yOffset=" + f3 + ", x=" + x + ", mStartX=" + this.mStartX);
            }
            setTriggerBack(z, true);
            if (!this.mTriggerBack) {
                f = 0.0f;
            } else {
                boolean z4 = this.mIsLeftPanel;
                if ((z4 && this.mArrowsPointLeft) || (!z4 && !this.mArrowsPointLeft)) {
                    f -= ((float) Math.cos(Math.toRadians(56.0f))) * this.mArrowLength;
                }
            }
            setDesiredTranslation(f, true);
            updateAngle(true);
            float height = (getHeight() / 2.0f) - this.mArrowLength;
            float signum2 = Math.signum(f3) * RUBBER_BAND_INTERPOLATOR.getInterpolation(MathUtils.constrain(Math.abs(f3) / (15.0f * height), 0.0f, 1.0f)) * height;
            if (this.mDesiredVerticalTranslation != signum2) {
                this.mDesiredVerticalTranslation = signum2;
                this.mVerticalTranslationAnimation.animateToFinalPosition(signum2);
                invalidate();
            }
            updateSamplingRect();
        } else if (actionMasked == 3) {
            Log.d("NoBackGesture", "NavigationBarEdgePanel ACTION_CANCEL");
            cancelBack();
            this.mRegionSamplingHelper.stop();
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public final void setDesiredTranslation(float f, boolean z) {
        if (this.mDesiredTranslation != f) {
            this.mDesiredTranslation = f;
            if (!z) {
                this.mCurrentTranslation = f;
                invalidate();
                return;
            }
            this.mTranslationAnimation.animateToFinalPosition(f);
        }
    }

    @Override // com.android.systemui.plugins.NavigationEdgeBackPlugin
    public final void setDisplaySize(Point point) {
        this.mDisplaySize.set(point.x, point.y);
        Point point2 = this.mDisplaySize;
        this.mScreenSize = Math.min(point2.x, point2.y);
    }

    @Override // com.android.systemui.plugins.NavigationEdgeBackPlugin
    public final void setInsets(int i, int i2) {
        this.mLeftInset = i;
        this.mRightInset = i2;
    }

    @Override // com.android.systemui.plugins.NavigationEdgeBackPlugin
    public final void setIsLeftPanel(boolean z) {
        int i;
        this.mIsLeftPanel = z;
        WindowManager.LayoutParams layoutParams = this.mLayoutParams;
        if (z) {
            i = 51;
        } else {
            i = 53;
        }
        layoutParams.gravity = i;
    }

    @Override // com.android.systemui.plugins.NavigationEdgeBackPlugin
    public final void setLayoutParams(WindowManager.LayoutParams layoutParams) {
        this.mLayoutParams = layoutParams;
        this.mWindowManager.addView(this, layoutParams);
    }

    public final void setTriggerBack(boolean z, boolean z2) {
        if (this.mTriggerBack != z) {
            this.mTriggerBack = z;
            this.mAngleAnimation.cancel();
            updateAngle(z2);
            this.mTranslationAnimation.cancel();
            BackAnimation backAnimation = this.mBackAnimation;
            if (backAnimation != null) {
                backAnimation.setTriggerBack(z);
            }
        }
    }

    public final void updateAngle(boolean z) {
        float f;
        SpringForce springForce;
        boolean z2 = this.mTriggerBack;
        if (z2) {
            f = this.mAngleOffset + 56.0f;
        } else {
            f = 90.0f;
        }
        if (f != this.mDesiredAngle) {
            if (!z) {
                this.mCurrentAngle = f;
                invalidate();
            } else {
                SpringAnimation springAnimation = this.mAngleAnimation;
                if (z2) {
                    springForce = this.mAngleAppearForce;
                } else {
                    springForce = this.mAngleDisappearForce;
                }
                Objects.requireNonNull(springAnimation);
                springAnimation.mSpring = springForce;
                this.mAngleAnimation.animateToFinalPosition(f);
            }
            this.mDesiredAngle = f;
        }
    }

    public final void updateIsDark(boolean z) {
        int i;
        int i2;
        if (this.mIsDark) {
            i = this.mProtectionColorDark;
        } else {
            i = this.mProtectionColorLight;
        }
        this.mProtectionPaint.setColor(i);
        if (this.mIsDark) {
            i2 = this.mArrowColorDark;
        } else {
            i2 = this.mArrowColorLight;
        }
        this.mArrowColor = i2;
        this.mArrowColorAnimator.cancel();
        if (!z) {
            int i3 = this.mArrowColor;
            this.mCurrentArrowColor = i3;
            this.mPaint.setColor(i3);
            invalidate();
            return;
        }
        this.mArrowStartColor = this.mCurrentArrowColor;
        this.mArrowColorAnimator.start();
    }

    public final void updateSamplingRect() {
        int i;
        WindowManager.LayoutParams layoutParams = this.mLayoutParams;
        int i2 = layoutParams.y;
        if (this.mIsLeftPanel) {
            i = this.mLeftInset;
        } else {
            i = (this.mDisplaySize.x - this.mRightInset) - layoutParams.width;
        }
        this.mSamplingRect.set(i, i2, layoutParams.width + i, layoutParams.height + i2);
        float f = this.mDesiredTranslation;
        if (!this.mTriggerBack) {
            f = this.mBaseTranslation;
            boolean z = this.mIsLeftPanel;
            if ((z && this.mArrowsPointLeft) || (!z && !this.mArrowsPointLeft)) {
                f -= ((float) Math.cos(Math.toRadians(56.0f))) * this.mArrowLength;
            }
        }
        float f2 = f - (this.mArrowThickness / 2.0f);
        if (!this.mIsLeftPanel) {
            f2 = this.mSamplingRect.width() - f2;
        }
        double d = 56.0f;
        float cos = ((float) Math.cos(Math.toRadians(d))) * this.mArrowLength;
        float sin = ((float) Math.sin(Math.toRadians(d))) * this.mArrowLength * 2.0f;
        if (!this.mArrowsPointLeft) {
            f2 -= cos;
        }
        this.mSamplingRect.offset((int) f2, (int) (((getHeight() * 0.5f) + this.mDesiredVerticalTranslation) - (sin / 2.0f)));
        Rect rect = this.mSamplingRect;
        int i3 = rect.left;
        int i4 = rect.top;
        rect.set(i3, i4, (int) (i3 + cos), (int) (i4 + sin));
        this.mRegionSamplingHelper.updateSamplingRect();
    }

    /* JADX WARN: Type inference failed for: r2v3, types: [com.android.systemui.navigationbar.gestural.NavigationBarEdgePanel$1] */
    public NavigationBarEdgePanel(Context context, BackAnimation backAnimation) {
        super(context);
        boolean z;
        Paint paint = new Paint();
        this.mPaint = paint;
        final boolean z2 = false;
        this.mShowProtection = false;
        this.mWindowManager = (WindowManager) context.getSystemService(WindowManager.class);
        this.mBackAnimation = backAnimation;
        float f = context.getResources().getDisplayMetrics().density;
        this.mDensity = f;
        float f2 = 32.0f * f;
        this.mBaseTranslation = f2;
        this.mArrowLength = 18.0f * f;
        float f3 = f * 2.5f;
        this.mArrowThickness = f3;
        this.mMinDeltaForSwitch = f2;
        paint.setStrokeWidth(f3);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mArrowColorAnimator = ofFloat;
        ofFloat.setDuration(120L);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.navigationbar.gestural.NavigationBarEdgePanel$$ExternalSyntheticLambda1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                NavigationBarEdgePanel navigationBarEdgePanel = NavigationBarEdgePanel.this;
                Objects.requireNonNull(navigationBarEdgePanel);
                int blendARGB = ColorUtils.blendARGB(navigationBarEdgePanel.mArrowStartColor, navigationBarEdgePanel.mArrowColor, valueAnimator.getAnimatedFraction());
                navigationBarEdgePanel.mCurrentArrowColor = blendARGB;
                navigationBarEdgePanel.mPaint.setColor(blendARGB);
                navigationBarEdgePanel.invalidate();
            }
        });
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mArrowDisappearAnimation = ofFloat2;
        ofFloat2.setDuration(100L);
        ofFloat2.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.systemui.navigationbar.gestural.NavigationBarEdgePanel$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                NavigationBarEdgePanel navigationBarEdgePanel = NavigationBarEdgePanel.this;
                Objects.requireNonNull(navigationBarEdgePanel);
                navigationBarEdgePanel.mDisappearAmount = ((Float) valueAnimator.getAnimatedValue()).floatValue();
                navigationBarEdgePanel.invalidate();
            }
        });
        SpringAnimation springAnimation = new SpringAnimation(this, CURRENT_ANGLE);
        this.mAngleAnimation = springAnimation;
        SpringForce springForce = new SpringForce();
        springForce.setStiffness(500.0f);
        springForce.setDampingRatio(0.5f);
        this.mAngleAppearForce = springForce;
        SpringForce springForce2 = new SpringForce();
        springForce2.setStiffness(1500.0f);
        springForce2.setDampingRatio(0.5f);
        springForce2.mFinalPosition = 90.0f;
        this.mAngleDisappearForce = springForce2;
        springAnimation.mSpring = springForce;
        springAnimation.mMaxValue = 90.0f;
        SpringAnimation springAnimation2 = new SpringAnimation(this, CURRENT_TRANSLATION);
        this.mTranslationAnimation = springAnimation2;
        SpringForce springForce3 = new SpringForce();
        springForce3.setStiffness(1500.0f);
        springForce3.setDampingRatio(0.75f);
        this.mRegularTranslationSpring = springForce3;
        SpringForce springForce4 = new SpringForce();
        springForce4.setStiffness(450.0f);
        springForce4.setDampingRatio(0.75f);
        this.mTriggerBackSpring = springForce4;
        springAnimation2.mSpring = springForce3;
        SpringAnimation springAnimation3 = new SpringAnimation(this, CURRENT_VERTICAL_TRANSLATION);
        this.mVerticalTranslationAnimation = springAnimation3;
        SpringForce springForce5 = new SpringForce();
        springForce5.setStiffness(1500.0f);
        springForce5.setDampingRatio(0.75f);
        springAnimation3.mSpring = springForce5;
        Paint paint2 = new Paint(paint);
        this.mProtectionPaint = paint2;
        paint2.setStrokeWidth(f3 + 2.0f);
        loadDimens();
        int themeAttr = Utils.getThemeAttr(context, 2130968913);
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, Utils.getThemeAttr(context, 2130969364));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, themeAttr);
        this.mArrowColorLight = Utils.getColorAttrDefaultColor(contextThemeWrapper, 2130969739);
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(contextThemeWrapper2, 2130969739);
        this.mArrowColorDark = colorAttrDefaultColor;
        this.mProtectionColorDark = this.mArrowColorLight;
        this.mProtectionColorLight = colorAttrDefaultColor;
        updateIsDark(false);
        if (getLayoutDirection() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mArrowsPointLeft = z;
        invalidate();
        float dimension = context.getResources().getDimension(2131166597);
        this.mSwipeTriggerThreshold = dimension;
        float dimension2 = context.getResources().getDimension(2131166598);
        if (backAnimation != null) {
            backAnimation.setSwipeThresholds(dimension, dimension2);
        }
        setVisibility(8);
        Executor executor = (Executor) Dependency.get(Dependency.BACKGROUND_EXECUTOR);
        z2 = ((View) this).mContext.getDisplayId() == 0 ? true : z2;
        RegionSamplingHelper regionSamplingHelper = new RegionSamplingHelper(this, new RegionSamplingHelper.SamplingCallback() { // from class: com.android.systemui.navigationbar.gestural.NavigationBarEdgePanel.5
            @Override // com.android.systemui.shared.navigationbar.RegionSamplingHelper.SamplingCallback
            public final Rect getSampledRegion() {
                return NavigationBarEdgePanel.this.mSamplingRect;
            }

            @Override // com.android.systemui.shared.navigationbar.RegionSamplingHelper.SamplingCallback
            public final void onRegionDarknessChanged(boolean z3) {
                NavigationBarEdgePanel navigationBarEdgePanel = NavigationBarEdgePanel.this;
                Objects.requireNonNull(navigationBarEdgePanel);
                navigationBarEdgePanel.mIsDark = !z3;
                navigationBarEdgePanel.updateIsDark(true);
            }

            @Override // com.android.systemui.shared.navigationbar.RegionSamplingHelper.SamplingCallback
            public final boolean isSamplingEnabled() {
                return z2;
            }
        }, executor);
        this.mRegionSamplingHelper = regionSamplingHelper;
        regionSamplingHelper.mWindowVisible = true;
        regionSamplingHelper.updateSamplingListener();
        this.mShowProtection = !z2;
    }

    public final void loadDimens() {
        Resources resources = getResources();
        this.mArrowPaddingEnd = resources.getDimensionPixelSize(2131166602);
        this.mMinArrowPosition = resources.getDimensionPixelSize(2131166599);
        this.mFingerOffset = resources.getDimensionPixelSize(2131166600);
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        boolean z;
        super.onConfigurationChanged(configuration);
        if (getLayoutDirection() == 0) {
            z = true;
        } else {
            z = false;
        }
        this.mArrowsPointLeft = z;
        invalidate();
        loadDimens();
    }

    @Override // android.view.View
    public final void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mMaxTranslation = getWidth() - this.mArrowPaddingEnd;
    }

    @Override // com.android.systemui.plugins.NavigationEdgeBackPlugin
    public final void setBackCallback(NavigationEdgeBackPlugin.BackCallback backCallback) {
        this.mBackCallback = backCallback;
    }
}
