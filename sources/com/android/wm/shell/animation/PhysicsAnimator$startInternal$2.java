package com.android.wm.shell.animation;

import androidx.dynamicanimation.animation.SpringAnimation;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes.dex */
public final /* synthetic */ class PhysicsAnimator$startInternal$2 extends FunctionReferenceImpl implements Function0<Unit> {
    public PhysicsAnimator$startInternal$2(SpringAnimation springAnimation) {
        super(0, springAnimation, SpringAnimation.class, "start", "start()V", 0);
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        ((SpringAnimation) this.receiver).start();
        return Unit.INSTANCE;
    }
}
