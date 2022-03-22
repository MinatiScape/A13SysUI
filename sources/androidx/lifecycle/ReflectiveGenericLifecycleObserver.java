package androidx.lifecycle;

import androidx.lifecycle.ClassesInfoCache;
import androidx.lifecycle.Lifecycle;
import java.util.List;
import java.util.Objects;
/* JADX INFO: Access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class ReflectiveGenericLifecycleObserver implements LifecycleEventObserver {
    public final ClassesInfoCache.CallbackInfo mInfo;
    public final Object mWrapped;

    @Override // androidx.lifecycle.LifecycleEventObserver
    public final void onStateChanged(LifecycleOwner lifecycleOwner, Lifecycle.Event event) {
        ClassesInfoCache.CallbackInfo callbackInfo = this.mInfo;
        Object obj = this.mWrapped;
        Objects.requireNonNull(callbackInfo);
        ClassesInfoCache.CallbackInfo.invokeMethodsForEvent((List) callbackInfo.mEventToHandlers.get(event), lifecycleOwner, event, obj);
        ClassesInfoCache.CallbackInfo.invokeMethodsForEvent((List) callbackInfo.mEventToHandlers.get(Lifecycle.Event.ON_ANY), lifecycleOwner, event, obj);
    }

    public ReflectiveGenericLifecycleObserver(Object obj) {
        this.mWrapped = obj;
        this.mInfo = ClassesInfoCache.sInstance.getInfo(obj.getClass());
    }
}
