package com.android.systemui.statusbar.connectivity;

import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionInfo;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.ScreenDecorations$2$$ExternalSyntheticLambda1;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.util.condition.Monitor$$ExternalSyntheticLambda1;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class CallbackHandler extends Handler implements NetworkController.EmergencyListener, SignalCallback {
    public static final SimpleDateFormat SSDF = new SimpleDateFormat("MM-dd HH:mm:ss.SSS");
    public int mHistoryIndex;
    public String mLastCallback;
    public final ArrayList<NetworkController.EmergencyListener> mEmergencyListeners = new ArrayList<>();
    public final ArrayList<SignalCallback> mSignalCallbacks = new ArrayList<>();
    public final String[] mHistory = new String[64];

    @Override // com.android.systemui.statusbar.connectivity.NetworkController.EmergencyListener
    public final void setEmergencyCallsOnly(boolean z) {
        obtainMessage(0, z ? 1 : 0, 0).sendToTarget();
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setMobileDataEnabled(boolean z) {
        obtainMessage(5, z ? 1 : 0, 0).sendToTarget();
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setNoSims(boolean z, boolean z2) {
        obtainMessage(2, z ? 1 : 0, z2 ? 1 : 0).sendToTarget();
    }

    @Override // android.os.Handler
    public final void handleMessage(Message message) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4;
        switch (message.what) {
            case 0:
                Iterator<NetworkController.EmergencyListener> it = this.mEmergencyListeners.iterator();
                while (it.hasNext()) {
                    NetworkController.EmergencyListener next = it.next();
                    if (message.arg1 != 0) {
                        z = true;
                    } else {
                        z = false;
                    }
                    next.setEmergencyCallsOnly(z);
                }
                return;
            case 1:
                Iterator<SignalCallback> it2 = this.mSignalCallbacks.iterator();
                while (it2.hasNext()) {
                    it2.next().setSubs((List) message.obj);
                }
                return;
            case 2:
                Iterator<SignalCallback> it3 = this.mSignalCallbacks.iterator();
                while (it3.hasNext()) {
                    SignalCallback next2 = it3.next();
                    if (message.arg1 != 0) {
                        z2 = true;
                    } else {
                        z2 = false;
                    }
                    if (message.arg2 != 0) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    next2.setNoSims(z2, z3);
                }
                return;
            case 3:
                Iterator<SignalCallback> it4 = this.mSignalCallbacks.iterator();
                while (it4.hasNext()) {
                    it4.next().setEthernetIndicators((IconState) message.obj);
                }
                return;
            case 4:
                Iterator<SignalCallback> it5 = this.mSignalCallbacks.iterator();
                while (it5.hasNext()) {
                    it5.next().setIsAirplaneMode((IconState) message.obj);
                }
                return;
            case 5:
                Iterator<SignalCallback> it6 = this.mSignalCallbacks.iterator();
                while (it6.hasNext()) {
                    SignalCallback next3 = it6.next();
                    if (message.arg1 != 0) {
                        z4 = true;
                    } else {
                        z4 = false;
                    }
                    next3.setMobileDataEnabled(z4);
                }
                return;
            case FalsingManager.VERSION /* 6 */:
                if (message.arg1 != 0) {
                    this.mEmergencyListeners.add((NetworkController.EmergencyListener) message.obj);
                    return;
                } else {
                    this.mEmergencyListeners.remove((NetworkController.EmergencyListener) message.obj);
                    return;
                }
            case 7:
                if (message.arg1 != 0) {
                    this.mSignalCallbacks.add((SignalCallback) message.obj);
                    return;
                } else {
                    this.mSignalCallbacks.remove((SignalCallback) message.obj);
                    return;
                }
            default:
                return;
        }
    }

    public final void recordLastCallback(String str) {
        String[] strArr = this.mHistory;
        int i = this.mHistoryIndex;
        strArr[i] = str;
        this.mHistoryIndex = (i + 1) % 64;
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setCallIndicator(final IconState iconState, final int i) {
        String str = "setCallIndicator: statusIcon=" + iconState + ",subId=" + i;
        if (!str.equals(this.mLastCallback)) {
            this.mLastCallback = str;
            recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + str + ",");
        }
        post(new Runnable() { // from class: com.android.systemui.statusbar.connectivity.CallbackHandler$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CallbackHandler callbackHandler = CallbackHandler.this;
                IconState iconState2 = iconState;
                int i2 = i;
                Objects.requireNonNull(callbackHandler);
                Iterator<SignalCallback> it = callbackHandler.mSignalCallbacks.iterator();
                while (it.hasNext()) {
                    it.next().setCallIndicator(iconState2, i2);
                }
            }
        });
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setConnectivityStatus(final boolean z, final boolean z2, final boolean z3) {
        String str = "setConnectivityStatus: noDefaultNetwork=" + z + ",noValidatedNetwork=" + z2 + ",noNetworksAvailable=" + z3;
        if (!str.equals(this.mLastCallback)) {
            this.mLastCallback = str;
            recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + str + ",");
        }
        post(new Runnable() { // from class: com.android.systemui.statusbar.connectivity.CallbackHandler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CallbackHandler callbackHandler = CallbackHandler.this;
                boolean z4 = z;
                boolean z5 = z2;
                boolean z6 = z3;
                Objects.requireNonNull(callbackHandler);
                Iterator<SignalCallback> it = callbackHandler.mSignalCallbacks.iterator();
                while (it.hasNext()) {
                    it.next().setConnectivityStatus(z4, z5, z6);
                }
            }
        });
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setEthernetIndicators(IconState iconState) {
        recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + ",setEthernetIndicators: icon=" + iconState);
        obtainMessage(3, iconState).sendToTarget();
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setIsAirplaneMode(IconState iconState) {
        String str = "setIsAirplaneMode: icon=" + iconState;
        if (!str.equals(this.mLastCallback)) {
            this.mLastCallback = str;
            recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + str + ",");
        }
        obtainMessage(4, iconState).sendToTarget();
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
        recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + mobileDataIndicators);
        post(new Monitor$$ExternalSyntheticLambda1(this, mobileDataIndicators, 1));
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setWifiIndicators(WifiIndicators wifiIndicators) {
        recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + wifiIndicators);
        post(new ScreenDecorations$2$$ExternalSyntheticLambda1(this, wifiIndicators, 1));
    }

    @VisibleForTesting
    public CallbackHandler(Looper looper) {
        super(looper);
    }

    @Override // com.android.systemui.statusbar.connectivity.SignalCallback
    public final void setSubs(List<SubscriptionInfo> list) {
        String str;
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m("setSubs: ", "subs=");
        if (list == null) {
            str = "";
        } else {
            str = list.toString();
        }
        m.append(str);
        String sb = m.toString();
        if (!sb.equals(this.mLastCallback)) {
            this.mLastCallback = sb;
            recordLastCallback(SSDF.format(Long.valueOf(System.currentTimeMillis())) + "," + sb + ",");
        }
        obtainMessage(1, list).sendToTarget();
    }
}
