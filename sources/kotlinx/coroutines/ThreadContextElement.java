package kotlinx.coroutines;

import kotlin.coroutines.CoroutineContext;
/* compiled from: ThreadContextElement.kt */
/* loaded from: classes.dex */
public interface ThreadContextElement<S> extends CoroutineContext.Element {
    void restoreThreadContext(Object obj);

    String updateThreadContext(CoroutineContext coroutineContext);
}
