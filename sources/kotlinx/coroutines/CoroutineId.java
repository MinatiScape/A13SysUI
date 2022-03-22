package kotlinx.coroutines;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import kotlin.coroutines.AbstractCoroutineContextElement;
import kotlin.coroutines.CoroutineContext;
import kotlin.text.StringsKt__StringsKt;
/* compiled from: CoroutineContext.kt */
/* loaded from: classes.dex */
public final class CoroutineId extends AbstractCoroutineContextElement implements ThreadContextElement<String> {
    public static final Key Key = new Key();
    public final long id;

    /* compiled from: CoroutineContext.kt */
    /* loaded from: classes.dex */
    public static final class Key implements CoroutineContext.Key<CoroutineId> {
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof CoroutineId) && this.id == ((CoroutineId) obj).id;
    }

    public final int hashCode() {
        return Long.hashCode(this.id);
    }

    public CoroutineId(long j) {
        super(Key);
        this.id = j;
    }

    @Override // kotlinx.coroutines.ThreadContextElement
    public final void restoreThreadContext(Object obj) {
        Thread.currentThread().setName((String) obj);
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("CoroutineId(");
        m.append(this.id);
        m.append(')');
        return m.toString();
    }

    @Override // kotlinx.coroutines.ThreadContextElement
    public final String updateThreadContext(CoroutineContext coroutineContext) {
        int i;
        CoroutineName coroutineName = (CoroutineName) coroutineContext.get(CoroutineName.Key);
        Thread currentThread = Thread.currentThread();
        String name = currentThread.getName();
        int lastIndex = StringsKt__StringsKt.getLastIndex(name);
        if (!(name instanceof String)) {
            i = StringsKt__StringsKt.indexOf$StringsKt__StringsKt(name, " @", lastIndex, 0, false, true);
        } else {
            i = name.lastIndexOf(" @", lastIndex);
        }
        if (i < 0) {
            i = name.length();
        }
        StringBuilder sb = new StringBuilder(i + 9 + 10);
        sb.append(name.substring(0, i));
        sb.append(" @");
        sb.append("coroutine");
        sb.append('#');
        sb.append(this.id);
        currentThread.setName(sb.toString());
        return name;
    }
}
