package androidx.lifecycle;

import androidx.arch.core.internal.SafeIterableMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
/* loaded from: classes.dex */
public final class MediatorLiveData<T> extends MutableLiveData<T> {
    public SafeIterableMap<LiveData<?>, Source<?>> mSources = new SafeIterableMap<>();

    /* loaded from: classes.dex */
    public static class Source<V> implements Observer<V> {
        public final LiveData<V> mLiveData;
        public final Observer<? super V> mObserver;
        public int mVersion = -1;

        @Override // androidx.lifecycle.Observer
        public final void onChanged(V v) {
            int i = this.mVersion;
            LiveData<V> liveData = this.mLiveData;
            Objects.requireNonNull(liveData);
            if (i != liveData.mVersion) {
                LiveData<V> liveData2 = this.mLiveData;
                Objects.requireNonNull(liveData2);
                this.mVersion = liveData2.mVersion;
                this.mObserver.onChanged(v);
            }
        }

        public Source(LiveData liveData, Transformations$1 transformations$1) {
            this.mLiveData = liveData;
            this.mObserver = transformations$1;
        }
    }

    @Override // androidx.lifecycle.LiveData
    public final void onActive() {
        Iterator<Map.Entry<LiveData<?>, Source<?>>> it = this.mSources.iterator();
        while (true) {
            SafeIterableMap.ListIterator listIterator = (SafeIterableMap.ListIterator) it;
            if (listIterator.hasNext()) {
                Source source = (Source) ((Map.Entry) listIterator.next()).getValue();
                Objects.requireNonNull(source);
                source.mLiveData.observeForever(source);
            } else {
                return;
            }
        }
    }

    @Override // androidx.lifecycle.LiveData
    public final void onInactive() {
        Iterator<Map.Entry<LiveData<?>, Source<?>>> it = this.mSources.iterator();
        while (true) {
            SafeIterableMap.ListIterator listIterator = (SafeIterableMap.ListIterator) it;
            if (listIterator.hasNext()) {
                Source source = (Source) ((Map.Entry) listIterator.next()).getValue();
                Objects.requireNonNull(source);
                source.mLiveData.removeObserver(source);
            } else {
                return;
            }
        }
    }
}
