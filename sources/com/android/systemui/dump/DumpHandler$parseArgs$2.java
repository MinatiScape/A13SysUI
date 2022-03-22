package com.android.systemui.dump;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: DumpHandler.kt */
/* loaded from: classes.dex */
public final class DumpHandler$parseArgs$2 extends Lambda implements Function1<String, Integer> {
    public static final DumpHandler$parseArgs$2 INSTANCE = new DumpHandler$parseArgs$2();

    public DumpHandler$parseArgs$2() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final Integer invoke(String str) {
        return Integer.valueOf(Integer.parseInt(str));
    }
}
