package com.google.protobuf;
/* loaded from: classes.dex */
public final class Android {
    public static final boolean IS_ROBOLECTRIC;
    public static final Class<?> MEMORY_CLASS;

    static {
        Class<?> cls;
        boolean z;
        Class<?> cls2 = null;
        try {
            cls = Class.forName("libcore.io.Memory");
        } catch (Throwable unused) {
            cls = null;
        }
        MEMORY_CLASS = cls;
        try {
            cls2 = Class.forName("org.robolectric.Robolectric");
        } catch (Throwable unused2) {
        }
        if (cls2 != null) {
            z = true;
        } else {
            z = false;
        }
        IS_ROBOLECTRIC = z;
    }
}
