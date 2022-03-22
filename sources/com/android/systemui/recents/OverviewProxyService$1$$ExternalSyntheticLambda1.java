package com.android.systemui.recents;

import android.os.Binder;
import com.android.systemui.recents.OverviewProxyService;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: R8$$SyntheticClass */
/* loaded from: classes.dex */
public final /* synthetic */ class OverviewProxyService$1$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ Binder f$0;
    public final /* synthetic */ float f$1;

    public /* synthetic */ OverviewProxyService$1$$ExternalSyntheticLambda1(Binder binder, float f, int i) {
        this.$r8$classId = i;
        this.f$0 = binder;
        this.f$1 = f;
    }

    @Override // java.lang.Runnable
    public final void run() {
        switch (this.$r8$classId) {
            case 0:
                OverviewProxyService.AnonymousClass1 r0 = (OverviewProxyService.AnonymousClass1) this.f$0;
                float f = this.f$1;
                Objects.requireNonNull(r0);
                OverviewProxyService overviewProxyService = OverviewProxyService.this;
                Objects.requireNonNull(overviewProxyService);
                int size = overviewProxyService.mConnectionCallbacks.size();
                while (true) {
                    size--;
                    if (size >= 0) {
                        ((OverviewProxyService.OverviewProxyListener) overviewProxyService.mConnectionCallbacks.get(size)).onAssistantGestureCompletion(f);
                    } else {
                        return;
                    }
                }
            default:
                PinnedStackListenerForwarder.PinnedTaskListenerImpl pinnedTaskListenerImpl = (PinnedStackListenerForwarder.PinnedTaskListenerImpl) this.f$0;
                float f2 = this.f$1;
                int i = PinnedStackListenerForwarder.PinnedTaskListenerImpl.$r8$clinit;
                Objects.requireNonNull(pinnedTaskListenerImpl);
                PinnedStackListenerForwarder pinnedStackListenerForwarder = PinnedStackListenerForwarder.this;
                Objects.requireNonNull(pinnedStackListenerForwarder);
                Iterator<PinnedStackListenerForwarder.PinnedTaskListener> it = pinnedStackListenerForwarder.mListeners.iterator();
                while (it.hasNext()) {
                    it.next().onAspectRatioChanged(f2);
                }
                return;
        }
    }
}
