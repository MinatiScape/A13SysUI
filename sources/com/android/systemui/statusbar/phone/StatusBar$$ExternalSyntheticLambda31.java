package com.android.systemui.statusbar.phone;

import android.graphics.Rect;
import android.os.Bundle;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda2;
import com.android.wm.shell.back.BackAnimationController;
import com.android.wm.shell.splitscreen.ISplitScreen;
import com.android.wm.shell.splitscreen.SplitScreen;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda31 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda31(Object obj, int i) {
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
                CreateUserActivity$$ExternalSyntheticLambda2 createUserActivity$$ExternalSyntheticLambda2 = new CreateUserActivity$$ExternalSyntheticLambda2(statusBar, 4);
                if (((Boolean) obj).booleanValue()) {
                    statusBar.mLightRevealScrim.post(createUserActivity$$ExternalSyntheticLambda2);
                    return;
                } else {
                    createUserActivity$$ExternalSyntheticLambda2.run();
                    return;
                }
            case 1:
                EdgeBackGestureHandler edgeBackGestureHandler = (EdgeBackGestureHandler) this.f$0;
                Objects.requireNonNull(edgeBackGestureHandler);
                edgeBackGestureHandler.mNavBarOverlayExcludedBounds.set((Rect) obj);
                return;
            case 2:
                ISplitScreen.Stub stub = (ISplitScreen.Stub) ((SplitScreen) obj).createExternalInterface();
                Objects.requireNonNull(stub);
                ((Bundle) this.f$0).putBinder("extra_shell_split_screen", stub);
                return;
            case 3:
                HeadsUpAppearanceController headsUpAppearanceController = (HeadsUpAppearanceController) this.f$0;
                Objects.requireNonNull(headsUpAppearanceController);
                ExpandableNotificationRow expandableNotificationRow = headsUpAppearanceController.mTrackedChild;
                headsUpAppearanceController.mTrackedChild = (ExpandableNotificationRow) obj;
                if (expandableNotificationRow != null) {
                    headsUpAppearanceController.updateHeader(expandableNotificationRow.mEntry);
                    return;
                }
                return;
            default:
                BackAnimationController.IBackAnimationImpl iBackAnimationImpl = (BackAnimationController.IBackAnimationImpl) this.f$0;
                BackAnimationController backAnimationController = (BackAnimationController) obj;
                int i = BackAnimationController.IBackAnimationImpl.$r8$clinit;
                Objects.requireNonNull(iBackAnimationImpl);
                BackAnimationController backAnimationController2 = iBackAnimationImpl.mController;
                int i2 = BackAnimationController.PROGRESS_THRESHOLD;
                Objects.requireNonNull(backAnimationController2);
                backAnimationController2.mBackToLauncherCallback = null;
                return;
        }
    }
}
