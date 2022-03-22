package com.android.systemui.statusbar.notification;

import android.util.Pools;
/* loaded from: classes.dex */
public final class ProgressTransformState extends TransformState {
    public static Pools.SimplePool<ProgressTransformState> sInstancePool = new Pools.SimplePool<>(40);

    @Override // com.android.systemui.statusbar.notification.TransformState
    public final boolean sameAs(TransformState transformState) {
        if (transformState instanceof ProgressTransformState) {
            return true;
        }
        return this.mSameAsAny;
    }

    @Override // com.android.systemui.statusbar.notification.TransformState
    public final void recycle() {
        super.recycle();
        sInstancePool.release(this);
    }
}
