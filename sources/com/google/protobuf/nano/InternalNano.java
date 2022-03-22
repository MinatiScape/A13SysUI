package com.google.protobuf.nano;

import java.nio.charset.Charset;
/* loaded from: classes.dex */
public final class InternalNano {
    public static final Charset UTF_8 = Charset.forName("UTF-8");
    public static final Object LAZY_INIT_LOCK = new Object();

    static {
        Charset.forName("ISO-8859-1");
    }
}
