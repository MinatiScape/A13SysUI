package com.android.keyguard;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Bundle;
import android.util.IndentingPrintWriter;
import android.util.Log;
import android.util.Pair;
import com.android.keyguard.CarrierTextManager;
import com.android.systemui.communal.CommunalHostView;
import com.android.systemui.communal.CommunalHostViewController;
import com.android.systemui.communal.CommunalStateController;
import com.android.systemui.statusbar.RemoteInputController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.util.condition.Monitor;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import com.google.android.systemui.assist.uihints.edgelights.mode.FulfillPerimeter;
import com.google.android.systemui.reversecharging.ReverseChargingController;
import com.google.common.util.concurrent.ListenableFuture;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class CarrierTextManager$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;
    public final /* synthetic */ Object f$1;

    public /* synthetic */ CarrierTextManager$$ExternalSyntheticLambda2(Object obj, Object obj2, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
        this.f$1 = obj2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        int i;
        String str;
        boolean z = false;
        switch (this.$r8$classId) {
            case 0:
                CarrierTextManager carrierTextManager = (CarrierTextManager) this.f$0;
                Objects.requireNonNull(carrierTextManager);
                carrierTextManager.handleSetListening((CarrierTextManager.CarrierTextCallback) this.f$1);
                return;
            case 1:
                CommunalHostViewController communalHostViewController = (CommunalHostViewController) this.f$0;
                CommunalHostViewController.ShowRequest showRequest = (CommunalHostViewController.ShowRequest) this.f$1;
                boolean z2 = CommunalHostViewController.DEBUG;
                Objects.requireNonNull(communalHostViewController);
                if (CommunalHostViewController.DEBUG) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("showSource. currentSource:");
                    m.append(showRequest.getSource());
                    Log.d("CommunalController", m.toString());
                }
                Objects.requireNonNull(showRequest);
                if (showRequest.mShouldShow) {
                    ((CommunalHostView) communalHostViewController.mView).removeAllViews();
                    ((CommunalHostView) communalHostViewController.mView).setVisibility(0);
                    ((CommunalHostView) communalHostViewController.mView).getContext();
                    ListenableFuture requestCommunalView = showRequest.getSource().requestCommunalView();
                    if (requestCommunalView == null) {
                        Log.e("CommunalController", "could not request communal view");
                        return;
                    } else {
                        requestCommunalView.addListener(new CarrierTextManager$$ExternalSyntheticLambda1(communalHostViewController, requestCommunalView, 2), communalHostViewController.mMainExecutor);
                        return;
                    }
                } else {
                    ((CommunalHostView) communalHostViewController.mView).removeAllViews();
                    ((CommunalHostView) communalHostViewController.mView).setVisibility(4);
                    CommunalStateController communalStateController = communalHostViewController.mCommunalStateController;
                    Objects.requireNonNull(communalStateController);
                    if (communalStateController.mCommunalViewShowing) {
                        communalStateController.mCommunalViewShowing = false;
                        Iterator it = new ArrayList(communalStateController.mCallbacks).iterator();
                        while (it.hasNext()) {
                            ((CommunalStateController.Callback) it.next()).onCommunalViewShowingChanged();
                        }
                        return;
                    }
                    return;
                }
            case 2:
                RemoteInputController remoteInputController = (RemoteInputController) this.f$0;
                IndentingPrintWriter indentingPrintWriter = (IndentingPrintWriter) this.f$1;
                boolean z3 = RemoteInputController.ENABLE_REMOTE_INPUT;
                Objects.requireNonNull(remoteInputController);
                Iterator<Pair<WeakReference<NotificationEntry>, Object>> it2 = remoteInputController.mOpen.iterator();
                while (it2.hasNext()) {
                    NotificationEntry notificationEntry = (NotificationEntry) ((WeakReference) it2.next().first).get();
                    if (notificationEntry == null) {
                        str = "???";
                    } else {
                        str = notificationEntry.mKey;
                    }
                    indentingPrintWriter.println(str);
                }
                return;
            case 3:
                Monitor monitor = (Monitor) this.f$0;
                Objects.requireNonNull(monitor);
                monitor.addCallbackLocked((Monitor.Callback) this.f$1);
                return;
            case 4:
                FulfillPerimeter.AnonymousClass1 r0 = (FulfillPerimeter.AnonymousClass1) this.f$0;
                int i2 = FulfillPerimeter.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r0);
                ((EdgeLightsView) this.f$1).commitModeTransition(FulfillPerimeter.this.mNextMode);
                return;
            default:
                ReverseChargingController reverseChargingController = (ReverseChargingController) this.f$0;
                Bundle bundle = (Bundle) this.f$1;
                if (ReverseChargingController.DEBUG) {
                    Objects.requireNonNull(reverseChargingController);
                    StringBuilder sb = new StringBuilder();
                    sb.append("onReverseInformationChangedOnMainThread(): rtx=");
                    if (bundle.getInt("key_rtx_mode") == 1) {
                        i = 1;
                    } else {
                        i = 0;
                    }
                    sb.append(i);
                    sb.append(" wlc=");
                    sb.append(reverseChargingController.mWirelessCharging ? 1 : 0);
                    sb.append(" mName=");
                    sb.append(reverseChargingController.mName);
                    sb.append(" bundle=");
                    sb.append(bundle.toString());
                    sb.append(" this=");
                    sb.append(reverseChargingController);
                    Log.d("ReverseChargingControl", sb.toString());
                }
                if (reverseChargingController.mWirelessCharging && reverseChargingController.mName != null) {
                    if (bundle.getInt("key_rtx_mode") == 1) {
                        z = true;
                    }
                    reverseChargingController.mReverse = z;
                    reverseChargingController.mRtxLevel = bundle.getInt("key_rtx_level");
                    reverseChargingController.fireReverseChanged();
                    return;
                }
                return;
        }
    }
}
