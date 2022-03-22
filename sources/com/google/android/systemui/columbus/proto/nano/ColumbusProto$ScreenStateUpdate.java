package com.google.android.systemui.columbus.proto.nano;

import com.google.protobuf.nano.CodedInputByteBufferNano;
import com.google.protobuf.nano.CodedOutputByteBufferNano;
import com.google.protobuf.nano.MessageNano;
import java.io.IOException;
/* loaded from: classes.dex */
public final class ColumbusProto$ScreenStateUpdate extends MessageNano {
    public int screenState = 0;

    @Override // com.google.protobuf.nano.MessageNano
    public final int computeSerializedSize() {
        int i = this.screenState;
        if (i != 0) {
            return 0 + CodedOutputByteBufferNano.computeInt32Size(1, i);
        }
        return 0;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final void writeTo(CodedOutputByteBufferNano codedOutputByteBufferNano) throws IOException {
        int i = this.screenState;
        if (i != 0) {
            codedOutputByteBufferNano.writeInt32(1, i);
        }
    }

    public ColumbusProto$ScreenStateUpdate() {
        this.cachedSize = -1;
    }

    @Override // com.google.protobuf.nano.MessageNano
    public final MessageNano mergeFrom(CodedInputByteBufferNano codedInputByteBufferNano) throws IOException {
        while (true) {
            int readTag = codedInputByteBufferNano.readTag();
            if (readTag == 0) {
                break;
            } else if (readTag == 8) {
                int readRawVarint32 = codedInputByteBufferNano.readRawVarint32();
                if (readRawVarint32 == 0 || readRawVarint32 == 1 || readRawVarint32 == 2) {
                    this.screenState = readRawVarint32;
                }
            } else if (!codedInputByteBufferNano.skipField(readTag)) {
                break;
            }
        }
        return this;
    }
}
