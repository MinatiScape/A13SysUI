package com.google.android.systemui.elmyra.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ElmyraFilters$Filter extends MessageNano {
    public static volatile ElmyraFilters$Filter[] _emptyArray;
    public int parametersCase_ = 0;
    public MessageNano parameters_ = null;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        int i = 0;
        if (this.parametersCase_ == 1) {
            i = 0 + CodedOutputByteBufferNano.computeMessageSize(1, this.parameters_);
        }
        if (this.parametersCase_ == 2) {
            i += CodedOutputByteBufferNano.computeMessageSize(2, this.parameters_);
        }
        if (this.parametersCase_ == 3) {
            i += CodedOutputByteBufferNano.computeMessageSize(3, this.parameters_);
        }
        if (this.parametersCase_ == 4) {
            return i + CodedOutputByteBufferNano.computeMessageSize(4, this.parameters_);
        }
        return i;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        if (this.parametersCase_ == 1) {
            codedOutputByteBufferNano.writeMessage(1, this.parameters_);
        }
        if (this.parametersCase_ == 2) {
            codedOutputByteBufferNano.writeMessage(2, this.parameters_);
        }
        if (this.parametersCase_ == 3) {
            codedOutputByteBufferNano.writeMessage(3, this.parameters_);
        }
        if (this.parametersCase_ == 4) {
            codedOutputByteBufferNano.writeMessage(4, this.parameters_);
        }
    }

    public ElmyraFilters$Filter() {
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 10) {
                if (this.parametersCase_ != 1) {
                    this.parameters_ = new MessageNano() { // from class: com.google.android.systemui.elmyra.proto.nano.ElmyraFilters$FIRFilter
                        public float[] coefficients = WireFormatNano.EMPTY_FLOAT_ARRAY;

                        @Override // com.google.protobuf.nano.MessageNano
                        public final int computeSerializedSize() {
                            float[] fArr = this.coefficients;
                            if (fArr == null || fArr.length <= 0) {
                                return 0;
                            }
                            return (fArr.length * 1) + (fArr.length * 4) + 0;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                            float[] fArr = this.coefficients;
                            if (fArr != null && fArr.length > 0) {
                                int i = 0;
                                while (true) {
                                    float[] fArr2 = this.coefficients;
                                    if (i < fArr2.length) {
                                        codedOutputByteBufferNano.writeFloat(1, fArr2[i]);
                                        i++;
                                    } else {
                                        return;
                                    }
                                }
                            }
                        }

                        {
                            this.cachedSize = -1;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano2) throws IOException {
                            int i;
                            int i2;
                            while (true) {
                                int readTag2 = codedInputByteBufferNano2.readTag();
                                if (readTag2 == 0) {
                                    break;
                                } else if (readTag2 == 10) {
                                    int readRawVarint32 = codedInputByteBufferNano2.readRawVarint32();
                                    int pushLimit = codedInputByteBufferNano2.pushLimit(readRawVarint32);
                                    int i3 = readRawVarint32 / 4;
                                    float[] fArr = this.coefficients;
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
                                        fArr2[i2] = codedInputByteBufferNano2.readFloat();
                                        i2++;
                                    }
                                    this.coefficients = fArr2;
                                    codedInputByteBufferNano2.currentLimit = pushLimit;
                                    int i5 = codedInputByteBufferNano2.bufferSize + codedInputByteBufferNano2.bufferSizeAfterLimit;
                                    codedInputByteBufferNano2.bufferSize = i5;
                                    if (i5 > pushLimit) {
                                        int i6 = i5 - pushLimit;
                                        codedInputByteBufferNano2.bufferSizeAfterLimit = i6;
                                        codedInputByteBufferNano2.bufferSize = i5 - i6;
                                    } else {
                                        codedInputByteBufferNano2.bufferSizeAfterLimit = 0;
                                    }
                                } else if (readTag2 == 13) {
                                    int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano2, 13);
                                    float[] fArr3 = this.coefficients;
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
                                        fArr4[i] = codedInputByteBufferNano2.readFloat();
                                        codedInputByteBufferNano2.readTag();
                                        i++;
                                    }
                                    fArr4[i] = codedInputByteBufferNano2.readFloat();
                                    this.coefficients = fArr4;
                                } else if (!codedInputByteBufferNano2.skipField(readTag2)) {
                                    break;
                                }
                            }
                            return this;
                        }
                    };
                }
                codedInputByteBufferNano.readMessage(this.parameters_);
                this.parametersCase_ = 1;
            } else if (readTag == 18) {
                if (this.parametersCase_ != 2) {
                    this.parameters_ = new MessageNano() { // from class: com.google.android.systemui.elmyra.proto.nano.ElmyraFilters$HighpassFilter
                        public float cutoff = 0.0f;
                        public float rate = 0.0f;

                        @Override // com.google.protobuf.nano.MessageNano
                        public final int computeSerializedSize() {
                            int i = 0;
                            if (Float.floatToIntBits(this.cutoff) != Float.floatToIntBits(0.0f)) {
                                i = 0 + CodedOutputByteBufferNano.computeFloatSize(1);
                            }
                            if (Float.floatToIntBits(this.rate) != Float.floatToIntBits(0.0f)) {
                                return i + CodedOutputByteBufferNano.computeFloatSize(2);
                            }
                            return i;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                            if (Float.floatToIntBits(this.cutoff) != Float.floatToIntBits(0.0f)) {
                                codedOutputByteBufferNano.writeFloat(1, this.cutoff);
                            }
                            if (Float.floatToIntBits(this.rate) != Float.floatToIntBits(0.0f)) {
                                codedOutputByteBufferNano.writeFloat(2, this.rate);
                            }
                        }

                        {
                            this.cachedSize = -1;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano2) throws IOException {
                            while (true) {
                                int readTag2 = codedInputByteBufferNano2.readTag();
                                if (readTag2 == 0) {
                                    break;
                                } else if (readTag2 == 13) {
                                    this.cutoff = codedInputByteBufferNano2.readFloat();
                                } else if (readTag2 == 21) {
                                    this.rate = codedInputByteBufferNano2.readFloat();
                                } else if (!codedInputByteBufferNano2.skipField(readTag2)) {
                                    break;
                                }
                            }
                            return this;
                        }
                    };
                }
                codedInputByteBufferNano.readMessage(this.parameters_);
                this.parametersCase_ = 2;
            } else if (readTag == 26) {
                if (this.parametersCase_ != 3) {
                    this.parameters_ = new MessageNano() { // from class: com.google.android.systemui.elmyra.proto.nano.ElmyraFilters$LowpassFilter
                        public float cutoff = 0.0f;
                        public float rate = 0.0f;

                        @Override // com.google.protobuf.nano.MessageNano
                        public final int computeSerializedSize() {
                            int i = 0;
                            if (Float.floatToIntBits(this.cutoff) != Float.floatToIntBits(0.0f)) {
                                i = 0 + CodedOutputByteBufferNano.computeFloatSize(1);
                            }
                            if (Float.floatToIntBits(this.rate) != Float.floatToIntBits(0.0f)) {
                                return i + CodedOutputByteBufferNano.computeFloatSize(2);
                            }
                            return i;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                            if (Float.floatToIntBits(this.cutoff) != Float.floatToIntBits(0.0f)) {
                                codedOutputByteBufferNano.writeFloat(1, this.cutoff);
                            }
                            if (Float.floatToIntBits(this.rate) != Float.floatToIntBits(0.0f)) {
                                codedOutputByteBufferNano.writeFloat(2, this.rate);
                            }
                        }

                        {
                            this.cachedSize = -1;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano2) throws IOException {
                            while (true) {
                                int readTag2 = codedInputByteBufferNano2.readTag();
                                if (readTag2 == 0) {
                                    break;
                                } else if (readTag2 == 13) {
                                    this.cutoff = codedInputByteBufferNano2.readFloat();
                                } else if (readTag2 == 21) {
                                    this.rate = codedInputByteBufferNano2.readFloat();
                                } else if (!codedInputByteBufferNano2.skipField(readTag2)) {
                                    break;
                                }
                            }
                            return this;
                        }
                    };
                }
                codedInputByteBufferNano.readMessage(this.parameters_);
                this.parametersCase_ = 3;
            } else if (readTag == 34) {
                if (this.parametersCase_ != 4) {
                    this.parameters_ = new MessageNano() { // from class: com.google.android.systemui.elmyra.proto.nano.ElmyraFilters$MedianFilter
                        public int windowSize = 0;

                        @Override // com.google.protobuf.nano.MessageNano
                        public final int computeSerializedSize() {
                            int i = this.windowSize;
                            if (i == 0) {
                                return 0;
                            }
                            return 0 + CodedOutputByteBufferNano.computeRawVarint32Size(i) + CodedOutputByteBufferNano.computeTagSize(1);
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
                            int i = this.windowSize;
                            if (i != 0) {
                                Objects.requireNonNull(codedOutputByteBufferNano);
                                codedOutputByteBufferNano.writeTag(1, 0);
                                codedOutputByteBufferNano.writeRawVarint32(i);
                            }
                        }

                        {
                            this.cachedSize = -1;
                        }

                        @Override // com.google.protobuf.nano.MessageNano
                        public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano2) throws IOException {
                            while (true) {
                                int readTag2 = codedInputByteBufferNano2.readTag();
                                if (readTag2 == 0) {
                                    break;
                                } else if (readTag2 == 8) {
                                    this.windowSize = codedInputByteBufferNano2.readRawVarint32();
                                } else if (!codedInputByteBufferNano2.skipField(readTag2)) {
                                    break;
                                }
                            }
                            return this;
                        }
                    };
                }
                codedInputByteBufferNano.readMessage(this.parameters_);
                this.parametersCase_ = 4;
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
