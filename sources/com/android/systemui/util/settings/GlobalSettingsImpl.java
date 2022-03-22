package com.android.systemui.util.settings;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
/* loaded from: classes.dex */
public final class GlobalSettingsImpl implements GlobalSettings {
    public final ContentResolver mContentResolver;

    @Override // com.android.systemui.util.settings.SettingsProxy
    public final String getStringForUser(String str, int i) {
        return Settings.Global.getStringForUser(this.mContentResolver, str, i);
    }

    @Override // com.android.systemui.util.settings.SettingsProxy
    public final boolean putStringForUser(String str, String str2, int i) {
        return Settings.Global.putStringForUser(this.mContentResolver, str, str2, i);
    }

    public GlobalSettingsImpl(ContentResolver contentResolver) {
        this.mContentResolver = contentResolver;
    }

    @Override // com.android.systemui.util.settings.SettingsProxy
    public final Uri getUriFor(String str) {
        return Settings.Global.getUriFor(str);
    }

    @Override // com.android.systemui.util.settings.SettingsProxy
    public final ContentResolver getContentResolver() {
        return this.mContentResolver;
    }
}
