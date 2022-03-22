package com.android.systemui.statusbar.phone;

import android.content.Context;
import com.android.systemui.unfold.util.NaturalRotationUnfoldProgressProvider;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
/* compiled from: NotificationPanelUnfoldAnimationController.kt */
/* loaded from: classes.dex */
public final class NotificationPanelUnfoldAnimationController {
    public final Context context;
    public final Lazy translateAnimator$delegate;

    public NotificationPanelUnfoldAnimationController(Context context, NaturalRotationUnfoldProgressProvider naturalRotationUnfoldProgressProvider) {
        this.context = context;
        this.translateAnimator$delegate = LazyKt__LazyJVMKt.lazy(new NotificationPanelUnfoldAnimationController$translateAnimator$2(naturalRotationUnfoldProgressProvider));
    }
}
