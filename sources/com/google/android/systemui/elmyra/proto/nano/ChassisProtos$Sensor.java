package com.google.android.systemui.elmyra.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ChassisProtos$Sensor extends MessageNano {
    public static volatile ChassisProtos$Sensor[] _emptyArray;
    public ElmyraFilters$Filter[] filters;
    public int source = 0;
    public int gain = 0;
    public float sensitivity = 0.0f;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        int i;
        int i2 = this.source;
        int i3 = 0;
        if (i2 != 0) {
            i = CodedOutputByteBufferNano.computeRawVarint32Size(i2) + CodedOutputByteBufferNano.computeTagSize(1) + 0;
        } else {
            i = 0;
        }
        int i4 = this.gain;
        if (i4 != 0) {
            i += CodedOutputByteBufferNano.computeInt32Size(2, i4);
        }
        if (Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f)) {
            i += CodedOutputByteBufferNano.computeFloatSize(3);
        }
        ElmyraFilters$Filter[] elmyraFilters$FilterArr = this.filters;
        if (elmyraFilters$FilterArr != null && elmyraFilters$FilterArr.length > 0) {
            while (true) {
                ElmyraFilters$Filter[] elmyraFilters$FilterArr2 = this.filters;
                if (i3 >= elmyraFilters$FilterArr2.length) {
                    break;
                }
                ElmyraFilters$Filter elmyraFilters$Filter = elmyraFilters$FilterArr2[i3];
                if (elmyraFilters$Filter != null) {
                    i += CodedOutputByteBufferNano.computeMessageSize(4, elmyraFilters$Filter);
                }
                i3++;
            }
        }
        return i;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.source;
        int i2 = 0;
        if (i != 0) {
            Objects.requireNonNull(codedOutputByteBufferNano);
            codedOutputByteBufferNano.writeTag(1, 0);
            codedOutputByteBufferNano.writeRawVarint32(i);
        }
        int i3 = this.gain;
        if (i3 != 0) {
            codedOutputByteBufferNano.writeInt32(2, i3);
        }
        if (Float.floatToIntBits(this.sensitivity) != Float.floatToIntBits(0.0f)) {
            codedOutputByteBufferNano.writeFloat(3, this.sensitivity);
        }
        ElmyraFilters$Filter[] elmyraFilters$FilterArr = this.filters;
        if (elmyraFilters$FilterArr != null && elmyraFilters$FilterArr.length > 0) {
            while (true) {
                ElmyraFilters$Filter[] elmyraFilters$FilterArr2 = this.filters;
                if (i2 < elmyraFilters$FilterArr2.length) {
                    ElmyraFilters$Filter elmyraFilters$Filter = elmyraFilters$FilterArr2[i2];
                    if (elmyraFilters$Filter != null) {
                        codedOutputByteBufferNano.writeMessage(4, elmyraFilters$Filter);
                    }
                    i2++;
                } else {
                    return;
                }
            }
        }
    }

    public ChassisProtos$Sensor() {
        if (ElmyraFilters$Filter._emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (ElmyraFilters$Filter._emptyArray == null) {
                    ElmyraFilters$Filter._emptyArray = new ElmyraFilters$Filter[0];
                }
            }
        }
        this.filters = ElmyraFilters$Filter._emptyArray;
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        int i;
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 8) {
                this.source = codedInputByteBufferNano.readRawVarint32();
            } else if (readTag == 16) {
                this.gain = codedInputByteBufferNano.readRawVarint32();
            } else if (readTag == 29) {
                this.sensitivity = codedInputByteBufferNano.readFloat();
            } else if (readTag == 34) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 34);
                ElmyraFilters$Filter[] elmyraFilters$FilterArr = this.filters;
                if (elmyraFilters$FilterArr == null) {
                    i = 0;
                } else {
                    i = elmyraFilters$FilterArr.length;
                }
                int i2 = repeatedFieldArrayLength + i;
                ElmyraFilters$Filter[] elmyraFilters$FilterArr2 = new ElmyraFilters$Filter[i2];
                if (i != 0) {
                    System.arraycopy(elmyraFilters$FilterArr, 0, elmyraFilters$FilterArr2, 0, i);
                }
                while (i < i2 - 1) {
                    elmyraFilters$FilterArr2[i] = new ElmyraFilters$Filter();
                    codedInputByteBufferNano.readMessage(elmyraFilters$FilterArr2[i]);
                    codedInputByteBufferNano.readTag();
                    i++;
                }
                elmyraFilters$FilterArr2[i] = new ElmyraFilters$Filter();
                codedInputByteBufferNano.readMessage(elmyraFilters$FilterArr2[i]);
                this.filters = elmyraFilters$FilterArr2;
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
