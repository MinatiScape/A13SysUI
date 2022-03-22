package com.android.systemui.statusbar.connectivity;

import androidx.leanback.R$layout;
import com.android.settingslib.SignalIcon$IconGroup;
/* loaded from: classes.dex */
public final class WifiIcons {
    public static final SignalIcon$IconGroup UNMERGED_WIFI;
    public static final int WIFI_LEVEL_COUNT;

    static {
        int[][] iArr = {new int[]{2131232198, 2131232199, 2131232200, 2131232201, 2131232202}, new int[]{17302891, 17302892, 17302893, 17302894, 17302895}};
        WIFI_LEVEL_COUNT = iArr[0].length;
        UNMERGED_WIFI = new SignalIcon$IconGroup("Wi-Fi Icons", iArr, iArr, R$layout.WIFI_CONNECTION_STRENGTH, 17302891, 17302891, 17302891, 17302891, 2131951764);
    }
}
