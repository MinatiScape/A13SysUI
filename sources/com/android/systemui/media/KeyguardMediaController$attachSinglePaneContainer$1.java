package com.android.systemui.media;

import android.view.ViewGroup;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.FunctionReferenceImpl;
/* compiled from: KeyguardMediaController.kt */
/* loaded from: classes.dex */
public /* synthetic */ class KeyguardMediaController$attachSinglePaneContainer$1 extends FunctionReferenceImpl implements Function1<Boolean, Unit> {
    public KeyguardMediaController$attachSinglePaneContainer$1(KeyguardMediaController keyguardMediaController) {
        super(1, keyguardMediaController, KeyguardMediaController.class, "onMediaHostVisibilityChanged", "onMediaHostVisibilityChanged(Z)V", 0);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Unit invoke(Boolean bool) {
        boolean booleanValue = bool.booleanValue();
        KeyguardMediaController keyguardMediaController = (KeyguardMediaController) this.receiver;
        Objects.requireNonNull(keyguardMediaController);
        keyguardMediaController.refreshMediaPosition();
        if (booleanValue) {
            ViewGroup.LayoutParams layoutParams = keyguardMediaController.mediaHost.getHostView().getLayoutParams();
            layoutParams.height = -2;
            layoutParams.width = -1;
        }
        return Unit.INSTANCE;
    }
}
