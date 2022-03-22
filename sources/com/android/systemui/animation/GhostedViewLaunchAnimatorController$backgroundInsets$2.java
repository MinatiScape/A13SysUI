package com.android.systemui.animation;

import android.graphics.Insets;
import android.graphics.drawable.Drawable;
import java.util.Objects;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: GhostedViewLaunchAnimatorController.kt */
/* loaded from: classes.dex */
public final class GhostedViewLaunchAnimatorController$backgroundInsets$2 extends Lambda implements Function0<Insets> {
    public final /* synthetic */ GhostedViewLaunchAnimatorController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public GhostedViewLaunchAnimatorController$backgroundInsets$2(GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController) {
        super(0);
        this.this$0 = ghostedViewLaunchAnimatorController;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Insets invoke() {
        Insets insets;
        GhostedViewLaunchAnimatorController ghostedViewLaunchAnimatorController = this.this$0;
        Objects.requireNonNull(ghostedViewLaunchAnimatorController);
        Drawable background = ghostedViewLaunchAnimatorController.ghostedView.getBackground();
        if (background == null) {
            insets = null;
        } else {
            insets = background.getOpticalInsets();
        }
        if (insets == null) {
            return Insets.NONE;
        }
        return insets;
    }
}
