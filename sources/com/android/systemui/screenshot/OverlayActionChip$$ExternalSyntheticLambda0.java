package com.android.systemui.screenshot;

import android.app.PendingIntent;
import android.util.Log;
import android.view.View;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class OverlayActionChip$$ExternalSyntheticLambda0 implements View.OnClickListener {
    public final /* synthetic */ PendingIntent f$0;
    public final /* synthetic */ Runnable f$1;

    public /* synthetic */ OverlayActionChip$$ExternalSyntheticLambda0(PendingIntent pendingIntent, Runnable runnable) {
        this.f$0 = pendingIntent;
        this.f$1 = runnable;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        PendingIntent pendingIntent = this.f$0;
        Runnable runnable = this.f$1;
        int i = OverlayActionChip.$r8$clinit;
        try {
            pendingIntent.send();
            runnable.run();
        } catch (PendingIntent.CanceledException e) {
            Log.e("ScreenshotActionChip", "Intent cancelled", e);
        }
    }
}
