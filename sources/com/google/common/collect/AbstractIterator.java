package com.google.common.collect;

import com.google.errorprone.annotations.CanIgnoreReturnValue;
import java.util.NoSuchElementException;
/* loaded from: classes.dex */
public abstract class AbstractIterator<T> extends UnmodifiableIterator<T> {
    public T next;
    public State state = State.NOT_READY;

    /* loaded from: classes.dex */
    public enum State {
        READY,
        NOT_READY,
        DONE,
        FAILED
    }

    public abstract T computeNext();

    @Override // java.util.Iterator
    @CanIgnoreReturnValue
    public final boolean hasNext() {
        boolean z;
        State state = this.state;
        State state2 = State.FAILED;
        if (state != state2) {
            z = true;
        } else {
            z = false;
        }
        if (z) {
            int ordinal = state.ordinal();
            if (ordinal == 0) {
                return true;
            }
            if (ordinal == 2) {
                return false;
            }
            this.state = state2;
            this.next = computeNext();
            if (this.state == State.DONE) {
                return false;
            }
            this.state = State.READY;
            return true;
        }
        throw new IllegalStateException();
    }

    @Override // java.util.Iterator
    @CanIgnoreReturnValue
    public final T next() {
        if (hasNext()) {
            this.state = State.NOT_READY;
            T t = this.next;
            this.next = null;
            return t;
        }
        throw new NoSuchElementException();
    }
}
