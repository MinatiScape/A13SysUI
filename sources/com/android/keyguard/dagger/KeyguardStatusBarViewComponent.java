package com.android.keyguard.dagger;

import com.android.systemui.statusbar.phone.KeyguardStatusBarView;
import com.android.systemui.statusbar.phone.KeyguardStatusBarViewController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
/* loaded from: classes.dex */
public interface KeyguardStatusBarViewComponent {

    /* loaded from: classes.dex */
    public interface Factory {
        KeyguardStatusBarViewComponent build(KeyguardStatusBarView keyguardStatusBarView, NotificationPanelViewController.NotificationPanelViewStateProvider notificationPanelViewStateProvider);
    }

    KeyguardStatusBarViewController getKeyguardStatusBarViewController();
}
