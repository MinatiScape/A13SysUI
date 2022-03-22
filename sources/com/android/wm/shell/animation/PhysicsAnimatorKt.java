package com.android.wm.shell.animation;

import com.android.wm.shell.animation.PhysicsAnimator;
import java.util.WeakHashMap;
/* compiled from: PhysicsAnimator.kt */
/* loaded from: classes.dex */
public final class PhysicsAnimatorKt {
    public static final WeakHashMap<Object, PhysicsAnimator<?>> animators = new WeakHashMap<>();
    public static final PhysicsAnimator.SpringConfig globalDefaultSpring = new PhysicsAnimator.SpringConfig(1500.0f, 0.5f);
    public static final PhysicsAnimator.FlingConfig globalDefaultFling = new PhysicsAnimator.FlingConfig(1.0f, -3.4028235E38f, Float.MAX_VALUE, 0.0f);
}
