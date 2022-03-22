package kotlin.io;
/* compiled from: Exceptions.kt */
/* loaded from: classes.dex */
public final class ExceptionsKt {
    public static String toLowerCase(String str) {
        boolean z;
        boolean z2;
        int length = str.length();
        int i = 0;
        while (i < length) {
            char charAt = str.charAt(i);
            if (charAt < 'A' || charAt > 'Z') {
                z = false;
            } else {
                z = true;
            }
            if (z) {
                char[] charArray = str.toCharArray();
                while (i < length) {
                    char c = charArray[i];
                    if (c < 'A' || c > 'Z') {
                        z2 = false;
                    } else {
                        z2 = true;
                    }
                    if (z2) {
                        charArray[i] = (char) (c ^ ' ');
                    }
                    i++;
                }
                return String.valueOf(charArray);
            }
            i++;
        }
        return str;
    }
}
