package com.google.android.systemui.statusbar;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* loaded from: classes.dex */
public final class OnVoiceAuthStateChangedData {
    public final int newState;
    public final int sessionToken;
    public final int userId;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OnVoiceAuthStateChangedData)) {
            return false;
        }
        OnVoiceAuthStateChangedData onVoiceAuthStateChangedData = (OnVoiceAuthStateChangedData) obj;
        return this.userId == onVoiceAuthStateChangedData.userId && this.sessionToken == onVoiceAuthStateChangedData.sessionToken && this.newState == onVoiceAuthStateChangedData.newState;
    }

    public final int hashCode() {
        return Integer.hashCode(this.newState) + FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.sessionToken, Integer.hashCode(this.userId) * 31, 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("OnVoiceAuthStateChangedData(userId=");
        m.append(this.userId);
        m.append(", sessionToken=");
        m.append(this.sessionToken);
        m.append(", newState=");
        return Insets$$ExternalSyntheticOutline0.m(m, this.newState, ')');
    }

    public OnVoiceAuthStateChangedData(int i, int i2, int i3) {
        this.userId = i;
        this.sessionToken = i2;
        this.newState = i3;
    }
}
