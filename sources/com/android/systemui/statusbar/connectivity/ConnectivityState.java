package com.android.systemui.statusbar.connectivity;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.settingslib.SignalIcon$IconGroup;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ConnectivityState.kt */
/* loaded from: classes.dex */
public class ConnectivityState {
    public boolean activityIn;
    public boolean activityOut;
    public boolean connected;
    public boolean enabled;
    public SignalIcon$IconGroup iconGroup;
    public int inetCondition;
    public int level;
    public int rssi;
    public long time;

    public boolean equals(Object obj) {
        if (obj == null || !Intrinsics.areEqual(obj.getClass(), getClass())) {
            return false;
        }
        ConnectivityState connectivityState = (ConnectivityState) obj;
        return connectivityState.connected == this.connected && connectivityState.enabled == this.enabled && connectivityState.level == this.level && connectivityState.inetCondition == this.inetCondition && connectivityState.iconGroup == this.iconGroup && connectivityState.activityIn == this.activityIn && connectivityState.activityOut == this.activityOut && connectivityState.rssi == this.rssi;
    }

    public void toString(StringBuilder sb) {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("connected=");
        m.append(this.connected);
        m.append(',');
        sb.append(m.toString());
        sb.append("enabled=" + this.enabled + ',');
        sb.append("level=" + this.level + ',');
        sb.append("inetCondition=" + this.inetCondition + ',');
        sb.append("iconGroup=" + this.iconGroup + ',');
        sb.append("activityIn=" + this.activityIn + ',');
        sb.append("activityOut=" + this.activityOut + ',');
        sb.append("rssi=" + this.rssi + ',');
        sb.append(Intrinsics.stringPlus("lastModified=", ConnectivityStateKt.sSDF.format(Long.valueOf(this.time))));
    }

    public void copyFrom(ConnectivityState connectivityState) {
        this.connected = connectivityState.connected;
        this.enabled = connectivityState.enabled;
        this.activityIn = connectivityState.activityIn;
        this.activityOut = connectivityState.activityOut;
        this.level = connectivityState.level;
        this.iconGroup = connectivityState.iconGroup;
        this.inetCondition = connectivityState.inetCondition;
        this.rssi = connectivityState.rssi;
        this.time = connectivityState.time;
    }

    public int hashCode() {
        int i;
        int hashCode = Boolean.hashCode(this.enabled);
        int hashCode2 = Boolean.hashCode(this.activityIn);
        int hashCode3 = (((Boolean.hashCode(this.activityOut) + ((hashCode2 + ((hashCode + (Boolean.hashCode(this.connected) * 31)) * 31)) * 31)) * 31) + this.level) * 31;
        SignalIcon$IconGroup signalIcon$IconGroup = this.iconGroup;
        if (signalIcon$IconGroup == null) {
            i = 0;
        } else {
            i = signalIcon$IconGroup.hashCode();
        }
        return Long.hashCode(this.time) + ((((((hashCode3 + i) * 31) + this.inetCondition) * 31) + this.rssi) * 31);
    }

    public final String toString() {
        if (this.time == 0) {
            return Intrinsics.stringPlus("Empty ", getClass().getSimpleName());
        }
        StringBuilder sb = new StringBuilder();
        toString(sb);
        return sb.toString();
    }
}
