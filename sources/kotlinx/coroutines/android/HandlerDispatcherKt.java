package kotlinx.coroutines.android;

import android.os.Handler;
import android.os.Looper;
import java.util.Objects;
import kotlin.Result;
/* compiled from: HandlerDispatcher.kt */
/* loaded from: classes.dex */
public final class HandlerDispatcherKt {
    static {
        Object obj;
        try {
            obj = new HandlerContext(asHandler(Looper.getMainLooper(), true));
        } catch (Throwable th) {
            obj = new Result.Failure(th);
        }
        if (obj instanceof Result.Failure) {
            obj = null;
        }
        HandlerDispatcher handlerDispatcher = (HandlerDispatcher) obj;
    }

    public static final Handler asHandler(Looper looper, boolean z) {
        if (!z) {
            return new Handler(looper);
        }
        Object invoke = Handler.class.getDeclaredMethod("createAsync", Looper.class).invoke(null, looper);
        Objects.requireNonNull(invoke, "null cannot be cast to non-null type android.os.Handler");
        return (Handler) invoke;
    }
}
