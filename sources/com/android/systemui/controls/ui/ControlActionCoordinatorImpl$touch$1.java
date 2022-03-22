package com.android.systemui.controls.ui;

import android.app.PendingIntent;
import android.service.controls.Control;
import android.service.controls.actions.CommandAction;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: ControlActionCoordinatorImpl.kt */
/* loaded from: classes.dex */
public final class ControlActionCoordinatorImpl$touch$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ Control $control;
    public final /* synthetic */ ControlViewHolder $cvh;
    public final /* synthetic */ String $templateId;
    public final /* synthetic */ ControlActionCoordinatorImpl this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ControlActionCoordinatorImpl$touch$1(ControlViewHolder controlViewHolder, ControlActionCoordinatorImpl controlActionCoordinatorImpl, Control control, String str) {
        super(0);
        this.$cvh = controlViewHolder;
        this.this$0 = controlActionCoordinatorImpl;
        this.$control = control;
        this.$templateId = str;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        ControlViewHolder controlViewHolder = this.$cvh;
        Objects.requireNonNull(controlViewHolder);
        controlViewHolder.layout.performHapticFeedback(6);
        if (this.$cvh.usePanel()) {
            ControlActionCoordinatorImpl controlActionCoordinatorImpl = this.this$0;
            ControlViewHolder controlViewHolder2 = this.$cvh;
            PendingIntent appIntent = this.$control.getAppIntent();
            Objects.requireNonNull(controlActionCoordinatorImpl);
            controlActionCoordinatorImpl.bgExecutor.execute(new ControlActionCoordinatorImpl$showDetail$1(controlActionCoordinatorImpl, appIntent, controlViewHolder2));
        } else {
            this.$cvh.action(new CommandAction(this.$templateId));
        }
        return Unit.INSTANCE;
    }
}
