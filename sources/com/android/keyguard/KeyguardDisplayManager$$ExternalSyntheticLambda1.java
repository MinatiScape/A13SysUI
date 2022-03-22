package com.android.keyguard;

import android.media.MediaRouter;
import android.util.Log;
import com.android.systemui.dreams.DreamOverlayContainerViewController;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.qs.external.CustomTile;
import com.android.systemui.statusbar.connectivity.NetworkControllerImpl;
import com.android.systemui.statusbar.phone.StatusBar;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardDisplayManager$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ KeyguardDisplayManager$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                KeyguardDisplayManager keyguardDisplayManager = (KeyguardDisplayManager) this.f$0;
                Objects.requireNonNull(keyguardDisplayManager);
                keyguardDisplayManager.mMediaRouter = (MediaRouter) keyguardDisplayManager.mContext.getSystemService(MediaRouter.class);
                return;
            case 1:
                KeyguardVisibilityHelper keyguardVisibilityHelper = (KeyguardVisibilityHelper) this.f$0;
                Objects.requireNonNull(keyguardVisibilityHelper);
                keyguardVisibilityHelper.mKeyguardViewVisibilityAnimating = false;
                keyguardVisibilityHelper.mView.setVisibility(8);
                return;
            case 2:
                DreamOverlayContainerViewController.$r8$lambda$Oxvj_GJUc06UJC_m7GrRwIKFrUA((DreamOverlayContainerViewController) this.f$0);
                return;
            case 3:
                int i = CustomTile.$r8$clinit;
                ((CustomTile) this.f$0).updateDefaultTileAndIcon();
                return;
            case 4:
                NetworkControllerImpl networkControllerImpl = (NetworkControllerImpl) this.f$0;
                if (NetworkControllerImpl.DEBUG) {
                    Objects.requireNonNull(networkControllerImpl);
                    Log.d("NetworkController", ": mClearForceValidated");
                }
                networkControllerImpl.mForceCellularValidated = false;
                networkControllerImpl.updateConnectivity();
                return;
            default:
                ActivityStarter.Callback callback = (ActivityStarter.Callback) this.f$0;
                long[] jArr = StatusBar.CAMERA_LAUNCH_GESTURE_VIBRATION_TIMINGS;
                if (callback != null) {
                    callback.onActivityStarted(-96);
                    return;
                }
                return;
        }
    }
}
