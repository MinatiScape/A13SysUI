package androidx.media;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.AudioAttributes;
/* loaded from: classes.dex */
public class AudioAttributesImplApi21 implements AudioAttributesImpl {
    public AudioAttributes mAudioAttributes;
    public int mLegacyStreamType = -1;

    public final boolean equals(Object obj) {
        if (!(obj instanceof AudioAttributesImplApi21)) {
            return false;
        }
        return this.mAudioAttributes.equals(((AudioAttributesImplApi21) obj).mAudioAttributes);
    }

    public final int hashCode() {
        return this.mAudioAttributes.hashCode();
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("AudioAttributesCompat: audioattributes=");
        m.append(this.mAudioAttributes);
        return m.toString();
    }
}
