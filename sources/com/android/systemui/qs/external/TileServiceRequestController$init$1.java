package com.android.systemui.qs.external;

import com.android.systemui.qs.external.TileServiceRequestController;
import com.android.systemui.statusbar.commandline.Command;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: TileServiceRequestController.kt */
/* loaded from: classes.dex */
public final class TileServiceRequestController$init$1 extends Lambda implements Function0<Command> {
    public final /* synthetic */ TileServiceRequestController this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public TileServiceRequestController$init$1(TileServiceRequestController tileServiceRequestController) {
        super(0);
        this.this$0 = tileServiceRequestController;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Command invoke() {
        return new TileServiceRequestController.TileServiceRequestCommand();
    }
}
