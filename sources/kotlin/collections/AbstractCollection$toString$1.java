package kotlin.collections;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: AbstractCollection.kt */
/* loaded from: classes.dex */
public final class AbstractCollection$toString$1 extends Lambda implements Function1<E, CharSequence> {
    public final /* synthetic */ AbstractCollection<E> this$0;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    /* JADX WARN: Multi-variable type inference failed */
    public AbstractCollection$toString$1(AbstractCollection<? extends E> abstractCollection) {
        super(1);
        this.this$0 = abstractCollection;
    }

    @Override // kotlin.jvm.functions.Function1
    public final CharSequence invoke(Object obj) {
        if (obj == this.this$0) {
            return "(this Collection)";
        }
        return String.valueOf(obj);
    }
}
