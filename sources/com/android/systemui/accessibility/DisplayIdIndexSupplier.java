package com.android.systemui.accessibility;

import android.hardware.display.DisplayManager;
import android.util.SparseArray;
import android.view.Display;
/* loaded from: classes.dex */
public abstract class DisplayIdIndexSupplier<T> {
    public final DisplayManager mDisplayManager;
    public final SparseArray<T> mSparseArray = new SparseArray<>();

    public abstract T createInstance(Display display);

    public final T get(int i) {
        T t = this.mSparseArray.get(i);
        if (t != null) {
            return t;
        }
        Display display = this.mDisplayManager.getDisplay(i);
        if (display == null) {
            return null;
        }
        T createInstance = createInstance(display);
        this.mSparseArray.put(i, createInstance);
        return createInstance;
    }

    public DisplayIdIndexSupplier(DisplayManager displayManager) {
        this.mDisplayManager = displayManager;
    }
}
