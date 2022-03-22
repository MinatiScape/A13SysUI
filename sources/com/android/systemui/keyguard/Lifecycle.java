package com.android.systemui.keyguard;

import java.util.ArrayList;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class Lifecycle<T> {
    public ArrayList<T> mObservers = new ArrayList<>();

    public final void dispatch(Consumer<T> consumer) {
        for (int i = 0; i < this.mObservers.size(); i++) {
            consumer.accept(this.mObservers.get(i));
        }
    }

    public final void addObserver(T t) {
        ArrayList<T> arrayList = this.mObservers;
        Objects.requireNonNull(t);
        arrayList.add(t);
    }
}
