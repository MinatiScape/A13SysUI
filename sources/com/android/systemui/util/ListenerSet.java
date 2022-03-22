package com.android.systemui.util;

import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;
import kotlin.jvm.internal.markers.KMappedMarker;
/* compiled from: ListenerSet.kt */
/* loaded from: classes.dex */
public final class ListenerSet<E> implements Iterable<E>, KMappedMarker {
    public final CopyOnWriteArrayList<E> listeners = new CopyOnWriteArrayList<>();

    public final boolean addIfAbsent(E e) {
        return this.listeners.addIfAbsent(e);
    }

    @Override // java.lang.Iterable
    public final Iterator<E> iterator() {
        return this.listeners.iterator();
    }

    public final boolean remove(E e) {
        return this.listeners.remove(e);
    }
}
