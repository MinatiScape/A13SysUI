package kotlin.coroutines.jvm.internal;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.jvm.internal.ModuleNameRetriever;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ContinuationImpl.kt */
/* loaded from: classes.dex */
public abstract class BaseContinuationImpl implements Continuation<Object>, CoroutineStackFrame, Serializable {
    private final Continuation<Object> completion;

    public abstract Object invokeSuspend(Object obj);

    public void releaseIntercepted() {
    }

    public Continuation<Unit> create(Object obj, Continuation<?> continuation) {
        throw new UnsupportedOperationException("create(Any?;Continuation) has not been overridden");
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public CoroutineStackFrame getCallerFrame() {
        Continuation<Object> continuation = this.completion;
        if (continuation instanceof CoroutineStackFrame) {
            return (CoroutineStackFrame) continuation;
        }
        return null;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v0, types: [kotlin.coroutines.Continuation, java.lang.Object, kotlin.coroutines.Continuation<java.lang.Object>] */
    /* JADX WARN: Unknown variable types count: 1 */
    @Override // kotlin.coroutines.Continuation
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void resumeWith(java.lang.Object r3) {
        /*
            r2 = this;
        L_0x0000:
            kotlin.coroutines.jvm.internal.BaseContinuationImpl r2 = (kotlin.coroutines.jvm.internal.BaseContinuationImpl) r2
            kotlin.coroutines.Continuation<java.lang.Object> r0 = r2.completion
            kotlin.jvm.internal.Intrinsics.checkNotNull(r0)
            java.lang.Object r3 = r2.invokeSuspend(r3)     // Catch: all -> 0x0010
            kotlin.coroutines.intrinsics.CoroutineSingletons r1 = kotlin.coroutines.intrinsics.CoroutineSingletons.COROUTINE_SUSPENDED     // Catch: all -> 0x0010
            if (r3 != r1) goto L_0x0017
            return
        L_0x0010:
            r3 = move-exception
            kotlin.Result$Failure r1 = new kotlin.Result$Failure
            r1.<init>(r3)
            r3 = r1
        L_0x0017:
            r2.releaseIntercepted()
            boolean r2 = r0 instanceof kotlin.coroutines.jvm.internal.BaseContinuationImpl
            if (r2 == 0) goto L_0x0020
            r2 = r0
            goto L_0x0000
        L_0x0020:
            r0.resumeWith(r3)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.coroutines.jvm.internal.BaseContinuationImpl.resumeWith(java.lang.Object):void");
    }

    public BaseContinuationImpl(Continuation<Object> continuation) {
        this.completion = continuation;
    }

    @Override // kotlin.coroutines.jvm.internal.CoroutineStackFrame
    public StackTraceElement getStackTraceElement() {
        int i;
        String str;
        Object obj;
        Object obj2;
        Object obj3;
        Integer num;
        int i2;
        DebugMetadata debugMetadata = (DebugMetadata) getClass().getAnnotation(DebugMetadata.class);
        String str2 = null;
        if (debugMetadata == null) {
            return null;
        }
        int v = debugMetadata.v();
        if (v <= 1) {
            int i3 = -1;
            try {
                Field declaredField = getClass().getDeclaredField("label");
                declaredField.setAccessible(true);
                Object obj4 = declaredField.get(this);
                if (obj4 instanceof Integer) {
                    num = (Integer) obj4;
                } else {
                    num = null;
                }
                if (num == null) {
                    i2 = 0;
                } else {
                    i2 = num.intValue();
                }
                i = i2 - 1;
            } catch (Exception unused) {
                i = -1;
            }
            if (i >= 0) {
                i3 = debugMetadata.l()[i];
            }
            ModuleNameRetriever.Cache cache = ModuleNameRetriever.cache;
            if (cache == null) {
                try {
                    ModuleNameRetriever.Cache cache2 = new ModuleNameRetriever.Cache(Class.class.getDeclaredMethod("getModule", new Class[0]), getClass().getClassLoader().loadClass("java.lang.Module").getDeclaredMethod("getDescriptor", new Class[0]), getClass().getClassLoader().loadClass("java.lang.module.ModuleDescriptor").getDeclaredMethod("name", new Class[0]));
                    ModuleNameRetriever.cache = cache2;
                    cache = cache2;
                } catch (Exception unused2) {
                    cache = ModuleNameRetriever.notOnJava9;
                    ModuleNameRetriever.cache = cache;
                }
            }
            if (cache != ModuleNameRetriever.notOnJava9) {
                Method method = cache.getModuleMethod;
                if (method == null) {
                    obj = null;
                } else {
                    obj = method.invoke(getClass(), new Object[0]);
                }
                if (obj != null) {
                    Method method2 = cache.getDescriptorMethod;
                    if (method2 == null) {
                        obj2 = null;
                    } else {
                        obj2 = method2.invoke(obj, new Object[0]);
                    }
                    if (obj2 != null) {
                        Method method3 = cache.nameMethod;
                        if (method3 == null) {
                            obj3 = null;
                        } else {
                            obj3 = method3.invoke(obj2, new Object[0]);
                        }
                        if (obj3 instanceof String) {
                            str2 = (String) obj3;
                        }
                    }
                }
            }
            if (str2 == null) {
                str = debugMetadata.c();
            } else {
                str = ((Object) str2) + '/' + debugMetadata.c();
            }
            return new StackTraceElement(str, debugMetadata.m(), debugMetadata.f(), i3);
        }
        throw new IllegalStateException(("Debug metadata version mismatch. Expected: 1, got " + v + ". Please update the Kotlin standard library.").toString());
    }

    public String toString() {
        Object stackTraceElement = getStackTraceElement();
        if (stackTraceElement == null) {
            stackTraceElement = getClass().getName();
        }
        return Intrinsics.stringPlus("Continuation at ", stackTraceElement);
    }

    public final Continuation<Object> getCompletion() {
        return this.completion;
    }
}
