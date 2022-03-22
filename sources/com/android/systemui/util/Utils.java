package com.android.systemui.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.provider.Settings;
import android.view.DisplayCutout;
import androidx.leanback.R$color;
import com.android.internal.policy.SystemBarUtils;
import java.util.ArrayList;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class Utils {
    public static boolean isHeadlessRemoteDisplayProvider(PackageManager packageManager, String str) {
        if (packageManager.checkPermission("android.permission.REMOTE_DISPLAY_PROVIDER", str) != 0) {
            return false;
        }
        Intent intent = new Intent("android.intent.action.MAIN");
        intent.addCategory("android.intent.category.LAUNCHER");
        intent.setPackage(str);
        return packageManager.queryIntentActivities(intent, 0).isEmpty();
    }

    public static int getStatusBarHeaderHeightKeyguard(Context context) {
        int i;
        int statusBarHeight = SystemBarUtils.getStatusBarHeight(context);
        DisplayCutout cutout = context.getDisplay().getCutout();
        if (cutout == null) {
            i = 0;
        } else {
            i = cutout.getWaterfallInsets().top;
        }
        return Math.max(statusBarHeight, context.getResources().getDimensionPixelSize(2131167059) + i);
    }

    public static boolean isGesturalModeOnDefaultDisplay(Context context, int i) {
        if (context.getDisplayId() != 0 || !R$color.isGesturalMode(i)) {
            return false;
        }
        return true;
    }

    public static void safeForeach(ArrayList arrayList, Consumer consumer) {
        int size = arrayList.size();
        while (true) {
            size--;
            if (size >= 0) {
                Object obj = arrayList.get(size);
                if (obj != null) {
                    consumer.accept(obj);
                }
            } else {
                return;
            }
        }
    }

    public static boolean shouldUseSplitNotificationShade(Resources resources) {
        return resources.getBoolean(2131034179);
    }

    public static boolean useMediaResumption(Context context) {
        int i = Settings.Secure.getInt(context.getContentResolver(), "qs_media_resumption", 1);
        if (!useQsMediaPlayer(context) || i <= 0) {
            return false;
        }
        return true;
    }

    public static boolean useQsMediaPlayer(Context context) {
        if (Settings.Global.getInt(context.getContentResolver(), "qs_media_controls", 1) > 0) {
            return true;
        }
        return false;
    }
}
