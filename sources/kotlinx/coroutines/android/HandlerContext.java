package kotlinx.coroutines.android;

import android.os.Handler;
import android.os.Looper;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.ExecutorCoroutineDispatcher;
import kotlinx.coroutines.Job;
import kotlinx.coroutines.MainCoroutineDispatcher;
import kotlinx.coroutines.internal.MainDispatcherLoader;
import kotlinx.coroutines.scheduling.LimitingDispatcher;
/* compiled from: HandlerDispatcher.kt */
/* loaded from: classes.dex */
public final class HandlerContext extends HandlerDispatcher {
    public volatile HandlerContext _immediate;
    public final Handler handler;
    public final HandlerContext immediate;
    public final boolean invokeImmediately;
    public final String name;

    public HandlerContext(Handler handler, String str, boolean z) {
        super(0);
        this.handler = handler;
        this.name = str;
        this.invokeImmediately = z;
        this._immediate = z ? this : null;
        HandlerContext handlerContext = this._immediate;
        if (handlerContext == null) {
            handlerContext = new HandlerContext(handler, str, true);
            this._immediate = handlerContext;
        }
        this.immediate = handlerContext;
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final void dispatch(CoroutineContext coroutineContext, Runnable runnable) {
        if (!this.handler.post(runnable)) {
            CancellationException cancellationException = new CancellationException("The task was rejected, the handler underlying the dispatcher '" + this + "' was closed");
            Job job = (Job) coroutineContext.get(Job.Key.$$INSTANCE);
            if (job != null) {
                job.cancel(cancellationException);
            }
            LimitingDispatcher limitingDispatcher = Dispatchers.IO;
            Objects.requireNonNull(limitingDispatcher);
            limitingDispatcher.dispatch(runnable, false);
        }
    }

    public final boolean equals(Object obj) {
        if (!(obj instanceof HandlerContext) || ((HandlerContext) obj).handler != this.handler) {
            return false;
        }
        return true;
    }

    public final int hashCode() {
        return System.identityHashCode(this.handler);
    }

    @Override // kotlinx.coroutines.CoroutineDispatcher
    public final boolean isDispatchNeeded() {
        if (!this.invokeImmediately || !Intrinsics.areEqual(Looper.myLooper(), this.handler.getLooper())) {
            return true;
        }
        return false;
    }

    @Override // kotlinx.coroutines.MainCoroutineDispatcher, kotlinx.coroutines.CoroutineDispatcher
    public final String toString() {
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
        String str2 = this.name;
        if (str2 == null) {
            str2 = this.handler.toString();
        }
        if (this.invokeImmediately) {
            return Intrinsics.stringPlus(str2, ".immediate");
        }
        return str2;
    }

    public HandlerContext(Handler handler) {
        this(handler, null, false);
    }

    @Override // kotlinx.coroutines.MainCoroutineDispatcher
    public final MainCoroutineDispatcher getImmediate() {
        return this.immediate;
    }
}
