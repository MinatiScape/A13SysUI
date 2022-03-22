package com.android.systemui.statusbar.connectivity;

import androidx.recyclerview.widget.LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SignalCallback.kt */
/* loaded from: classes.dex */
public final class MobileDataIndicators {
    public final boolean activityIn;
    public final boolean activityOut;
    public final CharSequence qsDescription;
    public final IconState qsIcon;
    public final int qsType;
    public final boolean roaming;
    public final boolean showTriangle;
    public final IconState statusIcon;
    public final int statusType;
    public final int subId;
    public final CharSequence typeContentDescription;
    public final CharSequence typeContentDescriptionHtml;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MobileDataIndicators)) {
            return false;
        }
        MobileDataIndicators mobileDataIndicators = (MobileDataIndicators) obj;
        return Intrinsics.areEqual(this.statusIcon, mobileDataIndicators.statusIcon) && Intrinsics.areEqual(this.qsIcon, mobileDataIndicators.qsIcon) && this.statusType == mobileDataIndicators.statusType && this.qsType == mobileDataIndicators.qsType && this.activityIn == mobileDataIndicators.activityIn && this.activityOut == mobileDataIndicators.activityOut && Intrinsics.areEqual(this.typeContentDescription, mobileDataIndicators.typeContentDescription) && Intrinsics.areEqual(this.typeContentDescriptionHtml, mobileDataIndicators.typeContentDescriptionHtml) && Intrinsics.areEqual(this.qsDescription, mobileDataIndicators.qsDescription) && this.subId == mobileDataIndicators.subId && this.roaming == mobileDataIndicators.roaming && this.showTriangle == mobileDataIndicators.showTriangle;
    }

    public final int hashCode() {
        int i;
        int i2;
        int i3;
        int i4;
        IconState iconState = this.statusIcon;
        int i5 = 0;
        if (iconState == null) {
            i = 0;
        } else {
            i = iconState.hashCode();
        }
        int i6 = i * 31;
        IconState iconState2 = this.qsIcon;
        if (iconState2 == null) {
            i2 = 0;
        } else {
            i2 = iconState2.hashCode();
        }
        int m = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.qsType, FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.statusType, (i6 + i2) * 31, 31), 31);
        boolean z = this.activityIn;
        int i7 = 1;
        if (z) {
            z = true;
        }
        int i8 = z ? 1 : 0;
        int i9 = z ? 1 : 0;
        int i10 = (m + i8) * 31;
        boolean z2 = this.activityOut;
        if (z2) {
            z2 = true;
        }
        int i11 = z2 ? 1 : 0;
        int i12 = z2 ? 1 : 0;
        int i13 = (i10 + i11) * 31;
        CharSequence charSequence = this.typeContentDescription;
        if (charSequence == null) {
            i3 = 0;
        } else {
            i3 = charSequence.hashCode();
        }
        int i14 = (i13 + i3) * 31;
        CharSequence charSequence2 = this.typeContentDescriptionHtml;
        if (charSequence2 == null) {
            i4 = 0;
        } else {
            i4 = charSequence2.hashCode();
        }
        int i15 = (i14 + i4) * 31;
        CharSequence charSequence3 = this.qsDescription;
        if (charSequence3 != null) {
            i5 = charSequence3.hashCode();
        }
        int m2 = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.subId, (i15 + i5) * 31, 31);
        boolean z3 = this.roaming;
        if (z3) {
            z3 = true;
        }
        int i16 = z3 ? 1 : 0;
        int i17 = z3 ? 1 : 0;
        int i18 = (m2 + i16) * 31;
        boolean z4 = this.showTriangle;
        if (!z4) {
            i7 = z4 ? 1 : 0;
        }
        return i18 + i7;
    }

    public final String toString() {
        String str;
        StringBuilder sb = new StringBuilder("MobileDataIndicators[");
        sb.append("statusIcon=");
        IconState iconState = this.statusIcon;
        String str2 = "";
        if (iconState == null) {
            str = str2;
        } else {
            str = iconState.toString();
        }
        sb.append(str);
        sb.append(",qsIcon=");
        IconState iconState2 = this.qsIcon;
        if (iconState2 != null) {
            str2 = iconState2.toString();
        }
        sb.append(str2);
        sb.append(",statusType=");
        sb.append(this.statusType);
        sb.append(",qsType=");
        sb.append(this.qsType);
        sb.append(",activityIn=");
        sb.append(this.activityIn);
        sb.append(",activityOut=");
        sb.append(this.activityOut);
        sb.append(",typeContentDescription=");
        sb.append(this.typeContentDescription);
        sb.append(",typeContentDescriptionHtml=");
        sb.append(this.typeContentDescriptionHtml);
        sb.append(",description=");
        sb.append(this.qsDescription);
        sb.append(",subId=");
        sb.append(this.subId);
        sb.append(",roaming=");
        sb.append(this.roaming);
        sb.append(",showTriangle=");
        return LinearLayoutManager$AnchorInfo$$ExternalSyntheticOutline0.m(sb, this.showTriangle, ']');
    }

    public MobileDataIndicators(IconState iconState, IconState iconState2, int i, int i2, boolean z, boolean z2, String str, CharSequence charSequence, CharSequence charSequence2, int i3, boolean z3, boolean z4) {
        this.statusIcon = iconState;
        this.qsIcon = iconState2;
        this.statusType = i;
        this.qsType = i2;
        this.activityIn = z;
        this.activityOut = z2;
        this.typeContentDescription = str;
        this.typeContentDescriptionHtml = charSequence;
        this.qsDescription = charSequence2;
        this.subId = i3;
        this.roaming = z3;
        this.showTriangle = z4;
    }
}
