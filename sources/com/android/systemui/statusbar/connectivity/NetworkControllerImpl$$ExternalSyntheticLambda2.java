package com.android.systemui.statusbar.connectivity;

import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NetworkControllerImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ NetworkControllerImpl f$0;
    public final /* synthetic */ boolean f$1;

    public /* synthetic */ NetworkControllerImpl$$ExternalSyntheticLambda2(NetworkControllerImpl networkControllerImpl, boolean z) {
        this.f$0 = networkControllerImpl;
        this.f$1 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        NetworkControllerImpl networkControllerImpl = this.f$0;
        boolean z = this.f$1;
        Objects.requireNonNull(networkControllerImpl);
        networkControllerImpl.mUserSetup = z;
        for (int i = 0; i < networkControllerImpl.mMobileSignalControllers.size(); i++) {
            MobileSignalController valueAt = networkControllerImpl.mMobileSignalControllers.valueAt(i);
            boolean z2 = networkControllerImpl.mUserSetup;
            Objects.requireNonNull(valueAt);
            ((MobileState) valueAt.mCurrentState).userSetup = z2;
            valueAt.notifyListenersIfNecessary();
        }
    }
}
