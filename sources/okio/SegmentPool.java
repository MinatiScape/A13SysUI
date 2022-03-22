package okio;

import java.util.concurrent.atomic.AtomicReference;
/* compiled from: SegmentPool.kt */
/* loaded from: classes.dex */
public final class SegmentPool {
    public static final int HASH_BUCKET_COUNT;
    public static final Segment LOCK = new Segment(new byte[0], 0, 0, false);
    public static final AtomicReference<Segment>[] hashBuckets;

    static {
        int highestOneBit = Integer.highestOneBit((Runtime.getRuntime().availableProcessors() * 2) - 1);
        HASH_BUCKET_COUNT = highestOneBit;
        AtomicReference<Segment>[] atomicReferenceArr = new AtomicReference[highestOneBit];
        for (int i = 0; i < highestOneBit; i++) {
            atomicReferenceArr[i] = new AtomicReference<>();
        }
        hashBuckets = atomicReferenceArr;
    }

    public static final void recycle(Segment segment) {
        boolean z;
        int i;
        if (segment.next == null && segment.prev == null) {
            z = true;
        } else {
            z = false;
        }
        if (!z) {
            throw new IllegalArgumentException("Failed requirement.".toString());
        } else if (!segment.shared) {
            AtomicReference<Segment> atomicReference = hashBuckets[(int) (Thread.currentThread().getId() & (HASH_BUCKET_COUNT - 1))];
            Segment segment2 = atomicReference.get();
            if (segment2 != LOCK) {
                if (segment2 == null) {
                    i = 0;
                } else {
                    i = segment2.limit;
                }
                if (i < 65536) {
                    segment.next = segment2;
                    segment.pos = 0;
                    segment.limit = i + 8192;
                    if (!atomicReference.compareAndSet(segment2, segment)) {
                        segment.next = null;
                    }
                }
            }
        }
    }

    public static final Segment take() {
        AtomicReference<Segment> atomicReference = hashBuckets[(int) (Thread.currentThread().getId() & (HASH_BUCKET_COUNT - 1))];
        Segment segment = LOCK;
        Segment andSet = atomicReference.getAndSet(segment);
        if (andSet == segment) {
            return new Segment();
        }
        if (andSet == null) {
            atomicReference.set(null);
            return new Segment();
        }
        atomicReference.set(andSet.next);
        andSet.next = null;
        andSet.limit = 0;
        return andSet;
    }
}
