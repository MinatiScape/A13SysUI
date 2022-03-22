package com.android.systemui.statusbar.phone;

import com.android.systemui.shared.animation.UnfoldConstantTranslateAnimator;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotificationPanelUnfoldAnimationController.kt */
/* loaded from: classes.dex */
public final class NotificationPanelUnfoldAnimationController$translateAnimator$2 extends Lambda implements Function0<UnfoldConstantTranslateAnimator> {
    public final /* synthetic */ NaturalRotationUnfoldProgressProvider $progressProvider;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotificationPanelUnfoldAnimationController$translateAnimator$2(NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        super(0);
        this.$progressProvider = naturalRotationUnfoldProgressProvider;
    }

    @Override // kotlin.jvm.functions.Function0
    public final UnfoldConstantTranslateAnimator invoke() {
        UnfoldConstantTranslateAnimator.Direction direction = UnfoldConstantTranslateAnimator.Direction.LEFT;
        UnfoldConstantTranslateAnimator.Direction direction2 = UnfoldConstantTranslateAnimator.Direction.RIGHT;
        return new UnfoldConstantTranslateAnimator(SetsKt__SetsKt.setOf(new UnfoldConstantTranslateAnimator.ViewIdToTranslate(2131428662, direction), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(2131428521, direction2), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(2131428704, direction2), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(2131427717, direction), new UnfoldConstantTranslateAnimator.ViewIdToTranslate(2131427797, direction)), this.$progressProvider);
    }
}
