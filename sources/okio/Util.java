package okio;
/* compiled from: -Util.kt */
/* renamed from: okio.-Util  reason: invalid class name */
/* loaded from: classes.dex */
public final class Util {
    public static final void checkOffsetAndCount(long j, long j2, long j3) {
        if ((j2 | j3) < 0 || j2 > j || j - j2 < j3) {
            throw new ArrayIndexOutOfBoundsException("size=" + j + " offset=" + j2 + " byteCount=" + j3);
        }
    }
}
