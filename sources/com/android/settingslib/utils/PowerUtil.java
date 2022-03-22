package com.android.settingslib.utils;

import android.content.Context;
import android.icu.text.DateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.concurrent.TimeUnit;
/* loaded from: classes.dex */
public final class PowerUtil {
    public static final /* synthetic */ int $r8$clinit = 0;
    public static final long FIFTEEN_MINUTES_MILLIS;
    public static final long ONE_DAY_MILLIS;
    public static final long ONE_HOUR_MILLIS = TimeUnit.HOURS.toMillis(1);
    public static final long SEVEN_MINUTES_MILLIS;
    public static final long TWO_DAYS_MILLIS;

    static {
        TimeUnit timeUnit = TimeUnit.MINUTES;
        SEVEN_MINUTES_MILLIS = timeUnit.toMillis(7L);
        FIFTEEN_MINUTES_MILLIS = timeUnit.toMillis(15L);
        TimeUnit timeUnit2 = TimeUnit.DAYS;
        ONE_DAY_MILLIS = timeUnit2.toMillis(1L);
        TWO_DAYS_MILLIS = timeUnit2.toMillis(2L);
        timeUnit.toMillis(1L);
    }

    public static String getBatteryRemainingShortStringFormatted(Context context, long j) {
        if (j <= 0) {
            return null;
        }
        if (j <= ONE_DAY_MILLIS) {
            return context.getString(2131953004, DateFormat.getInstanceForSkeleton(android.text.format.DateFormat.getTimeFormatString(context)).format(Date.from(Instant.ofEpochMilli(roundTimeToNearestThreshold(System.currentTimeMillis() + j, FIFTEEN_MINUTES_MILLIS)))));
        }
        return context.getString(2131953011, StringUtil.formatElapsedTime(context, roundTimeToNearestThreshold(j, ONE_HOUR_MILLIS), false));
    }

    public static long roundTimeToNearestThreshold(long j, long j2) {
        long abs = Math.abs(j);
        long abs2 = Math.abs(j2);
        long j3 = abs % abs2;
        if (j3 < abs2 / 2) {
            return abs - j3;
        }
        return (abs - j3) + abs2;
    }
}
