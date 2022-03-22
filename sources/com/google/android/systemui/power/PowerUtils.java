package com.google.android.systemui.power;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.LocaleList;
import android.os.UserHandle;
import android.text.format.DateFormat;
import androidx.core.app.NotificationCompat$Builder;
import com.android.internal.annotations.VisibleForTesting;
import java.util.Locale;
/* loaded from: classes.dex */
public final class PowerUtils {
    public static PendingIntent createNormalChargingIntent(Context context, String str) {
        return PendingIntent.getBroadcastAsUser(context, 0, new Intent(str).setPackage(context.getPackageName()).setFlags(1342177280), 67108864, UserHandle.CURRENT);
    }

    public static void overrideNotificationAppName(Context context, NotificationCompat$Builder notificationCompat$Builder, int i) {
        Bundle bundle = new Bundle();
        bundle.putString("android.substName", context.getString(i));
        Bundle bundle2 = notificationCompat$Builder.mExtras;
        if (bundle2 == null) {
            notificationCompat$Builder.mExtras = new Bundle(bundle);
        } else {
            bundle2.putAll(bundle);
        }
    }

    public static String getCurrentTime(Context context, long j) {
        String str;
        Locale locale = getLocale(context);
        if (DateFormat.is24HourFormat(context)) {
            str = "HH:mm";
        } else {
            str = "h:m";
        }
        return DateFormat.format(DateFormat.getBestDateTimePattern(locale, str), j).toString().toUpperCase(locale);
    }

    @VisibleForTesting
    public static Locale getLocale(Context context) {
        LocaleList locales = context.getResources().getConfiguration().getLocales();
        if (locales == null || locales.isEmpty()) {
            return Locale.getDefault();
        }
        return locales.get(0);
    }
}
