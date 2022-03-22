package com.android.systemui.volume;

import android.view.View;
import com.android.systemui.plugins.VolumeDialog;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.statusbar.policy.ZenModeControllerImpl;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class VolumeDialogComponent$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ VolumeDialogComponent$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        switch (this.$r8$classId) {
            case 0:
                VolumeDialogComponent volumeDialogComponent = (VolumeDialogComponent) this.f$0;
                VolumeDialog volumeDialog = (VolumeDialog) obj;
                Objects.requireNonNull(volumeDialogComponent);
                VolumeDialog volumeDialog2 = volumeDialogComponent.mDialog;
                if (volumeDialog2 != null) {
                    volumeDialog2.destroy();
                }
                volumeDialogComponent.mDialog = volumeDialog;
                volumeDialog.init(2020, volumeDialogComponent.mVolumeDialogCallback);
                return;
            case 1:
                ((List) this.f$0).add((View) obj);
                return;
            default:
                int i = ZenModeControllerImpl.$r8$clinit;
                ((ZenModeController.Callback) obj).onConfigChanged();
                return;
        }
    }
}
