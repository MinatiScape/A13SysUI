package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.hardware.SensorAdditionalInfo;
import android.os.UserHandle;
import android.os.UserManager;
import com.android.settingslib.applications.ApplicationsState;
import com.android.systemui.dreams.DreamOverlayStateController;
import com.android.systemui.util.sensors.AsyncSensorManager;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class StatusBar$$ExternalSyntheticLambda20 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ StatusBar$$ExternalSyntheticLambda20(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                StatusBar statusBar = (StatusBar) this.f$0;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                Objects.requireNonNull(statusBar);
                statusBar.mMainExecutor.execute((Runnable) this.f$1);
                return;
            case 1:
                ApplicationsState.AppEntry appEntry = (ApplicationsState.AppEntry) this.f$0;
                Context context = (Context) this.f$1;
                Objects.requireNonNull(appEntry);
                if (UserManager.get(context).isManagedProfile(UserHandle.getUserId(appEntry.info.uid))) {
                    appEntry.labelDescription = context.getString(2131951829, appEntry.label);
                    return;
                } else {
                    appEntry.labelDescription = appEntry.label;
                    return;
                }
            case 2:
                DreamOverlayStateController dreamOverlayStateController = (DreamOverlayStateController) this.f$0;
                DreamOverlayStateController.Callback callback = (DreamOverlayStateController.Callback) this.f$1;
                int i = DreamOverlayStateController.$r8$clinit;
                Objects.requireNonNull(dreamOverlayStateController);
                Objects.requireNonNull(callback, "Callback must not be null. b/128895449");
                if (!dreamOverlayStateController.mCallbacks.contains(callback)) {
                    dreamOverlayStateController.mCallbacks.add(callback);
                    if (!dreamOverlayStateController.mComplications.isEmpty()) {
                        callback.onComplicationsChanged();
                        return;
                    }
                    return;
                }
                return;
            default:
                AsyncSensorManager asyncSensorManager = (AsyncSensorManager) this.f$0;
                int i2 = AsyncSensorManager.$r8$clinit;
                Objects.requireNonNull(asyncSensorManager);
                asyncSensorManager.mInner.setOperationParameter((SensorAdditionalInfo) this.f$1);
                return;
        }
    }
}
