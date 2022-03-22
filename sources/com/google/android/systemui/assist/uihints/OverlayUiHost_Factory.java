package com.google.android.systemui.assist.uihints;

import android.content.Context;
import com.android.systemui.telephony.TelephonyListenerManager;
import com.google.android.systemui.elmyra.gates.TelephonyActivity;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class OverlayUiHost_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;
    public final Provider touchOutsideProvider;

    public /* synthetic */ OverlayUiHost_Factory(Provider provider, Provider provider2, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
        this.touchOutsideProvider = provider2;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new OverlayUiHost((Context) this.contextProvider.mo144get(), (TouchOutsideHandler) this.touchOutsideProvider.mo144get());
            default:
                return new TelephonyActivity((Context) this.contextProvider.mo144get(), (TelephonyListenerManager) this.touchOutsideProvider.mo144get());
        }
    }
}
