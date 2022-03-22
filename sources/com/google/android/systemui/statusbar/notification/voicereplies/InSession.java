package com.google.android.systemui.statusbar.notification.voicereplies;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class InSession extends VoiceReplyState {
    public final Function2<Session, Continuation<? super Unit>, Object> block;
    public final String initialContent;
    public final VoiceReplyTarget target;

    /* JADX WARN: Multi-variable type inference failed */
    public InSession(String str, Function2<? super Session, ? super Continuation<? super Unit>, ? extends Object> function2, VoiceReplyTarget voiceReplyTarget) {
        super(0);
        this.initialContent = str;
        this.block = function2;
        this.target = voiceReplyTarget;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof InSession)) {
            return false;
        }
        InSession inSession = (InSession) obj;
        return Intrinsics.areEqual(this.initialContent, inSession.initialContent) && Intrinsics.areEqual(this.block, inSession.block) && Intrinsics.areEqual(this.target, inSession.target);
    }

    public final int hashCode() {
        String str = this.initialContent;
        int hashCode = str == null ? 0 : str.hashCode();
        return this.target.hashCode() + ((this.block.hashCode() + (hashCode * 31)) * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("InSession(initialContent=");
        m.append((Object) this.initialContent);
        m.append(", block=");
        m.append(this.block);
        m.append(", target=");
        m.append(this.target);
        m.append(')');
        return m.toString();
    }
}
