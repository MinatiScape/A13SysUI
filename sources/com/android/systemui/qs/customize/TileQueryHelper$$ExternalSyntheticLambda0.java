package com.android.systemui.qs.customize;

import com.android.systemui.qs.customize.TileQueryHelper;
import java.util.ArrayList;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class TileQueryHelper$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TileQueryHelper f$0;
    public final /* synthetic */ ArrayList f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ TileQueryHelper$$ExternalSyntheticLambda0(TileQueryHelper tileQueryHelper, ArrayList arrayList, boolean z) {
        this.f$0 = tileQueryHelper;
        this.f$1 = arrayList;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        TileQueryHelper tileQueryHelper = this.f$0;
        ArrayList arrayList = this.f$1;
        boolean z = this.f$2;
        Objects.requireNonNull(tileQueryHelper);
        TileQueryHelper.TileStateListener tileStateListener = tileQueryHelper.mListener;
        if (tileStateListener != null) {
            TileAdapter tileAdapter = (TileAdapter) tileStateListener;
            tileAdapter.mAllTiles = arrayList;
            tileAdapter.recalcSpecs();
        }
        tileQueryHelper.mFinished = z;
    }
}
