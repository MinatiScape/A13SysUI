package com.airbnb.lottie.utils;

import android.util.Log;
import java.util.HashSet;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Logger {
    public static LogcatLogger INSTANCE = new LogcatLogger();

    public static void debug() {
        Objects.requireNonNull(INSTANCE);
    }

    public static void warning(String str) {
        Objects.requireNonNull(INSTANCE);
        HashSet hashSet = LogcatLogger.loggedMessages;
        if (!hashSet.contains(str)) {
            Log.w("LOTTIE", str, null);
            hashSet.add(str);
        }
    }
}
