package com.google.android.systemui.statusbar.notification.voicereplies;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.android.keyguard.FontInterpolator$VarFontKey$$ExternalSyntheticOutline0;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class StartSessionRequest {
    public final Function2<Session, Continuation<? super Unit>, Object> block;
    public final NotificationVoiceReplyHandler handler;
    public final String initialContent;
    public final Function0<Unit> onFail;
    public final int token;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof StartSessionRequest)) {
            return false;
        }
        StartSessionRequest startSessionRequest = (StartSessionRequest) obj;
        return Intrinsics.areEqual(this.handler, startSessionRequest.handler) && this.token == startSessionRequest.token && Intrinsics.areEqual(this.initialContent, startSessionRequest.initialContent) && Intrinsics.areEqual(this.onFail, startSessionRequest.onFail) && Intrinsics.areEqual(this.block, startSessionRequest.block);
    }

    public final int hashCode() {
        int i;
        int m = FontInterpolator$VarFontKey$$ExternalSyntheticOutline0.m(this.token, this.handler.hashCode() * 31, 31);
        String str = this.initialContent;
        if (str == null) {
            i = 0;
        } else {
            i = str.hashCode();
        }
        int hashCode = this.onFail.hashCode();
        return this.block.hashCode() + ((hashCode + ((m + i) * 31)) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("StartSessionRequest(handler=");
        m.append(this.handler);
        m.append(", token=");
        m.append(this.token);
        m.append(", initialContent=");
        m.append((Object) this.initialContent);
        m.append(", onFail=");
        m.append(this.onFail);
        m.append(", block=");
        m.append(this.block);
        m.append(')');
        return m.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public StartSessionRequest(NotificationVoiceReplyHandler notificationVoiceReplyHandler, int i, String str, Function0<Unit> function0, Function2<? super Session, ? super Continuation<? super Unit>, ? extends Object> function2) {
        this.handler = notificationVoiceReplyHandler;
        this.token = i;
        this.initialContent = str;
        this.onFail = function0;
        this.block = function2;
    }
}
