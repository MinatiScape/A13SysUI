package com.android.systemui.navigationbar;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import com.android.systemui.recents.OverviewProxyRecentsImpl;
import com.android.wm.shell.TaskViewTransitions;
import com.android.wm.shell.transition.Transitions;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TaskbarDelegate_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider contextProvider;

    public /* synthetic */ TaskbarDelegate_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new TaskbarDelegate((Context) this.contextProvider.mo144get());
            case 1:
                return (FingerprintManager) ((Context) this.contextProvider.mo144get()).getSystemService(FingerprintManager.class);
            case 2:
                return new OverviewProxyRecentsImpl(DoubleCheck.lazy(this.contextProvider));
            default:
                return new TaskViewTransitions((Transitions) this.contextProvider.mo144get());
        }
    }
}
