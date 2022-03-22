package com.google.android.systemui.autorotate.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class AutorotateProto$DeviceRotatedSensorHeader extends MessageNano {
    public long timestampBase = 0;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        long j = this.timestampBase;
        if (j != 0) {
            return 0 + CodedOutputByteBufferNano.computeInt64Size(1, j);
        }
        return 0;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        long j = this.timestampBase;
        if (j != 0) {
            codedOutputByteBufferNano.writeInt64(1, j);
        }
    }

    public AutorotateProto$DeviceRotatedSensorHeader() {
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 8) {
                this.timestampBase = codedInputByteBufferNano.readRawVarint64();
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
