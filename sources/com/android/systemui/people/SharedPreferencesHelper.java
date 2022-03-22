package com.android.systemui.people;

import android.content.SharedPreferences;
import com.android.systemui.people.widget.PeopleTileKey;
import java.util.Objects;
/* loaded from: classes.dex */
public final class SharedPreferencesHelper {
    public static void setPeopleTileKey(SharedPreferences sharedPreferences, PeopleTileKey peopleTileKey) {
        Objects.requireNonNull(peopleTileKey);
        String str = peopleTileKey.mShortcutId;
        int i = peopleTileKey.mUserId;
        String str2 = peopleTileKey.mPackageName;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("shortcut_id", str);
        edit.putInt("user_id", i);
        edit.putString("package_name", str2);
        edit.apply();
    }
}
