package com.airbnb.lottie.parser.moshi;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.hidl.base.V1_0.DebugInfo$$ExternalSyntheticOutline0;
import java.io.Closeable;
import java.io.IOException;
import java.util.Arrays;
/* loaded from: classes.dex */
public abstract class JsonReader implements Closeable {
    public static final String[] REPLACEMENT_CHARS = new String[128];
    public int stackSize;
    public int[] scopes = new int[32];
    public String[] pathNames = new String[32];
    public int[] pathIndices = new int[32];

    /* loaded from: classes.dex */
    public enum Token {
        BEGIN_ARRAY,
        END_ARRAY,
        BEGIN_OBJECT,
        END_OBJECT,
        NAME,
        STRING,
        NUMBER,
        BOOLEAN,
        NULL,
        END_DOCUMENT
    }

    public abstract void beginArray() throws IOException;

    public abstract void beginObject() throws IOException;

    public abstract void endArray() throws IOException;

    public abstract void endObject() throws IOException;

    public abstract boolean hasNext() throws IOException;

    public abstract boolean nextBoolean() throws IOException;

    public abstract double nextDouble() throws IOException;

    public abstract int nextInt() throws IOException;

    public abstract String nextString() throws IOException;

    public abstract Token peek() throws IOException;

    public abstract int selectName(Options options) throws IOException;

    public abstract void skipName() throws IOException;

    public abstract void skipValue() throws IOException;

    /* loaded from: classes.dex */
    public static final class Options {
        public final okio.Options doubleQuoteSuffix;
        public final String[] strings;

        /* JADX WARN: Removed duplicated region for block: B:19:0x004e A[Catch: IOException -> 0x0091, TryCatch #0 {IOException -> 0x0091, blocks: (B:2:0x0000, B:3:0x000a, B:5:0x000d, B:7:0x0032, B:9:0x003a, B:19:0x004e, B:20:0x0051, B:21:0x005a, B:23:0x005f, B:24:0x0062, B:25:0x0081), top: B:30:0x0000 }] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public static com.airbnb.lottie.parser.moshi.JsonReader.Options of(java.lang.String... r15) {
            /*
                int r0 = r15.length     // Catch: IOException -> 0x0091
                okio.ByteString[] r0 = new okio.ByteString[r0]     // Catch: IOException -> 0x0091
                okio.Buffer r1 = new okio.Buffer     // Catch: IOException -> 0x0091
                r1.<init>()     // Catch: IOException -> 0x0091
                r2 = 0
                r3 = r2
            L_0x000a:
                int r4 = r15.length     // Catch: IOException -> 0x0091
                if (r3 >= r4) goto L_0x0081
                r4 = r15[r3]     // Catch: IOException -> 0x0091
                java.lang.String[] r5 = com.airbnb.lottie.parser.moshi.JsonReader.REPLACEMENT_CHARS     // Catch: IOException -> 0x0091
                r6 = 1
                okio.Segment r7 = r1.writableSegment$external__okio__android_common__okio_lib(r6)     // Catch: IOException -> 0x0091
                byte[] r8 = r7.data     // Catch: IOException -> 0x0091
                int r9 = r7.limit     // Catch: IOException -> 0x0091
                int r10 = r9 + 1
                r7.limit = r10     // Catch: IOException -> 0x0091
                r7 = 34
                byte r7 = (byte) r7     // Catch: IOException -> 0x0091
                r8[r9] = r7     // Catch: IOException -> 0x0091
                long r8 = r1.size     // Catch: IOException -> 0x0091
                r10 = 1
                long r8 = r8 + r10
                r1.size = r8     // Catch: IOException -> 0x0091
                int r8 = r4.length()     // Catch: IOException -> 0x0091
                r9 = r2
                r12 = r9
            L_0x0030:
                if (r9 >= r8) goto L_0x005d
                char r13 = r4.charAt(r9)     // Catch: IOException -> 0x0091
                r14 = 128(0x80, float:1.794E-43)
                if (r13 >= r14) goto L_0x003f
                r13 = r5[r13]     // Catch: IOException -> 0x0091
                if (r13 != 0) goto L_0x004c
                goto L_0x005a
            L_0x003f:
                r14 = 8232(0x2028, float:1.1535E-41)
                if (r13 != r14) goto L_0x0046
                java.lang.String r13 = "\\u2028"
                goto L_0x004c
            L_0x0046:
                r14 = 8233(0x2029, float:1.1537E-41)
                if (r13 != r14) goto L_0x005a
                java.lang.String r13 = "\\u2029"
            L_0x004c:
                if (r12 >= r9) goto L_0x0051
                r1.writeUtf8(r4, r12, r9)     // Catch: IOException -> 0x0091
            L_0x0051:
                int r12 = r13.length()     // Catch: IOException -> 0x0091
                r1.writeUtf8(r13, r2, r12)     // Catch: IOException -> 0x0091
                int r12 = r9 + 1
            L_0x005a:
                int r9 = r9 + 1
                goto L_0x0030
            L_0x005d:
                if (r12 >= r8) goto L_0x0062
                r1.writeUtf8(r4, r12, r8)     // Catch: IOException -> 0x0091
            L_0x0062:
                okio.Segment r4 = r1.writableSegment$external__okio__android_common__okio_lib(r6)     // Catch: IOException -> 0x0091
                byte[] r5 = r4.data     // Catch: IOException -> 0x0091
                int r6 = r4.limit     // Catch: IOException -> 0x0091
                int r8 = r6 + 1
                r4.limit = r8     // Catch: IOException -> 0x0091
                r5[r6] = r7     // Catch: IOException -> 0x0091
                long r4 = r1.size     // Catch: IOException -> 0x0091
                long r4 = r4 + r10
                r1.size = r4     // Catch: IOException -> 0x0091
                r1.readByte()     // Catch: IOException -> 0x0091
                okio.ByteString r4 = r1.readByteString()     // Catch: IOException -> 0x0091
                r0[r3] = r4     // Catch: IOException -> 0x0091
                int r3 = r3 + 1
                goto L_0x000a
            L_0x0081:
                com.airbnb.lottie.parser.moshi.JsonReader$Options r1 = new com.airbnb.lottie.parser.moshi.JsonReader$Options     // Catch: IOException -> 0x0091
                java.lang.Object r15 = r15.clone()     // Catch: IOException -> 0x0091
                java.lang.String[] r15 = (java.lang.String[]) r15     // Catch: IOException -> 0x0091
                okio.Options r0 = okio.Options.of(r0)     // Catch: IOException -> 0x0091
                r1.<init>(r15, r0)     // Catch: IOException -> 0x0091
                return r1
            L_0x0091:
                r15 = move-exception
                java.lang.AssertionError r0 = new java.lang.AssertionError
                r0.<init>(r15)
                throw r0
            */
            throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.moshi.JsonReader.Options.of(java.lang.String[]):com.airbnb.lottie.parser.moshi.JsonReader$Options");
        }

        public Options(String[] strArr, okio.Options options) {
            this.strings = strArr;
            this.doubleQuoteSuffix = options;
        }
    }

    static {
        for (int i = 0; i <= 31; i++) {
            REPLACEMENT_CHARS[i] = String.format("\\u%04x", Integer.valueOf(i));
        }
        String[] strArr = REPLACEMENT_CHARS;
        strArr[34] = "\\\"";
        strArr[92] = "\\\\";
        strArr[9] = "\\t";
        strArr[8] = "\\b";
        strArr[10] = "\\n";
        strArr[13] = "\\r";
        strArr[12] = "\\f";
    }

    public final String getPath() {
        int i = this.stackSize;
        int[] iArr = this.scopes;
        String[] strArr = this.pathNames;
        int[] iArr2 = this.pathIndices;
        StringBuilder m = JsonReader$$ExternalSyntheticOutline0.m('$');
        for (int i2 = 0; i2 < i; i2++) {
            int i3 = iArr[i2];
            if (i3 == 1 || i3 == 2) {
                m.append('[');
                m.append(iArr2[i2]);
                m.append(']');
            } else if (i3 == 3 || i3 == 4 || i3 == 5) {
                m.append('.');
                if (strArr[i2] != null) {
                    m.append(strArr[i2]);
                }
            }
        }
        return m.toString();
    }

    public final void pushScope(int i) {
        int i2 = this.stackSize;
        int[] iArr = this.scopes;
        if (i2 == iArr.length) {
            if (i2 != 256) {
                this.scopes = Arrays.copyOf(iArr, iArr.length * 2);
                String[] strArr = this.pathNames;
                this.pathNames = (String[]) Arrays.copyOf(strArr, strArr.length * 2);
                int[] iArr2 = this.pathIndices;
                this.pathIndices = Arrays.copyOf(iArr2, iArr2.length * 2);
            } else {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Nesting too deep at ");
                m.append(getPath());
                throw new JsonDataException(m.toString());
            }
        }
        int[] iArr3 = this.scopes;
        int i3 = this.stackSize;
        this.stackSize = i3 + 1;
        iArr3[i3] = i;
    }

    public final JsonEncodingException syntaxError(String str) throws JsonEncodingException {
        StringBuilder m = DebugInfo$$ExternalSyntheticOutline0.m(str, " at path ");
        m.append(getPath());
        throw new JsonEncodingException(m.toString());
    }
}
