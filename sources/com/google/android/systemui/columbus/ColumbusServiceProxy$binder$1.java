package com.google.android.systemui.columbus;

import android.os.IBinder;
import android.os.IInterface;
import android.util.Log;
import com.google.android.systemui.columbus.ColumbusServiceProxy;
import com.google.android.systemui.columbus.IColumbusService;
import com.google.android.systemui.columbus.IColumbusServiceListener;
import java.util.ArrayList;
import java.util.Objects;
import kotlin.collections.CollectionsKt__ReversedViewsKt;
/* compiled from: ColumbusServiceProxy.kt */
/* loaded from: classes.dex */
public final class ColumbusServiceProxy$binder$1 extends IColumbusService.Stub {
    public final /* synthetic */ ColumbusServiceProxy this$0;

    public ColumbusServiceProxy$binder$1(ColumbusServiceProxy columbusServiceProxy) {
        this.this$0 = columbusServiceProxy;
    }

    @Override // com.google.android.systemui.columbus.IColumbusService
    public final void registerServiceListener(IBinder iBinder, IBinder iBinder2) {
        IColumbusServiceListener iColumbusServiceListener;
        ColumbusServiceProxy columbusServiceProxy = this.this$0;
        int i = ColumbusServiceProxy.$r8$clinit;
        Objects.requireNonNull(columbusServiceProxy);
        columbusServiceProxy.enforceCallingOrSelfPermission("com.google.android.columbus.permission.CONFIGURE_COLUMBUS_GESTURE", "Must have com.google.android.columbus.permission.CONFIGURE_COLUMBUS_GESTURE permission");
        if (iBinder == null) {
            Log.e("Columbus/ColumbusProxy", "Binder token must not be null");
        } else if (iBinder2 == null) {
            CollectionsKt__ReversedViewsKt.removeAll(this.this$0.columbusServiceListeners, new ColumbusServiceProxy$binder$1$registerServiceListener$1(iBinder));
        } else {
            ArrayList arrayList = this.this$0.columbusServiceListeners;
            IInterface queryLocalInterface = iBinder2.queryLocalInterface("com.google.android.systemui.columbus.IColumbusServiceListener");
            if (queryLocalInterface == null || !(queryLocalInterface instanceof IColumbusServiceListener)) {
                iColumbusServiceListener = new IColumbusServiceListener.Stub.Proxy(iBinder2);
            } else {
                iColumbusServiceListener = (IColumbusServiceListener) queryLocalInterface;
            }
            arrayList.add(new ColumbusServiceProxy.ColumbusServiceListener(iBinder, iColumbusServiceListener));
        }
    }
}
