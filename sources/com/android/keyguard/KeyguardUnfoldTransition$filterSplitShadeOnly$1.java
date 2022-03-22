package com.android.keyguard;

import java.util.Objects;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: KeyguardUnfoldTransition.kt */
/* loaded from: classes.dex */
public final class KeyguardUnfoldTransition$filterSplitShadeOnly$1 extends Lambda implements Function0<Boolean> {
    public final /* synthetic */ KeyguardUnfoldTransition this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public KeyguardUnfoldTransition$filterSplitShadeOnly$1(KeyguardUnfoldTransition keyguardUnfoldTransition) {
        super(0);
        this.this$0 = keyguardUnfoldTransition;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Boolean invoke() {
        KeyguardUnfoldTransition keyguardUnfoldTransition = this.this$0;
        Objects.requireNonNull(keyguardUnfoldTransition);
        return Boolean.valueOf(!keyguardUnfoldTransition.statusViewCentered);
    }
}
