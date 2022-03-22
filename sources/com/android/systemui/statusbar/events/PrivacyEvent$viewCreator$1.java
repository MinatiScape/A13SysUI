package com.android.systemui.statusbar.events;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.privacy.OngoingPrivacyChip;
import java.util.Objects;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: StatusEvent.kt */
/* loaded from: classes.dex */
public final class PrivacyEvent$viewCreator$1 extends Lambda implements Function1<Context, OngoingPrivacyChip> {
    public final /* synthetic */ PrivacyEvent this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public PrivacyEvent$viewCreator$1(PrivacyEvent privacyEvent) {
        super(1);
        this.this$0 = privacyEvent;
    }

    @Override // kotlin.jvm.functions.Function1
    public final OngoingPrivacyChip invoke(Context context) {
        View inflate = LayoutInflater.from(context).inflate(2131624348, (ViewGroup) null);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.android.systemui.privacy.OngoingPrivacyChip");
        OngoingPrivacyChip ongoingPrivacyChip = (OngoingPrivacyChip) inflate;
        PrivacyEvent privacyEvent = this.this$0;
        Objects.requireNonNull(privacyEvent);
        ongoingPrivacyChip.setPrivacyList(privacyEvent.privacyItems);
        PrivacyEvent privacyEvent2 = this.this$0;
        Objects.requireNonNull(privacyEvent2);
        ongoingPrivacyChip.setContentDescription(privacyEvent2.contentDescription);
        this.this$0.privacyChip = ongoingPrivacyChip;
        return ongoingPrivacyChip;
    }
}
