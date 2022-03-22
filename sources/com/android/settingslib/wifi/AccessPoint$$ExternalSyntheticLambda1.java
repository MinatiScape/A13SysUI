package com.android.settingslib.wifi;

import android.app.ActivityManager;
import android.net.NetworkInfo;
import android.net.NetworkKey;
import android.net.wifi.WifiInfo;
import android.os.RemoteException;
import android.view.View;
import androidx.dynamicanimation.animation.DynamicAnimation;
import com.android.systemui.biometrics.UdfpsEnrollProgressBarDrawable;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tiles.dialog.InternetDialogController;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.OverviewProxyService$$ExternalSyntheticLambda5;
import com.android.systemui.shared.rotation.FloatingRotationButton;
import com.android.systemui.shared.rotation.RotationButton;
import com.android.systemui.statusbar.connectivity.WifiSignalController;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.PhoneStatusBarPolicy;
import com.android.systemui.statusbar.policy.UserSwitcherController;
import com.android.systemui.volume.VolumeDialogImpl;
import com.android.wifitrackerlib.WifiEntry;
import com.android.wm.shell.bubbles.animation.PhysicsAnimationLayout;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class AccessPoint$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Object f$0;

    public /* synthetic */ AccessPoint$$ExternalSyntheticLambda1(Object obj, int i) {
        this.$r8$classId = i;
        this.f$0 = obj;
    }

    @Override // java.lang.Runnable
    public final void run() {
        boolean z;
        switch (this.$r8$classId) {
            case 0:
                Objects.requireNonNull((AccessPoint) this.f$0);
                return;
            case 1:
                UdfpsEnrollProgressBarDrawable udfpsEnrollProgressBarDrawable = (UdfpsEnrollProgressBarDrawable) this.f$0;
                Objects.requireNonNull(udfpsEnrollProgressBarDrawable);
                udfpsEnrollProgressBarDrawable.updateState(0, udfpsEnrollProgressBarDrawable.mTotalSteps, false);
                return;
            case 2:
                OverviewProxyService overviewProxyService = (OverviewProxyService) this.f$0;
                Objects.requireNonNull(overviewProxyService);
                overviewProxyService.mStatusBarOptionalLazy.get().ifPresent(new OverviewProxyService$$ExternalSyntheticLambda5(overviewProxyService, 0));
                return;
            case 3:
                FloatingRotationButton floatingRotationButton = (FloatingRotationButton) this.f$0;
                Objects.requireNonNull(floatingRotationButton);
                RotationButton.RotationButtonUpdatesCallback rotationButtonUpdatesCallback = floatingRotationButton.mUpdatesCallback;
                if (rotationButtonUpdatesCallback != null && floatingRotationButton.mIsShowing) {
                    NavigationBarView.this.notifyActiveTouchRegions();
                    return;
                }
                return;
            case 4:
                WifiSignalController wifiSignalController = (WifiSignalController) this.f$0;
                Objects.requireNonNull(wifiSignalController);
                WifiStatusTracker wifiStatusTracker = wifiSignalController.mWifiTracker;
                Objects.requireNonNull(wifiStatusTracker);
                if (wifiStatusTracker.mWifiManager != null) {
                    wifiStatusTracker.updateWifiState();
                    NetworkInfo networkInfo = wifiStatusTracker.mConnectivityManager.getNetworkInfo(1);
                    if (networkInfo == null || !networkInfo.isConnected()) {
                        z = false;
                    } else {
                        z = true;
                    }
                    wifiStatusTracker.connected = z;
                    String str = null;
                    wifiStatusTracker.mWifiInfo = null;
                    wifiStatusTracker.ssid = null;
                    if (z) {
                        WifiInfo connectionInfo = wifiStatusTracker.mWifiManager.getConnectionInfo();
                        wifiStatusTracker.mWifiInfo = connectionInfo;
                        if (connectionInfo != null) {
                            if (connectionInfo.isPasspointAp() || wifiStatusTracker.mWifiInfo.isOsuAp()) {
                                wifiStatusTracker.ssid = wifiStatusTracker.mWifiInfo.getPasspointProviderFriendlyName();
                            } else {
                                String ssid = wifiStatusTracker.mWifiInfo.getSSID();
                                if (ssid != null && !"<unknown ssid>".equals(ssid)) {
                                    str = ssid;
                                }
                                wifiStatusTracker.ssid = str;
                            }
                            wifiStatusTracker.isCarrierMerged = wifiStatusTracker.mWifiInfo.isCarrierMerged();
                            wifiStatusTracker.subId = wifiStatusTracker.mWifiInfo.getSubscriptionId();
                            int rssi = wifiStatusTracker.mWifiInfo.getRssi();
                            wifiStatusTracker.rssi = rssi;
                            wifiStatusTracker.level = wifiStatusTracker.mWifiManager.calculateSignalLevel(rssi);
                            NetworkKey createFromWifiInfo = NetworkKey.createFromWifiInfo(wifiStatusTracker.mWifiInfo);
                            if (wifiStatusTracker.mWifiNetworkScoreCache.getScoredNetwork(createFromWifiInfo) == null) {
                                wifiStatusTracker.mNetworkScoreManager.requestScores(new NetworkKey[]{createFromWifiInfo});
                            }
                        }
                    }
                    wifiStatusTracker.updateStatusLabel();
                }
                wifiSignalController.copyWifiStates();
                wifiSignalController.notifyListenersIfNecessary();
                return;
            case 5:
                AutoHideController autoHideController = (AutoHideController) this.f$0;
                Objects.requireNonNull(autoHideController);
                autoHideController.mNavigationBar.synchronizeState();
                return;
            case FalsingManager.VERSION /* 6 */:
                PhoneStatusBarPolicy.AnonymousClass1 r6 = (PhoneStatusBarPolicy.AnonymousClass1) this.f$0;
                int i = PhoneStatusBarPolicy.AnonymousClass1.$r8$clinit;
                Objects.requireNonNull(r6);
                PhoneStatusBarPolicy.this.mUserInfoController.reloadUserInfo();
                return;
            case 7:
                Runnable runnable = (Runnable) this.f$0;
                try {
                    ActivityManager.getService().resumeAppSwitches();
                } catch (RemoteException unused) {
                }
                runnable.run();
                return;
            case 8:
                UserSwitcherController.AnonymousClass6 r62 = (UserSwitcherController.AnonymousClass6) this.f$0;
                Objects.requireNonNull(r62);
                UserSwitcherController userSwitcherController = UserSwitcherController.this;
                userSwitcherController.mDeviceProvisionedController.removeCallback(userSwitcherController.mGuaranteeGuestPresentAfterProvisioned);
                return;
            case 9:
                VolumeDialogImpl volumeDialogImpl = (VolumeDialogImpl) this.f$0;
                String str2 = VolumeDialogImpl.TAG;
                Objects.requireNonNull(volumeDialogImpl);
                View view = volumeDialogImpl.mODICaptionsTooltipView;
                if (view != null) {
                    view.setVisibility(4);
                    return;
                }
                return;
            case 10:
                WifiEntry.ConnectActionListener connectActionListener = (WifiEntry.ConnectActionListener) this.f$0;
                Objects.requireNonNull(connectActionListener);
                WifiEntry wifiEntry = WifiEntry.this;
                WifiEntry.ConnectCallback connectCallback = wifiEntry.mConnectCallback;
                if (connectCallback != null && wifiEntry.mCalledConnect && wifiEntry.getConnectedState() == 0) {
                    ((InternetDialogController.WifiEntryConnectCallback) connectCallback).onConnectResult(2);
                    WifiEntry.this.mCalledConnect = false;
                    return;
                }
                return;
            default:
                PhysicsAnimationLayout.PhysicsPropertyAnimator physicsPropertyAnimator = (PhysicsAnimationLayout.PhysicsPropertyAnimator) this.f$0;
                Objects.requireNonNull(physicsPropertyAnimator);
                physicsPropertyAnimator.updateValueForChild(DynamicAnimation.TRANSLATION_X, physicsPropertyAnimator.mView, physicsPropertyAnimator.mCurrentPointOnPath.x);
                physicsPropertyAnimator.updateValueForChild(DynamicAnimation.TRANSLATION_Y, physicsPropertyAnimator.mView, physicsPropertyAnimator.mCurrentPointOnPath.y);
                return;
        }
    }
}
