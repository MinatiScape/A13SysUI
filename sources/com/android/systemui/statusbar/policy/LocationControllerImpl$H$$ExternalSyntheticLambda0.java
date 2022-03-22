package com.android.systemui.statusbar.policy;

import com.android.systemui.statusbar.policy.LocationController;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.android.wm.shell.splitscreen.StageCoordinator;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class LocationControllerImpl$H$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ boolean f$0;

    public /* synthetic */ LocationControllerImpl$H$$ExternalSyntheticLambda0(boolean z, int i) {
        this.$r8$classId = i;
        this.f$0 = z;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                ((LocationController.LocationChangeCallback) obj).onLocationSettingsChanged();
                return;
            default:
                boolean z = this.f$0;
                SplitScreenController splitScreenController = (SplitScreenController) obj;
                int i = SplitScreenController.ISplitScreenImpl.$r8$clinit;
                Objects.requireNonNull(splitScreenController);
                StageCoordinator stageCoordinator = splitScreenController.mStageCoordinator;
                Objects.requireNonNull(stageCoordinator);
                stageCoordinator.mExitSplitScreenOnHide = z;
                return;
        }
    }
}
