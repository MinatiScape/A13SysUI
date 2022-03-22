package kotlin.collections;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import androidx.core.graphics.Insets$$ExternalSyntheticOutline0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Lambda;
/* compiled from: _Collections.kt */
/* loaded from: classes.dex */
final class CollectionsKt___CollectionsKt$elementAt$1 extends Lambda implements Function1<Integer, Object> {
    public final /* synthetic */ int $index;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CollectionsKt___CollectionsKt$elementAt$1(int i) {
        super(1);
        this.$index = i;
    }

    @Override // kotlin.jvm.functions.Function1
    public final Object invoke(Integer num) {
        num.intValue();
        throw new IndexOutOfBoundsException(Insets$$ExternalSyntheticOutline0.m(VendorAtomValue$$ExternalSyntheticOutline1.m("Collection doesn't contain element at index "), this.$index, '.'));
    }
}
