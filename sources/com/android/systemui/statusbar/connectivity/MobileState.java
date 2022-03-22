package com.android.systemui.statusbar.connectivity;

import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import android.telephony.SignalStrength;
import android.telephony.TelephonyDisplayInfo;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MobileState.kt */
/* loaded from: classes.dex */
public final class MobileState extends ConnectivityState {
    public ServiceState serviceState;
    public SignalStrength signalStrength;
    public String networkName = null;
    public String networkNameData = null;
    public boolean dataSim = false;
    public boolean dataConnected = false;
    public boolean isEmergency = false;
    public boolean airplaneMode = false;
    public boolean carrierNetworkChangeMode = false;
    public boolean isDefault = false;
    public boolean userSetup = false;
    public boolean roaming = false;
    public int dataState = 0;
    public boolean defaultDataOff = false;
    public TelephonyDisplayInfo telephonyDisplayInfo = new TelephonyDisplayInfo(0, 0);

    @Override // com.android.systemui.statusbar.connectivity.ConnectivityState
    public final boolean equals(Object obj) {
        Class<?> cls;
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            cls = null;
        } else {
            cls = obj.getClass();
        }
        if (!Intrinsics.areEqual(MobileState.class, cls) || !super.equals(obj)) {
            return false;
        }
        Objects.requireNonNull(obj, "null cannot be cast to non-null type com.android.systemui.statusbar.connectivity.MobileState");
        MobileState mobileState = (MobileState) obj;
        return Intrinsics.areEqual(this.networkName, mobileState.networkName) && Intrinsics.areEqual(this.networkNameData, mobileState.networkNameData) && this.dataSim == mobileState.dataSim && this.dataConnected == mobileState.dataConnected && this.isEmergency == mobileState.isEmergency && this.airplaneMode == mobileState.airplaneMode && this.carrierNetworkChangeMode == mobileState.carrierNetworkChangeMode && this.isDefault == mobileState.isDefault && this.userSetup == mobileState.userSetup && this.roaming == mobileState.roaming && this.dataState == mobileState.dataState && this.defaultDataOff == mobileState.defaultDataOff && Intrinsics.areEqual(this.telephonyDisplayInfo, mobileState.telephonyDisplayInfo) && Intrinsics.areEqual(this.serviceState, mobileState.serviceState) && Intrinsics.areEqual(this.signalStrength, mobileState.signalStrength);
    }

    @Override // com.android.systemui.statusbar.connectivity.ConnectivityState
    public final void copyFrom(ConnectivityState connectivityState) {
        MobileState mobileState;
        if (connectivityState instanceof MobileState) {
            mobileState = (MobileState) connectivityState;
        } else {
            mobileState = null;
        }
        if (mobileState != null) {
            super.copyFrom(mobileState);
            this.networkName = mobileState.networkName;
            this.networkNameData = mobileState.networkNameData;
            this.dataSim = mobileState.dataSim;
            this.dataConnected = mobileState.dataConnected;
            this.isEmergency = mobileState.isEmergency;
            this.airplaneMode = mobileState.airplaneMode;
            this.carrierNetworkChangeMode = mobileState.carrierNetworkChangeMode;
            this.isDefault = mobileState.isDefault;
            this.userSetup = mobileState.userSetup;
            this.roaming = mobileState.roaming;
            this.dataState = mobileState.dataState;
            this.defaultDataOff = mobileState.defaultDataOff;
            this.telephonyDisplayInfo = mobileState.telephonyDisplayInfo;
            this.serviceState = mobileState.serviceState;
            this.signalStrength = mobileState.signalStrength;
            return;
        }
        throw new IllegalArgumentException("MobileState can only update from another MobileState");
    }

    public final String getOperatorAlphaShort() {
        String operatorAlphaShort;
        ServiceState serviceState = this.serviceState;
        if (serviceState == null || (operatorAlphaShort = serviceState.getOperatorAlphaShort()) == null) {
            return "";
        }
        return operatorAlphaShort;
    }

    public final boolean isInService() {
        boolean z;
        boolean z2;
        ServiceState serviceState = this.serviceState;
        if (serviceState == null) {
            return false;
        }
        int state = serviceState.getState();
        int dataRegistrationState = serviceState.getDataRegistrationState();
        if ((state == 1 || state == 2) && dataRegistrationState == 0) {
            NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 2);
            if (networkRegistrationInfo == null) {
                z = true;
            } else {
                if (networkRegistrationInfo.getRegistrationState() == 1 || networkRegistrationInfo.getRegistrationState() == 5) {
                    z2 = true;
                } else {
                    z2 = false;
                }
                z = !z2;
            }
            if (z) {
                state = 0;
            }
        }
        if (state == 3 || state == 1 || state == 2) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.statusbar.connectivity.ConnectivityState
    public final int hashCode() {
        int i;
        int i2;
        int i3;
        int hashCode = super.hashCode() * 31;
        String str = this.networkName;
        int i4 = 0;
        if (str == null) {
            i = 0;
        } else {
            i = str.hashCode();
        }
        int i5 = (hashCode + i) * 31;
        String str2 = this.networkNameData;
        if (str2 == null) {
            i2 = 0;
        } else {
            i2 = str2.hashCode();
        }
        int hashCode2 = Boolean.hashCode(this.dataSim);
        int hashCode3 = Boolean.hashCode(this.dataConnected);
        int hashCode4 = Boolean.hashCode(this.isEmergency);
        int hashCode5 = Boolean.hashCode(this.airplaneMode);
        int hashCode6 = Boolean.hashCode(this.carrierNetworkChangeMode);
        int hashCode7 = Boolean.hashCode(this.isDefault);
        int hashCode8 = Boolean.hashCode(this.userSetup);
        int hashCode9 = Boolean.hashCode(this.roaming);
        int hashCode10 = (this.telephonyDisplayInfo.hashCode() + ((Boolean.hashCode(this.defaultDataOff) + ((((hashCode9 + ((hashCode8 + ((hashCode7 + ((hashCode6 + ((hashCode5 + ((hashCode4 + ((hashCode3 + ((hashCode2 + ((i5 + i2) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31)) * 31) + this.dataState) * 31)) * 31)) * 31;
        ServiceState serviceState = this.serviceState;
        if (serviceState == null) {
            i3 = 0;
        } else {
            i3 = serviceState.hashCode();
        }
        int i6 = (hashCode10 + i3) * 31;
        SignalStrength signalStrength = this.signalStrength;
        if (signalStrength != null) {
            i4 = signalStrength.hashCode();
        }
        return i6 + i4;
    }

    /* JADX WARN: Code restructure failed: missing block: B:12:0x0158, code lost:
        if (r2 != false) goto L_0x015a;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x01f2, code lost:
        if (r2 == null) goto L_0x01f4;
     */
    @Override // com.android.systemui.statusbar.connectivity.ConnectivityState
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void toString(java.lang.StringBuilder r8) {
        /*
            Method dump skipped, instructions count: 592
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.connectivity.MobileState.toString(java.lang.StringBuilder):void");
    }
}
