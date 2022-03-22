package com.android.launcher3.icons;

import android.content.Context;
/* loaded from: classes.dex */
public final class IconFactory extends BaseIconFactory {
    public static IconFactory sPool;
    public static final Object sPoolSync = new Object();
    public final int mPoolId = 0;
    public IconFactory next;

    public IconFactory(Context context, int i, int i2) {
        super(context, i, i2, false);
    }

    @Override // com.android.launcher3.icons.BaseIconFactory, java.lang.AutoCloseable
    public final void close() {
        synchronized (sPoolSync) {
            if (this.mPoolId == 0) {
                this.mWrapperBackgroundColor = -1;
                this.next = sPool;
                sPool = this;
            }
        }
    }
}
