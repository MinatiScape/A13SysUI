package com.android.systemui.media.taptotransfer.common;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaTttChipState.kt */
/* loaded from: classes.dex */
public class MediaTttChipState {
    public final String appPackageName;

    public Drawable getAppIcon(Context context) {
        if (this.appPackageName == null) {
            return null;
        }
        try {
            return context.getPackageManager().getApplicationIcon(this.appPackageName);
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(MediaTttChipStateKt.TAG, Intrinsics.stringPlus("Cannot find icon for package ", this.appPackageName), e);
            return null;
        }
    }

    public String getAppName(Context context) {
        if (this.appPackageName == null) {
            return null;
        }
        try {
            return context.getPackageManager().getApplicationInfo(this.appPackageName, PackageManager.ApplicationInfoFlags.of(0L)).loadLabel(context.getPackageManager()).toString();
        } catch (PackageManager.NameNotFoundException e) {
            Log.w(MediaTttChipStateKt.TAG, Intrinsics.stringPlus("Cannot find name for package ", this.appPackageName), e);
            return null;
        }
    }

    public MediaTttChipState(String str) {
        this.appPackageName = str;
    }
}
