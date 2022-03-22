package com.android.systemui.controls.management;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
final class ControlAdapter$onCreateViewHolder$2 extends Lambda implements Function2<String, Boolean, Unit> {
    public final /* synthetic */ ControlAdapter this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ControlAdapter$onCreateViewHolder$2(ControlAdapter controlAdapter) {
        super(2);
        this.this$0 = controlAdapter;
    }

    @Override // kotlin.jvm.functions.Function2
    public final Unit invoke(String str, Boolean bool) {
        String str2 = str;
        boolean booleanValue = bool.booleanValue();
        ControlsModel controlsModel = this.this$0.model;
        if (controlsModel != null) {
            controlsModel.changeFavoriteStatus(str2, booleanValue);
        }
        return Unit.INSTANCE;
    }
}
