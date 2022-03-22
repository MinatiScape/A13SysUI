package androidx.concurrent.futures;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.concurrent.futures.AbstractResolvableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.lang.ref.WeakReference;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
/* loaded from: classes.dex */
public final class CallbackToFutureAdapter {

    /* loaded from: classes.dex */
    public static final class Completer<T> {
        public boolean attemptedSetting;
        public ResolvableFuture<Void> cancellationFuture = new ResolvableFuture<>();
        public SafeFuture<T> future;
        public Object tag;

        /* JADX WARN: Code restructure failed: missing block: B:11:0x0020, code lost:
            if (r6 != false) goto L_0x0024;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean set(T r6) {
            /*
                r5 = this;
                r0 = 1
                r5.attemptedSetting = r0
                androidx.concurrent.futures.CallbackToFutureAdapter$SafeFuture<T> r1 = r5.future
                r2 = 0
                r3 = 0
                if (r1 == 0) goto L_0x0023
                androidx.concurrent.futures.CallbackToFutureAdapter$SafeFuture$1 r1 = r1.delegate
                java.util.Objects.requireNonNull(r1)
                if (r6 != 0) goto L_0x0012
                java.lang.Object r6 = androidx.concurrent.futures.AbstractResolvableFuture.NULL
            L_0x0012:
                androidx.concurrent.futures.AbstractResolvableFuture$AtomicHelper r4 = androidx.concurrent.futures.AbstractResolvableFuture.ATOMIC_HELPER
                boolean r6 = r4.casValue(r1, r2, r6)
                if (r6 == 0) goto L_0x001f
                androidx.concurrent.futures.AbstractResolvableFuture.complete(r1)
                r6 = r0
                goto L_0x0020
            L_0x001f:
                r6 = r3
            L_0x0020:
                if (r6 == 0) goto L_0x0023
                goto L_0x0024
            L_0x0023:
                r0 = r3
            L_0x0024:
                if (r0 == 0) goto L_0x002c
                r5.tag = r2
                r5.future = r2
                r5.cancellationFuture = r2
            L_0x002c:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.concurrent.futures.CallbackToFutureAdapter.Completer.set(java.lang.Object):boolean");
        }

        /* JADX WARN: Code restructure failed: missing block: B:8:0x0021, code lost:
            if (r6 != false) goto L_0x0025;
         */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final boolean setException(java.lang.Exception r6) {
            /*
                r5 = this;
                r0 = 1
                r5.attemptedSetting = r0
                androidx.concurrent.futures.CallbackToFutureAdapter$SafeFuture<T> r1 = r5.future
                r2 = 0
                r3 = 0
                if (r1 == 0) goto L_0x0024
                androidx.concurrent.futures.CallbackToFutureAdapter$SafeFuture$1 r1 = r1.delegate
                java.util.Objects.requireNonNull(r1)
                androidx.concurrent.futures.AbstractResolvableFuture$Failure r4 = new androidx.concurrent.futures.AbstractResolvableFuture$Failure
                r4.<init>(r6)
                androidx.concurrent.futures.AbstractResolvableFuture$AtomicHelper r6 = androidx.concurrent.futures.AbstractResolvableFuture.ATOMIC_HELPER
                boolean r6 = r6.casValue(r1, r2, r4)
                if (r6 == 0) goto L_0x0020
                androidx.concurrent.futures.AbstractResolvableFuture.complete(r1)
                r6 = r0
                goto L_0x0021
            L_0x0020:
                r6 = r3
            L_0x0021:
                if (r6 == 0) goto L_0x0024
                goto L_0x0025
            L_0x0024:
                r0 = r3
            L_0x0025:
                if (r0 == 0) goto L_0x002d
                r5.tag = r2
                r5.future = r2
                r5.cancellationFuture = r2
            L_0x002d:
                return r0
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.concurrent.futures.CallbackToFutureAdapter.Completer.setException(java.lang.Exception):boolean");
        }

        public final void finalize() {
            ResolvableFuture<Void> resolvableFuture;
            SafeFuture<T> safeFuture = this.future;
            if (safeFuture != null && !safeFuture.isDone()) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("The completer object was garbage collected - this future would otherwise never complete. The tag was: ");
                m.append(this.tag);
                FutureGarbageCollectedException futureGarbageCollectedException = new FutureGarbageCollectedException(m.toString());
                SafeFuture.AnonymousClass1 r0 = safeFuture.delegate;
                Objects.requireNonNull(r0);
                if (AbstractResolvableFuture.ATOMIC_HELPER.casValue(r0, null, new AbstractResolvableFuture.Failure(futureGarbageCollectedException))) {
                    AbstractResolvableFuture.complete(r0);
                }
            }
            if (!this.attemptedSetting && (resolvableFuture = this.cancellationFuture) != null) {
                resolvableFuture.set(null);
            }
        }
    }

    /* loaded from: classes.dex */
    public interface Resolver<T> {
        Object attachCompleter(Completer<T> completer) throws Exception;
    }

    /* loaded from: classes.dex */
    public static final class SafeFuture<T> implements ListenableFuture<T> {
        public final WeakReference<Completer<T>> completerWeakReference;
        public final AnonymousClass1 delegate = new AbstractResolvableFuture<T>() { // from class: androidx.concurrent.futures.CallbackToFutureAdapter.SafeFuture.1
            @Override // androidx.concurrent.futures.AbstractResolvableFuture
            public final String pendingToString() {
                Completer<T> completer = SafeFuture.this.completerWeakReference.get();
                if (completer == null) {
                    return "Completer object has been garbage collected, future will fail soon";
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("tag=[");
                m.append(completer.tag);
                m.append("]");
                return m.toString();
            }
        };

        @Override // java.util.concurrent.Future
        public final T get() throws InterruptedException, ExecutionException {
            return get();
        }

        @Override // com.google.common.util.concurrent.ListenableFuture
        public final void addListener(Runnable runnable, Executor executor) {
            addListener(runnable, executor);
        }

        @Override // java.util.concurrent.Future
        public final boolean cancel(boolean z) {
            Completer<T> completer = this.completerWeakReference.get();
            boolean cancel = cancel(z);
            if (cancel && completer != null) {
                completer.tag = null;
                completer.future = null;
                completer.cancellationFuture.set(null);
            }
            return cancel;
        }

        @Override // java.util.concurrent.Future
        public final T get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            return get(j, timeUnit);
        }

        @Override // java.util.concurrent.Future
        public final boolean isCancelled() {
            AnonymousClass1 r0 = this.delegate;
            Objects.requireNonNull(r0);
            return r0.value instanceof AbstractResolvableFuture.Cancellation;
        }

        @Override // java.util.concurrent.Future
        public final boolean isDone() {
            return isDone();
        }

        public final String toString() {
            return toString();
        }

        /* JADX WARN: Type inference failed for: r0v0, types: [androidx.concurrent.futures.CallbackToFutureAdapter$SafeFuture$1] */
        public SafeFuture(Completer<T> completer) {
            this.completerWeakReference = new WeakReference<>(completer);
        }
    }

    public static SafeFuture getFuture(Resolver resolver) {
        Completer completer = new Completer();
        SafeFuture<T> safeFuture = new SafeFuture<>(completer);
        completer.future = safeFuture;
        completer.tag = resolver.getClass();
        try {
            Object attachCompleter = resolver.attachCompleter(completer);
            if (attachCompleter != null) {
                completer.tag = attachCompleter;
            }
        } catch (Exception e) {
            SafeFuture.AnonymousClass1 r0 = safeFuture.delegate;
            Objects.requireNonNull(r0);
            if (AbstractResolvableFuture.ATOMIC_HELPER.casValue(r0, null, new AbstractResolvableFuture.Failure(e))) {
                AbstractResolvableFuture.complete(r0);
            }
        }
        return safeFuture;
    }

    /* loaded from: classes.dex */
    public static final class FutureGarbageCollectedException extends Throwable {
        @Override // java.lang.Throwable
        public final synchronized Throwable fillInStackTrace() {
            return this;
        }

        public FutureGarbageCollectedException(String str) {
            super(str);
        }
    }
}
