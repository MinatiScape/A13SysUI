package com.android.systemui.controls.controller;

import android.os.IBinder;
import android.service.controls.Control;
import android.service.controls.IControlsSubscriber;
import android.service.controls.IControlsSubscription;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: StatefulControlSubscriber.kt */
/* loaded from: classes.dex */
public final class StatefulControlSubscriber extends IControlsSubscriber.Stub {
    public final DelayableExecutor bgExecutor;
    public final ControlsController controller;
    public final ControlsProviderLifecycleManager provider;
    public final long requestLimit = 100000;
    public IControlsSubscription subscription;
    public boolean subscriptionOpen;

    public final void onComplete(IBinder iBinder) {
        run(iBinder, new StatefulControlSubscriber$onComplete$1(this));
    }

    public final void onError(IBinder iBinder, String str) {
        run(iBinder, new StatefulControlSubscriber$onError$1(this, str));
    }

    public final void onNext(IBinder iBinder, Control control) {
        run(iBinder, new StatefulControlSubscriber$onNext$1(this, iBinder, control));
    }

    public final void onSubscribe(IBinder iBinder, IControlsSubscription iControlsSubscription) {
        run(iBinder, new StatefulControlSubscriber$onSubscribe$1(this, iControlsSubscription));
    }

    public final void run(IBinder iBinder, final Function0<Unit> function0) {
        ControlsProviderLifecycleManager controlsProviderLifecycleManager = this.provider;
        Objects.requireNonNull(controlsProviderLifecycleManager);
        if (Intrinsics.areEqual(controlsProviderLifecycleManager.token, iBinder)) {
            this.bgExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.controller.StatefulControlSubscriber$run$1
                @Override // java.lang.Runnable
                public final void run() {
                    function0.invoke();
                }
            });
        }
    }

    public StatefulControlSubscriber(ControlsController controlsController, ControlsProviderLifecycleManager controlsProviderLifecycleManager, DelayableExecutor delayableExecutor) {
        this.controller = controlsController;
        this.provider = controlsProviderLifecycleManager;
        this.bgExecutor = delayableExecutor;
    }
}
