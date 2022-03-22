package kotlinx.atomicfu;
/* compiled from: AtomicFU.kt */
/* loaded from: classes.dex */
public final class AtomicFU {
    public static float clamp(float f, float f2, float f3) {
        return f < f2 ? f2 : f > f3 ? f3 : f;
    }

    public static int clamp(int i, int i2, int i3) {
        return i < i2 ? i2 : i > i3 ? i3 : i;
    }

    public static final AtomicRef atomic(Object obj) {
        return new AtomicRef(obj);
    }
}
