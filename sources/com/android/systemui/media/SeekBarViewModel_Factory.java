package com.android.systemui.media;

import android.content.Context;
import android.os.Handler;
import com.android.systemui.util.concurrency.RepeatableExecutor;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.google.android.systemui.assist.OpaEnabledDispatcher;
import com.google.android.systemui.columbus.PowerManagerWrapper;
import dagger.internal.DoubleCheck;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SeekBarViewModel_Factory implements Factory {
    public final /* synthetic */ int $r8$classId;
    public final Provider bgExecutorProvider;

    public /* synthetic */ SeekBarViewModel_Factory(Provider provider, int i) {
        this.$r8$classId = i;
        this.bgExecutorProvider = provider;
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        switch (this.$r8$classId) {
            case 0:
                return new SeekBarViewModel((RepeatableExecutor) this.bgExecutorProvider.mo144get());
            case 1:
                return new TaskStackListenerImpl((Handler) this.bgExecutorProvider.mo144get());
            case 2:
                return new OpaEnabledDispatcher(DoubleCheck.lazy(this.bgExecutorProvider));
            default:
                return new PowerManagerWrapper((Context) this.bgExecutorProvider.mo144get());
        }
    }
}
