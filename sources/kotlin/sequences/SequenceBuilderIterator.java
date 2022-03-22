package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.ResultKt;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.intrinsics.CoroutineSingletons;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.markers.KMappedMarker;
/* compiled from: SequenceBuilder.kt */
/* loaded from: classes.dex */
public final class SequenceBuilderIterator<T> extends SequenceScope<T> implements Iterator<T>, Continuation<Unit>, KMappedMarker {
    public Iterator<? extends T> nextIterator;
    public Continuation<? super Unit> nextStep;
    public T nextValue;
    public int state;

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public final RuntimeException exceptionalState() {
        int i = this.state;
        if (i == 4) {
            return new NoSuchElementException();
        }
        if (i != 5) {
            return new IllegalStateException(Intrinsics.stringPlus("Unexpected state of the iterator: ", Integer.valueOf(this.state)));
        }
        return new IllegalStateException("Iterator has failed.");
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        while (true) {
            int i = this.state;
            if (i != 0) {
                if (i == 1) {
                    Iterator<? extends T> it = this.nextIterator;
                    Intrinsics.checkNotNull(it);
                    if (it.hasNext()) {
                        this.state = 2;
                        return true;
                    }
                    this.nextIterator = null;
                } else if (i == 2 || i == 3) {
                    return true;
                } else {
                    if (i == 4) {
                        return false;
                    }
                    throw exceptionalState();
                }
            }
            this.state = 5;
            Continuation<? super Unit> continuation = this.nextStep;
            Intrinsics.checkNotNull(continuation);
            this.nextStep = null;
            continuation.resumeWith(Unit.INSTANCE);
        }
    }

    @Override // java.util.Iterator
    public final T next() {
        int i = this.state;
        if (i == 0 || i == 1) {
            if (hasNext()) {
                return next();
            }
            throw new NoSuchElementException();
        } else if (i == 2) {
            this.state = 1;
            Iterator<? extends T> it = this.nextIterator;
            Intrinsics.checkNotNull(it);
            return (T) it.next();
        } else if (i == 3) {
            this.state = 0;
            T t = this.nextValue;
            this.nextValue = null;
            return t;
        } else {
            throw exceptionalState();
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // kotlin.sequences.SequenceScope
    public final void yield(Object obj, Continuation continuation) {
        this.nextValue = obj;
        this.state = 3;
        this.nextStep = continuation;
    }

    @Override // kotlin.coroutines.Continuation
    public final void resumeWith(Object obj) {
        ResultKt.throwOnFailure(obj);
        this.state = 4;
    }

    @Override // kotlin.sequences.SequenceScope
    public final Object yieldAll(Iterator<? extends T> it, Continuation<? super Unit> continuation) {
        if (!it.hasNext()) {
            return Unit.INSTANCE;
        }
        this.nextIterator = it;
        this.state = 2;
        this.nextStep = continuation;
        return CoroutineSingletons.COROUTINE_SUSPENDED;
    }

    @Override // kotlin.coroutines.Continuation
    public final CoroutineContext getContext() {
        return EmptyCoroutineContext.INSTANCE;
    }
}
