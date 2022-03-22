package com.android.wm.shell.pip;

import android.graphics.Rect;
import android.os.Bundle;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.screenshot.ScreenshotController$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.phone.NotificationIconContainer;
import com.android.wm.shell.bubbles.BubbleController;
import com.android.wm.shell.bubbles.BubbleEntry;
import com.google.android.systemui.assist.uihints.GreetingView;
import com.google.android.systemui.assist.uihints.TranscriptionController;
import java.util.List;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class PipTaskOrganizer$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ PipTaskOrganizer$$ExternalSyntheticLambda4(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                PipTaskOrganizer pipTaskOrganizer = (PipTaskOrganizer) this.f$0;
                Objects.requireNonNull(pipTaskOrganizer);
                pipTaskOrganizer.finishResizeForMenu((Rect) this.f$1);
                pipTaskOrganizer.sendOnPipTransitionFinished(2);
                return;
            case 1:
                OverviewProxyService.AnonymousClass1 r0 = (OverviewProxyService.AnonymousClass1) this.f$0;
                Bundle bundle = (Bundle) this.f$1;
                int i = OverviewProxyService.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r0);
                OverviewProxyService overviewProxyService = OverviewProxyService.this;
                Objects.requireNonNull(overviewProxyService);
                int size = overviewProxyService.mConnectionCallbacks.size();
                while (true) {
                    size--;
                    if (size >= 0) {
                        ((OverviewProxyService.OverviewProxyListener) overviewProxyService.mConnectionCallbacks.get(size)).startAssistant(bundle);
                    } else {
                        return;
                    }
                }
            case 2:
                NotificationRemoteInputManager.LegacyRemoteInputLifetimeExtender legacyRemoteInputLifetimeExtender = (NotificationRemoteInputManager.LegacyRemoteInputLifetimeExtender) this.f$0;
                String str = (String) this.f$1;
                Objects.requireNonNull(legacyRemoteInputLifetimeExtender);
                if (legacyRemoteInputLifetimeExtender.isNotificationKeptForRemoteInputHistory(str)) {
                    ((ScreenshotController$$ExternalSyntheticLambda3) legacyRemoteInputLifetimeExtender.mNotificationLifetimeFinishedCallback).onSafeToRemove(str);
                    return;
                }
                return;
            case 3:
                NotificationIconContainer notificationIconContainer = (NotificationIconContainer) this.f$0;
                NotificationIconContainer.AnonymousClass1 r1 = NotificationIconContainer.DOT_ANIMATION_PROPERTIES;
                Objects.requireNonNull(notificationIconContainer);
                notificationIconContainer.removeTransientView((StatusBarIconView) this.f$1);
                return;
            case 4:
                BubbleController bubbleController = (BubbleController) this.f$0;
                Objects.requireNonNull(bubbleController);
                for (BubbleEntry bubbleEntry : (List) this.f$1) {
                    if (BubbleController.canLaunchInTaskView(bubbleController.mContext, bubbleEntry)) {
                        bubbleController.updateBubble(bubbleEntry, true, false);
                    }
                }
                return;
            default:
                TranscriptionController transcriptionController = (TranscriptionController) this.f$0;
                Objects.requireNonNull(transcriptionController);
                GreetingView greetingView = (GreetingView) transcriptionController.mViewMap.get(TranscriptionController.State.GREETING);
                Objects.requireNonNull(greetingView);
                greetingView.setPadding(0, 0, 0, 0);
                greetingView.setText((String) this.f$1);
                greetingView.setVisibility(0);
                return;
        }
    }
}
