package kotlin.text;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import kotlin.jvm.internal.markers.KMappedMarker;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt___RangesKt;
/* compiled from: Strings.kt */
/* loaded from: classes.dex */
public final class DelimitedRangesSequence$iterator$1 implements Iterator<IntRange>, KMappedMarker {
    public int counter;
    public int currentStartIndex;
    public IntRange nextItem;
    public int nextSearchIndex;
    public int nextState = -1;
    public final /* synthetic */ DelimitedRangesSequence this$0;

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException("Operation is not supported for read-only collection");
    }

    public DelimitedRangesSequence$iterator$1(DelimitedRangesSequence delimitedRangesSequence) {
        this.this$0 = delimitedRangesSequence;
        int coerceIn = RangesKt___RangesKt.coerceIn(delimitedRangesSequence.startIndex, 0, delimitedRangesSequence.input.length());
        this.currentStartIndex = coerceIn;
        this.nextSearchIndex = coerceIn;
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x0019, code lost:
        if (r6 < r3) goto L_0x001b;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void calcNext() {
        /*
            r7 = this;
            int r0 = r7.nextSearchIndex
            r1 = 0
            if (r0 >= 0) goto L_0x000c
            r7.nextState = r1
            r0 = 0
            r7.nextItem = r0
            goto L_0x0091
        L_0x000c:
            kotlin.text.DelimitedRangesSequence r2 = r7.this$0
            int r3 = r2.limit
            r4 = 1
            r5 = -1
            if (r3 <= 0) goto L_0x001b
            int r6 = r7.counter
            int r6 = r6 + r4
            r7.counter = r6
            if (r6 >= r3) goto L_0x0023
        L_0x001b:
            java.lang.CharSequence r2 = r2.input
            int r2 = r2.length()
            if (r0 <= r2) goto L_0x0037
        L_0x0023:
            kotlin.ranges.IntRange r0 = new kotlin.ranges.IntRange
            int r1 = r7.currentStartIndex
            kotlin.text.DelimitedRangesSequence r2 = r7.this$0
            java.lang.CharSequence r2 = r2.input
            int r2 = kotlin.text.StringsKt__StringsKt.getLastIndex(r2)
            r0.<init>(r1, r2)
            r7.nextItem = r0
            r7.nextSearchIndex = r5
            goto L_0x008f
        L_0x0037:
            kotlin.text.DelimitedRangesSequence r0 = r7.this$0
            kotlin.jvm.functions.Function2<java.lang.CharSequence, java.lang.Integer, kotlin.Pair<java.lang.Integer, java.lang.Integer>> r2 = r0.getNextMatch
            java.lang.CharSequence r0 = r0.input
            int r3 = r7.nextSearchIndex
            java.lang.Integer r3 = java.lang.Integer.valueOf(r3)
            java.lang.Object r0 = r2.invoke(r0, r3)
            kotlin.Pair r0 = (kotlin.Pair) r0
            if (r0 != 0) goto L_0x005f
            kotlin.ranges.IntRange r0 = new kotlin.ranges.IntRange
            int r1 = r7.currentStartIndex
            kotlin.text.DelimitedRangesSequence r2 = r7.this$0
            java.lang.CharSequence r2 = r2.input
            int r2 = kotlin.text.StringsKt__StringsKt.getLastIndex(r2)
            r0.<init>(r1, r2)
            r7.nextItem = r0
            r7.nextSearchIndex = r5
            goto L_0x008f
        L_0x005f:
            java.lang.Object r2 = r0.component1()
            java.lang.Number r2 = (java.lang.Number) r2
            int r2 = r2.intValue()
            java.lang.Object r0 = r0.component2()
            java.lang.Number r0 = (java.lang.Number) r0
            int r0 = r0.intValue()
            int r3 = r7.currentStartIndex
            r5 = -2147483648(0xffffffff80000000, float:-0.0)
            if (r2 > r5) goto L_0x007c
            kotlin.ranges.IntRange r3 = kotlin.ranges.IntRange.EMPTY
            goto L_0x0084
        L_0x007c:
            kotlin.ranges.IntRange r5 = new kotlin.ranges.IntRange
            int r6 = r2 + (-1)
            r5.<init>(r3, r6)
            r3 = r5
        L_0x0084:
            r7.nextItem = r3
            int r2 = r2 + r0
            r7.currentStartIndex = r2
            if (r0 != 0) goto L_0x008c
            r1 = r4
        L_0x008c:
            int r2 = r2 + r1
            r7.nextSearchIndex = r2
        L_0x008f:
            r7.nextState = r4
        L_0x0091:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: kotlin.text.DelimitedRangesSequence$iterator$1.calcNext():void");
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        if (this.nextState == -1) {
            calcNext();
        }
        if (this.nextState == 1) {
            return true;
        }
        return false;
    }

    @Override // java.util.Iterator
    public final IntRange next() {
        if (this.nextState == -1) {
            calcNext();
        }
        if (this.nextState != 0) {
            IntRange intRange = this.nextItem;
            Objects.requireNonNull(intRange, "null cannot be cast to non-null type kotlin.ranges.IntRange");
            this.nextItem = null;
            this.nextState = -1;
            return intRange;
        }
        throw new NoSuchElementException();
    }
}
