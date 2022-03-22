package kotlinx.coroutines;

import kotlinx.coroutines.internal.MainDispatcherLoader;
/* compiled from: MainCoroutineDispatcher.kt */
/* loaded from: classes.dex */
public abstract class MainCoroutineDispatcher extends CoroutineDispatcher {
    public abstract MainCoroutineDispatcher getImmediate();

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public String toString() {
        String str;
        MainCoroutineDispatcher mainCoroutineDispatcher;
        ExecutorCoroutineDispatcher executorCoroutineDispatcher = Dispatchers.Default;
        MainCoroutineDispatcher mainCoroutineDispatcher2 = MainDispatcherLoader.dispatcher;
        if (this == mainCoroutineDispatcher2) {
            str = "Dispatchers.Main";
        } else {
            try {
                mainCoroutineDispatcher = mainCoroutineDispatcher2.getImmediate();
            } catch (UnsupportedOperationException unused) {
                mainCoroutineDispatcher = null;
            }
            if (this == mainCoroutineDispatcher) {
                str = "Dispatchers.Main.immediate";
            } else {
                str = null;
            }
        }
        if (str != null) {
            return str;
        }
        return DebugStringsKt.getClassSimpleName(this) + '@' + DebugStringsKt.getHexAddress(this);
    }
}
