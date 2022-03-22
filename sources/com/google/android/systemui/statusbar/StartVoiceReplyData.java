package com.google.android.systemui.statusbar;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationVoiceReplyManagerService.kt */
/* loaded from: classes.dex */
public final class StartVoiceReplyData {
    public final int sessionToken;
    public final int userId;
    public final String userMessageContent;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StartVoiceReplyData)) {
            return false;
        }
        StartVoiceReplyData startVoiceReplyData = (StartVoiceReplyData) obj;
        return this.userId == startVoiceReplyData.userId && this.sessionToken == startVoiceReplyData.sessionToken && Intrinsics.areEqual(this.userMessageContent, startVoiceReplyData.userMessageContent);
    }

    public final int hashCode() {
        int i;
        int m = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.sessionToken, Integer.hashCode(this.userId) * 31, 31);
        String str = this.userMessageContent;
        if (str == null) {
            i = 0;
        } else {
            i = str.hashCode();
        }
        return m + i;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("StartVoiceReplyData(userId=");
        m.append(this.userId);
        m.append(", sessionToken=");
        m.append(this.sessionToken);
        m.append(", userMessageContent=");
        m.append((Object) this.userMessageContent);
        m.append(')');
        return m.toString();
    }

    public StartVoiceReplyData(int i, int i2, String str) {
        this.userId = i;
        this.sessionToken = i2;
        this.userMessageContent = str;
    }
}
