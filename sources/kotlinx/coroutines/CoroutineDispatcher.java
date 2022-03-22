package kotlinx.coroutines;

import java.util.Objects;
import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.AbstractCoroutineContextKey;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.ContinuationInterceptor;
import kotlin.coroutines.CoroutineContext;
import kotlin.coroutines.EmptyCoroutineContext;
import kotlin.coroutines.jvm.internal.ContinuationImpl;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
import kotlinx.atomicfu.AtomicRef;
import kotlinx.coroutines.internal.DispatchedContinuation;
import kotlinx.coroutines.internal.DispatchedContinuationKt;
/* compiled from: CoroutineDispatcher.kt */
/* loaded from: classes.dex */
public abstract class CoroutineDispatcher extends AbstractCoroutineContextElement implements ContinuationInterceptor {
    public static final Key Key = new Key();

    /* compiled from: CoroutineDispatcher.kt */
    /* loaded from: classes.dex */
    public static final class Key extends AbstractCoroutineContextKey<ContinuationInterceptor, CoroutineDispatcher> {

        /* compiled from: CoroutineDispatcher.kt */
        /* renamed from: kotlinx.coroutines.CoroutineDispatcher$Key$1  reason: invalid class name */
        /* loaded from: classes.dex */
        public static final class AnonymousClass1 extends Lambda implements Function1<CoroutineContext.Element, CoroutineDispatcher> {
            public static final AnonymousClass1 INSTANCE = new AnonymousClass1();

            public AnonymousClass1() {
                super(1);
            }

            @Override // kotlin.jvm.functions.Function1
            public final CoroutineDispatcher invoke(CoroutineContext.Element element) {
                CoroutineContext.Element element2 = element;
                if (element2 instanceof CoroutineDispatcher) {
                    return (CoroutineDispatcher) element2;
                }
                return null;
            }
        }

        public Key() {
            super(ContinuationInterceptor.Key.$$INSTANCE, AnonymousClass1.INSTANCE);
        }
    }

    public abstract void dispatch(CoroutineContext coroutineContext, Runnable runnable);

    public boolean isDispatchNeeded() {
        return true;
    }

    public CoroutineDispatcher() {
        super(ContinuationInterceptor.Key.$$INSTANCE);
    }

    /* JADX WARN: Removed duplicated region for block: B:12:0x001a  */
    /* JADX WARN: Removed duplicated region for block: B:19:? A[RETURN, SYNTHETIC] */
    /* JADX WARN: Type inference failed for: r3v1, types: [E extends kotlin.coroutines.CoroutineContext$Element] */
    /* JADX WARN: Type inference failed for: r3v8 */
    @Override // kotlin.coroutines.AbstractCoroutineContextElement, kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final <E extends kotlin.coroutines.CoroutineContext.Element> E get(kotlin.coroutines.CoroutineContext.Key<E> r4) {
        /*
            r3 = this;
            boolean r0 = r4 instanceof kotlin.coroutines.AbstractCoroutineContextKey
            r1 = 0
            if (r0 == 0) goto L_0x0027
            kotlin.coroutines.AbstractCoroutineContextKey r4 = (kotlin.coroutines.AbstractCoroutineContextKey) r4
            kotlin.coroutines.CoroutineContext$Key r0 = r3.getKey()
            if (r0 == r4) goto L_0x0014
            kotlin.coroutines.CoroutineContext$Key<?> r2 = r4.topmostKey
            if (r2 != r0) goto L_0x0012
            goto L_0x0017
        L_0x0012:
            r0 = 0
            goto L_0x0018
        L_0x0014:
            java.util.Objects.requireNonNull(r4)
        L_0x0017:
            r0 = 1
        L_0x0018:
            if (r0 == 0) goto L_0x002e
            kotlin.jvm.functions.Function1<kotlin.coroutines.CoroutineContext$Element, E extends B> r4 = r4.safeCast
            java.lang.Object r3 = r4.invoke(r3)
            kotlin.coroutines.CoroutineContext$Element r3 = (kotlin.coroutines.CoroutineContext.Element) r3
            boolean r4 = r3 instanceof kotlin.coroutines.CoroutineContext.Element
            if (r4 == 0) goto L_0x002e
            goto L_0x002d
        L_0x0027:
            kotlin.coroutines.ContinuationInterceptor$Key r0 = kotlin.coroutines.ContinuationInterceptor.Key.$$INSTANCE
            if (r0 != r4) goto L_0x002c
            goto L_0x002d
        L_0x002c:
            r3 = r1
        L_0x002d:
            r1 = r3
        L_0x002e:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlinx.coroutines.CoroutineDispatcher.get(kotlin.coroutines.CoroutineContext$Key):kotlin.coroutines.CoroutineContext$Element");
    }

    @Override // kotlin.coroutines.ContinuationInterceptor
    public final DispatchedContinuation interceptContinuation(ContinuationImpl continuationImpl) {
        return new DispatchedContinuation(this, continuationImpl);
    }

    @Override // kotlin.coroutines.AbstractCoroutineContextElement, kotlin.coroutines.CoroutineContext.Element, kotlin.coroutines.CoroutineContext
    public final CoroutineContext minusKey(CoroutineContext.Key<?> key) {
        boolean z;
        if (key instanceof AbstractCoroutineContextKey) {
            AbstractCoroutineContextKey abstractCoroutineContextKey = (AbstractCoroutineContextKey) key;
            CoroutineContext.Key<?> key2 = getKey();
            if (key2 == abstractCoroutineContextKey) {
                Objects.requireNonNull(abstractCoroutineContextKey);
            } else if (abstractCoroutineContextKey.topmostKey != key2) {
                z = false;
                if (!z && ((CoroutineContext.Element) abstractCoroutineContextKey.safeCast.invoke(this)) != null) {
                    return EmptyCoroutineContext.INSTANCE;
                }
            }
            z = true;
            return !z ? this : this;
        } else if (ContinuationInterceptor.Key.$$INSTANCE == key) {
            return EmptyCoroutineContext.INSTANCE;
        } else {
            return this;
        }
    }

    @Override // kotlin.coroutines.ContinuationInterceptor
    public final void releaseInterceptedContinuation(Continuation<?> continuation) {
        CancellableContinuationImpl cancellableContinuationImpl;
        DispatchedContinuation dispatchedContinuation = (DispatchedContinuation) continuation;
        AtomicRef<Object> atomicRef = dispatchedContinuation._reusableCancellableContinuation;
        do {
            Objects.requireNonNull(atomicRef);
        } while (atomicRef.value == DispatchedContinuationKt.REUSABLE_CLAIMED);
        AtomicRef<Object> atomicRef2 = dispatchedContinuation._reusableCancellableContinuation;
        Objects.requireNonNull(atomicRef2);
        Object obj = atomicRef2.value;
        if (obj instanceof CancellableContinuationImpl) {
            cancellableContinuationImpl = (CancellableContinuationImpl) obj;
        } else {
            cancellableContinuationImpl = null;
        }
        if (cancellableContinuationImpl != null) {
            cancellableContinuationImpl.detachChild$external__kotlinx_coroutines__android_common__kotlinx_coroutines();
        }
    }

    public String toString() {
        return DebugStringsKt.getClassSimpleName(this) + '@' + DebugStringsKt.getHexAddress(this);
    }

    public void dispatchYield(CoroutineContext coroutineContext, Runnable runnable) {
        dispatch(coroutineContext, runnable);
    }
}
