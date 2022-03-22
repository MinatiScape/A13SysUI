package com.android.settingslib.mobile;

import android.content.Context;
import android.content.res.Resources;
import android.os.PersistableBundle;
import android.telephony.CarrierConfigManager;
import android.telephony.SubscriptionManager;
/* loaded from: classes.dex */
public final class MobileMappings {

    /* loaded from: classes.dex */
    public static class Config {
        public boolean hspaDataDistinguishable;
        public boolean showAtLeast3G = false;
        public boolean show4gFor3g = false;
        public boolean alwaysShowCdmaRssi = false;
        public boolean show4gForLte = false;
        public boolean hideLtePlus = false;
        public boolean alwaysShowDataRatIcon = false;

        public static Config readConfig(Context context) {
            Config config = new Config();
            Resources resources = context.getResources();
            config.showAtLeast3G = resources.getBoolean(2131034164);
            config.alwaysShowCdmaRssi = resources.getBoolean(17891364);
            config.hspaDataDistinguishable = resources.getBoolean(2131034139);
            SubscriptionManager.from(context);
            PersistableBundle configForSubId = ((CarrierConfigManager) context.getSystemService("carrier_config")).getConfigForSubId(SubscriptionManager.getDefaultDataSubscriptionId());
            if (configForSubId != null) {
                config.alwaysShowDataRatIcon = configForSubId.getBoolean("always_show_data_rat_icon_bool");
                config.show4gForLte = configForSubId.getBoolean("show_4g_for_lte_data_icon_bool");
                config.show4gFor3g = configForSubId.getBoolean("show_4g_for_3g_data_icon_bool");
                config.hideLtePlus = configForSubId.getBoolean("hide_lte_plus_data_icon_bool");
            }
            return config;
        }
    }

    public static String toDisplayIconKey(int i) {
        if (i == 1) {
            return Integer.toString(13) + "_CA";
        } else if (i == 2) {
            return Integer.toString(13) + "_CA_Plus";
        } else if (i == 3) {
            return Integer.toString(20);
        } else {
            if (i != 5) {
                return "unsupported";
            }
            return Integer.toString(20) + "_Plus";
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:19:0x00db  */
    /* JADX WARN: Removed duplicated region for block: B:23:0x00fa  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static java.util.HashMap mapIconSets(com.android.settingslib.mobile.MobileMappings.Config r10) {
        /*
            Method dump skipped, instructions count: 328
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.settingslib.mobile.MobileMappings.mapIconSets(com.android.settingslib.mobile.MobileMappings$Config):java.util.HashMap");
    }
}
