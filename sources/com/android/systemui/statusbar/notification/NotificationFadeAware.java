package com.android.systemui.statusbar.notification;

import android.view.View;
/* loaded from: classes.dex */
public interface NotificationFadeAware {

    /* loaded from: classes.dex */
    public interface FadeOptimizedNotification extends NotificationFadeAware {
        boolean isNotificationFaded();
    }

    void setNotificationFaded(boolean z);

    static void setLayerTypeForFaded(View view, boolean z) {
        int i;
        if (view != null) {
            if (z) {
                i = 2;
            } else {
                i = 0;
            }
            view.setLayerType(i, null);
        }
    }
}
