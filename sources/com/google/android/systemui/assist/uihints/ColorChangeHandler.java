package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
/* loaded from: classes.dex */
public final class ColorChangeHandler implements NgaMessageHandler.ConfigInfoListener {
    public final Context mContext;
    public boolean mIsDark;
    public PendingIntent mPendingIntent;

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ConfigInfoListener
    public final void onConfigInfo(NgaMessageHandler.ConfigInfo configInfo) {
        this.mPendingIntent = configInfo.onColorChanged;
        sendColor();
    }

    public final void sendColor() {
        if (this.mPendingIntent != null) {
            Intent intent = new Intent();
            intent.putExtra("is_dark", this.mIsDark);
            try {
                this.mPendingIntent.send(this.mContext, 0, intent);
            } catch (PendingIntent.CanceledException unused) {
                Log.w("ColorChangeHandler", "SysUI assist UI color changed PendingIntent canceled");
            }
        }
    }

    public ColorChangeHandler(Context context) {
        this.mContext = context;
    }
}
