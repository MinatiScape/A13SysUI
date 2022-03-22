package kotlin.text;

import java.util.Collection;
import java.util.Iterator;
import kotlin.collections.IntIterator;
import kotlin.ranges.IntProgressionIterator;
import kotlin.ranges.IntRange;
/* compiled from: StringsJVM.kt */
/* loaded from: classes.dex */
public class StringsKt__StringsJVMKt extends StringsKt__StringNumberConversionsKt {
    public static String replace$default(String str, String str2, String str3) {
        int indexOf = StringsKt__StringsKt.indexOf(str, str2, 0, false);
        if (indexOf < 0) {
            return str;
        }
        int length = str2.length();
        int i = 1;
        if (length >= 1) {
            i = length;
        }
        int length2 = str3.length() + (str.length() - length);
        if (length2 >= 0) {
            StringBuilder sb = new StringBuilder(length2);
            int i2 = 0;
            do {
                sb.append((CharSequence) str, i2, indexOf);
                sb.append(str3);
                i2 = indexOf + length;
                if (indexOf >= str.length()) {
                    break;
                }
                indexOf = StringsKt__StringsKt.indexOf(str, str2, indexOf + i, false);
            } while (indexOf > 0);
            sb.append((CharSequence) str, i2, str.length());
            return sb.toString();
        }
        throw new OutOfMemoryError();
    }

    public static final boolean regionMatches(String str, int i, String str2, int i2, int i3, boolean z) {
        if (!z) {
            return str.regionMatches(i, str2, i2, i3);
        }
        return str.regionMatches(z, i, str2, i2, i3);
    }

    public static final boolean isBlank(String str) {
        boolean z;
        if (str.length() != 0) {
            IntRange intRange = new IntRange(0, str.length() - 1);
            if (!(intRange instanceof Collection) || !((Collection) intRange).isEmpty()) {
                Iterator<Integer> it = intRange.iterator();
                while (((IntProgressionIterator) it).hasNext) {
                    if (!CharsKt__CharKt.isWhitespace(str.charAt(((IntIterator) it).nextInt()))) {
                        z = false;
                        break;
                    }
                }
            }
            z = true;
            if (!z) {
                return false;
            }
        }
        return true;
    }
}
