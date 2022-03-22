package com.google.android.systemui.assist.uihints;

import android.content.Context;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
/* compiled from: AssistantWarmer.kt */
/* loaded from: classes.dex */
public final class AssistantWarmer implements NgaMessageHandler.WarmingListener {
    public final Context context;
    public boolean primed;
    public NgaMessageHandler.WarmingRequest request;

    public AssistantWarmer(Context context) {
        this.context = context;
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.WarmingListener
    public final void onWarmingRequest(NgaMessageHandler.WarmingRequest warmingRequest) {
        this.request = warmingRequest;
    }
}
