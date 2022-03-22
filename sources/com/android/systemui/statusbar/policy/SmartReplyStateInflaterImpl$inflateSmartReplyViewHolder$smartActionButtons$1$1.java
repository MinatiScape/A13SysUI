package com.android.systemui.statusbar.policy;

import android.app.Notification;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public final class SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1 extends Lambda implements Function1<Notification.Action, Boolean> {
    public static final SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1 INSTANCE = new SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1();

    public SmartReplyStateInflaterImpl$inflateSmartReplyViewHolder$smartActionButtons$1$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(Notification.Action action) {
        boolean z;
        if (action.actionIntent != null) {
            z = true;
        } else {
            z = false;
        }
        return Boolean.valueOf(z);
    }
}
