package com.google.android.systemui.columbus.proto.nano;

import com.android.systemui.plugins.FalsingManager;
import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ColumbusProto$NanoappEvent extends MessageNano {
    public static volatile ColumbusProto$NanoappEvent[] _emptyArray;
    public long timestamp = 0;
    public int type = 0;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        long j = this.timestamp;
        int i = 0;
        if (j != 0) {
            i = 0 + CodedOutputByteBufferNano.computeRawVarint64Size(j) + CodedOutputByteBufferNano.computeTagSize(1);
        }
        int i2 = this.type;
        if (i2 != 0) {
            return i + CodedOutputByteBufferNano.computeInt32Size(2, i2);
        }
        return i;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        long j = this.timestamp;
        if (j != 0) {
            Objects.requireNonNull(codedOutputByteBufferNano);
            codedOutputByteBufferNano.writeTag(1, 0);
            codedOutputByteBufferNano.writeRawVarint64(j);
        }
        int i = this.type;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(2, i);
        }
    }

    public ColumbusProto$NanoappEvent() {
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag != 0) {
                if (readTag == 8) {
                    this.timestamp = codedInputByteBufferNano.readRawVarint64();
                } else if (readTag == 16) {
                    int readRawVarint32 = codedInputByteBufferNano.readRawVarint32();
                    switch (readRawVarint32) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                        case 4:
                        case 5:
                        case FalsingManager.VERSION /* 6 */:
                        case 7:
                        case 8:
                            this.type = readRawVarint32;
                            continue;
                    }
                } else if (!codedInputByteBufferNano.skipField(readTag)) {
                }
            }
        }
        return this;
    }
}
