package com.android.systemui.qs.external;

import android.util.Log;
import java.util.function.Consumer;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TileServiceRequestController.kt */
/* loaded from: classes.dex */
public final class TileServiceRequestController$TileServiceRequestCommand$execute$1<T> implements Consumer {
    public static final TileServiceRequestController$TileServiceRequestCommand$execute$1<T> INSTANCE = new TileServiceRequestController$TileServiceRequestCommand$execute$1<>();

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        Log.d("TileServiceRequestController", Intrinsics.stringPlus("Response: ", Integer.valueOf(((Number) obj).intValue())));
    }
}
