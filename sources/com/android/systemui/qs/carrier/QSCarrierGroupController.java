package com.android.systemui.qs.carrier;

import android.content.Context;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.telephony.SubscriptionManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import androidx.leanback.R$layout;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline1;
import com.android.keyguard.CarrierTextManager;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticOutline1;
import com.android.settingslib.mobile.TelephonyIcons;
import com.android.systemui.flags.FeatureFlags;
import com.android.systemui.flags.Flags;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.qs.tiles.dialog.InternetDialog$$ExternalSyntheticLambda3;
import com.android.systemui.statusbar.connectivity.IconState;
import com.android.systemui.statusbar.connectivity.MobileDataIndicators;
import com.android.systemui.statusbar.connectivity.NetworkController;
import com.android.systemui.statusbar.connectivity.SignalCallback;
import com.android.systemui.util.CarrierConfigTracker;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda1;
import com.android.systemui.wmshell.WMShell$8$$ExternalSyntheticLambda0;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final class QSCarrierGroupController {
    public final ActivityStarter mActivityStarter;
    public final Handler mBgHandler;
    public final Callback mCallback;
    public final CarrierConfigTracker mCarrierConfigTracker;
    public final CarrierTextManager mCarrierTextManager;
    public boolean mIsSingleCarrier;
    public boolean mListening;
    public H mMainHandler;
    public final NetworkController mNetworkController;
    public final TextView mNoSimTextView;
    public OnSingleCarrierChangedListener mOnSingleCarrierChangedListener;
    public final boolean mProviderModel;
    public final SlotIndexResolver mSlotIndexResolver;
    public final CellSignalState[] mInfos = new CellSignalState[3];
    public View[] mCarrierDividers = new View[2];
    public QSCarrier[] mCarrierGroups = new QSCarrier[3];
    public int[] mLastSignalLevel = new int[3];
    public String[] mLastSignalLevelDescription = new String[3];
    public final AnonymousClass1 mSignalCallback = new SignalCallback() { // from class: com.android.systemui.qs.carrier.QSCarrierGroupController.1
        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setCallIndicator(IconState iconState, int i) {
            QSCarrierGroupController qSCarrierGroupController = QSCarrierGroupController.this;
            if (qSCarrierGroupController.mProviderModel) {
                int slotIndex = qSCarrierGroupController.getSlotIndex(i);
                if (slotIndex >= 3) {
                    GridLayoutManager$$ExternalSyntheticOutline1.m("setMobileDataIndicators - slot: ", slotIndex, "QSCarrierGroup");
                } else if (slotIndex == -1) {
                    KeyguardUpdateMonitor$$ExternalSyntheticOutline1.m("Invalid SIM slot index for subscription: ", i, "QSCarrierGroup");
                } else {
                    boolean callStrengthConfig = QSCarrierGroupController.this.mCarrierConfigTracker.getCallStrengthConfig(i);
                    int i2 = iconState.icon;
                    if (i2 == 2131232227) {
                        if (iconState.visible) {
                            QSCarrierGroupController qSCarrierGroupController2 = QSCarrierGroupController.this;
                            qSCarrierGroupController2.mInfos[slotIndex] = new CellSignalState(true, i2, iconState.contentDescription, "", false, qSCarrierGroupController2.mProviderModel);
                        } else if (callStrengthConfig) {
                            QSCarrierGroupController qSCarrierGroupController3 = QSCarrierGroupController.this;
                            qSCarrierGroupController3.mInfos[slotIndex] = new CellSignalState(true, qSCarrierGroupController3.mLastSignalLevel[slotIndex], qSCarrierGroupController3.mLastSignalLevelDescription[slotIndex], "", false, qSCarrierGroupController3.mProviderModel);
                        } else {
                            QSCarrierGroupController qSCarrierGroupController4 = QSCarrierGroupController.this;
                            qSCarrierGroupController4.mInfos[slotIndex] = new CellSignalState(true, 2131232235, "", "", false, qSCarrierGroupController4.mProviderModel);
                        }
                        QSCarrierGroupController.this.mMainHandler.obtainMessage(1).sendToTarget();
                        return;
                    }
                    QSCarrierGroupController qSCarrierGroupController5 = QSCarrierGroupController.this;
                    qSCarrierGroupController5.mLastSignalLevel[slotIndex] = i2;
                    String[] strArr = qSCarrierGroupController5.mLastSignalLevelDescription;
                    String str = iconState.contentDescription;
                    strArr[slotIndex] = str;
                    CellSignalState[] cellSignalStateArr = qSCarrierGroupController5.mInfos;
                    if (cellSignalStateArr[slotIndex].mobileSignalIconId != 2131232227) {
                        if (callStrengthConfig) {
                            cellSignalStateArr[slotIndex] = new CellSignalState(true, i2, str, "", false, qSCarrierGroupController5.mProviderModel);
                        } else {
                            cellSignalStateArr[slotIndex] = new CellSignalState(true, 2131232235, "", "", false, qSCarrierGroupController5.mProviderModel);
                        }
                        qSCarrierGroupController5.mMainHandler.obtainMessage(1).sendToTarget();
                    }
                }
            }
        }

        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setMobileDataIndicators(MobileDataIndicators mobileDataIndicators) {
            QSCarrierGroupController qSCarrierGroupController = QSCarrierGroupController.this;
            if (!qSCarrierGroupController.mProviderModel) {
                int slotIndex = qSCarrierGroupController.getSlotIndex(mobileDataIndicators.subId);
                if (slotIndex >= 3) {
                    GridLayoutManager$$ExternalSyntheticOutline1.m("setMobileDataIndicators - slot: ", slotIndex, "QSCarrierGroup");
                } else if (slotIndex == -1) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid SIM slot index for subscription: ");
                    m.append(mobileDataIndicators.subId);
                    Log.e("QSCarrierGroup", m.toString());
                } else {
                    CellSignalState[] cellSignalStateArr = QSCarrierGroupController.this.mInfos;
                    IconState iconState = mobileDataIndicators.statusIcon;
                    boolean z = iconState.visible;
                    int i = iconState.icon;
                    String str = iconState.contentDescription;
                    String charSequence = mobileDataIndicators.typeContentDescription.toString();
                    boolean z2 = mobileDataIndicators.roaming;
                    QSCarrierGroupController qSCarrierGroupController2 = QSCarrierGroupController.this;
                    cellSignalStateArr[slotIndex] = new CellSignalState(z, i, str, charSequence, z2, qSCarrierGroupController2.mProviderModel);
                    qSCarrierGroupController2.mMainHandler.obtainMessage(1).sendToTarget();
                }
            }
        }

        @Override // com.android.systemui.statusbar.connectivity.SignalCallback
        public final void setNoSims(boolean z, boolean z2) {
            if (z) {
                for (int i = 0; i < 3; i++) {
                    CellSignalState[] cellSignalStateArr = QSCarrierGroupController.this.mInfos;
                    cellSignalStateArr[i] = cellSignalStateArr[i].changeVisibility(false);
                }
            }
            QSCarrierGroupController.this.mMainHandler.obtainMessage(1).sendToTarget();
        }
    };

    /* loaded from: classes.dex */
    public static class Callback implements CarrierTextManager.CarrierTextCallback {
        public H mHandler;

        @Override // com.android.keyguard.CarrierTextManager.CarrierTextCallback
        public final void updateCarrierInfo(CarrierTextManager.CarrierTextCallbackInfo carrierTextCallbackInfo) {
            this.mHandler.obtainMessage(0, carrierTextCallbackInfo).sendToTarget();
        }

        public Callback(H h) {
            this.mHandler = h;
        }
    }

    /* loaded from: classes.dex */
    public static class H extends Handler {
        public Consumer<CarrierTextManager.CarrierTextCallbackInfo> mUpdateCarrierInfo;
        public Runnable mUpdateState;

        @Override // android.os.Handler
        public final void handleMessage(Message message) {
            int i = message.what;
            if (i == 0) {
                this.mUpdateCarrierInfo.accept((CarrierTextManager.CarrierTextCallbackInfo) message.obj);
            } else if (i != 1) {
                super.handleMessage(message);
            } else {
                this.mUpdateState.run();
            }
        }

        public H(Looper looper, QSCarrierGroupController$$ExternalSyntheticLambda0 qSCarrierGroupController$$ExternalSyntheticLambda0, WMShell$8$$ExternalSyntheticLambda0 wMShell$8$$ExternalSyntheticLambda0) {
            super(looper);
            this.mUpdateCarrierInfo = qSCarrierGroupController$$ExternalSyntheticLambda0;
            this.mUpdateState = wMShell$8$$ExternalSyntheticLambda0;
        }
    }

    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface OnSingleCarrierChangedListener {
        void onSingleCarrierChanged(boolean z);
    }

    @FunctionalInterface
    /* loaded from: classes.dex */
    public interface SlotIndexResolver {
        int getSlotIndex(int i);
    }

    /* loaded from: classes.dex */
    public static class Builder {
        public final ActivityStarter mActivityStarter;
        public final CarrierConfigTracker mCarrierConfigTracker;
        public final CarrierTextManager.Builder mCarrierTextControllerBuilder;
        public final Context mContext;
        public final FeatureFlags mFeatureFlags;
        public final Handler mHandler;
        public final Looper mLooper;
        public final NetworkController mNetworkController;
        public final SlotIndexResolver mSlotIndexResolver;
        public QSCarrierGroup mView;

        public Builder(ActivityStarter activityStarter, Handler handler, Looper looper, NetworkController networkController, CarrierTextManager.Builder builder, Context context, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags, SlotIndexResolver slotIndexResolver) {
            this.mActivityStarter = activityStarter;
            this.mHandler = handler;
            this.mLooper = looper;
            this.mNetworkController = networkController;
            this.mCarrierTextControllerBuilder = builder;
            this.mContext = context;
            this.mCarrierConfigTracker = carrierConfigTracker;
            this.mFeatureFlags = featureFlags;
            this.mSlotIndexResolver = slotIndexResolver;
        }
    }

    /* loaded from: classes.dex */
    public static class SubscriptionManagerSlotIndexResolver implements SlotIndexResolver {
        @Override // com.android.systemui.qs.carrier.QSCarrierGroupController.SlotIndexResolver
        public final int getSlotIndex(int i) {
            return SubscriptionManager.getSlotIndex(i);
        }
    }

    /* JADX WARN: Type inference failed for: r6v4, types: [com.android.systemui.qs.carrier.QSCarrierGroupController$1] */
    /* JADX WARN: Type inference failed for: r9v0, types: [com.android.systemui.qs.carrier.QSCarrierGroupController$$ExternalSyntheticLambda0] */
    public QSCarrierGroupController(QSCarrierGroup qSCarrierGroup, ActivityStarter activityStarter, Handler handler, Looper looper, NetworkController networkController, CarrierTextManager.Builder builder, Context context, CarrierConfigTracker carrierConfigTracker, FeatureFlags featureFlags, SlotIndexResolver slotIndexResolver) {
        boolean z = false;
        if (featureFlags.isEnabled(Flags.COMBINED_STATUS_BAR_SIGNAL_ICONS)) {
            this.mProviderModel = true;
        } else {
            this.mProviderModel = false;
        }
        this.mActivityStarter = activityStarter;
        this.mBgHandler = handler;
        this.mNetworkController = networkController;
        Objects.requireNonNull(builder);
        builder.mShowAirplaneMode = false;
        builder.mShowMissingSim = false;
        this.mCarrierTextManager = builder.build();
        this.mCarrierConfigTracker = carrierConfigTracker;
        this.mSlotIndexResolver = slotIndexResolver;
        InternetDialog$$ExternalSyntheticLambda3 internetDialog$$ExternalSyntheticLambda3 = new InternetDialog$$ExternalSyntheticLambda3(this, 2);
        qSCarrierGroup.setOnClickListener(internetDialog$$ExternalSyntheticLambda3);
        TextView textView = (TextView) qSCarrierGroup.findViewById(2131428498);
        this.mNoSimTextView = textView;
        textView.setOnClickListener(internetDialog$$ExternalSyntheticLambda3);
        H h = new H(looper, new Consumer() { // from class: com.android.systemui.qs.carrier.QSCarrierGroupController$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                QSCarrierGroupController qSCarrierGroupController = QSCarrierGroupController.this;
                CarrierTextManager.CarrierTextCallbackInfo carrierTextCallbackInfo = (CarrierTextManager.CarrierTextCallbackInfo) obj;
                Objects.requireNonNull(qSCarrierGroupController);
                if (!qSCarrierGroupController.mMainHandler.getLooper().isCurrentThread()) {
                    qSCarrierGroupController.mMainHandler.obtainMessage(0, carrierTextCallbackInfo).sendToTarget();
                    return;
                }
                qSCarrierGroupController.mNoSimTextView.setVisibility(8);
                if (carrierTextCallbackInfo.airplaneMode || !carrierTextCallbackInfo.anySimReady) {
                    for (int i = 0; i < 3; i++) {
                        CellSignalState[] cellSignalStateArr = qSCarrierGroupController.mInfos;
                        cellSignalStateArr[i] = cellSignalStateArr[i].changeVisibility(false);
                        QSCarrier qSCarrier = qSCarrierGroupController.mCarrierGroups[i];
                        Objects.requireNonNull(qSCarrier);
                        qSCarrier.mCarrierText.setText("");
                        qSCarrierGroupController.mCarrierGroups[i].setVisibility(8);
                    }
                    qSCarrierGroupController.mNoSimTextView.setText(carrierTextCallbackInfo.carrierText);
                    if (!TextUtils.isEmpty(carrierTextCallbackInfo.carrierText)) {
                        qSCarrierGroupController.mNoSimTextView.setVisibility(0);
                    }
                } else {
                    boolean[] zArr = new boolean[3];
                    if (carrierTextCallbackInfo.listOfCarriers.length == carrierTextCallbackInfo.subscriptionIds.length) {
                        for (int i2 = 0; i2 < 3 && i2 < carrierTextCallbackInfo.listOfCarriers.length; i2++) {
                            int slotIndex = qSCarrierGroupController.getSlotIndex(carrierTextCallbackInfo.subscriptionIds[i2]);
                            if (slotIndex >= 3) {
                                GridLayoutManager$$ExternalSyntheticOutline1.m("updateInfoCarrier - slot: ", slotIndex, "QSCarrierGroup");
                            } else if (slotIndex == -1) {
                                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid SIM slot index for subscription: ");
                                m.append(carrierTextCallbackInfo.subscriptionIds[i2]);
                                Log.e("QSCarrierGroup", m.toString());
                            } else {
                                CellSignalState[] cellSignalStateArr2 = qSCarrierGroupController.mInfos;
                                cellSignalStateArr2[slotIndex] = cellSignalStateArr2[slotIndex].changeVisibility(true);
                                zArr[slotIndex] = true;
                                QSCarrier qSCarrier2 = qSCarrierGroupController.mCarrierGroups[slotIndex];
                                String trim = carrierTextCallbackInfo.listOfCarriers[i2].toString().trim();
                                Objects.requireNonNull(qSCarrier2);
                                qSCarrier2.mCarrierText.setText(trim);
                                qSCarrierGroupController.mCarrierGroups[slotIndex].setVisibility(0);
                            }
                        }
                        for (int i3 = 0; i3 < 3; i3++) {
                            if (!zArr[i3]) {
                                CellSignalState[] cellSignalStateArr3 = qSCarrierGroupController.mInfos;
                                cellSignalStateArr3[i3] = cellSignalStateArr3[i3].changeVisibility(false);
                                qSCarrierGroupController.mCarrierGroups[i3].setVisibility(8);
                            }
                        }
                    } else {
                        Log.e("QSCarrierGroup", "Carrier information arrays not of same length");
                    }
                }
                qSCarrierGroupController.handleUpdateState();
            }
        }, new WMShell$8$$ExternalSyntheticLambda0(this, 2));
        this.mMainHandler = h;
        this.mCallback = new Callback(h);
        this.mCarrierGroups[0] = (QSCarrier) qSCarrierGroup.findViewById(2131427670);
        this.mCarrierGroups[1] = (QSCarrier) qSCarrierGroup.findViewById(2131427671);
        this.mCarrierGroups[2] = (QSCarrier) qSCarrierGroup.findViewById(2131427672);
        this.mCarrierDividers[0] = qSCarrierGroup.findViewById(2131428643);
        this.mCarrierDividers[1] = qSCarrierGroup.findViewById(2131428644);
        for (int i = 0; i < 3; i++) {
            this.mInfos[i] = new CellSignalState(true, 2131232227, context.getText(2131951761).toString(), "", false, this.mProviderModel);
            this.mLastSignalLevel[i] = TelephonyIcons.MOBILE_CALL_STRENGTH_ICONS[0];
            this.mLastSignalLevelDescription[i] = context.getText(R$layout.PHONE_SIGNAL_STRENGTH[0]).toString();
            this.mCarrierGroups[i].setOnClickListener(internetDialog$$ExternalSyntheticLambda3);
        }
        int i2 = 0;
        for (int i3 = 0; i3 < 3; i3++) {
            if (this.mInfos[i3].visible) {
                i2++;
            }
        }
        this.mIsSingleCarrier = i2 == 1 ? true : z;
        qSCarrierGroup.setImportantForAccessibility(1);
        qSCarrierGroup.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.qs.carrier.QSCarrierGroupController.2
            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewAttachedToWindow(View view) {
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public final void onViewDetachedFromWindow(View view) {
                QSCarrierGroupController.this.setListening(false);
            }
        });
    }

    public int getSlotIndex(int i) {
        return this.mSlotIndexResolver.getSlotIndex(i);
    }

    public final void handleUpdateState() {
        boolean z;
        int i;
        if (!this.mMainHandler.getLooper().isCurrentThread()) {
            this.mMainHandler.obtainMessage(1).sendToTarget();
            return;
        }
        int i2 = 0;
        int i3 = 0;
        for (int i4 = 0; i4 < 3; i4++) {
            if (this.mInfos[i4].visible) {
                i3++;
            }
        }
        if (i3 == 1) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            for (int i5 = 0; i5 < 3; i5++) {
                CellSignalState[] cellSignalStateArr = this.mInfos;
                if (cellSignalStateArr[i5].visible && cellSignalStateArr[i5].mobileSignalIconId == 2131232235) {
                    cellSignalStateArr[i5] = new CellSignalState(true, 2131231768, "", "", false, this.mProviderModel);
                }
            }
        }
        for (int i6 = 0; i6 < 3; i6++) {
            this.mCarrierGroups[i6].updateState(this.mInfos[i6], z);
        }
        View view = this.mCarrierDividers[0];
        CellSignalState[] cellSignalStateArr2 = this.mInfos;
        if (!cellSignalStateArr2[0].visible || !cellSignalStateArr2[1].visible) {
            i = 8;
        } else {
            i = 0;
        }
        view.setVisibility(i);
        View view2 = this.mCarrierDividers[1];
        CellSignalState[] cellSignalStateArr3 = this.mInfos;
        if ((!cellSignalStateArr3[1].visible || !cellSignalStateArr3[2].visible) && (!cellSignalStateArr3[0].visible || !cellSignalStateArr3[2].visible)) {
            i2 = 8;
        }
        view2.setVisibility(i2);
        if (this.mIsSingleCarrier != z) {
            this.mIsSingleCarrier = z;
            OnSingleCarrierChangedListener onSingleCarrierChangedListener = this.mOnSingleCarrierChangedListener;
            if (onSingleCarrierChangedListener != null) {
                onSingleCarrierChangedListener.onSingleCarrierChanged(z);
            }
        }
    }

    public final void setListening(boolean z) {
        if (z != this.mListening) {
            this.mListening = z;
            this.mBgHandler.post(new WMShell$7$$ExternalSyntheticLambda1(this, 3));
        }
    }
}
