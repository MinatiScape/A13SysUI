package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
/* loaded from: classes.dex */
public final class TouchOutsideHandler implements NgaMessageHandler.ConfigInfoListener {
    public PendingIntent mTouchOutside;

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ConfigInfoListener
    public final void onConfigInfo(NgaMessageHandler.ConfigInfo configInfo) {
        this.mTouchOutside = configInfo.onTouchOutside;
    }
}
