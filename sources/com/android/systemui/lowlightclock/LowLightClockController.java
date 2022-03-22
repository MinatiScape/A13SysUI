package com.android.systemui.lowlightclock;

import com.android.systemui.statusbar.phone.NotificationShadeWindowView;
/* loaded from: classes.dex */
public interface LowLightClockController {
    void attachLowLightClockView(NotificationShadeWindowView notificationShadeWindowView);

    void dozeTimeTick();

    boolean isLowLightClockEnabled();

    boolean showLowLightClock(boolean z);
}
