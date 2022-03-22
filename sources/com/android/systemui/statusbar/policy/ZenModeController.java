package com.android.systemui.statusbar.policy;

import android.app.NotificationManager;
import android.net.Uri;
import android.service.notification.ZenModeConfig;
/* loaded from: classes.dex */
public interface ZenModeController extends CallbackController<Callback> {

    /* loaded from: classes.dex */
    public interface Callback {
        default void onConfigChanged() {
        }

        default void onZenChanged(int i) {
        }
    }

    boolean areNotificationsHiddenInShade();

    ZenModeConfig getConfig();

    NotificationManager.Policy getConsolidatedPolicy();

    long getNextAlarm();

    int getZen();

    void setZen(int i, Uri uri, String str);
}
