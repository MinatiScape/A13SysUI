package com.android.systemui.classifier;

import android.view.MotionEvent;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
/* loaded from: classes.dex */
public final class TimeLimitedMotionEventBuffer implements List<MotionEvent> {
    public final LinkedList<MotionEvent> mMotionEvents = new LinkedList<>();

    /* loaded from: classes.dex */
    public class Iter implements ListIterator<MotionEvent> {
        public final ListIterator<MotionEvent> mIterator;

        @Override // java.util.ListIterator
        public final void add(MotionEvent motionEvent) {
            throw new UnsupportedOperationException();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public final boolean hasNext() {
            return this.mIterator.hasNext();
        }

        @Override // java.util.ListIterator
        public final boolean hasPrevious() {
            return this.mIterator.hasPrevious();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public final Object next() {
            return this.mIterator.next();
        }

        @Override // java.util.ListIterator
        public final int nextIndex() {
            return this.mIterator.nextIndex();
        }

        @Override // java.util.ListIterator
        public final MotionEvent previous() {
            return this.mIterator.previous();
        }

        @Override // java.util.ListIterator
        public final int previousIndex() {
            return this.mIterator.previousIndex();
        }

        @Override // java.util.ListIterator, java.util.Iterator
        public final void remove() {
            this.mIterator.remove();
        }

        @Override // java.util.ListIterator
        public final void set(MotionEvent motionEvent) {
            throw new UnsupportedOperationException();
        }

        public Iter(TimeLimitedMotionEventBuffer timeLimitedMotionEventBuffer, int i) {
            this.mIterator = timeLimitedMotionEventBuffer.mMotionEvents.listIterator(i);
        }
    }

    @Override // java.util.List
    public final void add(int i, MotionEvent motionEvent) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public final boolean addAll(Collection<? extends MotionEvent> collection) {
        boolean addAll = this.mMotionEvents.addAll(collection);
        ejectOldEvents();
        return addAll;
    }

    @Override // java.util.List
    public final ListIterator<MotionEvent> listIterator() {
        return new Iter(this, 0);
    }

    @Override // java.util.List
    public final MotionEvent remove(int i) {
        return this.mMotionEvents.remove(i);
    }

    @Override // java.util.List, java.util.Collection
    public final Object[] toArray() {
        return this.mMotionEvents.toArray();
    }

    @Override // java.util.List, java.util.Collection
    public final void clear() {
        this.mMotionEvents.clear();
    }

    @Override // java.util.List, java.util.Collection
    public final boolean contains(Object obj) {
        return this.mMotionEvents.contains(obj);
    }

    @Override // java.util.List, java.util.Collection
    public final boolean containsAll(Collection<?> collection) {
        return this.mMotionEvents.containsAll(collection);
    }

    public final void ejectOldEvents() {
        if (!this.mMotionEvents.isEmpty()) {
            ListIterator<MotionEvent> listIterator = listIterator();
            long eventTime = this.mMotionEvents.getLast().getEventTime();
            while (true) {
                Iter iter = (Iter) listIterator;
                if (iter.hasNext()) {
                    MotionEvent motionEvent = (MotionEvent) iter.next();
                    if (eventTime - motionEvent.getEventTime() > 1000) {
                        iter.remove();
                        motionEvent.recycle();
                    }
                } else {
                    return;
                }
            }
        }
    }

    @Override // java.util.List, java.util.Collection
    public final boolean equals(Object obj) {
        return this.mMotionEvents.equals(obj);
    }

    @Override // java.util.List
    public final MotionEvent get(int i) {
        return this.mMotionEvents.get(i);
    }

    @Override // java.util.List, java.util.Collection
    public final int hashCode() {
        return this.mMotionEvents.hashCode();
    }

    @Override // java.util.List
    public final int indexOf(Object obj) {
        return this.mMotionEvents.indexOf(obj);
    }

    @Override // java.util.List, java.util.Collection
    public final boolean isEmpty() {
        return this.mMotionEvents.isEmpty();
    }

    @Override // java.util.List, java.util.Collection, java.lang.Iterable
    public final Iterator<MotionEvent> iterator() {
        return this.mMotionEvents.iterator();
    }

    @Override // java.util.List
    public final int lastIndexOf(Object obj) {
        return this.mMotionEvents.lastIndexOf(obj);
    }

    @Override // java.util.List
    public final ListIterator<MotionEvent> listIterator(int i) {
        return new Iter(this, i);
    }

    @Override // java.util.List, java.util.Collection
    public final boolean remove(Object obj) {
        return this.mMotionEvents.remove(obj);
    }

    @Override // java.util.List, java.util.Collection
    public final boolean removeAll(Collection<?> collection) {
        return this.mMotionEvents.removeAll(collection);
    }

    @Override // java.util.List, java.util.Collection
    public final boolean retainAll(Collection<?> collection) {
        return this.mMotionEvents.retainAll(collection);
    }

    @Override // java.util.List
    public final MotionEvent set(int i, MotionEvent motionEvent) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public final int size() {
        return this.mMotionEvents.size();
    }

    @Override // java.util.List
    public final List<MotionEvent> subList(int i, int i2) {
        throw new UnsupportedOperationException();
    }

    @Override // java.util.List, java.util.Collection
    public final <T> T[] toArray(T[] tArr) {
        return (T[]) this.mMotionEvents.toArray(tArr);
    }

    @Override // java.util.List, java.util.Collection
    public final boolean add(Object obj) {
        boolean add = this.mMotionEvents.add((MotionEvent) obj);
        ejectOldEvents();
        return add;
    }

    @Override // java.util.List
    public final boolean addAll(int i, Collection<? extends MotionEvent> collection) {
        throw new UnsupportedOperationException();
    }
}
