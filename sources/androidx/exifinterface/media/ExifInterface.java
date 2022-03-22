package androidx.exifinterface.media;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline1;
import android.media.MediaDataSource;
import android.media.MediaMetadataRetriever;
import android.system.Os;
import android.system.OsConstants;
import android.util.Log;
import android.util.Pair;
import androidx.activity.result.ActivityResultRegistry$3$$ExternalSyntheticOutline0;
import com.android.systemui.keyguard.KeyguardSliceProvider;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.CRC32;
/* loaded from: classes.dex */
public final class ExifInterface {
    public static final Charset ASCII;
    public static final Pattern DATETIME_PRIMARY_FORMAT_PATTERN;
    public static final Pattern DATETIME_SECONDARY_FORMAT_PATTERN;
    public static final ExifTag[][] EXIF_TAGS;
    public static final Pattern GPS_TIMESTAMP_PATTERN;
    public static final byte[] IDENTIFIER_EXIF_APP1;
    public static final byte[] IDENTIFIER_XMP_APP1;
    public boolean mAreThumbnailStripsConsecutive;
    public final HashMap<String, ExifAttribute>[] mAttributes;
    public HashSet mAttributesOffsets;
    public ByteOrder mExifByteOrder = ByteOrder.BIG_ENDIAN;
    public boolean mHasThumbnail;
    public boolean mHasThumbnailStrips;
    public int mMimeType;
    public int mOffsetToExifData;
    public int mOrfMakerNoteOffset;
    public int mOrfThumbnailLength;
    public int mOrfThumbnailOffset;
    public FileDescriptor mSeekableFileDescriptor;
    public byte[] mThumbnailBytes;
    public int mThumbnailCompression;
    public int mThumbnailLength;
    public int mThumbnailOffset;
    public boolean mXmpIsFromSeparateMarker;
    public static final boolean DEBUG = Log.isLoggable("ExifInterface", 3);
    public static final List<Integer> ROTATION_ORDER = Arrays.asList(1, 6, 3, 8);
    public static final List<Integer> FLIPPED_ROTATION_ORDER = Arrays.asList(2, 7, 4, 5);
    public static final int[] BITS_PER_SAMPLE_RGB = {8, 8, 8};
    public static final int[] BITS_PER_SAMPLE_GREYSCALE_2 = {8};
    public static final byte[] JPEG_SIGNATURE = {-1, -40, -1};
    public static final byte[] HEIF_TYPE_FTYP = {102, 116, 121, 112};
    public static final byte[] HEIF_BRAND_MIF1 = {109, 105, 102, 49};
    public static final byte[] HEIF_BRAND_HEIC = {104, 101, 105, 99};
    public static final byte[] ORF_MAKER_NOTE_HEADER_1 = {79, 76, 89, 77, 80, 0};
    public static final byte[] ORF_MAKER_NOTE_HEADER_2 = {79, 76, 89, 77, 80, 85, 83, 0, 73, 73};
    public static final byte[] PNG_SIGNATURE = {-119, 80, 78, 71, 13, 10, 26, 10};
    public static final byte[] PNG_CHUNK_TYPE_EXIF = {101, 88, 73, 102};
    public static final byte[] PNG_CHUNK_TYPE_IHDR = {73, 72, 68, 82};
    public static final byte[] PNG_CHUNK_TYPE_IEND = {73, 69, 78, 68};
    public static final byte[] WEBP_SIGNATURE_1 = {82, 73, 70, 70};
    public static final byte[] WEBP_SIGNATURE_2 = {87, 69, 66, 80};
    public static final byte[] WEBP_CHUNK_TYPE_EXIF = {69, 88, 73, 70};
    public static final byte[] WEBP_VP8_SIGNATURE = {-99, 1, 42};
    public static final byte[] WEBP_CHUNK_TYPE_VP8X = "VP8X".getBytes(Charset.defaultCharset());
    public static final byte[] WEBP_CHUNK_TYPE_VP8L = "VP8L".getBytes(Charset.defaultCharset());
    public static final byte[] WEBP_CHUNK_TYPE_VP8 = "VP8 ".getBytes(Charset.defaultCharset());
    public static final byte[] WEBP_CHUNK_TYPE_ANIM = "ANIM".getBytes(Charset.defaultCharset());
    public static final byte[] WEBP_CHUNK_TYPE_ANMF = "ANMF".getBytes(Charset.defaultCharset());
    public static final String[] IFD_FORMAT_NAMES = {"", "BYTE", "STRING", "USHORT", "ULONG", "URATIONAL", "SBYTE", "UNDEFINED", "SSHORT", "SLONG", "SRATIONAL", "SINGLE", "DOUBLE", "IFD"};
    public static final int[] IFD_FORMAT_BYTES_PER_FORMAT = {0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8, 1};
    public static final byte[] EXIF_ASCII_PREFIX = {65, 83, 67, 73, 73, 0, 0, 0};
    public static final ExifTag TAG_RAF_IMAGE_SIZE = new ExifTag("StripOffsets", 273, 3);
    public static final ExifTag[] EXIF_POINTER_TAGS = {new ExifTag("SubIFDPointer", 330, 4), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifTag("CameraSettingsIFDPointer", 8224, 1), new ExifTag("ImageProcessingIFDPointer", 8256, 1)};
    public static final HashMap<Integer, ExifTag>[] sExifTagMapsForReading = new HashMap[10];
    public static final HashMap<String, ExifTag>[] sExifTagMapsForWriting = new HashMap[10];
    public static final HashSet<String> sTagSetForCompatibility = new HashSet<>(Arrays.asList("FNumber", "DigitalZoomRatio", "ExposureTime", "SubjectDistance", "GPSTimeStamp"));
    public static final HashMap<Integer, Integer> sExifPointerTagMap = new HashMap<>();

    /* loaded from: classes.dex */
    public static class ByteOrderedDataInputStream extends InputStream implements DataInput {
        public ByteOrder mByteOrder;
        public final DataInputStream mDataInputStream;
        public int mPosition;
        public byte[] mSkipBuffer;
        public static final ByteOrder LITTLE_ENDIAN = ByteOrder.LITTLE_ENDIAN;
        public static final ByteOrder BIG_ENDIAN = ByteOrder.BIG_ENDIAN;

        public ByteOrderedDataInputStream(byte[] bArr) throws IOException {
            this(new ByteArrayInputStream(bArr), ByteOrder.BIG_ENDIAN);
        }

        @Override // java.io.InputStream
        public final int read() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.read();
        }

        @Override // java.io.DataInput
        public final void readFully(byte[] bArr, int i, int i2) throws IOException {
            this.mPosition += i2;
            this.mDataInputStream.readFully(bArr, i, i2);
        }

        public final void skipFully(int i) throws IOException {
            int i2 = 0;
            while (i2 < i) {
                int i3 = i - i2;
                int skip = (int) this.mDataInputStream.skip(i3);
                if (skip <= 0) {
                    if (this.mSkipBuffer == null) {
                        this.mSkipBuffer = new byte[8192];
                    }
                    skip = this.mDataInputStream.read(this.mSkipBuffer, 0, Math.min(8192, i3));
                    if (skip == -1) {
                        throw new EOFException(ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Reached EOF while skipping ", i, " bytes."));
                    }
                }
                i2 += skip;
            }
            this.mPosition += i2;
        }

        public ByteOrderedDataInputStream(BufferedInputStream bufferedInputStream) throws IOException {
            this(bufferedInputStream, ByteOrder.BIG_ENDIAN);
        }

        @Override // java.io.InputStream
        public final int available() throws IOException {
            return this.mDataInputStream.available();
        }

        @Override // java.io.InputStream
        public final void mark(int i) {
            throw new UnsupportedOperationException("Mark is currently unsupported");
        }

        @Override // java.io.DataInput
        public final boolean readBoolean() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readBoolean();
        }

        @Override // java.io.DataInput
        public final byte readByte() throws IOException {
            this.mPosition++;
            int read = this.mDataInputStream.read();
            if (read >= 0) {
                return (byte) read;
            }
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public final char readChar() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readChar();
        }

        @Override // java.io.DataInput
        public final int readInt() throws IOException {
            this.mPosition += 4;
            int read = this.mDataInputStream.read();
            int read2 = this.mDataInputStream.read();
            int read3 = this.mDataInputStream.read();
            int read4 = this.mDataInputStream.read();
            if ((read | read2 | read3 | read4) >= 0) {
                ByteOrder byteOrder = this.mByteOrder;
                if (byteOrder == LITTLE_ENDIAN) {
                    return (read4 << 24) + (read3 << 16) + (read2 << 8) + read;
                }
                if (byteOrder == BIG_ENDIAN) {
                    return (read << 24) + (read2 << 16) + (read3 << 8) + read4;
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid byte order: ");
                m.append(this.mByteOrder);
                throw new IOException(m.toString());
            }
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public final String readLine() throws IOException {
            Log.d("ExifInterface", "Currently unsupported");
            return null;
        }

        @Override // java.io.DataInput
        public final long readLong() throws IOException {
            this.mPosition += 8;
            int read = this.mDataInputStream.read();
            int read2 = this.mDataInputStream.read();
            int read3 = this.mDataInputStream.read();
            int read4 = this.mDataInputStream.read();
            int read5 = this.mDataInputStream.read();
            int read6 = this.mDataInputStream.read();
            int read7 = this.mDataInputStream.read();
            int read8 = this.mDataInputStream.read();
            if ((read | read2 | read3 | read4 | read5 | read6 | read7 | read8) >= 0) {
                ByteOrder byteOrder = this.mByteOrder;
                if (byteOrder == LITTLE_ENDIAN) {
                    return (read8 << 56) + (read7 << 48) + (read6 << 40) + (read5 << 32) + (read4 << 24) + (read3 << 16) + (read2 << 8) + read;
                }
                if (byteOrder == BIG_ENDIAN) {
                    return (read << 56) + (read2 << 48) + (read3 << 40) + (read4 << 32) + (read5 << 24) + (read6 << 16) + (read7 << 8) + read8;
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid byte order: ");
                m.append(this.mByteOrder);
                throw new IOException(m.toString());
            }
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public final short readShort() throws IOException {
            int i;
            this.mPosition += 2;
            int read = this.mDataInputStream.read();
            int read2 = this.mDataInputStream.read();
            if ((read | read2) >= 0) {
                ByteOrder byteOrder = this.mByteOrder;
                if (byteOrder == LITTLE_ENDIAN) {
                    i = (read2 << 8) + read;
                } else if (byteOrder == BIG_ENDIAN) {
                    i = (read << 8) + read2;
                } else {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid byte order: ");
                    m.append(this.mByteOrder);
                    throw new IOException(m.toString());
                }
                return (short) i;
            }
            throw new EOFException();
        }

        @Override // java.io.DataInput
        public final String readUTF() throws IOException {
            this.mPosition += 2;
            return this.mDataInputStream.readUTF();
        }

        @Override // java.io.DataInput
        public final int readUnsignedByte() throws IOException {
            this.mPosition++;
            return this.mDataInputStream.readUnsignedByte();
        }

        @Override // java.io.DataInput
        public final int readUnsignedShort() throws IOException {
            this.mPosition += 2;
            int read = this.mDataInputStream.read();
            int read2 = this.mDataInputStream.read();
            if ((read | read2) >= 0) {
                ByteOrder byteOrder = this.mByteOrder;
                if (byteOrder == LITTLE_ENDIAN) {
                    return (read2 << 8) + read;
                }
                if (byteOrder == BIG_ENDIAN) {
                    return (read << 8) + read2;
                }
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid byte order: ");
                m.append(this.mByteOrder);
                throw new IOException(m.toString());
            }
            throw new EOFException();
        }

        @Override // java.io.InputStream
        public final void reset() {
            throw new UnsupportedOperationException("Reset is currently unsupported");
        }

        @Override // java.io.DataInput
        public final int skipBytes(int i) throws IOException {
            throw new UnsupportedOperationException("skipBytes is currently unsupported");
        }

        public ByteOrderedDataInputStream(InputStream inputStream, ByteOrder byteOrder) throws IOException {
            this.mByteOrder = ByteOrder.BIG_ENDIAN;
            DataInputStream dataInputStream = new DataInputStream(inputStream);
            this.mDataInputStream = dataInputStream;
            dataInputStream.mark(0);
            this.mPosition = 0;
            this.mByteOrder = byteOrder;
        }

        @Override // java.io.InputStream
        public final int read(byte[] bArr, int i, int i2) throws IOException {
            int read = this.mDataInputStream.read(bArr, i, i2);
            this.mPosition += read;
            return read;
        }

        @Override // java.io.DataInput
        public final double readDouble() throws IOException {
            return Double.longBitsToDouble(readLong());
        }

        @Override // java.io.DataInput
        public final float readFloat() throws IOException {
            return Float.intBitsToFloat(readInt());
        }

        @Override // java.io.DataInput
        public final void readFully(byte[] bArr) throws IOException {
            this.mPosition += bArr.length;
            this.mDataInputStream.readFully(bArr);
        }
    }

    /* loaded from: classes.dex */
    public static class ByteOrderedDataOutputStream extends FilterOutputStream {
        public ByteOrder mByteOrder;
        public final OutputStream mOutputStream;

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public final void write(byte[] bArr) throws IOException {
            this.mOutputStream.write(bArr);
        }

        @Override // java.io.FilterOutputStream, java.io.OutputStream
        public final void write(byte[] bArr, int i, int i2) throws IOException {
            this.mOutputStream.write(bArr, i, i2);
        }

        public final void writeByte(int i) throws IOException {
            this.mOutputStream.write(i);
        }

        public final void writeInt(int i) throws IOException {
            ByteOrder byteOrder = this.mByteOrder;
            if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write((i >>> 0) & 255);
                this.mOutputStream.write((i >>> 8) & 255);
                this.mOutputStream.write((i >>> 16) & 255);
                this.mOutputStream.write((i >>> 24) & 255);
            } else if (byteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write((i >>> 24) & 255);
                this.mOutputStream.write((i >>> 16) & 255);
                this.mOutputStream.write((i >>> 8) & 255);
                this.mOutputStream.write((i >>> 0) & 255);
            }
        }

        public final void writeShort(short s) throws IOException {
            ByteOrder byteOrder = this.mByteOrder;
            if (byteOrder == ByteOrder.LITTLE_ENDIAN) {
                this.mOutputStream.write((s >>> 0) & 255);
                this.mOutputStream.write((s >>> 8) & 255);
            } else if (byteOrder == ByteOrder.BIG_ENDIAN) {
                this.mOutputStream.write((s >>> 8) & 255);
                this.mOutputStream.write((s >>> 0) & 255);
            }
        }

        public ByteOrderedDataOutputStream(OutputStream outputStream, ByteOrder byteOrder) {
            super(outputStream);
            this.mOutputStream = outputStream;
            this.mByteOrder = byteOrder;
        }
    }

    /* loaded from: classes.dex */
    public static class ExifAttribute {
        public final byte[] bytes;
        public final long bytesOffset;
        public final int format;
        public final int numberOfComponents;

        public ExifAttribute(int i, int i2, byte[] bArr) {
            this(i, i2, -1L, bArr);
        }

        public static ExifAttribute createULong(long[] jArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[4] * jArr.length]);
            wrap.order(byteOrder);
            for (long j : jArr) {
                wrap.putInt((int) j);
            }
            return new ExifAttribute(4, jArr.length, wrap.array());
        }

        public static ExifAttribute createUShort(int[] iArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[3] * iArr.length]);
            wrap.order(byteOrder);
            for (int i : iArr) {
                wrap.putShort((short) i);
            }
            return new ExifAttribute(3, iArr.length, wrap.array());
        }

        public ExifAttribute(int i, int i2, long j, byte[] bArr) {
            this.format = i;
            this.numberOfComponents = i2;
            this.bytesOffset = j;
            this.bytes = bArr;
        }

        public static ExifAttribute createString(String str) {
            byte[] bytes = (str + (char) 0).getBytes(ExifInterface.ASCII);
            return new ExifAttribute(2, bytes.length, bytes);
        }

        public static ExifAttribute createURational(Rational[] rationalArr, ByteOrder byteOrder) {
            ByteBuffer wrap = ByteBuffer.wrap(new byte[ExifInterface.IFD_FORMAT_BYTES_PER_FORMAT[5] * rationalArr.length]);
            wrap.order(byteOrder);
            for (Rational rational : rationalArr) {
                wrap.putInt((int) rational.numerator);
                wrap.putInt((int) rational.denominator);
            }
            return new ExifAttribute(5, rationalArr.length, wrap.array());
        }

        /* JADX WARN: Not initialized variable reg: 3, insn: 0x01a7: MOVE  (r2 I:??[OBJECT, ARRAY]) = (r3 I:??[OBJECT, ARRAY]), block:B:137:0x01a7 */
        /* JADX WARN: Removed duplicated region for block: B:156:0x01aa A[EXC_TOP_SPLITTER, SYNTHETIC] */
        /*
            Code decompiled incorrectly, please refer to instructions dump.
            To view partially-correct add '--show-bad-code' argument
        */
        public final java.lang.Object getValue(java.nio.ByteOrder r13) {
            /*
                Method dump skipped, instructions count: 464
                To view this dump add '--comments-level debug' option
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.ExifAttribute.getValue(java.nio.ByteOrder):java.lang.Object");
        }

        public final String toString() {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("(");
            m.append(ExifInterface.IFD_FORMAT_NAMES[this.format]);
            m.append(", data length:");
            m.append(this.bytes.length);
            m.append(")");
            return m.toString();
        }

        public final double getDoubleValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a double value");
            } else if (value instanceof String) {
                return Double.parseDouble((String) value);
            } else {
                if (value instanceof long[]) {
                    long[] jArr = (long[]) value;
                    if (jArr.length == 1) {
                        return jArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof int[]) {
                    int[] iArr = (int[]) value;
                    if (iArr.length == 1) {
                        return iArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof double[]) {
                    double[] dArr = (double[]) value;
                    if (dArr.length == 1) {
                        return dArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof Rational[]) {
                    Rational[] rationalArr = (Rational[]) value;
                    if (rationalArr.length == 1) {
                        Rational rational = rationalArr[0];
                        Objects.requireNonNull(rational);
                        return rational.numerator / rational.denominator;
                    }
                    throw new NumberFormatException("There are more than one component");
                } else {
                    throw new NumberFormatException("Couldn't find a double value");
                }
            }
        }

        public final int getIntValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                throw new NumberFormatException("NULL can't be converted to a integer value");
            } else if (value instanceof String) {
                return Integer.parseInt((String) value);
            } else {
                if (value instanceof long[]) {
                    long[] jArr = (long[]) value;
                    if (jArr.length == 1) {
                        return (int) jArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else if (value instanceof int[]) {
                    int[] iArr = (int[]) value;
                    if (iArr.length == 1) {
                        return iArr[0];
                    }
                    throw new NumberFormatException("There are more than one component");
                } else {
                    throw new NumberFormatException("Couldn't find a integer value");
                }
            }
        }

        public final String getStringValue(ByteOrder byteOrder) {
            Object value = getValue(byteOrder);
            if (value == null) {
                return null;
            }
            if (value instanceof String) {
                return (String) value;
            }
            StringBuilder sb = new StringBuilder();
            int i = 0;
            if (value instanceof long[]) {
                long[] jArr = (long[]) value;
                while (i < jArr.length) {
                    sb.append(jArr[i]);
                    i++;
                    if (i != jArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            } else if (value instanceof int[]) {
                int[] iArr = (int[]) value;
                while (i < iArr.length) {
                    sb.append(iArr[i]);
                    i++;
                    if (i != iArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            } else if (value instanceof double[]) {
                double[] dArr = (double[]) value;
                while (i < dArr.length) {
                    sb.append(dArr[i]);
                    i++;
                    if (i != dArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            } else if (!(value instanceof Rational[])) {
                return null;
            } else {
                Rational[] rationalArr = (Rational[]) value;
                while (i < rationalArr.length) {
                    sb.append(rationalArr[i].numerator);
                    sb.append('/');
                    sb.append(rationalArr[i].denominator);
                    i++;
                    if (i != rationalArr.length) {
                        sb.append(",");
                    }
                }
                return sb.toString();
            }
        }

        public static ExifAttribute createULong(long j, ByteOrder byteOrder) {
            return createULong(new long[]{j}, byteOrder);
        }

        public static ExifAttribute createUShort(int i, ByteOrder byteOrder) {
            return createUShort(new int[]{i}, byteOrder);
        }
    }

    /* loaded from: classes.dex */
    public static class Rational {
        public final long denominator;
        public final long numerator;

        public final String toString() {
            return this.numerator + "/" + this.denominator;
        }

        public Rational(long j, long j2) {
            if (j2 == 0) {
                this.numerator = 0L;
                this.denominator = 1L;
                return;
            }
            this.numerator = j;
            this.denominator = j2;
        }
    }

    /* loaded from: classes.dex */
    public static class SeekableByteOrderedDataInputStream extends ByteOrderedDataInputStream {
        public SeekableByteOrderedDataInputStream(byte[] bArr) throws IOException {
            super(bArr);
            this.mDataInputStream.mark(Integer.MAX_VALUE);
        }

        public final void seek(long j) throws IOException {
            int i = this.mPosition;
            if (i > j) {
                this.mPosition = 0;
                this.mDataInputStream.reset();
            } else {
                j -= i;
            }
            skipFully((int) j);
        }

        public SeekableByteOrderedDataInputStream(BufferedInputStream bufferedInputStream) throws IOException {
            super(bufferedInputStream);
            if (bufferedInputStream.markSupported()) {
                this.mDataInputStream.mark(Integer.MAX_VALUE);
                return;
            }
            throw new IllegalArgumentException("Cannot create SeekableByteOrderedDataInputStream with stream that does not support mark/reset");
        }
    }

    static {
        ExifTag[] exifTagArr;
        ExifTag[] exifTagArr2 = {new ExifTag("NewSubfileType", 254, 4), new ExifTag("SubfileType", 255, 4), new ExifTag("ImageWidth", 256, 3, 4), new ExifTag("ImageLength", 257, 3, 4), new ExifTag("BitsPerSample", 258, 3), new ExifTag("Compression", 259, 3), new ExifTag("PhotometricInterpretation", 262, 3), new ExifTag("ImageDescription", 270, 2), new ExifTag("Make", 271, 2), new ExifTag("Model", 272, 2), new ExifTag("StripOffsets", 273, 3, 4), new ExifTag("Orientation", 274, 3), new ExifTag("SamplesPerPixel", 277, 3), new ExifTag("RowsPerStrip", 278, 3, 4), new ExifTag("StripByteCounts", 279, 3, 4), new ExifTag("XResolution", 282, 5), new ExifTag("YResolution", 283, 5), new ExifTag("PlanarConfiguration", 284, 3), new ExifTag("ResolutionUnit", 296, 3), new ExifTag("TransferFunction", 301, 3), new ExifTag("Software", 305, 2), new ExifTag("DateTime", 306, 2), new ExifTag("Artist", 315, 2), new ExifTag("WhitePoint", 318, 5), new ExifTag("PrimaryChromaticities", 319, 5), new ExifTag("SubIFDPointer", 330, 4), new ExifTag("JPEGInterchangeFormat", 513, 4), new ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifTag("YCbCrCoefficients", 529, 5), new ExifTag("YCbCrSubSampling", 530, 3), new ExifTag("YCbCrPositioning", 531, 3), new ExifTag("ReferenceBlackWhite", 532, 5), new ExifTag("Copyright", 33432, 2), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("SensorTopBorder", 4, 4), new ExifTag("SensorLeftBorder", 5, 4), new ExifTag("SensorBottomBorder", 6, 4), new ExifTag("SensorRightBorder", 7, 4), new ExifTag("ISO", 23, 3), new ExifTag("JpgFromRaw", 46, 7), new ExifTag("Xmp", 700, 1)};
        EXIF_TAGS = new ExifTag[][]{exifTagArr2, new ExifTag[]{new ExifTag("ExposureTime", 33434, 5), new ExifTag("FNumber", 33437, 5), new ExifTag("ExposureProgram", 34850, 3), new ExifTag("SpectralSensitivity", 34852, 2), new ExifTag("PhotographicSensitivity", 34855, 3), new ExifTag("OECF", 34856, 7), new ExifTag("SensitivityType", 34864, 3), new ExifTag("StandardOutputSensitivity", 34865, 4), new ExifTag("RecommendedExposureIndex", 34866, 4), new ExifTag("ISOSpeed", 34867, 4), new ExifTag("ISOSpeedLatitudeyyy", 34868, 4), new ExifTag("ISOSpeedLatitudezzz", 34869, 4), new ExifTag("ExifVersion", 36864, 2), new ExifTag("DateTimeOriginal", 36867, 2), new ExifTag("DateTimeDigitized", 36868, 2), new ExifTag("OffsetTime", 36880, 2), new ExifTag("OffsetTimeOriginal", 36881, 2), new ExifTag("OffsetTimeDigitized", 36882, 2), new ExifTag("ComponentsConfiguration", 37121, 7), new ExifTag("CompressedBitsPerPixel", 37122, 5), new ExifTag("ShutterSpeedValue", 37377, 10), new ExifTag("ApertureValue", 37378, 5), new ExifTag("BrightnessValue", 37379, 10), new ExifTag("ExposureBiasValue", 37380, 10), new ExifTag("MaxApertureValue", 37381, 5), new ExifTag("SubjectDistance", 37382, 5), new ExifTag("MeteringMode", 37383, 3), new ExifTag("LightSource", 37384, 3), new ExifTag("Flash", 37385, 3), new ExifTag("FocalLength", 37386, 5), new ExifTag("SubjectArea", 37396, 3), new ExifTag("MakerNote", 37500, 7), new ExifTag("UserComment", 37510, 7), new ExifTag("SubSecTime", 37520, 2), new ExifTag("SubSecTimeOriginal", 37521, 2), new ExifTag("SubSecTimeDigitized", 37522, 2), new ExifTag("FlashpixVersion", 40960, 7), new ExifTag("ColorSpace", 40961, 3), new ExifTag("PixelXDimension", 40962, 3, 4), new ExifTag("PixelYDimension", 40963, 3, 4), new ExifTag("RelatedSoundFile", 40964, 2), new ExifTag("InteroperabilityIFDPointer", 40965, 4), new ExifTag("FlashEnergy", 41483, 5), new ExifTag("SpatialFrequencyResponse", 41484, 7), new ExifTag("FocalPlaneXResolution", 41486, 5), new ExifTag("FocalPlaneYResolution", 41487, 5), new ExifTag("FocalPlaneResolutionUnit", 41488, 3), new ExifTag("SubjectLocation", 41492, 3), new ExifTag("ExposureIndex", 41493, 5), new ExifTag("SensingMethod", 41495, 3), new ExifTag("FileSource", 41728, 7), new ExifTag("SceneType", 41729, 7), new ExifTag("CFAPattern", 41730, 7), new ExifTag("CustomRendered", 41985, 3), new ExifTag("ExposureMode", 41986, 3), new ExifTag("WhiteBalance", 41987, 3), new ExifTag("DigitalZoomRatio", 41988, 5), new ExifTag("FocalLengthIn35mmFilm", 41989, 3), new ExifTag("SceneCaptureType", 41990, 3), new ExifTag("GainControl", 41991, 3), new ExifTag("Contrast", 41992, 3), new ExifTag("Saturation", 41993, 3), new ExifTag("Sharpness", 41994, 3), new ExifTag("DeviceSettingDescription", 41995, 7), new ExifTag("SubjectDistanceRange", 41996, 3), new ExifTag("ImageUniqueID", 42016, 2), new ExifTag("CameraOwnerName", 42032, 2), new ExifTag("BodySerialNumber", 42033, 2), new ExifTag("LensSpecification", 42034, 5), new ExifTag("LensMake", 42035, 2), new ExifTag("LensModel", 42036, 2), new ExifTag("Gamma", 42240, 5), new ExifTag("DNGVersion", 50706, 1), new ExifTag("DefaultCropSize", 50720, 3, 4)}, new ExifTag[]{new ExifTag("GPSVersionID", 0, 1), new ExifTag("GPSLatitudeRef", 1, 2), new ExifTag("GPSLatitude", 2, 5, 10), new ExifTag("GPSLongitudeRef", 3, 2), new ExifTag("GPSLongitude", 4, 5, 10), new ExifTag("GPSAltitudeRef", 5, 1), new ExifTag("GPSAltitude", 6, 5), new ExifTag("GPSTimeStamp", 7, 5), new ExifTag("GPSSatellites", 8, 2), new ExifTag("GPSStatus", 9, 2), new ExifTag("GPSMeasureMode", 10, 2), new ExifTag("GPSDOP", 11, 5), new ExifTag("GPSSpeedRef", 12, 2), new ExifTag("GPSSpeed", 13, 5), new ExifTag("GPSTrackRef", 14, 2), new ExifTag("GPSTrack", 15, 5), new ExifTag("GPSImgDirectionRef", 16, 2), new ExifTag("GPSImgDirection", 17, 5), new ExifTag("GPSMapDatum", 18, 2), new ExifTag("GPSDestLatitudeRef", 19, 2), new ExifTag("GPSDestLatitude", 20, 5), new ExifTag("GPSDestLongitudeRef", 21, 2), new ExifTag("GPSDestLongitude", 22, 5), new ExifTag("GPSDestBearingRef", 23, 2), new ExifTag("GPSDestBearing", 24, 5), new ExifTag("GPSDestDistanceRef", 25, 2), new ExifTag("GPSDestDistance", 26, 5), new ExifTag("GPSProcessingMethod", 27, 7), new ExifTag("GPSAreaInformation", 28, 7), new ExifTag("GPSDateStamp", 29, 2), new ExifTag("GPSDifferential", 30, 3), new ExifTag("GPSHPositioningError", 31, 5)}, new ExifTag[]{new ExifTag("InteroperabilityIndex", 1, 2)}, new ExifTag[]{new ExifTag("NewSubfileType", 254, 4), new ExifTag("SubfileType", 255, 4), new ExifTag("ThumbnailImageWidth", 256, 3, 4), new ExifTag("ThumbnailImageLength", 257, 3, 4), new ExifTag("BitsPerSample", 258, 3), new ExifTag("Compression", 259, 3), new ExifTag("PhotometricInterpretation", 262, 3), new ExifTag("ImageDescription", 270, 2), new ExifTag("Make", 271, 2), new ExifTag("Model", 272, 2), new ExifTag("StripOffsets", 273, 3, 4), new ExifTag("ThumbnailOrientation", 274, 3), new ExifTag("SamplesPerPixel", 277, 3), new ExifTag("RowsPerStrip", 278, 3, 4), new ExifTag("StripByteCounts", 279, 3, 4), new ExifTag("XResolution", 282, 5), new ExifTag("YResolution", 283, 5), new ExifTag("PlanarConfiguration", 284, 3), new ExifTag("ResolutionUnit", 296, 3), new ExifTag("TransferFunction", 301, 3), new ExifTag("Software", 305, 2), new ExifTag("DateTime", 306, 2), new ExifTag("Artist", 315, 2), new ExifTag("WhitePoint", 318, 5), new ExifTag("PrimaryChromaticities", 319, 5), new ExifTag("SubIFDPointer", 330, 4), new ExifTag("JPEGInterchangeFormat", 513, 4), new ExifTag("JPEGInterchangeFormatLength", 514, 4), new ExifTag("YCbCrCoefficients", 529, 5), new ExifTag("YCbCrSubSampling", 530, 3), new ExifTag("YCbCrPositioning", 531, 3), new ExifTag("ReferenceBlackWhite", 532, 5), new ExifTag("Xmp", 700, 1), new ExifTag("Copyright", 33432, 2), new ExifTag("ExifIFDPointer", 34665, 4), new ExifTag("GPSInfoIFDPointer", 34853, 4), new ExifTag("DNGVersion", 50706, 1), new ExifTag("DefaultCropSize", 50720, 3, 4)}, exifTagArr2, new ExifTag[]{new ExifTag("ThumbnailImage", 256, 7), new ExifTag("CameraSettingsIFDPointer", 8224, 4), new ExifTag("ImageProcessingIFDPointer", 8256, 4)}, new ExifTag[]{new ExifTag("PreviewImageStart", 257, 4), new ExifTag("PreviewImageLength", 258, 4)}, new ExifTag[]{new ExifTag("AspectFrame", 4371, 3)}, new ExifTag[]{new ExifTag("ColorSpace", 55, 3)}};
        Charset forName = Charset.forName("US-ASCII");
        ASCII = forName;
        IDENTIFIER_EXIF_APP1 = "Exif\u0000\u0000".getBytes(forName);
        IDENTIFIER_XMP_APP1 = "http://ns.adobe.com/xap/1.0/\u0000".getBytes(forName);
        Locale locale = Locale.US;
        new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", locale).setTimeZone(TimeZone.getTimeZone("UTC"));
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", locale).setTimeZone(TimeZone.getTimeZone("UTC"));
        int i = 0;
        while (true) {
            ExifTag[][] exifTagArr3 = EXIF_TAGS;
            if (i < exifTagArr3.length) {
                sExifTagMapsForReading[i] = new HashMap<>();
                sExifTagMapsForWriting[i] = new HashMap<>();
                for (ExifTag exifTag : exifTagArr3[i]) {
                    sExifTagMapsForReading[i].put(Integer.valueOf(exifTag.number), exifTag);
                    sExifTagMapsForWriting[i].put(exifTag.name, exifTag);
                }
                i++;
            } else {
                HashMap<Integer, Integer> hashMap = sExifPointerTagMap;
                ExifTag[] exifTagArr4 = EXIF_POINTER_TAGS;
                hashMap.put(Integer.valueOf(exifTagArr4[0].number), 5);
                hashMap.put(Integer.valueOf(exifTagArr4[1].number), 1);
                hashMap.put(Integer.valueOf(exifTagArr4[2].number), 2);
                hashMap.put(Integer.valueOf(exifTagArr4[3].number), 3);
                hashMap.put(Integer.valueOf(exifTagArr4[4].number), 7);
                hashMap.put(Integer.valueOf(exifTagArr4[5].number), 8);
                Pattern.compile(".*[1-9].*");
                GPS_TIMESTAMP_PATTERN = Pattern.compile("^(\\d{2}):(\\d{2}):(\\d{2})$");
                DATETIME_PRIMARY_FORMAT_PATTERN = Pattern.compile("^(\\d{4}):(\\d{2}):(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2})$");
                DATETIME_SECONDARY_FORMAT_PATTERN = Pattern.compile("^(\\d{4})-(\\d{2})-(\\d{2})\\s(\\d{2}):(\\d{2}):(\\d{2})$");
                return;
            }
        }
    }

    public static void copyChunksUpToGivenChunkType(ByteOrderedDataInputStream byteOrderedDataInputStream, ByteOrderedDataOutputStream byteOrderedDataOutputStream, byte[] bArr, byte[] bArr2) throws IOException {
        String str;
        while (true) {
            byte[] bArr3 = new byte[4];
            if (byteOrderedDataInputStream.read(bArr3) != 4) {
                StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Encountered invalid length while copying WebP chunks up tochunk type ");
                Charset charset = ASCII;
                m.append(new String(bArr, charset));
                if (bArr2 == null) {
                    str = "";
                } else {
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m(" or ");
                    m2.append(new String(bArr2, charset));
                    str = m2.toString();
                }
                m.append(str);
                throw new IOException(m.toString());
            }
            int readInt = byteOrderedDataInputStream.readInt();
            byteOrderedDataOutputStream.write(bArr3);
            byteOrderedDataOutputStream.writeInt(readInt);
            if (readInt % 2 == 1) {
                readInt++;
            }
            ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream, readInt);
            if (Arrays.equals(bArr3, bArr)) {
                return;
            }
            if (bArr2 != null && Arrays.equals(bArr3, bArr2)) {
                return;
            }
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:15:0x0035 A[Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062, TryCatch #1 {IOException | UnsupportedOperationException -> 0x0062, blocks: (B:3:0x0002, B:5:0x0007, B:6:0x0013, B:15:0x0035, B:17:0x0040, B:20:0x0047, B:23:0x004f, B:24:0x0053, B:25:0x0056, B:30:0x0064, B:32:0x006d, B:34:0x0073, B:36:0x0079, B:38:0x007f), top: B:52:0x0002, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:30:0x0064 A[Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062, TryCatch #1 {IOException | UnsupportedOperationException -> 0x0062, blocks: (B:3:0x0002, B:5:0x0007, B:6:0x0013, B:15:0x0035, B:17:0x0040, B:20:0x0047, B:23:0x004f, B:24:0x0053, B:25:0x0056, B:30:0x0064, B:32:0x006d, B:34:0x0073, B:36:0x0079, B:38:0x007f), top: B:52:0x0002, outer: #0 }] */
    /* JADX WARN: Removed duplicated region for block: B:54:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void loadAttributes(java.io.FileInputStream r7) {
        /*
            r6 = this;
            r0 = 0
            r1 = r0
        L_0x0002:
            androidx.exifinterface.media.ExifInterface$ExifTag[][] r2 = androidx.exifinterface.media.ExifInterface.EXIF_TAGS     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            int r2 = r2.length     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            if (r1 >= r2) goto L_0x0013
            java.util.HashMap<java.lang.String, androidx.exifinterface.media.ExifInterface$ExifAttribute>[] r2 = r6.mAttributes     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            java.util.HashMap r3 = new java.util.HashMap     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            r3.<init>()     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            r2[r1] = r3     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            int r1 = r1 + 1
            goto L_0x0002
        L_0x0013:
            java.io.BufferedInputStream r1 = new java.io.BufferedInputStream     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            r2 = 5000(0x1388, float:7.006E-42)
            r1.<init>(r7, r2)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            int r7 = r6.getMimeType(r1)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            r6.mMimeType = r7     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            r2 = 14
            r3 = 13
            r4 = 9
            r5 = 4
            if (r7 == r5) goto L_0x0032
            if (r7 == r4) goto L_0x0032
            if (r7 == r3) goto L_0x0032
            if (r7 != r2) goto L_0x0030
            goto L_0x0032
        L_0x0030:
            r7 = 1
            goto L_0x0033
        L_0x0032:
            r7 = r0
        L_0x0033:
            if (r7 == 0) goto L_0x0064
            androidx.exifinterface.media.ExifInterface$SeekableByteOrderedDataInputStream r7 = new androidx.exifinterface.media.ExifInterface$SeekableByteOrderedDataInputStream     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            r7.<init>(r1)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            int r0 = r6.mMimeType     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            r1 = 12
            if (r0 != r1) goto L_0x0044
            r6.getHeifAttributes(r7)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            goto L_0x0056
        L_0x0044:
            r1 = 7
            if (r0 != r1) goto L_0x004b
            r6.getOrfAttributes(r7)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            goto L_0x0056
        L_0x004b:
            r1 = 10
            if (r0 != r1) goto L_0x0053
            r6.getRw2Attributes(r7)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            goto L_0x0056
        L_0x0053:
            r6.getRawAttributes(r7)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
        L_0x0056:
            int r0 = r6.mOffsetToExifData     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            long r0 = (long) r0     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            r7.seek(r0)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            r6.setThumbnailData(r7)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            goto L_0x0082
        L_0x0060:
            r7 = move-exception
            goto L_0x009e
        L_0x0062:
            r7 = move-exception
            goto L_0x008a
        L_0x0064:
            androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream r7 = new androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            r7.<init>(r1)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            int r1 = r6.mMimeType     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            if (r1 != r5) goto L_0x0071
            r6.getJpegAttributes(r7, r0, r0)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            goto L_0x0082
        L_0x0071:
            if (r1 != r3) goto L_0x0077
            r6.getPngAttributes(r7)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            goto L_0x0082
        L_0x0077:
            if (r1 != r4) goto L_0x007d
            r6.getRafAttributes(r7)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
            goto L_0x0082
        L_0x007d:
            if (r1 != r2) goto L_0x0082
            r6.getWebpAttributes(r7)     // Catch: all -> 0x0060, IOException | UnsupportedOperationException -> 0x0062
        L_0x0082:
            r6.addDefaultValuesForCompatibility()
            boolean r7 = androidx.exifinterface.media.ExifInterface.DEBUG
            if (r7 == 0) goto L_0x009d
            goto L_0x009a
        L_0x008a:
            boolean r0 = androidx.exifinterface.media.ExifInterface.DEBUG     // Catch: all -> 0x0060
            if (r0 == 0) goto L_0x0095
            java.lang.String r1 = "ExifInterface"
            java.lang.String r2 = "Invalid image: ExifInterface got an unsupported image format file(ExifInterface supports JPEG and some RAW image formats only) or a corrupted JPEG file to ExifInterface."
            android.util.Log.w(r1, r2, r7)     // Catch: all -> 0x0060
        L_0x0095:
            r6.addDefaultValuesForCompatibility()
            if (r0 == 0) goto L_0x009d
        L_0x009a:
            r6.printAttributes()
        L_0x009d:
            return
        L_0x009e:
            r6.addDefaultValuesForCompatibility()
            boolean r0 = androidx.exifinterface.media.ExifInterface.DEBUG
            if (r0 == 0) goto L_0x00a8
            r6.printAttributes()
        L_0x00a8:
            throw r7
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.loadAttributes(java.io.FileInputStream):void");
    }

    public final void printAttributes() {
        for (int i = 0; i < this.mAttributes.length; i++) {
            StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("The size of tag group[", i, "]: ");
            m.append(this.mAttributes[i].size());
            Log.d("ExifInterface", m.toString());
            for (Map.Entry<String, ExifAttribute> entry : this.mAttributes[i].entrySet()) {
                ExifAttribute value = entry.getValue();
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("tagName: ");
                m2.append(entry.getKey());
                m2.append(", tagType: ");
                m2.append(value.toString());
                m2.append(", tagValue: '");
                m2.append(value.getStringValue(this.mExifByteOrder));
                m2.append("'");
                Log.d("ExifInterface", m2.toString());
            }
        }
    }

    public final void removeAttribute(String str) {
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            this.mAttributes[i].remove(str);
        }
    }

    public final void validateImages() throws IOException {
        swapBasedOnImageSize(0, 5);
        swapBasedOnImageSize(0, 4);
        swapBasedOnImageSize(5, 4);
        ExifAttribute exifAttribute = this.mAttributes[1].get("PixelXDimension");
        ExifAttribute exifAttribute2 = this.mAttributes[1].get("PixelYDimension");
        if (!(exifAttribute == null || exifAttribute2 == null)) {
            this.mAttributes[0].put("ImageWidth", exifAttribute);
            this.mAttributes[0].put("ImageLength", exifAttribute2);
        }
        if (this.mAttributes[4].isEmpty() && isThumbnail(this.mAttributes[5])) {
            HashMap<String, ExifAttribute>[] hashMapArr = this.mAttributes;
            hashMapArr[4] = hashMapArr[5];
            hashMapArr[5] = new HashMap<>();
        }
        if (!isThumbnail(this.mAttributes[4])) {
            Log.d("ExifInterface", "No image meets the size requirements of a thumbnail image.");
        }
        replaceInvalidTags(0, "ThumbnailOrientation", "Orientation");
        replaceInvalidTags(0, "ThumbnailImageLength", "ImageLength");
        replaceInvalidTags(0, "ThumbnailImageWidth", "ImageWidth");
        replaceInvalidTags(5, "ThumbnailOrientation", "Orientation");
        replaceInvalidTags(5, "ThumbnailImageLength", "ImageLength");
        replaceInvalidTags(5, "ThumbnailImageWidth", "ImageWidth");
        replaceInvalidTags(4, "Orientation", "ThumbnailOrientation");
        replaceInvalidTags(4, "ImageLength", "ThumbnailImageLength");
        replaceInvalidTags(4, "ImageWidth", "ThumbnailImageWidth");
    }

    public static Pair<Integer, Integer> guessDataFormat(String str) {
        int i;
        int i2;
        if (str.contains(",")) {
            String[] split = str.split(",", -1);
            Pair<Integer, Integer> guessDataFormat = guessDataFormat(split[0]);
            if (((Integer) guessDataFormat.first).intValue() == 2) {
                return guessDataFormat;
            }
            for (int i3 = 1; i3 < split.length; i3++) {
                Pair<Integer, Integer> guessDataFormat2 = guessDataFormat(split[i3]);
                if (((Integer) guessDataFormat2.first).equals(guessDataFormat.first) || ((Integer) guessDataFormat2.second).equals(guessDataFormat.first)) {
                    i = ((Integer) guessDataFormat.first).intValue();
                } else {
                    i = -1;
                }
                if (((Integer) guessDataFormat.second).intValue() == -1 || (!((Integer) guessDataFormat2.first).equals(guessDataFormat.second) && !((Integer) guessDataFormat2.second).equals(guessDataFormat.second))) {
                    i2 = -1;
                } else {
                    i2 = ((Integer) guessDataFormat.second).intValue();
                }
                if (i == -1 && i2 == -1) {
                    return new Pair<>(2, -1);
                }
                if (i == -1) {
                    guessDataFormat = new Pair<>(Integer.valueOf(i2), -1);
                } else if (i2 == -1) {
                    guessDataFormat = new Pair<>(Integer.valueOf(i), -1);
                }
            }
            return guessDataFormat;
        } else if (str.contains("/")) {
            String[] split2 = str.split("/", -1);
            if (split2.length == 2) {
                try {
                    long parseDouble = (long) Double.parseDouble(split2[0]);
                    long parseDouble2 = (long) Double.parseDouble(split2[1]);
                    if (parseDouble >= 0 && parseDouble2 >= 0) {
                        if (parseDouble <= 2147483647L && parseDouble2 <= 2147483647L) {
                            return new Pair<>(10, 5);
                        }
                        return new Pair<>(5, -1);
                    }
                    return new Pair<>(10, -1);
                } catch (NumberFormatException unused) {
                }
            }
            return new Pair<>(2, -1);
        } else {
            try {
                try {
                    Long valueOf = Long.valueOf(Long.parseLong(str));
                    if (valueOf.longValue() >= 0 && valueOf.longValue() <= 65535) {
                        return new Pair<>(3, 4);
                    }
                    if (valueOf.longValue() < 0) {
                        return new Pair<>(9, -1);
                    }
                    return new Pair<>(4, -1);
                } catch (NumberFormatException unused2) {
                    return new Pair<>(2, -1);
                }
            } catch (NumberFormatException unused3) {
                Double.parseDouble(str);
                return new Pair<>(12, -1);
            }
        }
    }

    public final void addDefaultValuesForCompatibility() {
        String attribute = getAttribute("DateTimeOriginal");
        if (attribute != null && getAttribute("DateTime") == null) {
            this.mAttributes[0].put("DateTime", ExifAttribute.createString(attribute));
        }
        if (getAttribute("ImageWidth") == null) {
            this.mAttributes[0].put("ImageWidth", ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (getAttribute("ImageLength") == null) {
            this.mAttributes[0].put("ImageLength", ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (getAttribute("Orientation") == null) {
            this.mAttributes[0].put("Orientation", ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (getAttribute("LightSource") == null) {
            this.mAttributes[1].put("LightSource", ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
    }

    public final String getAttribute(String str) {
        String str2;
        ExifAttribute exifAttribute;
        if ("ISOSpeedRatings".equals(str)) {
            if (DEBUG) {
                Log.d("ExifInterface", "getExifAttribute: Replacing TAG_ISO_SPEED_RATINGS with TAG_PHOTOGRAPHIC_SENSITIVITY.");
            }
            str2 = "PhotographicSensitivity";
        } else {
            str2 = str;
        }
        int i = 0;
        while (true) {
            if (i >= EXIF_TAGS.length) {
                exifAttribute = null;
                break;
            }
            exifAttribute = this.mAttributes[i].get(str2);
            if (exifAttribute != null) {
                break;
            }
            i++;
        }
        if (exifAttribute != null) {
            if (!sTagSetForCompatibility.contains(str)) {
                return exifAttribute.getStringValue(this.mExifByteOrder);
            }
            if (str.equals("GPSTimeStamp")) {
                int i2 = exifAttribute.format;
                if (i2 == 5 || i2 == 10) {
                    Rational[] rationalArr = (Rational[]) exifAttribute.getValue(this.mExifByteOrder);
                    if (rationalArr != null && rationalArr.length == 3) {
                        return String.format("%02d:%02d:%02d", Integer.valueOf((int) (((float) rationalArr[0].numerator) / ((float) rationalArr[0].denominator))), Integer.valueOf((int) (((float) rationalArr[1].numerator) / ((float) rationalArr[1].denominator))), Integer.valueOf((int) (((float) rationalArr[2].numerator) / ((float) rationalArr[2].denominator))));
                    }
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid GPS Timestamp array. array=");
                    m.append(Arrays.toString(rationalArr));
                    Log.w("ExifInterface", m.toString());
                    return null;
                }
                StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("GPS Timestamp format is not rational. format=");
                m2.append(exifAttribute.format);
                Log.w("ExifInterface", m2.toString());
                return null;
            }
            try {
                return Double.toString(exifAttribute.getDoubleValue(this.mExifByteOrder));
            } catch (NumberFormatException unused) {
            }
        }
        return null;
    }

    /* JADX WARN: Code restructure failed: missing block: B:85:0x01a8, code lost:
        r18.mByteOrder = r17.mExifByteOrder;
     */
    /* JADX WARN: Code restructure failed: missing block: B:86:0x01ac, code lost:
        return;
     */
    /* JADX WARN: Removed duplicated region for block: B:32:0x00ae A[FALL_THROUGH] */
    /* JADX WARN: Removed duplicated region for block: B:62:0x0137  */
    /* JADX WARN: Removed duplicated region for block: B:63:0x014e  */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0168  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void getJpegAttributes(androidx.exifinterface.media.ExifInterface.ByteOrderedDataInputStream r18, int r19, int r20) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 542
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.getJpegAttributes(androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream, int, int):void");
    }

    /* JADX WARN: Code restructure failed: missing block: B:67:0x00cf, code lost:
        if (r8 != null) goto L_0x00d1;
     */
    /* JADX WARN: Removed duplicated region for block: B:114:0x0143 A[RETURN] */
    /* JADX WARN: Removed duplicated region for block: B:115:0x0146  */
    /* JADX WARN: Removed duplicated region for block: B:146:0x018f  */
    /* JADX WARN: Removed duplicated region for block: B:153:0x0111 A[EXC_TOP_SPLITTER, SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:93:0x010f A[RETURN] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final int getMimeType(java.io.BufferedInputStream r18) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 403
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.getMimeType(java.io.BufferedInputStream):int");
    }

    public final void getPngAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        if (DEBUG) {
            Log.d("ExifInterface", "getPngAttributes starting with: " + byteOrderedDataInputStream);
        }
        byteOrderedDataInputStream.mByteOrder = ByteOrder.BIG_ENDIAN;
        byte[] bArr = PNG_SIGNATURE;
        byteOrderedDataInputStream.skipFully(bArr.length);
        int length = bArr.length + 0;
        while (true) {
            try {
                int readInt = byteOrderedDataInputStream.readInt();
                int i = length + 4;
                byte[] bArr2 = new byte[4];
                if (byteOrderedDataInputStream.read(bArr2) == 4) {
                    int i2 = i + 4;
                    if (i2 == 16 && !Arrays.equals(bArr2, PNG_CHUNK_TYPE_IHDR)) {
                        throw new IOException("Encountered invalid PNG file--IHDR chunk should appearas the first chunk");
                    }
                    if (!Arrays.equals(bArr2, PNG_CHUNK_TYPE_IEND)) {
                        if (Arrays.equals(bArr2, PNG_CHUNK_TYPE_EXIF)) {
                            byte[] bArr3 = new byte[readInt];
                            if (byteOrderedDataInputStream.read(bArr3) == readInt) {
                                int readInt2 = byteOrderedDataInputStream.readInt();
                                CRC32 crc32 = new CRC32();
                                crc32.update(bArr2);
                                crc32.update(bArr3);
                                if (((int) crc32.getValue()) == readInt2) {
                                    this.mOffsetToExifData = i2;
                                    readExifSegment(bArr3, 0);
                                    validateImages();
                                    setThumbnailData(new ByteOrderedDataInputStream(bArr3));
                                    return;
                                }
                                throw new IOException("Encountered invalid CRC value for PNG-EXIF chunk.\n recorded CRC value: " + readInt2 + ", calculated CRC value: " + crc32.getValue());
                            }
                            throw new IOException("Failed to read given length for given PNG chunk type: " + ExifInterfaceUtils.byteArrayToHexString(bArr2));
                        }
                        int i3 = readInt + 4;
                        byteOrderedDataInputStream.skipFully(i3);
                        length = i2 + i3;
                    } else {
                        return;
                    }
                } else {
                    throw new IOException("Encountered invalid length while parsing PNG chunktype");
                }
            } catch (EOFException unused) {
                throw new IOException("Encountered corrupt PNG file.");
            }
        }
    }

    public final void getRafAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        boolean z = DEBUG;
        if (z) {
            Log.d("ExifInterface", "getRafAttributes starting with: " + byteOrderedDataInputStream);
        }
        byteOrderedDataInputStream.skipFully(84);
        byte[] bArr = new byte[4];
        byte[] bArr2 = new byte[4];
        byte[] bArr3 = new byte[4];
        byteOrderedDataInputStream.read(bArr);
        byteOrderedDataInputStream.read(bArr2);
        byteOrderedDataInputStream.read(bArr3);
        int i = ByteBuffer.wrap(bArr).getInt();
        int i2 = ByteBuffer.wrap(bArr2).getInt();
        int i3 = ByteBuffer.wrap(bArr3).getInt();
        byte[] bArr4 = new byte[i2];
        byteOrderedDataInputStream.skipFully(i - byteOrderedDataInputStream.mPosition);
        byteOrderedDataInputStream.read(bArr4);
        getJpegAttributes(new ByteOrderedDataInputStream(bArr4), i, 5);
        byteOrderedDataInputStream.skipFully(i3 - byteOrderedDataInputStream.mPosition);
        byteOrderedDataInputStream.mByteOrder = ByteOrder.BIG_ENDIAN;
        int readInt = byteOrderedDataInputStream.readInt();
        if (z) {
            ExifInterface$$ExternalSyntheticOutline1.m("numberOfDirectoryEntry: ", readInt, "ExifInterface");
        }
        for (int i4 = 0; i4 < readInt; i4++) {
            int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
            int readUnsignedShort2 = byteOrderedDataInputStream.readUnsignedShort();
            if (readUnsignedShort == TAG_RAF_IMAGE_SIZE.number) {
                short readShort = byteOrderedDataInputStream.readShort();
                short readShort2 = byteOrderedDataInputStream.readShort();
                ExifAttribute createUShort = ExifAttribute.createUShort(readShort, this.mExifByteOrder);
                ExifAttribute createUShort2 = ExifAttribute.createUShort(readShort2, this.mExifByteOrder);
                this.mAttributes[0].put("ImageLength", createUShort);
                this.mAttributes[0].put("ImageWidth", createUShort2);
                if (DEBUG) {
                    Log.d("ExifInterface", "Updated to length: " + ((int) readShort) + ", width: " + ((int) readShort2));
                    return;
                }
                return;
            }
            byteOrderedDataInputStream.skipFully(readUnsignedShort2);
        }
    }

    public final void getRw2Attributes(SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream) throws IOException {
        if (DEBUG) {
            Log.d("ExifInterface", "getRw2Attributes starting with: " + seekableByteOrderedDataInputStream);
        }
        getRawAttributes(seekableByteOrderedDataInputStream);
        ExifAttribute exifAttribute = this.mAttributes[0].get("JpgFromRaw");
        if (exifAttribute != null) {
            getJpegAttributes(new ByteOrderedDataInputStream(exifAttribute.bytes), (int) exifAttribute.bytesOffset, 5);
        }
        ExifAttribute exifAttribute2 = this.mAttributes[0].get("ISO");
        ExifAttribute exifAttribute3 = this.mAttributes[1].get("PhotographicSensitivity");
        if (exifAttribute2 != null && exifAttribute3 == null) {
            this.mAttributes[1].put("PhotographicSensitivity", exifAttribute2);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Not initialized variable reg: 2, insn: 0x0074: MOVE  (r1 I:??[OBJECT, ARRAY]) = (r2 I:??[OBJECT, ARRAY]), block:B:39:0x0074 */
    /* JADX WARN: Type inference failed for: r0v1, types: [byte[]] */
    /* JADX WARN: Type inference failed for: r0v5 */
    /* JADX WARN: Type inference failed for: r0v7, types: [java.io.FileDescriptor] */
    public final byte[] getThumbnailBytes() {
        Throwable th;
        Exception e;
        FileInputStream fileInputStream;
        Closeable closeable = null;
        if (!this.mHasThumbnail) {
            return null;
        }
        FileDescriptor fileDescriptor = this.mThumbnailBytes;
        try {
            if (fileDescriptor != 0) {
                return fileDescriptor;
            }
            try {
                fileDescriptor = Os.dup(this.mSeekableFileDescriptor);
            } catch (Exception e2) {
                e = e2;
                fileDescriptor = 0;
                fileInputStream = null;
            } catch (Throwable th2) {
                th = th2;
                fileDescriptor = 0;
            }
            try {
                Os.lseek(fileDescriptor, 0L, OsConstants.SEEK_SET);
                fileInputStream = new FileInputStream((FileDescriptor) fileDescriptor);
                try {
                    if (fileInputStream.skip(this.mThumbnailOffset + this.mOffsetToExifData) == this.mThumbnailOffset + this.mOffsetToExifData) {
                        byte[] bArr = new byte[this.mThumbnailLength];
                        if (fileInputStream.read(bArr) == this.mThumbnailLength) {
                            this.mThumbnailBytes = bArr;
                            ExifInterfaceUtils.closeQuietly(fileInputStream);
                            if (fileDescriptor != 0) {
                                ExifInterfaceUtils.closeFileDescriptor(fileDescriptor);
                            }
                            return bArr;
                        }
                        throw new IOException("Corrupted image");
                    }
                    throw new IOException("Corrupted image");
                } catch (Exception e3) {
                    e = e3;
                    Log.d("ExifInterface", "Encountered exception while getting thumbnail", e);
                    ExifInterfaceUtils.closeQuietly(fileInputStream);
                    if (fileDescriptor != 0) {
                        ExifInterfaceUtils.closeFileDescriptor(fileDescriptor);
                    }
                    return null;
                }
            } catch (Exception e4) {
                e = e4;
                fileInputStream = null;
            } catch (Throwable th3) {
                th = th3;
                ExifInterfaceUtils.closeQuietly(closeable);
                if (fileDescriptor != 0) {
                    ExifInterfaceUtils.closeFileDescriptor(fileDescriptor);
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
        }
    }

    public final void getWebpAttributes(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        if (DEBUG) {
            Log.d("ExifInterface", "getWebpAttributes starting with: " + byteOrderedDataInputStream);
        }
        byteOrderedDataInputStream.mByteOrder = ByteOrder.LITTLE_ENDIAN;
        byteOrderedDataInputStream.skipFully(WEBP_SIGNATURE_1.length);
        int readInt = byteOrderedDataInputStream.readInt() + 8;
        byte[] bArr = WEBP_SIGNATURE_2;
        byteOrderedDataInputStream.skipFully(bArr.length);
        int length = bArr.length + 8;
        while (true) {
            try {
                byte[] bArr2 = new byte[4];
                if (byteOrderedDataInputStream.read(bArr2) == 4) {
                    int readInt2 = byteOrderedDataInputStream.readInt();
                    int i = length + 4 + 4;
                    if (Arrays.equals(WEBP_CHUNK_TYPE_EXIF, bArr2)) {
                        byte[] bArr3 = new byte[readInt2];
                        if (byteOrderedDataInputStream.read(bArr3) == readInt2) {
                            this.mOffsetToExifData = i;
                            readExifSegment(bArr3, 0);
                            setThumbnailData(new ByteOrderedDataInputStream(bArr3));
                            return;
                        }
                        throw new IOException("Failed to read given length for given PNG chunk type: " + ExifInterfaceUtils.byteArrayToHexString(bArr2));
                    }
                    if (readInt2 % 2 == 1) {
                        readInt2++;
                    }
                    length = i + readInt2;
                    if (length != readInt) {
                        if (length <= readInt) {
                            byteOrderedDataInputStream.skipFully(readInt2);
                        } else {
                            throw new IOException("Encountered WebP file with invalid chunk size");
                        }
                    } else {
                        return;
                    }
                } else {
                    throw new IOException("Encountered invalid length while parsing WebP chunktype");
                }
            } catch (EOFException unused) {
                throw new IOException("Encountered corrupt WebP file.");
            }
        }
    }

    public final void handleThumbnailFromJfif(ByteOrderedDataInputStream byteOrderedDataInputStream, HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get("JPEGInterchangeFormat");
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get("JPEGInterchangeFormatLength");
        if (exifAttribute != null && exifAttribute2 != null) {
            int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
            int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
            if (this.mMimeType == 7) {
                intValue += this.mOrfMakerNoteOffset;
            }
            if (intValue > 0 && intValue2 > 0) {
                this.mHasThumbnail = true;
                if (this.mSeekableFileDescriptor == null) {
                    byte[] bArr = new byte[intValue2];
                    byteOrderedDataInputStream.skip(intValue);
                    byteOrderedDataInputStream.read(bArr);
                    this.mThumbnailBytes = bArr;
                }
                this.mThumbnailOffset = intValue;
                this.mThumbnailLength = intValue2;
            }
            if (DEBUG) {
                Log.d("ExifInterface", "Setting thumbnail attributes with offset: " + intValue + ", length: " + intValue2);
            }
        }
    }

    public final boolean isThumbnail(HashMap hashMap) throws IOException {
        ExifAttribute exifAttribute = (ExifAttribute) hashMap.get("ImageLength");
        ExifAttribute exifAttribute2 = (ExifAttribute) hashMap.get("ImageWidth");
        if (exifAttribute == null || exifAttribute2 == null) {
            return false;
        }
        int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
        int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
        if (intValue > 512 || intValue2 > 512) {
            return false;
        }
        return true;
    }

    public final void readExifSegment(byte[] bArr, int i) throws IOException {
        SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream = new SeekableByteOrderedDataInputStream(bArr);
        parseTiffHeaders(seekableByteOrderedDataInputStream);
        readImageFileDirectory(seekableByteOrderedDataInputStream, i);
    }

    /* JADX WARN: Removed duplicated region for block: B:108:0x01f8  */
    /* JADX WARN: Removed duplicated region for block: B:111:0x0216  */
    /* JADX WARN: Removed duplicated region for block: B:116:0x024b  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00ca  */
    /* JADX WARN: Removed duplicated region for block: B:53:0x00e6  */
    /* JADX WARN: Removed duplicated region for block: B:69:0x011b  */
    /* JADX WARN: Removed duplicated region for block: B:70:0x0120  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void readImageFileDirectory(androidx.exifinterface.media.ExifInterface.SeekableByteOrderedDataInputStream r22, int r23) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 835
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.readImageFileDirectory(androidx.exifinterface.media.ExifInterface$SeekableByteOrderedDataInputStream, int):void");
    }

    public final void replaceInvalidTags(int i, String str, String str2) {
        if (!this.mAttributes[i].isEmpty() && this.mAttributes[i].get(str) != null) {
            HashMap<String, ExifAttribute>[] hashMapArr = this.mAttributes;
            hashMapArr[i].put(str2, hashMapArr[i].get(str));
            this.mAttributes[i].remove(str);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Removed duplicated region for block: B:87:0x0136  */
    /* JADX WARN: Type inference failed for: r9v2, types: [java.io.OutputStream, java.io.Closeable, java.io.FileOutputStream] */
    /* JADX WARN: Unknown variable types count: 1 */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void saveAttributes() throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 365
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.saveAttributes():void");
    }

    public final void saveJpegAttributes(BufferedInputStream bufferedInputStream, BufferedOutputStream bufferedOutputStream) throws IOException {
        if (DEBUG) {
            Log.d("ExifInterface", "saveJpegAttributes starting with (inputStream: " + bufferedInputStream + ", outputStream: " + bufferedOutputStream + ")");
        }
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bufferedInputStream);
        ByteOrderedDataOutputStream byteOrderedDataOutputStream = new ByteOrderedDataOutputStream(bufferedOutputStream, ByteOrder.BIG_ENDIAN);
        if (byteOrderedDataInputStream.readByte() == -1) {
            byteOrderedDataOutputStream.writeByte(-1);
            if (byteOrderedDataInputStream.readByte() == -40) {
                byteOrderedDataOutputStream.writeByte(-40);
                ExifAttribute exifAttribute = null;
                if (getAttribute("Xmp") != null && this.mXmpIsFromSeparateMarker) {
                    exifAttribute = this.mAttributes[0].remove("Xmp");
                }
                byteOrderedDataOutputStream.writeByte(-1);
                byteOrderedDataOutputStream.writeByte(-31);
                writeExifSegment(byteOrderedDataOutputStream);
                if (exifAttribute != null) {
                    this.mAttributes[0].put("Xmp", exifAttribute);
                }
                byte[] bArr = new byte[4096];
                while (byteOrderedDataInputStream.readByte() == -1) {
                    byte readByte = byteOrderedDataInputStream.readByte();
                    if (readByte == -39 || readByte == -38) {
                        byteOrderedDataOutputStream.writeByte(-1);
                        byteOrderedDataOutputStream.writeByte(readByte);
                        ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream);
                        return;
                    } else if (readByte != -31) {
                        byteOrderedDataOutputStream.writeByte(-1);
                        byteOrderedDataOutputStream.writeByte(readByte);
                        int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
                        byteOrderedDataOutputStream.writeShort((short) readUnsignedShort);
                        int i = readUnsignedShort - 2;
                        if (i >= 0) {
                            while (i > 0) {
                                int read = byteOrderedDataInputStream.read(bArr, 0, Math.min(i, 4096));
                                if (read >= 0) {
                                    byteOrderedDataOutputStream.write(bArr, 0, read);
                                    i -= read;
                                }
                            }
                        } else {
                            throw new IOException("Invalid length");
                        }
                    } else {
                        int readUnsignedShort2 = byteOrderedDataInputStream.readUnsignedShort() - 2;
                        if (readUnsignedShort2 >= 0) {
                            byte[] bArr2 = new byte[6];
                            if (readUnsignedShort2 >= 6) {
                                if (byteOrderedDataInputStream.read(bArr2) != 6) {
                                    throw new IOException("Invalid exif");
                                } else if (Arrays.equals(bArr2, IDENTIFIER_EXIF_APP1)) {
                                    byteOrderedDataInputStream.skipFully(readUnsignedShort2 - 6);
                                }
                            }
                            byteOrderedDataOutputStream.writeByte(-1);
                            byteOrderedDataOutputStream.writeByte(readByte);
                            byteOrderedDataOutputStream.writeShort((short) (readUnsignedShort2 + 2));
                            if (readUnsignedShort2 >= 6) {
                                readUnsignedShort2 -= 6;
                                byteOrderedDataOutputStream.write(bArr2);
                            }
                            while (readUnsignedShort2 > 0) {
                                int read2 = byteOrderedDataInputStream.read(bArr, 0, Math.min(readUnsignedShort2, 4096));
                                if (read2 >= 0) {
                                    byteOrderedDataOutputStream.write(bArr, 0, read2);
                                    readUnsignedShort2 -= read2;
                                }
                            }
                        } else {
                            throw new IOException("Invalid length");
                        }
                    }
                }
                throw new IOException("Invalid marker");
            }
            throw new IOException("Invalid marker");
        }
        throw new IOException("Invalid marker");
    }

    public final void savePngAttributes(BufferedInputStream bufferedInputStream, BufferedOutputStream bufferedOutputStream) throws IOException {
        Throwable th;
        ByteArrayOutputStream byteArrayOutputStream;
        if (DEBUG) {
            Log.d("ExifInterface", "savePngAttributes starting with (inputStream: " + bufferedInputStream + ", outputStream: " + bufferedOutputStream + ")");
        }
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bufferedInputStream);
        ByteOrder byteOrder = ByteOrder.BIG_ENDIAN;
        ByteOrderedDataOutputStream byteOrderedDataOutputStream = new ByteOrderedDataOutputStream(bufferedOutputStream, byteOrder);
        byte[] bArr = PNG_SIGNATURE;
        ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream, bArr.length);
        int i = this.mOffsetToExifData;
        if (i == 0) {
            int readInt = byteOrderedDataInputStream.readInt();
            byteOrderedDataOutputStream.writeInt(readInt);
            ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream, readInt + 4 + 4);
        } else {
            ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream, ((i - bArr.length) - 4) - 4);
            byteOrderedDataInputStream.skipFully(byteOrderedDataInputStream.readInt() + 4 + 4);
        }
        ByteArrayOutputStream byteArrayOutputStream2 = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream();
        } catch (Throwable th2) {
            th = th2;
        }
        try {
            ByteOrderedDataOutputStream byteOrderedDataOutputStream2 = new ByteOrderedDataOutputStream(byteArrayOutputStream, byteOrder);
            writeExifSegment(byteOrderedDataOutputStream2);
            byte[] byteArray = ((ByteArrayOutputStream) byteOrderedDataOutputStream2.mOutputStream).toByteArray();
            byteOrderedDataOutputStream.write(byteArray);
            CRC32 crc32 = new CRC32();
            crc32.update(byteArray, 4, byteArray.length - 4);
            byteOrderedDataOutputStream.writeInt((int) crc32.getValue());
            ExifInterfaceUtils.closeQuietly(byteArrayOutputStream);
            ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream);
        } catch (Throwable th3) {
            th = th3;
            byteArrayOutputStream2 = byteArrayOutputStream;
            ExifInterfaceUtils.closeQuietly(byteArrayOutputStream2);
            throw th;
        }
    }

    public final void saveWebpAttributes(BufferedInputStream bufferedInputStream, BufferedOutputStream bufferedOutputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream;
        Throwable th;
        Exception e;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        int i6;
        if (DEBUG) {
            Log.d("ExifInterface", "saveWebpAttributes starting with (inputStream: " + bufferedInputStream + ", outputStream: " + bufferedOutputStream + ")");
        }
        ByteOrder byteOrder = ByteOrder.LITTLE_ENDIAN;
        ByteOrderedDataInputStream byteOrderedDataInputStream = new ByteOrderedDataInputStream(bufferedInputStream, byteOrder);
        ByteOrderedDataOutputStream byteOrderedDataOutputStream = new ByteOrderedDataOutputStream(bufferedOutputStream, byteOrder);
        byte[] bArr = WEBP_SIGNATURE_1;
        ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream, bArr.length);
        byte[] bArr2 = WEBP_SIGNATURE_2;
        byteOrderedDataInputStream.skipFully(bArr2.length + 4);
        try {
            try {
                byteArrayOutputStream = new ByteArrayOutputStream();
            } catch (Throwable th2) {
                th = th2;
                byteArrayOutputStream = null;
            }
        } catch (Exception e2) {
            e = e2;
        }
        try {
            ByteOrderedDataOutputStream byteOrderedDataOutputStream2 = new ByteOrderedDataOutputStream(byteArrayOutputStream, byteOrder);
            int i7 = this.mOffsetToExifData;
            if (i7 != 0) {
                ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream2, ((i7 - ((bArr.length + 4) + bArr2.length)) - 4) - 4);
                byteOrderedDataInputStream.skipFully(4);
                byteOrderedDataInputStream.skipFully(byteOrderedDataInputStream.readInt());
                writeExifSegment(byteOrderedDataOutputStream2);
            } else {
                byte[] bArr3 = new byte[4];
                if (byteOrderedDataInputStream.read(bArr3) == 4) {
                    byte[] bArr4 = WEBP_CHUNK_TYPE_VP8X;
                    boolean z = false;
                    if (Arrays.equals(bArr3, bArr4)) {
                        int readInt = byteOrderedDataInputStream.readInt();
                        if (readInt % 2 == 1) {
                            i6 = readInt + 1;
                        } else {
                            i6 = readInt;
                        }
                        byte[] bArr5 = new byte[i6];
                        byteOrderedDataInputStream.read(bArr5);
                        bArr5[0] = (byte) (8 | bArr5[0]);
                        if (((bArr5[0] >> 1) & 1) == 1) {
                            z = true;
                        }
                        byteOrderedDataOutputStream2.write(bArr4);
                        byteOrderedDataOutputStream2.writeInt(readInt);
                        byteOrderedDataOutputStream2.write(bArr5);
                        if (z) {
                            copyChunksUpToGivenChunkType(byteOrderedDataInputStream, byteOrderedDataOutputStream2, WEBP_CHUNK_TYPE_ANIM, null);
                            while (true) {
                                byte[] bArr6 = new byte[4];
                                bufferedInputStream.read(bArr6);
                                if (!Arrays.equals(bArr6, WEBP_CHUNK_TYPE_ANMF)) {
                                    break;
                                }
                                int readInt2 = byteOrderedDataInputStream.readInt();
                                byteOrderedDataOutputStream2.write(bArr6);
                                byteOrderedDataOutputStream2.writeInt(readInt2);
                                if (readInt2 % 2 == 1) {
                                    readInt2++;
                                }
                                ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream2, readInt2);
                            }
                            writeExifSegment(byteOrderedDataOutputStream2);
                        } else {
                            copyChunksUpToGivenChunkType(byteOrderedDataInputStream, byteOrderedDataOutputStream2, WEBP_CHUNK_TYPE_VP8, WEBP_CHUNK_TYPE_VP8L);
                            writeExifSegment(byteOrderedDataOutputStream2);
                        }
                    } else {
                        byte[] bArr7 = WEBP_CHUNK_TYPE_VP8;
                        if (Arrays.equals(bArr3, bArr7) || Arrays.equals(bArr3, WEBP_CHUNK_TYPE_VP8L)) {
                            int readInt3 = byteOrderedDataInputStream.readInt();
                            if (readInt3 % 2 == 1) {
                                i = readInt3 + 1;
                            } else {
                                i = readInt3;
                            }
                            byte[] bArr8 = new byte[3];
                            if (Arrays.equals(bArr3, bArr7)) {
                                byteOrderedDataInputStream.read(bArr8);
                                byte[] bArr9 = new byte[3];
                                if (byteOrderedDataInputStream.read(bArr9) != 3 || !Arrays.equals(WEBP_VP8_SIGNATURE, bArr9)) {
                                    throw new IOException("Encountered error while checking VP8 signature");
                                }
                                i5 = byteOrderedDataInputStream.readInt();
                                i4 = (i5 << 18) >> 18;
                                i3 = (i5 << 2) >> 18;
                                i -= 10;
                                i2 = 0;
                            } else if (!Arrays.equals(bArr3, WEBP_CHUNK_TYPE_VP8L)) {
                                i5 = 0;
                                i4 = 0;
                                i3 = 0;
                                i2 = 0;
                            } else if (byteOrderedDataInputStream.readByte() == 47) {
                                int readInt4 = byteOrderedDataInputStream.readInt();
                                i2 = readInt4 & 8;
                                i -= 5;
                                i3 = ((readInt4 << 4) >> 18) + 1;
                                i5 = readInt4;
                                i4 = ((readInt4 << 18) >> 18) + 1;
                            } else {
                                throw new IOException("Encountered error while checking VP8L signature");
                            }
                            byteOrderedDataOutputStream2.write(bArr4);
                            byteOrderedDataOutputStream2.writeInt(10);
                            byte[] bArr10 = new byte[10];
                            bArr10[0] = (byte) (bArr10[0] | 8);
                            bArr10[0] = (byte) (bArr10[0] | (i2 << 4));
                            int i8 = i4 - 1;
                            int i9 = i3 - 1;
                            bArr10[4] = (byte) i8;
                            bArr10[5] = (byte) (i8 >> 8);
                            bArr10[6] = (byte) (i8 >> 16);
                            bArr10[7] = (byte) i9;
                            bArr10[8] = (byte) (i9 >> 8);
                            bArr10[9] = (byte) (i9 >> 16);
                            byteOrderedDataOutputStream2.write(bArr10);
                            byteOrderedDataOutputStream2.write(bArr3);
                            byteOrderedDataOutputStream2.writeInt(readInt3);
                            if (Arrays.equals(bArr3, bArr7)) {
                                byteOrderedDataOutputStream2.write(bArr8);
                                byteOrderedDataOutputStream2.write(WEBP_VP8_SIGNATURE);
                                byteOrderedDataOutputStream2.writeInt(i5);
                            } else if (Arrays.equals(bArr3, WEBP_CHUNK_TYPE_VP8L)) {
                                byteOrderedDataOutputStream2.write(47);
                                byteOrderedDataOutputStream2.writeInt(i5);
                            }
                            ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream2, i);
                            writeExifSegment(byteOrderedDataOutputStream2);
                        }
                    }
                } else {
                    throw new IOException("Encountered invalid length while parsing WebP chunk type");
                }
            }
            ExifInterfaceUtils.copy(byteOrderedDataInputStream, byteOrderedDataOutputStream2);
            int size = byteArrayOutputStream.size();
            byte[] bArr11 = WEBP_SIGNATURE_2;
            byteOrderedDataOutputStream.writeInt(size + bArr11.length);
            byteOrderedDataOutputStream.write(bArr11);
            byteArrayOutputStream.writeTo(byteOrderedDataOutputStream);
            ExifInterfaceUtils.closeQuietly(byteArrayOutputStream);
        } catch (Exception e3) {
            e = e3;
            throw new IOException("Failed to save WebP file", e);
        } catch (Throwable th3) {
            th = th3;
            ExifInterfaceUtils.closeQuietly(byteArrayOutputStream);
            throw th;
        }
    }

    public final void setAttribute(String str, String str2) {
        ExifTag exifTag;
        int i;
        ExifAttribute exifAttribute;
        int i2;
        String str3;
        Matcher matcher;
        String str4 = str;
        String str5 = str2;
        if (("DateTime".equals(str4) || "DateTimeOriginal".equals(str4) || "DateTimeDigitized".equals(str4)) && str5 != null) {
            boolean find = DATETIME_PRIMARY_FORMAT_PATTERN.matcher(str5).find();
            boolean find2 = DATETIME_SECONDARY_FORMAT_PATTERN.matcher(str5).find();
            if (str2.length() != 19 || (!find && !find2)) {
                Log.w("ExifInterface", "Invalid value for " + str4 + " : " + str5);
                return;
            } else if (find2) {
                str5 = str5.replaceAll("-", ":");
            }
        }
        if ("ISOSpeedRatings".equals(str4)) {
            if (DEBUG) {
                Log.d("ExifInterface", "setAttribute: Replacing TAG_ISO_SPEED_RATINGS with TAG_PHOTOGRAPHIC_SENSITIVITY.");
            }
            str4 = "PhotographicSensitivity";
        }
        int i3 = 2;
        int i4 = 1;
        if (str5 != null && sTagSetForCompatibility.contains(str4)) {
            if (str4.equals("GPSTimeStamp")) {
                if (!GPS_TIMESTAMP_PATTERN.matcher(str5).find()) {
                    Log.w("ExifInterface", "Invalid value for " + str4 + " : " + str5);
                    return;
                }
                str5 = Integer.parseInt(matcher.group(1)) + "/1," + Integer.parseInt(matcher.group(2)) + "/1," + Integer.parseInt(matcher.group(3)) + "/1";
            } else {
                try {
                    str5 = ((long) (Double.parseDouble(str5) * 10000.0d)) + "/10000";
                } catch (NumberFormatException unused) {
                    Log.w("ExifInterface", "Invalid value for " + str4 + " : " + str5);
                    return;
                }
            }
        }
        int i5 = 0;
        int i6 = 0;
        while (i5 < EXIF_TAGS.length) {
            if ((i5 != 4 || this.mHasThumbnail) && (exifTag = sExifTagMapsForWriting[i5].get(str4)) != null) {
                if (str5 != null) {
                    Pair<Integer, Integer> guessDataFormat = guessDataFormat(str5);
                    int i7 = -1;
                    if (exifTag.primaryFormat == ((Integer) guessDataFormat.first).intValue() || exifTag.primaryFormat == ((Integer) guessDataFormat.second).intValue()) {
                        i = exifTag.primaryFormat;
                    } else {
                        int i8 = exifTag.secondaryFormat;
                        if (i8 == -1 || !(i8 == ((Integer) guessDataFormat.first).intValue() || exifTag.secondaryFormat == ((Integer) guessDataFormat.second).intValue())) {
                            int i9 = exifTag.primaryFormat;
                            if (i9 == i4 || i9 == 7 || i9 == i3) {
                                i = i9;
                            } else if (DEBUG) {
                                StringBuilder m = ActivityResultRegistry$3$$ExternalSyntheticOutline0.m("Given tag (", str4, ") value didn't match with one of expected formats: ");
                                String[] strArr = IFD_FORMAT_NAMES;
                                m.append(strArr[exifTag.primaryFormat]);
                                String str6 = "";
                                if (exifTag.secondaryFormat == -1) {
                                    str3 = str6;
                                } else {
                                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m(", ");
                                    m2.append(strArr[exifTag.secondaryFormat]);
                                    str3 = m2.toString();
                                }
                                m.append(str3);
                                m.append(" (guess: ");
                                m.append(strArr[((Integer) guessDataFormat.first).intValue()]);
                                if (((Integer) guessDataFormat.second).intValue() != -1) {
                                    StringBuilder m3 = VendorAtomValue$$ExternalSyntheticOutline1.m(", ");
                                    m3.append(strArr[((Integer) guessDataFormat.second).intValue()]);
                                    str6 = m3.toString();
                                }
                                m.append(str6);
                                m.append(")");
                                Log.d("ExifInterface", m.toString());
                            }
                        } else {
                            i = exifTag.secondaryFormat;
                        }
                    }
                    switch (i) {
                        case 1:
                            HashMap<String, ExifAttribute> hashMap = this.mAttributes[i5];
                            i4 = 1;
                            if (str5.length() == 1) {
                                i2 = 0;
                                if (str5.charAt(0) >= '0' && str5.charAt(0) <= '1') {
                                    exifAttribute = new ExifAttribute(1, 1, new byte[]{(byte) (str5.charAt(0) - '0')});
                                    hashMap.put(str4, exifAttribute);
                                    i6 = i2;
                                    break;
                                }
                            } else {
                                i2 = 0;
                            }
                            byte[] bytes = str5.getBytes(ASCII);
                            exifAttribute = new ExifAttribute(1, bytes.length, bytes);
                            hashMap.put(str4, exifAttribute);
                            i6 = i2;
                            break;
                        case 2:
                        case 7:
                            this.mAttributes[i5].put(str4, ExifAttribute.createString(str5));
                            i6 = 0;
                            i4 = 1;
                            break;
                        case 3:
                            String[] split = str5.split(",", -1);
                            int[] iArr = new int[split.length];
                            for (int i10 = 0; i10 < split.length; i10++) {
                                iArr[i10] = Integer.parseInt(split[i10]);
                            }
                            this.mAttributes[i5].put(str4, ExifAttribute.createUShort(iArr, this.mExifByteOrder));
                            i6 = 0;
                            i4 = 1;
                            break;
                        case 4:
                            String[] split2 = str5.split(",", -1);
                            long[] jArr = new long[split2.length];
                            for (int i11 = 0; i11 < split2.length; i11++) {
                                jArr[i11] = Long.parseLong(split2[i11]);
                            }
                            this.mAttributes[i5].put(str4, ExifAttribute.createULong(jArr, this.mExifByteOrder));
                            i6 = 0;
                            i4 = 1;
                            break;
                        case 5:
                            String[] split3 = str5.split(",", -1);
                            Rational[] rationalArr = new Rational[split3.length];
                            for (int i12 = 0; i12 < split3.length; i12++) {
                                String[] split4 = split3[i12].split("/", -1);
                                rationalArr[i12] = new Rational((long) Double.parseDouble(split4[0]), (long) Double.parseDouble(split4[1]));
                            }
                            this.mAttributes[i5].put(str4, ExifAttribute.createURational(rationalArr, this.mExifByteOrder));
                            i6 = 0;
                            i4 = 1;
                            break;
                        case FalsingManager.VERSION /* 6 */:
                        case 8:
                        case QSTileImpl.H.STALE /* 11 */:
                        default:
                            if (DEBUG) {
                                ExifInterface$$ExternalSyntheticOutline1.m("Data format isn't one of expected formats: ", i, "ExifInterface");
                                break;
                            } else {
                                break;
                            }
                        case 9:
                            String[] split5 = str5.split(",", -1);
                            int length = split5.length;
                            int[] iArr2 = new int[length];
                            for (int i13 = 0; i13 < split5.length; i13++) {
                                iArr2[i13] = Integer.parseInt(split5[i13]);
                            }
                            HashMap<String, ExifAttribute> hashMap2 = this.mAttributes[i5];
                            ByteOrder byteOrder = this.mExifByteOrder;
                            ByteBuffer wrap = ByteBuffer.wrap(new byte[IFD_FORMAT_BYTES_PER_FORMAT[9] * length]);
                            wrap.order(byteOrder);
                            for (int i14 = 0; i14 < length; i14++) {
                                wrap.putInt(iArr2[i14]);
                            }
                            hashMap2.put(str4, new ExifAttribute(9, length, wrap.array()));
                            i6 = 0;
                            i4 = 1;
                            break;
                        case 10:
                            String[] split6 = str5.split(",", -1);
                            int length2 = split6.length;
                            Rational[] rationalArr2 = new Rational[length2];
                            int i15 = i4;
                            int i16 = i6;
                            while (i6 < split6.length) {
                                String[] split7 = split6[i6].split("/", i7);
                                rationalArr2[i6] = new Rational((long) Double.parseDouble(split7[i16]), (long) Double.parseDouble(split7[i15]));
                                i6++;
                                i16 = 0;
                                i15 = 1;
                                i7 = -1;
                            }
                            HashMap<String, ExifAttribute> hashMap3 = this.mAttributes[i5];
                            ByteOrder byteOrder2 = this.mExifByteOrder;
                            ByteBuffer wrap2 = ByteBuffer.wrap(new byte[IFD_FORMAT_BYTES_PER_FORMAT[10] * length2]);
                            wrap2.order(byteOrder2);
                            for (int i17 = 0; i17 < length2; i17++) {
                                Rational rational = rationalArr2[i17];
                                wrap2.putInt((int) rational.numerator);
                                wrap2.putInt((int) rational.denominator);
                            }
                            hashMap3.put(str4, new ExifAttribute(10, length2, wrap2.array()));
                            i6 = 0;
                            i4 = 1;
                            break;
                        case KeyguardSliceProvider.ALARM_VISIBILITY_HOURS /* 12 */:
                            String[] split8 = str5.split(",", -1);
                            int length3 = split8.length;
                            double[] dArr = new double[length3];
                            for (int i18 = i6; i18 < split8.length; i18++) {
                                dArr[i18] = Double.parseDouble(split8[i18]);
                            }
                            HashMap<String, ExifAttribute> hashMap4 = this.mAttributes[i5];
                            ByteOrder byteOrder3 = this.mExifByteOrder;
                            ByteBuffer wrap3 = ByteBuffer.wrap(new byte[IFD_FORMAT_BYTES_PER_FORMAT[12] * length3]);
                            wrap3.order(byteOrder3);
                            for (int i19 = i6; i19 < length3; i19++) {
                                wrap3.putDouble(dArr[i19]);
                            }
                            hashMap4.put(str4, new ExifAttribute(12, length3, wrap3.array()));
                            break;
                    }
                } else {
                    this.mAttributes[i5].remove(str4);
                }
            }
            i5++;
            i3 = 2;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:31:0x007e  */
    /* JADX WARN: Removed duplicated region for block: B:78:? A[RETURN, SYNTHETIC] */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct add '--show-bad-code' argument
    */
    public final void setThumbnailData(androidx.exifinterface.media.ExifInterface.ByteOrderedDataInputStream r19) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 361
            To view this dump add '--comments-level debug' option
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.exifinterface.media.ExifInterface.setThumbnailData(androidx.exifinterface.media.ExifInterface$ByteOrderedDataInputStream):void");
    }

    public final void swapBasedOnImageSize(int i, int i2) throws IOException {
        if (!this.mAttributes[i].isEmpty() && !this.mAttributes[i2].isEmpty()) {
            ExifAttribute exifAttribute = this.mAttributes[i].get("ImageLength");
            ExifAttribute exifAttribute2 = this.mAttributes[i].get("ImageWidth");
            ExifAttribute exifAttribute3 = this.mAttributes[i2].get("ImageLength");
            ExifAttribute exifAttribute4 = this.mAttributes[i2].get("ImageWidth");
            if (exifAttribute == null || exifAttribute2 == null) {
                if (DEBUG) {
                    Log.d("ExifInterface", "First image does not contain valid size information");
                }
            } else if (exifAttribute3 != null && exifAttribute4 != null) {
                int intValue = exifAttribute.getIntValue(this.mExifByteOrder);
                int intValue2 = exifAttribute2.getIntValue(this.mExifByteOrder);
                int intValue3 = exifAttribute3.getIntValue(this.mExifByteOrder);
                int intValue4 = exifAttribute4.getIntValue(this.mExifByteOrder);
                if (intValue < intValue3 && intValue2 < intValue4) {
                    HashMap<String, ExifAttribute>[] hashMapArr = this.mAttributes;
                    HashMap<String, ExifAttribute> hashMap = hashMapArr[i];
                    hashMapArr[i] = hashMapArr[i2];
                    hashMapArr[i2] = hashMap;
                }
            } else if (DEBUG) {
                Log.d("ExifInterface", "Second image does not contain valid size information");
            }
        } else if (DEBUG) {
            Log.d("ExifInterface", "Cannot perform swap since only one image data exists");
        }
    }

    public final void updateImageSizeValues(SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream, int i) throws IOException {
        ExifAttribute exifAttribute;
        ExifAttribute exifAttribute2;
        ExifAttribute exifAttribute3 = this.mAttributes[i].get("DefaultCropSize");
        ExifAttribute exifAttribute4 = this.mAttributes[i].get("SensorTopBorder");
        ExifAttribute exifAttribute5 = this.mAttributes[i].get("SensorLeftBorder");
        ExifAttribute exifAttribute6 = this.mAttributes[i].get("SensorBottomBorder");
        ExifAttribute exifAttribute7 = this.mAttributes[i].get("SensorRightBorder");
        if (exifAttribute3 != null) {
            if (exifAttribute3.format == 5) {
                Rational[] rationalArr = (Rational[]) exifAttribute3.getValue(this.mExifByteOrder);
                if (rationalArr == null || rationalArr.length != 2) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid crop size values. cropSize=");
                    m.append(Arrays.toString(rationalArr));
                    Log.w("ExifInterface", m.toString());
                    return;
                }
                exifAttribute2 = ExifAttribute.createURational(new Rational[]{rationalArr[0]}, this.mExifByteOrder);
                exifAttribute = ExifAttribute.createURational(new Rational[]{rationalArr[1]}, this.mExifByteOrder);
            } else {
                int[] iArr = (int[]) exifAttribute3.getValue(this.mExifByteOrder);
                if (iArr == null || iArr.length != 2) {
                    StringBuilder m2 = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid crop size values. cropSize=");
                    m2.append(Arrays.toString(iArr));
                    Log.w("ExifInterface", m2.toString());
                    return;
                }
                exifAttribute2 = ExifAttribute.createUShort(iArr[0], this.mExifByteOrder);
                exifAttribute = ExifAttribute.createUShort(iArr[1], this.mExifByteOrder);
            }
            this.mAttributes[i].put("ImageWidth", exifAttribute2);
            this.mAttributes[i].put("ImageLength", exifAttribute);
        } else if (exifAttribute4 == null || exifAttribute5 == null || exifAttribute6 == null || exifAttribute7 == null) {
            ExifAttribute exifAttribute8 = this.mAttributes[i].get("ImageLength");
            ExifAttribute exifAttribute9 = this.mAttributes[i].get("ImageWidth");
            if (exifAttribute8 == null || exifAttribute9 == null) {
                ExifAttribute exifAttribute10 = this.mAttributes[i].get("JPEGInterchangeFormat");
                ExifAttribute exifAttribute11 = this.mAttributes[i].get("JPEGInterchangeFormatLength");
                if (exifAttribute10 != null && exifAttribute11 != null) {
                    int intValue = exifAttribute10.getIntValue(this.mExifByteOrder);
                    int intValue2 = exifAttribute10.getIntValue(this.mExifByteOrder);
                    seekableByteOrderedDataInputStream.seek(intValue);
                    byte[] bArr = new byte[intValue2];
                    seekableByteOrderedDataInputStream.read(bArr);
                    getJpegAttributes(new ByteOrderedDataInputStream(bArr), intValue, i);
                }
            }
        } else {
            int intValue3 = exifAttribute4.getIntValue(this.mExifByteOrder);
            int intValue4 = exifAttribute6.getIntValue(this.mExifByteOrder);
            int intValue5 = exifAttribute7.getIntValue(this.mExifByteOrder);
            int intValue6 = exifAttribute5.getIntValue(this.mExifByteOrder);
            if (intValue4 > intValue3 && intValue5 > intValue6) {
                ExifAttribute createUShort = ExifAttribute.createUShort(intValue4 - intValue3, this.mExifByteOrder);
                ExifAttribute createUShort2 = ExifAttribute.createUShort(intValue5 - intValue6, this.mExifByteOrder);
                this.mAttributes[i].put("ImageLength", createUShort);
                this.mAttributes[i].put("ImageWidth", createUShort2);
            }
        }
    }

    public final int writeExifSegment(ByteOrderedDataOutputStream byteOrderedDataOutputStream) throws IOException {
        short s;
        ExifTag[][] exifTagArr = EXIF_TAGS;
        int[] iArr = new int[exifTagArr.length];
        int[] iArr2 = new int[exifTagArr.length];
        for (ExifTag exifTag : EXIF_POINTER_TAGS) {
            removeAttribute(exifTag.name);
        }
        if (this.mHasThumbnail) {
            if (this.mHasThumbnailStrips) {
                removeAttribute("StripOffsets");
                removeAttribute("StripByteCounts");
            } else {
                removeAttribute("JPEGInterchangeFormat");
                removeAttribute("JPEGInterchangeFormatLength");
            }
        }
        for (int i = 0; i < EXIF_TAGS.length; i++) {
            for (Object obj : this.mAttributes[i].entrySet().toArray()) {
                Map.Entry entry = (Map.Entry) obj;
                if (entry.getValue() == null) {
                    this.mAttributes[i].remove(entry.getKey());
                }
            }
        }
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong(0L, this.mExifByteOrder));
        }
        if (this.mHasThumbnail) {
            if (this.mHasThumbnailStrips) {
                this.mAttributes[4].put("StripOffsets", ExifAttribute.createUShort(0, this.mExifByteOrder));
                this.mAttributes[4].put("StripByteCounts", ExifAttribute.createUShort(this.mThumbnailLength, this.mExifByteOrder));
            } else {
                this.mAttributes[4].put("JPEGInterchangeFormat", ExifAttribute.createULong(0L, this.mExifByteOrder));
                this.mAttributes[4].put("JPEGInterchangeFormatLength", ExifAttribute.createULong(this.mThumbnailLength, this.mExifByteOrder));
            }
        }
        for (int i2 = 0; i2 < EXIF_TAGS.length; i2++) {
            int i3 = 0;
            for (Map.Entry<String, ExifAttribute> entry2 : this.mAttributes[i2].entrySet()) {
                ExifAttribute value = entry2.getValue();
                Objects.requireNonNull(value);
                int i4 = IFD_FORMAT_BYTES_PER_FORMAT[value.format] * value.numberOfComponents;
                if (i4 > 4) {
                    i3 += i4;
                }
            }
            iArr2[i2] = iArr2[i2] + i3;
        }
        int i5 = 8;
        for (int i6 = 0; i6 < EXIF_TAGS.length; i6++) {
            if (!this.mAttributes[i6].isEmpty()) {
                iArr[i6] = i5;
                i5 = (this.mAttributes[i6].size() * 12) + 2 + 4 + iArr2[i6] + i5;
            }
        }
        if (this.mHasThumbnail) {
            if (this.mHasThumbnailStrips) {
                this.mAttributes[4].put("StripOffsets", ExifAttribute.createUShort(i5, this.mExifByteOrder));
            } else {
                this.mAttributes[4].put("JPEGInterchangeFormat", ExifAttribute.createULong(i5, this.mExifByteOrder));
            }
            this.mThumbnailOffset = i5;
            i5 += this.mThumbnailLength;
        }
        if (this.mMimeType == 4) {
            i5 += 8;
        }
        if (DEBUG) {
            for (int i7 = 0; i7 < EXIF_TAGS.length; i7++) {
                Log.d("ExifInterface", String.format("index: %d, offsets: %d, tag count: %d, data sizes: %d, total size: %d", Integer.valueOf(i7), Integer.valueOf(iArr[i7]), Integer.valueOf(this.mAttributes[i7].size()), Integer.valueOf(iArr2[i7]), Integer.valueOf(i5)));
            }
        }
        if (!this.mAttributes[1].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[1].name, ExifAttribute.createULong(iArr[1], this.mExifByteOrder));
        }
        if (!this.mAttributes[2].isEmpty()) {
            this.mAttributes[0].put(EXIF_POINTER_TAGS[2].name, ExifAttribute.createULong(iArr[2], this.mExifByteOrder));
        }
        if (!this.mAttributes[3].isEmpty()) {
            this.mAttributes[1].put(EXIF_POINTER_TAGS[3].name, ExifAttribute.createULong(iArr[3], this.mExifByteOrder));
        }
        int i8 = this.mMimeType;
        if (i8 == 4) {
            byteOrderedDataOutputStream.writeShort((short) i5);
            byteOrderedDataOutputStream.write(IDENTIFIER_EXIF_APP1);
        } else if (i8 == 13) {
            byteOrderedDataOutputStream.writeInt(i5);
            byteOrderedDataOutputStream.write(PNG_CHUNK_TYPE_EXIF);
        } else if (i8 == 14) {
            byteOrderedDataOutputStream.write(WEBP_CHUNK_TYPE_EXIF);
            byteOrderedDataOutputStream.writeInt(i5);
        }
        if (this.mExifByteOrder == ByteOrder.BIG_ENDIAN) {
            s = 19789;
        } else {
            s = 18761;
        }
        byteOrderedDataOutputStream.writeShort(s);
        byteOrderedDataOutputStream.mByteOrder = this.mExifByteOrder;
        byteOrderedDataOutputStream.writeShort((short) 42);
        byteOrderedDataOutputStream.writeInt((int) 8);
        for (int i9 = 0; i9 < EXIF_TAGS.length; i9++) {
            if (!this.mAttributes[i9].isEmpty()) {
                byteOrderedDataOutputStream.writeShort((short) this.mAttributes[i9].size());
                int size = (this.mAttributes[i9].size() * 12) + iArr[i9] + 2 + 4;
                for (Map.Entry<String, ExifAttribute> entry3 : this.mAttributes[i9].entrySet()) {
                    int i10 = sExifTagMapsForWriting[i9].get(entry3.getKey()).number;
                    ExifAttribute value2 = entry3.getValue();
                    Objects.requireNonNull(value2);
                    int i11 = IFD_FORMAT_BYTES_PER_FORMAT[value2.format] * value2.numberOfComponents;
                    byteOrderedDataOutputStream.writeShort((short) i10);
                    byteOrderedDataOutputStream.writeShort((short) value2.format);
                    byteOrderedDataOutputStream.writeInt(value2.numberOfComponents);
                    if (i11 > 4) {
                        byteOrderedDataOutputStream.writeInt(size);
                        size += i11;
                    } else {
                        byteOrderedDataOutputStream.write(value2.bytes);
                        if (i11 < 4) {
                            while (i11 < 4) {
                                byteOrderedDataOutputStream.writeByte(0);
                                i11++;
                            }
                        }
                    }
                }
                if (i9 != 0 || this.mAttributes[4].isEmpty()) {
                    byteOrderedDataOutputStream.writeInt((int) 0);
                } else {
                    byteOrderedDataOutputStream.writeInt(iArr[4]);
                }
                for (Map.Entry<String, ExifAttribute> entry4 : this.mAttributes[i9].entrySet()) {
                    byte[] bArr = entry4.getValue().bytes;
                    if (bArr.length > 4) {
                        byteOrderedDataOutputStream.write(bArr, 0, bArr.length);
                    }
                }
            }
        }
        if (this.mHasThumbnail) {
            byteOrderedDataOutputStream.write(getThumbnailBytes());
        }
        if (this.mMimeType == 14 && i5 % 2 == 1) {
            byteOrderedDataOutputStream.writeByte(0);
        }
        byteOrderedDataOutputStream.mByteOrder = ByteOrder.BIG_ENDIAN;
        return i5;
    }

    public ExifInterface(FileDescriptor fileDescriptor) throws IOException {
        boolean z;
        Throwable th;
        ExifTag[][] exifTagArr = EXIF_TAGS;
        this.mAttributes = new HashMap[exifTagArr.length];
        this.mAttributesOffsets = new HashSet(exifTagArr.length);
        Objects.requireNonNull(fileDescriptor, "fileDescriptor cannot be null");
        boolean z2 = true;
        try {
            Os.lseek(fileDescriptor, 0L, OsConstants.SEEK_CUR);
            z = true;
        } catch (Exception unused) {
            if (DEBUG) {
                Log.d("ExifInterface", "The file descriptor for the given input is not seekable");
            }
            z = false;
        }
        FileInputStream fileInputStream = null;
        if (z) {
            this.mSeekableFileDescriptor = fileDescriptor;
            try {
                fileDescriptor = Os.dup(fileDescriptor);
            } catch (Exception e) {
                throw new IOException("Failed to duplicate file descriptor", e);
            }
        } else {
            this.mSeekableFileDescriptor = null;
            z2 = false;
        }
        try {
            FileInputStream fileInputStream2 = new FileInputStream(fileDescriptor);
            try {
                loadAttributes(fileInputStream2);
                ExifInterfaceUtils.closeQuietly(fileInputStream2);
                if (z2) {
                    ExifInterfaceUtils.closeFileDescriptor(fileDescriptor);
                }
            } catch (Throwable th2) {
                th = th2;
                fileInputStream = fileInputStream2;
                ExifInterfaceUtils.closeQuietly(fileInputStream);
                if (z2) {
                    ExifInterfaceUtils.closeFileDescriptor(fileDescriptor);
                }
                throw th;
            }
        } catch (Throwable th3) {
            th = th3;
        }
    }

    public static ByteOrder readByteOrder(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        short readShort = byteOrderedDataInputStream.readShort();
        if (readShort == 18761) {
            if (DEBUG) {
                Log.d("ExifInterface", "readExifSegment: Byte Align II");
            }
            return ByteOrder.LITTLE_ENDIAN;
        } else if (readShort == 19789) {
            if (DEBUG) {
                Log.d("ExifInterface", "readExifSegment: Byte Align MM");
            }
            return ByteOrder.BIG_ENDIAN;
        } else {
            StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid byte order: ");
            m.append(Integer.toHexString(readShort));
            throw new IOException(m.toString());
        }
    }

    public final void getHeifAttributes(final SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream) throws IOException {
        String str;
        String str2;
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            try {
                mediaMetadataRetriever.setDataSource(new MediaDataSource() { // from class: androidx.exifinterface.media.ExifInterface.1
                    public long mPosition;

                    @Override // java.io.Closeable, java.lang.AutoCloseable
                    public final void close() throws IOException {
                    }

                    @Override // android.media.MediaDataSource
                    public final long getSize() throws IOException {
                        return -1L;
                    }

                    @Override // android.media.MediaDataSource
                    public final int readAt(long j, byte[] bArr, int i, int i2) throws IOException {
                        if (i2 == 0) {
                            return 0;
                        }
                        if (j < 0) {
                            return -1;
                        }
                        try {
                            long j2 = this.mPosition;
                            if (j2 != j) {
                                if (j2 >= 0 && j >= j2 + SeekableByteOrderedDataInputStream.this.available()) {
                                    return -1;
                                }
                                SeekableByteOrderedDataInputStream.this.seek(j);
                                this.mPosition = j;
                            }
                            if (i2 > SeekableByteOrderedDataInputStream.this.available()) {
                                i2 = SeekableByteOrderedDataInputStream.this.available();
                            }
                            int read = SeekableByteOrderedDataInputStream.this.read(bArr, i, i2);
                            if (read >= 0) {
                                this.mPosition += read;
                                return read;
                            }
                        } catch (IOException unused) {
                        }
                        this.mPosition = -1L;
                        return -1;
                    }
                });
                String extractMetadata = mediaMetadataRetriever.extractMetadata(33);
                String extractMetadata2 = mediaMetadataRetriever.extractMetadata(34);
                String extractMetadata3 = mediaMetadataRetriever.extractMetadata(26);
                String extractMetadata4 = mediaMetadataRetriever.extractMetadata(17);
                String str3 = null;
                if ("yes".equals(extractMetadata3)) {
                    str3 = mediaMetadataRetriever.extractMetadata(29);
                    str2 = mediaMetadataRetriever.extractMetadata(30);
                    str = mediaMetadataRetriever.extractMetadata(31);
                } else if ("yes".equals(extractMetadata4)) {
                    str3 = mediaMetadataRetriever.extractMetadata(18);
                    str2 = mediaMetadataRetriever.extractMetadata(19);
                    str = mediaMetadataRetriever.extractMetadata(24);
                } else {
                    str2 = null;
                    str = null;
                }
                if (str3 != null) {
                    this.mAttributes[0].put("ImageWidth", ExifAttribute.createUShort(Integer.parseInt(str3), this.mExifByteOrder));
                }
                if (str2 != null) {
                    this.mAttributes[0].put("ImageLength", ExifAttribute.createUShort(Integer.parseInt(str2), this.mExifByteOrder));
                }
                if (str != null) {
                    int i = 1;
                    int parseInt = Integer.parseInt(str);
                    if (parseInt == 90) {
                        i = 6;
                    } else if (parseInt == 180) {
                        i = 3;
                    } else if (parseInt == 270) {
                        i = 8;
                    }
                    this.mAttributes[0].put("Orientation", ExifAttribute.createUShort(i, this.mExifByteOrder));
                }
                if (!(extractMetadata == null || extractMetadata2 == null)) {
                    int parseInt2 = Integer.parseInt(extractMetadata);
                    int parseInt3 = Integer.parseInt(extractMetadata2);
                    if (parseInt3 > 6) {
                        seekableByteOrderedDataInputStream.seek(parseInt2);
                        byte[] bArr = new byte[6];
                        if (seekableByteOrderedDataInputStream.read(bArr) == 6) {
                            int i2 = parseInt2 + 6;
                            int i3 = parseInt3 - 6;
                            if (Arrays.equals(bArr, IDENTIFIER_EXIF_APP1)) {
                                byte[] bArr2 = new byte[i3];
                                if (seekableByteOrderedDataInputStream.read(bArr2) == i3) {
                                    this.mOffsetToExifData = i2;
                                    readExifSegment(bArr2, 0);
                                } else {
                                    throw new IOException("Can't read exif");
                                }
                            } else {
                                throw new IOException("Invalid identifier");
                            }
                        } else {
                            throw new IOException("Can't read identifier");
                        }
                    } else {
                        throw new IOException("Invalid exif length");
                    }
                }
                if (DEBUG) {
                    Log.d("ExifInterface", "Heif meta: " + str3 + "x" + str2 + ", rotation " + str);
                }
            } catch (RuntimeException unused) {
                throw new UnsupportedOperationException("Failed to read EXIF from HEIF file. Given stream is either malformed or unsupported.");
            }
        } finally {
            mediaMetadataRetriever.release();
        }
    }

    public final void getOrfAttributes(SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream) throws IOException {
        getRawAttributes(seekableByteOrderedDataInputStream);
        ExifAttribute exifAttribute = this.mAttributes[1].get("MakerNote");
        if (exifAttribute != null) {
            SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream2 = new SeekableByteOrderedDataInputStream(exifAttribute.bytes);
            seekableByteOrderedDataInputStream2.mByteOrder = this.mExifByteOrder;
            byte[] bArr = ORF_MAKER_NOTE_HEADER_1;
            byte[] bArr2 = new byte[bArr.length];
            seekableByteOrderedDataInputStream2.readFully(bArr2);
            seekableByteOrderedDataInputStream2.seek(0L);
            byte[] bArr3 = ORF_MAKER_NOTE_HEADER_2;
            byte[] bArr4 = new byte[bArr3.length];
            seekableByteOrderedDataInputStream2.readFully(bArr4);
            if (Arrays.equals(bArr2, bArr)) {
                seekableByteOrderedDataInputStream2.seek(8L);
            } else if (Arrays.equals(bArr4, bArr3)) {
                seekableByteOrderedDataInputStream2.seek(12L);
            }
            readImageFileDirectory(seekableByteOrderedDataInputStream2, 6);
            ExifAttribute exifAttribute2 = this.mAttributes[7].get("PreviewImageStart");
            ExifAttribute exifAttribute3 = this.mAttributes[7].get("PreviewImageLength");
            if (!(exifAttribute2 == null || exifAttribute3 == null)) {
                this.mAttributes[5].put("JPEGInterchangeFormat", exifAttribute2);
                this.mAttributes[5].put("JPEGInterchangeFormatLength", exifAttribute3);
            }
            ExifAttribute exifAttribute4 = this.mAttributes[8].get("AspectFrame");
            if (exifAttribute4 != null) {
                int[] iArr = (int[]) exifAttribute4.getValue(this.mExifByteOrder);
                if (iArr == null || iArr.length != 4) {
                    StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid aspect frame values. frame=");
                    m.append(Arrays.toString(iArr));
                    Log.w("ExifInterface", m.toString());
                } else if (iArr[2] > iArr[0] && iArr[3] > iArr[1]) {
                    int i = (iArr[2] - iArr[0]) + 1;
                    int i2 = (iArr[3] - iArr[1]) + 1;
                    if (i < i2) {
                        int i3 = i + i2;
                        i2 = i3 - i2;
                        i = i3 - i2;
                    }
                    ExifAttribute createUShort = ExifAttribute.createUShort(i, this.mExifByteOrder);
                    ExifAttribute createUShort2 = ExifAttribute.createUShort(i2, this.mExifByteOrder);
                    this.mAttributes[0].put("ImageWidth", createUShort);
                    this.mAttributes[0].put("ImageLength", createUShort2);
                }
            }
        }
    }

    public final void getRawAttributes(SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream) throws IOException {
        ExifAttribute exifAttribute;
        parseTiffHeaders(seekableByteOrderedDataInputStream);
        readImageFileDirectory(seekableByteOrderedDataInputStream, 0);
        updateImageSizeValues(seekableByteOrderedDataInputStream, 0);
        updateImageSizeValues(seekableByteOrderedDataInputStream, 5);
        updateImageSizeValues(seekableByteOrderedDataInputStream, 4);
        validateImages();
        if (this.mMimeType == 8 && (exifAttribute = this.mAttributes[1].get("MakerNote")) != null) {
            SeekableByteOrderedDataInputStream seekableByteOrderedDataInputStream2 = new SeekableByteOrderedDataInputStream(exifAttribute.bytes);
            seekableByteOrderedDataInputStream2.mByteOrder = this.mExifByteOrder;
            seekableByteOrderedDataInputStream2.skipFully(6);
            readImageFileDirectory(seekableByteOrderedDataInputStream2, 9);
            ExifAttribute exifAttribute2 = this.mAttributes[9].get("ColorSpace");
            if (exifAttribute2 != null) {
                this.mAttributes[1].put("ColorSpace", exifAttribute2);
            }
        }
    }

    public final void parseTiffHeaders(ByteOrderedDataInputStream byteOrderedDataInputStream) throws IOException {
        ByteOrder readByteOrder = readByteOrder(byteOrderedDataInputStream);
        this.mExifByteOrder = readByteOrder;
        byteOrderedDataInputStream.mByteOrder = readByteOrder;
        int readUnsignedShort = byteOrderedDataInputStream.readUnsignedShort();
        int i = this.mMimeType;
        if (i == 7 || i == 10 || readUnsignedShort == 42) {
            int readInt = byteOrderedDataInputStream.readInt();
            if (readInt >= 8) {
                int i2 = readInt - 8;
                if (i2 > 0) {
                    byteOrderedDataInputStream.skipFully(i2);
                    return;
                }
                return;
            }
            throw new IOException(VendorAtomValue$$ExternalSyntheticOutline0.m("Invalid first Ifd offset: ", readInt));
        }
        StringBuilder m = VendorAtomValue$$ExternalSyntheticOutline1.m("Invalid start code: ");
        m.append(Integer.toHexString(readUnsignedShort));
        throw new IOException(m.toString());
    }

    /* loaded from: classes.dex */
    public static class ExifTag {
        public final String name;
        public final int number;
        public final int primaryFormat;
        public final int secondaryFormat;

        public ExifTag(String str, int i, int i2) {
            this.name = str;
            this.number = i;
            this.primaryFormat = i2;
            this.secondaryFormat = -1;
        }

        public ExifTag(String str, int i, int i2, int i3) {
            this.name = str;
            this.number = i;
            this.primaryFormat = i2;
            this.secondaryFormat = i3;
        }
    }
}
