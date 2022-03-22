package com.android.systemui.controls.controller;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsProviderLifecycleManager.kt */
/* loaded from: classes.dex */
public final class ControlsProviderLifecycleManager$bindService$1 implements Runnable {
    public final /* synthetic */ boolean $bind;
    public final /* synthetic */ ControlsProviderLifecycleManager this$0;

    public ControlsProviderLifecycleManager$bindService$1(ControlsProviderLifecycleManager controlsProviderLifecycleManager, boolean z) {
        this.this$0 = controlsProviderLifecycleManager;
        this.$bind = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.this$0;
        boolean z = this.$bind;
        controlsProviderLifecycleManager.requiresBound = z;
        if (!z) {
            Log.d(controlsProviderLifecycleManager.TAG, Intrinsics.stringPlus("Unbinding service ", controlsProviderLifecycleManager.intent));
            ControlsProviderLifecycleManager controlsProviderLifecycleManager2 = this.this$0;
            controlsProviderLifecycleManager2.bindTryCount = 0;
            if (controlsProviderLifecycleManager2.wrapper != null) {
                controlsProviderLifecycleManager2.context.unbindService(controlsProviderLifecycleManager2.serviceConnection);
            }
            this.this$0.wrapper = null;
        } else if (controlsProviderLifecycleManager.bindTryCount != 5) {
            Log.d(controlsProviderLifecycleManager.TAG, Intrinsics.stringPlus("Binding service ", controlsProviderLifecycleManager.intent));
            ControlsProviderLifecycleManager controlsProviderLifecycleManager3 = this.this$0;
            controlsProviderLifecycleManager3.bindTryCount++;
            try {
                Context context = controlsProviderLifecycleManager3.context;
                Intent intent = controlsProviderLifecycleManager3.intent;
                ControlsProviderLifecycleManager$serviceConnection$1 controlsProviderLifecycleManager$serviceConnection$1 = controlsProviderLifecycleManager3.serviceConnection;
                Objects.requireNonNull(controlsProviderLifecycleManager3);
                context.bindServiceAsUser(intent, controlsProviderLifecycleManager$serviceConnection$1, 67109121, controlsProviderLifecycleManager3.user);
            } catch (SecurityException e) {
                Log.e(this.this$0.TAG, "Failed to bind to service", e);
            }
        }
    }
}
