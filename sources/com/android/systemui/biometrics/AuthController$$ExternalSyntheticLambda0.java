package com.android.systemui.biometrics;

import com.android.wm.shell.startingsurface.StartingSurfaceDrawer;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AuthController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ AuthController$$ExternalSyntheticLambda0(Object obj, int i, int i2) {
        this.$r8$classId = i2;
        this.f$0 = obj;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                AuthController authController = (AuthController) this.f$0;
                int i = this.f$1;
                Objects.requireNonNull(authController);
                AuthDialog authDialog = authController.mCurrentDialog;
                String string = authController.mContext.getString(17040291);
                AuthContainerView authContainerView = (AuthContainerView) authDialog;
                Objects.requireNonNull(authContainerView);
                authContainerView.mBiometricView.onAuthenticationFailed(i, string);
                return;
            default:
                StartingSurfaceDrawer startingSurfaceDrawer = (StartingSurfaceDrawer) this.f$0;
                int i2 = this.f$1;
                Objects.requireNonNull(startingSurfaceDrawer);
                startingSurfaceDrawer.onAppSplashScreenViewRemoved(i2, false);
                return;
        }
    }
}
