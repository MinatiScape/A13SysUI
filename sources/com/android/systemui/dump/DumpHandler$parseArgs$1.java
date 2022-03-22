package com.android.systemui.dump;

import kotlin.collections.ArraysKt___ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: DumpHandler.kt */
/* loaded from: classes.dex */
public final class DumpHandler$parseArgs$1 extends Lambda implements Function1<String, String> {
    public static final DumpHandler$parseArgs$1 INSTANCE = new DumpHandler$parseArgs$1();

    public DumpHandler$parseArgs$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final String invoke(String str) {
        String str2 = str;
        if (ArraysKt___ArraysKt.contains(DumpHandlerKt.PRIORITY_OPTIONS, str2)) {
            return str2;
        }
        throw new IllegalArgumentException();
    }
}
