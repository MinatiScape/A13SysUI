package com.android.systemui.scrim;

import com.android.internal.colorextraction.ColorExtractor;
import com.android.systemui.model.SysUiState;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.wmshell.BubblesManager;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ScrimView$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ ScrimView$$ExternalSyntheticLambda2(StatusBar statusBar, boolean z) {
        this.$r8$classId = 1;
        this.f$0 = statusBar;
        this.f$2 = z;
        this.f$1 = "ShellStartingWindow";
    }

    public /* synthetic */ ScrimView$$ExternalSyntheticLambda2(Object obj, Object obj2, boolean z, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                ScrimView.$r8$lambda$0hg1lN64kAlaRiHts8Cd8Pck26U((ScrimView) this.f$0, (ColorExtractor.GradientColors) this.f$1, this.f$2);
                return;
            case 1:
                StatusBar statusBar = (StatusBar) this.f$0;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.mNotificationShadeWindowController.setRequestTopUi(this.f$2, (String) this.f$1);
                return;
            default:
                BubblesManager.AnonymousClass5 r0 = (BubblesManager.AnonymousClass5) this.f$0;
                SysUiState sysUiState = (SysUiState) this.f$1;
                boolean z = this.f$2;
                Objects.requireNonNull(r0);
                sysUiState.setFlag(8388608, z);
                sysUiState.commitUpdate(BubblesManager.this.mContext.getDisplayId());
                return;
        }
    }
}
