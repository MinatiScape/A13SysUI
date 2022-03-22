package com.android.systemui.media;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaData.kt */
/* loaded from: classes.dex */
public final class MediaButton {
    public MediaAction endCustom;
    public MediaAction nextOrCustom;
    public MediaAction playOrPause;
    public MediaAction prevOrCustom;
    public MediaAction startCustom;

    public MediaButton() {
        this(0);
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof MediaButton)) {
            return false;
        }
        MediaButton mediaButton = (MediaButton) obj;
        return Intrinsics.areEqual(this.playOrPause, mediaButton.playOrPause) && Intrinsics.areEqual(this.nextOrCustom, mediaButton.nextOrCustom) && Intrinsics.areEqual(this.prevOrCustom, mediaButton.prevOrCustom) && Intrinsics.areEqual(this.startCustom, mediaButton.startCustom) && Intrinsics.areEqual(this.endCustom, mediaButton.endCustom);
    }

    public final int hashCode() {
        MediaAction mediaAction = this.playOrPause;
        int i = 0;
        int hashCode = (mediaAction == null ? 0 : mediaAction.hashCode()) * 31;
        MediaAction mediaAction2 = this.nextOrCustom;
        int hashCode2 = (hashCode + (mediaAction2 == null ? 0 : mediaAction2.hashCode())) * 31;
        MediaAction mediaAction3 = this.prevOrCustom;
        int hashCode3 = (hashCode2 + (mediaAction3 == null ? 0 : mediaAction3.hashCode())) * 31;
        MediaAction mediaAction4 = this.startCustom;
        int hashCode4 = (hashCode3 + (mediaAction4 == null ? 0 : mediaAction4.hashCode())) * 31;
        MediaAction mediaAction5 = this.endCustom;
        if (mediaAction5 != null) {
            i = mediaAction5.hashCode();
        }
        return hashCode4 + i;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("MediaButton(playOrPause=");
        m.append(this.playOrPause);
        m.append(", nextOrCustom=");
        m.append(this.nextOrCustom);
        m.append(", prevOrCustom=");
        m.append(this.prevOrCustom);
        m.append(", startCustom=");
        m.append(this.startCustom);
        m.append(", endCustom=");
        m.append(this.endCustom);
        m.append(')');
        return m.toString();
    }

    public MediaButton(int i) {
        this.playOrPause = null;
        this.nextOrCustom = null;
        this.prevOrCustom = null;
        this.startCustom = null;
        this.endCustom = null;
    }
}
