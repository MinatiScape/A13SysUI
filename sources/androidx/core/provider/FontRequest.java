package androidx.core.provider;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.util.Base64;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public final class FontRequest {
    public final List<List<byte[]>> mCertificates;
    public final String mIdentifier;
    public final String mProviderAuthority;
    public final String mProviderPackage;
    public final String mQuery;

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("FontRequest {mProviderAuthority: ");
        m.append(this.mProviderAuthority);
        m.append(", mProviderPackage: ");
        m.append(this.mProviderPackage);
        m.append(", mQuery: ");
        m.append(this.mQuery);
        m.append(", mCertificates:");
        sb.append(m.toString());
        for (int i = 0; i < this.mCertificates.size(); i++) {
            sb.append(" [");
            List<byte[]> list = this.mCertificates.get(i);
            for (int i2 = 0; i2 < list.size(); i2++) {
                sb.append(" \"");
                sb.append(Base64.encodeToString(list.get(i2), 0));
                sb.append("\"");
            }
            sb.append(" ]");
        }
        return MotionController$$ExternalSyntheticOutline1.m(sb, "}", "mCertificatesArray: 0");
    }

    public FontRequest(String str, String str2, String str3, List<List<byte[]>> list) {
        Objects.requireNonNull(str);
        this.mProviderAuthority = str;
        Objects.requireNonNull(str2);
        this.mProviderPackage = str2;
        this.mQuery = str3;
        Objects.requireNonNull(list);
        this.mCertificates = list;
        this.mIdentifier = str + "-" + str2 + "-" + str3;
    }
}
