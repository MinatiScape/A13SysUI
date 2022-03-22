package com.android.systemui.controls.controller;

import android.service.controls.IControlsSubscription;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatefulControlSubscriber.kt */
/* loaded from: classes.dex */
public final class StatefulControlSubscriber$onSubscribe$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ IControlsSubscription $subs;
    public final /* synthetic */ StatefulControlSubscriber this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public StatefulControlSubscriber$onSubscribe$1(StatefulControlSubscriber statefulControlSubscriber, IControlsSubscription iControlsSubscription) {
        super(0);
        this.this$0 = statefulControlSubscriber;
        this.$subs = iControlsSubscription;
    }

    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        StatefulControlSubscriber statefulControlSubscriber = this.this$0;
        statefulControlSubscriber.subscriptionOpen = true;
        IControlsSubscription iControlsSubscription = this.$subs;
        statefulControlSubscriber.subscription = iControlsSubscription;
        statefulControlSubscriber.provider.startSubscription(iControlsSubscription, statefulControlSubscriber.requestLimit);
        return Unit.INSTANCE;
    }
}
