package com.google.common.util.concurrent;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.google.common.base.Throwables;
import com.google.common.util.concurrent.internal.InternalFutureFailureAccess;
import com.google.errorprone.annotations.CanIgnoreReturnValue;
import com.google.errorprone.annotations.ForOverride;
import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Objects;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import java.util.concurrent.locks.LockSupport;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Unsafe;
/* loaded from: classes.dex */
public abstract class AbstractFuture<V> extends InternalFutureFailureAccess implements ListenableFuture<V> {
    public static final AtomicHelper ATOMIC_HELPER;
    public static final boolean GENERATE_CANCELLATION_CAUSES;
    public static final Object NULL;
    public static final Logger log;
    public volatile Listener listeners;
    public volatile Object value;
    public volatile Waiter waiters;

    /* loaded from: classes.dex */
    public static abstract class AtomicHelper {
        public abstract boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2);

        public abstract boolean casValue(AbstractFuture<?> abstractFuture, Object obj, Object obj2);

        public abstract boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2);

        public abstract void putNext(Waiter waiter, Waiter waiter2);

        public abstract void putThread(Waiter waiter, Thread thread);
    }

    /* loaded from: classes.dex */
    public static final class Cancellation {
        public static final Cancellation CAUSELESS_CANCELLED;
        public static final Cancellation CAUSELESS_INTERRUPTED;
        public final Throwable cause;
        public final boolean wasInterrupted;

        static {
            if (AbstractFuture.GENERATE_CANCELLATION_CAUSES) {
                CAUSELESS_CANCELLED = null;
                CAUSELESS_INTERRUPTED = null;
                return;
            }
            CAUSELESS_CANCELLED = new Cancellation(false, null);
            CAUSELESS_INTERRUPTED = new Cancellation(true, null);
        }

        public Cancellation(boolean z, Throwable th) {
            this.wasInterrupted = z;
            this.cause = th;
        }
    }

    /* loaded from: classes.dex */
    public static final class SafeAtomicHelper extends AtomicHelper {
        public final AtomicReferenceFieldUpdater<AbstractFuture, Listener> listenersUpdater;
        public final AtomicReferenceFieldUpdater<AbstractFuture, Object> valueUpdater;
        public final AtomicReferenceFieldUpdater<Waiter, Waiter> waiterNextUpdater;
        public final AtomicReferenceFieldUpdater<Waiter, Thread> waiterThreadUpdater;
        public final AtomicReferenceFieldUpdater<AbstractFuture, Waiter> waitersUpdater;

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2) {
            return this.listenersUpdater.compareAndSet(abstractFuture, listener, listener2);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final boolean casValue(AbstractFuture<?> abstractFuture, Object obj, Object obj2) {
            return this.valueUpdater.compareAndSet(abstractFuture, obj, obj2);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2) {
            return this.waitersUpdater.compareAndSet(abstractFuture, waiter, waiter2);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final void putNext(Waiter waiter, Waiter waiter2) {
            this.waiterNextUpdater.lazySet(waiter, waiter2);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final void putThread(Waiter waiter, Thread thread) {
            this.waiterThreadUpdater.lazySet(waiter, thread);
        }

        public SafeAtomicHelper(AtomicReferenceFieldUpdater<Waiter, Thread> atomicReferenceFieldUpdater, AtomicReferenceFieldUpdater<Waiter, Waiter> atomicReferenceFieldUpdater2, AtomicReferenceFieldUpdater<AbstractFuture, Waiter> atomicReferenceFieldUpdater3, AtomicReferenceFieldUpdater<AbstractFuture, Listener> atomicReferenceFieldUpdater4, AtomicReferenceFieldUpdater<AbstractFuture, Object> atomicReferenceFieldUpdater5) {
            this.waiterThreadUpdater = atomicReferenceFieldUpdater;
            this.waiterNextUpdater = atomicReferenceFieldUpdater2;
            this.waitersUpdater = atomicReferenceFieldUpdater3;
            this.listenersUpdater = atomicReferenceFieldUpdater4;
            this.valueUpdater = atomicReferenceFieldUpdater5;
        }
    }

    /* loaded from: classes.dex */
    public static final class SetFuture<V> implements Runnable {
        public final ListenableFuture<? extends V> future;
        public final AbstractFuture<V> owner;

        @Override // java.lang.Runnable
        public final void run() {
            if (this.owner.value == this) {
                if (AbstractFuture.ATOMIC_HELPER.casValue(this.owner, this, AbstractFuture.getFutureValue(this.future))) {
                    AbstractFuture.complete(this.owner);
                }
            }
        }

        public SetFuture(AbstractFuture<V> abstractFuture, ListenableFuture<? extends V> listenableFuture) {
            this.owner = abstractFuture;
            this.future = listenableFuture;
        }
    }

    /* loaded from: classes.dex */
    public interface Trusted<V> extends ListenableFuture<V> {
    }

    /* loaded from: classes.dex */
    public static abstract class TrustedFuture<V> extends AbstractFuture<V> implements Trusted<V> {
        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @CanIgnoreReturnValue
        public final V get() throws InterruptedException, ExecutionException {
            return (V) AbstractFuture.super.get();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @CanIgnoreReturnValue
        public final V get(long j, TimeUnit timeUnit) throws InterruptedException, ExecutionException, TimeoutException {
            return (V) AbstractFuture.super.get(j, timeUnit);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isCancelled() {
            return this.value instanceof Cancellation;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        @CanIgnoreReturnValue
        public final boolean cancel(boolean z) {
            return AbstractFuture.super.cancel(z);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, java.util.concurrent.Future
        public final boolean isDone() {
            return AbstractFuture.super.isDone();
        }

        @Override // com.google.common.util.concurrent.AbstractFuture, com.google.common.util.concurrent.ListenableFuture
        public final void addListener(Runnable runnable, Executor executor) {
            AbstractFuture.super.addListener(runnable, executor);
        }
    }

    /* loaded from: classes.dex */
    public static final class UnsafeAtomicHelper extends AtomicHelper {
        public static final long LISTENERS_OFFSET;
        public static final Unsafe UNSAFE;
        public static final long VALUE_OFFSET;
        public static final long WAITERS_OFFSET;
        public static final long WAITER_NEXT_OFFSET;
        public static final long WAITER_THREAD_OFFSET;

        static {
            Unsafe unsafe;
            try {
                try {
                    unsafe = Unsafe.getUnsafe();
                } catch (PrivilegedActionException e) {
                    throw new RuntimeException("Could not initialize intrinsics", e.getCause());
                }
            } catch (SecurityException unused) {
                unsafe = (Unsafe) AccessController.doPrivileged(new PrivilegedExceptionAction<Unsafe>() { // from class: com.google.common.util.concurrent.AbstractFuture.UnsafeAtomicHelper.1
                    @Override // java.security.PrivilegedExceptionAction
                    public final Unsafe run() throws Exception {
                        Field[] declaredFields;
                        for (Field field : Unsafe.class.getDeclaredFields()) {
                            field.setAccessible(true);
                            Object obj = field.get(null);
                            if (Unsafe.class.isInstance(obj)) {
                                return (Unsafe) Unsafe.class.cast(obj);
                            }
                        }
                        throw new NoSuchFieldError("the Unsafe");
                    }
                });
            }
            try {
                WAITERS_OFFSET = unsafe.objectFieldOffset(AbstractFuture.class.getDeclaredField("waiters"));
                LISTENERS_OFFSET = unsafe.objectFieldOffset(AbstractFuture.class.getDeclaredField("listeners"));
                VALUE_OFFSET = unsafe.objectFieldOffset(AbstractFuture.class.getDeclaredField("value"));
                WAITER_THREAD_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("thread"));
                WAITER_NEXT_OFFSET = unsafe.objectFieldOffset(Waiter.class.getDeclaredField("next"));
                UNSAFE = unsafe;
            } catch (Exception e2) {
                String str = Throwables.SHARED_SECRETS_CLASSNAME;
                if (e2 instanceof RuntimeException) {
                    throw ((RuntimeException) e2);
                } else if (!(e2 instanceof Error)) {
                    throw new RuntimeException(e2);
                } else {
                    throw ((Error) e2);
                }
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2) {
            return UNSAFE.compareAndSwapObject(abstractFuture, LISTENERS_OFFSET, listener, listener2);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final boolean casValue(AbstractFuture<?> abstractFuture, Object obj, Object obj2) {
            return UNSAFE.compareAndSwapObject(abstractFuture, VALUE_OFFSET, obj, obj2);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2) {
            return UNSAFE.compareAndSwapObject(abstractFuture, WAITERS_OFFSET, waiter, waiter2);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final void putNext(Waiter waiter, Waiter waiter2) {
            UNSAFE.putObject(waiter, WAITER_NEXT_OFFSET, waiter2);
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final void putThread(Waiter waiter, Thread thread) {
            UNSAFE.putObject(waiter, WAITER_THREAD_OFFSET, thread);
        }
    }

    /* loaded from: classes.dex */
    public static final class Waiter {
        public static final Waiter TOMBSTONE = new Waiter(0);
        public volatile Waiter next;
        public volatile Thread thread;

        public Waiter(int i) {
        }

        public Waiter() {
            AbstractFuture.ATOMIC_HELPER.putThread(this, Thread.currentThread());
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r1v7, types: [com.google.common.util.concurrent.AbstractFuture<V>, com.google.common.util.concurrent.AbstractFuture] */
    /* JADX WARN: Type inference failed for: r4v2, types: [com.google.common.util.concurrent.AbstractFuture$AtomicHelper] */
    public static void complete(AbstractFuture<?> abstractFuture) {
        Waiter waiter;
        Listener listener;
        Listener listener2;
        AbstractFuture<V> abstractFuture2;
        Listener listener3 = null;
        while (true) {
            Objects.requireNonNull(abstractFuture);
            do {
                waiter = abstractFuture.waiters;
            } while (!ATOMIC_HELPER.casWaiters(abstractFuture, waiter, Waiter.TOMBSTONE));
            while (waiter != null) {
                Thread thread = waiter.thread;
                if (thread != null) {
                    waiter.thread = null;
                    LockSupport.unpark(thread);
                }
                waiter = waiter.next;
            }
            abstractFuture.afterDone();
            do {
                listener = abstractFuture.listeners;
            } while (!ATOMIC_HELPER.casListeners(abstractFuture, listener, Listener.TOMBSTONE));
            while (listener != null) {
                Listener listener4 = listener.next;
                listener.next = listener3;
                listener3 = listener;
                listener = listener4;
            }
            while (listener3 != null) {
                listener2 = listener3.next;
                Runnable runnable = listener3.task;
                Objects.requireNonNull(runnable);
                Runnable runnable2 = runnable;
                if (runnable2 instanceof SetFuture) {
                    SetFuture setFuture = (SetFuture) runnable2;
                    abstractFuture2 = setFuture.owner;
                    if (abstractFuture2.value == setFuture) {
                        if (ATOMIC_HELPER.casValue(abstractFuture2, setFuture, getFutureValue(setFuture.future))) {
                            break;
                        }
                    } else {
                        continue;
                    }
                } else {
                    Executor executor = listener3.executor;
                    Objects.requireNonNull(executor);
                    executeListener(runnable2, executor);
                }
                listener3 = listener2;
            }
            return;
            listener3 = listener2;
            abstractFuture = abstractFuture2;
        }
    }

    @ForOverride
    public void afterDone() {
    }

    /* JADX WARN: Code restructure failed: missing block: B:23:0x004c, code lost:
        java.util.concurrent.locks.LockSupport.parkNanos(r17, java.lang.Math.min(r4, 2147483647999999999L));
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x005c, code lost:
        if (java.lang.Thread.interrupted() != false) goto L_0x0086;
     */
    /* JADX WARN: Code restructure failed: missing block: B:25:0x005e, code lost:
        r4 = r17.value;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x0060, code lost:
        if (r4 == null) goto L_0x0064;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0062, code lost:
        r5 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0064, code lost:
        r5 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0069, code lost:
        if ((r5 & (!(r4 instanceof com.google.common.util.concurrent.AbstractFuture.SetFuture))) == false) goto L_0x0070;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x006f, code lost:
        return (V) getDoneValue(r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0070, code lost:
        r4 = r10 - java.lang.System.nanoTime();
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0078, code lost:
        if (r4 >= 1000) goto L_0x004c;
     */
    /* JADX WARN: Code restructure failed: missing block: B:35:0x007a, code lost:
        removeWaiter(r8);
        r8 = true;
        r13 = 1000;
        r6 = r4;
        r11 = r10;
        r4 = r20;
        r5 = r4;
        r2 = r18;
        r1 = r17;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x0086, code lost:
        removeWaiter(r8);
     */
    /* JADX WARN: Code restructure failed: missing block: B:37:0x008e, code lost:
        throw new java.lang.InterruptedException();
     */
    /* JADX WARN: Removed duplicated region for block: B:45:0x00ac  */
    /* JADX WARN: Removed duplicated region for block: B:59:0x00d3  */
    /* JADX WARN: Unsupported multi-entry loop pattern (BACK_EDGE: B:55:0x00c4 -> B:56:0x00ca). Please submit an issue!!! */
    @Override // java.util.concurrent.Future
    @com.google.errorprone.annotations.CanIgnoreReturnValue
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public V get(long r18, java.util.concurrent.TimeUnit r20) throws java.lang.InterruptedException, java.util.concurrent.TimeoutException, java.util.concurrent.ExecutionException {
        /*
            Method dump skipped, instructions count: 395
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractFuture.get(long, java.util.concurrent.TimeUnit):java.lang.Object");
    }

    public final void removeWaiter(Waiter waiter) {
        waiter.thread = null;
        while (true) {
            Waiter waiter2 = this.waiters;
            if (waiter2 != Waiter.TOMBSTONE) {
                Waiter waiter3 = null;
                while (waiter2 != null) {
                    Waiter waiter4 = waiter2.next;
                    if (waiter2.thread != null) {
                        waiter3 = waiter2;
                    } else if (waiter3 != null) {
                        waiter3.next = waiter4;
                        if (waiter3.thread == null) {
                            break;
                        }
                    } else if (!ATOMIC_HELPER.casWaiters(this, waiter2, waiter4)) {
                        break;
                    }
                    waiter2 = waiter4;
                }
                return;
            }
            return;
        }
    }

    /* loaded from: classes.dex */
    public static final class Failure {
        public static final Failure FALLBACK_INSTANCE = new Failure(new Throwable("Failure occurred while trying to finish a future.") { // from class: com.google.common.util.concurrent.AbstractFuture.Failure.1
            @Override // java.lang.Throwable
            public final synchronized Throwable fillInStackTrace() {
                return this;
            }
        });
        public final Throwable exception;

        public Failure(Throwable th) {
            Objects.requireNonNull(th);
            this.exception = th;
        }
    }

    static {
        boolean z;
        Throwable th;
        AtomicHelper atomicHelper;
        try {
            z = Boolean.parseBoolean(System.getProperty("guava.concurrent.generate_cancellation_cause", "false"));
        } catch (SecurityException unused) {
            z = false;
        }
        GENERATE_CANCELLATION_CAUSES = z;
        log = Logger.getLogger(AbstractFuture.class.getName());
        Throwable th2 = null;
        try {
            atomicHelper = new UnsafeAtomicHelper();
            th = null;
        } catch (Throwable th3) {
            th = th3;
            try {
                atomicHelper = new SafeAtomicHelper(AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Thread.class, "thread"), AtomicReferenceFieldUpdater.newUpdater(Waiter.class, Waiter.class, "next"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Waiter.class, "waiters"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Listener.class, "listeners"), AtomicReferenceFieldUpdater.newUpdater(AbstractFuture.class, Object.class, "value"));
            } catch (Throwable th4) {
                th2 = th4;
                atomicHelper = new SynchronizedHelper();
            }
        }
        ATOMIC_HELPER = atomicHelper;
        if (th2 != null) {
            Logger logger = log;
            Level level = Level.SEVERE;
            logger.log(level, "UnsafeAtomicHelper is broken!", th);
            logger.log(level, "SafeAtomicHelper is broken!", th2);
        }
        NULL = new Object();
    }

    private void addDoneString(StringBuilder sb) {
        V v;
        boolean z = false;
        while (true) {
            try {
                try {
                    v = get();
                    break;
                } catch (InterruptedException unused) {
                    z = true;
                } catch (Throwable th) {
                    if (z) {
                        Thread.currentThread().interrupt();
                    }
                    throw th;
                }
            } catch (CancellationException unused2) {
                sb.append("CANCELLED");
                return;
            } catch (RuntimeException e) {
                sb.append("UNKNOWN, cause=[");
                sb.append(e.getClass());
                sb.append(" thrown from get()]");
                return;
            } catch (ExecutionException e2) {
                sb.append("FAILURE, cause=[");
                sb.append(e2.getCause());
                sb.append("]");
                return;
            }
        }
        if (z) {
            Thread.currentThread().interrupt();
        }
        sb.append("SUCCESS, result=[");
        appendResultObject(sb, v);
        sb.append("]");
    }

    private static Object getDoneValue(Object obj) throws ExecutionException {
        if (obj instanceof Cancellation) {
            Throwable th = ((Cancellation) obj).cause;
            CancellationException cancellationException = new CancellationException("Task was cancelled.");
            cancellationException.initCause(th);
            throw cancellationException;
        } else if (obj instanceof Failure) {
            throw new ExecutionException(((Failure) obj).exception);
        } else if (obj == NULL) {
            return null;
        } else {
            return obj;
        }
    }

    public static Object getFutureValue(ListenableFuture<?> listenableFuture) {
        Object obj;
        Throwable tryInternalFastPathGetFailure;
        if (listenableFuture instanceof Trusted) {
            Object obj2 = ((AbstractFuture) listenableFuture).value;
            if (obj2 instanceof Cancellation) {
                Cancellation cancellation = (Cancellation) obj2;
                if (cancellation.wasInterrupted) {
                    obj2 = cancellation.cause != null ? new Cancellation(false, cancellation.cause) : Cancellation.CAUSELESS_CANCELLED;
                }
            }
            Objects.requireNonNull(obj2);
            return obj2;
        } else if ((listenableFuture instanceof InternalFutureFailureAccess) && (tryInternalFastPathGetFailure = ((InternalFutureFailureAccess) listenableFuture).tryInternalFastPathGetFailure()) != null) {
            return new Failure(tryInternalFastPathGetFailure);
        } else {
            boolean isCancelled = listenableFuture.isCancelled();
            boolean z = true;
            if ((!GENERATE_CANCELLATION_CAUSES) && isCancelled) {
                Cancellation cancellation2 = Cancellation.CAUSELESS_CANCELLED;
                Objects.requireNonNull(cancellation2);
                return cancellation2;
            }
            boolean z2 = false;
            while (true) {
                try {
                    try {
                        obj = listenableFuture.get();
                        break;
                    } catch (InterruptedException unused) {
                        z2 = z;
                    } catch (Throwable th) {
                        if (z2) {
                            Thread.currentThread().interrupt();
                        }
                        throw th;
                    }
                } catch (CancellationException e) {
                    if (isCancelled) {
                        return new Cancellation(false, e);
                    }
                    return new Failure(new IllegalArgumentException("get() threw CancellationException, despite reporting isCancelled() == false: " + listenableFuture, e));
                } catch (ExecutionException e2) {
                    if (!isCancelled) {
                        return new Failure(e2.getCause());
                    }
                    return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + listenableFuture, e2));
                } catch (Throwable th2) {
                    return new Failure(th2);
                }
            }
            if (z2) {
                Thread.currentThread().interrupt();
            }
            if (isCancelled) {
                return new Cancellation(false, new IllegalArgumentException("get() did not throw CancellationException, despite reporting isCancelled() == true: " + listenableFuture));
            } else if (obj == null) {
                return NULL;
            } else {
                return obj;
            }
        }
    }

    @Override // com.google.common.util.concurrent.ListenableFuture
    public void addListener(Runnable runnable, Executor executor) {
        Listener listener;
        Objects.requireNonNull(executor, "Executor was null.");
        if (isDone() || (listener = this.listeners) == Listener.TOMBSTONE) {
            executeListener(runnable, executor);
        }
        Listener listener2 = new Listener(runnable, executor);
        do {
            listener2.next = listener;
            if (!ATOMIC_HELPER.casListeners(this, listener, listener2)) {
                listener = this.listeners;
            } else {
                return;
            }
        } while (listener != Listener.TOMBSTONE);
        executeListener(runnable, executor);
    }

    public final void appendResultObject(StringBuilder sb, Object obj) {
        if (obj == null) {
            sb.append("null");
        } else if (obj == this) {
            sb.append("this future");
        } else {
            sb.append(obj.getClass().getName());
            sb.append("@");
            sb.append(Integer.toHexString(System.identityHashCode(obj)));
        }
    }

    @Override // java.util.concurrent.Future
    @CanIgnoreReturnValue
    public boolean cancel(boolean z) {
        boolean z2;
        Cancellation cancellation;
        boolean z3;
        Object obj = this.value;
        if (obj == null) {
            z2 = true;
        } else {
            z2 = false;
        }
        if (!z2 && !(obj instanceof SetFuture)) {
            return false;
        }
        if (GENERATE_CANCELLATION_CAUSES) {
            cancellation = new Cancellation(z, new CancellationException("Future.cancel() was called."));
        } else {
            if (z) {
                cancellation = Cancellation.CAUSELESS_INTERRUPTED;
            } else {
                cancellation = Cancellation.CAUSELESS_CANCELLED;
            }
            Objects.requireNonNull(cancellation);
        }
        boolean z4 = false;
        while (true) {
            if (ATOMIC_HELPER.casValue(this, obj, cancellation)) {
                complete(this);
                if (!(obj instanceof SetFuture)) {
                    return true;
                }
                ListenableFuture<? extends V> listenableFuture = ((SetFuture) obj).future;
                if (listenableFuture instanceof Trusted) {
                    this = (AbstractFuture) listenableFuture;
                    obj = this.value;
                    if (obj == null) {
                        z3 = true;
                    } else {
                        z3 = false;
                    }
                    if (!z3 && !(obj instanceof SetFuture)) {
                        return true;
                    }
                    z4 = true;
                } else {
                    listenableFuture.cancel(z);
                    return true;
                }
            } else {
                obj = this.value;
                if (!(obj instanceof SetFuture)) {
                    return z4;
                }
            }
        }
    }

    @Override // java.util.concurrent.Future
    public boolean isCancelled() {
        return this.value instanceof Cancellation;
    }

    @Override // java.util.concurrent.Future
    public boolean isDone() {
        Object obj;
        boolean z;
        if (this.value != null) {
            z = true;
        } else {
            z = false;
        }
        return (!(obj instanceof SetFuture)) & z;
    }

    public String pendingToString() {
        if (!(this instanceof ScheduledFuture)) {
            return null;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("remaining delay=[");
        m.append(((ScheduledFuture) this).getDelay(TimeUnit.MILLISECONDS));
        m.append(" ms]");
        return m.toString();
    }

    @CanIgnoreReturnValue
    public boolean setException(Throwable th) {
        Objects.requireNonNull(th);
        if (!ATOMIC_HELPER.casValue(this, null, new Failure(th))) {
            return false;
        }
        complete(this);
        return true;
    }

    /* JADX WARN: Removed duplicated region for block: B:28:0x00a5  */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00b9  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final java.lang.String toString() {
        /*
            r6 = this;
            java.lang.StringBuilder r0 = new java.lang.StringBuilder
            r0.<init>()
            java.lang.Class r1 = r6.getClass()
            java.lang.String r1 = r1.getName()
            java.lang.String r2 = "com.google.common.util.concurrent."
            boolean r1 = r1.startsWith(r2)
            if (r1 == 0) goto L_0x0021
            java.lang.Class r1 = r6.getClass()
            java.lang.String r1 = r1.getSimpleName()
            r0.append(r1)
            goto L_0x002c
        L_0x0021:
            java.lang.Class r1 = r6.getClass()
            java.lang.String r1 = r1.getName()
            r0.append(r1)
        L_0x002c:
            r1 = 64
            r0.append(r1)
            int r1 = java.lang.System.identityHashCode(r6)
            java.lang.String r1 = java.lang.Integer.toHexString(r1)
            r0.append(r1)
            java.lang.String r1 = "[status="
            r0.append(r1)
            boolean r1 = r6.isCancelled()
            java.lang.String r2 = "]"
            if (r1 == 0) goto L_0x0050
            java.lang.String r6 = "CANCELLED"
            r0.append(r6)
            goto L_0x00d4
        L_0x0050:
            boolean r1 = r6.isDone()
            if (r1 == 0) goto L_0x005b
            r6.addDoneString(r0)
            goto L_0x00d4
        L_0x005b:
            int r1 = r0.length()
            java.lang.String r3 = "PENDING"
            r0.append(r3)
            java.lang.Object r3 = r6.value
            boolean r4 = r3 instanceof com.google.common.util.concurrent.AbstractFuture.SetFuture
            java.lang.String r5 = "Exception thrown from implementation: "
            if (r4 == 0) goto L_0x0091
            java.lang.String r4 = ", setFuture=["
            r0.append(r4)
            com.google.common.util.concurrent.AbstractFuture$SetFuture r3 = (com.google.common.util.concurrent.AbstractFuture.SetFuture) r3
            com.google.common.util.concurrent.ListenableFuture<? extends V> r3 = r3.future
            if (r3 != r6) goto L_0x007e
            java.lang.String r3 = "this future"
            r0.append(r3)     // Catch: RuntimeException | StackOverflowError -> 0x0082
            goto L_0x008d
        L_0x007e:
            r0.append(r3)     // Catch: RuntimeException | StackOverflowError -> 0x0082
            goto L_0x008d
        L_0x0082:
            r3 = move-exception
            r0.append(r5)
            java.lang.Class r3 = r3.getClass()
            r0.append(r3)
        L_0x008d:
            r0.append(r2)
            goto L_0x00c4
        L_0x0091:
            java.lang.String r3 = r6.pendingToString()     // Catch: RuntimeException | StackOverflowError -> 0x00a7
            int r4 = com.google.common.base.Platform.$r8$clinit     // Catch: RuntimeException | StackOverflowError -> 0x00a7
            if (r3 == 0) goto L_0x00a2
            boolean r4 = r3.isEmpty()     // Catch: RuntimeException | StackOverflowError -> 0x00a7
            if (r4 == 0) goto L_0x00a0
            goto L_0x00a2
        L_0x00a0:
            r4 = 0
            goto L_0x00a3
        L_0x00a2:
            r4 = 1
        L_0x00a3:
            if (r4 == 0) goto L_0x00b7
            r3 = 0
            goto L_0x00b7
        L_0x00a7:
            r3 = move-exception
            java.lang.StringBuilder r4 = android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1.m(r5)
            java.lang.Class r3 = r3.getClass()
            r4.append(r3)
            java.lang.String r3 = r4.toString()
        L_0x00b7:
            if (r3 == 0) goto L_0x00c4
            java.lang.String r4 = ", info=["
            r0.append(r4)
            r0.append(r3)
            r0.append(r2)
        L_0x00c4:
            boolean r3 = r6.isDone()
            if (r3 == 0) goto L_0x00d4
            int r3 = r0.length()
            r0.delete(r1, r3)
            r6.addDoneString(r0)
        L_0x00d4:
            r0.append(r2)
            java.lang.String r6 = r0.toString()
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractFuture.toString():java.lang.String");
    }

    @Override // com.google.common.util.concurrent.internal.InternalFutureFailureAccess
    public final Throwable tryInternalFastPathGetFailure() {
        if (!(this instanceof Trusted)) {
            return null;
        }
        Object obj = this.value;
        if (obj instanceof Failure) {
            return ((Failure) obj).exception;
        }
        return null;
    }

    /* loaded from: classes.dex */
    public static final class Listener {
        public static final Listener TOMBSTONE = new Listener();
        public final Executor executor;
        public Listener next;
        public final Runnable task;

        public Listener(Runnable runnable, Executor executor) {
            this.task = runnable;
            this.executor = executor;
        }

        public Listener() {
            this.task = null;
            this.executor = null;
        }
    }

    public static void executeListener(Runnable runnable, Executor executor) {
        try {
            executor.execute(runnable);
        } catch (RuntimeException e) {
            Logger logger = log;
            Level level = Level.SEVERE;
            logger.log(level, "RuntimeException while executing runnable " + runnable + " with executor " + executor, (Throwable) e);
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:17:0x0030, code lost:
        java.util.concurrent.locks.LockSupport.park(r6);
     */
    /* JADX WARN: Code restructure failed: missing block: B:18:0x0037, code lost:
        if (java.lang.Thread.interrupted() != false) goto L_0x004b;
     */
    /* JADX WARN: Code restructure failed: missing block: B:19:0x0039, code lost:
        r0 = r6.value;
     */
    /* JADX WARN: Code restructure failed: missing block: B:20:0x003b, code lost:
        if (r0 == null) goto L_0x003f;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x003d, code lost:
        r4 = true;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x003f, code lost:
        r4 = false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:24:0x0044, code lost:
        if ((r4 & (!(r0 instanceof com.google.common.util.concurrent.AbstractFuture.SetFuture))) == false) goto L_0x0030;
     */
    /* JADX WARN: Code restructure failed: missing block: B:26:0x004a, code lost:
        return (V) getDoneValue(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x004b, code lost:
        removeWaiter(r3);
     */
    /* JADX WARN: Code restructure failed: missing block: B:28:0x0053, code lost:
        throw new java.lang.InterruptedException();
     */
    @Override // java.util.concurrent.Future
    @com.google.errorprone.annotations.CanIgnoreReturnValue
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public V get() throws java.lang.InterruptedException, java.util.concurrent.ExecutionException {
        /*
            r6 = this;
            boolean r0 = java.lang.Thread.interrupted()
            if (r0 != 0) goto L_0x0064
            java.lang.Object r0 = r6.value
            r1 = 0
            r2 = 1
            if (r0 == 0) goto L_0x000e
            r3 = r2
            goto L_0x000f
        L_0x000e:
            r3 = r1
        L_0x000f:
            boolean r4 = r0 instanceof com.google.common.util.concurrent.AbstractFuture.SetFuture
            r4 = r4 ^ r2
            r3 = r3 & r4
            if (r3 == 0) goto L_0x001a
            java.lang.Object r6 = getDoneValue(r0)
            return r6
        L_0x001a:
            com.google.common.util.concurrent.AbstractFuture$Waiter r0 = r6.waiters
            com.google.common.util.concurrent.AbstractFuture$Waiter r3 = com.google.common.util.concurrent.AbstractFuture.Waiter.TOMBSTONE
            if (r0 == r3) goto L_0x005a
            com.google.common.util.concurrent.AbstractFuture$Waiter r3 = new com.google.common.util.concurrent.AbstractFuture$Waiter
            r3.<init>()
        L_0x0025:
            com.google.common.util.concurrent.AbstractFuture$AtomicHelper r4 = com.google.common.util.concurrent.AbstractFuture.ATOMIC_HELPER
            r4.putNext(r3, r0)
            boolean r0 = r4.casWaiters(r6, r0, r3)
            if (r0 == 0) goto L_0x0054
        L_0x0030:
            java.util.concurrent.locks.LockSupport.park(r6)
            boolean r0 = java.lang.Thread.interrupted()
            if (r0 != 0) goto L_0x004b
            java.lang.Object r0 = r6.value
            if (r0 == 0) goto L_0x003f
            r4 = r2
            goto L_0x0040
        L_0x003f:
            r4 = r1
        L_0x0040:
            boolean r5 = r0 instanceof com.google.common.util.concurrent.AbstractFuture.SetFuture
            r5 = r5 ^ r2
            r4 = r4 & r5
            if (r4 == 0) goto L_0x0030
            java.lang.Object r6 = getDoneValue(r0)
            return r6
        L_0x004b:
            r6.removeWaiter(r3)
            java.lang.InterruptedException r6 = new java.lang.InterruptedException
            r6.<init>()
            throw r6
        L_0x0054:
            com.google.common.util.concurrent.AbstractFuture$Waiter r0 = r6.waiters
            com.google.common.util.concurrent.AbstractFuture$Waiter r4 = com.google.common.util.concurrent.AbstractFuture.Waiter.TOMBSTONE
            if (r0 != r4) goto L_0x0025
        L_0x005a:
            java.lang.Object r6 = r6.value
            java.util.Objects.requireNonNull(r6)
            java.lang.Object r6 = getDoneValue(r6)
            return r6
        L_0x0064:
            java.lang.InterruptedException r6 = new java.lang.InterruptedException
            r6.<init>()
            throw r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.common.util.concurrent.AbstractFuture.get():java.lang.Object");
    }

    /* loaded from: classes.dex */
    public static final class SynchronizedHelper extends AtomicHelper {
        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final boolean casListeners(AbstractFuture<?> abstractFuture, Listener listener, Listener listener2) {
            synchronized (abstractFuture) {
                if (abstractFuture.listeners != listener) {
                    return false;
                }
                abstractFuture.listeners = listener2;
                return true;
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final boolean casValue(AbstractFuture<?> abstractFuture, Object obj, Object obj2) {
            synchronized (abstractFuture) {
                if (abstractFuture.value != obj) {
                    return false;
                }
                abstractFuture.value = obj2;
                return true;
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final boolean casWaiters(AbstractFuture<?> abstractFuture, Waiter waiter, Waiter waiter2) {
            synchronized (abstractFuture) {
                if (abstractFuture.waiters != waiter) {
                    return false;
                }
                abstractFuture.waiters = waiter2;
                return true;
            }
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final void putNext(Waiter waiter, Waiter waiter2) {
            waiter.next = waiter2;
        }

        @Override // com.google.common.util.concurrent.AbstractFuture.AtomicHelper
        public final void putThread(Waiter waiter, Thread thread) {
            waiter.thread = thread;
        }
    }
}
