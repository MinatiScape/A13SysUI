package com.google.protobuf.nano;

import android.frameworks.stats.VendorAtomValue$$ExternalSyntheticOutline0;
import androidx.exifinterface.media.ExifInterface$$ExternalSyntheticOutline0;
import java.io.IOException;
import java.util.Objects;
/* loaded from: classes.dex */
public final class WireFormatNano {
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final byte[] EMPTY_BYTES = new byte[0];

    public static final int getRepeatedFieldArrayLength(CodedInputByteBufferNano codedInputByteBufferNano, int i) throws IOException {
        Objects.requireNonNull(codedInputByteBufferNano);
        int i2 = codedInputByteBufferNano.bufferPos - codedInputByteBufferNano.bufferStart;
        codedInputByteBufferNano.skipField(i);
        int i3 = 1;
        while (codedInputByteBufferNano.readTag() == i) {
            codedInputByteBufferNano.skipField(i);
            i3++;
        }
        int i4 = codedInputByteBufferNano.bufferPos;
        int i5 = codedInputByteBufferNano.bufferStart;
        if (i2 > i4 - i5) {
            StringBuilder m = ExifInterface$$ExternalSyntheticOutline0.m("Position ", i2, " is beyond current ");
            m.append(codedInputByteBufferNano.bufferPos - codedInputByteBufferNano.bufferStart);
            throw new IllegalArgumentException(m.toString());
        } else if (i2 >= 0) {
            codedInputByteBufferNano.bufferPos = i5 + i2;
            return i3;
        } else {
            throw new IllegalArgumentException(VendorAtomValue$$ExternalSyntheticOutline0.m("Bad position ", i2));
        }
    }
}
