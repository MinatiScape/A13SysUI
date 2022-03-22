package com.google.android.systemui.autorotate.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.InternalNano;
import com.google.protobuf.nano.MessageNano;
import com.google.protobuf.nano.WireFormatNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class AutorotateProto$DeviceRotatedSensorData extends MessageNano {
    public AutorotateProto$DeviceRotatedSensorHeader header = null;
    public AutorotateProto$DeviceRotatedSensorSample[] sample;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        int i;
        AutorotateProto$DeviceRotatedSensorHeader autorotateProto$DeviceRotatedSensorHeader = this.header;
        int i2 = 0;
        if (autorotateProto$DeviceRotatedSensorHeader != null) {
            i = CodedOutputByteBufferNano.computeMessageSize(1, autorotateProto$DeviceRotatedSensorHeader) + 0;
        } else {
            i = 0;
        }
        AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr = this.sample;
        if (autorotateProto$DeviceRotatedSensorSampleArr != null && autorotateProto$DeviceRotatedSensorSampleArr.length > 0) {
            while (true) {
                AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr2 = this.sample;
                if (i2 >= autorotateProto$DeviceRotatedSensorSampleArr2.length) {
                    break;
                }
                AutorotateProto$DeviceRotatedSensorSample autorotateProto$DeviceRotatedSensorSample = autorotateProto$DeviceRotatedSensorSampleArr2[i2];
                if (autorotateProto$DeviceRotatedSensorSample != null) {
                    i += CodedOutputByteBufferNano.computeMessageSize(2, autorotateProto$DeviceRotatedSensorSample);
                }
                i2++;
            }
        }
        return i;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        AutorotateProto$DeviceRotatedSensorHeader autorotateProto$DeviceRotatedSensorHeader = this.header;
        if (autorotateProto$DeviceRotatedSensorHeader != null) {
            codedOutputByteBufferNano.writeMessage(1, autorotateProto$DeviceRotatedSensorHeader);
        }
        AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr = this.sample;
        if (autorotateProto$DeviceRotatedSensorSampleArr != null && autorotateProto$DeviceRotatedSensorSampleArr.length > 0) {
            int i = 0;
            while (true) {
                AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr2 = this.sample;
                if (i < autorotateProto$DeviceRotatedSensorSampleArr2.length) {
                    AutorotateProto$DeviceRotatedSensorSample autorotateProto$DeviceRotatedSensorSample = autorotateProto$DeviceRotatedSensorSampleArr2[i];
                    if (autorotateProto$DeviceRotatedSensorSample != null) {
                        codedOutputByteBufferNano.writeMessage(2, autorotateProto$DeviceRotatedSensorSample);
                    }
                    i++;
                } else {
                    return;
                }
            }
        }
    }

    public AutorotateProto$DeviceRotatedSensorData() {
        if (AutorotateProto$DeviceRotatedSensorSample._emptyArray == null) {
            synchronized (InternalNano.LAZY_INIT_LOCK) {
                if (AutorotateProto$DeviceRotatedSensorSample._emptyArray == null) {
                    AutorotateProto$DeviceRotatedSensorSample._emptyArray = new AutorotateProto$DeviceRotatedSensorSample[0];
                }
            }
        }
        this.sample = AutorotateProto$DeviceRotatedSensorSample._emptyArray;
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        int i;
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 10) {
                if (this.header == null) {
                    this.header = new AutorotateProto$DeviceRotatedSensorHeader();
                }
                codedInputByteBufferNano.readMessage(this.header);
            } else if (readTag == 18) {
                int repeatedFieldArrayLength = WireFormatNano.getRepeatedFieldArrayLength(codedInputByteBufferNano, 18);
                AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr = this.sample;
                if (autorotateProto$DeviceRotatedSensorSampleArr == null) {
                    i = 0;
                } else {
                    i = autorotateProto$DeviceRotatedSensorSampleArr.length;
                }
                int i2 = repeatedFieldArrayLength + i;
                AutorotateProto$DeviceRotatedSensorSample[] autorotateProto$DeviceRotatedSensorSampleArr2 = new AutorotateProto$DeviceRotatedSensorSample[i2];
                if (i != 0) {
                    System.arraycopy(autorotateProto$DeviceRotatedSensorSampleArr, 0, autorotateProto$DeviceRotatedSensorSampleArr2, 0, i);
                }
                while (i < i2 - 1) {
                    autorotateProto$DeviceRotatedSensorSampleArr2[i] = new AutorotateProto$DeviceRotatedSensorSample();
                    codedInputByteBufferNano.readMessage(autorotateProto$DeviceRotatedSensorSampleArr2[i]);
                    codedInputByteBufferNano.readTag();
                    i++;
                }
                autorotateProto$DeviceRotatedSensorSampleArr2[i] = new AutorotateProto$DeviceRotatedSensorSample();
                codedInputByteBufferNano.readMessage(autorotateProto$DeviceRotatedSensorSampleArr2[i]);
                this.sample = autorotateProto$DeviceRotatedSensorSampleArr2;
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
