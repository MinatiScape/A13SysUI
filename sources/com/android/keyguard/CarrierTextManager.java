package com.android.keyguard;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.telephony.ServiceState;
import android.telephony.SubscriptionInfo;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.recyclerview.widget.GridLayoutManager$$ExternalSyntheticOutline0;
import com.android.systemui.keyguard.WakefulnessLifecycle;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.scrim.ScrimView$$ExternalSyntheticLambda0;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda3;
import com.android.wm.shell.TaskView$$ExternalSyntheticLambda4;
import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
/* loaded from: classes.dex */
public final class CarrierTextManager {
    public static final boolean DEBUG = KeyguardConstants.DEBUG;
    public final Executor mBgExecutor;
    public CarrierTextCallback mCarrierTextCallback;
    public final Context mContext;
    public final boolean mIsEmergencyCallCapable;
    public KeyguardUpdateMonitor mKeyguardUpdateMonitor;
    public final Executor mMainExecutor;
    public final CharSequence mSeparator;
    public final boolean mShowAirplaneMode;
    public final boolean mShowMissingSim;
    public final boolean[] mSimErrorState;
    public final int mSimSlotsNumber;
    public boolean mTelephonyCapable;
    public final TelephonyListenerManager mTelephonyListenerManager;
    public final TelephonyManager mTelephonyManager;
    public final WakefulnessLifecycle mWakefulnessLifecycle;
    public final WifiManager mWifiManager;
    public final AtomicBoolean mNetworkSupported = new AtomicBoolean();
    public final AnonymousClass1 mWakefulnessObserver = new WakefulnessLifecycle.Observer() { // from class: com.android.keyguard.CarrierTextManager.1
        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public final void onFinishedWakingUp() {
            CarrierTextCallback carrierTextCallback = CarrierTextManager.this.mCarrierTextCallback;
            if (carrierTextCallback != null) {
                carrierTextCallback.finishedWakingUp();
            }
        }

        @Override // com.android.systemui.keyguard.WakefulnessLifecycle.Observer
        public final void onStartedGoingToSleep() {
            CarrierTextCallback carrierTextCallback = CarrierTextManager.this.mCarrierTextCallback;
            if (carrierTextCallback != null) {
                carrierTextCallback.startedGoingToSleep();
            }
        }
    };
    public final KeyguardUpdateMonitorCallback mCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.CarrierTextManager.2
        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onRefreshCarrierInfo() {
            if (CarrierTextManager.DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onRefreshCarrierInfo(), mTelephonyCapable: ");
                m.append(Boolean.toString(CarrierTextManager.this.mTelephonyCapable));
                Log.d("CarrierTextController", m.toString());
            }
            CarrierTextManager.this.updateCarrierText();
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onSimStateChanged(int i, int i2, int i3) {
            if (i2 < 0 || i2 >= CarrierTextManager.this.mSimSlotsNumber) {
                StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("onSimStateChanged() - slotId invalid: ", i2, " mTelephonyCapable: ");
                m.append(Boolean.toString(CarrierTextManager.this.mTelephonyCapable));
                Log.d("CarrierTextController", m.toString());
                return;
            }
            if (CarrierTextManager.DEBUG) {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("onSimStateChanged: ");
                m2.append(CarrierTextManager.this.getStatusForIccState(i3));
                Log.d("CarrierTextController", m2.toString());
            }
            if (CarrierTextManager.this.getStatusForIccState(i3) == StatusMode.SimIoError) {
                CarrierTextManager carrierTextManager = CarrierTextManager.this;
                carrierTextManager.mSimErrorState[i2] = true;
                carrierTextManager.updateCarrierText();
                return;
            }
            CarrierTextManager carrierTextManager2 = CarrierTextManager.this;
            boolean[] zArr = carrierTextManager2.mSimErrorState;
            if (zArr[i2]) {
                zArr[i2] = false;
                carrierTextManager2.updateCarrierText();
            }
        }

        @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
        public final void onTelephonyCapable(boolean z) {
            if (CarrierTextManager.DEBUG) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onTelephonyCapable() mTelephonyCapable: ");
                m.append(Boolean.toString(z));
                Log.d("CarrierTextController", m.toString());
            }
            CarrierTextManager carrierTextManager = CarrierTextManager.this;
            carrierTextManager.mTelephonyCapable = z;
            carrierTextManager.updateCarrierText();
        }
    };
    public final AnonymousClass3 mPhoneStateListener = new TelephonyCallback.ActiveDataSubscriptionIdListener() { // from class: com.android.keyguard.CarrierTextManager.3
        public final void onActiveDataSubscriptionIdChanged(int i) {
            if (CarrierTextManager.this.mNetworkSupported.get()) {
                CarrierTextManager carrierTextManager = CarrierTextManager.this;
                if (carrierTextManager.mCarrierTextCallback != null) {
                    carrierTextManager.updateCarrierText();
                }
            }
        }
    };

    /* loaded from: classes.dex */
    public static class Builder {
        public final Executor mBgExecutor;
        public final Context mContext;
        public final KeyguardUpdateMonitor mKeyguardUpdateMonitor;
        public final Executor mMainExecutor;
        public final String mSeparator;
        public boolean mShowAirplaneMode;
        public boolean mShowMissingSim;
        public final TelephonyListenerManager mTelephonyListenerManager;
        public final TelephonyManager mTelephonyManager;
        public final WakefulnessLifecycle mWakefulnessLifecycle;
        public final WifiManager mWifiManager;

        public final CarrierTextManager build() {
            return new CarrierTextManager(this.mContext, this.mSeparator, this.mShowAirplaneMode, this.mShowMissingSim, this.mWifiManager, this.mTelephonyManager, this.mTelephonyListenerManager, this.mWakefulnessLifecycle, this.mMainExecutor, this.mBgExecutor, this.mKeyguardUpdateMonitor);
        }

        public Builder(Context context, Resources resources, WifiManager wifiManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WakefulnessLifecycle wakefulnessLifecycle, Executor executor, Executor executor2, KeyguardUpdateMonitor keyguardUpdateMonitor) {
            this.mContext = context;
            this.mSeparator = resources.getString(17040554);
            this.mWifiManager = wifiManager;
            this.mTelephonyManager = telephonyManager;
            this.mTelephonyListenerManager = telephonyListenerManager;
            this.mWakefulnessLifecycle = wakefulnessLifecycle;
            this.mMainExecutor = executor;
            this.mBgExecutor = executor2;
            this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        }
    }

    /* loaded from: classes.dex */
    public interface CarrierTextCallback {
        default void finishedWakingUp() {
        }

        default void startedGoingToSleep() {
        }

        default void updateCarrierInfo(CarrierTextCallbackInfo carrierTextCallbackInfo) {
        }
    }

    /* loaded from: classes.dex */
    public static final class CarrierTextCallbackInfo {
        public boolean airplaneMode;
        public final boolean anySimReady;
        public final CharSequence carrierText;
        public final CharSequence[] listOfCarriers;
        public final int[] subscriptionIds;

        public CarrierTextCallbackInfo(CharSequence charSequence, CharSequence[] charSequenceArr, boolean z, int[] iArr) {
            this(charSequence, charSequenceArr, z, iArr, false);
        }

        public CarrierTextCallbackInfo(CharSequence charSequence, CharSequence[] charSequenceArr, boolean z, int[] iArr, boolean z2) {
            this.carrierText = charSequence;
            this.listOfCarriers = charSequenceArr;
            this.anySimReady = z;
            this.subscriptionIds = iArr;
            this.airplaneMode = z2;
        }
    }

    /* loaded from: classes.dex */
    public enum StatusMode {
        Normal,
        /* JADX INFO: Fake field, exist only in values array */
        NetworkLocked,
        SimMissing,
        SimMissingLocked,
        SimPukLocked,
        SimLocked,
        SimPermDisabled,
        SimNotReady,
        SimIoError,
        SimUnknown
    }

    public final void handleSetListening(CarrierTextCallback carrierTextCallback) {
        if (carrierTextCallback != null) {
            this.mCarrierTextCallback = carrierTextCallback;
            if (this.mNetworkSupported.get()) {
                this.mMainExecutor.execute(new TaskView$$ExternalSyntheticLambda3(this, 1));
                TelephonyListenerManager telephonyListenerManager = this.mTelephonyListenerManager;
                AnonymousClass3 r2 = this.mPhoneStateListener;
                Objects.requireNonNull(telephonyListenerManager);
                com.android.systemui.telephony.TelephonyCallback telephonyCallback = telephonyListenerManager.mTelephonyCallback;
                Objects.requireNonNull(telephonyCallback);
                telephonyCallback.mActiveDataSubscriptionIdListeners.add(r2);
                telephonyListenerManager.updateListening();
                return;
            }
            this.mMainExecutor.execute(new TaskView$$ExternalSyntheticLambda4(carrierTextCallback, 1));
            return;
        }
        this.mCarrierTextCallback = null;
        this.mMainExecutor.execute(new ScrimView$$ExternalSyntheticLambda0(this, 1));
        TelephonyListenerManager telephonyListenerManager2 = this.mTelephonyListenerManager;
        AnonymousClass3 r22 = this.mPhoneStateListener;
        Objects.requireNonNull(telephonyListenerManager2);
        com.android.systemui.telephony.TelephonyCallback telephonyCallback2 = telephonyListenerManager2.mTelephonyCallback;
        Objects.requireNonNull(telephonyCallback2);
        telephonyCallback2.mActiveDataSubscriptionIdListeners.remove(r22);
        telephonyListenerManager2.updateListening();
    }

    public final StatusMode getStatusForIccState(int i) {
        StatusMode statusMode = StatusMode.SimUnknown;
        KeyguardUpdateMonitor keyguardUpdateMonitor = this.mKeyguardUpdateMonitor;
        Objects.requireNonNull(keyguardUpdateMonitor);
        boolean z = true;
        if (keyguardUpdateMonitor.mDeviceProvisioned || !(i == 1 || i == 7)) {
            z = false;
        }
        if (z) {
            i = 4;
        }
        switch (i) {
            case 0:
                return statusMode;
            case 1:
                return StatusMode.SimMissing;
            case 2:
                return StatusMode.SimLocked;
            case 3:
                return StatusMode.SimPukLocked;
            case 4:
                return StatusMode.SimMissingLocked;
            case 5:
                return StatusMode.Normal;
            case FalsingManager.VERSION /* 6 */:
                return StatusMode.SimNotReady;
            case 7:
                return StatusMode.SimPermDisabled;
            case 8:
                return StatusMode.SimIoError;
            default:
                return statusMode;
        }
    }

    public final CharSequence makeCarrierStringOnEmergencyCapable(CharSequence charSequence, CharSequence charSequence2) {
        if (this.mIsEmergencyCallCapable) {
            return concatenate(charSequence, charSequence2, this.mSeparator);
        }
        return charSequence;
    }

    public void postToCallback(CarrierTextCallbackInfo carrierTextCallbackInfo) {
        CarrierTextCallback carrierTextCallback = this.mCarrierTextCallback;
        if (carrierTextCallback != null) {
            this.mMainExecutor.execute(new CarrierTextManager$$ExternalSyntheticLambda1(carrierTextCallback, carrierTextCallbackInfo, 0));
        }
    }

    public final void updateCarrierText() {
        String str;
        boolean z;
        String str2;
        boolean z2;
        boolean z3;
        String str3;
        String str4;
        String str5;
        String str6;
        int[] iArr;
        int[] iArr2;
        ServiceState serviceState;
        WifiManager wifiManager;
        ArrayList filteredSubscriptionInfo = this.mKeyguardUpdateMonitor.getFilteredSubscriptionInfo();
        int size = filteredSubscriptionInfo.size();
        int[] iArr3 = new int[size];
        int[] iArr4 = new int[this.mSimSlotsNumber];
        for (int i = 0; i < this.mSimSlotsNumber; i++) {
            iArr4[i] = -1;
        }
        CharSequence[] charSequenceArr = new CharSequence[size];
        if (DEBUG) {
            ExifInterface$$ExternalSyntheticOutline1.m("updateCarrierText(): ", size, "CarrierTextController");
        }
        boolean z4 = true;
        int i2 = 0;
        boolean z5 = false;
        while (true) {
            str = "";
            if (i2 >= size) {
                break;
            }
            int subscriptionId = ((SubscriptionInfo) filteredSubscriptionInfo.get(i2)).getSubscriptionId();
            charSequenceArr[i2] = str;
            iArr3[i2] = subscriptionId;
            iArr4[((SubscriptionInfo) filteredSubscriptionInfo.get(i2)).getSimSlotIndex()] = i2;
            int simState = this.mKeyguardUpdateMonitor.getSimState(subscriptionId);
            CharSequence carrierName = ((SubscriptionInfo) filteredSubscriptionInfo.get(i2)).getCarrierName();
            CharSequence carrierTextForSimState = getCarrierTextForSimState(simState, carrierName);
            boolean z6 = DEBUG;
            if (z6) {
                iArr2 = iArr3;
                iArr = iArr4;
                StringBuilder m = GridLayoutManager$$ExternalSyntheticOutline0.m("Handling (subId=", subscriptionId, "): ", simState, " ");
                m.append((Object) carrierName);
                Log.d("CarrierTextController", m.toString());
            } else {
                iArr = iArr4;
                iArr2 = iArr3;
            }
            if (carrierTextForSimState != null) {
                charSequenceArr[i2] = carrierTextForSimState;
                z4 = false;
            }
            if (simState == 5 && (serviceState = this.mKeyguardUpdateMonitor.mServiceStates.get(Integer.valueOf(subscriptionId))) != null && serviceState.getDataRegistrationState() == 0 && !(serviceState.getRilDataRadioTechnology() == 18 && ((wifiManager = this.mWifiManager) == null || !wifiManager.isWifiEnabled() || this.mWifiManager.getConnectionInfo() == null || this.mWifiManager.getConnectionInfo().getBSSID() == null))) {
                if (z6) {
                    Log.d("CarrierTextController", "SIM ready and in service: subId=" + subscriptionId + ", ss=" + serviceState);
                }
                z5 = true;
            }
            i2++;
            iArr3 = iArr2;
            iArr4 = iArr;
        }
        String str7 = null;
        if (z4 && !z5) {
            if (size != 0) {
                if (!this.mShowMissingSim || !this.mTelephonyCapable) {
                    str6 = str;
                } else {
                    str6 = this.mContext.getString(2131952547);
                }
                str7 = makeCarrierStringOnEmergencyCapable(str6, ((SubscriptionInfo) filteredSubscriptionInfo.get(0)).getCarrierName());
            } else {
                CharSequence text = this.mContext.getText(17040189);
                Intent registerReceiver = this.mContext.registerReceiver(null, new IntentFilter("android.telephony.action.SERVICE_PROVIDERS_UPDATED"));
                if (registerReceiver != null) {
                    if (registerReceiver.getBooleanExtra("android.telephony.extra.SHOW_SPN", false)) {
                        str4 = registerReceiver.getStringExtra("android.telephony.extra.SPN");
                    } else {
                        str4 = str;
                    }
                    if (registerReceiver.getBooleanExtra("android.telephony.extra.SHOW_PLMN", false)) {
                        str5 = registerReceiver.getStringExtra("android.telephony.extra.PLMN");
                    } else {
                        str5 = str;
                    }
                    if (DEBUG) {
                        Log.d("CarrierTextController", "Getting plmn/spn sticky brdcst " + str5 + "/" + str4);
                    }
                    if (Objects.equals(str5, str4)) {
                        text = str5;
                    } else {
                        text = concatenate(str5, str4, this.mSeparator);
                    }
                }
                if (!this.mShowMissingSim || !this.mTelephonyCapable) {
                    str3 = str;
                } else {
                    str3 = this.mContext.getString(2131952547);
                }
                str7 = makeCarrierStringOnEmergencyCapable(str3, text);
            }
        }
        if (TextUtils.isEmpty(str7)) {
            CharSequence charSequence = this.mSeparator;
            if (size == 0) {
                str7 = str;
            } else {
                StringBuilder sb = new StringBuilder();
                for (int i3 = 0; i3 < size; i3++) {
                    if (!TextUtils.isEmpty(charSequenceArr[i3])) {
                        if (!TextUtils.isEmpty(sb)) {
                            sb.append(charSequence);
                        }
                        sb.append(charSequenceArr[i3]);
                    }
                }
                str7 = sb.toString();
            }
        }
        CharSequence carrierTextForSimState2 = getCarrierTextForSimState(8, str);
        CharSequence charSequence2 = str7;
        int i4 = 0;
        while (true) {
            if (i4 >= this.mTelephonyManager.getActiveModemCount()) {
                break;
            }
            if (this.mSimErrorState[i4]) {
                if (z4) {
                    charSequence2 = concatenate(carrierTextForSimState2, this.mContext.getText(17040189), this.mSeparator);
                    break;
                } else if (iArr4[i4] != -1) {
                    int i5 = iArr4[i4];
                    charSequenceArr[i5] = concatenate(carrierTextForSimState2, charSequenceArr[i5], this.mSeparator);
                } else {
                    charSequence2 = concatenate(charSequence2, carrierTextForSimState2, this.mSeparator);
                }
            }
            i4++;
        }
        if (!z5) {
            z2 = false;
            if (Settings.Global.getInt(this.mContext.getContentResolver(), "airplane_mode_on", 0) != 0) {
                z3 = true;
            } else {
                z3 = false;
            }
            if (z3) {
                if (this.mShowAirplaneMode) {
                    str = this.mContext.getString(2131951873);
                }
                z = true;
                str2 = str;
                postToCallback(new CarrierTextCallbackInfo(str2, charSequenceArr, !z4, iArr3, z));
            }
        } else {
            z2 = false;
        }
        z = z2;
        str2 = charSequence2;
        postToCallback(new CarrierTextCallbackInfo(str2, charSequenceArr, !z4, iArr3, z));
    }

    /* JADX WARN: Type inference failed for: r0v1, types: [com.android.keyguard.CarrierTextManager$1] */
    public CarrierTextManager(Context context, String str, boolean z, boolean z2, WifiManager wifiManager, TelephonyManager telephonyManager, TelephonyListenerManager telephonyListenerManager, WakefulnessLifecycle wakefulnessLifecycle, Executor executor, Executor executor2, KeyguardUpdateMonitor keyguardUpdateMonitor) {
        this.mContext = context;
        this.mIsEmergencyCallCapable = telephonyManager.isVoiceCapable();
        this.mShowAirplaneMode = z;
        this.mShowMissingSim = z2;
        this.mWifiManager = wifiManager;
        this.mTelephonyManager = telephonyManager;
        this.mSeparator = str;
        this.mTelephonyListenerManager = telephonyListenerManager;
        this.mWakefulnessLifecycle = wakefulnessLifecycle;
        int supportedModemCount = telephonyManager.getSupportedModemCount();
        this.mSimSlotsNumber = supportedModemCount;
        this.mSimErrorState = new boolean[supportedModemCount];
        this.mMainExecutor = executor;
        this.mBgExecutor = executor2;
        this.mKeyguardUpdateMonitor = keyguardUpdateMonitor;
        executor2.execute(new CarrierTextManager$$ExternalSyntheticLambda0(this, 0));
    }

    public static CharSequence concatenate(CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3) {
        boolean z = !TextUtils.isEmpty(charSequence);
        boolean z2 = !TextUtils.isEmpty(charSequence2);
        if (z && z2) {
            StringBuilder sb = new StringBuilder();
            sb.append(charSequence);
            sb.append(charSequence3);
            sb.append(charSequence2);
            return sb.toString();
        } else if (z) {
            return charSequence;
        } else {
            if (z2) {
                return charSequence2;
            }
            return "";
        }
    }

    public final CharSequence getCarrierTextForSimState(int i, CharSequence charSequence) {
        switch (getStatusForIccState(i).ordinal()) {
            case 0:
                return charSequence;
            case 1:
                return makeCarrierStringOnEmergencyCapable(this.mContext.getText(2131952548), charSequence);
            case 2:
            case 3:
            case 9:
            default:
                return null;
            case 4:
                return makeCarrierStringOnLocked(this.mContext.getText(2131952560), charSequence);
            case 5:
                return makeCarrierStringOnLocked(this.mContext.getText(2131952559), charSequence);
            case FalsingManager.VERSION /* 6 */:
                return makeCarrierStringOnEmergencyCapable(this.mContext.getText(2131952550), charSequence);
            case 7:
                return "";
            case 8:
                return makeCarrierStringOnEmergencyCapable(this.mContext.getText(2131952558), charSequence);
        }
    }

    public final CharSequence makeCarrierStringOnLocked(CharSequence charSequence, CharSequence charSequence2) {
        boolean z = !TextUtils.isEmpty(charSequence);
        boolean z2 = !TextUtils.isEmpty(charSequence2);
        if (z && z2) {
            return this.mContext.getString(2131952530, charSequence2, charSequence);
        }
        if (z) {
            return charSequence;
        }
        if (z2) {
            return charSequence2;
        }
        return "";
    }
}
