package com.android.systemui.keyguard;

import android.view.View;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: KeyguardUnlockAnimationController.kt */
/* loaded from: classes.dex */
public final class KeyguardUnlockAnimationController$resetSmartspaceTransition$1 implements Runnable {
    public final /* synthetic */ KeyguardUnlockAnimationController this$0;

    public KeyguardUnlockAnimationController$resetSmartspaceTransition$1(KeyguardUnlockAnimationController keyguardUnlockAnimationController) {
        this.this$0 = keyguardUnlockAnimationController;
    }

    @Override // java.lang.Runnable
    public final void run() {
        KeyguardUnlockAnimationController keyguardUnlockAnimationController = this.this$0;
        Objects.requireNonNull(keyguardUnlockAnimationController);
        View view = keyguardUnlockAnimationController.lockscreenSmartspace;
        Intrinsics.checkNotNull(view);
        view.setTranslationX(0.0f);
        KeyguardUnlockAnimationController keyguardUnlockAnimationController2 = this.this$0;
        Objects.requireNonNull(keyguardUnlockAnimationController2);
        View view2 = keyguardUnlockAnimationController2.lockscreenSmartspace;
        Intrinsics.checkNotNull(view2);
        view2.setTranslationY(0.0f);
    }
}
