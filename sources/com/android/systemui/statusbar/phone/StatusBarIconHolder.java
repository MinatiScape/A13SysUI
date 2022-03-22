package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.graphics.drawable.Icon;
import android.os.UserHandle;
import com.android.internal.statusbar.StatusBarIcon;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
/* loaded from: classes.dex */
public final class StatusBarIconHolder {
    public StatusBarIcon mIcon;
    public StatusBarSignalPolicy.MobileIconState mMobileState;
    public StatusBarSignalPolicy.WifiIconState mWifiState;
    public int mType = 0;
    public int mTag = 0;

    public static StatusBarIconHolder fromCallIndicatorState(Context context, StatusBarSignalPolicy.CallIndicatorIconState callIndicatorIconState) {
        int i;
        String str;
        StatusBarIconHolder statusBarIconHolder = new StatusBarIconHolder();
        boolean z = callIndicatorIconState.isNoCalling;
        if (z) {
            i = callIndicatorIconState.noCallingResId;
        } else {
            i = callIndicatorIconState.callStrengthResId;
        }
        if (z) {
            str = callIndicatorIconState.noCallingDescription;
        } else {
            str = callIndicatorIconState.callStrengthDescription;
        }
        statusBarIconHolder.mIcon = new StatusBarIcon(UserHandle.SYSTEM, context.getPackageName(), Icon.createWithResource(context, i), 0, 0, str);
        statusBarIconHolder.mTag = callIndicatorIconState.subId;
        return statusBarIconHolder;
    }
}
