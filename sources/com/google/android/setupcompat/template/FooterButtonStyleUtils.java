package com.google.android.setupcompat.template;

import android.content.Context;
import android.content.res.ColorStateList;
import android.widget.Button;
import com.google.android.setupcompat.partnerconfig.PartnerConfig;
import com.google.android.setupcompat.partnerconfig.PartnerConfigHelper;
import java.util.HashMap;
/* loaded from: classes.dex */
public final class FooterButtonStyleUtils {
    public static final HashMap<Integer, ColorStateList> defaultTextColor = new HashMap<>();

    public static void updateButtonTextDisabledColorWithPartnerConfig(Context context, Button button, PartnerConfig partnerConfig) {
        if (PartnerConfigHelper.get(context).isPartnerConfigAvailable(partnerConfig)) {
            int color = PartnerConfigHelper.get(context).getColor(context, partnerConfig);
            if (color != 0) {
                button.setTextColor(ColorStateList.valueOf(color));
                return;
            }
            return;
        }
        HashMap<Integer, ColorStateList> hashMap = defaultTextColor;
        if (hashMap.containsKey(Integer.valueOf(button.getId()))) {
            button.setTextColor(hashMap.get(Integer.valueOf(button.getId())));
            return;
        }
        throw new IllegalStateException("There is no saved default color for button");
    }
}
