package okio;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.os.Build;
import androidx.constraintlayout.motion.widget.MotionController$$ExternalSyntheticOutline0;
import java.util.Locale;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Okio {
    public static boolean contains(Object[] objArr, Object obj) {
        for (Object obj2 : objArr) {
            if (Objects.equals(obj2, obj)) {
                return true;
            }
        }
        return false;
    }

    public static Object createTable(int i) {
        if (i < 2 || i > 1073741824 || Integer.highestOneBit(i) != i) {
            throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("must be power of 2 between 2^1 and 2^30: ", i));
        } else if (i <= 256) {
            return new byte[i];
        } else {
            if (i <= 65536) {
                return new short[i];
            }
            return new int[i];
        }
    }

    public static float interpolate(float[] fArr, float f, float f2) {
        if (f2 >= 1.0f) {
            return 1.0f;
        }
        if (f2 <= 0.0f) {
            return 0.0f;
        }
        int min = Math.min((int) ((fArr.length - 1) * f2), fArr.length - 2);
        return MotionController$$ExternalSyntheticOutline0.m(fArr[min + 1], fArr[min], (f2 - (min * f)) / f, fArr[min]);
    }

    public static boolean isDateInputKeyboardMissingSeparatorCharacters() {
        String str = Build.MANUFACTURER;
        Locale locale = Locale.ENGLISH;
        if (str.toLowerCase(locale).equals("lge") || str.toLowerCase(locale).equals("samsung")) {
            return true;
        }
        return false;
    }

    public static int tableGet(Object obj, int i) {
        if (obj instanceof byte[]) {
            return ((byte[]) obj)[i] & 255;
        }
        if (obj instanceof short[]) {
            return ((short[]) obj)[i] & 65535;
        }
        return ((int[]) obj)[i];
    }

    public static void tableSet(Object obj, int i, int i2) {
        if (obj instanceof byte[]) {
            ((byte[]) obj)[i] = (byte) i2;
        } else if (obj instanceof short[]) {
            ((short[]) obj)[i] = (short) i2;
        } else {
            ((int[]) obj)[i] = i2;
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x002a, code lost:
        r9 = r6 & r11;
     */
    /* JADX WARN: Code restructure failed: missing block: B:14:0x002c, code lost:
        if (r5 != (-1)) goto L_0x0032;
     */
    /* JADX WARN: Code restructure failed: missing block: B:15:0x002e, code lost:
        tableSet(r12, r1, r9);
     */
    /* JADX WARN: Code restructure failed: missing block: B:16:0x0032, code lost:
        r13[r5] = (r9 & r11) | (r13[r5] & r4);
     */
    /* JADX WARN: Code restructure failed: missing block: B:17:0x0039, code lost:
        return r2;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int remove(java.lang.Object r9, java.lang.Object r10, int r11, java.lang.Object r12, int[] r13, java.lang.Object[] r14, java.lang.Object[] r15) {
        /*
            int r0 = okio.Okio__OkioKt.smearedHash(r9)
            r1 = r0 & r11
            int r2 = tableGet(r12, r1)
            r3 = -1
            if (r2 != 0) goto L_0x000e
            return r3
        L_0x000e:
            int r4 = ~r11
            r0 = r0 & r4
            r5 = r3
        L_0x0011:
            int r2 = r2 + r3
            r6 = r13[r2]
            r7 = r6 & r4
            if (r7 != r0) goto L_0x003a
            r7 = r14[r2]
            boolean r7 = androidx.recyclerview.R$dimen.equal(r9, r7)
            if (r7 == 0) goto L_0x003a
            if (r15 == 0) goto L_0x002a
            r7 = r15[r2]
            boolean r7 = androidx.recyclerview.R$dimen.equal(r10, r7)
            if (r7 == 0) goto L_0x003a
        L_0x002a:
            r9 = r6 & r11
            if (r5 != r3) goto L_0x0032
            tableSet(r12, r1, r9)
            goto L_0x0039
        L_0x0032:
            r10 = r13[r5]
            r10 = r10 & r4
            r9 = r9 & r11
            r9 = r9 | r10
            r13[r5] = r9
        L_0x0039:
            return r2
        L_0x003a:
            r5 = r6 & r11
            if (r5 != 0) goto L_0x003f
            return r3
        L_0x003f:
            r8 = r5
            r5 = r2
            r2 = r8
            goto L_0x0011
        */
        throw new UnsupportedOperationException("Method not decompiled: okio.Okio.remove(java.lang.Object, java.lang.Object, int, java.lang.Object, int[], java.lang.Object[], java.lang.Object[]):int");
    }
}
