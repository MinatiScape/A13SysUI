package com.android.systemui.statusbar.notification.collection;

import android.view.Choreographer;
import com.android.systemui.util.ListenerSet;
import com.android.systemui.util.concurrency.DelayableExecutor;
import com.android.systemui.wmshell.WMShell$7$$ExternalSyntheticLambda1;
import java.util.Iterator;
import java.util.Objects;
/* compiled from: NotifPipelineChoreographer.kt */
/* loaded from: classes.dex */
public final class NotifPipelineChoreographerImpl implements NotifPipelineChoreographer {
    public final DelayableExecutor executor;
    public boolean isScheduled;
    public Runnable timeoutSubscription;
    public final Choreographer viewChoreographer;
    public final ListenerSet<Runnable> listeners = new ListenerSet<>();
    public final NotifPipelineChoreographerImpl$frameCallback$1 frameCallback = new Choreographer.FrameCallback() { // from class: com.android.systemui.statusbar.notification.collection.NotifPipelineChoreographerImpl$frameCallback$1
        @Override // android.view.Choreographer.FrameCallback
        public final void doFrame(long j) {
            NotifPipelineChoreographerImpl notifPipelineChoreographerImpl = NotifPipelineChoreographerImpl.this;
            if (notifPipelineChoreographerImpl.isScheduled) {
                notifPipelineChoreographerImpl.isScheduled = false;
                Runnable runnable = notifPipelineChoreographerImpl.timeoutSubscription;
                if (runnable != null) {
                    runnable.run();
                }
                for (Runnable runnable2 : NotifPipelineChoreographerImpl.this.listeners) {
                    runnable2.run();
                }
            }
        }
    };

    @Override // com.android.systemui.statusbar.notification.collection.NotifPipelineChoreographer
    public final void addOnEvalListener(WMShell$7$$ExternalSyntheticLambda1 wMShell$7$$ExternalSyntheticLambda1) {
        this.listeners.addIfAbsent(wMShell$7$$ExternalSyntheticLambda1);
    }

    @Override // com.android.systemui.statusbar.notification.collection.NotifPipelineChoreographer
    public final void schedule() {
        if (!this.isScheduled) {
            this.isScheduled = true;
            this.viewChoreographer.postFrameCallback(this.frameCallback);
            if (this.isScheduled) {
                this.timeoutSubscription = this.executor.executeDelayed(new Runnable() { // from class: com.android.systemui.statusbar.notification.collection.NotifPipelineChoreographerImpl$schedule$1
                    @Override // java.lang.Runnable
                    public final void run() {
                        NotifPipelineChoreographerImpl notifPipelineChoreographerImpl = NotifPipelineChoreographerImpl.this;
                        Objects.requireNonNull(notifPipelineChoreographerImpl);
                        if (notifPipelineChoreographerImpl.isScheduled) {
                            notifPipelineChoreographerImpl.isScheduled = false;
                            notifPipelineChoreographerImpl.viewChoreographer.removeFrameCallback(notifPipelineChoreographerImpl.frameCallback);
                            Iterator<Runnable> it = notifPipelineChoreographerImpl.listeners.iterator();
                            while (it.hasNext()) {
                                it.next().run();
                            }
                        }
                    }
                }, 100L);
            }
        }
    }

    /* JADX WARN: Type inference failed for: r1v2, types: [com.android.systemui.statusbar.notification.collection.NotifPipelineChoreographerImpl$frameCallback$1] */
    public NotifPipelineChoreographerImpl(Choreographer choreographer, DelayableExecutor delayableExecutor) {
        this.viewChoreographer = choreographer;
        this.executor = delayableExecutor;
    }
}
