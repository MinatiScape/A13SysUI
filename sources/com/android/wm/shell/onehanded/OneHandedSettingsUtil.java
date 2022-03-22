package com.android.wm.shell.onehanded;

import android.content.ContentResolver;
import android.database.ContentObserver;
import android.net.Uri;
import android.provider.Settings;
import com.android.internal.accessibility.AccessibilityShortcutController;
/* loaded from: classes.dex */
public final class OneHandedSettingsUtil {
    public static final String ONE_HANDED_MODE_TARGET_NAME = AccessibilityShortcutController.ONE_HANDED_COMPONENT_NAME.getShortClassName();

    public static boolean getSettingsOneHandedModeEnabled(ContentResolver contentResolver, int i) {
        if (Settings.Secure.getIntForUser(contentResolver, "one_handed_mode_enabled", 0, i) == 1) {
            return true;
        }
        return false;
    }

    public static boolean getSettingsSwipeToNotificationEnabled(ContentResolver contentResolver, int i) {
        if (Settings.Secure.getIntForUser(contentResolver, "swipe_bottom_to_notification_enabled", 0, i) == 1) {
            return true;
        }
        return false;
    }

    public static Uri registerSettingsKeyObserver(String str, ContentResolver contentResolver, ContentObserver contentObserver, int i) {
        Uri uriFor = Settings.Secure.getUriFor(str);
        if (!(contentResolver == null || uriFor == null)) {
            contentResolver.registerContentObserver(uriFor, false, contentObserver, i);
        }
        return uriFor;
    }
}
