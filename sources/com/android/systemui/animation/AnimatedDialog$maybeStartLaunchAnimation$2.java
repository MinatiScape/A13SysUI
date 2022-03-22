package com.android.systemui.animation;

import android.view.ViewGroup;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: DialogLaunchAnimator.kt */
/* loaded from: classes.dex */
public final class AnimatedDialog$maybeStartLaunchAnimation$2 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ AnimatedDialog this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public AnimatedDialog$maybeStartLaunchAnimation$2(AnimatedDialog animatedDialog) {
        super(0);
        this.this$0 = animatedDialog;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        AnimatedDialog animatedDialog = this.this$0;
        Objects.requireNonNull(animatedDialog);
        animatedDialog.touchSurface.setTag(2131428205, null);
        AnimatedDialog animatedDialog2 = this.this$0;
        Objects.requireNonNull(animatedDialog2);
        animatedDialog2.touchSurface.setVisibility(4);
        AnimatedDialog animatedDialog3 = this.this$0;
        animatedDialog3.isLaunching = false;
        if (animatedDialog3.dismissRequested) {
            animatedDialog3.dialog.dismiss();
        }
        AnimatedDialog animatedDialog4 = this.this$0;
        if (animatedDialog4.backgroundLayoutListener != null) {
            ViewGroup viewGroup = animatedDialog4.dialogContentWithBackground;
            Intrinsics.checkNotNull(viewGroup);
            viewGroup.addOnLayoutChangeListener(this.this$0.backgroundLayoutListener);
        }
        return Unit.INSTANCE;
    }
}
