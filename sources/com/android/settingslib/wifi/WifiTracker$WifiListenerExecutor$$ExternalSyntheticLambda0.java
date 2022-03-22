package com.android.settingslib.wifi;

import androidx.recyclerview.R$dimen;
import com.android.settingslib.wifi.WifiTracker;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ WifiTracker.WifiListenerExecutor f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda0(WifiTracker.WifiListenerExecutor wifiListenerExecutor, int i) {
        this.f$0 = wifiListenerExecutor;
        this.f$1 = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        WifiTracker.WifiListenerExecutor wifiListenerExecutor = this.f$0;
        int i = this.f$1;
        Objects.requireNonNull(wifiListenerExecutor);
        WifiTracker.WifiListenerExecutor wifiListenerExecutor2 = (WifiTracker.WifiListenerExecutor) wifiListenerExecutor.mDelegatee;
        Objects.requireNonNull(wifiListenerExecutor2);
        R$dimen.postOnMainThread(new WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda1(wifiListenerExecutor2, String.format("Invoking onWifiStateChanged callback with state %d", Integer.valueOf(i)), new WifiTracker$WifiListenerExecutor$$ExternalSyntheticLambda0(wifiListenerExecutor2, i)));
    }
}
