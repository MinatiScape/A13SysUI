package com.android.systemui.statusbar.connectivity;

import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: WifiState.kt */
/* loaded from: classes.dex */
public final class WifiState extends ConnectivityState {
    public String ssid = null;
    public boolean isTransient = false;
    public boolean isDefault = false;
    public String statusLabel = null;
    public boolean isCarrierMerged = false;
    public int subId = 0;

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
        if (!Intrinsics.areEqual(WifiState.class, cls) || !super.equals(obj)) {
            return false;
        }
        Objects.requireNonNull(obj, "null cannot be cast to non-null type com.android.systemui.statusbar.connectivity.WifiState");
        WifiState wifiState = (WifiState) obj;
        return Intrinsics.areEqual(this.ssid, wifiState.ssid) && this.isTransient == wifiState.isTransient && this.isDefault == wifiState.isDefault && Intrinsics.areEqual(this.statusLabel, wifiState.statusLabel) && this.isCarrierMerged == wifiState.isCarrierMerged && this.subId == wifiState.subId;
    }

    @Override // com.android.systemui.statusbar.connectivity.ConnectivityState
    public final void copyFrom(ConnectivityState connectivityState) {
        super.copyFrom(connectivityState);
        WifiState wifiState = (WifiState) connectivityState;
        this.ssid = wifiState.ssid;
        this.isTransient = wifiState.isTransient;
        this.isDefault = wifiState.isDefault;
        this.statusLabel = wifiState.statusLabel;
        this.isCarrierMerged = wifiState.isCarrierMerged;
        this.subId = wifiState.subId;
    }

    @Override // com.android.systemui.statusbar.connectivity.ConnectivityState
    public final int hashCode() {
        int i;
        int hashCode = super.hashCode() * 31;
        String str = this.ssid;
        int i2 = 0;
        if (str == null) {
            i = 0;
        } else {
            i = str.hashCode();
        }
        int hashCode2 = (Boolean.hashCode(this.isDefault) + ((Boolean.hashCode(this.isTransient) + ((hashCode + i) * 31)) * 31)) * 31;
        String str2 = this.statusLabel;
        if (str2 != null) {
            i2 = str2.hashCode();
        }
        return ((Boolean.hashCode(this.isCarrierMerged) + ((hashCode2 + i2) * 31)) * 31) + this.subId;
    }

    @Override // com.android.systemui.statusbar.connectivity.ConnectivityState
    public final void toString(StringBuilder sb) {
        super.toString(sb);
        sb.append(",ssid=");
        sb.append(this.ssid);
        sb.append(",isTransient=");
        sb.append(this.isTransient);
        sb.append(",isDefault=");
        sb.append(this.isDefault);
        sb.append(",statusLabel=");
        sb.append(this.statusLabel);
        sb.append(",isCarrierMerged=");
        sb.append(this.isCarrierMerged);
        sb.append(",subId=");
        sb.append(this.subId);
    }
}
