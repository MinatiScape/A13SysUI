package kotlinx.coroutines.internal;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.MainCoroutineDispatcher;
/* compiled from: MainDispatchers.kt */
/* loaded from: classes.dex */
public final class MissingMainCoroutineDispatcher extends MainCoroutineDispatcher {
    public final Throwable cause;
    public final String errorHint;

    @Override // kotlinx.coroutines.MainCoroutineDispatcher
    public final MainCoroutineDispatcher getImmediate() {
        return this;
    }

    public final Void missing() {
        String str;
        if (this.cause != null) {
            String str2 = this.errorHint;
            if (str2 == null || (str = Intrinsics.stringPlus(". ", str2)) == null) {
                str = "";
            }
            throw new IllegalStateException(Intrinsics.stringPlus("Module with the Main dispatcher had failed to initialize", str), this.cause);
        }
        throw new IllegalStateException("Module with the Main dispatcher is missing. Add dependency providing the Main dispatcher, e.g. 'kotlinx-coroutines-android' and ensure it has the same version as 'kotlinx-coroutines-core'");
    }

    @Override // kotlinx.coroutines.MainCoroutineDispatcher, kotlinx.coroutines.CoroutineDispatcher
    public final String toString() {
        String str;
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Dispatchers.Main[missing");
        Throwable th = this.cause;
        if (th != null) {
            str = Intrinsics.stringPlus(", cause=", th);
        } else {
            str = "";
        }
        m.append(str);
        m.append(']');
        return m.toString();
    }

    public MissingMainCoroutineDispatcher(Throwable th, String str) {
        this.cause = th;
        this.errorHint = str;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        missing();
        throw null;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final boolean isDispatchNeeded() {
        missing();
        throw null;
    }
}
