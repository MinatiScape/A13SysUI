package com.android.systemui.statusbar.notification.collection;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.os.Trace;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline1;
import androidx.lifecycle.Observer;
import com.android.systemui.util.ListenerSet;
import java.util.Iterator;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Lambda;
/* compiled from: NotifLiveDataStoreImpl.kt */
/* loaded from: classes.dex */
public final class NotifLiveDataImpl$setValueAndProvideDispatcher$1 extends Lambda implements Function0<Unit> {
    public final /* synthetic */ T $value;
    public final /* synthetic */ NotifLiveDataImpl<T> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public NotifLiveDataImpl$setValueAndProvideDispatcher$1(NotifLiveDataImpl<T> notifLiveDataImpl, T t) {
        super(0);
        this.this$0 = notifLiveDataImpl;
        this.$value = t;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.jvm.functions.Function0
    public final Unit invoke() {
        ListenerSet<Observer<T>> listenerSet = this.this$0.syncObservers;
        Objects.requireNonNull(listenerSet);
        if (!listenerSet.listeners.isEmpty()) {
            String m = MotionController$$ExternalSyntheticOutline1.m(VendorAtomValue$$ExternalSyntheticOutline1.m("NotifLiveData("), this.this$0.name, ").dispatchToSyncObservers");
            NotifLiveDataImpl<T> notifLiveDataImpl = this.this$0;
            T t = this.$value;
            Trace.beginSection(m);
            try {
                Iterator it = notifLiveDataImpl.syncObservers.iterator();
                while (it.hasNext()) {
                    ((Observer) it.next()).onChanged(t);
                }
            } finally {
                Trace.endSection();
            }
        }
        ListenerSet<Observer<T>> listenerSet2 = this.this$0.asyncObservers;
        Objects.requireNonNull(listenerSet2);
        if (!listenerSet2.listeners.isEmpty()) {
            final NotifLiveDataImpl<T> notifLiveDataImpl2 = this.this$0;
            notifLiveDataImpl2.mainExecutor.execute(new Runnable() { // from class: com.android.systemui.statusbar.notification.collection.NotifLiveDataImpl$setValueAndProvideDispatcher$1.2
                /* JADX WARN: Type inference failed for: r0v1, types: [T, java.lang.Object] */
                /* JADX WARN: Unknown variable types count: 1 */
                @Override // java.lang.Runnable
                /*
                    Code decompiled incorrectly, please refer to instructions dump.
                    To view partially-correct add '--show-bad-code' argument
                */
                public final void run() {
                    /*
                        r3 = this;
                        com.android.systemui.statusbar.notification.collection.NotifLiveDataImpl<java.lang.Object> r3 = r1
                        java.util.Objects.requireNonNull(r3)
                        java.util.concurrent.atomic.AtomicReference<T> r0 = r3.atomicValue
                        java.lang.Object r0 = r0.get()
                        T r1 = r3.lastAsyncValue
                        boolean r1 = kotlin.jvm.internal.Intrinsics.areEqual(r1, r0)
                        if (r1 != 0) goto L_0x004b
                        r3.lastAsyncValue = r0
                        java.lang.String r1 = "NotifLiveData("
                        java.lang.StringBuilder r1 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r1)
                        java.lang.String r2 = r3.name
                        r1.append(r2)
                        java.lang.String r2 = ").dispatchToAsyncObservers"
                        r1.append(r2)
                        java.lang.String r1 = r1.toString()
                        android.os.Trace.beginSection(r1)
                        com.android.systemui.util.ListenerSet<androidx.lifecycle.Observer<T>> r3 = r3.asyncObservers     // Catch: all -> 0x0046
                        java.util.Iterator r3 = r3.iterator()     // Catch: all -> 0x0046
                    L_0x0032:
                        boolean r1 = r3.hasNext()     // Catch: all -> 0x0046
                        if (r1 == 0) goto L_0x0042
                        java.lang.Object r1 = r3.next()     // Catch: all -> 0x0046
                        androidx.lifecycle.Observer r1 = (androidx.lifecycle.Observer) r1     // Catch: all -> 0x0046
                        r1.onChanged(r0)     // Catch: all -> 0x0046
                        goto L_0x0032
                    L_0x0042:
                        android.os.Trace.endSection()
                        goto L_0x004b
                    L_0x0046:
                        r3 = move-exception
                        android.os.Trace.endSection()
                        throw r3
                    L_0x004b:
                        return
                    */
                    throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.collection.NotifLiveDataImpl$setValueAndProvideDispatcher$1.AnonymousClass2.run():void");
                }
            });
        }
        return Unit.INSTANCE;
    }
}
