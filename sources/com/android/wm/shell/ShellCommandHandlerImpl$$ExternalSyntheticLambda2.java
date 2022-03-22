package com.android.wm.shell;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.provider.Settings;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.statusbar.StatusBarIconView;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;
import com.android.systemui.statusbar.policy.LocationController;
import com.android.systemui.statusbar.policy.LocationControllerImpl;
import com.android.wm.shell.splitscreen.SplitScreenController;
import com.google.android.systemui.elmyra.ElmyraService;
import com.google.android.systemui.elmyra.gates.Gate;
import com.google.android.systemui.elmyra.gates.WakeMode;
import java.util.Objects;
import java.util.function.Consumer;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class ShellCommandHandlerImpl$$ExternalSyntheticLambda2 implements Consumer {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ ShellCommandHandlerImpl$$ExternalSyntheticLambda2(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        boolean z;
        AnimationDrawable animationDrawable;
        boolean z2 = false;
        switch (this.$r8$classId) {
            case 0:
                ((SplitScreenController) obj).setSideStageVisibility(((Boolean) this.f$0).booleanValue());
                return;
            case 1:
                StatusBarIconView statusBarIconView = (StatusBarIconView) this.f$0;
                StatusBarIconView.AnonymousClass1 r0 = StatusBarIconView.ICON_APPEAR_AMOUNT;
                Objects.requireNonNull(statusBarIconView);
                statusBarIconView.mDozeAmount = ((Float) obj).floatValue();
                statusBarIconView.updateDecorColor();
                statusBarIconView.updateIconColor();
                float f = statusBarIconView.mDozeAmount;
                int i = (f > 0.0f ? 1 : (f == 0.0f ? 0 : -1));
                if (i == 0 || f == 1.0f) {
                    if (i == 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    if (statusBarIconView.mAllowAnimation != z) {
                        statusBarIconView.mAllowAnimation = z;
                        statusBarIconView.updateAnim();
                        if (!statusBarIconView.mAllowAnimation && (animationDrawable = statusBarIconView.mAnim) != null) {
                            if (statusBarIconView.getVisibility() == 0) {
                                z2 = true;
                            }
                            animationDrawable.setVisible(z2, true);
                            return;
                        }
                        return;
                    }
                    return;
                }
                return;
            case 2:
                KeyguardBottomAreaView keyguardBottomAreaView = (KeyguardBottomAreaView) this.f$0;
                Intent intent = KeyguardBottomAreaView.PHONE_INTENT;
                Objects.requireNonNull(keyguardBottomAreaView);
                ((ControlsListingController) obj).removeCallback(keyguardBottomAreaView.mListingCallback);
                return;
            case 3:
                LocationControllerImpl.H h = (LocationControllerImpl.H) this.f$0;
                int i2 = LocationControllerImpl.H.$r8$clinit;
                Objects.requireNonNull(h);
                boolean z3 = LocationControllerImpl.this.mAreActiveLocationRequests;
                ((LocationController.LocationChangeCallback) obj).onLocationActiveChanged();
                return;
            case 4:
                SplitScreenController.ISplitScreenImpl iSplitScreenImpl = (SplitScreenController.ISplitScreenImpl) this.f$0;
                SplitScreenController splitScreenController = (SplitScreenController) obj;
                int i3 = SplitScreenController.ISplitScreenImpl.$r8$clinit;
                Objects.requireNonNull(iSplitScreenImpl);
                iSplitScreenImpl.mListener.unregister();
                return;
            case 5:
                ElmyraService elmyraService = (ElmyraService) this.f$0;
                Gate gate = (Gate) obj;
                Objects.requireNonNull(elmyraService);
                ElmyraService.AnonymousClass2 r4 = elmyraService.mGateListener;
                Objects.requireNonNull(gate);
                gate.mListener = r4;
                return;
            default:
                WakeMode wakeMode = (WakeMode) this.f$0;
                Uri uri = (Uri) obj;
                Objects.requireNonNull(wakeMode);
                if (Settings.Secure.getIntForUser(wakeMode.mContext.getContentResolver(), "assist_gesture_wake_enabled", 1, -2) != 0) {
                    z2 = true;
                }
                if (z2 != wakeMode.mWakeSettingEnabled) {
                    wakeMode.mWakeSettingEnabled = z2;
                    wakeMode.notifyListener();
                    return;
                }
                return;
        }
    }
}
