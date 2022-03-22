package com.android.systemui.statusbar;

import android.media.MediaRoute2Info;
import android.os.VibrationEffect;
import android.util.Log;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.media.dialog.MediaOutputController;
import com.android.wm.shell.common.DisplayController;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class VibratorHelper$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ VibratorHelper$$ExternalSyntheticLambda1(Object obj, int i, int i2) {
        this.$r8$classId = i2;
        this.f$0 = obj;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                VibratorHelper vibratorHelper = (VibratorHelper) this.f$0;
                int i = this.f$1;
                Objects.requireNonNull(vibratorHelper);
                vibratorHelper.mVibrator.vibrate(VibrationEffect.get(i, false), VibratorHelper.TOUCH_VIBRATION_ATTRIBUTES);
                return;
            case 1:
                MediaDevice mediaDevice = (MediaDevice) this.f$0;
                int i2 = this.f$1;
                boolean z = MediaOutputController.DEBUG;
                Objects.requireNonNull(mediaDevice);
                MediaRoute2Info mediaRoute2Info = mediaDevice.mRouteInfo;
                if (mediaRoute2Info == null) {
                    Log.w("MediaDevice", "Unable to set volume. RouteInfo is empty");
                    return;
                } else {
                    mediaDevice.mRouterManager.setRouteVolume(mediaRoute2Info, i2);
                    return;
                }
            default:
                DisplayController.DisplayWindowListenerImpl displayWindowListenerImpl = (DisplayController.DisplayWindowListenerImpl) this.f$0;
                int i3 = this.f$1;
                int i4 = DisplayController.DisplayWindowListenerImpl.$r8$clinit;
                Objects.requireNonNull(displayWindowListenerImpl);
                DisplayController.this.onDisplayAdded(i3);
                return;
        }
    }
}
