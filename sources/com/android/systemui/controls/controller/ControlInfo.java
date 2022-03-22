package com.android.systemui.controls.controller;

import com.airbnb.lottie.parser.moshi.JsonReader$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlInfo.kt */
/* loaded from: classes.dex */
public final class ControlInfo {
    public final String controlId;
    public final CharSequence controlSubtitle;
    public final CharSequence controlTitle;
    public final int deviceType;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ControlInfo)) {
            return false;
        }
        ControlInfo controlInfo = (ControlInfo) obj;
        return Intrinsics.areEqual(this.controlId, controlInfo.controlId) && Intrinsics.areEqual(this.controlTitle, controlInfo.controlTitle) && Intrinsics.areEqual(this.controlSubtitle, controlInfo.controlSubtitle) && this.deviceType == controlInfo.deviceType;
    }

    public final int hashCode() {
        int hashCode = this.controlTitle.hashCode();
        int hashCode2 = this.controlSubtitle.hashCode();
        return Integer.hashCode(this.deviceType) + ((hashCode2 + ((hashCode + (this.controlId.hashCode() * 31)) * 31)) * 31);
    }

    public final String toString() {
        StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m(':');
        m.append(this.controlId);
        m.append(':');
        m.append((Object) this.controlTitle);
        m.append(':');
        m.append(this.deviceType);
        return m.toString();
    }

    public ControlInfo(String str, CharSequence charSequence, CharSequence charSequence2, int i) {
        this.controlId = str;
        this.controlTitle = charSequence;
        this.controlSubtitle = charSequence2;
        this.deviceType = i;
    }
}
