package com.android.wm.shell.common.magnetictarget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.PointF;
import android.os.VibrationAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.VelocityTracker;
import android.view.View;
import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.DismissCircleView;
import java.util.ArrayList;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function5;
/* compiled from: MagnetizedObject.kt */
/* loaded from: classes.dex */
public abstract class MagnetizedObject<T> {
    public final PhysicsAnimator<T> animator;
    public PhysicsAnimator.SpringConfig flungIntoTargetSpringConfig;
    public MagnetListener magnetListener;
    public boolean movedBeyondSlop;
    public PhysicsAnimator.SpringConfig springConfig;
    public MagneticTarget targetObjectIsStuckTo;
    public int touchSlop;
    public final T underlyingObject;
    public final Vibrator vibrator;
    public final FloatPropertyCompat<? super T> xProperty;
    public final FloatPropertyCompat<? super T> yProperty;
    public final int[] objectLocationOnScreen = new int[2];
    public final ArrayList<MagneticTarget> associatedTargets = new ArrayList<>();
    public final VelocityTracker velocityTracker = VelocityTracker.obtain();
    public final VibrationAttributes vibrationAttributes = VibrationAttributes.createForUsage(18);
    public PointF touchDown = new PointF();
    public Function5<? super MagneticTarget, ? super Float, ? super Float, ? super Boolean, ? super Function0<Unit>, Unit> animateStuckToTarget = new MagnetizedObject$animateStuckToTarget$1(this);
    public boolean flingToTargetEnabled = true;
    public float flingToTargetWidthPercent = 3.0f;
    public float flingToTargetMinVelocity = 4000.0f;
    public float flingUnstuckFromTargetMinVelocity = 4000.0f;
    public float stickToTargetMaxXVelocity = 2000.0f;
    public boolean hapticsEnabled = true;

    /* compiled from: MagnetizedObject.kt */
    /* loaded from: classes.dex */
    public interface MagnetListener {
        void onReleasedInTarget();

        void onStuckToTarget();

        void onUnstuckFromTarget(float f, float f2, boolean z);
    }

    public abstract float getHeight(T t);

    public abstract void getLocationOnScreen(T t, int[] iArr);

    public abstract float getWidth(T t);

    /* compiled from: MagnetizedObject.kt */
    /* loaded from: classes.dex */
    public static final class MagneticTarget {
        public int magneticFieldRadiusPx;
        public final View targetView;
        public final PointF centerOnScreen = new PointF();
        public final int[] tempLoc = new int[2];

        public MagneticTarget(DismissCircleView dismissCircleView, int i) {
            this.targetView = dismissCircleView;
            this.magneticFieldRadiusPx = i;
        }
    }

    public final void cancelAnimations$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell() {
        this.animator.cancel(this.xProperty, this.yProperty);
    }

    public final MagnetListener getMagnetListener() {
        MagnetListener magnetListener = this.magnetListener;
        if (magnetListener != null) {
            return magnetListener;
        }
        return null;
    }

    public final boolean getObjectStuckToTarget() {
        if (this.targetObjectIsStuckTo != null) {
            return true;
        }
        return false;
    }

    /* JADX WARN: Removed duplicated region for block: B:106:0x023d A[SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean maybeConsumeMotionEvent(android.view.MotionEvent r15) {
        /*
            Method dump skipped, instructions count: 615
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.common.magnetictarget.MagnetizedObject.maybeConsumeMotionEvent(android.view.MotionEvent):boolean");
    }

    @SuppressLint({"MissingPermission"})
    public final void vibrateIfEnabled(int i) {
        if (this.hapticsEnabled) {
            this.vibrator.vibrate(VibrationEffect.createPredefined(i), this.vibrationAttributes);
        }
    }

    public MagnetizedObject(Context context, T t, FloatPropertyCompat<? super T> floatPropertyCompat, FloatPropertyCompat<? super T> floatPropertyCompat2) {
        this.underlyingObject = t;
        this.xProperty = floatPropertyCompat;
        this.yProperty = floatPropertyCompat2;
        Function1<Object, ? extends PhysicsAnimator<?>> function1 = PhysicsAnimator.instanceConstructor;
        this.animator = PhysicsAnimator.Companion.getInstance(t);
        Object systemService = context.getSystemService("vibrator");
        Objects.requireNonNull(systemService, "null cannot be cast to non-null type android.os.Vibrator");
        this.vibrator = (Vibrator) systemService;
        PhysicsAnimator.SpringConfig springConfig = new PhysicsAnimator.SpringConfig(1500.0f, 1.0f);
        this.springConfig = springConfig;
        this.flungIntoTargetSpringConfig = springConfig;
    }
}
