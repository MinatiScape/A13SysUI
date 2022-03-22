package com.android.systemui.screenshot;

import com.android.systemui.screenshot.ScreenshotController;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenshotController$$ExternalSyntheticLambda3 implements ScreenshotController.ActionsReadyListener, NotificationLifetimeExtender.NotificationSafeToRemoveCallback, NotificationStackScrollLayout.ClearAllAnimationListener {
    public final /* synthetic */ Object f$0;

    @Override // com.android.systemui.screenshot.ScreenshotController.ActionsReadyListener
    public final void onActionsReady(ScreenshotController.SavedImageData savedImageData) {
        ((ScreenshotController) this.f$0).logSuccessOnActionsReady(savedImageData);
    }

    public /* synthetic */ ScreenshotController$$ExternalSyntheticLambda3(Object obj) {
        this.f$0 = obj;
    }

    public final void onSafeToRemove(String str) {
        NotificationEntryManager notificationEntryManager = (NotificationEntryManager) this.f$0;
        Objects.requireNonNull(notificationEntryManager);
        notificationEntryManager.removeNotification(str, notificationEntryManager.mLatestRankingMap, 0);
    }
}
