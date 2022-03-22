package com.android.systemui.keyguard;

import com.android.systemui.keyguard.ScreenLifecycle;
import com.android.systemui.unfold.updates.screen.ScreenStatusProvider;
import java.util.ArrayList;
import java.util.Iterator;
/* compiled from: LifecycleScreenStatusProvider.kt */
/* loaded from: classes.dex */
public final class LifecycleScreenStatusProvider implements ScreenStatusProvider, ScreenLifecycle.Observer {
    public final ArrayList listeners = new ArrayList();

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void addCallback(ScreenStatusProvider.ScreenListener screenListener) {
        this.listeners.add(screenListener);
    }

    @Override // com.android.systemui.keyguard.ScreenLifecycle.Observer
    public final void onScreenTurnedOn() {
        Iterator it = this.listeners.iterator();
        while (it.hasNext()) {
            ((ScreenStatusProvider.ScreenListener) it.next()).onScreenTurnedOn();
        }
    }

    @Override // com.android.systemui.statusbar.policy.CallbackController
    public final void removeCallback(ScreenStatusProvider.ScreenListener screenListener) {
        this.listeners.remove(screenListener);
    }

    public LifecycleScreenStatusProvider(ScreenLifecycle screenLifecycle) {
        screenLifecycle.mObservers.add(this);
    }
}
