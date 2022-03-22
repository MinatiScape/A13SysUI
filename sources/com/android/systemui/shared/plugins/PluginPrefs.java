package com.android.systemui.shared.plugins;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.ArraySet;
/* loaded from: classes.dex */
public final class PluginPrefs {
    public final ArraySet mPluginActions;
    public final SharedPreferences mSharedPrefs;

    public PluginPrefs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("plugin_prefs", 0);
        this.mSharedPrefs = sharedPreferences;
        this.mPluginActions = new ArraySet(sharedPreferences.getStringSet("actions", null));
    }
}
