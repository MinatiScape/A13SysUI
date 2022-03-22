package androidx.collection;

import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public class LruCache<K, V> {
    public int hitCount;
    public final LinkedHashMap<K, V> map;
    public int maxSize;
    public int missCount;
    public int size;

    public final synchronized String toString() {
        int i;
        int i2 = this.hitCount;
        int i3 = this.missCount + i2;
        if (i3 != 0) {
            i = (i2 * 100) / i3;
        } else {
            i = 0;
        }
        return String.format(Locale.US, "LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", Integer.valueOf(this.maxSize), Integer.valueOf(this.hitCount), Integer.valueOf(this.missCount), Integer.valueOf(i));
    }

    /* JADX WARN: Code restructure failed: missing block: B:20:0x0062, code lost:
        throw new java.lang.IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void trimToSize(int r3) {
        /*
            r2 = this;
        L_0x0000:
            monitor-enter(r2)
            int r0 = r2.size     // Catch: all -> 0x0063
            if (r0 < 0) goto L_0x0044
            java.util.LinkedHashMap<K, V> r0 = r2.map     // Catch: all -> 0x0063
            boolean r0 = r0.isEmpty()     // Catch: all -> 0x0063
            if (r0 == 0) goto L_0x0011
            int r0 = r2.size     // Catch: all -> 0x0063
            if (r0 != 0) goto L_0x0044
        L_0x0011:
            int r0 = r2.size     // Catch: all -> 0x0063
            if (r0 <= r3) goto L_0x0042
            java.util.LinkedHashMap<K, V> r0 = r2.map     // Catch: all -> 0x0063
            boolean r0 = r0.isEmpty()     // Catch: all -> 0x0063
            if (r0 == 0) goto L_0x001e
            goto L_0x0042
        L_0x001e:
            java.util.LinkedHashMap<K, V> r0 = r2.map     // Catch: all -> 0x0063
            java.util.Set r0 = r0.entrySet()     // Catch: all -> 0x0063
            java.util.Iterator r0 = r0.iterator()     // Catch: all -> 0x0063
            java.lang.Object r0 = r0.next()     // Catch: all -> 0x0063
            java.util.Map$Entry r0 = (java.util.Map.Entry) r0     // Catch: all -> 0x0063
            java.lang.Object r1 = r0.getKey()     // Catch: all -> 0x0063
            r0.getValue()     // Catch: all -> 0x0063
            java.util.LinkedHashMap<K, V> r0 = r2.map     // Catch: all -> 0x0063
            r0.remove(r1)     // Catch: all -> 0x0063
            int r0 = r2.size     // Catch: all -> 0x0063
            int r0 = r0 + (-1)
            r2.size = r0     // Catch: all -> 0x0063
            monitor-exit(r2)     // Catch: all -> 0x0063
            goto L_0x0000
        L_0x0042:
            monitor-exit(r2)     // Catch: all -> 0x0063
            return
        L_0x0044:
            java.lang.IllegalStateException r3 = new java.lang.IllegalStateException     // Catch: all -> 0x0063
            java.lang.StringBuilder r0 = new java.lang.StringBuilder     // Catch: all -> 0x0063
            r0.<init>()     // Catch: all -> 0x0063
            java.lang.Class r1 = r2.getClass()     // Catch: all -> 0x0063
            java.lang.String r1 = r1.getName()     // Catch: all -> 0x0063
            r0.append(r1)     // Catch: all -> 0x0063
            java.lang.String r1 = ".sizeOf() is reporting inconsistent results!"
            r0.append(r1)     // Catch: all -> 0x0063
            java.lang.String r0 = r0.toString()     // Catch: all -> 0x0063
            r3.<init>(r0)     // Catch: all -> 0x0063
            throw r3     // Catch: all -> 0x0063
        L_0x0063:
            r3 = move-exception
            monitor-exit(r2)     // Catch: all -> 0x0063
            throw r3
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.collection.LruCache.trimToSize(int):void");
    }

    public final V get(K k) {
        Objects.requireNonNull(k, "key == null");
        synchronized (this) {
            V v = this.map.get(k);
            if (v != null) {
                this.hitCount++;
                return v;
            }
            this.missCount++;
            return null;
        }
    }

    public final V put(K k, V v) {
        V put;
        if (k != null) {
            synchronized (this) {
                this.size++;
                put = this.map.put(k, v);
                if (put != null) {
                    this.size--;
                }
            }
            trimToSize(this.maxSize);
            return put;
        }
        throw new NullPointerException("key == null || value == null");
    }

    public LruCache(int i) {
        if (i > 0) {
            this.maxSize = i;
            this.map = new LinkedHashMap<>(0, 0.75f, true);
            return;
        }
        throw new IllegalArgumentException("maxSize <= 0");
    }
}
