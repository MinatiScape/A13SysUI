package androidx.core.text;

import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import androidx.core.text.TextDirectionHeuristicsCompat;
import java.util.Locale;
/* loaded from: classes.dex */
public final class BidiFormatter {
    public static final BidiFormatter DEFAULT_LTR_INSTANCE;
    public static final BidiFormatter DEFAULT_RTL_INSTANCE;
    public static final TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal DEFAULT_TEXT_DIRECTION_HEURISTIC;
    public static final String LRM_STRING = Character.toString(8206);
    public static final String RLM_STRING = Character.toString(8207);
    public final TextDirectionHeuristicCompat mDefaultTextDirectionHeuristicCompat;
    public final int mFlags;
    public final boolean mIsRtlContext;

    /* loaded from: classes.dex */
    public static class DirectionalityEstimator {
        public static final byte[] DIR_TYPE_CACHE = new byte[1792];
        public int charIndex;
        public char lastChar;
        public final int length;
        public final CharSequence text;

        static {
            for (int i = 0; i < 1792; i++) {
                DIR_TYPE_CACHE[i] = Character.getDirectionality(i);
            }
        }

        public final byte dirTypeBackward() {
            char charAt = this.text.charAt(this.charIndex - 1);
            this.lastChar = charAt;
            if (Character.isLowSurrogate(charAt)) {
                int codePointBefore = Character.codePointBefore(this.text, this.charIndex);
                this.charIndex -= Character.charCount(codePointBefore);
                return Character.getDirectionality(codePointBefore);
            }
            this.charIndex--;
            char c = this.lastChar;
            if (c < 1792) {
                return DIR_TYPE_CACHE[c];
            }
            return Character.getDirectionality(c);
        }

        public DirectionalityEstimator(CharSequence charSequence) {
            this.text = charSequence;
            this.length = charSequence.length();
        }
    }

    static {
        TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal textDirectionHeuristicInternal = TextDirectionHeuristicsCompat.FIRSTSTRONG_LTR;
        DEFAULT_TEXT_DIRECTION_HEURISTIC = textDirectionHeuristicInternal;
        DEFAULT_LTR_INSTANCE = new BidiFormatter(false, 2, textDirectionHeuristicInternal);
        DEFAULT_RTL_INSTANCE = new BidiFormatter(true, 2, textDirectionHeuristicInternal);
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x0070, code lost:
        if (r3 != 0) goto L_0x0073;
     */
    /* JADX WARN: Code restructure failed: missing block: B:27:0x0073, code lost:
        if (r4 == 0) goto L_0x0077;
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0079, code lost:
        if (r0.charIndex <= 0) goto L_0x0091;
     */
    /* JADX WARN: Code restructure failed: missing block: B:32:0x007f, code lost:
        switch(r0.dirTypeBackward()) {
            case 14: goto L_0x008a;
            case 15: goto L_0x008a;
            case 16: goto L_0x0086;
            case 17: goto L_0x0086;
            case 18: goto L_0x0083;
            default: goto L_0x0082;
        };
     */
    /* JADX WARN: Code restructure failed: missing block: B:33:0x0083, code lost:
        r5 = r5 + 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:34:0x0086, code lost:
        if (r3 != r5) goto L_0x008e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:36:0x008a, code lost:
        if (r3 != r5) goto L_0x008e;
     */
    /* JADX WARN: Code restructure failed: missing block: B:38:0x008e, code lost:
        r5 = r5 - 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:39:0x0091, code lost:
        return r4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:68:?, code lost:
        return 1;
     */
    /* JADX WARN: Code restructure failed: missing block: B:70:?, code lost:
        return 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:71:?, code lost:
        return 0;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int getEntryDir(java.lang.CharSequence r9) {
        /*
            androidx.core.text.BidiFormatter$DirectionalityEstimator r0 = new androidx.core.text.BidiFormatter$DirectionalityEstimator
            r0.<init>(r9)
            r9 = 0
            r0.charIndex = r9
            r1 = -1
            r2 = 1
            r3 = r9
            r4 = r3
            r5 = r4
        L_0x000d:
            int r6 = r0.charIndex
            int r7 = r0.length
            if (r6 >= r7) goto L_0x0070
            if (r3 != 0) goto L_0x0070
            java.lang.CharSequence r7 = r0.text
            char r6 = r7.charAt(r6)
            r0.lastChar = r6
            boolean r6 = java.lang.Character.isHighSurrogate(r6)
            if (r6 == 0) goto L_0x0039
            java.lang.CharSequence r6 = r0.text
            int r7 = r0.charIndex
            int r6 = java.lang.Character.codePointAt(r6, r7)
            int r7 = r0.charIndex
            int r8 = java.lang.Character.charCount(r6)
            int r8 = r8 + r7
            r0.charIndex = r8
            byte r6 = java.lang.Character.getDirectionality(r6)
            goto L_0x004d
        L_0x0039:
            int r6 = r0.charIndex
            int r6 = r6 + r2
            r0.charIndex = r6
            char r6 = r0.lastChar
            r7 = 1792(0x700, float:2.511E-42)
            if (r6 >= r7) goto L_0x0049
            byte[] r7 = androidx.core.text.BidiFormatter.DirectionalityEstimator.DIR_TYPE_CACHE
            r6 = r7[r6]
            goto L_0x004d
        L_0x0049:
            byte r6 = java.lang.Character.getDirectionality(r6)
        L_0x004d:
            if (r6 == 0) goto L_0x006b
            if (r6 == r2) goto L_0x0068
            r7 = 2
            if (r6 == r7) goto L_0x0068
            r7 = 9
            if (r6 == r7) goto L_0x000d
            switch(r6) {
                case 14: goto L_0x0064;
                case 15: goto L_0x0064;
                case 16: goto L_0x0060;
                case 17: goto L_0x0060;
                case 18: goto L_0x005c;
                default: goto L_0x005b;
            }
        L_0x005b:
            goto L_0x006e
        L_0x005c:
            int r5 = r5 + (-1)
            r4 = r9
            goto L_0x000d
        L_0x0060:
            int r5 = r5 + 1
            r4 = r2
            goto L_0x000d
        L_0x0064:
            int r5 = r5 + 1
            r4 = r1
            goto L_0x000d
        L_0x0068:
            if (r5 != 0) goto L_0x006e
            goto L_0x0088
        L_0x006b:
            if (r5 != 0) goto L_0x006e
            goto L_0x008c
        L_0x006e:
            r3 = r5
            goto L_0x000d
        L_0x0070:
            if (r3 != 0) goto L_0x0073
            goto L_0x0091
        L_0x0073:
            if (r4 == 0) goto L_0x0077
            r9 = r4
            goto L_0x0091
        L_0x0077:
            int r4 = r0.charIndex
            if (r4 <= 0) goto L_0x0091
            byte r4 = r0.dirTypeBackward()
            switch(r4) {
                case 14: goto L_0x008a;
                case 15: goto L_0x008a;
                case 16: goto L_0x0086;
                case 17: goto L_0x0086;
                case 18: goto L_0x0083;
                default: goto L_0x0082;
            }
        L_0x0082:
            goto L_0x0077
        L_0x0083:
            int r5 = r5 + 1
            goto L_0x0077
        L_0x0086:
            if (r3 != r5) goto L_0x008e
        L_0x0088:
            r9 = r2
            goto L_0x0091
        L_0x008a:
            if (r3 != r5) goto L_0x008e
        L_0x008c:
            r9 = r1
            goto L_0x0091
        L_0x008e:
            int r5 = r5 + (-1)
            goto L_0x0077
        L_0x0091:
            return r9
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.text.BidiFormatter.getEntryDir(java.lang.CharSequence):int");
    }

    /* JADX WARN: Code restructure failed: missing block: B:26:0x0041, code lost:
        return 1;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public static int getExitDir(java.lang.CharSequence r6) {
        /*
            androidx.core.text.BidiFormatter$DirectionalityEstimator r0 = new androidx.core.text.BidiFormatter$DirectionalityEstimator
            r0.<init>(r6)
            int r6 = r0.length
            r0.charIndex = r6
            r6 = 0
            r1 = r6
        L_0x000b:
            r2 = r1
        L_0x000c:
            int r3 = r0.charIndex
            r4 = 1
            if (r3 <= 0) goto L_0x0041
            byte r3 = r0.dirTypeBackward()
            if (r3 == 0) goto L_0x0039
            if (r3 == r4) goto L_0x0032
            r5 = 2
            if (r3 == r5) goto L_0x0032
            r5 = 9
            if (r3 == r5) goto L_0x000c
            switch(r3) {
                case 14: goto L_0x002c;
                case 15: goto L_0x002c;
                case 16: goto L_0x0029;
                case 17: goto L_0x0029;
                case 18: goto L_0x0026;
                default: goto L_0x0023;
            }
        L_0x0023:
            if (r1 != 0) goto L_0x000c
            goto L_0x003f
        L_0x0026:
            int r2 = r2 + 1
            goto L_0x000c
        L_0x0029:
            if (r1 != r2) goto L_0x002f
            goto L_0x0034
        L_0x002c:
            if (r1 != r2) goto L_0x002f
            goto L_0x003b
        L_0x002f:
            int r2 = r2 + (-1)
            goto L_0x000c
        L_0x0032:
            if (r2 != 0) goto L_0x0036
        L_0x0034:
            r6 = r4
            goto L_0x0041
        L_0x0036:
            if (r1 != 0) goto L_0x000c
            goto L_0x003f
        L_0x0039:
            if (r2 != 0) goto L_0x003d
        L_0x003b:
            r6 = -1
            goto L_0x0041
        L_0x003d:
            if (r1 != 0) goto L_0x000c
        L_0x003f:
            r1 = r2
            goto L_0x000b
        L_0x0041:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.text.BidiFormatter.getExitDir(java.lang.CharSequence):int");
    }

    public final String unicodeWrap(String str) {
        TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal textDirectionHeuristicInternal;
        char c;
        TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal textDirectionHeuristicInternal2;
        String str2;
        TextDirectionHeuristicCompat textDirectionHeuristicCompat = this.mDefaultTextDirectionHeuristicCompat;
        if (str == null) {
            return null;
        }
        boolean z = false;
        boolean isRtl = ((TextDirectionHeuristicsCompat.TextDirectionHeuristicImpl) textDirectionHeuristicCompat).isRtl(str, str.length());
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder();
        if ((this.mFlags & 2) != 0) {
            z = true;
        }
        String str3 = "";
        if (z) {
            if (isRtl) {
                textDirectionHeuristicInternal2 = TextDirectionHeuristicsCompat.RTL;
            } else {
                textDirectionHeuristicInternal2 = TextDirectionHeuristicsCompat.LTR;
            }
            boolean isRtl2 = textDirectionHeuristicInternal2.isRtl(str, str.length());
            if (!this.mIsRtlContext && (isRtl2 || getEntryDir(str) == 1)) {
                str2 = LRM_STRING;
            } else if (!this.mIsRtlContext || (isRtl2 && getEntryDir(str) != -1)) {
                str2 = str3;
            } else {
                str2 = RLM_STRING;
            }
            spannableStringBuilder.append((CharSequence) str2);
        }
        if (isRtl != this.mIsRtlContext) {
            if (isRtl) {
                c = 8235;
            } else {
                c = 8234;
            }
            spannableStringBuilder.append(c);
            spannableStringBuilder.append((CharSequence) str);
            spannableStringBuilder.append((char) 8236);
        } else {
            spannableStringBuilder.append((CharSequence) str);
        }
        if (isRtl) {
            textDirectionHeuristicInternal = TextDirectionHeuristicsCompat.RTL;
        } else {
            textDirectionHeuristicInternal = TextDirectionHeuristicsCompat.LTR;
        }
        boolean isRtl3 = textDirectionHeuristicInternal.isRtl(str, str.length());
        if (!this.mIsRtlContext && (isRtl3 || getExitDir(str) == 1)) {
            str3 = LRM_STRING;
        } else if (this.mIsRtlContext && (!isRtl3 || getExitDir(str) == -1)) {
            str3 = RLM_STRING;
        }
        spannableStringBuilder.append((CharSequence) str3);
        return spannableStringBuilder.toString();
    }

    public BidiFormatter(boolean z, int i, TextDirectionHeuristicsCompat.TextDirectionHeuristicInternal textDirectionHeuristicInternal) {
        this.mIsRtlContext = z;
        this.mFlags = i;
        this.mDefaultTextDirectionHeuristicCompat = textDirectionHeuristicInternal;
    }

    public static BidiFormatter getInstance() {
        Locale locale = Locale.getDefault();
        int i = TextUtilsCompat.$r8$clinit;
        boolean z = true;
        if (TextUtils.getLayoutDirectionFromLocale(locale) != 1) {
            z = false;
        }
        if (z) {
            return DEFAULT_RTL_INSTANCE;
        }
        return DEFAULT_LTR_INSTANCE;
    }
}
