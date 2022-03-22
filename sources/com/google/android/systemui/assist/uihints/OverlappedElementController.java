package com.google.android.systemui.assist.uihints;

import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import com.android.systemui.Dependency;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.shared.recents.IOverviewProxy;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
import java.util.Objects;
/* loaded from: classes.dex */
public final class OverlappedElementController {
    public float mAlpha = 1.0f;
    public final Lazy<StatusBar> mStatusBarLazy;

    public final void setAlpha(float f) {
        float f2 = this.mAlpha;
        if (f2 != f) {
            if (f2 == 1.0f && f < 1.0f) {
                Log.v("OverlappedElementController", "Overlapped elements becoming transparent.");
            } else if (f2 < 1.0f && f == 1.0f) {
                Log.v("OverlappedElementController", "Overlapped elements becoming opaque.");
            }
            this.mAlpha = f;
            StatusBar statusBar = this.mStatusBarLazy.get();
            OverviewProxyService overviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
            float f3 = 1.0f - f;
            Objects.requireNonNull(overviewProxyService);
            try {
                IOverviewProxy iOverviewProxy = overviewProxyService.mOverviewProxy;
                if (iOverviewProxy != null) {
                    iOverviewProxy.onAssistantVisibilityChanged(f3);
                } else {
                    Log.e("OverviewProxyService", "Failed to get overview proxy for assistant visibility.");
                }
            } catch (RemoteException e) {
                Log.e("OverviewProxyService", "Failed to call notifyAssistantVisibilityChanged()", e);
            }
            Objects.requireNonNull(statusBar);
            View view = statusBar.mAmbientIndicationContainer;
            if (view != null) {
                view.setAlpha(f);
            }
            NotificationPanelViewController notificationPanelViewController = statusBar.mNotificationPanelViewController;
            Objects.requireNonNull(notificationPanelViewController);
            KeyguardBottomAreaView keyguardBottomAreaView = notificationPanelViewController.mKeyguardBottomArea;
            if (keyguardBottomAreaView != null) {
                keyguardBottomAreaView.setAffordanceAlpha(f);
            }
        }
    }

    public OverlappedElementController(Lazy<StatusBar> lazy) {
        this.mStatusBarLazy = lazy;
    }
}
