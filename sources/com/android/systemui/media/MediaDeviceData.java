package com.android.systemui.media;

import android.app.PendingIntent;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.graphics.drawable.Drawable;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaData.kt */
/* loaded from: classes.dex */
public final class MediaDeviceData {
    public final boolean enabled;
    public final Drawable icon;
    public final PendingIntent intent;
    public final CharSequence name;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaDeviceData)) {
            return false;
        }
        MediaDeviceData mediaDeviceData = (MediaDeviceData) obj;
        return this.enabled == mediaDeviceData.enabled && Intrinsics.areEqual(this.icon, mediaDeviceData.icon) && Intrinsics.areEqual(this.name, mediaDeviceData.name) && Intrinsics.areEqual(this.intent, mediaDeviceData.intent);
    }

    public final int hashCode() {
        boolean z = this.enabled;
        if (z) {
            z = true;
        }
        int i = z ? 1 : 0;
        int i2 = z ? 1 : 0;
        int i3 = i * 31;
        Drawable drawable = this.icon;
        int i4 = 0;
        int hashCode = (i3 + (drawable == null ? 0 : drawable.hashCode())) * 31;
        CharSequence charSequence = this.name;
        int hashCode2 = (hashCode + (charSequence == null ? 0 : charSequence.hashCode())) * 31;
        PendingIntent pendingIntent = this.intent;
        if (pendingIntent != null) {
            i4 = pendingIntent.hashCode();
        }
        return hashCode2 + i4;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("MediaDeviceData(enabled=");
        m.append(this.enabled);
        m.append(", icon=");
        m.append(this.icon);
        m.append(", name=");
        m.append((Object) this.name);
        m.append(", intent=");
        m.append(this.intent);
        m.append(')');
        return m.toString();
    }

    public MediaDeviceData(boolean z, Drawable drawable, CharSequence charSequence, PendingIntent pendingIntent) {
        this.enabled = z;
        this.icon = drawable;
        this.name = charSequence;
        this.intent = pendingIntent;
    }
}
