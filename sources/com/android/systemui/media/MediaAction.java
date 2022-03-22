package com.android.systemui.media;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Icon;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaData.kt */
/* loaded from: classes.dex */
public final class MediaAction {
    public final Runnable action;
    public final CharSequence contentDescription;
    public final Icon icon;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaAction)) {
            return false;
        }
        MediaAction mediaAction = (MediaAction) obj;
        return Intrinsics.areEqual(this.icon, mediaAction.icon) && Intrinsics.areEqual(this.action, mediaAction.action) && Intrinsics.areEqual(this.contentDescription, mediaAction.contentDescription);
    }

    public final int hashCode() {
        Icon icon = this.icon;
        int i = 0;
        int hashCode = (icon == null ? 0 : icon.hashCode()) * 31;
        Runnable runnable = this.action;
        int hashCode2 = (hashCode + (runnable == null ? 0 : runnable.hashCode())) * 31;
        CharSequence charSequence = this.contentDescription;
        if (charSequence != null) {
            i = charSequence.hashCode();
        }
        return hashCode2 + i;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("MediaAction(icon=");
        m.append(this.icon);
        m.append(", action=");
        m.append(this.action);
        m.append(", contentDescription=");
        m.append((Object) this.contentDescription);
        m.append(')');
        return m.toString();
    }

    public MediaAction(Icon icon, Runnable runnable, CharSequence charSequence) {
        this.icon = icon;
        this.action = runnable;
        this.contentDescription = charSequence;
    }
}
