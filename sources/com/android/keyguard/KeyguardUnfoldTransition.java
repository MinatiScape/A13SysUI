package com.android.keyguard;

import android.content.Context;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.jvm.functions.Function0;
/* compiled from: KeyguardUnfoldTransition.kt */
/* loaded from: classes.dex */
public final class KeyguardUnfoldTransition {
    public final Context context;
    public boolean statusViewCentered;
    public final Lazy translateAnimator$delegate;
    public final Function0<Boolean> filterSplitShadeOnly = new KeyguardUnfoldTransition$filterSplitShadeOnly$1(this);
    public final Function0<Boolean> filterNever = KeyguardUnfoldTransition$filterNever$1.INSTANCE;

    public KeyguardUnfoldTransition(Context context, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        this.context = context;
        this.translateAnimator$delegate = LazyKt__LazyJVMKt.lazy(new KeyguardUnfoldTransition$translateAnimator$2(this, naturalRotationUnfoldProgressProvider));
    }
}
