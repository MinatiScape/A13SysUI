package com.android.systemui;

import android.content.Context;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
/* loaded from: classes.dex */
public final class Prefs {

    @Retention(RetentionPolicy.SOURCE)
    /* loaded from: classes.dex */
    public @interface Key {
    }

    public static boolean getBoolean(Context context, String str) {
        return context.getSharedPreferences(context.getPackageName(), 0).getBoolean(str, false);
    }

    public static void putBoolean(Context context, String str, boolean z) {
        context.getSharedPreferences(context.getPackageName(), 0).edit().putBoolean(str, z).apply();
    }
}
