package com.android.systemui.qs.tiles;

import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import androidx.leanback.R$raw;
import java.text.DecimalFormat;
/* loaded from: classes.dex */
public class DataUsageDetailView extends LinearLayout {
    public DataUsageDetailView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        new DecimalFormat("#.##");
    }

    @Override // android.view.View
    public final void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        R$raw.updateFontSize(this, 16908310, 2131166851);
        R$raw.updateFontSize(this, 2131429164, 2131166852);
        R$raw.updateFontSize(this, 2131429158, 2131166851);
        R$raw.updateFontSize(this, 2131429161, 2131166851);
        R$raw.updateFontSize(this, 2131429162, 2131166851);
        R$raw.updateFontSize(this, 2131429160, 2131166851);
    }
}
