package com.android.systemui.animation;

import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.animation.AnimatedDialog;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: DialogLaunchAnimator.kt */
/* loaded from: classes.dex */
final class AnimatedDialog$hideDialogIntoView$2 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ Function1<Boolean, Unit> $onAnimationFinished;
    public final /* synthetic */ AnimatedDialog this$0;

    /* compiled from: DialogLaunchAnimator.kt */
    /* renamed from: com.android.systemui.animation.AnimatedDialog$hideDialogIntoView$2$1  reason: invalid class name */
    /* loaded from: classes.dex */
    final class AnonymousClass1 extends Lambda implements Function0<Unit> {
        public final /* synthetic */ Function1<Boolean, Unit> $onAnimationFinished;
        public final /* synthetic */ AnimatedDialog this$0;

        /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
        /* JADX WARN: Multi-variable type inference failed */
        public AnonymousClass1(Function1<? super Boolean, Unit> function1, AnimatedDialog animatedDialog) {
            super(0);
            this.$onAnimationFinished = function1;
            this.this$0 = animatedDialog;
        }

        @Override // kotlin.jvm.functions.Function0
        public final Unit invoke() {
            this.$onAnimationFinished.invoke(Boolean.TRUE);
            AnimatedDialog animatedDialog = this.this$0;
            animatedDialog.onDialogDismissed.invoke(animatedDialog);
            return Unit.INSTANCE;
        }
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public AnimatedDialog$hideDialogIntoView$2(AnimatedDialog animatedDialog, Function1<? super Boolean, Unit> function1) {
        super(0);
        this.this$0 = animatedDialog;
        this.$onAnimationFinished = function1;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        LaunchableView launchableView;
        AnimatedDialog animatedDialog = this.this$0;
        Objects.requireNonNull(animatedDialog);
        View view = animatedDialog.touchSurface;
        if (view instanceof LaunchableView) {
            launchableView = (LaunchableView) view;
        } else {
            launchableView = null;
        }
        if (launchableView != null) {
            launchableView.setShouldBlockVisibilityChanges(false);
        }
        AnimatedDialog animatedDialog2 = this.this$0;
        Objects.requireNonNull(animatedDialog2);
        animatedDialog2.touchSurface.setVisibility(0);
        AnimatedDialog animatedDialog3 = this.this$0;
        Objects.requireNonNull(animatedDialog3);
        ViewGroup viewGroup = animatedDialog3.dialogContentWithBackground;
        Intrinsics.checkNotNull(viewGroup);
        viewGroup.setVisibility(4);
        AnimatedDialog.AnimatedBoundsLayoutListener animatedBoundsLayoutListener = this.this$0.backgroundLayoutListener;
        if (animatedBoundsLayoutListener != null) {
            viewGroup.removeOnLayoutChangeListener(animatedBoundsLayoutListener);
        }
        AnimatedDialog animatedDialog4 = this.this$0;
        AnonymousClass1 r1 = new AnonymousClass1(this.$onAnimationFinished, animatedDialog4);
        Objects.requireNonNull(animatedDialog4);
        if (animatedDialog4.forceDisableSynchronization) {
            r1.invoke();
        } else {
            boolean z = ViewRootSync.forceDisableSynchronization;
            ViewRootSync.synchronizeNextDraw(animatedDialog4.touchSurface, animatedDialog4.getDecorView(), r1);
        }
        return Unit.INSTANCE;
    }
}
