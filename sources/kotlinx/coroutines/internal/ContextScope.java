package kotlinx.coroutines.internal;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.coroutines.CoroutineContext;
import kotlinx.coroutines.CoroutineScope;
/* compiled from: Scopes.kt */
/* loaded from: classes.dex */
public final class ContextScope implements CoroutineScope {
    public final CoroutineContext coroutineContext;

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("CoroutineScope(coroutineContext=");
        m.append(this.coroutineContext);
        m.append(')');
        return m.toString();
    }

    public ContextScope(CoroutineContext coroutineContext) {
        this.coroutineContext = coroutineContext;
    }

    @Override // kotlinx.coroutines.CoroutineScope
    public final CoroutineContext getCoroutineContext() {
        return this.coroutineContext;
    }
}
