package kotlinx.coroutines;

import kotlinx.atomicfu.AtomicBoolean;
/* compiled from: CompletionState.kt */
/* loaded from: classes.dex */
public class CompletedExceptionally {
    public final AtomicBoolean _handled;
    public final Throwable cause;

    public final String toString() {
        return DebugStringsKt.getClassSimpleName(this) + '[' + this.cause + ']';
    }

    public CompletedExceptionally(Throwable th, boolean z) {
        this.cause = th;
        this._handled = new AtomicBoolean(z);
    }
}
