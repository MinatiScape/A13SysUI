package kotlinx.coroutines;

import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
/* compiled from: Unconfined.kt */
/* loaded from: classes.dex */
public final class YieldContext extends AbstractCoroutineContextElement {
    public static final Key Key = new Key();
    public boolean dispatcherWasUnconfined;

    /* compiled from: Unconfined.kt */
    /* loaded from: classes.dex */
    public static final class Key implements CoroutineContext.Key<YieldContext> {
    }

    public YieldContext() {
        super(Key);
    }
}
