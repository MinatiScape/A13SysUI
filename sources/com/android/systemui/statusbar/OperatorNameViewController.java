package com.android.systemui.statusbar;

import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyManager;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.keyguard.KeyguardUpdateMonitorCallback;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.tuner.TunerService;
import com.android.systemui.util.ViewController;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class OperatorNameViewController extends ViewController<OperatorNameView> {
    public final DarkIconDispatcher mDarkIconDispatcher;
    public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final NetworkController mNetworkController;
    public final TelephonyManager mTelephonyManager;
    public final TunerService mTunerService;
    public final OperatorNameViewController$$ExternalSyntheticLambda0 mDarkReceiver = new DarkIconDispatcher.DarkReceiver() { // from class: com.android.systemui.statusbar.OperatorNameViewController$$ExternalSyntheticLambda0
        @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
        public final void onDarkChanged(ArrayList arrayList, float f, int i) {
            OperatorNameViewController operatorNameViewController = OperatorNameViewController.this;
            Objects.requireNonNull(operatorNameViewController);
            T t = operatorNameViewController.mView;
            ((OperatorNameView) t).setTextColor(DarkIconDispatcher.getTint(arrayList, t, i));
        }
    };
    public final AnonymousClass1 mSignalCallback = new SignalCallback() { // from class: com.android.systemui.statusbar.OperatorNameViewController.1
        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setIsAirplaneMode(IconState iconState) {
            OperatorNameViewController.this.update();
        }
    };
    public final OperatorNameViewController$$ExternalSyntheticLambda1 mTunable = new TunerService.Tunable() { // from class: com.android.systemui.statusbar.OperatorNameViewController$$ExternalSyntheticLambda1
        @Override // com.android.systemui.tuner.TunerService.Tunable
        public final void onTuningChanged(String str, String str2) {
            OperatorNameViewController operatorNameViewController = OperatorNameViewController.this;
            Objects.requireNonNull(operatorNameViewController);
            operatorNameViewController.update();
        }
    };
    public final AnonymousClass2 mKeyguardUpdateMonitorCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.systemui.statusbar.OperatorNameViewController.2
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onRefreshCarrierInfo() {
            OperatorNameViewController operatorNameViewController = OperatorNameViewController.this;
            ((OperatorNameView) operatorNameViewController.mView).updateText(operatorNameViewController.getSubInfos());
        }
    };

    /* loaded from: classes.dex */
    public static class Factory {
        public final DarkIconDispatcher mDarkIconDispatcher;
        public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        public final NetworkController mNetworkController;
        public final TelephonyManager mTelephonyManager;
        public final TunerService mTunerService;

        public Factory(DarkIconDispatcher darkIconDispatcher, NetworkController networkController, TunerService tunerService, TelephonyManager telephonyManager, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            this.mDarkIconDispatcher = darkIconDispatcher;
            this.mNetworkController = networkController;
            this.mTunerService = tunerService;
            this.mTelephonyManager = telephonyManager;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        }
    }

    /* loaded from: classes.dex */
    public static class SubInfo {
        public final CharSequence mCarrierName;
        public final ServiceState mServiceState;
        public final int mSimState;

        public SubInfo(CharSequence charSequence, int i, ServiceState serviceState) {
            this.mCarrierName = charSequence;
            this.mSimState = i;
            this.mServiceState = serviceState;
        }
    }

    public final ArrayList getSubInfos() {
        ArrayList arrayList = new ArrayList();
        Iterator it = this.mKeyguardUpdateMonitor.getFilteredSubscriptionInfo().iterator();
        while (it.hasNext()) {
            SubscriptionInfo subscriptionInfo = (SubscriptionInfo) it.next();
            int subscriptionId = subscriptionInfo.getSubscriptionId();
            CharSequence carrierName = subscriptionInfo.getCarrierName();
            int simState = this.mKeyguardUpdateMonitor.getSimState(subscriptionId);
            KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
            Objects.requireNonNull(keyguardUpdateMonitor);
            arrayList.add(new SubInfo(carrierName, simState, keyguardUpdateMonitor.mServiceStates.get(Integer.valueOf(subscriptionId))));
        }
        return arrayList;
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewAttached() {
        this.mDarkIconDispatcher.addDarkReceiver(this.mDarkReceiver);
        this.mNetworkController.addCallback(this.mSignalCallback);
        this.mTunerService.addTunable(this.mTunable, "show_operator_name");
        this.mKeyguardUpdateMonitor.registerCallback(this.mKeyguardUpdateMonitorCallback);
    }

    @Override // com.android.systemui.util.ViewController
    public final void onViewDetached() {
        this.mDarkIconDispatcher.removeDarkReceiver(this.mDarkReceiver);
        this.mNetworkController.removeCallback(this.mSignalCallback);
        this.mTunerService.removeTunable(this.mTunable);
        this.mKeyguardUpdateMonitor.removeCallback(this.mKeyguardUpdateMonitorCallback);
    }

    public final void update() {
        OperatorNameView operatorNameView = (OperatorNameView) this.mView;
        boolean z = true;
        if (this.mTunerService.getValue("show_operator_name", 1) == 0) {
            z = false;
        }
        operatorNameView.update(z, this.mTelephonyManager.isDataCapable(), getSubInfos());
    }

    /* JADX WARN: Type inference failed for: r1v1, types: [com.android.systemui.statusbar.OperatorNameViewController$$ExternalSyntheticLambda0] */
    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.OperatorNameViewController$1] */
    /* JADX WARN: Type inference failed for: r1v3, types: [com.android.systemui.statusbar.OperatorNameViewController$$ExternalSyntheticLambda1] */
    /* JADX WARN: Type inference failed for: r1v4, types: [com.android.systemui.statusbar.OperatorNameViewController$2] */
    public OperatorNameViewController(OperatorNameView operatorNameView, DarkIconDispatcher darkIconDispatcher, NetworkController networkController, TunerService tunerService, TelephonyManager telephonyManager, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        super(operatorNameView);
        this.mDarkIconDispatcher = darkIconDispatcher;
        this.mNetworkController = networkController;
        this.mTunerService = tunerService;
        this.mTelephonyManager = telephonyManager;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
    }
}
