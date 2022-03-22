package com.android.systemui.statusbar.policy;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.concurrency.DelayableExecutor;
import kotlin.jvm.functions.Function0;
/* compiled from: BatteryStateNotifier.kt */
/* loaded from: classes.dex */
public final class BatteryStateNotifier implements BatteryController.BatteryStateChangeCallback {
    public final Context context;
    public final BatteryController controller;
    public final DelayableExecutor delayableExecutor;
    public final NotificationManager noMan;
    public boolean stateUnknown;

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public final void onBatteryUnknownStateChanged(boolean z) {
        this.stateUnknown = z;
        if (z) {
            NotificationChannel notificationChannel = new NotificationChannel("battery_status", "Battery status", 3);
            this.noMan.createNotificationChannel(notificationChannel);
            this.noMan.notify("BatteryStateNotifier", 666, new Notification.Builder(this.context, notificationChannel.getId()).setAutoCancel(false).setContentTitle(this.context.getString(2131951923)).setContentText(this.context.getString(2131951922)).setSmallIcon(17303574).setContentIntent(PendingIntent.getActivity(this.context, 0, new Intent("android.intent.action.VIEW", Uri.parse(this.context.getString(2131952130))), 67108864)).setAutoCancel(true).setOngoing(true).build());
            return;
        }
        final BatteryStateNotifier$scheduleNotificationCancel$r$1 batteryStateNotifier$scheduleNotificationCancel$r$1 = new BatteryStateNotifier$scheduleNotificationCancel$r$1(this);
        this.delayableExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.policy.BatteryStateNotifierKt$sam$java_lang_Runnable$0
            @Override // java.lang.Runnable
            public final /* synthetic */ void run() {
                Function0.this.invoke();
            }
        }, 14400000L);
    }

    public BatteryStateNotifier(BatteryController batteryController, NotificationManager notificationManager, DelayableExecutor delayableExecutor, Context context) {
        this.controller = batteryController;
        this.noMan = notificationManager;
        this.delayableExecutor = delayableExecutor;
        this.context = context;
    }
}
