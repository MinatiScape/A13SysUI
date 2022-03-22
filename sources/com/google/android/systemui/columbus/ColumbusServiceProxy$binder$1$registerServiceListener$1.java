package com.google.android.systemui.columbus;

import android.os.IBinder;
import com.google.android.systemui.columbus.ColumbusServiceProxy;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: ColumbusServiceProxy.kt */
/* loaded from: classes.dex */
public final class ColumbusServiceProxy$binder$1$registerServiceListener$1 extends Lambda implements Function1<ColumbusServiceProxy.ColumbusServiceListener, Boolean> {
    public final /* synthetic */ IBinder $token;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public ColumbusServiceProxy$binder$1$registerServiceListener$1(IBinder iBinder) {
        super(1);
        this.$token = iBinder;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Boolean invoke(ColumbusServiceProxy.ColumbusServiceListener columbusServiceListener) {
        ColumbusServiceProxy.ColumbusServiceListener columbusServiceListener2 = columbusServiceListener;
        boolean z = false;
        if (Intrinsics.areEqual(this.$token, columbusServiceListener2.token)) {
            IBinder iBinder = columbusServiceListener2.token;
            if (iBinder != null) {
                iBinder.unlinkToDeath(columbusServiceListener2, 0);
            }
            z = true;
        }
        return Boolean.valueOf(z);
    }
}
