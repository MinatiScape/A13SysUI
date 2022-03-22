package com.google.android.systemui.elmyra;

import android.content.Context;
/* loaded from: classes.dex */
public final class SnapshotConfiguration {
    public final int mCompleteGestures;
    public final int mIncompleteGestures;
    public final int mSnapshotDelayAfterGesture;

    public SnapshotConfiguration(Context context) {
        this.mCompleteGestures = context.getResources().getInteger(2131492928);
        this.mIncompleteGestures = context.getResources().getInteger(2131492929);
        this.mSnapshotDelayAfterGesture = context.getResources().getInteger(2131492927);
    }
}
