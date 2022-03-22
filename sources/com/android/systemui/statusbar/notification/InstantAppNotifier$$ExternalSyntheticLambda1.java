package com.android.systemui.statusbar.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.net.Uri;
import android.os.UserHandle;
import android.util.Log;
import android.util.Pair;
import com.google.android.systemui.assist.uihints.TakeScreenshotHandler;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class InstantAppNotifier$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ InstantAppNotifier$$ExternalSyntheticLambda1(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                InstantAppNotifier instantAppNotifier = (InstantAppNotifier) this.f$0;
                Pair pair = (Pair) obj;
                Objects.requireNonNull(instantAppNotifier);
                instantAppNotifier.mCurrentNotifs.remove(pair);
                ((NotificationManager) this.f$1).cancelAsUser((String) pair.first, 7, new UserHandle(((Integer) pair.second).intValue()));
                return;
            default:
                TakeScreenshotHandler takeScreenshotHandler = (TakeScreenshotHandler) this.f$0;
                PendingIntent pendingIntent = (PendingIntent) this.f$1;
                Uri uri = (Uri) obj;
                Objects.requireNonNull(takeScreenshotHandler);
                if (pendingIntent != null) {
                    try {
                        Intent intent = new Intent();
                        if (uri != null) {
                            z = true;
                        } else {
                            z = false;
                        }
                        intent.putExtra("success", z);
                        intent.putExtra("uri", uri);
                        pendingIntent.send(takeScreenshotHandler.mContext, 0, intent);
                        return;
                    } catch (PendingIntent.CanceledException unused) {
                        Log.w("TakeScreenshotHandler", "Intent was cancelled");
                        return;
                    }
                } else {
                    return;
                }
        }
    }
}
