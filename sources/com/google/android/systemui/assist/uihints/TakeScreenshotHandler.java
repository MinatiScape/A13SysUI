package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.android.internal.util.ScreenshotHelper;
import com.android.systemui.statusbar.notification.InstantAppNotifier$$ExternalSyntheticLambda1;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
/* loaded from: classes.dex */
public final class TakeScreenshotHandler implements NgaMessageHandler.TakeScreenshotListener {
    public final Context mContext;
    public final Handler mHandler = new Handler(Looper.getMainLooper());
    public final ScreenshotHelper mScreenshotHelper;

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.TakeScreenshotListener
    public final void onTakeScreenshot(PendingIntent pendingIntent) {
        this.mScreenshotHelper.takeScreenshot(1, true, true, this.mHandler, new InstantAppNotifier$$ExternalSyntheticLambda1(this, pendingIntent, 1));
    }

    public TakeScreenshotHandler(Context context) {
        this.mContext = context;
        this.mScreenshotHelper = new ScreenshotHelper(context);
    }
}
