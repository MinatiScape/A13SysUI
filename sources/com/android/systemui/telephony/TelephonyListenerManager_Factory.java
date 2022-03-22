package com.android.systemui.telephony;

import android.content.Context;
import android.telephony.TelephonyManager;
import com.google.android.systemui.assist.AssistManagerGoogle;
import com.google.android.systemui.columbus.gates.TelephonyActivity;
import com.google.android.systemui.elmyra.feedback.AssistInvocationEffect;
import com.google.android.systemui.elmyra.feedback.OpaHomeButton;
import com.google.android.systemui.elmyra.feedback.OpaLockscreen;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TelephonyListenerManager_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider executorProvider;
    public final Provider telephonyCallbackProvider;
    public final Provider telephonyManagerProvider;

    public /* synthetic */ TelephonyListenerManager_Factory(Provider provider, Provider provider2, Provider provider3, int i) {
        this.$r8$classId = i;
        this.telephonyManagerProvider = provider;
        this.executorProvider = provider2;
        this.telephonyCallbackProvider = provider3;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new TelephonyListenerManager((TelephonyManager) this.telephonyManagerProvider.mo144get(), (Executor) this.executorProvider.mo144get(), (TelephonyCallback) this.telephonyCallbackProvider.mo144get());
            case 1:
                return new TelephonyActivity((Context) this.telephonyManagerProvider.mo144get(), DoubleCheck.lazy(this.executorProvider), DoubleCheck.lazy(this.telephonyCallbackProvider));
            default:
                return new AssistInvocationEffect((AssistManagerGoogle) this.telephonyManagerProvider.mo144get(), (OpaHomeButton) this.executorProvider.mo144get(), (OpaLockscreen) this.telephonyCallbackProvider.mo144get());
        }
    }
}
