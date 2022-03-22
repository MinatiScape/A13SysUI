package com.android.systemui.monet;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* JADX INFO: Access modifiers changed from: package-private */
/* compiled from: ColorScheme.kt */
/* loaded from: classes.dex */
public final class ColorScheme$Companion$humanReadable$1 extends Lambda implements Function1<Integer, CharSequence> {
    public static final ColorScheme$Companion$humanReadable$1 INSTANCE = new ColorScheme$Companion$humanReadable$1();

    public ColorScheme$Companion$humanReadable$1() {
        super(1);
    }

    @Override // kotlin.jvm.functions.Function1
    public final CharSequence invoke(Integer num) {
        return Intrinsics.stringPlus("#", Integer.toHexString(num.intValue()));
    }
}
