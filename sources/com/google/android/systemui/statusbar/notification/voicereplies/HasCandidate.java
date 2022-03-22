package com.google.android.systemui.statusbar.notification.voicereplies;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes.dex */
public final class HasCandidate extends VoiceReplyState {
    public final VoiceReplyTarget candidate;

    public HasCandidate(VoiceReplyTarget voiceReplyTarget) {
        super(0);
        this.candidate = voiceReplyTarget;
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof HasCandidate) && Intrinsics.areEqual(this.candidate, ((HasCandidate) obj).candidate);
    }

    public final int hashCode() {
        return this.candidate.hashCode();
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("HasCandidate(candidate=");
        m.append(this.candidate);
        m.append(')');
        return m.toString();
    }
}
