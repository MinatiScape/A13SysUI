package com.android.systemui.dreams.complication;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
/* loaded from: classes.dex */
public final class ComplicationId {
    public int mId;

    /* loaded from: classes.dex */
    public static class Factory {
        public int mNextId;
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("ComplicationId{mId=");
        m.append(this.mId);
        m.append("}");
        return m.toString();
    }

    public ComplicationId(int i) {
        this.mId = i;
    }
}
