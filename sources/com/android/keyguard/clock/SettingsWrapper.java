package com.android.keyguard.clock;

import android.content.ContentResolver;
import android.provider.Settings;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Objects;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public final class SettingsWrapper {
    public final ContentResolver mContentResolver;
    public final Migration mMigration;

    /* loaded from: classes.dex */
    public interface Migration {
    }

    /* loaded from: classes.dex */
    public static final class Migrator implements Migration {
        public final ContentResolver mContentResolver;

        public Migrator(ContentResolver contentResolver) {
            this.mContentResolver = contentResolver;
        }
    }

    @VisibleForTesting
    public String decode(String str, int i) {
        if (str == null) {
            return str;
        }
        try {
            try {
                return new JSONObject(str).getString("clock");
            } catch (JSONException e) {
                Log.e("ClockFaceSettings", "JSON object does not contain clock field.", e);
                return null;
            }
        } catch (JSONException e2) {
            Log.e("ClockFaceSettings", "Settings value is not valid JSON", e2);
            Migrator migrator = (Migrator) this.mMigration;
            Objects.requireNonNull(migrator);
            try {
                JSONObject jSONObject = new JSONObject();
                jSONObject.put("clock", str);
                Settings.Secure.putStringForUser(migrator.mContentResolver, "lock_screen_custom_clock_face", jSONObject.toString(), i);
            } catch (JSONException e3) {
                Log.e("ClockFaceSettings", "Failed migrating settings value to JSON format", e3);
            }
            return str;
        }
    }

    @VisibleForTesting
    public SettingsWrapper(ContentResolver contentResolver, Migration migration) {
        this.mContentResolver = contentResolver;
        this.mMigration = migration;
    }
}
