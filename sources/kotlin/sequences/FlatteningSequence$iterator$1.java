package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
/* compiled from: Sequences.kt */
/* loaded from: classes.dex */
public final class FlatteningSequence$iterator$1 implements Iterator<E>, KMappedMarker {
    public Iterator<? extends E> itemIterator;
    public final Iterator<T> iterator;
    public final /* synthetic */ FlatteningSequence<T, R, E> this$0;

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public FlatteningSequence$iterator$1(FlatteningSequence<T, R, E> flatteningSequence) {
        this.this$0 = flatteningSequence;
        this.iterator = flatteningSequence.sequence.iterator();
    }

    /* JADX WARN: Code restructure failed: missing block: B:18:0x003f, code lost:
        return true;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final boolean ensureItemIterator() {
        /*
            r5 = this;
            java.util.Iterator<? extends E> r0 = r5.itemIterator
            r1 = 1
            r2 = 0
            if (r0 != 0) goto L_0x0008
        L_0x0006:
            r0 = r2
            goto L_0x000f
        L_0x0008:
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L_0x0006
            r0 = r1
        L_0x000f:
            if (r0 == 0) goto L_0x0014
            r0 = 0
            r5.itemIterator = r0
        L_0x0014:
            java.util.Iterator<? extends E> r0 = r5.itemIterator
            if (r0 != 0) goto L_0x003f
            java.util.Iterator<T> r0 = r5.iterator
            boolean r0 = r0.hasNext()
            if (r0 != 0) goto L_0x0021
            return r2
        L_0x0021:
            java.util.Iterator<T> r0 = r5.iterator
            java.lang.Object r0 = r0.next()
            kotlin.sequences.FlatteningSequence<T, R, E> r3 = r5.this$0
            kotlin.jvm.functions.Function1<R, java.util.Iterator<E>> r4 = r3.iterator
            kotlin.jvm.functions.Function1<T, R> r3 = r3.transformer
            java.lang.Object r0 = r3.invoke(r0)
            java.lang.Object r0 = r4.invoke(r0)
            java.util.Iterator r0 = (java.util.Iterator) r0
            boolean r3 = r0.hasNext()
            if (r3 == 0) goto L_0x0014
            r5.itemIterator = r0
        L_0x003f:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.sequences.FlatteningSequence$iterator$1.ensureItemIterator():boolean");
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        return ensureItemIterator();
    }

    /* JADX WARN: Type inference failed for: r1v3, types: [E, java.lang.Object] */
    @Override // java.util.Iterator
    public final E next() {
        if (ensureItemIterator()) {
            Iterator<? extends E> it = this.itemIterator;
            Intrinsics.checkNotNull(it);
            return it.next();
        }
        throw new NoSuchElementException();
    }
}
