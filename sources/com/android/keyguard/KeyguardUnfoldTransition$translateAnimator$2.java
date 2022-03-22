package com.android.keyguard;

import com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: KeyguardUnfoldTransition.kt */
/* loaded from: classes.dex */
public final class KeyguardUnfoldTransition$translateAnimator$2 extends Lambda implements Function0<UnfoldConstantTranslateAnimator> {
    public final /* synthetic */ NaturalRotationUnfoldProgressProvider $unfoldProgressProvider;
    public final /* synthetic */ KeyguardUnfoldTransition this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public KeyguardUnfoldTransition$translateAnimator$2(KeyguardUnfoldTransition keyguardUnfoldTransition, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        super(0);
        this.this$0 = keyguardUnfoldTransition;
        this.$unfoldProgressProvider = naturalRotationUnfoldProgressProvider;
    }

    @Override // kotlin.jvm.functions.Function0
    public final UnfoldConstantTranslateAnimator invoke() {
        UnfoldConstantTranslateAnimator.Direction direction = UnfoldConstantTranslateAnimator.Direction.LEFT;
        KeyguardUnfoldTransition keyguardUnfoldTransition = this.this$0;
        Function0<Boolean> function0 = keyguardUnfoldTransition.filterNever;
        Function0<Boolean> function02 = keyguardUnfoldTransition.filterSplitShadeOnly;
        UnfoldConstantTranslateAnimator.Direction direction2 = UnfoldConstantTranslateAnimator.Direction.RIGHT;
        return new UnfoldConstantTranslateAnimator(SetsKt__SetsKt.setOf(new UnfoldConstantTranslateAnimator.ViewIdToTranslate(2131428194, direction, function0), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(2131427757, direction, function0), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(2131428283, direction, function02), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(2131428282, direction, function0), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(2131428521, direction2, function02), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(2131429239, direction2, function0)), this.$unfoldProgressProvider);
    }
}
