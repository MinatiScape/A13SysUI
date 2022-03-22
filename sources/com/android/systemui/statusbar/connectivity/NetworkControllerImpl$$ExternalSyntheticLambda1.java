package com.android.systemui.statusbar.connectivity;

import android.telephony.SubscriptionInfo;
import android.util.Log;
import android.view.View;
import com.android.systemui.statusbar.phone.fragment.CollapsedStatusBarFragment;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class NetworkControllerImpl$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ NetworkControllerImpl$$ExternalSyntheticLambda1(Object obj, int i, int i2) {
        this.$r8$classId = i2;
        this.f$0 = obj;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                NetworkControllerImpl networkControllerImpl = (NetworkControllerImpl) this.f$0;
                int i = this.f$1;
                Objects.requireNonNull(networkControllerImpl);
                int i2 = networkControllerImpl.mActiveMobileDataSubscription;
                boolean z2 = false;
                if (networkControllerImpl.mValidatedTransports.get(0)) {
                    SubscriptionInfo activeSubscriptionInfo = networkControllerImpl.mSubscriptionManager.getActiveSubscriptionInfo(i2);
                    SubscriptionInfo activeSubscriptionInfo2 = networkControllerImpl.mSubscriptionManager.getActiveSubscriptionInfo(i);
                    if (activeSubscriptionInfo == null || activeSubscriptionInfo2 == null || activeSubscriptionInfo.getGroupUuid() == null || !activeSubscriptionInfo.getGroupUuid().equals(activeSubscriptionInfo2.getGroupUuid())) {
                        z = false;
                    } else {
                        z = true;
                    }
                    if (z) {
                        z2 = true;
                    }
                }
                if (z2) {
                    if (NetworkControllerImpl.DEBUG) {
                        Log.d("NetworkController", ": mForceCellularValidated to true.");
                    }
                    networkControllerImpl.mForceCellularValidated = true;
                    networkControllerImpl.mReceiverHandler.removeCallbacks(networkControllerImpl.mClearForceValidated);
                    networkControllerImpl.mReceiverHandler.postDelayed(networkControllerImpl.mClearForceValidated, 2000L);
                }
                networkControllerImpl.mActiveMobileDataSubscription = i;
                networkControllerImpl.doUpdateMobileControllers();
                return;
            default:
                int i3 = this.f$1;
                int i4 = CollapsedStatusBarFragment.$r8$clinit;
                ((View) this.f$0).setVisibility(i3);
                return;
        }
    }
}
