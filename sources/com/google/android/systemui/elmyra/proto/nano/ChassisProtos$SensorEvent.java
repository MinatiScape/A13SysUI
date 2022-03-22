package com.google.android.systemui.elmyra.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ChassisProtos$SensorEvent extends MessageNano {
    public long timestamp = 0;
    public float[] values = WireFormatNano.EMPTY_FLOAT_ARRAY;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        long j = this.timestamp;
        int i = 0;
        if (j != 0) {
            i = 0 + CodedOutputByteBufferNano.computeRawVarint64Size(j) + CodedOutputByteBufferNano.computeTagSize(1);
        }
        float[] fArr = this.values;
        if (fArr == null || fArr.length <= 0) {
            return i;
        }
        return (fArr.length * 1) + (fArr.length * 4) + i;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        long j = this.timestamp;
        int i = 0;
        if (j != 0) {
            Objects.requireNonNull(codedOutputByteBufferNano);
            codedOutputByteBufferNano.writeTag(1, 0);
            codedOutputByteBufferNano.writeRawVarint64(j);
        }
        float[] fArr = this.values;
        if (fArr != null && fArr.length > 0) {
            while (true) {
                float[] fArr2 = this.values;
                if (i < fArr2.length) {
                    codedOutputByteBufferNano.writeFloat(2, fArr2[i]);
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public ChassisProtos$SensorEvent() {
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        int i;
        int i2;
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 8) {
                this.timestamp = codedInputByteBufferNano.readRawVarint64();
            } else if (readTag == 18) {
                int readRawVarint32 = codedInputByteBufferNano.readRawVarint32();
                int pushLimit = codedInputByteBufferNano.pushLimit(readRawVarint32);
                int i3 = readRawVarint32 / 4;
                float[] fArr = this.values;
                if (fArr == null) {
                    i2 = 0;
                } else {
                    i2 = fArr.length;
                }
                int i4 = i3 + i2;
                float[] fArr2 = new float[i4];
                if (i2 != 0) {
                    System.arraycopy(fArr, 0, fArr2, 0, i2);
                }
                while (i2 < i4) {
                    fArr2[i2] = codedInputByteBufferNano.readFloat();
                    i2++;
                }
                this.values = fArr2;
                codedInputByteBufferNano.currentLimit = pushLimit;
                int i5 = codedInputByteBufferNano.bufferSize + codedInputByteBufferNano.bufferSizeAfterLimit;
                codedInputByteBufferNano.bufferSize = i5;
                if (i5 > pushLimit) {
                    int i6 = i5 - pushLimit;
                    codedInputByteBufferNano.bufferSizeAfterLimit = i6;
                    codedInputByteBufferNano.bufferSize = i5 - i6;
                } else {
                    codedInputByteBufferNano.bufferSizeAfterLimit = 0;
                }
            } else if (readTag == 21) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 21);
                float[] fArr3 = this.values;
                if (fArr3 == null) {
                    i = 0;
                } else {
                    i = fArr3.length;
                }
                int i7 = repeatedFieldArrayLength + i;
                float[] fArr4 = new float[i7];
                if (i != 0) {
                    System.arraycopy(fArr3, 0, fArr4, 0, i);
                }
                while (i < i7 - 1) {
                    fArr4[i] = codedInputByteBufferNano.readFloat();
                    codedInputByteBufferNano.readTag();
                    i++;
                }
                fArr4[i] = codedInputByteBufferNano.readFloat();
                this.values = fArr4;
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
