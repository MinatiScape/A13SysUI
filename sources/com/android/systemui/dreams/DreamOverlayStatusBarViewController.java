package com.android.systemui.dreams;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;
import android.widget.ImageView;
import com.android.systemui.battery.BatteryMeterView;
import com.android.systemui.battery.BatteryMeterViewController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.util.ViewController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class DreamOverlayStatusBarViewController extends ViewController<DreamOverlayStatusBarView> {
    public final BatteryController mBatteryController;
    public final BatteryMeterViewController mBatteryMeterViewController;
    public final ConnectivityManager mConnectivityManager;
    public final boolean mShowPercentAvailable;
    public final NetworkRequest mNetworkRequest = new NetworkRequest.Builder().clearCapabilities().addTransportType(1).build();
    public final AnonymousClass1 mNetworkCallback = new ConnectivityManager.NetworkCallback() { // from class: com.android.systemui.dreams.DreamOverlayStatusBarViewController.1
        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onAvailable(Network network) {
            DreamOverlayStatusBarViewController.this.onWifiAvailabilityChanged(true);
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            DreamOverlayStatusBarViewController.this.onWifiAvailabilityChanged(networkCapabilities.hasTransport(1));
        }

        @Override // android.net.ConnectivityManager.NetworkCallback
        public final void onLost(Network network) {
            DreamOverlayStatusBarViewController.this.onWifiAvailabilityChanged(false);
        }
    };
    public final AnonymousClass2 mBatteryStateChangeCallback = new BatteryController.BatteryStateChangeCallback() { // from class: com.android.systemui.dreams.DreamOverlayStatusBarViewController.2
        @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
        public final void onBatteryLevelChanged(int i, boolean z, boolean z2) {
            int i2;
            DreamOverlayStatusBarViewController dreamOverlayStatusBarViewController = DreamOverlayStatusBarViewController.this;
            Objects.requireNonNull(dreamOverlayStatusBarViewController);
            int i3 = 1;
            if (z2) {
                i2 = 2;
            } else {
                i2 = 1;
            }
            if (dreamOverlayStatusBarViewController.mBatteryStatus != i2) {
                dreamOverlayStatusBarViewController.mBatteryStatus = i2;
                DreamOverlayStatusBarView dreamOverlayStatusBarView = (DreamOverlayStatusBarView) dreamOverlayStatusBarViewController.mView;
                if (i2 != 2 || !dreamOverlayStatusBarViewController.mShowPercentAvailable) {
                    i3 = 0;
                }
                Objects.requireNonNull(dreamOverlayStatusBarView);
                BatteryMeterView batteryMeterView = dreamOverlayStatusBarView.mBatteryView;
                Objects.requireNonNull(batteryMeterView);
                batteryMeterView.setPercentShowMode(i3);
            }
        }
    };
    public int mWifiStatus = 0;
    public int mBatteryStatus = 0;

    public final void onWifiAvailabilityChanged(boolean z) {
        int i;
        boolean z2 = true;
        if (z) {
            i = 2;
        } else {
            i = 1;
        }
        if (this.mWifiStatus != i) {
            this.mWifiStatus = i;
            DreamOverlayStatusBarView dreamOverlayStatusBarView = (DreamOverlayStatusBarView) this.mView;
            int i2 = 0;
            if (i != 1) {
                z2 = false;
            }
            Objects.requireNonNull(dreamOverlayStatusBarView);
            ImageView imageView = dreamOverlayStatusBarView.mWifiStatusView;
            if (!z2) {
                i2 = 8;
            }
            imageView.setVisibility(i2);
        }
    }

    @Override // com.android.systemui.util.ViewController
    public final void onInit() {
        this.mBatteryMeterViewController.init();
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mBatteryController.addCallback(this.mBatteryStateChangeCallback);
        this.mConnectivityManager.registerNetworkCallback(this.mNetworkRequest, this.mNetworkCallback);
        ConnectivityManager connectivityManager = this.mConnectivityManager;
        NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(connectivityManager.getActiveNetwork());
        boolean z = true;
        if (networkCapabilities == null || !networkCapabilities.hasTransport(1)) {
            z = false;
        }
        onWifiAvailabilityChanged(z);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mBatteryController.removeCallback(this.mBatteryStateChangeCallback);
        this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
    }

    /* JADX WARN: Type inference failed for: r3v5, types: [com.android.systemui.dreams.DreamOverlayStatusBarViewController$1] */
    /* JADX WARN: Type inference failed for: r3v6, types: [com.android.systemui.dreams.DreamOverlayStatusBarViewController$2] */
    public DreamOverlayStatusBarViewController(Context context, DreamOverlayStatusBarView dreamOverlayStatusBarView, BatteryController batteryController, BatteryMeterViewController batteryMeterViewController, ConnectivityManager connectivityManager) {
        super(dreamOverlayStatusBarView);
        this.mBatteryController = batteryController;
        this.mBatteryMeterViewController = batteryMeterViewController;
        this.mConnectivityManager = connectivityManager;
        this.mShowPercentAvailable = context.getResources().getBoolean(17891383);
    }
}
