package com.android.systemui.media;

import android.app.smartspace.SmartspaceAction;
import android.content.Intent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import java.util.List;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SmartspaceMediaData.kt */
/* loaded from: classes.dex */
public final class SmartspaceMediaData {
    public final int backgroundColor;
    public final SmartspaceAction cardAction;
    public final Intent dismissIntent;
    public final long headphoneConnectionTimeMillis;
    public final boolean isActive;
    public final boolean isValid;
    public final String packageName;
    public final List<SmartspaceAction> recommendations;
    public final String targetId;

    public static SmartspaceMediaData copy$default(SmartspaceMediaData smartspaceMediaData, String str, boolean z, boolean z2, Intent intent, int i, long j, int i2) {
        String str2;
        boolean z3;
        boolean z4;
        String str3;
        SmartspaceAction smartspaceAction;
        List<SmartspaceAction> list;
        Intent intent2;
        int i3;
        long j2;
        if ((i2 & 1) != 0) {
            str2 = smartspaceMediaData.targetId;
        } else {
            str2 = str;
        }
        if ((i2 & 2) != 0) {
            z3 = smartspaceMediaData.isActive;
        } else {
            z3 = z;
        }
        if ((i2 & 4) != 0) {
            z4 = smartspaceMediaData.isValid;
        } else {
            z4 = z2;
        }
        if ((i2 & 8) != 0) {
            str3 = smartspaceMediaData.packageName;
        } else {
            str3 = null;
        }
        if ((i2 & 16) != 0) {
            smartspaceAction = smartspaceMediaData.cardAction;
        } else {
            smartspaceAction = null;
        }
        if ((i2 & 32) != 0) {
            list = smartspaceMediaData.recommendations;
        } else {
            list = null;
        }
        if ((i2 & 64) != 0) {
            intent2 = smartspaceMediaData.dismissIntent;
        } else {
            intent2 = intent;
        }
        if ((i2 & 128) != 0) {
            i3 = smartspaceMediaData.backgroundColor;
        } else {
            i3 = i;
        }
        if ((i2 & 256) != 0) {
            j2 = smartspaceMediaData.headphoneConnectionTimeMillis;
        } else {
            j2 = j;
        }
        Objects.requireNonNull(smartspaceMediaData);
        return new SmartspaceMediaData(str2, z3, z4, str3, smartspaceAction, list, intent2, i3, j2);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof SmartspaceMediaData)) {
            return false;
        }
        SmartspaceMediaData smartspaceMediaData = (SmartspaceMediaData) obj;
        return Intrinsics.areEqual(this.targetId, smartspaceMediaData.targetId) && this.isActive == smartspaceMediaData.isActive && this.isValid == smartspaceMediaData.isValid && Intrinsics.areEqual(this.packageName, smartspaceMediaData.packageName) && Intrinsics.areEqual(this.cardAction, smartspaceMediaData.cardAction) && Intrinsics.areEqual(this.recommendations, smartspaceMediaData.recommendations) && Intrinsics.areEqual(this.dismissIntent, smartspaceMediaData.dismissIntent) && this.backgroundColor == smartspaceMediaData.backgroundColor && this.headphoneConnectionTimeMillis == smartspaceMediaData.headphoneConnectionTimeMillis;
    }

    public final int hashCode() {
        int i;
        int hashCode = this.targetId.hashCode() * 31;
        boolean z = this.isActive;
        int i2 = 1;
        if (z) {
            z = true;
        }
        int i3 = z ? 1 : 0;
        int i4 = z ? 1 : 0;
        int i5 = (hashCode + i3) * 31;
        boolean z2 = this.isValid;
        if (!z2) {
            i2 = z2 ? 1 : 0;
        }
        int hashCode2 = (this.packageName.hashCode() + ((i5 + i2) * 31)) * 31;
        SmartspaceAction smartspaceAction = this.cardAction;
        int i6 = 0;
        if (smartspaceAction == null) {
            i = 0;
        } else {
            i = smartspaceAction.hashCode();
        }
        int hashCode3 = (this.recommendations.hashCode() + ((hashCode2 + i) * 31)) * 31;
        Intent intent = this.dismissIntent;
        if (intent != null) {
            i6 = intent.hashCode();
        }
        return Long.hashCode(this.headphoneConnectionTimeMillis) + FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.backgroundColor, (hashCode3 + i6) * 31, 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("SmartspaceMediaData(targetId=");
        m.append(this.targetId);
        m.append(", isActive=");
        m.append(this.isActive);
        m.append(", isValid=");
        m.append(this.isValid);
        m.append(", packageName=");
        m.append(this.packageName);
        m.append(", cardAction=");
        m.append(this.cardAction);
        m.append(", recommendations=");
        m.append(this.recommendations);
        m.append(", dismissIntent=");
        m.append(this.dismissIntent);
        m.append(", backgroundColor=");
        m.append(this.backgroundColor);
        m.append(", headphoneConnectionTimeMillis=");
        m.append(this.headphoneConnectionTimeMillis);
        m.append(')');
        return m.toString();
    }

    public SmartspaceMediaData(String str, boolean z, boolean z2, String str2, SmartspaceAction smartspaceAction, List<SmartspaceAction> list, Intent intent, int i, long j) {
        this.targetId = str;
        this.isActive = z;
        this.isValid = z2;
        this.packageName = str2;
        this.cardAction = smartspaceAction;
        this.recommendations = list;
        this.dismissIntent = intent;
        this.backgroundColor = i;
        this.headphoneConnectionTimeMillis = j;
    }
}
