package com.android.systemui.privacy;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PrivacyItem.kt */
/* loaded from: classes.dex */
public final class PrivacyApplication {
    public final String packageName;
    public final int uid;

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PrivacyApplication)) {
            return false;
        }
        PrivacyApplication privacyApplication = (PrivacyApplication) obj;
        return Intrinsics.areEqual(this.packageName, privacyApplication.packageName) && this.uid == privacyApplication.uid;
    }

    public final int hashCode() {
        return Integer.hashCode(this.uid) + (this.packageName.hashCode() * 31);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("PrivacyApplication(packageName=");
        m.append(this.packageName);
        m.append(", uid=");
        return Insets$$ExternalSyntheticOutline0.m(m, this.uid, ')');
    }

    public PrivacyApplication(String str, int i) {
        this.packageName = str;
        this.uid = i;
    }
}
