package com.android.wm.shell.dagger;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import com.android.wm.shell.common.HandlerExecutor;
import com.android.wm.shell.common.ShellExecutor;
import dagger.internal.Factory;
/* loaded from: classes.dex */
public final class WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory implements Factory<ShellExecutor> {

    /* loaded from: classes.dex */
    public static final class InstanceHolder {
        public static final WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory INSTANCE = new WMShellConcurrencyModule_ProvideShellAnimationExecutorFactory();
    }

    @Override // javax.inject.Provider
    /* renamed from: get */
    public final Object mo144get() {
        HandlerThread handlerThread = new HandlerThread("wmshell.anim", -4);
        handlerThread.start();
        if (Build.IS_DEBUGGABLE) {
            handlerThread.getLooper().setTraceTag(32L);
            handlerThread.getLooper().setSlowLogThresholdMs(30L, 30L);
        }
        return new HandlerExecutor(Handler.createAsync(handlerThread.getLooper()));
    }
}
