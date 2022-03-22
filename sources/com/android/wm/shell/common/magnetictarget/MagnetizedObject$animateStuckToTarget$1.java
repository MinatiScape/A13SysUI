package com.android.wm.shell.common.magnetictarget;

import androidx.dynamicanimation.animation.FloatPropertyCompat;
import com.android.wm.shell.animation.PhysicsAnimator;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
import java.util.Objects;
import kotlin.Unit;
import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function5;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: MagnetizedObject.kt */
/* loaded from: classes.dex */
public /* synthetic */ class MagnetizedObject$animateStuckToTarget$1 extends FunctionReferenceImpl implements Function5<MagnetizedObject.MagneticTarget, Float, Float, Boolean, Function0<? extends Unit>, Unit> {
    public MagnetizedObject$animateStuckToTarget$1(Object obj) {
        super(5, obj, MagnetizedObject.class, "animateStuckToTargetInternal", "animateStuckToTargetInternal(Lcom/android/wm/shell/common/magnetictarget/MagnetizedObject$MagneticTarget;FFZLkotlin/jvm/functions/Function0;)V", 0);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.jvm.functions.Function5
    public final void invoke(MagnetizedObject.MagneticTarget magneticTarget, Float f, Float f2, Boolean bool, Object obj) {
        PhysicsAnimator.SpringConfig springConfig;
        float floatValue = f.floatValue();
        float floatValue2 = f2.floatValue();
        boolean booleanValue = bool.booleanValue();
        Function0 function0 = (Function0) obj;
        MagnetizedObject magnetizedObject = (MagnetizedObject) this.receiver;
        Objects.requireNonNull(magnetizedObject);
        magneticTarget.targetView.post(new MagnetizedObject$MagneticTarget$updateLocationOnScreen$1(magneticTarget));
        magnetizedObject.getLocationOnScreen(magnetizedObject.underlyingObject, magnetizedObject.objectLocationOnScreen);
        float width = (magneticTarget.centerOnScreen.x - (magnetizedObject.getWidth(magnetizedObject.underlyingObject) / 2.0f)) - magnetizedObject.objectLocationOnScreen[0];
        float height = (magneticTarget.centerOnScreen.y - (magnetizedObject.getHeight(magnetizedObject.underlyingObject) / 2.0f)) - magnetizedObject.objectLocationOnScreen[1];
        if (booleanValue) {
            springConfig = magnetizedObject.flungIntoTargetSpringConfig;
        } else {
            springConfig = magnetizedObject.springConfig;
        }
        magnetizedObject.cancelAnimations$frameworks__base__libs__WindowManager__Shell__android_common__WindowManager_Shell();
        PhysicsAnimator<T> physicsAnimator = magnetizedObject.animator;
        FloatPropertyCompat<? super T> floatPropertyCompat = magnetizedObject.xProperty;
        physicsAnimator.spring(floatPropertyCompat, floatPropertyCompat.getValue(magnetizedObject.underlyingObject) + width, floatValue, springConfig);
        FloatPropertyCompat<? super T> floatPropertyCompat2 = magnetizedObject.yProperty;
        physicsAnimator.spring(floatPropertyCompat2, floatPropertyCompat2.getValue(magnetizedObject.underlyingObject) + height, floatValue2, springConfig);
        if (function0 != null) {
            PhysicsAnimator<T> physicsAnimator2 = magnetizedObject.animator;
            Objects.requireNonNull(physicsAnimator2);
            physicsAnimator2.endActions.addAll(ArraysKt___ArraysKt.filterNotNull(new Function0[]{function0}));
        }
        magnetizedObject.animator.start();
    }
}
