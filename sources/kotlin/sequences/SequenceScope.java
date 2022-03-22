package kotlin.sequences;

import java.util.Iterator;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
/* compiled from: SequenceBuilder.kt */
/* loaded from: classes.dex */
public abstract class SequenceScope<T> {
    public abstract void yield(Object obj, Continuation continuation);

    public abstract Object yieldAll(Iterator<? extends T> it, Continuation<? super Unit> continuation);
}
