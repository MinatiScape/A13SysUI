package com.google.android.systemui.elmyra.actions;

import com.android.systemui.assist.AssistManager;
import com.android.systemui.media.dialog.MediaOutputBaseDialog;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.OverviewProxyService$1$$ExternalSyntheticLambda16;
import com.android.systemui.statusbar.KeyguardIndicationController;
import com.android.systemui.statusbar.phone.AutoTileManager;
import com.android.systemui.statusbar.phone.DozeParameters;
import com.android.systemui.statusbar.phone.ScrimController;
import com.android.systemui.util.AlarmTimeout;
import com.google.android.systemui.elmyra.ElmyraService;
import com.google.android.systemui.elmyra.actions.Action;
import dagger.Lazy;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class Action$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ Action$$ExternalSyntheticLambda0(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        long j;
        switch (this.$r8$classId) {
            case 0:
                Action action = (Action) this.f$0;
                Objects.requireNonNull(action);
                Action.Listener listener = action.mListener;
                if (listener != null) {
                    ElmyraService.this.updateSensorListener();
                    return;
                }
                return;
            case 1:
                MediaOutputBaseDialog mediaOutputBaseDialog = (MediaOutputBaseDialog) this.f$0;
                int i = MediaOutputBaseDialog.$r8$clinit;
                Objects.requireNonNull(mediaOutputBaseDialog);
                mediaOutputBaseDialog.refresh();
                return;
            case 2:
                OverviewProxyService.AnonymousClass1 r3 = (OverviewProxyService.AnonymousClass1) this.f$0;
                int i2 = OverviewProxyService.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r3);
                OverviewProxyService.this.mStatusBarOptionalLazy.get().ifPresent(OverviewProxyService$1$$ExternalSyntheticLambda16.INSTANCE);
                return;
            case 3:
                KeyguardIndicationController keyguardIndicationController = (KeyguardIndicationController) this.f$0;
                Objects.requireNonNull(keyguardIndicationController);
                keyguardIndicationController.mWakeLock.setAcquired(false);
                return;
            case 4:
                AutoTileManager.AutoAddSetting autoAddSetting = (AutoTileManager.AutoAddSetting) this.f$0;
                int i3 = AutoTileManager.AutoAddSetting.$r8$clinit;
                Objects.requireNonNull(autoAddSetting);
                autoAddSetting.setListening(false);
                return;
            case 5:
                ScrimController scrimController = (ScrimController) this.f$0;
                boolean z = ScrimController.DEBUG;
                Objects.requireNonNull(scrimController);
                AlarmTimeout alarmTimeout = scrimController.mTimeTicker;
                DozeParameters dozeParameters = scrimController.mDozeParameters;
                Objects.requireNonNull(dozeParameters);
                if (dozeParameters.mControlScreenOffAnimation) {
                    j = 2500;
                } else {
                    j = dozeParameters.mAlwaysOnPolicy.wallpaperVisibilityDuration;
                }
                alarmTimeout.schedule(j);
                return;
            default:
                ((AssistManager) ((Lazy) this.f$0).get()).hideAssist();
                return;
        }
    }
}
