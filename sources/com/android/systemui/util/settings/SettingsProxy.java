package com.android.systemui.util.settings;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.Settings;
import com.android.systemui.communal.conditions.CommunalSettingCondition;
import com.android.systemui.statusbar.policy.LocationControllerImpl;
/* loaded from: classes.dex */
public interface SettingsProxy {
    ContentResolver getContentResolver();

    default int getIntForUser(String str, int i, int i2) {
        String stringForUser = getStringForUser(str, i2);
        if (stringForUser == null) {
            return i;
        }
        try {
            return Integer.parseInt(stringForUser);
        } catch (NumberFormatException unused) {
            return i;
        }
    }

    String getStringForUser(String str, int i);

    Uri getUriFor(String str);

    boolean putStringForUser(String str, String str2, int i);

    boolean putStringForUser$1(String str, String str2, int i);

    default void registerContentObserver(LocationControllerImpl.AnonymousClass1 r2) {
        registerContentObserver(getUriFor("locationShowSystemOps"), r2);
    }

    default void registerContentObserverForUser(String str, ContentObserver contentObserver, int i) {
        registerContentObserverForUser(getUriFor(str), contentObserver, i);
    }

    default float getFloatForUser(int i) {
        String stringForUser = getStringForUser("animator_duration_scale", i);
        if (stringForUser != null) {
            try {
                return Float.parseFloat(stringForUser);
            } catch (NumberFormatException unused) {
            }
        }
        return 1.0f;
    }

    default void registerContentObserver(Uri uri, ContentObserver contentObserver) {
        registerContentObserverForUser(uri, contentObserver, getUserId());
    }

    default float getFloat() {
        return getFloatForUser(getUserId());
    }

    default int getInt(String str, int i) {
        return getIntForUser(str, i, getUserId());
    }

    default int getIntForUser(int i) throws Settings.SettingNotFoundException {
        try {
            return Integer.parseInt(getStringForUser("timeout_to_user_zero", i));
        } catch (NumberFormatException unused) {
            throw new Settings.SettingNotFoundException("timeout_to_user_zero");
        }
    }

    default String getString(String str) {
        return getStringForUser(str, getUserId());
    }

    default int getUserId() {
        return getContentResolver().getUserId();
    }

    default boolean putInt(String str, int i) {
        return putIntForUser(str, i, getUserId());
    }

    default boolean putIntForUser(String str, int i, int i2) {
        return putStringForUser(str, Integer.toString(i), i2);
    }

    default boolean putString(String str, String str2) {
        return putStringForUser(str, str2, getUserId());
    }

    default void registerContentObserver(Uri uri, boolean z, ContentObserver contentObserver) {
        registerContentObserverForUser(uri, z, contentObserver, getUserId());
    }

    default void registerContentObserverForUser(Uri uri, ContentObserver contentObserver, int i) {
        registerContentObserverForUser(uri, false, contentObserver, i);
    }

    default void unregisterContentObserver(ContentObserver contentObserver) {
        getContentResolver().unregisterContentObserver(contentObserver);
    }

    default void registerContentObserverForUser(CommunalSettingCondition.AnonymousClass1 r3) {
        registerContentObserverForUser(getUriFor("communal_mode_enabled"), false, r3, 0);
    }

    default void registerContentObserverForUser(Uri uri, boolean z, ContentObserver contentObserver, int i) {
        getContentResolver().registerContentObserver(uri, z, contentObserver, i);
    }
}
