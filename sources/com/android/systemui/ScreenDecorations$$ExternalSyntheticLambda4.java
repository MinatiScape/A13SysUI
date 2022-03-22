package com.android.systemui;

import android.graphics.Rect;
import android.os.SystemClock;
import android.view.View;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayout;
import com.android.systemui.statusbar.notification.stack.NotificationStackScrollLayoutController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.wm.shell.compatui.letterboxedu.LetterboxEduAnimationController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScreenDecorations$$ExternalSyntheticLambda4 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ScreenDecorations$$ExternalSyntheticLambda4(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ScreenDecorations screenDecorations = (ScreenDecorations) this.f$0;
                boolean z = ScreenDecorations.DEBUG_DISABLE_SCREEN_DECORATIONS;
                Objects.requireNonNull(screenDecorations);
                screenDecorations.mTunerService.addTunable(screenDecorations, "sysui_rounded_size");
                return;
            case 1:
                KeyguardViewMediator keyguardViewMediator = (KeyguardViewMediator) this.f$0;
                boolean z2 = KeyguardViewMediator.DEBUG;
                Objects.requireNonNull(keyguardViewMediator);
                keyguardViewMediator.handleStartKeyguardExitAnimation(SystemClock.uptimeMillis() + keyguardViewMediator.mHideAnimation.getStartOffset(), keyguardViewMediator.mHideAnimation.getDuration(), null, null, null, null);
                return;
            case 2:
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this.f$0;
                Rect rect = NotificationPanelViewController.M_DUMMY_DIRTY_RECT;
                Objects.requireNonNull(notificationPanelViewController);
                notificationPanelViewController.mHeadsUpAnimatingAway = false;
                NotificationStackScrollLayoutController notificationStackScrollLayoutController = notificationPanelViewController.mNotificationStackScrollLayoutController;
                Objects.requireNonNull(notificationStackScrollLayoutController);
                NotificationStackScrollLayout notificationStackScrollLayout = notificationStackScrollLayoutController.mView;
                Objects.requireNonNull(notificationStackScrollLayout);
                notificationStackScrollLayout.mHeadsUpAnimatingAway = false;
                notificationStackScrollLayout.updateClipping();
                notificationPanelViewController.updateVisibility();
                notificationPanelViewController.updatePanelExpansionAndVisibility();
                return;
            default:
                LetterboxEduAnimationController.AnonymousClass3 r0 = LetterboxEduAnimationController.DRAWABLE_ALPHA;
                ((View) this.f$0).setAlpha(1.0f);
                return;
        }
    }
}
