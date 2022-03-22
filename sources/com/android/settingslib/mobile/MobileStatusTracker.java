package com.android.settingslib.mobile;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Handler;
import android.os.Looper;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyCallback;
import android.telephony.TelephonyDisplayInfo;
import android.telephony.TelephonyManager;
import android.util.Log;
import androidx.core.view.ViewCompat$$ExternalSyntheticLambda0;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline1;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline2;
import com.android.keyguard.KeyguardUpdateMonitor$$ExternalSyntheticLambda6;
import com.android.systemui.statusbar.connectivity.MobileSignalController;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MobileStatusTracker {
    public final Callback mCallback;
    public final SubscriptionDefaults mDefaults;
    public final TelephonyManager mPhone;
    public final Handler mReceiverHandler;
    public final SubscriptionInfo mSubscriptionInfo;
    public final MobileTelephonyCallback mTelephonyCallback = new MobileTelephonyCallback();
    public final MobileStatus mMobileStatus = new MobileStatus();

    /* loaded from: classes.dex */
    public interface Callback {
    }

    /* loaded from: classes.dex */
    public static class MobileStatus {
        public boolean activityIn;
        public boolean activityOut;
        public boolean carrierNetworkChangeMode;
        public boolean dataSim;
        public int dataState;
        public ServiceState serviceState;
        public SignalStrength signalStrength;
        public TelephonyDisplayInfo telephonyDisplayInfo;

        public MobileStatus() {
            this.dataState = 0;
            this.telephonyDisplayInfo = new TelephonyDisplayInfo(0, 0);
        }

        public final String toString() {
            String str;
            Object obj;
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("[activityIn=");
            m.append(this.activityIn);
            m.append(',');
            m.append("activityOut=");
            m.append(this.activityOut);
            m.append(',');
            m.append("dataSim=");
            m.append(this.dataSim);
            m.append(',');
            m.append("carrierNetworkChangeMode=");
            m.append(this.carrierNetworkChangeMode);
            m.append(',');
            m.append("dataState=");
            m.append(this.dataState);
            m.append(',');
            m.append("serviceState=");
            String str2 = "";
            if (this.serviceState == null) {
                str = str2;
            } else {
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("mVoiceRegState=");
                m2.append(this.serviceState.getState());
                m2.append("(");
                m2.append(ServiceState.rilServiceStateToString(this.serviceState.getState()));
                m2.append("), mDataRegState=");
                m2.append(this.serviceState.getDataRegState());
                m2.append("(");
                m2.append(ServiceState.rilServiceStateToString(this.serviceState.getDataRegState()));
                m2.append(")");
                str = m2.toString();
            }
            m.append(str);
            m.append(',');
            m.append("signalStrength=");
            SignalStrength signalStrength = this.signalStrength;
            if (signalStrength == null) {
                obj = str2;
            } else {
                obj = Integer.valueOf(signalStrength.getLevel());
            }
            m.append(obj);
            m.append(',');
            m.append("telephonyDisplayInfo=");
            TelephonyDisplayInfo telephonyDisplayInfo = this.telephonyDisplayInfo;
            if (telephonyDisplayInfo != null) {
                str2 = telephonyDisplayInfo.toString();
            }
            m.append(str2);
            m.append(']');
            return m.toString();
        }

        public MobileStatus(MobileStatus mobileStatus) {
            this.dataState = 0;
            this.telephonyDisplayInfo = new TelephonyDisplayInfo(0, 0);
            this.activityIn = mobileStatus.activityIn;
            this.activityOut = mobileStatus.activityOut;
            this.dataSim = mobileStatus.dataSim;
            this.carrierNetworkChangeMode = mobileStatus.carrierNetworkChangeMode;
            this.dataState = mobileStatus.dataState;
            this.serviceState = mobileStatus.serviceState;
            this.signalStrength = mobileStatus.signalStrength;
            this.telephonyDisplayInfo = mobileStatus.telephonyDisplayInfo;
        }
    }

    /* loaded from: classes.dex */
    public class MobileTelephonyCallback extends TelephonyCallback implements TelephonyCallback.ServiceStateListener, TelephonyCallback.SignalStrengthsListener, TelephonyCallback.DataConnectionStateListener, TelephonyCallback.DataActivityListener, TelephonyCallback.CarrierNetworkListener, TelephonyCallback.ActiveDataSubscriptionIdListener, TelephonyCallback.DisplayInfoListener {
        public MobileTelephonyCallback() {
        }

        public final void onActiveDataSubscriptionIdChanged(int i) {
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                ExifInterface$$ExternalSyntheticOutline1.m("onActiveDataSubscriptionIdChanged: subId=", i, "MobileStatusTracker");
            }
            MobileStatusTracker.this.updateDataSim();
            MobileStatusTracker mobileStatusTracker = MobileStatusTracker.this;
            ((MobileSignalController.AnonymousClass1) mobileStatusTracker.mCallback).onMobileStatusChanged(true, new MobileStatus(mobileStatusTracker.mMobileStatus));
        }

        public final void onCarrierNetworkChange(boolean z) {
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                ViewCompat$$ExternalSyntheticLambda0.m("onCarrierNetworkChange: active=", z, "MobileStatusTracker");
            }
            MobileStatusTracker mobileStatusTracker = MobileStatusTracker.this;
            MobileStatus mobileStatus = mobileStatusTracker.mMobileStatus;
            mobileStatus.carrierNetworkChangeMode = z;
            ((MobileSignalController.AnonymousClass1) mobileStatusTracker.mCallback).onMobileStatusChanged(true, new MobileStatus(mobileStatus));
        }

        public final void onDataActivity(int i) {
            boolean z;
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                ExifInterface$$ExternalSyntheticOutline1.m("onDataActivity: direction=", i, "MobileStatusTracker");
            }
            MobileStatusTracker mobileStatusTracker = MobileStatusTracker.this;
            Objects.requireNonNull(mobileStatusTracker);
            MobileStatus mobileStatus = mobileStatusTracker.mMobileStatus;
            boolean z2 = true;
            if (i == 3 || i == 1) {
                z = true;
            } else {
                z = false;
            }
            mobileStatus.activityIn = z;
            if (!(i == 3 || i == 2)) {
                z2 = false;
            }
            mobileStatus.activityOut = z2;
            MobileStatusTracker mobileStatusTracker2 = MobileStatusTracker.this;
            ((MobileSignalController.AnonymousClass1) mobileStatusTracker2.mCallback).onMobileStatusChanged(false, new MobileStatus(mobileStatusTracker2.mMobileStatus));
        }

        public final void onDataConnectionStateChanged(int i, int i2) {
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                Log.d("MobileStatusTracker", "onDataConnectionStateChanged: state=" + i + " type=" + i2);
            }
            MobileStatusTracker mobileStatusTracker = MobileStatusTracker.this;
            MobileStatus mobileStatus = mobileStatusTracker.mMobileStatus;
            mobileStatus.dataState = i;
            ((MobileSignalController.AnonymousClass1) mobileStatusTracker.mCallback).onMobileStatusChanged(true, new MobileStatus(mobileStatus));
        }

        public final void onDisplayInfoChanged(TelephonyDisplayInfo telephonyDisplayInfo) {
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                Log.d("MobileStatusTracker", "onDisplayInfoChanged: telephonyDisplayInfo=" + telephonyDisplayInfo);
            }
            MobileStatusTracker mobileStatusTracker = MobileStatusTracker.this;
            MobileStatus mobileStatus = mobileStatusTracker.mMobileStatus;
            mobileStatus.telephonyDisplayInfo = telephonyDisplayInfo;
            ((MobileSignalController.AnonymousClass1) mobileStatusTracker.mCallback).onMobileStatusChanged(true, new MobileStatus(mobileStatus));
        }

        public final void onServiceStateChanged(ServiceState serviceState) {
            Object obj;
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("onServiceStateChanged voiceState=");
                Object obj2 = "";
                if (serviceState == null) {
                    obj = obj2;
                } else {
                    obj = Integer.valueOf(serviceState.getState());
                }
                m.append(obj);
                m.append(" dataState=");
                if (serviceState != null) {
                    obj2 = Integer.valueOf(serviceState.getDataRegistrationState());
                }
                m.append(obj2);
                Log.d("MobileStatusTracker", m.toString());
            }
            MobileStatusTracker mobileStatusTracker = MobileStatusTracker.this;
            MobileStatus mobileStatus = mobileStatusTracker.mMobileStatus;
            mobileStatus.serviceState = serviceState;
            ((MobileSignalController.AnonymousClass1) mobileStatusTracker.mCallback).onMobileStatusChanged(true, new MobileStatus(mobileStatus));
        }

        public final void onSignalStrengthsChanged(SignalStrength signalStrength) {
            String str;
            if (Log.isLoggable("MobileStatusTracker", 3)) {
                StringBuilder sb = new StringBuilder();
                sb.append("onSignalStrengthsChanged signalStrength=");
                sb.append(signalStrength);
                if (signalStrength == null) {
                    str = "";
                } else {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m(" level=");
                    m.append(signalStrength.getLevel());
                    str = m.toString();
                }
                ExifInterface$$ExternalSyntheticOutline2.m(sb, str, "MobileStatusTracker");
            }
            MobileStatusTracker mobileStatusTracker = MobileStatusTracker.this;
            MobileStatus mobileStatus = mobileStatusTracker.mMobileStatus;
            mobileStatus.signalStrength = signalStrength;
            ((MobileSignalController.AnonymousClass1) mobileStatusTracker.mCallback).onMobileStatusChanged(true, new MobileStatus(mobileStatus));
        }
    }

    /* loaded from: classes.dex */
    public static class SubscriptionDefaults {
    }

    public final void setListening(boolean z) {
        if (z) {
            TelephonyManager telephonyManager = this.mPhone;
            Handler handler = this.mReceiverHandler;
            Objects.requireNonNull(handler);
            telephonyManager.registerTelephonyCallback(new MobileStatusTracker$$ExternalSyntheticLambda0(handler, 0), this.mTelephonyCallback);
            return;
        }
        this.mPhone.unregisterTelephonyCallback(this.mTelephonyCallback);
    }

    public final void updateDataSim() {
        Objects.requireNonNull(this.mDefaults);
        int activeDataSubscriptionId = SubscriptionManager.getActiveDataSubscriptionId();
        boolean z = true;
        if (SubscriptionManager.isValidSubscriptionId(activeDataSubscriptionId)) {
            MobileStatus mobileStatus = this.mMobileStatus;
            if (activeDataSubscriptionId != this.mSubscriptionInfo.getSubscriptionId()) {
                z = false;
            }
            mobileStatus.dataSim = z;
            return;
        }
        this.mMobileStatus.dataSim = true;
    }

    public MobileStatusTracker(TelephonyManager telephonyManager, Looper looper, SubscriptionInfo subscriptionInfo, SubscriptionDefaults subscriptionDefaults, MobileSignalController.AnonymousClass1 r5) {
        this.mPhone = telephonyManager;
        Handler handler = new Handler(looper);
        this.mReceiverHandler = handler;
        this.mSubscriptionInfo = subscriptionInfo;
        this.mDefaults = subscriptionDefaults;
        this.mCallback = r5;
        updateDataSim();
        handler.post(new KeyguardUpdateMonitor$$ExternalSyntheticLambda6(this, 2));
    }
}
