package com.android.systemui.qs.external;

import android.content.Context;
import android.content.SharedPreferences;
/* compiled from: CustomTileStatePersister.kt */
/* loaded from: classes.dex */
public final class CustomTileStatePersister {
    public final SharedPreferences sharedPreferences;

    public CustomTileStatePersister(Context context) {
        this.sharedPreferences = context.getSharedPreferences("custom_tiles_state", 0);
    }
}
