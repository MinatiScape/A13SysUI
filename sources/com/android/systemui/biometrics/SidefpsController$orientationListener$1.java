package com.android.systemui.biometrics;

import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: SidefpsController.kt */
/* loaded from: classes.dex */
public final class SidefpsController$orientationListener$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ SidefpsController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public SidefpsController$orientationListener$1(SidefpsController sidefpsController) {
        super(0);
        this.this$0 = sidefpsController;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        SidefpsController sidefpsController = this.this$0;
        Objects.requireNonNull(sidefpsController);
        if (sidefpsController.overlayView != null) {
            sidefpsController.setOverlayView(sidefpsController.createOverlayForDisplay());
        }
        return Unit.INSTANCE;
    }
}
