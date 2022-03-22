package com.android.systemui.statusbar.phone;

import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import com.android.wm.shell.back.BackAnimation;
import com.android.wm.shell.back.IBackAnimation;
import com.google.android.systemui.gamedashboard.EntryPointController;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda32 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda32(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                StatusBar statusBar = (StatusBar) this.f$0;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.mBrightnessMirrorVisible = ((Boolean) obj).booleanValue();
                statusBar.updateScrimController();
                return;
            case 1:
                IBackAnimation.Stub stub = (IBackAnimation.Stub) ((BackAnimation) obj).createExternalInterface();
                Objects.requireNonNull(stub);
                ((Bundle) this.f$0).putBinder("extra_shell_back_animation", stub);
                return;
            case 2:
                HeadsUpAppearanceController headsUpAppearanceController = (HeadsUpAppearanceController) this.f$0;
                Objects.requireNonNull(headsUpAppearanceController);
                headsUpAppearanceController.hide((View) obj, 4, null);
                return;
            case 3:
                NotificationPanelViewController notificationPanelViewController = (NotificationPanelViewController) this.f$0;
                boolean booleanValue = ((Boolean) obj).booleanValue();
                Rect rect = NotificationPanelViewController.M_DUMMY_DIRTY_RECT;
                Objects.requireNonNull(notificationPanelViewController);
                if (notificationPanelViewController.mQs != null) {
                    if (booleanValue) {
                        notificationPanelViewController.mAnimateNextNotificationBounds = true;
                        notificationPanelViewController.mNotificationBoundsAnimationDuration = 360L;
                        notificationPanelViewController.mNotificationBoundsAnimationDelay = 0L;
                    }
                    notificationPanelViewController.setQSClippingBounds();
                    return;
                }
                return;
            default:
                EntryPointController entryPointController = (EntryPointController) this.f$0;
                Objects.requireNonNull(entryPointController);
                entryPointController.mInSplitScreen = ((Boolean) obj).booleanValue();
                return;
        }
    }
}
