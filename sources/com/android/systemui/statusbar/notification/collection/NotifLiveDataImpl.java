package com.android.systemui.statusbar.notification.collection;

import androidx.lifecycle.Observer;
import com.android.systemui.statusbar.phone.LightsOutNotifController$$ExternalSyntheticLambda0;
import com.android.systemui.util.ListenerSet;
import java.io.Serializable;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicReference;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotifLiveDataStoreImpl.kt */
/* loaded from: classes.dex */
public final class NotifLiveDataImpl<T> {
    public final AtomicReference<T> atomicValue;
    public T lastAsyncValue;
    public final Executor mainExecutor;
    public final String name;
    public final ListenerSet<Observer<T>> syncObservers = new ListenerSet<>();
    public final ListenerSet<Observer<T>> asyncObservers = new ListenerSet<>();

    public final void addSyncObserver(LightsOutNotifController$$ExternalSyntheticLambda0 lightsOutNotifController$$ExternalSyntheticLambda0) {
        this.syncObservers.addIfAbsent(lightsOutNotifController$$ExternalSyntheticLambda0);
    }

    public final T getValue() {
        return this.atomicValue.get();
    }

    public final void removeObserver(LightsOutNotifController$$ExternalSyntheticLambda0 lightsOutNotifController$$ExternalSyntheticLambda0) {
        this.syncObservers.remove(lightsOutNotifController$$ExternalSyntheticLambda0);
        this.asyncObservers.remove(lightsOutNotifController$$ExternalSyntheticLambda0);
    }

    public final Function0<Unit> setValueAndProvideDispatcher(T t) {
        if (!Intrinsics.areEqual(this.atomicValue.getAndSet(t), t)) {
            return new NotifLiveDataImpl$setValueAndProvideDispatcher$1(this, t);
        }
        return NotifLiveDataImpl$setValueAndProvideDispatcher$2.INSTANCE;
    }

    public NotifLiveDataImpl(String str, Serializable serializable, Executor executor) {
        this.name = str;
        this.mainExecutor = executor;
        this.atomicValue = new AtomicReference<>(serializable);
    }
}
