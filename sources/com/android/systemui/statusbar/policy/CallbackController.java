package com.android.systemui.statusbar.policy;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import java.util.Objects;
/* loaded from: classes.dex */
public interface CallbackController<T> {
    void addCallback(T t);

    default T observe(LifecycleOwner lifecycleOwner, T t) {
        return observe(lifecycleOwner.getLifecycle(), (Lifecycle) t);
    }

    void removeCallback(T t);

    default T observe(Lifecycle lifecycle, final T t) {
        lifecycle.addObserver(new LifecycleEventObserver() { // from class: com.android.systemui.statusbar.policy.CallbackController$$ExternalSyntheticLambda0
            @Override // androidx.lifecycle.LifecycleEventObserver
            public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
                CallbackController callbackController = CallbackController.this;
                Object obj = t;
                Objects.requireNonNull(callbackController);
                if (event == Lifecycle.Event.ON_RESUME) {
                    callbackController.addCallback(obj);
                } else if (event == Lifecycle.Event.ON_PAUSE) {
                    callbackController.removeCallback(obj);
                }
            }
        });
        return t;
    }
}
