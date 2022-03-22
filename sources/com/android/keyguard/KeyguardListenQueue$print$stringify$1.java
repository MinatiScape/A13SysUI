package com.android.keyguard;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import java.text.DateFormat;
import java.util.Date;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: KeyguardListenQueue.kt */
/* loaded from: classes.dex */
final class KeyguardListenQueue$print$stringify$1 extends Lambda implements Function1<KeyguardListenModel, String> {
    public final /* synthetic */ DateFormat $dateFormat;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public KeyguardListenQueue$print$stringify$1(DateFormat dateFormat) {
        super(1);
        this.$dateFormat = dateFormat;
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(KeyguardListenModel keyguardListenModel) {
        KeyguardListenModel keyguardListenModel2 = keyguardListenModel;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("    ");
        m.append((Object) this.$dateFormat.format(new Date(keyguardListenModel2.getTimeMillis())));
        m.append(' ');
        m.append(keyguardListenModel2);
        return m.toString();
    }
}
