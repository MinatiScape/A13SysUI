package com.airbnb.lottie.parser.moshi;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import com.airbnb.lottie.parser.moshi.JsonReader;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.io.EOFException;
import java.io.IOException;
import java.util.Objects;
import kotlin.text.Charsets;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.RealBufferedSource;
/* loaded from: classes.dex */
public final class JsonUtf8Reader extends JsonReader {
    public final Buffer buffer;
    public int peeked = 0;
    public long peekedLong;
    public int peekedNumberLength;
    public String peekedString;
    public final BufferedSource source;
    public static final ByteString SINGLE_QUOTE_OR_SLASH = ByteString.encodeUtf8("'\\");
    public static final ByteString DOUBLE_QUOTE_OR_SLASH = ByteString.encodeUtf8("\"\\");
    public static final ByteString UNQUOTED_STRING_TERMINALS = ByteString.encodeUtf8("{}[]:, \n\t\r\f/\\;#=");
    public static final ByteString LINEFEED_OR_CARRIAGE_RETURN = ByteString.encodeUtf8("\n\r");

    @Override // java.io.Closeable, java.lang.AutoCloseable
    public final void close() throws IOException {
        this.peeked = 0;
        this.scopes[0] = 8;
        this.stackSize = 1;
        Buffer buffer = this.buffer;
        Objects.requireNonNull(buffer);
        buffer.skip(buffer.size);
        this.source.close();
    }

    public final int nextNonWhitespace(boolean z) throws IOException {
        int i = 0;
        while (true) {
            int i2 = i + 1;
            if (this.source.request(i2)) {
                byte b = this.buffer.getByte(i);
                if (b == 10 || b == 32 || b == 13 || b == 9) {
                    i = i2;
                } else {
                    this.buffer.skip(i2 - 1);
                    if (b == 47) {
                        if (!this.source.request(2L)) {
                            return b;
                        }
                        checkLenient();
                        throw null;
                    } else if (b != 35) {
                        return b;
                    } else {
                        checkLenient();
                        throw null;
                    }
                }
            } else if (!z) {
                return -1;
            } else {
                throw new EOFException("End of input");
            }
        }
    }

    public final String nextQuotedValue(ByteString byteString) throws IOException {
        StringBuilder sb = null;
        while (true) {
            long indexOfElement = this.source.indexOfElement(byteString);
            if (indexOfElement == -1) {
                syntaxError("Unterminated string");
                throw null;
            } else if (this.buffer.getByte(indexOfElement) == 92) {
                if (sb == null) {
                    sb = new StringBuilder();
                }
                sb.append(this.buffer.readUtf8(indexOfElement));
                this.buffer.readByte();
                sb.append(readEscapeCharacter());
            } else if (sb == null) {
                String readUtf8 = this.buffer.readUtf8(indexOfElement);
                this.buffer.readByte();
                return readUtf8;
            } else {
                sb.append(this.buffer.readUtf8(indexOfElement));
                this.buffer.readByte();
                return sb.toString();
            }
        }
    }

    @Override // com.airbnb.lottie.parser.moshi.JsonReader
    public final void skipValue() throws IOException {
        int i = 0;
        do {
            int i2 = this.peeked;
            if (i2 == 0) {
                i2 = doPeek();
            }
            if (i2 == 3) {
                pushScope(1);
            } else if (i2 == 1) {
                pushScope(3);
            } else {
                if (i2 == 4) {
                    i--;
                    if (i >= 0) {
                        this.stackSize--;
                    } else {
                        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected a value but was ");
                        m.append(peek());
                        m.append(" at path ");
                        m.append(getPath());
                        throw new JsonDataException(m.toString());
                    }
                } else if (i2 == 2) {
                    i--;
                    if (i >= 0) {
                        this.stackSize--;
                    } else {
                        StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected a value but was ");
                        m2.append(peek());
                        m2.append(" at path ");
                        m2.append(getPath());
                        throw new JsonDataException(m2.toString());
                    }
                } else if (i2 == 14 || i2 == 10) {
                    long indexOfElement = this.source.indexOfElement(UNQUOTED_STRING_TERMINALS);
                    Buffer buffer = this.buffer;
                    if (indexOfElement == -1) {
                        Objects.requireNonNull(buffer);
                        indexOfElement = buffer.size;
                    }
                    buffer.skip(indexOfElement);
                } else if (i2 == 9 || i2 == 13) {
                    skipQuotedValue(DOUBLE_QUOTE_OR_SLASH);
                } else if (i2 == 8 || i2 == 12) {
                    skipQuotedValue(SINGLE_QUOTE_OR_SLASH);
                } else if (i2 == 17) {
                    this.buffer.skip(this.peekedNumberLength);
                } else if (i2 == 18) {
                    StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected a value but was ");
                    m3.append(peek());
                    m3.append(" at path ");
                    m3.append(getPath());
                    throw new JsonDataException(m3.toString());
                }
                this.peeked = 0;
            }
            i++;
            this.peeked = 0;
        } while (i != 0);
        int[] iArr = this.pathIndices;
        int i3 = this.stackSize;
        int i4 = i3 - 1;
        iArr[i4] = iArr[i4] + 1;
        this.pathNames[i3 - 1] = "null";
    }

    static {
        ByteString.encodeUtf8("*/");
    }

    @Override // com.airbnb.lottie.parser.moshi.JsonReader
    public final void beginArray() throws IOException {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 3) {
            pushScope(1);
            this.pathIndices[this.stackSize - 1] = 0;
            this.peeked = 0;
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected BEGIN_ARRAY but was ");
        m.append(peek());
        m.append(" at path ");
        m.append(getPath());
        throw new JsonDataException(m.toString());
    }

    @Override // com.airbnb.lottie.parser.moshi.JsonReader
    public final void beginObject() throws IOException {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 1) {
            pushScope(3);
            this.peeked = 0;
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected BEGIN_OBJECT but was ");
        m.append(peek());
        m.append(" at path ");
        m.append(getPath());
        throw new JsonDataException(m.toString());
    }

    public final void checkLenient() throws IOException {
        syntaxError("Use JsonReader.setLenient(true) to accept malformed JSON");
        throw null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:124:0x01ac, code lost:
        if (isLiteral(r2) != false) goto L_0x020d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:125:0x01ae, code lost:
        r2 = 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:126:0x01af, code lost:
        if (r5 != r2) goto L_0x01d5;
     */
    /* JADX WARN: Code restructure failed: missing block: B:127:0x01b1, code lost:
        if (r6 == false) goto L_0x01d4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:129:0x01b7, code lost:
        if (r7 != Long.MIN_VALUE) goto L_0x01bb;
     */
    /* JADX WARN: Code restructure failed: missing block: B:130:0x01b9, code lost:
        if (r9 == false) goto L_0x01d4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:132:0x01bf, code lost:
        if (r7 != 0) goto L_0x01c3;
     */
    /* JADX WARN: Code restructure failed: missing block: B:133:0x01c1, code lost:
        if (r9 != false) goto L_0x01d4;
     */
    /* JADX WARN: Code restructure failed: missing block: B:134:0x01c3, code lost:
        if (r9 == false) goto L_0x01c6;
     */
    /* JADX WARN: Code restructure failed: missing block: B:135:0x01c6, code lost:
        r7 = -r7;
     */
    /* JADX WARN: Code restructure failed: missing block: B:136:0x01c7, code lost:
        r17.peekedLong = r7;
        r17.buffer.skip(r1);
        r14 = 16;
        r17.peeked = 16;
     */
    /* JADX WARN: Code restructure failed: missing block: B:137:0x01d4, code lost:
        r2 = 2;
     */
    /* JADX WARN: Code restructure failed: missing block: B:138:0x01d5, code lost:
        if (r5 == r2) goto L_0x01dc;
     */
    /* JADX WARN: Code restructure failed: missing block: B:139:0x01d7, code lost:
        if (r5 == 4) goto L_0x01dc;
     */
    /* JADX WARN: Code restructure failed: missing block: B:141:0x01da, code lost:
        if (r5 != 7) goto L_0x020d;
     */
    /* JADX WARN: Code restructure failed: missing block: B:142:0x01dc, code lost:
        r17.peekedNumberLength = r1;
        r14 = 17;
        r17.peeked = 17;
     */
    /* JADX WARN: Removed duplicated region for block: B:162:0x0210 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:163:0x0211  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x0125 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:82:0x0126  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int doPeek() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 708
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.parser.moshi.JsonUtf8Reader.doPeek():int");
    }

    @Override // com.airbnb.lottie.parser.moshi.JsonReader
    public final void endArray() throws IOException {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 4) {
            int i2 = this.stackSize - 1;
            this.stackSize = i2;
            int[] iArr = this.pathIndices;
            int i3 = i2 - 1;
            iArr[i3] = iArr[i3] + 1;
            this.peeked = 0;
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected END_ARRAY but was ");
        m.append(peek());
        m.append(" at path ");
        m.append(getPath());
        throw new JsonDataException(m.toString());
    }

    @Override // com.airbnb.lottie.parser.moshi.JsonReader
    public final void endObject() throws IOException {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 2) {
            int i2 = this.stackSize - 1;
            this.stackSize = i2;
            this.pathNames[i2] = null;
            int[] iArr = this.pathIndices;
            int i3 = i2 - 1;
            iArr[i3] = iArr[i3] + 1;
            this.peeked = 0;
            return;
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected END_OBJECT but was ");
        m.append(peek());
        m.append(" at path ");
        m.append(getPath());
        throw new JsonDataException(m.toString());
    }

    public final int findName(String str, JsonReader.Options options) {
        int length = options.strings.length;
        for (int i = 0; i < length; i++) {
            if (str.equals(options.strings[i])) {
                this.peeked = 0;
                this.pathNames[this.stackSize - 1] = str;
                return i;
            }
        }
        return -1;
    }

    @Override // com.airbnb.lottie.parser.moshi.JsonReader
    public final boolean hasNext() throws IOException {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 2 || i == 4 || i == 18) {
            return false;
        }
        return true;
    }

    public final boolean isLiteral(int i) throws IOException {
        if (i == 9 || i == 10 || i == 12 || i == 13 || i == 32) {
            return false;
        }
        if (i != 35) {
            if (i == 44) {
                return false;
            }
            if (!(i == 47 || i == 61)) {
                if (i == 123 || i == 125 || i == 58) {
                    return false;
                }
                if (i != 59) {
                    switch (i) {
                        case 91:
                        case 93:
                            return false;
                        case 92:
                            break;
                        default:
                            return true;
                    }
                }
            }
        }
        checkLenient();
        throw null;
    }

    @Override // com.airbnb.lottie.parser.moshi.JsonReader
    public final boolean nextBoolean() throws IOException {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 5) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            return true;
        } else if (i == 6) {
            this.peeked = 0;
            int[] iArr2 = this.pathIndices;
            int i3 = this.stackSize - 1;
            iArr2[i3] = iArr2[i3] + 1;
            return false;
        } else {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected a boolean but was ");
            m.append(peek());
            m.append(" at path ");
            m.append(getPath());
            throw new JsonDataException(m.toString());
        }
    }

    @Override // com.airbnb.lottie.parser.moshi.JsonReader
    public final double nextDouble() throws IOException {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 16) {
            this.peeked = 0;
            int[] iArr = this.pathIndices;
            int i2 = this.stackSize - 1;
            iArr[i2] = iArr[i2] + 1;
            return this.peekedLong;
        }
        if (i == 17) {
            this.peekedString = this.buffer.readUtf8(this.peekedNumberLength);
        } else if (i == 9) {
            this.peekedString = nextQuotedValue(DOUBLE_QUOTE_OR_SLASH);
        } else if (i == 8) {
            this.peekedString = nextQuotedValue(SINGLE_QUOTE_OR_SLASH);
        } else if (i == 10) {
            this.peekedString = nextUnquotedValue();
        } else if (i != 11) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected a double but was ");
            m.append(peek());
            m.append(" at path ");
            m.append(getPath());
            throw new JsonDataException(m.toString());
        }
        this.peeked = 11;
        try {
            double parseDouble = Double.parseDouble(this.peekedString);
            if (Double.isNaN(parseDouble) || Double.isInfinite(parseDouble)) {
                throw new JsonEncodingException("JSON forbids NaN and infinities: " + parseDouble + " at path " + getPath());
            }
            this.peekedString = null;
            this.peeked = 0;
            int[] iArr2 = this.pathIndices;
            int i3 = this.stackSize - 1;
            iArr2[i3] = iArr2[i3] + 1;
            return parseDouble;
        } catch (NumberFormatException unused) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected a double but was ");
            m2.append(this.peekedString);
            m2.append(" at path ");
            m2.append(getPath());
            throw new JsonDataException(m2.toString());
        }
    }

    @Override // com.airbnb.lottie.parser.moshi.JsonReader
    public final int nextInt() throws IOException {
        String str;
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 16) {
            long j = this.peekedLong;
            int i2 = (int) j;
            if (j == i2) {
                this.peeked = 0;
                int[] iArr = this.pathIndices;
                int i3 = this.stackSize - 1;
                iArr[i3] = iArr[i3] + 1;
                return i2;
            }
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected an int but was ");
            m.append(this.peekedLong);
            m.append(" at path ");
            m.append(getPath());
            throw new JsonDataException(m.toString());
        }
        if (i == 17) {
            this.peekedString = this.buffer.readUtf8(this.peekedNumberLength);
        } else if (i == 9 || i == 8) {
            if (i == 9) {
                str = nextQuotedValue(DOUBLE_QUOTE_OR_SLASH);
            } else {
                str = nextQuotedValue(SINGLE_QUOTE_OR_SLASH);
            }
            this.peekedString = str;
            try {
                int parseInt = Integer.parseInt(str);
                this.peeked = 0;
                int[] iArr2 = this.pathIndices;
                int i4 = this.stackSize - 1;
                iArr2[i4] = iArr2[i4] + 1;
                return parseInt;
            } catch (NumberFormatException unused) {
            }
        } else if (i != 11) {
            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected an int but was ");
            m2.append(peek());
            m2.append(" at path ");
            m2.append(getPath());
            throw new JsonDataException(m2.toString());
        }
        this.peeked = 11;
        try {
            double parseDouble = Double.parseDouble(this.peekedString);
            int i5 = (int) parseDouble;
            if (i5 == parseDouble) {
                this.peekedString = null;
                this.peeked = 0;
                int[] iArr3 = this.pathIndices;
                int i6 = this.stackSize - 1;
                iArr3[i6] = iArr3[i6] + 1;
                return i5;
            }
            StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected an int but was ");
            m3.append(this.peekedString);
            m3.append(" at path ");
            m3.append(getPath());
            throw new JsonDataException(m3.toString());
        } catch (NumberFormatException unused2) {
            StringBuilder m4 = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected an int but was ");
            m4.append(this.peekedString);
            m4.append(" at path ");
            m4.append(getPath());
            throw new JsonDataException(m4.toString());
        }
    }

    public final String nextName() throws IOException {
        String str;
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 14) {
            str = nextUnquotedValue();
        } else if (i == 13) {
            str = nextQuotedValue(DOUBLE_QUOTE_OR_SLASH);
        } else if (i == 12) {
            str = nextQuotedValue(SINGLE_QUOTE_OR_SLASH);
        } else if (i == 15) {
            str = this.peekedString;
        } else {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected a name but was ");
            m.append(peek());
            m.append(" at path ");
            m.append(getPath());
            throw new JsonDataException(m.toString());
        }
        this.peeked = 0;
        this.pathNames[this.stackSize - 1] = str;
        return str;
    }

    @Override // com.airbnb.lottie.parser.moshi.JsonReader
    public final String nextString() throws IOException {
        String str;
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 10) {
            str = nextUnquotedValue();
        } else if (i == 9) {
            str = nextQuotedValue(DOUBLE_QUOTE_OR_SLASH);
        } else if (i == 8) {
            str = nextQuotedValue(SINGLE_QUOTE_OR_SLASH);
        } else if (i == 11) {
            str = this.peekedString;
            this.peekedString = null;
        } else if (i == 16) {
            str = Long.toString(this.peekedLong);
        } else if (i == 17) {
            str = this.buffer.readUtf8(this.peekedNumberLength);
        } else {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected a string but was ");
            m.append(peek());
            m.append(" at path ");
            m.append(getPath());
            throw new JsonDataException(m.toString());
        }
        this.peeked = 0;
        int[] iArr = this.pathIndices;
        int i2 = this.stackSize - 1;
        iArr[i2] = iArr[i2] + 1;
        return str;
    }

    public final String nextUnquotedValue() throws IOException {
        long indexOfElement = this.source.indexOfElement(UNQUOTED_STRING_TERMINALS);
        int i = (indexOfElement > (-1L) ? 1 : (indexOfElement == (-1L) ? 0 : -1));
        Buffer buffer = this.buffer;
        if (i != 0) {
            return buffer.readUtf8(indexOfElement);
        }
        Objects.requireNonNull(buffer);
        return buffer.readString(buffer.size, Charsets.UTF_8);
    }

    @Override // com.airbnb.lottie.parser.moshi.JsonReader
    public final JsonReader.Token peek() throws IOException {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        switch (i) {
            case 1:
                return JsonReader.Token.BEGIN_OBJECT;
            case 2:
                return JsonReader.Token.END_OBJECT;
            case 3:
                return JsonReader.Token.BEGIN_ARRAY;
            case 4:
                return JsonReader.Token.END_ARRAY;
            case 5:
            case FalsingManager.VERSION /* 6 */:
                return JsonReader.Token.BOOLEAN;
            case 7:
                return JsonReader.Token.NULL;
            case 8:
            case 9:
            case 10:
            case QSTileImpl.H.STALE /* 11 */:
                return JsonReader.Token.STRING;
            case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
            case QS.VERSION /* 13 */:
            case 14:
            case 15:
                return JsonReader.Token.NAME;
            case 16:
            case 17:
                return JsonReader.Token.NUMBER;
            case 18:
                return JsonReader.Token.END_DOCUMENT;
            default:
                throw new AssertionError();
        }
    }

    public final char readEscapeCharacter() throws IOException {
        int i;
        int i2;
        if (this.source.request(1L)) {
            byte readByte = this.buffer.readByte();
            if (readByte == 10 || readByte == 34 || readByte == 39 || readByte == 47 || readByte == 92) {
                return (char) readByte;
            }
            if (readByte == 98) {
                return '\b';
            }
            if (readByte == 102) {
                return '\f';
            }
            if (readByte == 110) {
                return '\n';
            }
            if (readByte == 114) {
                return '\r';
            }
            if (readByte == 116) {
                return '\t';
            }
            if (readByte != 117) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid escape sequence: \\");
                m.append((char) readByte);
                syntaxError(m.toString());
                throw null;
            } else if (this.source.request(4L)) {
                char c = 0;
                for (int i3 = 0; i3 < 4; i3++) {
                    byte b = this.buffer.getByte(i3);
                    char c2 = (char) (c << 4);
                    if (b < 48 || b > 57) {
                        if (b >= 97 && b <= 102) {
                            i2 = b - 97;
                        } else if (b < 65 || b > 70) {
                            StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("\\u");
                            m2.append(this.buffer.readUtf8(4L));
                            syntaxError(m2.toString());
                            throw null;
                        } else {
                            i2 = b - 65;
                        }
                        i = i2 + 10;
                    } else {
                        i = b - 48;
                    }
                    c = (char) (i + c2);
                }
                this.buffer.skip(4L);
                return c;
            } else {
                StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m("Unterminated escape sequence at path ");
                m3.append(getPath());
                throw new EOFException(m3.toString());
            }
        } else {
            syntaxError("Unterminated escape sequence");
            throw null;
        }
    }

    @Override // com.airbnb.lottie.parser.moshi.JsonReader
    public final int selectName(JsonReader.Options options) throws IOException {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i < 12 || i > 15) {
            return -1;
        }
        if (i == 15) {
            return findName(this.peekedString, options);
        }
        int select = this.source.select(options.doubleQuoteSuffix);
        if (select != -1) {
            this.peeked = 0;
            this.pathNames[this.stackSize - 1] = options.strings[select];
            return select;
        }
        String str = this.pathNames[this.stackSize - 1];
        String nextName = nextName();
        int findName = findName(nextName, options);
        if (findName == -1) {
            this.peeked = 15;
            this.peekedString = nextName;
            this.pathNames[this.stackSize - 1] = str;
        }
        return findName;
    }

    @Override // com.airbnb.lottie.parser.moshi.JsonReader
    public final void skipName() throws IOException {
        int i = this.peeked;
        if (i == 0) {
            i = doPeek();
        }
        if (i == 14) {
            long indexOfElement = this.source.indexOfElement(UNQUOTED_STRING_TERMINALS);
            Buffer buffer = this.buffer;
            if (indexOfElement == -1) {
                Objects.requireNonNull(buffer);
                indexOfElement = buffer.size;
            }
            buffer.skip(indexOfElement);
        } else if (i == 13) {
            skipQuotedValue(DOUBLE_QUOTE_OR_SLASH);
        } else if (i == 12) {
            skipQuotedValue(SINGLE_QUOTE_OR_SLASH);
        } else if (i != 15) {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Expected a name but was ");
            m.append(peek());
            m.append(" at path ");
            m.append(getPath());
            throw new JsonDataException(m.toString());
        }
        this.peeked = 0;
        this.pathNames[this.stackSize - 1] = "null";
    }

    public final void skipQuotedValue(ByteString byteString) throws IOException {
        while (true) {
            long indexOfElement = this.source.indexOfElement(byteString);
            if (indexOfElement == -1) {
                syntaxError("Unterminated string");
                throw null;
            } else if (this.buffer.getByte(indexOfElement) == 92) {
                this.buffer.skip(indexOfElement + 1);
                readEscapeCharacter();
            } else {
                this.buffer.skip(indexOfElement + 1);
                return;
            }
        }
    }

    public final String toString() {
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("JsonReader(");
        m.append(this.source);
        m.append(")");
        return m.toString();
    }

    public JsonUtf8Reader(RealBufferedSource realBufferedSource) {
        this.source = realBufferedSource;
        this.buffer = realBufferedSource.bufferField;
        pushScope(6);
    }
}
