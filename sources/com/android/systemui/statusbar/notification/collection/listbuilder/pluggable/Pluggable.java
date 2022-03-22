package com.android.systemui.statusbar.notification.collection.listbuilder.pluggable;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Trace;
/* loaded from: classes.dex */
public abstract class Pluggable<This> {
    public PluggableListener<This> mListener;
    public final String mName;

    /* loaded from: classes.dex */
    public interface PluggableListener<T> {
        void onPluggableInvalidated(T t);
    }

    public void onCleanup() {
    }

    public final void invalidateList() {
        if (this.mListener != null) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Pluggable<");
            m.append(this.mName);
            m.append(">.invalidateList");
            Trace.beginSection(m.toString());
            this.mListener.onPluggableInvalidated(this);
            Trace.endSection();
        }
    }

    public Pluggable(String str) {
        this.mName = str;
    }
}
